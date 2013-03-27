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


import java.util.Map;

/**
 * <p>An abstract implementation of {@link GenericFactory} which provides a stub 
 * implementation of the contract that throws {@link UnsupportedOperationException}s 
 * upon invocation.
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class AbstractGenericFactory<Input, Output> implements GenericFactory<Input, Output> {

	
	/**
	 * <p><b>Unsupported</b>. Override to provide an implementation.</p>
	 * 
	 * <p>See {@link GenericFactory#newInstance()}</p>
	 */
	@Override
	public Output newInstance() {
		
		StringBuilder builder = new StringBuilder()
		.append(getClass().getName())
		.append(".newInstance() is unsupported.");
		
		throw new UnsupportedOperationException(builder.toString());
	}

	/**
	 * <p><b>Unsupported</b>. Override to provide an implementation.</p>
	 * 
	 * <p>See {@link GenericFactory#newInstance(Map)}</p>
	 */
	@Override
	public Output newInstance(Map<String, Input> inputMap) {
		
		StringBuilder builder = new StringBuilder()
		.append(getClass().getName())
		.append(".newInstance(Map<String, Input>) is unsupported.");
		
		throw new UnsupportedOperationException(builder.toString());
	}

	/**
	 * <p><b>Unsupported</b>. Override to provide an implementation.</p>
	 * 
	 * <p>See {@link GenericFactory#newInstance(Object, Object...)}</p>
	 */
	@Override
	public Output newInstance(Input input, Input... inputs) {
		
		StringBuilder builder = new StringBuilder()
		.append(getClass().getName())
		.append(".newInstance(Input, Input...) is unsupported.");
		
		throw new UnsupportedOperationException(builder.toString());
	}
}
