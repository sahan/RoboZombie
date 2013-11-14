package com.lonepulse.robozombie.util;

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

import org.apache.http.HttpEntity;

import com.lonepulse.robozombie.RoboZombieRuntimeException;

/**
 * <p>This runtime exception is thrown when a specific {@link HttpEntity} implementation failed 
 * to be resolved for a generic object.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class EntityResolutionFailedException extends RoboZombieRuntimeException {

	
	private static final long serialVersionUID = 905599478471679754L;
	

	/**
	 * <p>Displays a detailed description along with the stacktrace.</p>
	 * 
	 * @param genericEntity
	 * 			the generic object which failed to be translated to an {@link HttpEntity}
	 * 
	 * @since 1.2.4
	 */
	public EntityResolutionFailedException(Object genericEntity) {
	
		this("Failed to resolve the org.apache.http.HttpEntity for type " + genericEntity.getClass().getName());
	}
	
	/**
	 * <p>Displays a detailed description along with the stacktrace.</p>
	 * 
	 * @param genericType
	 * 			the {@link Class} type which failed to be translated to an {@link HttpEntity}
	 * 
	 * @since 1.2.4
	 */
	public EntityResolutionFailedException(Class<?> genericType) {
		
		this("Failed to resolve the org.apache.http.HttpEntity for type " + genericType.getName());
	}
	
	/**
	 * <p>Displays a detailed description along with the stacktrace.</p>
	 * 
	 * @param genericEntity
	 * 			the generic object whose specific {@link HttpEntity} failed to be resolved
	 * 
	 * @param rootCause
	 * 			the root cause which resulted in this resolution failure
	 * 
	 * @since 1.2.4
	 */
	public EntityResolutionFailedException(Object genericEntity, Throwable rootCause) {
		
		this("Failed to resolve the org.apache.http.HttpEntity for type " + 
			  genericEntity.getClass().getName(), rootCause);
	}
	
	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException()}.
	 */
	public EntityResolutionFailedException() {
	}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(String)}.
	 */
	public EntityResolutionFailedException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(Throwable)}.
	 */
	public EntityResolutionFailedException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(String, Throwable)}.
	 */
	public EntityResolutionFailedException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
