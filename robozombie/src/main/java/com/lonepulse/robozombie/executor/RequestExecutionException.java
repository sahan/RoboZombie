package com.lonepulse.robozombie.executor;

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

import com.lonepulse.robozombie.RoboZombieRuntimeException;

/**
 * <p>This runtime exception is thrown whenever a failure occurs in processing a request invocation.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class RequestExecutionException extends RoboZombieRuntimeException {

	
	private static final long serialVersionUID = -7083028842706994616L;
	
	
	/**
	 * <p>If the given exception is an instance of {@link RequestExecutionException} it is returned as 
	 * it is, else a new {@link RequestExecutionException} is created using the given exception as the 
	 * root cause.</p>
	 *
	 * @param request
	 * 			the {@link Method} which represents the invoked request's definition
	 * <br><br>
	 * @param endpoint
	 * 			the endpoint {@link Class} whose request execution resulted in a failure
	 * <br><br>
	 * @param e
	 * 			the exception to be wrapped in an instance of {@link RequestExecutionException}
	 * <br><br>
	 * @return the wrapped instance of {@link RequestExecutionException}
	 * <br><br>
	 * @since 1.3.0
	 */
	public static final RequestExecutionException wrap(Method request, Class<?> endpoint, Exception e) {
		
		return (e instanceof RequestExecutionException)? 
				(RequestExecutionException)e :new RequestExecutionException(request, endpoint, e);
	}
	
	
	/**
	 * <p>Displays a detailed description with information on the endpoint and request definition.<p>
	 * 
	 * @param request
	 * 			the {@link Method} which represents the invoked request's definition
	 * <br><br>
	 * @param endpoint
	 * 			the endpoint {@link Class} whose request execution resulted in a failure
	 * <br><br>
	 * @param rootCause
	 * 			the parent cause which resulted in the request execution failure 
	 * <br><br>
	 * @since 1.3.0
	 */
	public RequestExecutionException(Method request, Class<?> endpoint, Throwable rootCause) {
		
		super(new StringBuilder("Failed to execute request <").append(request.getName())
			  .append("> on <").append(endpoint.getSimpleName()).append(">").toString(), rootCause);
	}
	
	/**
	 * <p>See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(String, Throwable)}.</p>
	 * <br><br>
	 * @since 1.3.0
	 */
	public RequestExecutionException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
	
	/**
	 * <p>See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(String)}.</p>
	 * <br><br>
	 * @since 1.3.0
	 */
	public RequestExecutionException(String detailMessage) {
		
		super(detailMessage);
	}
}
