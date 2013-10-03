package com.lonepulse.robozombie.validator;

import com.lonepulse.robozombie.inject.ProxyInvocationConfiguration;


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
 * <p>This runtime exception is thrown whenever an {@link EndpointValidator} fails to validate 
 * the request method definition for a {@link ProxyInvocationConfiguration}.
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class RequestValidationFailedException extends ValidationFailedException {


	private static final long serialVersionUID = 2136119555081435490L;
	

	/**
	 * <p>Displays a detailed description using the information contained in the given 
	 * {@link ProxyInvocationConfiguration}. 
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} whose request validation failed
	 */
	public RequestValidationFailedException(ProxyInvocationConfiguration config) {
		
		this("Validation failed for the endpoint method definition " + config.getRequest().getName() + 
			 "on endpoint " + config.getEndpointClass().getName());
	}
	
	/**
	 * <p>Displays a detailed description using the information contained in the given 
	 * {@link ProxyInvocationConfiguration} along with the root cause. 
	 */
	public RequestValidationFailedException(ProxyInvocationConfiguration config, Throwable rootCause) {
		
		this("Validation failed for the endpoint method definition " + config.getRequest().getName() + 
				"on endpoint " + config.getEndpointClass().getName(), rootCause);
	}
	
	/**
	 * See {@link ValidationFailedException#ValidationFailedException()}.
	 */
	public RequestValidationFailedException() {
	}

	/**
	 * See {@link ValidationFailedException#ValidationFailedException(String)}.
	 */
	public RequestValidationFailedException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link ValidationFailedException#ValidationFailedException(Throwable)}.
	 */
	public RequestValidationFailedException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link ValidationFailedException#ValidationFailedException(String, Throwable)}.
	 */
	public RequestValidationFailedException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
