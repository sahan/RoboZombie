package com.lonepulse.robozombie.response;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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
import com.lonepulse.robozombie.proxy.InvocationContext;
import com.lonepulse.robozombie.util.Metadata;

/**
 * <p>This {@link AbstractResponseProcessor} retrieves the <i>response headers</i> from an 
 * {@link HttpResponse} and exposes them via <b>in-out</b> parameters on the endpoint request definition. 
 * It targets <b>dynamic response headers</b> which are identified by the @{@link Header} annotation 
 * used on parameters of type {@link StringBuilder}.</p>
 * 
 * <p><b>Note</b> that the endpoint may return multiple response headers with the same name and each of 
 * these may be retrieved by annotating multiple parameters with @{@link Header}. An alternative would be 
 * to implement an {@link AbstractDeserializer} and processing the headers manually. This could also be 
 * achieved by running the request <i>asynchronously</i> and processing the headers in an {@link AsyncHandler}).</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
class HeaderProcessor extends AbstractResponseProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} along with the {@link HttpResponse} and retrieves all response 
	 * headers which are discovered in the {@link HttpResponse}. These are then injected into their matching 
	 * {@link StringBuilder} which are identified by @{@link Header} on the endpoint request definition. The 
	 * response headers and the in-out parameters are matched using the header name and all parameters with 
	 * a runtime value of {@code null} will be ignored.</p> 
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which is used to discover any @{@link Header} metadata in its 
	 * 			<i>request</i> and <i>args</i>
	 * <br><br>
	 * @param response
	 * 			the {@link HttpResponse} whose headers are to be retrieved and injected in the in-out 
	 * 			{@link StringBuilder} parameters found on the request definition
	 * <br><br>
	 * @return the <i>same</i> deserialized response entity instance which was supplied as a parameter 
	 * <br><br>
	 * @throws ResponseProcessorException
	 * 			if the response-header retrieval or injection failed due to an unrecoverable error
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	protected Object process(InvocationContext context, HttpResponse response, Object content) {

		try {
			
			List<Map.Entry<Header, Object>> headers = Metadata.onParams(Header.class, context);
			
			String name;
			StringBuilder value;
			
			for (Map.Entry<Header, Object> header : headers) {
				
				if(header.getValue() instanceof StringBuilder) {
					
					name = header.getKey().value();
					value = (StringBuilder)header.getValue();
					
					org.apache.http.Header[] responseHeaders = response.getHeaders(name);
					
					if(responseHeaders != null && responseHeaders.length > 0) {
					
						String responseHeaderValue = responseHeaders[0].getValue();
						value.replace(0, value.length(), responseHeaderValue == null? "" :responseHeaderValue);
						
						response.removeHeader(responseHeaders[0]); //remaining headers (equally named) processed if in-out params available
					}
				}
			}
			
			return content;
		}
		catch(Exception e) {
			
			throw new ResponseProcessorException(getClass(), context, e);
		}
	}
}
