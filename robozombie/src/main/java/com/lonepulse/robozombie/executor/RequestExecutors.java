package com.lonepulse.robozombie.executor;

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


import java.lang.reflect.Method;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.annotation.Asynchronous;
import com.lonepulse.robozombie.inject.InvocationContext;
import com.lonepulse.robozombie.inject.Zombie;
import com.lonepulse.robozombie.util.Resolver;

/**
 * <p>Exposes all available {@link RequestExecutor}s and delegates communication. 
 * 
 * @version 1.2.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum RequestExecutors implements RequestExecutor {

	/**
	 * See {@link BasicRequestExecutor}.
	 * 
	 * @since 1.1.0
	 */
	BASIC(new BasicRequestExecutor(new BasicExecutionHandler())),
	
	/**
	 * See {@link AsyncRequestExecutor}.
	 * 
	 * @since 1.1.0
	 */
	ASYNC(new AsyncRequestExecutor(new AsyncExecutionHandler()));
	
	
	/**
	 * The exposed instance of {@link RequestExecutor}.
	 */
	private RequestExecutor requestExecutor;

	
	/**
	 * <p>Instantiates {@link #requestExecutor} with the give instance of 
	 * {@link RequestExecutor}.
	 * 
	 * @param requestExecutor
	 * 			the associated instance of {@link RequestExecutor}
	 */
	private RequestExecutors(RequestExecutor requestExecutor) {
		
		this.requestExecutor = requestExecutor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse execute(HttpRequestBase httpRequestBase, InvocationContext config)
	throws RequestExecutionException {
	
		return this.requestExecutor.execute(httpRequestBase, config);
	}
	
	/**
	 * <p>The instance of {@link Resolver} which retrieves suitable 
	 * {@link RequestExecutor}s for a given endpoint request.
	 * 
	 * @since 1.1.0
	 */
	public static final Resolver<InvocationContext, RequestExecutor> RESOLVER 
		= new Resolver<InvocationContext, RequestExecutor>() {
	
		/**
		 * <p>Takes an endpoint {@link Method} and discovers a suitable 
		 * {@link RequestExecutor}.
		 * 
		 * @param config
		 * 			the {@link InvocationContext} for resolving 
		 * 			the associated {@link RequestExecutor}
		 * 
		 * @return an {@link AsyncRequestExecutor} if the request method or 
		 * 		   endpoint is annotated with {@link Asynchronous}, else a 
		 * 		   basic request builder.
		 * <br><br>
		 * @since 1.1.0
		 */
		@Override
		public RequestExecutor resolve(InvocationContext config) {
	
			if(config.getEndpoint().isAnnotationPresent(Asynchronous.class)
				|| config.getRequest().isAnnotationPresent(Asynchronous.class)) {

				return RequestExecutors.ASYNC.requestExecutor;
			}
			else {
				 
				return RequestExecutors.BASIC.requestExecutor;
			}
		}
	};
	
	/**
	 * <p>Manages services related to {@link Zombie.Configuration}s which govern all configurable aspects of 
	 * request execution.</p>
	 *  
	 * <p>See {@link ConfigurationManager}</p>
	 * 
	 * @since 1.2.0
	 */
	public static final ConfigurationManager CONFIGURATION = new ConfigurationService();
}
