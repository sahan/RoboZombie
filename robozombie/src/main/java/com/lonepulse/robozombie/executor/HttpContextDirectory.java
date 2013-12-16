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

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.lonepulse.robozombie.AbstractGenericFactory;
import com.lonepulse.robozombie.Directory;
import com.lonepulse.robozombie.GenericFactory;
import com.lonepulse.robozombie.RoboZombieRuntimeException;

/**
 * <p>A registry of {@link HttpContext}s which maintain endpoint <i>state</i>.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
enum HttpContextDirectory implements Directory<Class<?>, HttpContext> {
	
	
	/**
	 * <p>The {@link HttpContextDirectory} which caches the {@link HttpContext}s which can be bound 
	 * and looked up using their endpoints.</p>
	 * 
	 * @since 1.3.0
	 */
	INSTANCE;

	
	private static final GenericFactory<Void, HttpContext, RoboZombieRuntimeException> 
	CONTEXT_FACTORY = new AbstractGenericFactory<Void, HttpContext, RoboZombieRuntimeException>() {
		
		@Override
		public HttpContext newInstance() {
			
			try {
				
				CookieStore cookieStore = new BasicCookieStore();
				HttpContext httpContext = new BasicHttpContext();
				
				httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
				
				return httpContext;
			}
			catch(Exception e) {
				
				throw new RoboZombieRuntimeException(e);
			}
		}
	};
	
	private static final Map<String, HttpContext> CONTEXTS = new HashMap<String, HttpContext>();
	
	
	/**
	 * <p>Registers the given {@link HttpContext} under the specified endpoint's name. If an instance 
	 * of {@link HttpContext} is already registered under the endpoint, the existing instance will be 
	 * returned without being replaced by the given {@link HttpContext}.</p>
	 *
	 * @param endpoint
	 * 			the {@link Class} of the endpoint definition to which the {@link HttpContext} is bound
	 * <br><br>
	 * @param httpContext
	 * 			the {@link HttpContext} which is to be bound under the given endpoint definition
	 * <br><br>
	 * @return the {@link HttpContext} which was bound under the specified endpoint definition 
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	public synchronized HttpContext bind(Class<?> endpoint, HttpContext httpContext) {
		
		String name = endpoint.getName();
		
		if(!CONTEXTS.containsKey(name)) {
			
			CONTEXTS.put(name, httpContext);
		}
		
		return lookup(endpoint);
	}

	/**
	 * <p>Retrieves the bound {@link HttpContext} for the specified endpoint. If no {@link HttpContext} 
	 * exists, a new instance will be created, registered under the endpoint and returned.</p>
	 *
	 * @param endpoint
	 * 			the {@link Class} of the endpoint definition whose {@link HttpContext} is to be retrieved
	 * <br><br>
	 * @return the bound {@link HttpContext} or a new {@link HttpContext} if no instance was bound
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	public synchronized HttpContext lookup(Class<?> endpoint) {
		
		HttpContext httpContext = CONTEXTS.get(endpoint.getName());
		
		return (httpContext == null)? 
				bind(endpoint, CONTEXT_FACTORY.newInstance()) :httpContext;
	}
}
