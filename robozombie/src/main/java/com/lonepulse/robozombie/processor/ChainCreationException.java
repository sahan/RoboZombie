package com.lonepulse.robozombie.processor;

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
 * <p>This runtime exception is thrown when a {@link AbstractProcessorChain} failed to be created by 
 * instantiating the individual {@link ProcessorChainLink}s and linking them.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
class ChainCreationException extends ProcessorChainException {
	
	
	private static final long serialVersionUID = 2867161442144265001L;
	

	/**
	 * See {@link ProcessorChainException#ProcessorChainException()}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public ChainCreationException() {}

	/**
	 * See {@link ProcessorChainException#ProcessorChainException(String)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public ChainCreationException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link ProcessorChainException#ProcessorChainException(Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public ChainCreationException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link ProcessorChainException#ProcessorChainException(String, Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public ChainCreationException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
