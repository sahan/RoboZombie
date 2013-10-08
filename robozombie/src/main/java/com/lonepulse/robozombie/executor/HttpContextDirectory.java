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

import com.lonepulse.robozombie.util.Directory;

/**
 * <p>A <b>singleton</b> implementation of the {@link Directory} policy 
 * which allows registering and retrieving {@link HttpContext}s for stateful 
 * endpoints.
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
enum HttpContextDirectory implements Directory<Class<?>, HttpContext> {
	
	/**
	 * <p>The instance of {@link HttpContextDirectory} which allows 
	 * access to the {@link HttpContext} cache.
	 * 
	 * @since 1.1.0
	 */
	INSTANCE;

	
	/**
	 * <p>The {@link Map} of {@link HttpContext}s which are maintained for 
	 * stateful endpoints. 
	 */
	private static Map<Class<?>, HttpContext> CONTEXTS = new HashMap<Class<?>, HttpContext>();

	/**
	 * <p>The instance of {@link HttpContextFactory} which creates new instance of 
	 * {@link HttpContext}s.
	 */
	private final HttpContextFactory httpContextFactory = new HttpContextFactory();
	
	
	/**
	 * See {@link Directory#put(Class, Object)}.
	 */
	@Override
	public synchronized HttpContext put(Class<?> entryKey, HttpContext entryValue) {
		
		if(!CONTEXTS.containsKey(entryKey))
			CONTEXTS.put(entryKey, entryValue);
		
		return entryValue;
	}

	/**
	 * See {@link Directory#post(Class, Object)}.
	 */
	@Override
	public synchronized HttpContext post(Class<?> entryKey, HttpContext entryValue) {
		
		return CONTEXTS.put(entryKey, entryValue);
	}

	/**
	 * See {@link Directory#get(Class)}.
	 */
	@Override
	public synchronized HttpContext get(Class<?> entryKey) {
		
		HttpContext httpContext = CONTEXTS.get(entryKey); 
		
		return (httpContext == null)? 
					put(entryKey, httpContextFactory.newInstance()) :httpContext;
	}

	/**
	 * See {@link Directory#delete(Class)}.
	 */
	@Override
	public synchronized HttpContext delete(Class<?> entryKey) {
		
		return CONTEXTS.remove(entryKey);
	}
}
