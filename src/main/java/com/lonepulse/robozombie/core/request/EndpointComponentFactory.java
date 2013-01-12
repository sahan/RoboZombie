package com.lonepulse.robozombie.core.request;

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

import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;

/**
 * <p>This interface declares the generic policy for a <i>factory</i> which builds the 
 * components of an endpoint.
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface EndpointComponentFactory<T> {

	/**
	 * <p>Takes an array of arguments, constructs the component and returns it.
	 * 
	 * @param proxyInvocationConfiguration
	 * 			the arguments which are used to create the component
	 * 
	 * @return the created component
	 * <br><br>
	 * @since 1.1.1
	 */
	public abstract T create(ProxyInvocationConfiguration proxyInvocationConfiguration);
}
