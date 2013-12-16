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

import java.lang.reflect.Method;

import com.lonepulse.robozombie.annotation.Deserialize;
import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Request;

/**
 * <p>This runtime exception is thrown when a {@link Deserializer} to be used for a particular 
 * {@link Endpoint} or {@link Request} has not been declared using @{@link Deserialize}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class DeserializerUndefinedException extends ResponseProcessorException {

	
	private static final long serialVersionUID = -7605261918947538557L;

	
	/**
	 * <p>Displays a detailed description with information on the endpoint and request definitions.</p> 
	 * 
	 * @param endpoint
	 * 			the {@link Class} of the endpoint definition interface 
	 * <br><br>
	 * @param method
	 * 			the {@link Method} which defines the invoked request
	 * <br><br>
	 * @since 1.3.0
	 */
	public DeserializerUndefinedException(Class<?> endpoint, Method method) {
		
		this(new StringBuilder("Deserialization failed for request <").append(method.getName())
			 .append("> on endpoint <").append(endpoint.getName()).append(">: a deserializer")
			 .append(" has not been attached via the @").append(Deserialize.class.getSimpleName())
			 .append(" annotation.").toString());
	}

	/**
	 * <p>See {@link ResponseProcessorException#ResponseProcessorException(String)}.</p>
	 * <br><br>
	 * @since 1.3.0
	 */
	public DeserializerUndefinedException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * <p>See {@link ResponseProcessorException#ResponseProcessorException(Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public DeserializerUndefinedException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * <p>See {@link ResponseProcessorException#ResponseProcessorException(String, Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public DeserializerUndefinedException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
