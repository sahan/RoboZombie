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

/**
 * <p>This runtime exception is thrown whenever there is a failure in serializing a model to a content-type 
 * suitable for transmission.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class SerializerException extends RequestProcessorException {

	
	private static final long serialVersionUID = 4070462606231006857L;
	

	/**
	 * See {@link RequestProcessorException#RequestProcessorException()}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public SerializerException() {}

	/**
	 * See {@link RequestProcessorException#RequestProcessorException(String)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public SerializerException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RequestProcessorException#RequestProcessorException(Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public SerializerException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RequestProcessorException#RequestProcessorException(String, Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public SerializerException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
