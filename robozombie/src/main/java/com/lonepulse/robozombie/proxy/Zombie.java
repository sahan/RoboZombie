package com.lonepulse.robozombie.proxy;

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

import static com.lonepulse.robozombie.util.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import android.util.Log;

import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.annotation.Config;
import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.executor.ConfigurationFailedException;
import com.lonepulse.robozombie.executor.RequestExecutors;
import com.lonepulse.robozombie.util.Fields;

/**
 * <p>An animated corpse which spreads the {@link Endpoint} infection via a {@link Bite}. Used for 
 * <b>injecting</b> concrete implementations of endpoint interface definitions. Place an @{@link Bite} 
 * annotation on all instance properties which are endpoints and invoke {@link Zombie#infect(Object)}.</p> 
 *  
 * @version 1.3.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class Zombie {
	
	/**
	 * <p>The <b>default configuration</b> which is used for endpoint request execution. The configured 
	 * properties pertain to the <a href="http://hc.apache.org">Apache HTTP Components</a> library which 
	 * provides the foundation for network communication.</p>
	 * 
	 * <p>Configurations can be revised for each {@link Endpoint} using <b>@Config</b> by specifying the 
	 * {@link Class} of a {@link Config} extension. Simply override the required template methods and provide 
	 * a <b>new instance</b> of the desired property. For example, override {@link Config#httpClient()} to 
	 * return a custom {@link HttpClient} which might be configured with alternative {@link Scheme}s, timeouts ..etc.</p>
	 * 
	 * <p>For more information on configuring your own instance of {@link HttpClient} refer the 
	 * <a href="http://hc.apache.org/httpcomponents-client-4.2.x/tutorial/html/index.html">Apache HC Tutorial</a>.</p>
	 * 
	 * <p><b>Note</b> that all extensions must expose a default non-parameterized constructor.</p>
	 *  
	 * @version 1.2.0
	 * <br><br>
	 * @since 1.2.4
	 * <br><br>
	 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	public abstract static class Configuration {
		
		
		private static final Configuration DEFAULT = RequestExecutors.CONFIGURATION.getDefault();
		
		
		/**
		 * <p>The <i>out-of-the-box</i> configuration for an instance of {@link HttpClient} which will be 
		 * used for executing all endpoint requests.</p> 
		 * 
		 * <p>It registers two {@link Scheme}s:</p>
		 * 
		 * <ol>
		 * 	<li><b>HTTP</b> on port <b>80</b> using sockets from {@link PlainSocketFactory#getSocketFactory}</li>
		 * 	<li><b>HTTPS</b> on port <b>443</b> using sockets from {@link SSLSocketFactory#getSocketFactory}</li>
		 * </ol>
		 * 
		 * <p>It uses a {@link ThreadSafeClientConnManager} with the following parameters:</p>
		 * <ol>
		 * 	<li><b>Redirecting:</b> enabled</li>
		 * 	<li><b>Connection Timeout:</b> 30 seconds</li>
		 * 	<li><b>Socket Timeout:</b> 30 seconds</li>
		 * 	<li><b>Socket Buffer Size:</b> 12000 bytes</li>
		 * 	<li><b>User-Agent:</b> via <code>System.getProperty("http.agent")</code></li>
		 * </ol>
		 *
		 * @return the instance of {@link HttpClient} which will be used for request execution
		 * <br><br>
		 * @throws ConfigurationFailedException
		 * 			if the default configuration for the {@link HttpClient} failed to be instantiated
		 * <br><br>
		 * @since 1.2.4
		 * <br><br>
		 * @see <a href="http://hc.apache.org/httpcomponents-client-4.2.x/tutorial/html/index.html">Apache HC Tutorial</a>
		 */
		public HttpClient httpClient() {
			
			return DEFAULT.httpClient();
		}
	}
	
	
	private Zombie() {}
	
	/**
	 * <p>Accepts an object and scans it for {@link Bite} annotations. If found, a <b>thread-safe proxy</b> 
	 * for the endpoint interface will be injected.</p>
	 * <br>
	 * <b>Usage:</b>
	 * <br><br>
	 * <ul>
	 * <li>
	 * <h5>Property Injection</h5>
	 * <pre>
	 * <code>@Bite
	 * TwitterEndpoint twitterEndpoint;
	 * {
	 * &nbsp; &nbsp; Zombie.infect(this);
	 * }
	 * </code>
	 * </pre>
	 * </li>
	 * <li>
	 * <h5>Setter Injection</h5>
	 * <pre>
	 * <code>
	 * private TwitterEndpoint twitterEndpoint;
	 * </code>
	 * <code>@Bite
	 * public void setTwitterEndpoint(TwitterEndpoint twitterEndpoint) {
	 * 
	 * &nbsp; &nbsp; this.twitterEndpoint = twitterEndpoint;
	 * }
	 * </code>
	 * </pre>
	 * </li>
	 * </ul>
	 * 
	 * @param victim
	 * 			an object with endpoint references marked to be <i>bitten</i> and infected 
	 * <br><br>
	 * @param moreVictims
	 * 			more unsuspecting objects with endpoint references to be infected
	 * <br><br>
	 * @throws NullPointerException
	 * 			if the object supplied for endpoint injection is {@code null} 
	 * <br><br>
	 * @since 1.2.4
	 */
	public static void infect(Object victim, Object... moreVictims) {
		
		assertNotNull(victim);
		
		List<Object> injectees = new ArrayList<Object>();
		injectees.add(victim);
		
		if(moreVictims != null && moreVictims.length > 0) {
			
			injectees.addAll(Arrays.asList(moreVictims));
		}
		
		Class<?> endpointInterface = null;
		
		for (Object injectee : injectees) {
		
			Class<?> type = injectee.getClass();
			
			do {
				
				for (Field field : Fields.in(type).annotatedWith(Bite.class)) {
					
					try {
						
						endpointInterface = field.getType();
						Object proxyInstance = EndpointProxyFactory.INSTANCE.create(endpointInterface); 
						
						try { //1.Simple Field Injection 
							
							field.set(injectee, proxyInstance);
						}
						catch (IllegalAccessException iae) { //2.Setter Injection 
							
							String fieldName = field.getName();
							String mutatorName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
							
							try {
							
								Method mutator = injectee.getClass().getDeclaredMethod(mutatorName, endpointInterface);
								mutator.invoke(injectee, proxyInstance);
							}
							catch (NoSuchMethodException nsme) { //3.Forced Field Injection
								
								field.setAccessible(true);
								field.set(injectee, proxyInstance);
							}
						}
					} 
					catch (Exception e) {
						
						Log.e(Zombie.class.getName(), new StringBuilder()
						.append("Failed to inject the endpoint proxy instance of type ")
						.append(endpointInterface.getName())
						.append(" on property ")
						.append(field.getName())
						.append(" at ")
						.append(injectee.getClass().getName())
						.append(". ").toString(), e);
					}
				}
				
				type = type.getSuperclass();
			}
			while(!type.equals(Object.class));
		}
	}
}
