package com.lonepulse.robozombie.core.request.header;

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
 * <p>Specifies the contract for creating HTTP request headers. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface HeaderBuilder {

	/**
	 * <p>Populates the created {@link HttpRequestBase} with any headers.</p> 
	 * 
	 * @param httpRequestBase
	 * 			the {@link HttpRequestBase} which is created by this instance of 
	 * 			abstract request builder
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} which supplies the parameters
	 * <br><br>
	 * @return the {@link HttpRequestBase} with the headers populated
	 * <br><br>
	 * @throws Exception
	 * 			a generic exception is thrown in case operation failed
	 * <br><br>
	 * @since 1.1.0
	 */
	HttpRequestBase build(HttpRequestBase httpRequestBase, ProxyInvocationConfiguration config)
    throws Exception;
}
