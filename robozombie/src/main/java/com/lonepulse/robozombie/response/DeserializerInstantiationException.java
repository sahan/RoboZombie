package com.lonepulse.robozombie.response;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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
 * <p>This runtime exception is thrown when a <b>custom</b> {@link Deserializer} failed to be instantiated 
 * using the <b>default constructor</b> on its {@link Class}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
class DeserializerInstantiationException extends DeserializerException {

	
	private static final long serialVersionUID = 1181478243813659896L;
	

	/**
	 * <p>Displays a detailed error message with the {@link Class} of the deserializer type which failed 
	 * to be instantiated.</p>
	 * 
	 * @param deserializerType
	 * 			the {@link Class} of the deserializer which failed to be instantiated
	 * <br><br>
	 * @since 1.3.0
	 */
	public DeserializerInstantiationException(Class<? extends Deserializer<?>> deserializerType) {
		
		super(new StringBuilder("The deserializer of type ").append(deserializerType.getName())
			  .append(" failed to be instantiated using its default constructor. Please ensure that an")
			  .append(" accessible default constructor is available. ").toString());
	}
	
	/**
	 * <p>Displays a detailed error message with the {@link Class} of the deserializer type which failed 
	 * to be instantiated, while preserving the stacktrace.</p>
	 * 
	 * @param deserializerType
	 * 			the {@link Class} of the deserializer which failed to be instantiated
	 * <br><br>
	 * @param rootCause
	 * 			the root {@link Throwable} cause which led to instantiation failure
	 * <br><br>
	 * @since 1.3.0
	 */
	public DeserializerInstantiationException(Class<? extends Deserializer<?>> deserializerType, Throwable rootCause) {
		
		super(new StringBuilder("The deserializer of type ").append(deserializerType.getName())
			  .append(" failed to be instantiated using its default constructor. Please ensure that an")
			  .append(" accessible default constructor is available. ").toString(), rootCause);
	}
}
