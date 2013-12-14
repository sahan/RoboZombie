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
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import com.lonepulse.robozombie.annotation.PathParam;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.proxy.InvocationException;
import com.lonepulse.robozombie.proxy.Zombie;

/**
 * <p>Performs unit testing on {@link PathEndpoint}.</p>
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
public class PathEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Bite
	private PathEndpoint pathEndpoint;
	
	
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for a {@link Request} with a subpath.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testSubpath() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/subpath", body = "hello";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));
		
		assertEquals(body, pathEndpoint.subpath());
		verify(getRequestedFor(urlMatching(subpath)));
	}
	
	/**
	 * <p>Test for a RESTful {@link Request} with a subpath having {@link PathParam}s.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testRestfulSubpathWithParams() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/restfulsubpathwithparam/\\S+", body = "hello", 
			   id = "doctorwho", url = "/restfulsubpathwithparam/" + id;
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));
		
		assertEquals(body, pathEndpoint.restfulSubpathWithParam(id));
		verify(getRequestedFor(urlEqualTo(url)));
	}
	
	/**
	 * <p>Test for {@link PathEndpoint#restfulSubpathWithIllegalParamType(Long)}.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testRestfulSubpathWithIllegalParamType() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/restfulsubpathwithillegalparamtype/\\S+", body = "hello";
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		assertNull(pathEndpoint.restfulSubpathWithIllegalParamType(1L));
	}
}
