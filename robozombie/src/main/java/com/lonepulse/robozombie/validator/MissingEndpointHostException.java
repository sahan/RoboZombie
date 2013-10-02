package com.lonepulse.robozombie.validator;


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


/**
 * <p>This runtime exception is thrown when a hostname is not specified on an endpoint interface.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class MissingEndpointHostException extends EndpointValidationFailedException {

	
	private static final long serialVersionUID = 223448727330932043L;
	

	/**
	 * <p>Displays a detailed description along with the stacktrace. 
	 */
	public MissingEndpointHostException(Class<?> endpointInterface) {
		
		this("The endpoint " + endpointInterface.getName() + " should specify a host name via the @Endpoint annotation.");
	}
	
	/**
	 * See {@link EndpointValidationFailedException#EndpointValidationFailedException()}.
	 */
	public MissingEndpointHostException() {
	}

	/**
	 * See {@link EndpointValidationFailedException#EndpointValidationFailedException(String)}.
	 */
	public MissingEndpointHostException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link EndpointValidationFailedException#EndpointValidationFailedException(Throwable)}.
	 */
	public MissingEndpointHostException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link EndpointValidationFailedException#EndpointValidationFailedException(String, Throwable)}.
	 */
	public MissingEndpointHostException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
