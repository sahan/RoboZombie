package com.lonepulse.robozombie.core.response;

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

import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.core.processor.executor.RequestExecutor;
import com.lonepulse.robozombie.core.response.parser.ResponseParser;
import com.lonepulse.robozombie.util.Resolver;


/**
 * <p>Exposes all available {@link ResponseHandler}s and delegates communication. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum ResponseHandlers implements ResponseHandler {

	/**
	 * See {@link BasicResponseHandler}.
	 * 
	 * @since 1.1.0
	 */
	BASIC(new BasicResponseHandler());
	
	
	/**
	 * The exposed instance of {@link ResponseHandler}.
	 */
	private ResponseHandler responseHandler;

	
	/**
	 * <p>Instantiates {@link #responseHandler} with the give instance of 
	 * {@link ResponseHandler}.
	 * 
	 * @param requestExecutor
	 * 			the associated instance of {@link RequestExecutor}
	 */
	private ResponseHandlers(ResponseHandler responseHandler) {
		
		this.responseHandler = responseHandler;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object handle(HttpResponse httpResponse, ProxyInvocationConfiguration config)
	throws ResponseHandlerException {

		return this.responseHandler.handle(httpResponse, config);
	}
	
	/**
	 * <p>The instance of {@link Resolver} which retrieves suitable {@link ResponseHandler}s 
	 * for a given endpoint request.
	 * 
	 * @since 1.1.0
	 */
	public static final Resolver<ProxyInvocationConfiguration, ResponseHandler> RESOLVER 
		= new Resolver<ProxyInvocationConfiguration, ResponseHandler>() {
	
		/**
		 * <p>Takes an endpoint {@link Method} and discovers a suitable {@link ResponseHandler}.
		 * 
		 * @param config
		 * 			the {@link ProxyInvocationConfiguration} for resolving the 
		 * 			associated {@link ResponseParser}
		 * 
		 * @return the resolved {@link ResponseParser} or {@link String} response 
		 * 		   parser if the resolution cannot be solved 
		 * 
		 * <br><br>
		 * @since 1.1.0
		 */
		@Override
		public ResponseHandler resolve(ProxyInvocationConfiguration config) {
	
			return ResponseHandlers.BASIC;
		}
	};
}
