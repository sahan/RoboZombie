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


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.lonepulse.robozombie.annotation.Param;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.annotation.Rest;
import com.lonepulse.robozombie.processor.ProxyInvocationConfiguration;

/**
 * <p>This utility class offers some common operations which are used in building requests - most commonly 
 * using the information contained within a {@link ProxyInvocationConfiguration}.
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @category utility
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class RequestUtils {
	
	
	/**
	 * <p>Constructor visibility restricted. Instantiation is nonsensical. 
	 */
	private RequestUtils() {}
	
	
	/**
	 * <p>Finds all <b><i>constant</i> request parameters</b> in the given {@link ProxyInvocationConfiguration}.</p> 
	 * <p>Constant request parameters are introduced with @{@link Request.Param} at <b>request level</b> using 
	 * either the @{@link Request} or the @{@link Rest} annotation.</p>
	 *
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} from which all {@link Request.Param} annotations applied 
	 * 			on the endpoint method will be extracted
	 * 
	 * @return an unmodifiable {@link List} which aggregates all the @{@link Request.Param} annotations found on 
	 * 		   the {@link Request} or {@link Rest}ful request 
	 * 
	 * @since 1.2.4
	 */
	public static List<Request.Param> findConstantRequestParams(ProxyInvocationConfiguration config) {
		
		Method request = config.getRequest();
		
		Request webRequest = request.getAnnotation(Request.class);
		Rest restfulRequest = request.getAnnotation(Rest.class);
		
		Request.Param[] requestParams = (webRequest != null)? webRequest.params() :restfulRequest.params();
		
		return Collections.unmodifiableList(Arrays.asList(requestParams));
	}
	
	/**
	 * <p>Finds all <b>request parameters</b> in the given {@link ProxyInvocationConfiguration}.</p> 
	 * 
	 * <p>Request parameters are introduced with @{@link Param} on arguments to an endpoint request method.</p>
	 *
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} from which all {@link Param} annotations applied 
	 * 			on the endpoint method arguments will be extracted
	 * 
	 * @return an unmodifiable {@link Map} in the form {@code Map<name, value>} which aggregates all the param 
	 * 		   names coupled with the value of the linked method argument
	 * 
	 * @since 1.2.4
	 */
	public static Map<String, Object> findRequestParams(ProxyInvocationConfiguration config) {
		
		Map<String, Object> paramMap = new LinkedHashMap<String, Object>(); 
		
		Method request = config.getRequest();
		Object[] paramValues = config.getRequestArgs();
		
		Annotation[][] annotationsForAllParams = request.getParameterAnnotations();
		
		int i = 0;
		
		for (Annotation[] annotationsForEachParam : annotationsForAllParams) {
			
			for (Annotation annotation : annotationsForEachParam) {
			
				if(Param.class.isAssignableFrom(annotation.getClass())) {
					
					paramMap.put(((Param)annotation).value(), paramValues[i++]);
					break; //only one @Param annotation is expected per endpoint method argument
				}
			}
		}
		
		return Collections.unmodifiableMap(paramMap);
	}
}
