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

import org.apache.http.protocol.HttpContext;

/**
 * <p>A registry of {@link HttpContext}s which maintain endpoint <i>state</i>.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
enum HttpContextDirectory {
	
	/**
	 * <p>The instance of {@link HttpContextDirectory} which exposes the {@link HttpContext} which 
	 * can be bound and looked up using their endpoints.</p>
	 * 
	 * @since 1.2.4
	 */
	INSTANCE;

	
	private static Map<String, HttpContext> CONTEXTS = new HashMap<String, HttpContext>();
	
	private final HttpContextFactory httpContextFactory = new HttpContextFactory();
	
	
	/**
	 * <p>Registers the given {@link HttpContext} under the specified endpoint's name. If an instance 
	 * of {@link HttpContext} is already registered under the endpoint, the existing will be returned 
	 * without being replaced by the given {@link HttpContext}.</p>
	 *
	 * @param endpoint
	 * 			the {@link Class} of the endpoint definition for which the {@link HttpContext} is bound
	 * <br><br>
	 * @param httpContext
	 * 			the {@link HttpContext} which is to be bound under the given endpoint definition
	 * <br><br>
	 * @return the bound {@link HttpContext} for the specified endpoint definition 
	 * <br><br>
	 * @since 1.2.4
	 */
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
	 * @since 1.2.4
	 */
	public synchronized HttpContext lookup(Class<?> endpoint) {
		
		HttpContext httpContext = CONTEXTS.get(endpoint.getName());
		
		return (httpContext == null)? 
				bind(endpoint, httpContextFactory.newInstance()) :httpContext;
	}
}
