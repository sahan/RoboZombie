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

import static com.lonepulse.robozombie.util.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http42.client.methods.HttpPatch;

import com.lonepulse.robozombie.annotation.FormParams;
import com.lonepulse.robozombie.annotation.Headers;
import com.lonepulse.robozombie.annotation.Param;
import com.lonepulse.robozombie.annotation.QueryParams;
import com.lonepulse.robozombie.annotation.Request.RequestMethod;
import com.lonepulse.robozombie.proxy.InvocationContext;
import com.lonepulse.robozombie.util.Metadata;

/**
 * <p>This utility class offers some common operations which are used in building requests - most commonly 
 * using the information contained within a {@link InvocationContext}.
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
final class RequestUtils {
	
	
	private RequestUtils() {}
	
	/**
	 * <p>Finds all <b><i>constant</i> request parameters</b> in the given {@link InvocationContext}.</p> 
	 * <p>Constant request parameters are introduced with @{@link Param} at <b>request level</b> using 
	 * the @{@link QueryParams} annotation.</p>
	 *
	 * @param context
	 * 			the {@link InvocationContext} from which all {@link QueryParams} annotations applied 
	 * 			on the endpoint method will be extracted
	 * <br><br>
	 * @return an <b>unmodifiable</b> {@link List} which aggregates all the @{@link Param} annotations 
	 * 	   	   found on the {@link QueryParams} annotation
	 * <br><br>
	 * @throws NullPointerException
	 * 			if the supplied {@link InvocationContext} was {@code null}
	 * <br><br>
	 * @since 1.3.0
	 */
	 static List<Param> findStaticQueryParams(InvocationContext context) {
		
		Method request = assertNotNull(context).getRequest();
		
		QueryParams queryParams = request.getAnnotation(QueryParams.class);
		
		return Collections.unmodifiableList(queryParams != null? 
				Arrays.asList(queryParams.value()) : new ArrayList<Param>());
	 }
	 
	 /**
	  * <p>Finds all <b><i>constant</i> form parameters</b> in the given {@link InvocationContext}.</p> 
	  * <p>Constant form parameters are introduced with @{@link Param} at <b>request level</b> using 
	  * the @{@link FormParams} annotation.</p>
	  *
	  * @param context
	  * 			the {@link InvocationContext} from which all {@link FormParams} annotations applied 
	  * 			on the endpoint method will be extracted
	  * <br><br>
	  * @return an <b>unmodifiable</b> {@link List} which aggregates all the @{@link Param} annotations 
	  * 	   	   found on the {@link FormParams} annotation
	  * <br><br>
	  * @throws NullPointerException
	  * 			if the supplied {@link InvocationContext} was {@code null}
	  * <br><br>
	  * @since 1.3.0
	  */
	 static List<Param> findStaticFormParams(InvocationContext context) {
		 
		 Method request = assertNotNull(context).getRequest();
		 
		 FormParams formParams = request.getAnnotation(FormParams.class);
		 
		 return Collections.unmodifiableList(formParams != null? 
				 Arrays.asList(formParams.value()) : new ArrayList<Param>());
	 }
	
	/**
	 * <p>Finds all <b>static headers</b> in the given {@link InvocationContext} and returns an unmodifiable 
	 * {@link List} of {@link Map.Entry} instances with the header <i>name</i> as the key and the runtime 
	 * argument as the <i>value</i>.
	 *  
	 * <b>Note</b> that this implementation of {@link Map.Entry#setValue(Object)} throws an 
	 * {@link UnsupportedOperationException}. This list may contain multiple entries with the <i>same name</i> 
	 * as these headers are meant to be <b>added</b> ant not overwritten for a request.</p>
	 * 
	 * <p><i>Static-headers</i> are specified at request-level with @{@link Headers}</p>
	 * 
	 * <br><br>
	 * @param context
	 * 			the {@link InvocationContext} from which all static headers will be discovered
	 * <br><br>
	 * @return an <b>unmodifiable</b> {@link List} of {@link Map.Entry} instances with the header <i>name</i> 
	 * 		   as the key and the runtime argument as the <i>value</i>; <b>note</b> that this implementation 
	 * 			of {@link Map.Entry#setValue(Object)} hrows an {@link UnsupportedOperationException} 
	 * <br><br>
	 * @throws NullPointerException
	 * 			if the supplied {@link InvocationContext} was {@code null}
	 * <br><br>
	 * @since 1.3.0
	 */
	static List<Map.Entry<String, Object>> findStaticHeaders(InvocationContext context) {
		
		assertNotNull(context);
		
		Method request = context.getRequest();
		Headers headerSet = request.getAnnotation(Headers.class);
		
		List<Map.Entry<String, Object>> headers = new ArrayList<Map.Entry<String, Object>>();
		
		if(headerSet != null && headerSet.value() != null && headerSet.value().length > 0) {
		
			List<Headers.Header> staticHeaders = Arrays.asList(headerSet.value());
			
			for (final Headers.Header staticHeader : staticHeaders) {
				
				headers.add(new Map.Entry<String, Object>() {

					@Override
					public String getKey() {
						return staticHeader.name();
					}

					@Override
					public Object getValue() {
						return staticHeader.value();
					}

					@Override
					public Object setValue(Object value) {
						throw new UnsupportedOperationException();
					}
				});
			}
		}
		
		return Collections.unmodifiableList(headers);
	}
	
	/**
	 * <p>Retrieves the proper extension of {@link HttpRequestBase} for the given {@link InvocationContext}. 
	 * This implementation is solely dependent upon the {@link RequestMethod} property in the annotated 
	 * metdata of the endpoint method definition.</p>
	 *
	 * @param context
	 * 			the {@link InvocationContext} for which a {@link HttpRequestBase} is to be generated 
	 * <br><br>
	 * @return the {@link HttpRequestBase} translated from the {@link InvocationContext}'s {@link RequestMethod}
	 * <br><br>
	 * @throws NullPointerException
	 * 			if the supplied {@link InvocationContext} was {@code null} 
	 * <br><br>
	 * @since 1.3.0
	 */
	static HttpRequestBase translateRequestMethod(InvocationContext context) {
		
		RequestMethod requestMethod = Metadata.findMethod(assertNotNull(context).getRequest());
		
		switch (requestMethod) {
		
			case POST: return new HttpPost();
			case PUT: return new HttpPut();
			case PATCH: return new HttpPatch();
			case DELETE: return new HttpDelete();
			case HEAD: return new HttpHead();
			case TRACE: return new HttpTrace();
			case OPTIONS: return new HttpOptions();
			
			case GET: default: return new HttpGet();
		}
	}
}
