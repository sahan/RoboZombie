package com.lonepulse.robozombie.proxy;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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

import static com.lonepulse.robozombie.util.Assert.assertNotNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>This is an implementation of {@link ProxyFactory} which is used for constructing thread-safe 
 * proxies of endpoint definitions.</p>
 * 
 * @version 2.2.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
enum EndpointProxyFactory implements ProxyFactory {
	
	
	/**
	 * <p>The {@link EndpointProxyFactory} which creates thread-safe proxies of endpoint definitions.</p>
	 * 
	 * @since 1.1.0
	 */
	INSTANCE;
	
	
	private static final Map<String, Object> ENDPOINTS = Collections.synchronizedMap(new HashMap<String, Object>());
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized <T> T create(final Class<T> endpoint) {

		String proxyKey = assertNotNull(endpoint).getName();
		
		if(ENDPOINTS.containsKey(proxyKey)) {
			
			return endpoint.cast(ENDPOINTS.get(proxyKey));
		}
		
		final ProxyInvocation.Template template = new ProxyInvocation.Template(endpoint){};
		
		try {
			
			T endpointProxy = endpoint.cast(Proxy.newProxyInstance(
				endpoint.getClassLoader(), new Class<?>[] {endpoint} , new InvocationHandler() {
				
				@Override
				public Object invoke(final Object proxy, final Method method, final Object[] args) {

					return ProxyInvocation.newInstance(template, proxy, method, args).invoke();
				}
			}));
			
			ENDPOINTS.put(proxyKey, endpointProxy);
			
			return endpointProxy;
		}
		catch(Exception e) {
			
			throw new ProxyFactoryException(getClass(), e);
		}
	}
}
