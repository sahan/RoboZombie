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

import com.lonepulse.robozombie.annotation.Deserializer;

/**
 * <p>This runtime exception is thrown when the return type of a request method definition cannot be 
 * assigned to the designated {@link Deserializer}'s return type. 
 * 
 * @version 1.1.1
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class DeserializerNotAssignableException extends DeserializerException {


	private static final long serialVersionUID = -2526887708389941964L;

	
	/**
	 * <p>Displays a detailed description with information on the deserializer's output type and request 
	 * return type.</p>
	 * 
	 * @param deserializerOutputType
	 * 			the output type of the {@link Deserializer} which was found to be incompatible
	 * <br><br>
	 * @param requestReturnType
	 * 			the return type of the requuest definition which was found to be incompatible
	 * <br><br>
	 * @since 1.2.4
	 */
	public DeserializerNotAssignableException(Class<?> deserializerOutputType, Class<?> requestReturnType) {
		
		this(new StringBuilder("Cannot assign the deserializer's response of type ").append(deserializerOutputType.getName())
			 .append(" to an instance of the request return type ").append(requestReturnType.getName()).toString());
	}
	
	/**
	 * See {@link DeserializerException#DeserializerException()}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public DeserializerNotAssignableException() {}

	/**
	 * See {@link DeserializerException#DeserializerException(String)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public DeserializerNotAssignableException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link DeserializerException#DeserializerException(Throwable)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public DeserializerNotAssignableException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link DeserializerException#DeserializerException(String, Throwable)}.
	 */
	public DeserializerNotAssignableException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
