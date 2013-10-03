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

import com.lonepulse.robozombie.annotation.Entity;
import com.lonepulse.robozombie.inject.ProxyInvocationConfiguration;

/**
 * <p>This runtime exception is thrown when many @{@link Entity} annotations are discovered on the 
 * argument list of an endpoint method.</p>
 * 
 * <p>It is not imperative that you handle recovery from these failures. However, a recovery could 
 * be attempted by selecting a single annotated entity from the lot based on some method of choice.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class MultipleEntityException extends RequestProcessorException {
	

	private static final long serialVersionUID = 7599536137960728412L;
	

	/**
	 * <p>Displays a detailed description along with the stacktrace.
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} where multiple @{@link Entity} annotations were discovered
	 * 
	 * @since 1.2.4
	 */
	public MultipleEntityException(ProxyInvocationConfiguration config) {
	
		this("Multiple entities annotated with @" + Entity.class.getSimpleName() + 
			 " was not found on the request named [" + config.getRequest().getName() + "] for the URI [" + 
			 config.getUri().toString() + "]. Only a single entity may be annotated and enclosed this request. ");
	}
	
	/**
	 * See {@link RequestProcessorException#RequestPopulatorException()}.
	 */
	public MultipleEntityException() {
	}

	/**
	 * See {@link RequestProcessorException#RequestPopulatorException(String)}.
	 */
	public MultipleEntityException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RequestProcessorException#RequestPopulatorException(Throwable)}.
	 */
	public MultipleEntityException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RequestProcessorException#RequestPopulatorException(String, Throwable)}.
	 */
	public MultipleEntityException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
