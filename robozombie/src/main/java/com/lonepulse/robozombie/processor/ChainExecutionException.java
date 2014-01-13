package com.lonepulse.robozombie.processor;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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
 * <p>This runtime exception is thrown when a {@link AbstractProcessorChain} halted due to an unrecoverable 
 * failure in one of its {@link ProcessorChainLink}s.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
class ChainExecutionException extends ProcessorChainException {
	
	
	private static final long serialVersionUID = -352259198806492953L;
	

	/**
	 * See {@link ProcessorChainException#ProcessorChainException()}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public ChainExecutionException() {}

	/**
	 * See {@link ProcessorChainException#ProcessorChainException(String)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public ChainExecutionException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link ProcessorChainException#ProcessorChainException(Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public ChainExecutionException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link ProcessorChainException#ProcessorChainException(String, Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public ChainExecutionException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
