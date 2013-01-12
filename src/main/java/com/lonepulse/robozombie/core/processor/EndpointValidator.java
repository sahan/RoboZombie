package com.lonepulse.robozombie.core.processor;

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

import java.net.URI;

import com.lonepulse.robozombie.core.annotation.Endpoint;

/**
 * <p>The policy for validating an endpoint interface.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface EndpointValidator {

	/**
	 * <p>Takes the given endpoint {@link Class} representation and validates 
	 * it to be a true endpoint interface.</p>
	 * 
	 * @param endpointInterface
	 * 				the {@link Class} of the interface which is models an 
	 * 				{@link Endpoint}
	 * 
	 * @return the parsed {@link URI} 
	 * <br><br>
	 * @since 1.1.1 
	 */
	public abstract URI validate(Class<?> endpointInterface);
}
