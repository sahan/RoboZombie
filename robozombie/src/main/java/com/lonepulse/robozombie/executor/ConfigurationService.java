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

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.lonepulse.robozombie.proxy.Zombie;
import com.lonepulse.robozombie.proxy.Zombie.Configuration;

/**
 * <p>This is a concrete implementation of {@link ConfigurationManager} which manages request execution 
 * configurations defined as instances of {@link Zombie.Configuration}.</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
final class ConfigurationService implements ConfigurationManager {

	
	/**
	 * <p>The <i>out-of-the-box</i> configuration for an instance of {@link HttpClient} which will be used for 
	 * executing all endpoint requests. Below is a detailed description of all configured properties.</p> 
	 * <br>
	 * <ul>
	 * <li>
	 * <p><b>HttpClient</b></p>
	 * <br>
	 * <p>It registers two {@link Scheme}s:</p>
	 * <br>
	 * <ol>
	 * 	<li><b>HTTP</b> on port <b>80</b> using sockets from {@link PlainSocketFactory#getSocketFactory}</li>
	 * 	<li><b>HTTPS</b> on port <b>443</b> using sockets from {@link SSLSocketFactory#getSocketFactory}</li>
	 * </ol>
	 * 
	 * <p>It uses a {@link ThreadSafeClientConnManager} with the following parameters:</p>
	 * <br>
	 * <ol>
	 * 	<li><b>Redirecting:</b> enabled</li>
	 * 	<li><b>Connection Timeout:</b> 30 seconds</li>
	 * 	<li><b>Socket Timeout:</b> 30 seconds</li>
	 * 	<li><b>Socket Buffer Size:</b> 12000 bytes</li>
	 * 	<li><b>User-Agent:</b> via <code>System.getProperty("http.agent")</code></li>
	 * </ol>
	 * </li>
	 * </ul>
	 * @return the instance of {@link HttpClient} which will be used for request execution
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public Configuration getDefault() {
		
		return new Configuration() {

			@Override
			public HttpClient httpClient() {
				
				try {
				
					HttpParams params = new BasicHttpParams();
					HttpClientParams.setRedirecting(params, true);
					HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
					HttpConnectionParams.setSoTimeout(params, 30 * 1000);
					HttpConnectionParams.setSocketBufferSize(params, 12000);
			        HttpProtocolParams.setUserAgent(params, System.getProperty("http.agent"));
			        
			        SchemeRegistry schemeRegistry = new SchemeRegistry();
			        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

			        ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);

			        return new DefaultHttpClient(manager, params);
				}
				catch(Exception e) {
					
					throw new ConfigurationFailedException(e);
				}
			}
		};
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration register(Class<?> endpointClass) {
		
		try {
			
			if(endpointClass.isAnnotationPresent(com.lonepulse.robozombie.annotation.Config.class)) {
				
				Configuration configuration = endpointClass.getAnnotation(
					com.lonepulse.robozombie.annotation.Config.class).value().newInstance();
				
				HttpClient httpClient = configuration.httpClient();
				HttpClientDirectory.INSTANCE.bind(endpointClass, httpClient); //currently the only configurable property
				
				return configuration;
			}
			else {
				
				HttpClientDirectory.INSTANCE.bind(endpointClass, HttpClientDirectory.DEFAULT);
				
				return new Configuration(){};
			}
		}
		catch(Exception e) {
			
			throw new ConfigurationFailedException(endpointClass, e);
		}
	}
}
