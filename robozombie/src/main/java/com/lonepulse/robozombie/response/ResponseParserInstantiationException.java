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


/**
 * <p>This runtime exception is thrown when a <b>custom</b> {@link Deserializer} failed to be 
 * instantiated using the <b>default constructor</b> on its {@link Class}.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class ResponseParserInstantiationException extends ResponseParserException {

	
	private static final long serialVersionUID = 1181478243813659896L;
	

	/**
	 * <p>Displays a detailed error message with the {@link Class} of the response parser type 
	 * which failed to be instantiated.</p>
	 * 
	 * @param parserType
	 * 			the {@link Class} of the parser which failed to be instantiated
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseParserInstantiationException(Class<? extends Deserializer<?>> parserType) {
		
		super(new StringBuilder("The response parser of type ").append(parserType.getName())
			  .append(" failed to be instantiated using its default constructor. Please ensure that an")
			  .append(" accessible default constructor is available. ").toString());
	}
	
	/**
	 * <p>Displays a detailed error message with the {@link Class} of the response parser type 
	 * which failed to be instantiated.</p>
	 * 
	 * @param parserType
	 * 			the {@link Class} of the parser which failed to be instantiated
	 * <br><br>
	 * @param rootCause
	 * 			the root {@link Throwable} cause which led to instantiation failure
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseParserInstantiationException(Class<? extends Deserializer<?>> parserType, Throwable rootCause) {
		
		super(new StringBuilder("The response parser of type ").append(parserType.getName())
			  .append(" failed to be instantiated using its default constructor. Please ensure that an")
			  .append(" accessible default constructor is available. ").toString(), rootCause);
	}
}
