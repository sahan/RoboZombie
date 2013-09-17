package com.lonepulse.robozombie;

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

/**
 * <p>This {@link RoboZombieRuntimeException} is thrown whenever a failure occurs in creating 
 * an instance via reflective invocation of it's default or parameterized constructor.
 * 
 * @see InstantiationException
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class RoboZombieInstantiationException extends RoboZombieRuntimeException {


	private static final long serialVersionUID = -6336187359180038139L;

	
	/**
	 * <p>Displays a detailed description along with the stacktrace. 
	 */
	public RoboZombieInstantiationException(Class<?> componentClass, InstantiationException rootCause) {
		
		this("Could not instantiate component " + componentClass.getName() + 
			 ". Make sure that a default constructor is available and accessible. Note that " + componentClass.getName() + 
			 " cannot be an abstract class, an interface class, " + "an array class, a primitive type, or void.", rootCause);
	}
	
	/**
	 * <p>Displays a detailed description along with the stacktrace. 
	 */
	public RoboZombieInstantiationException(Class<?> componentClass, Constructor<?> constructor, InstantiationException rootCause) {
		
		this("Could not instantiate component " + componentClass.getName() + 
			 " via the parameterized constructor with arguments " + constructor.getParameterTypes().toString() + 
			 ". Make sure that this constructor is available and accessible. Note that " + componentClass.getName() + 
			 " cannot be an abstract class, an interface class, an array class, a primitive type, or void.", rootCause);
	}
	
	/**
	 * See {@link RuntimeException#RuntimeException()}.
	 */
	public RoboZombieInstantiationException() {
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 */
	public RoboZombieInstantiationException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 */
	public RoboZombieInstantiationException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 */
	public RoboZombieInstantiationException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
