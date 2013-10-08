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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.lonepulse.robozombie.annotation.Configuration;
import com.lonepulse.robozombie.inject.MockEndpoint;
import com.lonepulse.robozombie.inject.Zombie;

/**
 * <p>Performs <b>Unit Testing</b> on the instance of {@link HttpClientDirectory}.</p>
 * 
 * @category test
 * <br><br>
 * @version 1.1.1
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class HttpClientDirectoryTest {
	

	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private HttpClient client1;
	private HttpClient client2;
	private HttpClient client3;
	
	/**
	 * <p>Sets up the test case by instantiating three instances of the default {@link HttpClient} configuration</p>. 
	 */
	@Before
	public void setUp() {
		
		this.client1 = new Zombie.Configuration(){}.httpClient();
		this.client2 = new Zombie.Configuration(){}.httpClient();
		this.client3 = new Zombie.Configuration(){}.httpClient();
	}
	
	/**
	 * <p>Test for directory integrity by trying to add a custom {@link HttpClient} with an endpoint 
	 * which does not use an @{@link Configuration} annotation.</p>
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testIntegrity() {
		
		HttpClientDirectory.INSTANCE.put(MockEndpoint.class, client1); //custom configuration absent on MockEndpoint
		assertTrue(HttpClientDirectory.INSTANCE.get(MockEndpoint.class) == HttpClientDirectory.DEFAULT);
	}
	
	/**
	 * <p>Test for switching the {@link HttpClient} of an endpoint which has already been added.</p>
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testSwitching() {
		
		HttpClientDirectory.INSTANCE.post(ConfigEndpoint.class, client1);
		assertTrue(HttpClientDirectory.INSTANCE.get(ConfigEndpoint.class) == client1);
		
		HttpClientDirectory.INSTANCE.post(ConfigEndpoint.class, client2);
		assertTrue(HttpClientDirectory.INSTANCE.get(ConfigEndpoint.class) == client2);
	}
	
	/**
	 * <p>Test for shutting down the connection manager of an {@link HttpClient} when it is removed.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testShutdown() throws ClientProtocolException, IOException {
		
		String subpath = "/test", body = "ghostintheshell";
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));
		
		HttpClientDirectory.INSTANCE.post(ConfigEndpoint.class, client3);
		assertTrue(HttpClientDirectory.INSTANCE.get(ConfigEndpoint.class) == client3);
		
		HttpHost host = new HttpHost("0.0.0.0", 8080, "http");
		HttpGet request = new HttpGet(subpath);
		
		String response = EntityUtils.toString(client3.execute(host, request).getEntity());
		assertEquals(body, response);
		
		HttpClientDirectory.INSTANCE.delete(ConfigEndpoint.class);
		
		expectedException.expect(Is.isA(IllegalStateException.class));
		client3.execute(host, request).getEntity();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		assertNull(HttpClientDirectory.INSTANCE.get(ConfigEndpoint.class));
	}
}
