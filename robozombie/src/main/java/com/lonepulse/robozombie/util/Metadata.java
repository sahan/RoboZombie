package com.lonepulse.robozombie.util;

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

import static com.lonepulse.robozombie.util.Assert.assertNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lonepulse.robozombie.annotation.DELETE;
import com.lonepulse.robozombie.annotation.GET;
import com.lonepulse.robozombie.annotation.HEAD;
import com.lonepulse.robozombie.annotation.OPTIONS;
import com.lonepulse.robozombie.annotation.PATCH;
import com.lonepulse.robozombie.annotation.POST;
import com.lonepulse.robozombie.annotation.PUT;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.annotation.Request.RequestMethod;
import com.lonepulse.robozombie.annotation.TRACE;
import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>A collection of <b>generic</b> utility services which enables the discovery of metadata on endpoint 
 * and request definitions.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @category utility
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class Metadata {
	
	
	private Metadata() {}
	
	
	 /**
	 * <p>Finds all parameters on the request definition which are annotated with the given type and returns 
	 * the annotation instance together with the runtime argument.</p>
	 * 
	 * @param type
	 * 			the {@link Class} of the annotation to look for on the request parameters
	 * <br><br>
	 * @param context
	 * 			the {@link InvocationContext} containing the request definition to extract the metadata from 
	 * <br><br>
	 * @return an <b>unmodifiable</b> {@link List} of {@link Map.Entry} instances with the annotation as 
	 * 		   the and the runtime argument as the <i>value</i>; <b>note</b> that the implementation of 
	 * 		   {@link Map.Entry#setValue(Object)} throws an {@link UnsupportedOperationException} 
	 * <br><br>
	 * @since 1.3.0
	 */
	public static <T extends Annotation> List<Entry<T, Object>> onParams(final Class<T> type, InvocationContext context) {
		 
		assertNotNull(type);
		assertNotNull(context);
		
		List<Entry<T, Object>> metadata = new ArrayList<Entry<T, Object>>();
			
		Method request = context.getRequest();
		List<Object> paramValues = context.getArguments();
		
		Annotation[][] annotationsForAllParams = request.getParameterAnnotations();
		
		for (int i = 0; i < annotationsForAllParams.length; i++) {
			
			final Object value = paramValues.get(i);
			 
			for (final Annotation annotation : annotationsForAllParams[i]) {
				
				if(type.isAssignableFrom(annotation.getClass())) {
					
					metadata.add(new Map.Entry<T, Object>() {

						@Override
						public T getKey() {
							
							return type.cast(annotation);
						}

						@Override
						public Object getValue() {
							
							return value;
						}

						@Override
						public Object setValue(Object value) {
							
							throw new UnsupportedOperationException();
						}
					});
					
					break;
				}
			}
		}
		
		return Collections.unmodifiableList(metadata);
	 }
	
	/**
	 * <p>Takes the {@link Method} definition of a request and discovers the {@link RequestMethod} which 
	 * has been specified using annotated metadata.</p>
	 *
	 * @param definition
	 * 			the {@link Method} definition for the request whose HTTP method is to be discovered  
	 * <br><br>
	 * @return the {@link RequestMethod} for the given request definition; else {@code null} if no 
	 * 		   {@link RequestMethod} metadata can be found
	 * <br><br>
	 * @since 1.3.0
	 */
	public static RequestMethod findMethod(Method definition) {
		
		Request request = definition.getAnnotation(Request.class);
		
		if(request != null) {
			
			return request.method();
		}
		
		Annotation[] annotations = definition.getAnnotations();
			
		for (Annotation annotation : annotations) {
				
			if(annotation.annotationType().isAnnotationPresent(Request.class)) {
					
				return annotation.annotationType().getAnnotation(Request.class).method();
			}
		}
		
		return null;
	}
	
	/**
	 * <p>Takes the {@link Method} definition of a request and discovers the sub-path (if any) which is 
	 * specified using annotated metadata.</p>
	 *
	 * @param definition
	 * 			the {@link Method} definition for the request whose sub-path is to be discovered  
	 * <br><br>
	 * @return the sub-path for the given request definition; else <b>an empty String</p> if no sub-path 
	 * 		   metadata can be found
	 * <br><br>
	 * @since 1.3.0
	 */
	public static String findPath(Method definition) {
		
		Request request = definition.getAnnotation(Request.class);
		
		if(request != null) {
			
			return request.path();
		}
		
		String path = "";
		Annotation[] annotations = definition.getAnnotations();
		
		for (Annotation annotation : annotations) {
			
			if(annotation.annotationType().isAnnotationPresent(Request.class)) {
				
				Class<? extends Annotation> type = annotation.annotationType();
				
				path = type.getAnnotation(Request.class).path();
				
				if("".equals(path)) {
					
					path = type.equals(GET.class)? ((GET)annotation).value() :
						   type.equals(POST.class)? ((POST)annotation).value() :
						   type.equals(PUT.class)? ((PUT)annotation).value() :
						   type.equals(PATCH.class)? ((PATCH)annotation).value() : 
					       type.equals(DELETE.class)? ((DELETE)annotation).value() :
					       type.equals(HEAD.class)? ((HEAD)annotation).value() :
					       type.equals(TRACE.class)? ((TRACE)annotation).value() :
					       type.equals(OPTIONS.class)? ((OPTIONS)annotation).value() : "";
				}
				
				break;
			}
		}
		
		return path;
	}
}
