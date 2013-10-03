package com.lonepulse.robozombie.inject;

import com.lonepulse.robozombie.RoboZombieRuntimeException;

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
 * <p>This runtime exception is thrown whenever a failure occurs in creating 
 * a proxy via an instance of {@link ProxyFactory}.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class ProxyFactoryException extends RoboZombieRuntimeException {


	private static final long serialVersionUID = -1466493374397626604L;

	
	/**
	 * <p>Displays a detailed description along with the stacktrace. 
	 */
	public ProxyFactoryException(Class<?> proxyFactoryInstance, Throwable rootCause) {
		
		this("Error in proxy factory " + proxyFactoryInstance.getName(), rootCause);
	}
	
	/**
	 * See {@link RuntimeException#RuntimeException()}.
	 */
	public ProxyFactoryException() {
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 */
	public ProxyFactoryException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 */
	public ProxyFactoryException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 */
	public ProxyFactoryException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
