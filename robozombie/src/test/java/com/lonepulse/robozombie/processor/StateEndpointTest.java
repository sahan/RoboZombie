package com.lonepulse.robozombie.processor;

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
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.annotation.Stateful;
import com.lonepulse.robozombie.proxy.Zombie;

/**
 * <p>Performs unit testing on {@link StateEndpoint}.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @category test
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
@RunWith(RobolectricTestRunner.class)
public class StateEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Bite
	private StateEndpoint stateEndpoint;
	
	
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for {@link Stateful} requests.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testState() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String cookie = "JSESSIONID=1111";
		
		stubFor(get(urlEqualTo("/stateful"))
				.willReturn(aResponse()
				.withStatus(200)
				.withHeader("Set-Cookie", cookie)
				.withBody("empty")));
		
		StringBuilder cookieHeader = new StringBuilder();
		
		stateEndpoint.stateful(cookieHeader);
		assertEquals(cookie, cookieHeader.toString()); //the server should return a cookie to store 
		
		stateEndpoint.stateful(new StringBuilder()); //a second request should submit the cookie
		
		verify(getRequestedFor(urlMatching("/stateful"))
			   .withHeader("Cookie", matching(cookie)));
	}
}
