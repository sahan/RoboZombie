package com.lonepulse.robozombie.core;

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
 * <p>An extension of {@link RuntimeException} which marks unrecoverable 
 * runtime discrepancies.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class RoboZombieRuntimeException extends RuntimeException {


	private static final long serialVersionUID = 6349350227556147057L;

	
	/**
	 * See {@link RuntimeException#RuntimeException()}.
	 */
	public RoboZombieRuntimeException() {
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 */
	public RoboZombieRuntimeException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 */
	public RoboZombieRuntimeException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 */
	public RoboZombieRuntimeException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
