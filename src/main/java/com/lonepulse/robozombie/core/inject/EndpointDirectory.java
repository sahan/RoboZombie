package com.lonepulse.robozombie.core.inject;

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

/**
 * <p>A <b>singleton</b> implementation of the {@link ClassDirectory} policy 
 * which allows registering and retrieving <b>endpoint proxies</b>. 
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum EndpointDirectory implements ClassDirectory {
	
	
	/**
	 * <p>The only instance of {@link EndpointDirectory} which allows the clients to access its services. 
	 */
	INSTANCE;

	/**
	 * <p>The {@link Map} of endpoint interface implementations which are maintained as <i>Singletons</i>. 
	 */
	private static volatile Map<Class<?>, Object> ENDPOINTS = new HashMap<Class<?>, Object>();
	
	
	/**
	 * See {@link ClassDirectory#put(Class, Object)}.
	 */
	@Override
	public void put(Class<?> entryKey, Object entryValue) {
		
		if(!ENDPOINTS.containsKey(entryKey))
			ENDPOINTS.put(entryKey, entryValue);
	}

	/**
	 * See {@link ClassDirectory#post(Class, Object)}.
	 */
	@Override
	public Object post(Class<?> entryKey, Object entryValue) {
		
		return entryKey.cast(ENDPOINTS.put(entryKey, entryValue));
	}

	/**
	 * See {@link ClassDirectory#get(Class)}.
	 */
	@Override
	public Object get(Class<?> entryKey) {
		
		return entryKey.cast(ENDPOINTS.get(entryKey));
	}

	/**
	 * See {@link ClassDirectory#delete(Class)}.
	 */
	@Override
	public Object delete(Class<?> entryKey) {
		
		return entryKey.cast(ENDPOINTS.remove(entryKey));
	}
}
