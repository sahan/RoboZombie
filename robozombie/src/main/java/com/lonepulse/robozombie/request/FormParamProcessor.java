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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http42.HttpHeaders;
import org.apache.http42.entity.ContentType;

import com.lonepulse.robozombie.annotation.FormParam;
import com.lonepulse.robozombie.annotation.FormParams;
import com.lonepulse.robozombie.annotation.Param;
import com.lonepulse.robozombie.proxy.InvocationContext;
import com.lonepulse.robozombie.util.Metadata;
/**
 * <p>This is a concrete implementation of {@link AbstractRequestProcessor} which discovers <b>form 
 * parameters</b> in a request which are annotated with @{@link FormParam} or @{@link FormParams} and 
 * constructs a a list of <a href="http://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms"> 
 * form-urlencoded</a> <b>name-value</b> pairs which will be sent in the request body.</p> 
 * 
 * <p>The @{@link FormParam} annotation should be used on an implementation of {@link CharSequence} which 
 * provides the <i>value</i> for each <i>name-value</i> pair; and the supplied {@link FormParam#value()} 
 * provides the <i>name</i>.</p>
 * 
 * <p>The @{@link FormParams} annotation should be used on a {@code Map<CharSequence, CharSequence>} of 
 * name and value pairs.</p>
 * 
 * @version 1.3.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class FormParamProcessor extends AbstractRequestProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} with an {@link HttpEntityEnclosingRequestBase} and 
	 * creates a list of <a href="http://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms">
	 * form-urlencoded</a> name-value pairs using arguments annotated with @{@link FormParam} and 
	 * @{@link FormParams}. It's then inserted to the body of the request being processed.</p>
	 * 
	 * <p><b>Note</b> that any {@link HttpRequestBase}s which aren't {@link HttpEntityEnclosingRequestBase}s 
	 * will be ignored.</p>
	 * 
	 * <p>See {@link AbstractRequestProcessor#process(InvocationContext, HttpRequestBase)}.</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which is used to discover any annotated form parameters 
	 * <br><br>
	 * @param request
	 * 			prefers an instance of {@link HttpPost} so as to conform with HTTP 1.1; however, other  
	 * 			{@link HttpEntityEnclosingRequestBase}s will be entertained to allow compliance with 
	 * 			unusual endpoint definitions (as long as they are {@link HttpEntityEnclosingRequestBase}s) 
	 * <br><br>
 	 * @return the same instance of {@link HttpRequestBase} which was given for processing form parameters 
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if a form parameters failed to be created and inserted into the request body
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	protected HttpRequestBase process(InvocationContext context, HttpRequestBase request) {

		try {
			
			if(request instanceof HttpEntityEnclosingRequestBase) {
				
				List<NameValuePair> nameValuePairs = new LinkedList<NameValuePair>();
				
				//add static name and value pairs
				List<Param> constantFormParams = RequestUtils.findStaticFormParams(context);
				
				for (Param param : constantFormParams) {
					
					nameValuePairs.add(new BasicNameValuePair(param.name(), param.value()));
				}
				
				//add individual name and value pairs
				List<Entry<FormParam, Object>> formParams = Metadata.onParams(FormParam.class, context);
				
				for (Entry<FormParam, Object> entry : formParams) {
					
					String name = entry.getKey().value();
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
				
				//add batch name and value pairs (along with any static params)
				List<Entry<FormParams, Object>> queryParamMaps = Metadata.onParams(FormParams.class, context);
				
				for (Entry<FormParams, Object> entry : queryParamMaps) {
					
					Param[] constantParams = entry.getKey().value();
					
					if(constantParams != null && constantParams.length > 0) {
					
						for (Param param : constantParams) {
							
							nameValuePairs.add(new BasicNameValuePair(param.name(), param.value()));
						}
					}
					
					Object map = entry.getValue();
					
					if(!(map instanceof Map)) {
					
						StringBuilder errorContext = new StringBuilder()
						.append("@FormParams can only be applied on <java.util.Map>s. ")
						.append("Please refactor the method to provide a Map of name and value pairs. ");
						
						throw new RequestProcessorException(new IllegalArgumentException(errorContext.toString()));
					}
					
					Map<?, ?> nameAndValues = (Map<?, ?>)map;
					
					for (Entry<?, ?> nameAndValue : nameAndValues.entrySet()) {
						
						Object name = nameAndValue.getKey();
						Object value = nameAndValue.getValue();
						
						if(!(name instanceof CharSequence && 
							(value instanceof CharSequence || value instanceof Collection))) {
							
							StringBuilder errorContext = new StringBuilder()
							.append("The <java.util.Map> identified by @FormParams can only contain mappings of type ")
							.append("<java.lang.CharSequence, java.lang.CharSequence> or ")
							.append("<java.lang.CharSequence, java.util.Collection<? extends CharSequence>>");
							
							throw new RequestProcessorException(new IllegalArgumentException(errorContext.toString()));
						}
						
						if(value instanceof CharSequence) {
						
							nameValuePairs.add(new BasicNameValuePair(
								((CharSequence)name).toString(), ((CharSequence)value).toString()));
						}
						else { //add multi-valued form params 
							
							Collection<?> multivalues = (Collection<?>) value;
							
							for (Object multivalue : multivalues) {
								
								if(!(multivalue instanceof CharSequence)) {
									
									StringBuilder errorContext = new StringBuilder()
									.append("Values for the <java.util.Map> identified by @FormParams can only contain collections ")
									.append("of type java.util.Collection<? extends CharSequence>");
									
									throw new RequestProcessorException(new IllegalArgumentException(errorContext.toString()));
								}
								
								nameValuePairs.add(new BasicNameValuePair(
										((CharSequence)name).toString(), ((CharSequence)multivalue).toString()));
							}
						}
					}
				}
				
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs);
				urlEncodedFormEntity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
	
				request.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
				((HttpEntityEnclosingRequestBase)request).setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}
			
			return request;
		}
		catch(Exception e) {
			
			throw (e instanceof RequestProcessorException)? 
					(RequestProcessorException)e :new RequestProcessorException(context, getClass(), e);
		}
	}
}
