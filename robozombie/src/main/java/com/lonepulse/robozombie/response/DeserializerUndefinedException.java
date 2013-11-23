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

import com.lonepulse.robozombie.annotation.Deserializer;
import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Request;

/**
 * <p>This runtime exception is thrown when a {@link Deserializer} to be used for a particular 
 * {@link Endpoint}, or {@link Request} thereof, has not been declared using {@link Deserializer}.
 * 
 * @version 1.1.1
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class DeserializerUndefinedException extends ResponseProcessorException {

	
	private static final long serialVersionUID = -7605261918947538557L;

	
	/**
	 * <p>Displays a detailed description along with the stacktrace. 
	 */
	public DeserializerUndefinedException(Class<?> endpoint, Method method) {
		
		this(new StringBuilder("Deserialization failed for request ").append(method.getName())
			 .append(" on endpoint ").append(endpoint.getName()).append(": a ").append(Deserializer.class.getName())
			 .append(" has not been defined via the ").append(Deserializer.class.getName()).append(" annotation.").toString());
	}

	/**
	 * <p>Displays a detailed description along with the stacktrace. 
	 */
	public DeserializerUndefinedException() {
		
		this("Deserialization failed for request. A " + Deserializer.class.getName() + 
			  " has not been defined via the " + Deserializer.class.getName() + " annotation.");
	}

	/**
	 * <p>See {@link ResponseProcessorException#ResponseProcessorException(String)}.
	 */
	public DeserializerUndefinedException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * <p>See {@link ResponseProcessorException#ResponseProcessorException(Throwable)}.
	 */
	public DeserializerUndefinedException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * <p>See {@link ResponseProcessorException#ResponseProcessorException(String, Throwable)}.
	 */
	public DeserializerUndefinedException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}