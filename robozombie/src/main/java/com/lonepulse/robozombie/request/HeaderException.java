package com.lonepulse.robozombie.request;

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
 * <p>This runtime exception is thrown due to an unrecoverable error in processing dynamic header values 
 * for HTTP requests.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class HeaderException extends RoboZombieRuntimeException {

	
	private static final long serialVersionUID = -7083028842706994616L;
	

	/**
	 * <p>Displays a detailed description along with the stacktrace. 
	 */
	public HeaderException(Method request, Class<?> endpoint, Throwable rootCause) {
		
		this("Failed to process headers for " + request.getName() + 
			 " on " + endpoint.getSimpleName(), rootCause);
	}
	
	/**
	 * See {@link RuntimeException#RuntimeException()}.
	 */
	public HeaderException() {
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 */
	public HeaderException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 */
	public HeaderException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 */
	public HeaderException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
