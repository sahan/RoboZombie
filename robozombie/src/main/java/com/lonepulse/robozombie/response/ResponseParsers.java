package com.lonepulse.robozombie.response;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 Lonepulse
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.annotation.Parser.ParserType;
import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>Exposes all available {@link ResponseParser}s, resolves concrete instances of their parser types 
 * and mediates communication.</p> 
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum ResponseParsers implements ResponseParser<Object> {

	/**
	 * See {@link RawResponseParser}.
	 * 
	 * @since 1.1.0
	 */
	RAW(new RawResponseParser()),
	
	/**
	 * See {@link JsonResponseParser}.
	 * 
	 * @since 1.1.0
	 */
	JSON(new JsonResponseParser()),
	
	/**
	 * See {@link XmlResponseParser}.
	 * 
	 * @since 1.2.4
	 */
	XML(new XmlResponseParser());
	

	private static final Map<String, ResponseParser<?>> PARSERS = new HashMap<String, ResponseParser<?>>();
	
	private final ResponseParser<?> responseParser;
	
	
	private ResponseParsers(ResponseParser<?> responseParser) {
	
		this.responseParser = responseParser;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object parse(HttpResponse httpResponse, InvocationContext context) {
		
		return this.responseParser.parse(httpResponse, context);
	}
	
	/**
	 * <p>Retrieves the {@link ResponseParser} which is identified by the given {@link ParserType}.</p>
	 * 
	 * @param config
	 * 			the {@link ParserType} whose implementation of {@link ResponseParser} is retrieved
	 * 
	 * @return the implementation of {@link ResponseParser} which serves the given {@link ParserType}
	 * <br><br>
	 * @since 1.2.4
	 */
	public static final ResponseParser<?> resolve(ParserType parserType) {
		
		switch (parserType) {
		
			case JSON:
				return ResponseParsers.JSON.responseParser;
					
			case XML:
				return ResponseParsers.XML.responseParser;
				
			case RAW: case UNDEFINED: default:
				return ResponseParsers.RAW.responseParser;
		}
	}
	
	/**
	 * <p>Retrieves the {@link ResponseParser} which is defined for the given {@link Class}.</p>
	 * 
	 * @param parserType
	 * 			the {@link Class} whose implementation of {@link ResponseParser} is retrieved
	 * 
	 * @return the implementation of {@link ResponseParser} for the given {@link Class}
	 * <br><br>
	 * @throws ResponseParserInstantiationException
	 * 			if a custom response parser failed to be instantiated using its <b>default constructor</b> 
	 * <br><br>
	 * @since 1.2.4
	 */
	public static final ResponseParser<?> resolve(Class<? extends ResponseParser<?>> parserType) {
		
		try {
			
			synchronized(PARSERS) {
				
				String key = parserType.getName();
				
				ResponseParser<?> parser = PARSERS.get(key);
				
				if(parser == null) {
					
					parser = parserType.newInstance();
					PARSERS.put(key, parser);
				}
				
				return parser;
			}
		}
		catch(Exception e) {
			
			throw new ResponseParserInstantiationException(parserType);
		}
	}
}
