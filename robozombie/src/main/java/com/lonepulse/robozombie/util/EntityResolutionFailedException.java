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
 * <p>This runtime exception is thrown when a specific {@link HttpEntity} implementation failed to be 
 * resolved for a generic object or {@link Class}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public class EntityResolutionFailedException extends RoboZombieRuntimeException {

	
	private static final long serialVersionUID = 905599478471679754L;
	

	/**
	 * <p>Displays a detailed description with information generic object type which failed to be resolved 
	 * to an {@link HttpEntity}.</p>
	 * <p>Displays a detailed description with information generic object type which failed to be resolved 
	 * to an {@link HttpEntity}.</p>
	 * 
	 * @param genericEntity
	 * 			the generic object which failed to be translated to an {@link HttpEntity}
	 * <br><br>
	 * @since 1.3.0
	 */
	public EntityResolutionFailedException(Object genericEntity) {
	
		this("Failed to resolve the org.apache.http.HttpEntity for type " + genericEntity.getClass().getName());
	}
	
	/**
	 * <p>Displays a detailed description with information generic {@link Class} type which failed to be 
	 * resolved to an {@link HttpEntity}.</p>
	 * 
	 * @param genericType
	 * 			the {@link Class} type which failed to be translated to an {@link HttpEntity}
	 * <br><br>
	 * @since 1.3.0
	 */
	public EntityResolutionFailedException(Class<?> genericType) {
		
		this("Failed to resolve the org.apache.http.HttpEntity for type " + genericType.getName());
	}
	
	/**
	 * <p>Displays a detailed description with information generic object type which failed to be resolved 
	 * to an {@link HttpEntity}, while preserving the stacktrace.</p>
	 * 
	 * @param genericEntity
	 * 			the generic object whose specific {@link HttpEntity} failed to be resolved
	 * <br><br>
	 * @param rootCause
	 * 			the root cause which resulted in this resolution failure
	 * <br><br>
	 * @since 1.3.0
	 */
	public EntityResolutionFailedException(Object genericEntity, Throwable rootCause) {
		
		this("Failed to resolve the org.apache.http.HttpEntity for type " + 
			  genericEntity.getClass().getName(), rootCause);
	}
	
	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException()}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public EntityResolutionFailedException() {}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(String)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public EntityResolutionFailedException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public EntityResolutionFailedException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RoboZombieRuntimeException#RoboZombieRuntimeException(String, Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public EntityResolutionFailedException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
