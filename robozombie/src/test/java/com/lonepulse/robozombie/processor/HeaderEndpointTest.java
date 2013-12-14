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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.annotation.Header;
import com.lonepulse.robozombie.annotation.Headers;
import com.lonepulse.robozombie.proxy.InvocationException;
import com.lonepulse.robozombie.proxy.Zombie;

/**
 * <p>Performs unit testing on {@link HeaderEndpoint}.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @category test
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@RunWith(RobolectricTestRunner.class)
public class HeaderEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Bite
	private HeaderEndpoint headerEndpoint;
	
	
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for a request {@link Header}.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testRequestHeader() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/requestheader", header = "mobile";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		headerEndpoint.requestHeader(header);
		
		verify(getRequestedFor(urlMatching(subpath))
			   .withHeader("User-Agent", matching(header)));
	}
	
	/**
	 * <p>Test for {@link HeaderEndpoint#requestHeader(String)} with a <i>skipped header</i>.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testRequestHeaderSkip() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/requestheaderskip";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody("hello")));
		
		headerEndpoint.requestHeaderSkip(null);
		verify(getRequestedFor(urlMatching(subpath)));
	}
	
	/**
	 * <p>Test for {@link HeaderEndpoint#requestHeaderTypeError(int)}.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testRequestHeaderTypeError() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/requestheadertypeerror";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody("hello")));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		assertNull(headerEndpoint.requestHeaderTypeError(512));
	}

	/**
	 * <p>Tests response header retrieval with {@link Header}.</p> 
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testResponseHeader() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
	
		String subpath = "/responseheader", body = "hello", 
			   header = "Server", headerValue = "Jetty(9.0.x)";
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withHeader(header, headerValue)
				.withBody(body)));
		
		StringBuilder responseHeader = new StringBuilder();
		
		assertEquals(headerEndpoint.responseHeader(responseHeader), body);
		
		String server = responseHeader.toString();
		assertTrue(server != null);
		assertTrue(server.equals(headerValue));
	}
	
	/**
	 * <p>Test for {@link HeaderEndpoint#requestHeader(String)} with a <i>skipped header</i>.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testResponseHeaderSkip() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/responseheaderskip", body = "hello",
			   headerValue = "Wed, 01 Jan 2014 10:00:00 GMT";
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));
		
		assertEquals(headerEndpoint.responseHeaderSkip(headerValue), body);
		
		verify(getRequestedFor(urlMatching(subpath))
			   .withHeader("Expires", matching(headerValue)));
	}
	
	/**
	 * <p>Test for {@link Headers} and {@link Headers.Header}.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testHeaderSet() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/headerset";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		headerEndpoint.headerSet();
		
		verify(getRequestedFor(urlMatching(subpath))
				.withHeader("Accept", matching("application/json"))
				.withHeader("Accept-Charset", matching("utf-8")));
	}
}
