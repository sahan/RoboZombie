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


import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;

/**
 * <p>Specifies the contract for creating HTTP requests. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface RequestBuilder {
	
	/**
	 * <p>Creates an {@link HttpRequestBase} and configures it using the information in 
	 * the given {@link ProxyInvocationConfiguration} instance.</p> 
	 * 
	 * @param config
	 * 			the instance of {@link ProxyInvocationConfiguration} which supplies the parameters
	 * <br><br>
	 * @return the {@link HttpRequestBase} with the populated parameters
	 * <br><br>
	 * @throws {@link RequestBuilderException}
	 * 				thrown if the {@link HttpRequestBase} cannot be built	
	 * <br><br>
	 * @since 1.1.0
	 */
	HttpRequestBase build(ProxyInvocationConfiguration config);
}
