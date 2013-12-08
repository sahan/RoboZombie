package com.lonepulse.robozombie.response;

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
import com.lonepulse.robozombie.inject.InvocationException;
import com.lonepulse.robozombie.inject.Zombie;
import com.lonepulse.robozombie.model.User;

/**
 * <p>Performs <b>Unit Testing</b> on the proxy of {@link DeserializerEndpoint}.</p>
 * 
 * @category test
 * <br><br>
 * @version 1.1.1
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@RunWith(RobolectricTestRunner.class)
public class DeserializerEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Bite
	private DeserializerEndpoint deserializerEndpoint;
	
	
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for integration with the Gson library when it's not detected on the classpath.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test 
	public final void testGsonUnavailable() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/gsonunavailable";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withBody(new User(1, "Illyasviel", "Von Einzbern", 15, false).toString())
				.withStatus(200)));

		expectedException.expect(Is.isA(InvocationException.class));
		
		deserializerEndpoint.gsonUnavailable();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
	
	/**
	 * <p>Test for integration with the Simple-XML library when it's not detected on the classpath.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test 
	public final void testSimpleXmlUnavailable() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/simplexmlunavailable";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withBody(new User(1, "Kotomine", "Kirei", 34, false).toString())
				.withStatus(200)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		deserializerEndpoint.simpleXmlUnavailable();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
}
