package com.lonepulse.robozombie.core.response.parser;

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

import java.lang.reflect.Method;

/**
 * <p>This runtime exception is thrown when a header parameter annotation is 
 * marked on a variable other than that of type {@link StringBuilder}.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class HeaderParamTypeException extends ResponseParserException {

	
	private static final long serialVersionUID = -8060844427557378441L;

	
	/**
	 * <p>Displays a detailed description along with the stacktrace. 
	 */
	public HeaderParamTypeException(Object param, Method method) {
		
		this("Variable header parameters should be of type " + 
			 StringBuilder.class.getSimpleName() + ". Instead type " + 
			 param.getClass().getName() + " was found on request " + 
			 method.getName());
	}
	
	/**
	 * See {@link RuntimeException#RuntimeException()}.
	 */
	public HeaderParamTypeException() {
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 */
	public HeaderParamTypeException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 */
	public HeaderParamTypeException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 */
	public HeaderParamTypeException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
