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


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.lonepulse.robozombie.annotation.Header;
import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This utility class offers some common operations which are used in parsing responses using the information 
 * contained within a {@link InvocationContext}.
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @category utility
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 * 
 * TODO revise utilities isolate a common algorithm for annotated params extraction 
 */
final class ResponseUtils {
	
	
	/**
	 * <p>Constructor visibility restricted. Instantiation is nonsensical. 
	 */
	private ResponseUtils() {}
	
	/**
	 * <p>Finds all <b>dynamic response headers</b> in the given {@link InvocationContext} and 
	 * returns an unmodifiable {@link List} of {@link Map.Entry} instances with the header <i>name</i> as the 
	 * key and the runtime argument as the <i>value</i>. <b>Note</b> that this implementation of 
	 * {@link Map.Entry#setValue(Object)} throws an {@link UnsupportedOperationException}. This list may contain 
	 * multiple entries with the <i>same name</i> (as returned by the endpoint) to be processed separately.</p>
	 * 
	 * <p>Response headers are specified at argument-level with @{@link Header} <b>on a {@link StringBuilder}</b>.</p>
	 * 
	 * <br><br>
	 * @param config
	 * 			the {@link InvocationContext} from which all response headers will be discovered
	 * <br><br>
	 * @return an <b>unmodifiable</b> {@link List} of {@link Map.Entry} instances with the header <i>name</i> as 
	 * 		   the key and the runtime argument as the <i>value</i>; <b>note</b> that this implementation of 
	 * 		   {@link Map.Entry#setValue(Object)} throws an {@link UnsupportedOperationException} 
	 * <br><br>
	 * @throws IllegalArgumentException
	 * 			if the supplied {@link InvocationContext} was {@code null}
	 * <br><br>
	 * @since 1.2.4
	 */
	public static List<Map.Entry<String, Object>> findHeaders(InvocationContext config) {
		
		if(config == null) {
			
			new IllegalArgumentException("The supplied Proxy Invocation Configuration cannot be <null>.");
		}
		
		Method request = config.getRequest();
		List<Object> paramValues = config.getArguments();
		
		List<Map.Entry<String, Object>> headers = new ArrayList<Map.Entry<String, Object>>();
		
		Annotation[][] annotationsForAllParams = request.getParameterAnnotations();
		
		for (int i = 0; i < annotationsForAllParams.length; i++) {
			
			for (Annotation annotation : annotationsForAllParams[i]) {
				
				if(Header.class.isAssignableFrom(annotation.getClass())) {
					
					final Header header = (Header)annotation;
					final Object paramValue = paramValues.get(i);
					
					headers.add(new Map.Entry<String, Object>() {

						@Override
						public String getKey() {
							return header.value();
						}

						@Override
						public Object getValue() {
							return paramValue;
						}

						@Override
						public Object setValue(Object value) {
							throw new UnsupportedOperationException();
						}
					});
					
					break; //only one @Header annotation is expected per endpoint method argument
				}
			}
		}
		
		return Collections.unmodifiableList(headers);
	}
}
