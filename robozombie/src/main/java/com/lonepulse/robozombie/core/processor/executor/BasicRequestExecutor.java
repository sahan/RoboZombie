package com.lonepulse.robozombie.core.processor.executor;

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


import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HttpContext;

import com.lonepulse.robozombie.core.MultiThreadedHttpClient;
import com.lonepulse.robozombie.core.annotation.Stateful;
import com.lonepulse.robozombie.core.cookie.HttpContextDirectory;
import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;

/**
 * <p>A concrete implementation of {@link RequestExecutor} which executes 
 * {@link HttpRequest}s. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class BasicRequestExecutor implements RequestExecutor {

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized HttpResponse execute(HttpRequestBase httpRequestBase, ProxyInvocationConfiguration config)
	throws RequestExecutionException {
	
		try {
			
			Class<?> endpointClass = config.getEndpointClass();
			Method request = config.getRequest();
			HttpResponse httpResponse;
			
			if(endpointClass.isAnnotationPresent(Stateful.class)
				|| request.isAnnotationPresent(Stateful.class)) {
				
				HttpContext httpContext = HttpContextDirectory.INSTANCE.get(endpointClass);
				httpResponse = MultiThreadedHttpClient.INSTANCE.executeRequest(httpRequestBase, httpContext);
			}
			else {
				
				httpResponse = MultiThreadedHttpClient.INSTANCE.executeRequest(httpRequestBase);
			}
			
			if(!(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)) {
				
				StringBuilder builder = new StringBuilder()
				.append("HTTP request for ")
				.append(httpRequestBase.getURI())
				.append(" failed with status code ")
				.append(httpResponse.getStatusLine().getStatusCode())
				.append(", ")
				.append(httpResponse.getStatusLine().getReasonPhrase());
				
				throw new IOException(builder.toString());
			}
		
			return httpResponse;
		}
		catch (Exception e) {
			
			throw new RequestExecutionException(config.getRequest(), config.getEndpointClass(), e);
		}
	}
}
