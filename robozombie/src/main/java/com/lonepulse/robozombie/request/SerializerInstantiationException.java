package com.lonepulse.robozombie.request;

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

import org.apache.http.HttpEntity;

import com.lonepulse.robozombie.util.Entities;
import com.lonepulse.robozombie.util.EntityResolutionFailedException;

/**
 * <p>This runtime exception is thrown when a <b>custom</b> {@link Serializer} failed to be instantiated 
 * using the <b>default constructor</b> on its {@link Class}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
class SerializerInstantiationException extends SerializerException {

	
	private static final long serialVersionUID = -5176137903007152524L;
	

	/**
	 * <p>Displays a detailed error message with information on the {@link Class} of the serializer type 
	 * which failed to be instantiated.</p>
	 * 
	 * @param serializerType
	 * 			the {@link Class} of the {@link Serializer} which failed to be instantiated
	 * <br><br>
	 * @since 1.3.0
	 */
	public SerializerInstantiationException(Class<? extends Serializer<?,?>> serializerType) {
		
		super(new StringBuilder("The serializer of type ").append(serializerType.getName())
			  .append(" failed to be instantiated using its default constructor. Please ensure that an")
			  .append(" accessible default constructor is available. ").toString());
	}
	
	/**
	 * <p>Displays a detailed error message with information on the {@link Class} of the {@link Serializer} 
	 * which failed to be instantiated due to an {@link EntityResolutionFailedException}.</p>
	 * 
	 * <p>See {@link AbstractSerializer#AbstractSerializer(Class)}.</p>
	 * 
	 * @param serializerType
	 * 			the {@link Class} of the {@link Serializer} which failed to be instantiated
	 * <br><br>
	 * @param erfe
	 * 			the {@link EntityResolutionFailedException} which served as the root cause 
	 * 			for {@link Serializer} instantiation failure
	 * <br><br>
	 * @since 1.3.0
	 */
	public <INPUT, OUTPUT> SerializerInstantiationException(
		@SuppressWarnings("rawtypes") Class<? extends Serializer> serializerType, EntityResolutionFailedException erfe) {
		
		super(new StringBuilder("Serializers of type ").append(serializerType.getName())
			  .append(" produce an output which cannot be translated to an ")
			  .append(HttpEntity.class.getName())
			  .append(". \nPlease verify that the output type complies with those specified on ")
			  .append(Entities.class.getName()).append("#resolve(Class<?>) or at the very least ")
			  .append("ensure that the output type is \"java.io.Serializable\"").toString(), erfe);
	}
	
	/**
	 * <p>Displays a detailed error message with information on the {@link Class} of the {@link Serializer} 
	 * which failed to be instantiated, while preserving the stacktrace.</p>
	 * 
	 * @param serializerType
	 * 			the {@link Class} of the {@link Serializer} which failed to be instantiated
	 * <br><br>
	 * @param rootCause
	 * 			the root {@link Throwable} cause which led to instantiation failure
	 * <br><br>
	 * @since 1.3.0
	 */
	public SerializerInstantiationException(
		Class<? extends Serializer<?,?>> serializerType, Throwable rootCause) {
		
		super(new StringBuilder("The serializer of type ").append(serializerType.getName())
			  .append(" failed to be instantiated using its default constructor. Please ensure that an")
			  .append(" accessible default constructor is available. ").toString(), rootCause);
	}
}
