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

import java.lang.reflect.Method;
import java.util.List;

import com.lonepulse.robozombie.annotation.Request;

/**
 * <p>This runtime exception is thrown when an endpoint contains request definitions without the required 
 * metadata provided by an @{@link Request} annotation.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class StrayEndpointRequestException extends EndpointValidationFailedException {
	

	private static final long serialVersionUID = 2582806890934513234L;
	
	
	private static String concatenate(List<Method> strayRequests) {
		
		StringBuilder builder = new StringBuilder();
		
		for (Method request : strayRequests) {
			
			builder.append(", <").append(request.getName()).append(">");
		}
		
		return builder.toString();
	}

	/**
	 * <p>Accepts the malformed request definitions and displays a detailed description.</p> 
	 * 
	 * @param strayRequests
	 * 			the list of request {@link Method} definitions with missing metadata
	 * <br><br> 
	 * @since 1.2.4
	 */
	public StrayEndpointRequestException(List<Method> strayRequests) {
		
		this(new StringBuilder("All endpoint request definitions require an @")
			 .append(Request.class.getName()).append(" annotation. Please add @Request on the request method(s)")
			 .append(StrayEndpointRequestException.concatenate(strayRequests)).append(".").toString());
	}
	
	/**
	 * See {@link EndpointValidationFailedException#EndpointValidationFailedException()}.
	 * <br><br> 
	 * @since 1.2.4
	 */
	public StrayEndpointRequestException() {}

	/**
	 * See {@link EndpointValidationFailedException#EndpointValidationFailedException(String)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public StrayEndpointRequestException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link EndpointValidationFailedException#EndpointValidationFailedException(Throwable)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public StrayEndpointRequestException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link EndpointValidationFailedException#EndpointValidationFailedException(String, Throwable)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public StrayEndpointRequestException(String detailMessage, Throwable rootCause) {

		super(detailMessage, rootCause);
	}
}
