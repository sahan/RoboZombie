package com.lonepulse.robozombie.inject;

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

import com.lonepulse.robozombie.ValidationFailedException;


/**
 * <p>This runtime exception is thrown whenever an {@link EndpointValidator} fails to validate an 
 * endpoint definition against a set of predefined rules.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class EndpointValidationFailedException extends ValidationFailedException {


	private static final long serialVersionUID = -7647297337822890607L;
	

	/**
	 * <p>Displays a detailed description using information about the endpoint definition, along with 
	 * the stacktrace.</p>
	 * 
	 * @param endpoint
	 * 			the {@link Class} of the endpoint definition
	 * 
	 * @param rootCause
	 * 			the root {@link Throwable} cause which resulted in this failure
	 * <br><br>
	 * @since 1.2.4
	 */
	public EndpointValidationFailedException(Class<?> endpoint, Throwable rootCause) {
		
		this(new StringBuilder("Failed to validate endpoint ")
			 .append( endpoint == null? "<null>" :endpoint.getName()).toString(), rootCause);
	}
	
	/**
	 * See {@link ValidationFailedException#ValidationFailedException()}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public EndpointValidationFailedException() {}

	/**
	 * See {@link ValidationFailedException#ValidationFailedException(String)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public EndpointValidationFailedException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link ValidationFailedException#ValidationFailedException(Throwable)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public EndpointValidationFailedException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link ValidationFailedException#ValidationFailedException(String, Throwable)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public EndpointValidationFailedException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
