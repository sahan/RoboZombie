package com.lonepulse.robozombie.core.request;

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

import java.lang.reflect.Method;

/**
 * <p>This runtime exception is thrown when a required annotation is missing from 
 * a request method on the endpoint interface.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class MissingRequestAnnotationException extends RequestBuilderException {

	
	private static final long serialVersionUID = -5554974798638106181L;


	/**
	 * <p>Displays a detailed description along with the stacktrace.
	 * 
	 * @param requestMethod
	 * 			the request {@link Method} which is missing an annotation
	 * 
	 * @param missingAnnotation
	 * 			the annotation which is missing
	 * 
	 * @since 1.1.0
	 */
	public MissingRequestAnnotationException(Method requestMethod, Class<?> missingAnnotation) {
		
		this("Missing annotation " + missingAnnotation.getName() + " on request method " + 
			  requestMethod.getName());
	}
	
	/**
	 * See {@link RuntimeException#RuntimeException()}.
	 */
	public MissingRequestAnnotationException() {
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 */
	public MissingRequestAnnotationException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 */
	public MissingRequestAnnotationException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 */
	public MissingRequestAnnotationException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
