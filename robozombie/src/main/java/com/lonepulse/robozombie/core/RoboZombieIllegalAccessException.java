package com.lonepulse.robozombie.core;

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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * <p>This {@link RoboZombieRuntimeException} is thrown when a {@link Field}, {@link Method} or 
 * {@link Constructor} on a {@link Class} cannot be accessed reflectively; or if the target 
 * {@link Class} itself cannot be accessed.
 * 
 * @see IllegalAccessException
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class RoboZombieIllegalAccessException extends RoboZombieRuntimeException {


	private static final long serialVersionUID = -8300092576066969939L;

	
	/**
	 * <p>Displays a detailed description along with the stacktrace. 
	 */
	public RoboZombieIllegalAccessException(Class<?> componentClass, IllegalAccessException rootCause) {
		
		this("Accessiblity failed on " + componentClass.getName() + 
			 ". Make sure that the invoked field, method or constructor is accessible.", rootCause);
	}
	
	/**
	 * See {@link RuntimeException#RuntimeException()}.
	 */
	public RoboZombieIllegalAccessException() {
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 */
	public RoboZombieIllegalAccessException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 */
	public RoboZombieIllegalAccessException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 */
	public RoboZombieIllegalAccessException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
