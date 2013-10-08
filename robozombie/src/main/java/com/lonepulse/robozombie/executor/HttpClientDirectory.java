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

import org.apache.http.client.HttpClient;

import com.lonepulse.robozombie.annotation.Configuration;
import com.lonepulse.robozombie.inject.Zombie;
import com.lonepulse.robozombie.util.Directory;

/**
 * <p>A directory of {@link HttpClient}s which are configured to be used for a specific endpoint.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
enum HttpClientDirectory implements Directory<Class<?>, HttpClient> {
	
	/**
	 * <p>The instance of {@link HttpClientDirectory} which caches {@link HttpClient}s that are uniquely configured 
	 * for each endpoint. An {@link HttpClient} is stored or accessed using an endpoint {@link Class}.</p>
	 * 
	 * @since 1.2.4
	 */
	INSTANCE;
	
	
	private static Map<String, HttpClient> DIRECTORY  = new HashMap<String, HttpClient>();
	
	private static Map<String, String> ENDPOINT_CONFIGS = new HashMap<String, String>();
	
	/**
	 * <p>The default configuration for an {@link HttpClient} which will be used to executing endpoint requests if 
	 * no specialized configuration is provided.</p>
	 * 
	 * <p></p>
	 *  
	 * @since 1.2.4
	 */
	public static final HttpClient DEFAULT;
	
	static {
		
		DEFAULT = new Zombie.Configuration(){}.httpClient();
		DIRECTORY.put(Zombie.Configuration.class.getName(), DEFAULT);
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				HttpClientDirectory.shutdownAll();
			}
		}));
	}
	
	
	/**
	 * <p>Adds an instance of {@link HttpClient} to the directory keyed under the given {@link Class} of the endpoint 
	 * interface. If an {@link HttpClient} already exists under the given endpoint, <i>no attempt will be made to replace 
	 * the existing instance</i>.</p>
	 * 
	 * @param endpointClass
	 * 			the {@link Class} of the endpoint whose {@link HttpClient} is to be added to the directory
	 * <br><br>
	 * @param httpClient
	 * 			the {@link HttpClient} which is to be added to the directory under the given endpoint {@link Class}
	 * <br><br>
	 * @return the {@link HttpClient} which was added under the given endpoint {@link Class}
	 * <br><br> 
	 * @since 1.2.4
	 */
	@Override
	public synchronized HttpClient put(Class<?> endpointClass, HttpClient httpClient) {
		
		String configClassName = HttpClientDirectory.getConfigClassName(endpointClass);
		String endpointClassName = endpointClass.getName();
			
		if(!ENDPOINT_CONFIGS.containsKey(endpointClassName)) {
			
			ENDPOINT_CONFIGS.put(endpointClassName, configClassName);
		}
		
		if(!DIRECTORY.containsKey(configClassName)) {
			
			DIRECTORY.put(configClassName, httpClient);
		}
		
		return get(endpointClass);
	}

	/**
	 * <p>Adds an instance of {@link HttpClient} to the directory keyed under the given {@link Class} of the endpoint 
	 * interface. If an {@link HttpClient} already exists under the given endpoint class, <i>it will be replaced with 
	 * the given instance</i>.</p>
	 * 
	 * @param endpointClass
	 * 			the {@link Class} of the endpoint whose {@link HttpClient} is to be added to the directory
	 * <br><br>
	 * @param httpClient
	 * 			the {@link HttpClient} which is to be added to the directory under the given endpoint {@link Class}
	 * <br><br>
	 * @return the given {@link HttpClient} which replaces any instance which may have existed under its key
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public synchronized HttpClient post(Class<?> endpointClass, HttpClient httpClient) {
		
		String configClassName = HttpClientDirectory.getConfigClassName(endpointClass);
		
		ENDPOINT_CONFIGS.put(endpointClass.getName(), configClassName);
		DIRECTORY.put(configClassName, httpClient);
		
		return get(endpointClass);
	}

	/**
	 * <p>Retrieves the {@link HttpClient} which was added under the given endpoint. If no instance was keyed for this 
	 * endpoint {@link Class}, {@code null} is returned.</p>
	 *  
	 * @param endpointClass
	 * 			the {@link Class} of the endpoint whose {@link HttpClient} is to be retrieved
	 * <br><br>
	 * @return the {@link HttpClient} which was added under the given endpoint, else the pre-configured instance of 
	 * 		   (see {@link Zombie.Configuration#httpClient()}) if not {@link HttpClient} can be found for the endpoint
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public synchronized HttpClient get(Class<?> endpointClass) {
		
		HttpClient httpClient = DIRECTORY.get(ENDPOINT_CONFIGS.get(endpointClass.getName()));
		return httpClient == null? DEFAULT :httpClient;
	}

	/**
	 * <p>Removes the {@link HttpClient} which was added under the given endpoint {@link Class} after 
	 * its connection manager has been shutdown.</p>
	 * 
	 * <p><b>Note</b> that this may affect other endpoints which share this {@link HttpClient}.</p>
	 * 
	 * @param endpointClass
	 * 			the {@link Class} of the endpoint whose entry is to be removed from this directory
	 * <br><br>
	 * @return the {@link HttpClient} which existed under the given endpoint {@link Class}, else {@code null} 
	 * 		   if not {@link HttpClient} existed or if the shutdown operation on the client failed
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public synchronized HttpClient delete(Class<?> endpointClass) {
		
		String key = ENDPOINT_CONFIGS.remove(endpointClass.getName());
		HttpClient httpClient = DIRECTORY.get(key);
		
		if(httpClient != null) {
			
			httpClient.getConnectionManager().shutdown();
		}
		
		return DIRECTORY.remove(key);
	}
	
	private static String getConfigClassName(Class<?> endpointClass) {
		
		return endpointClass.isAnnotationPresent(Configuration.class)?
				endpointClass.getAnnotation(Configuration.class).value().getName() :Zombie.Configuration.class.getName();
	}
	
	private static void shutdownAll() {
		
		synchronized (DIRECTORY) {
			
			for (HttpClient httpClient : DIRECTORY.values()) {
				
				try {
					
					httpClient.getConnectionManager().shutdown();
				}
				catch(Exception e) {}
			}
		}
	}
}
