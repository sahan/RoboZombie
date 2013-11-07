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

import com.lonepulse.robozombie.ContentType;
import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>Exposes all available {@link AbstractDeserializer}s, resolves concrete instances of their parser types 
 * and mediates communication.</p> 
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum Deserializers implements Deserializer<Object> {

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
	

	
	private static final Map<String, AbstractDeserializer<?>> PARSERS = new HashMap<String, AbstractDeserializer<?>>();
	
	private final AbstractDeserializer<?> deserializer;
	
	
	private Deserializers(AbstractDeserializer<?> deserializer) {
	
		this.deserializer = deserializer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object run(HttpResponse httpResponse, InvocationContext context) {
		
		return this.deserializer.run(httpResponse, context);
	}
	
	/**
	 * <p>Retrieves the {@link AbstractDeserializer} which is identified by the given {@link ContentType}.</p>
	 * 
	 * @param config
	 * 			the {@link ContentType} whose implementation of {@link AbstractDeserializer} is retrieved
	 * 
	 * @return the implementation of {@link AbstractDeserializer} which serves the given {@link ContentType}
	 * <br><br>
	 * @since 1.2.4
	 */
	public static final AbstractDeserializer<?> resolve(ContentType parserType) {
		
		switch (parserType) {
		
			case JSON:
				return Deserializers.JSON.deserializer;
					
			case XML:
				return Deserializers.XML.deserializer;
				
			case PLAIN: case UNDEFINED: default:
				return Deserializers.RAW.deserializer;
		}
	}
	
	/**
	 * <p>Retrieves the {@link AbstractDeserializer} which is defined for the given {@link Class}.</p>
	 * 
	 * @param parserType
	 * 			the {@link Class} whose implementation of {@link AbstractDeserializer} is retrieved
	 * 
	 * @return the implementation of {@link AbstractDeserializer} for the given {@link Class}
	 * <br><br>
	 * @throws ResponseParserInstantiationException
	 * 			if a custom response parser failed to be instantiated using its <b>default constructor</b> 
	 * <br><br>
	 * @since 1.2.4
	 */
	public static final AbstractDeserializer<?> resolve(Class<? extends AbstractDeserializer<?>> parserType) {
		
		try {
			
			synchronized(PARSERS) {
				
				String key = parserType.getName();
				
				AbstractDeserializer<?> parser = PARSERS.get(key);
				
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
