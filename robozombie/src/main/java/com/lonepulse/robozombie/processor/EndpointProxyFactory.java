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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.processor.executor.RequestExecutors;
import com.lonepulse.robozombie.processor.validator.Validators;
import com.lonepulse.robozombie.request.RequestBuilders;
import com.lonepulse.robozombie.response.parser.ResponseParsers;

/**
 * <p>This factory is used for creating dynamic proxies for communication 
 * endpoint interfaces that are annotated with {@link Endpoint}.</p>
 * 
 * @version 2.1.2
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum EndpointProxyFactory implements ProxyFactory {
	
	/**
	 * <p>The single instance of the factory which caters to all endpoint 
	 * injection requirements by creating endpoint proxies.
	 * 
	 * @since 2.1.2
	 */
	INSTANCE;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized <T> T create(final Class<T> endpointClass) {

		try {
		
			final ProxyInvocationConfiguration.Builder builder = new  ProxyInvocationConfiguration.Builder()
			.setEndpointClass(endpointClass);
			
			//1.Validate the endpoint interface and create the URI
			final URI uri = (URI) Validators.ENDPOINT.validate(builder.build());
			
			T endpointProxy = endpointClass.cast(Proxy.newProxyInstance(endpointClass.getClassLoader(), 
												 new Class<?>[] {endpointClass} , 
												 new InvocationHandler() {
				
				@Override
				public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
					
					//2.Continue to construct the ProxyInvocationConfiguration
					ProxyInvocationConfiguration config = builder
					.setEndpointClass(endpointClass)
					.setUri(uri)
					.setProxy(proxy)
					.setRequest(method)
					.setRequestArgs(args)
					.build();
					
					//3.Create the request
					HttpRequestBase httpRequestBase 
						= RequestBuilders.RESOLVER.resolve(config).build(config);
					
					//4.Execute the HttpRequestBase either asynchronously or synchronously
					HttpResponse httpResponse 
						= RequestExecutors.RESOLVER.resolve(config).execute(httpRequestBase, config);

					if(httpResponse == null) return null; //request is asynchronous 
						
					//5.Parse the received HttpResponse and return the entity
					return ResponseParsers.RESOLVER.resolve(config).parse(httpResponse, config);
				}
			}));
			
			return endpointProxy;
		}
		catch(Exception e) {
			
			throw new ProxyFactoryException(getClass(), e);
		}
	}
}
