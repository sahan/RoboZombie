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

import org.apache.http.HttpResponse;

/**
 * <p>This runtime exception is thrown dow whenever there is a failure in deserializing 
 * the content of an {@link HttpResponse} to the desired entity.
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class DeserializerException extends ResponseProcessorException {

	
	private static final long serialVersionUID = 8193182870145739105L;

	
	/**
	 * See {@link ResponseProcessorException#ResponseProcessorException()}.
	 */
	public DeserializerException() {}

	/**
	 * See {@link ResponseProcessorException#ResponseProcessorException(String)}.
	 */
	public DeserializerException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link ResponseProcessorException#ResponseProcessorException(Throwable)}.
	 */
	public DeserializerException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link ResponseProcessorException#ResponseProcessorException(String, Throwable)}.
	 */
	public DeserializerException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
