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
 * <p>This runtime exception is thrown due to a failure in deserializing the content of an 
 * {@link HttpResponse} to the desired entity.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class DeserializerException extends ResponseProcessorException {

	
	private static final long serialVersionUID = 8193182870145739105L;

	
	/**
	 * See {@link ResponseProcessorException#ResponseProcessorException()}.
	 * @since 1.2.4
	 * <br><br>
	 */
	public DeserializerException() {}

	/**
	 * See {@link ResponseProcessorException#ResponseProcessorException(String)}.
	 * @since 1.2.4
	 * <br><br>
	 */
	public DeserializerException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link ResponseProcessorException#ResponseProcessorException(Throwable)}.
	 * @since 1.2.4
	 * <br><br>
	 */
	public DeserializerException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link ResponseProcessorException#ResponseProcessorException(String, Throwable)}.
	 * @since 1.2.4
	 * <br><br>
	 * <br><br>
	 * @since 1.2.4
	 */
	public DeserializerException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
