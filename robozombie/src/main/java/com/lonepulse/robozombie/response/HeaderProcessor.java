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


import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.annotation.Header;
import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This is a concrete implementation of {@link ResponseProcessor} which retrieves the <i>response-headers</i> 
 * from an {@link HttpResponse} and exposes them via <b>in-out</b> parameters on the endpoint request request 
 * definition. It targets <b>dynamic response headers</b> which are identified by the @{@link Header} annotation 
 * used on parameters of type {@link StringBuilder}.</p>
 * 
 * <p><b>Note</b> that the endpoint may return multiple response headers with the same name and each of these may 
 * be retrieved by annotating multiple parameters with @{@link Header}. An alternative would be to implement an 
 * {@link AbstractResponseParser} and processing the headers manually (this could also be achieved by running the 
 * request <i>asynchronously</i> and processing the headers in an {@link AsyncHandler}).</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class HeaderProcessor extends AbstractResponseProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} along with the {@link HttpResponse} plus the parsed entity 
	 * response (if any) and retrieves all HTTP response headers which are discovered in the {@link HttpResponse}. 
	 * These are then injected into their matching {@link StringBuilder} which are identified by @{@link Header} on 
	 * the endpoint request definition. The HTTP response headers and the in-out parameters are matched using the header 
	 * name and all parameters with a runtime value of {@code null} will be ignored.</p> 
	 * 
	 * <p>See {@link ResponseUtils#findHeaders(InvocationContext)}.</p>
	 * 
	 * @param httpResponse
	 * 			the instance of {@link HttpResponse} whose headers are to be retrieves and injected in the in-out 
	 * 			{@link StringBuilder} parameters found on the request definition
	 * <br><br>
	 * @param config
	 * 			an immutable instance of {@link InvocationContext} which is used to discover any 
	 * 			@{@link Header} metadata in its <i>request</i> and <i>args</i>
	 * <br><br>
	 * @return the <i>same</i> parsed response entity instance which was supplied as a parameter 
	 * <br><br>
	 * @throws ResponseProcessorException
	 * 			if the response-header retrieval or injection failed due to an unrecoverable error
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	protected Object process(HttpResponse httpResponse, InvocationContext config, Object parsedResponse)
	throws ResponseProcessorException {

		try {
			
			if(httpResponse != null) {
			
				List<Map.Entry<String, Object>> headers = ResponseUtils.findHeaders(config);
				
				String name;
				StringBuilder value;
				
				for (Map.Entry<String, Object> header : headers) {
					
					if(header.getValue() instanceof StringBuilder) {
						
						name = header.getKey();
						value = (StringBuilder)header.getValue();
						
						if(value == null || value.equals("")) {
							
							continue; //skip headers which are omitted for the current invocation
						}
						
						org.apache.http.Header[] responseHeaders = httpResponse.getHeaders(name);
						
						if(responseHeaders != null && responseHeaders.length > 0) {
						
							String responseHeaderValue = responseHeaders[0].getValue();
							value.replace(0, value.length(), responseHeaderValue == null? "" :responseHeaderValue);
							
							httpResponse.removeHeader(responseHeaders[0]); //remaining headers (equally named) processed if in-out params available
						}
					}
				}
			}
			
			return parsedResponse;
		}
		catch(Exception e) {
			
			throw (e instanceof ResponseProcessorException)? 
					(ResponseProcessorException)e :new ResponseProcessorException(getClass(), config, e);
		}
	}
}
