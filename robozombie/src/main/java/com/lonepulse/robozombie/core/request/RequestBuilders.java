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


import java.lang.reflect.Method;

import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.rest.annotation.Rest;
import com.lonepulse.robozombie.rest.request.RestfulRequestBuilder;
import com.lonepulse.robozombie.util.Resolver;

/**
 * <p>Exposes all available {@link RequestBuilder}s and delegates communication. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum RequestBuilders implements RequestBuilder {

	
	/**
	 * See {@link BasicRequestBuilder}.
	 * 
	 * @since 1.1.0
	 */
	BASIC(new BasicRequestBuilder()),
	
	/**
	 * See {@link RestfulRequestBuilder}.
	 * 
	 * @since 1.1.0
	 */
	RESTFUL(new RestfulRequestBuilder());
	
	
	/**
	 * The exposed instance of {@link RequestBuilder}.
	 */
	private RequestBuilder requestBuilder;

	
	/**
	 * <p>Instantiates {@link #requestBuilder} with the give instance of 
	 * {@link RequestBuilder}.
	 * 
	 * @param requestBuilder
	 * 			the associated instance of {@link RequestBuilder}
	 */
	private RequestBuilders(RequestBuilder requestBuilder) {
	
		this.requestBuilder = requestBuilder;
	}

	/**
	 * See {@link RequestBuilder#build(ProxyInvocationConfiguration)}.
	 * 
	 * @since 1.1.0
	 */
	@Override
	public HttpRequestBase build(ProxyInvocationConfiguration config) {

		return this.requestBuilder.build(config);
	}
	
	
	/**
	 * <p>The instance of {@link Resolver} which retrieves suitable 
	 * {@link RequestBuilder}s for a given endpoint request.
	 * 
	 * @since 1.1.0
	 */
	public static final Resolver<ProxyInvocationConfiguration, RequestBuilder> RESOLVER 
		= new Resolver<ProxyInvocationConfiguration, RequestBuilder>() {
	
		/**
		 * <p>Takes an endpoint {@link Method} and discovers a suitable 
		 * {@link RequestBuilder}.
		 * 
		 * @param config
		 * 			the {@link ProxyInvocationConfiguration} for resolving 
		 * 			the associated {@link RequestBuilder}
		 * 
		 * @return a {@link RestfulRequestBuilder} if the request is RESTful, 
		 * 		   else a {@link BasicRequestBuilder}.
		 * 
		 * <br><br>
		 * @since 1.1.0
		 */
		@Override
		public RequestBuilder resolve(ProxyInvocationConfiguration config) {
	
			return config.getRequest().isAnnotationPresent(Rest.class)? 
						RequestBuilders.RESTFUL.requestBuilder 
							:RequestBuilders.BASIC.requestBuilder;
		}
	};
}
