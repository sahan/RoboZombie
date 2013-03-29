package com.lonepulse.robozombie.core.response.parser;

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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.lonepulse.robozombie.core.annotation.Header;
import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.util.AnnotationExtractor;

/**
 * <p>This is an implementation of {@link ResponseParser} which defines and executes the 
 * steps in <i>parsing</i>.</p>
 * 
 * <p>User defined {@link ResponseParser}s must extend this class and override the 
 * {@link AbstractResponseParser#processResponse(HttpResponse)} and {@link AbstractResponseParser#getType()} 
 * methods.</p>
 * <br><br>
 * <ul>
 *  <li>
 *  <b>{@link AbstractResponseParser#processResponse(HttpResponse)}</b><br>
 *  Retrieves the necessary information from {@link HttpResponse} and returns an instance of 
 *  a custom {@link ResponseParser}.
 *  <br><br><b>
 *  Sample Code from {@link StringResponseParser}:<br><br></b>
 *  <font color="#2E2E2E">
 *  <code>
 *  String responseString = EntityUtils.toString(httpResponse.getEntity());
 *	return responseString.subSequence(0, responseString.length());
 *  </code>
 *  </font><br><br>
 *  <p>The request's return type can be obtained by calling {@link #getRequestReturnType()}. 
 *  This may be used within the {@link #processResponse(HttpResponse)} as necessary.</p> 
 *  </li>
 *  <br><br><br>
 *  <li>
 *  <b>{@link AbstractResponseParser#getType()}</b><br>
 *  Returns the {@link Class} of the type handled by the custom {@link ResponseParser}. 
 *  <br><br><b>
 *  Sample Code from {@link StringResponseParser}:<br><br></b>
 *  <font color="#2E2E2E">
 *  <code>
 *  return CharSequence.class;
 *  </code>
 *  </font>
 *  </li>
 * </ul> 
 * <br>
 * 
 * @version 1.1.6
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public abstract class AbstractResponseParser<T> implements ResponseParser<T> {
	

	/**
	 * <p>The {@link Class} of the desired request return type.</p>
	 * <br><br> 
	 * @since 1.1.5
	 */
	protected Class<? extends Object> requestReturnType = null; 
	
	
	/**
	 * <p>Accessor for {@link #requestReturnType}.
	 * 
	 * @return {@link #requestReturnType}
	 * <br><br>
	 * @since 1.1.5
	 */
	protected Class<? extends Object> getRequestReturnType() {

		return requestReturnType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T parse(HttpResponse httpResponse, ProxyInvocationConfiguration config) {
		
		this.requestReturnType = config.getRequest().getReturnType();
		
		throwIfNotAssignable(requestReturnType);
		
		try {
			
			processHeaders(httpResponse, config);
			return processResponse(httpResponse);
		}
		catch(Exception e) {
		
			throw new ResponseParserException(e);
		}
		finally {
			
			try {
			
				EntityUtils.toString(httpResponse.getEntity());
			}
			catch (Exception e) {} //consume quietly
		}
	}
	
	/**
	 * <p>Checks if the desired request return type can be instantiated from 
	 * an instance of the parser's return type.</p>
	 * 
	 * @param requestReturnType
	 * 				the {@link Class} of the request return type
	 */
	private void throwIfNotAssignable(Class<? extends Object> requestReturnType) {
		
		if(!getType().isAssignableFrom(requestReturnType))   
			throw new ResponseParserNotAssignableException(getType(), requestReturnType);
	}
	
	/**
	 * <p>Processes the variable headers returned from the {@link HttpResponse} and assigns 
	 * them back to back request variables so that the caller may gain access to them.</p>
	 * 
	 * @param httpResponse
	 * 			the {@link HttpResponse} which was received after request execution
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} which supplies the header param 
	 * 			annotations
	 */
	private void processHeaders(HttpResponse httpResponse, ProxyInvocationConfiguration config) {
		
		Object[] requestArgs = config.getRequestArgs();
		
		Map<StringBuilder, Header> variableHeaderParams = AnnotationExtractor.extractHeaders(config.getRequest(), requestArgs);
		Set<Map.Entry<StringBuilder, Header>> variableEntries = variableHeaderParams.entrySet();

		List<org.apache.http.Header> headersList = Arrays.asList(httpResponse.getAllHeaders());
		Map<String, String> responseHeaders = new HashMap<String, String>();
		
		for (org.apache.http.Header header : headersList)
			responseHeaders.put(header.getName(), header.getValue());
		
		for (Map.Entry<StringBuilder, Header> entry : variableEntries) {
			
			String responseHeaderValue = responseHeaders.get(entry.getValue().value());
			
			for(int i = 0; i < requestArgs.length; i++) {
				
				Object object = requestArgs[i];
						
				if(object instanceof StringBuilder) {

					StringBuilder arg = ((StringBuilder)object);
					StringBuilder argBeforeRequest = (StringBuilder)entry.getKey();
					
					if(arg.toString().equals(argBeforeRequest.toString()) 
						&& (responseHeaderValue != null)
						&& !responseHeaderValue.equals("")) {
						
						arg.replace(0, arg.length(), responseHeaderValue);
					}
				}
			}
		}
	}
	
	/**
	 * <p>Takes in the {@link HttpResponse} returned from the request execution 
	 * and parses the content within the response into and instance of the 
	 * specified type.</p>
	 * 
	 * @param httpResponse
	 * 				the {@link HttpResponse} from which the content is extracted
     * <br><br>
	 * @return the entity which is created after parsing the output
	 * <br><br>
	 * @throws Exception 
	 * 				Parsing failures may occur due to many reasons
	 * <br><br>
	 * @since 1.1.4
	 */
	protected abstract T processResponse(HttpResponse httpResponse) throws Exception;
	
	/**
	 * <p>Allows any {@link ResponseParser} extension to determine the type 
	 * {@link Class} of the instantiated {@link ResponseParser}.</p>
	 * 
	 * @return the type {@link Class} of the instantiated {@link ResponseParser}
	 * <br><br>
	 * @since 1.1.4
	 */
	protected abstract Class<T> getType();
}
