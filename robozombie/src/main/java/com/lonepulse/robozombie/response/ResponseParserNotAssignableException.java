package com.lonepulse.robozombie.response;

import com.lonepulse.robozombie.annotation.Deserializer;
import com.lonepulse.robozombie.annotation.Request;

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
 * <p>This runtime exception is thrown when the return type of a {@link Request} method 
 * cannot be assigned to the designated {@link Deserializer}'s return type. 
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class ResponseParserNotAssignableException extends ResponseParserException {


	private static final long serialVersionUID = -2526887708389941964L;

	
	/**
	 * <p>Displays a detailed description along with the stacktrace.
	 */
	public ResponseParserNotAssignableException(Class<?> parserReturnType, Class<?> requestReturnType) {
		
		this(new StringBuilder("Cannot assign the parser's response of type ").append(parserReturnType.getName())
			 .append(" to an instance of the request return type ").append(requestReturnType.getName()).toString());
	}
	
	/**
	 * See {@link RuntimeException#RuntimeException()}.
	 */
	public ResponseParserNotAssignableException() {
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 */
	public ResponseParserNotAssignableException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 */
	public ResponseParserNotAssignableException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 */
	public ResponseParserNotAssignableException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
