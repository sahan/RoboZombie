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

import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.core.response.AsyncHandler;

/**
 * <p>Declares the asynchronous network communication capabilities of the application.</p> 
 * 
 * @version 1.1.2
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface AsyncHttpClientContract extends HttpClientContract {
	

	/**
	 * <p>Takes an {@link HttpRequestBase}, executes it asynchronously 
	 * and uses the results to run the {@link AsyncHandler}.
	 * 
	 * @param httpRequestBase 
	 * 			any request of type {@link HttpRequestBase}
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} of the current request
	 * <br><br>
	 * @since 1.1.2
	 */
	public abstract <T extends HttpRequestBase> 
	void executeAsyncRequest(final T httpRequestBase, final ProxyInvocationConfiguration config);
}
