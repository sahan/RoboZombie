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

import com.lonepulse.robozombie.RoboZombieRuntimeException;

/**
 * <p>This runtime exception is thrown due to a failure in creating a proxy which reflects a given 
 * endpoint definition using an instance of {@link ProxyFactory}.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
class ProxyFactoryException extends RoboZombieRuntimeException {


	private static final long serialVersionUID = -1466493374397626604L;

	
	/**
	 * <p>Displays a simple message which identifies the {@link ProxyFactory} and preserves the root 
	 * cause on stacktrace.</p> 
	 * 
	 * @param proxyFactory
	 * 			the {@link Class} of the proxy factory which generated the failure
	 * <br><br>
	 * @param rootCause
	 * 			the root {@link Throwable} cause which resulted in the failure
	 * <br><br>
	 * @since 1.1.0
	 */
	public ProxyFactoryException(Class<?> proxyFactory, Throwable rootCause) {
		
		this("Error in proxy factory " + proxyFactory.getName(), rootCause);
	}
	
	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException()}.
	 * <br><br>
	 * @since 1.1.0
	 */
	public ProxyFactoryException() {}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(String)}.
	 * <br><br>
	 * @since 1.1.0
	 */
	public ProxyFactoryException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(Throwable)}.
	 * <br><br>
	 * @since 1.1.0
	 */
	public ProxyFactoryException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(String, Throwable)}.
	 * <br><br>
	 * @since 1.1.0
	 */
	public ProxyFactoryException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
