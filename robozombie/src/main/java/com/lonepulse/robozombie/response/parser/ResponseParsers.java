package com.lonepulse.robozombie.response.parser;

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


import java.lang.reflect.Method;

import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.RoboZombieIllegalAccessException;
import com.lonepulse.robozombie.RoboZombieInstantiationException;
import com.lonepulse.robozombie.annotation.Parser;
import com.lonepulse.robozombie.processor.ProxyInvocationConfiguration;
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
	 * See {@link StringResponseParser}.
	 * 
	 * @since 1.1.0
	 */
	STRING(new StringResponseParser()),
	
	/**
	 * See {@link ObjectResponseParser}.
	 * 
	 * @since 1.1.0
	 */
	OBJECT(new ObjectResponseParser()),
	
	/**
	 * See {@link JsonResponseParser}.
	 * 
	 * @since 1.1.0
	 */
	JSON(new JsonResponseParser());
	

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
	 * <p>The instance of {@link Resolver} which retrieves suitable {@link ResponseParser}s 
	 * for a given endpoint request.
	 * 
	 * @since 1.1.0
	 */
	public static final Resolver<ProxyInvocationConfiguration, ResponseParser<? extends Object>> RESOLVER 
		= new Resolver<ProxyInvocationConfiguration, ResponseParser<? extends Object>>() {
	
		/**
		 * <p>Takes an endpoint {@link Method} and discovers a suitable {@link ResponseParser}.
		 * 
		 * @param config
		 * 			the {@link ProxyInvocationConfiguration} for resolving the 
		 * 			associated {@link ResponseParser}
		 * 
		 * @return the resolved {@link ResponseParser} or {@link StringResponseParser} 
		 * 		   if the resolution cannot be solved 
		 * 
		 * <br><br>
		 * @since 1.1.0
		 */
		@Override
		public ResponseParser<? extends Object> resolve(ProxyInvocationConfiguration config) {
	
			Class<?> typeClass = config.getEndpointClass();
			Method method = config.getRequest();
			
			if(!method.getReturnType().equals(Void.class)) { //i.e. a response is expected
		
				Parser parser = null;
				
				if(method.isAnnotationPresent(Parser.class)) //check parser definition at method level first
					parser = method.getAnnotation(Parser.class);
				
				else if(typeClass.isAnnotationPresent(Parser.class))
					parser = typeClass.getAnnotation(Parser.class);
					
				else
					throw new ResponseParserUndefinedException(typeClass, method);
				
				switch (parser.value()) {
				
					case STRING:
						return ResponseParsers.STRING.responseParser;
						
					case JSON:
						return ResponseParsers.JSON.responseParser;
							
					case OBJECT:
						return ResponseParsers.OBJECT.responseParser;
							
					case UNDEFINED: {
						
						try {
							
							ResponseParser<?> responseParser 
								= ResponseParser.class.cast(parser.type().newInstance());
							
							return responseParser;
						} 
						catch (InstantiationException ie) {
						
							throw new RoboZombieInstantiationException(parser.type(), ie);
						} 
						catch (IllegalAccessException iae) {
							
							throw new RoboZombieIllegalAccessException(parser.type(), iae);
						}
					}
				}
			}
			
			return ResponseParsers.STRING.responseParser;
		}
	};
}
