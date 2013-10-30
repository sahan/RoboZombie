package com.lonepulse.robozombie.request;

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


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http42.HttpHeaders;
import org.apache.http42.entity.ContentType;

import com.lonepulse.robozombie.annotation.FormParam;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This is a concrete implementation of {@link RequestProcessor} which discovers <i>form parameters</i> 
 * in a request declaration by searching for any arguments which are annotated with @{@link FormParam} and 
 * constructs a list of <a href="http://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms">
 * form-urlencoded</a> <b>name-value</b> pairs which will be sent in the request body.</p> 
 * 
 * <p>The @{@link FormParam} annotation should be used on an implementation of {@link CharSequence} which 
 * provides the <i>value</i> for each <i>name-value</i> pair; and the supplied {@link FormParam#value()} 
 * provides the <i>name</i>.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class FormParamProcessor extends AbstractRequestProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} along with an {@link HttpEntityEnclosingRequestBase} and 
	 * creates a list of <a href="http://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms">
	 * form-urlencoded</a> <b>name-value</b> pairs using any arguments which were annotated with @{@link FormParam}. 
	 * This is then inserted to the request body of the given {@link HttpEntityEnclosingRequestBase}.</p>
	 * 
	 * <p><b>Note</b> that any {@link HttpRequestBase}s which does not extend {@link HttpEntityEnclosingRequestBase} 
	 * will be ignored.</p>
	 * 
	 * <p><b>Note</b> that any constant request parameters which are annotated with @{@link Request.Param} will be 
	 * treated as <b>name-value</b> pairs to used as <b>form-urlencoded</b> params.</p>
	 * 
	 * <p>See {@link RequestProcessor#process(HttpRequestBase, InvocationContext)}.</p>
	 * 
	 * @param httpRequestBase
	 * 			prefers an instance of {@link HttpPost} so as to conform with HTTP 1.1; however, other  
	 * 			{@link HttpEntityEnclosingRequestBase}s will be entertained to allow compliance with unusual 
	 * 			endpoint definitions (as long as they are {@link HttpEntityEnclosingRequestBase}s) 
	 * <br><br>
	 * @param config
	 * 			an immutable instance of {@link InvocationContext} which is used to form the query 
	 * 			string and create the {@link HttpGet} request
	 * <br><br>
 	 * @return the same instance of {@link HttpRequestBase} which was given for processing form params 
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if an {@link HttpGet} instance failed to be created or if a query parameter failed to be inserted
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	protected HttpRequestBase process(HttpRequestBase httpRequestBase, InvocationContext config) throws RequestProcessorException {

		try {
			
			if(httpRequestBase instanceof HttpEntityEnclosingRequestBase) {
				
				List<NameValuePair> nameValuePairs = new LinkedList<NameValuePair>();
				
				List<Request.Param> constantFormParams = RequestUtils.findConstantRequestParams(config);
				Map<String, Object> formParams = RequestUtils.findFormParams(config);
				
				for (Request.Param param : constantFormParams) {
					
					nameValuePairs.add(new BasicNameValuePair(param.name(), param.value()));
				}
				
				for (Entry<String, Object> entry : formParams.entrySet()) {
					
					String name = entry.getKey();
					Object value = entry.getValue();
					
					if(!(value instanceof CharSequence)) {
						
						StringBuilder errorContext = new StringBuilder()
						.append("Form (url-encoded) parameters can only be of type ")
						.append(CharSequence.class.getName())
						.append(". Please consider implementing CharSequence ")
						.append("and providing a meaningful toString() representation for the ")
						.append("<name> of the form parameter. ");
						
						throw new RequestProcessorException(new IllegalArgumentException(errorContext.toString()));
					}
					
					nameValuePairs.add(new BasicNameValuePair(name, String.valueOf(value)));
				}
				
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs);
				urlEncodedFormEntity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
	
				httpRequestBase.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
				((HttpEntityEnclosingRequestBase)httpRequestBase).setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}
			
			return httpRequestBase;
		}
		catch(Exception e) {
			
			throw (e instanceof RequestProcessorException)? 
					(RequestProcessorException)e :new RequestProcessorException(getClass(), config, e);
		}
	}
}