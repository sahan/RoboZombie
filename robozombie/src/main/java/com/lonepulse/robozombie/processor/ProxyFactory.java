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

import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Param;
import com.lonepulse.robozombie.annotation.Request;

/**
 * <p>This is is a common policy which all proxy factories must implement.
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
interface ProxyFactory {

	/**
	 * <p>This factory method takes a {@link Class} representation of an interface 
	 * which models a communication endpoint and returns a single instance of a 
	 * dynamically generated proxy for that endpoint interface.</p>
	 * <br>
	 * <p>The interface should be annotated with {@link Endpoint} and may 
	 * contain one or more abstract method declarations which are annotated with 
	 * {@link Request} (and optionally {@link Param}).</p>
	 * 
	 * @param typeClass
	 * 				the {@link Class} of the interface which model an {@link Endpoint}
	 * <br><br>
	 * @return the dynamically created proxy for the endpoint interface
	 * <br><br>
	 * @throws ProxyFactoryException
	 * 				due to any failure in creating a proxy
	 * <br><br>
	 * @since 1.1.1			
	 */
	public abstract <T> T create(final Class<T> typeClass);
}
