package com.lonepulse.robozombie.core.inject;

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

import java.lang.reflect.Field;

import com.lonepulse.robozombie.core.RoboZombieRuntimeException;

/**
 * <p>This runtime exception is thrown whenever a failure occurs in creating 
 * a proxy via the proxy factory.
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class InjectionException extends RoboZombieRuntimeException {


	private static final long serialVersionUID = -1466493374397626604L;

	
	/**
	 * <p>Displays a detailed description along with the stacktrace.
	 */
	public InjectionException(Field field, Class<?> injectee, Class<?> endpoint, Throwable cause) {
		
		this("Failed to inject the endpoint proxy instance of type " + endpoint.getName() + 
			 " on member " + field.getName() + " at " + injectee.getName(), cause);
	}
	
	/**
	 * See {@link RuntimeException#RuntimeException()}.
	 */
	public InjectionException() {
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 */
	public InjectionException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 */
	public InjectionException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 */
	public InjectionException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
