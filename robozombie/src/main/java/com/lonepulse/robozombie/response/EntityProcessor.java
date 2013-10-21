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


import java.lang.reflect.Method;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http42.util.EntityUtils;

import com.lonepulse.robozombie.annotation.Asynchronous;
import com.lonepulse.robozombie.annotation.Header;
import com.lonepulse.robozombie.annotation.Parser;
import com.lonepulse.robozombie.annotation.Parser.ParserType;
import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This is a concrete implementation of {@link ResponseProcessor} which retrieves the {@link HttpEntity} 
 * of an {@link HttpResponse} and parses it using the defined {@link ParserType}. {@link ParserType}s are defined 
 * using @{@link Parser} either at the endpoint level or at the request level. All endpoint request declarations 
 * which defined a return type should be associated with a response parser. Custom response parsers may be used 
 * by extending {@link AbstractResponseParser} and defining its type on {@link Parser#type()}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class EntityProcessor extends AbstractResponseProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} along with the {@link HttpResponse} plus the results-map 
	 * and retrieves the {@link HttpEntity} form the response. This is then fed all HTTP response headers which 
	 * are discovered in the {@link HttpResponse}. These are then injected into their matching {@link StringBuilder} 
	 * which are identified by @{@link Header} on the endpoint request definition. The HTTP response headers and the 
	 * in-out parameters are matched using the header name and all parameters with a runtime value of {@code null} 
	 * will be ignored.</p> 
	 * 
	 * @param httpResponse
	 * 			the instance of {@link HttpResponse} whose headers are to be retrieves and injected in the in-out 
	 * 			{@link StringBuilder} parameters found on the request definition
	 * <br><br>
	 * @param config
	 * 			an immutable instance of {@link InvocationContext} which is used to discover any 
	 * 			@{@link Header} metadata in its <i>request</i> and <i>args</i> 
	 * <br><br>
	 * @throws ResponseProcessorException
	 * 			if the response-header retrieval or injection failed due to an unrecoverable error
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	protected Object process(HttpResponse httpResponse, InvocationContext config, Object parsedResponse)
	throws ResponseProcessorException {

		HttpEntity httpEntity = httpResponse != null? httpResponse.getEntity() :null;
		
		if(httpEntity != null) {
			
			try {
				
				Method request = config.getRequest();
				Class<?> responseType = request.getReturnType();
				
				boolean handleAsync = (config.getEndpoint().isAnnotationPresent(Asynchronous.class) 
									   || request.isAnnotationPresent(Asynchronous.class));
				
				boolean responseExpected = !responseType.equals(void.class) && !responseType.equals(Void.class); 
				
				if(handleAsync || responseExpected) {
					
					Class<?> endpoint = config.getEndpoint();
					ResponseParser<?> responseParser = null;
			
					Parser parser = null;
					
					if(request.isAnnotationPresent(Parser.class)) {
						
						parser = request.getAnnotation(Parser.class);
					}
					else if(endpoint.isAnnotationPresent(Parser.class)) {
						
						parser = endpoint.getAnnotation(Parser.class);
					}
					else if(handleAsync || CharSequence.class.isAssignableFrom(responseType)) {
						
						responseParser = ResponseParsers.RESOLVER.resolve(ParserType.RAW);
					}
					else {
						
						throw new ResponseParserUndefinedException(endpoint, request);
					}
					
					if(parser != null) {
						
						if(parser.value() == ParserType.UNDEFINED) {
							
							responseParser = ResponseParser.class.cast(parser.type().newInstance()); 
						}
						else {
							
							responseParser = ResponseParsers.RESOLVER.resolve(parser.value()); 
						}
					}
					
					return responseParser.parse(httpResponse, config);
				}
			}
			catch(Exception e) {
				
				throw (e instanceof ResponseProcessorException)? 
						(ResponseProcessorException)e :new ResponseProcessorException(getClass(), config, e);
			}
			finally {
				
				EntityUtils.consumeQuietly(httpEntity);
			}
		}
		
		return parsedResponse;
	}
}
