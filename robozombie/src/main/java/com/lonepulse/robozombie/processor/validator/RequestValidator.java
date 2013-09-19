package com.lonepulse.robozombie.processor.validator;

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

import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.processor.ProxyInvocationConfiguration;

/**
 * <p>A concrete implementation of {@link RequestValidator} which validates a request definition 
 * on an endpoint.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class RequestValidator implements Validator<Void> {

	/**
	 * <p>Validates a request definition {@link Method} on an endpoint which is contained in the 
	 * property {@link ProxyInvocationConfiguration#getRequest()}. It checks for the mandatory 
	 * existence of an @{@link Request} annotation.</p>
	 * 
	 * <p>See {@link Validator#validate(ProxyInvocationConfiguration)}.
	 * 
	 * @param config
	 * 			a partially built {@link ProxyInvocationConfiguration} which contains the request 
	 * 			definition in {@link ProxyInvocationConfiguration#getRequest()} 
	 * 
	 * @return <b>{@code null}</b> - no validation result is returned; execution without any 
	 * 		   exception indication a <i>pass</i>
	 * 
	 * @throws ValidationFailedException
	 * 			if an @{@link Request} annotation was not found or if its metadata is corrupt
	 * 
	 * @throws IllegalArgumentException 
	 * 			if the given {@link ProxyInvocationConfiguration} is {@code null} or if its 
	 * 			{@link ProxyInvocationConfiguration#getRequest()} property is {@code null}
	 * 
	 * @since 1.2.4
	 */
	@Override
	public Void validate(ProxyInvocationConfiguration config) throws ValidationFailedException {

		if(config == null) {
			
			throw new IllegalArgumentException("The ProxyInvocationConfiguration cannot be <null>. ");
		}
		
		if(config.getRequest() == null) {
			
			throw new IllegalArgumentException("The ProxyInvocationConfiguration's <request> property cannot be <null>. ");
		}
		
		Method request = config.getRequest();
		
		try {
			
			Request requestAnnotation = request.getAnnotation(Request.class);
			
			if(requestAnnotation == null) {
				
				throw new MissingRequestAnnotationException();
			}
			
			return null;
		}
		catch(Exception e) {
			
			throw new RequestValidationFailedException(config, e);
		}
	}
}
