package com.lonepulse.robozombie.request;

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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http42.client.utils.URIBuilder;

import com.lonepulse.robozombie.annotation.Param;
import com.lonepulse.robozombie.annotation.QueryParam;
import com.lonepulse.robozombie.annotation.QueryParams;
import com.lonepulse.robozombie.proxy.InvocationContext;
import com.lonepulse.robozombie.util.Metadata;

/**
 * <p>This {@link AbstractRequestProcessor} discovers <i>query parameters</i> in a request which are 
 * annotated with @{@link QueryParam} or @{@link QueryParams} and constructs a 
 * <a href="http://en.wikipedia.org/wiki/Query_string">query string</a> to be appended to the URL.</p> 
 * 
 * <p>The @{@link QueryParam} annotation should be used on an implementation of {@link CharSequence} which 
 * provides the <i>value</i> for each <i>name-value</i> pair; and the supplied {@link QueryParam#value()} 
 * provides the <i>name</i>.</p>
 * 
 * <p>The @{@link QueryParams} annotation should be used on a {@code Map<CharSequence, CharSequence>} of 
 * name and value pairs.</p>
 * 
 * <p>Processor Dependencies:</p>
 * <ul>
 * 	<li>{@link UriProcessor}</li>
 * </ul>
 * 
 * @version 1.3.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
class QueryParamProcessor extends AbstractRequestProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} along with an {@link HttpRequestBase} and creates a 
	 * <a href="http://en.wikipedia.org/wiki/Query_string">query string</a> using arguments annotated 
	 * with @{@link QueryParam} and @{@link QueryParams}; which is subsequently appended to the URI.</p>
	 * 
	 * <p>See {@link AbstractRequestProcessor#process(InvocationContext, HttpRequestBase)}.</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which is used to discover annotated query parameters
	 * <br><br>
	 * @param request
	 * 			prefers an instance of {@link HttpGet} so as to conform with HTTP 1.1; however, other 
	 * 			request types will be entertained to allow compliance with unusual endpoint definitions 
	 * <br><br>
 	 * @return the same instance of {@link HttpRequestBase} which was given for processing query parameters
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if the creation of a query string failed due to an unrecoverable errorS
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	protected HttpRequestBase process(InvocationContext context, HttpRequestBase request) {

		try {
			
			URIBuilder uriBuilder = new URIBuilder(request.getURI());
			
			//add static name and value pairs
			List<Param> constantQueryParams = RequestUtils.findStaticQueryParams(context);
			
			for (Param param : constantQueryParams) {
				
				uriBuilder.setParameter(param.name(), param.value());
			}
			
			//add individual name and value pairs
			List<Entry<QueryParam, Object>> queryParams = Metadata.onParams(QueryParam.class, context);
			
			for (Entry<QueryParam, Object> entry : queryParams) {
				
				String name = entry.getKey().value();
				Object value = entry.getValue();
				
				if(!(value instanceof CharSequence)) {
				
					StringBuilder errorContext = new StringBuilder()
					.append("Query parameters can only be of type ")
					.append(CharSequence.class.getName())
					.append(". Please consider implementing CharSequence ")
					.append("and providing a meaningful toString() representation for the ")
					.append("<name> of the query parameter. ");
					
					throw new RequestProcessorException(new IllegalArgumentException(errorContext.toString()));
				}
				
				uriBuilder.setParameter(name, ((CharSequence)value).toString());
			}
			
			//add batch name and value pairs (along with any static params)
			List<Entry<QueryParams, Object>> queryParamMaps = Metadata.onParams(QueryParams.class, context);
			
			for (Entry<QueryParams, Object> entry : queryParamMaps) {
				
				Param[] constantParams = entry.getKey().value();
				
				if(constantParams != null && constantParams.length > 0) {
				
					for (Param param : constantParams) {
						
						uriBuilder.setParameter(param.name(), param.value());
					}
				}
				
				Object map = entry.getValue();
				
				if(!(map instanceof Map)) {
				
					StringBuilder errorContext = new StringBuilder()
					.append("@QueryParams can only be applied on <java.util.Map>s. ")
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
						.append("The <java.util.Map> identified by @QueryParams can only contain mappings of type ")
						.append("<java.lang.CharSequence, java.lang.CharSequence> or ")
						.append("<java.lang.CharSequence, java.util.Collection<? extends CharSequence>>");
						
						throw new RequestProcessorException(new IllegalArgumentException(errorContext.toString()));
					}
					
					if(value instanceof CharSequence) {
					
						uriBuilder.addParameter(((CharSequence)name).toString(), ((CharSequence)value).toString());
					}
					else { //add multi-valued query params 
						
						Collection<?> multivalues = (Collection<?>) value;
						
						for (Object multivalue : multivalues) {
							
							if(!(multivalue instanceof CharSequence)) {
								
								StringBuilder errorContext = new StringBuilder()
								.append("Values for the <java.util.Map> identified by @QueryParams can only contain collections ")
								.append("of type java.util.Collection<? extends CharSequence>");
								
								throw new RequestProcessorException(new IllegalArgumentException(errorContext.toString()));
							}
							
							uriBuilder.addParameter(((CharSequence)name).toString(), ((CharSequence)multivalue).toString());
						}
					}
				}
			}
			
			request.setURI(uriBuilder.build());
			
			return request;
		}
		catch(Exception e) {
			
			throw (e instanceof RequestProcessorException)? 
					(RequestProcessorException)e :new RequestProcessorException(context, getClass(), e);
		}
	}
}
