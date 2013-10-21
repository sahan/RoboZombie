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

import org.apache.http.HttpRequest;

import com.lonepulse.robozombie.RoboZombieRuntimeException;
import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This runtime exception is thrown due to an unrecoverable error which occurred when 
 * translating a proxy invocation to an {@link HttpRequest}.   
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class RequestTranslationException extends RoboZombieRuntimeException {

	
	private static final long serialVersionUID = -8808562122307442038L;
	

	/**
	 * <p>Displays a detailed description along with the stacktrace.
	 * 
	 * @param config
	 * 			the {@link InvocationContext} which failed to be translated 
	 * 			to an {@link HttpRequest}
	 * 
	 * @since 1.2.4
	 */
	public RequestTranslationException(InvocationContext config) {
	
		this("Failed to translate an HTTP request for the method <" + config.getRequest().getName() + 
			 "> on the endopint <" + config.getEndpoint().getName());
	}
	
	/**
	 * <p>Displays a detailed description along with the stacktrace.
	 * 
	 * @param config
	 * 			the {@link InvocationContext} which failed to be translated 
	 * 			to an {@link HttpRequest}
	 * 
	 * @param rootCause
	 * 			the root cause of which led to a translation failure
	 * 
	 * @since 1.2.4
	 */
	public RequestTranslationException(InvocationContext config, Throwable rootCause) {
		
		this("Failed to translate an HTTP request for the method <" + config.getRequest().getName() + 
				"> on the endopint <" + config.getEndpoint().getName(), rootCause);
	}
	
	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException()}.
	 * 
	 * @since 1.2.4
	 */
	public RequestTranslationException() {
	}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(String)}.
	 * 
	 * @since 1.2.4
	 */
	public RequestTranslationException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(Throwable)}.
	 * 
	 * @since 1.2.4
	 */
	public RequestTranslationException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(String, Throwable)}.
	 * 
	 * @since 1.2.4
	 */
	public RequestTranslationException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
