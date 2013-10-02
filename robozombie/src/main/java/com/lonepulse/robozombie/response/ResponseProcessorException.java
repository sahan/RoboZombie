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

import java.util.Arrays;

import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.RoboZombieRuntimeException;

/**
 * <p>This runtime exception is thrown when an HTTP {@link ResponseProcessor} fails to execute successfully 
 * for a given {@link HttpResponse} and {@link ProxyInvocationConfiguration}.
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class ResponseProcessorException extends RoboZombieRuntimeException {

	
	private static final long serialVersionUID = -7772538141198806201L;
	

	/**
	 * <p>Displays a detailed description along with the stacktrace.
	 * 
	 * @param responseProcessorClass
	 * 			the {@link Class} of the {@link ResponseProcessor} implementation which failed
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} which caused the {@link ResponseProcessor} to fail
	 * 
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseProcessorException(Class<?> responseProcessorClass, 
									  ProxyInvocationConfiguration config) {
	
		this(new StringBuilder(responseProcessorClass.getName())
			 .append(" failed to process the response for the request [")
			 .append(config.getRequest().getName())
			 .append("] on [")
			 .append(config.getUri().toASCIIString())
			 .append("] with arguments ")
			 .append(Arrays.toString(config.getRequestArgs())).toString());
	}
	
	/**
	 * <p>Displays a detailed description along with the stacktrace.
	 * 
	 * @param responseProcessorClass
	 * 			the {@link Class} of the {@link ResponseProcessor} implementation which failed
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} which caused the {@link ResponseProcessor} to fail
	 * 
	 * @param rootCause
	 * 			the parent exception which caused the {@link RequestProcessor} to fail
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseProcessorException(Class<?> responseProcessorClass, 
									  ProxyInvocationConfiguration config, 
									  Throwable rootCause) {
		
		this(new StringBuilder(responseProcessorClass.getName())
			 .append(" failed to process the response for the request [")
			 .append(config.getRequest().getName())
			 .append("] on [")
			 .append(config.getUri().toASCIIString())
			 .append("] with arguments ")
			 .append(Arrays.toString(config.getRequestArgs())).toString(), rootCause);
	}
	
	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException()}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseProcessorException() {}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(String)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseProcessorException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(Throwable)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseProcessorException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(String, Throwable)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseProcessorException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
