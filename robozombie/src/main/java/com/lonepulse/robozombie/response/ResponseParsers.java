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


import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.annotation.Parser.ParserType;
import com.lonepulse.robozombie.inject.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.util.Resolver;

/**
 * <p>Exposes all available {@link ResponseParser}s and delegates communication. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
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
	

	/**
	 * <p>The exposed instance of {@link AbstractResponseParser}.
	 */
	private AbstractResponseParser<? extends Object> responseParser;
	
	
	/**
	 * <p>Instantiates {@link #responseParser} with the give instance of 
	 * {@link ResponseParser}.
	 * 
	 * @param responseParser
	 * 			the associated instance of {@link ResponseParser}
	 */
	private ResponseParsers(AbstractResponseParser<? extends Object> responseParser) {
	
		this.responseParser = responseParser;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object parse(HttpResponse httpResponse, ProxyInvocationConfiguration config) {
		
		return this.responseParser.parse(httpResponse, config);
	}
	
	
	/**
	 * <p>The {@link Resolver} which retrieves the associated {@link ResponseParser} for the 
	 * a given {@link ParserType}.
	 * 
	 * @version 1.1.0
	 * <br><br>
	 * @since 1.2.4
	 */
	public static final Resolver<ParserType, ResponseParser<? extends Object>> RESOLVER 
		= new Resolver<ParserType, ResponseParser<? extends Object>>() {
	
		/**
		 * <p>Retrieves the {@link ResponseParser} which is defined for the given {@link ParserType}.
		 * 
		 * @param config
		 * 			the {@link ParserType} for which the associated implementation of {@link ResponseParser} 
		 * 			is retrieved
		 * 
		 * @return the implementation of {@link ResponseParser} which serves the given {@link ParserType}
		 * 
		 * <br><br>
		 * @since 1.2.4
		 */
		@Override
		public ResponseParser<? extends Object> resolve(ParserType parserType) {
	
			switch (parserType) {
			
				case JSON:
					return ResponseParsers.JSON.responseParser;
						
				case XML:
					return ResponseParsers.XML.responseParser;
					
				case RAW: case UNDEFINED: default:
					return ResponseParsers.RAW.responseParser;
			}
		}
	};
}
