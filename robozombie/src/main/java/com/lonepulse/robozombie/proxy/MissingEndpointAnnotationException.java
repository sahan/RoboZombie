package com.lonepulse.robozombie.proxy;

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

import com.lonepulse.robozombie.annotation.Endpoint;

/**
 * <p>This runtime exception is thrown when the @{@link Endpoint} annotation is missing on an 
 * endpoint definition.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
class MissingEndpointAnnotationException extends EndpointValidationFailedException {

	
	private static final long serialVersionUID = 4087362624687849076L;

	
	/**
	 * <p>Displays a detailed description with the endpoint definition and the missing annotation.</p>
	 * 
	 * @param endpoint
	 * 			the {@link Class} of the endpoint definition interface
	 * <br><br>
	 * @param missingAnnotation
	 * 			the required annotation which was not found on the endpoint definition
	 * <br><br>
	 * @since 1.3.0
	 */
	public MissingEndpointAnnotationException(Class<?> endpoint, Class<?> missingAnnotation) {
		
		this(new StringBuilder("Missing annotation @").append(missingAnnotation.getName())
			 .append( " on endpoint <").append(endpoint.getName()).append(">").toString());
	}
	
	/**
	 * See {@link EndpointValidationFailedException#EndpointValidationFailedException()}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public MissingEndpointAnnotationException() {}

	/**
	 * See {@link EndpointValidationFailedException#EndpointValidationFailedException(String)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public MissingEndpointAnnotationException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link EndpointValidationFailedException#EndpointValidationFailedException(Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public MissingEndpointAnnotationException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link EndpointValidationFailedException#EndpointValidationFailedException(String, Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public MissingEndpointAnnotationException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
