package com.lonepulse.robozombie.inject;

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

import java.util.Map;
import java.util.HashMap;

import com.lonepulse.robozombie.util.Directory;

/**
 * <p>A directory of <b>dynamic proxies</b> which were created for endpoint interface definitions.</p> 
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
enum EndpointDirectory implements Directory<Class<?>, Object> {
	
	/**
	 * <p>The instance of {@link EndpointDirectory} which caches all endpoint proxies. They can be stored or 
	 * retrieved using the {@link Class} of their endpoint definition interface.</p>
	 * 
	 * @since 1.1.0
	 */
	INSTANCE;

	
	private static Map<String, Object> ENDPOINTS = new HashMap<String, Object>();
	
	
	/**
	 * See {@link Directory#put(Class, Object)}.
	 */
	@Override
	public synchronized Object put(Class<?> endpointClass, Object proxy) {
		
		String className = endpointClass.getName();
		
		if(!ENDPOINTS.containsKey(className)) {
			
			ENDPOINTS.put(className, proxy);
		}
		
		return ENDPOINTS.get(className);
	}

	/**
	 * See {@link Directory#post(Class, Object)}.
	 */
	@Override
	public synchronized Object post(Class<?> endpointClass, Object proxy) {
		
		return ENDPOINTS.put(endpointClass.getName(), proxy);
	}

	/**
	 * See {@link Directory#get(Class)}.
	 */
	@Override
	public synchronized Object get(Class<?> endpointClass) {
		
		return ENDPOINTS.get(endpointClass.getName());
	}

	/**
	 * See {@link Directory#delete(Class)}.
	 */
	@Override
	public synchronized Object delete(Class<?> endpointClass) {
		
		return ENDPOINTS.remove(endpointClass.getName());
	}
}
