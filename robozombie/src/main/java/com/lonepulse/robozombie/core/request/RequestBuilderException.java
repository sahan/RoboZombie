package com.lonepulse.robozombie.core.request;

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

import com.lonepulse.robozombie.core.RoboZombieRuntimeException;
import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;

/**
 * <p>This runtime exception is thrown when an HTTP request cannot be created 
 * using a give configuration.   
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class RequestBuilderException extends RoboZombieRuntimeException {

	
	private static final long serialVersionUID = -1466493374397626604L;

	
	/**
	 * <p>Displays a detailed description along with the stacktrace.
	 */
	public RequestBuilderException(Class<? extends AbstractRequestBuilder> requestCreatorClass, 
									ProxyInvocationConfiguration config, 
									Throwable throwable) {
	
		this(requestCreatorClass.getName() + " was unable to create request with configuration " + config, throwable);
	}
	
	/**
	 * See {@link RuntimeException#RuntimeException()}.
	 */
	public RequestBuilderException() {
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 */
	public RequestBuilderException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 */
	public RequestBuilderException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 */
	public RequestBuilderException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
