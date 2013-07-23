package com.lonepulse.robozombie.test.endpoint;

/*
 * #%L
 * ZombieLink
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
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.lonepulse.robozombie.core.annotation.Bite;
import com.lonepulse.robozombie.core.inject.Zombie;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * <p>Performs <b>Unit Testing</b> on the proxy of {@link MockEndpoint}.
 * 
 * @category test
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@RunWith(RobolectricTestRunner.class)
public class MockEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Bite
	private MockEndpoint mockEndpoint;
	
	
	/**
	 * <p>Sets up the test case by performing endpoint injection on {@link #mockEndpoint}.
	 * 
	 * @throws java.lang.Exception
	 * 			if the test case setup or endpoint injection failed
	 */
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	@Test
	public final void testState() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String cookie = "JSESSIONID=1111";
		
		stubFor(get(urlEqualTo("/stateful"))
				.willReturn(aResponse().withStatus(200)
				.withHeader("Set-Cookie", cookie)
				.withBody("empty")));
		
		StringBuilder cookieHeader = new StringBuilder();
		
		mockEndpoint.stateful(cookieHeader);
		assertEquals(cookie, cookieHeader.toString()); //the server should return a cookie to store 
		
		mockEndpoint.stateful(new StringBuilder()); //a second request should submit the cookie
		
		verify(getRequestedFor(urlMatching("/stateful")).withHeader("Cookie", matching(cookie)));
	}
}
