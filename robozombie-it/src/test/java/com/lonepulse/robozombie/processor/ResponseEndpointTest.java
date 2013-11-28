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
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.executor.RequestFailedException;
import com.lonepulse.robozombie.inject.Zombie;

/**
 * <p>Performs <b>Unit Testing</b> on the proxy of {@link ResponseEndpoint}.
 * 
 * @category test
 * <br><br>
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@RunWith(RobolectricTestRunner.class)
public class ResponseEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Bite
	private ResponseEndpoint responseEndpoint;
	
	
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for response failures.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testFailure() throws ParseException, IOException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/failure", content = "jabberwocky";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withBody(content)
				.withStatus(404)));
		
		try {
		
			responseEndpoint.failure();
			fail("Failed request did not throw a context aware <RequestFailedException>.");
		}
		catch(RequestFailedException rfe) {

			assertTrue(rfe.hasResponse());
			assertNotNull(rfe.getResponse());
			assertNotNull(rfe.getContext());
			assertEquals(content, EntityUtils.toString(rfe.getResponse().getEntity()));
		}
		
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
	
	/**
	 * <p>Test for successful responses without any content.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testNoContent() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/nocontent";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(204)));
		
		String response = responseEndpoint.noContent();
		
		assertNull(response);
		
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
	
	/**
	 * <p>Test for successful response indicating a content reset.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testResetContent() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/resetcontent";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(205)));
		
		String response = responseEndpoint.resetContent();
		
		assertNull(response);
		
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
}
