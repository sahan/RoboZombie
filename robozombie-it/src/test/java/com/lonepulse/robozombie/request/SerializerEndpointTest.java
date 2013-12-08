package com.lonepulse.robozombie.request;

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
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

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
 * <p>Performs <b>Unit Testing</b> on the proxy of {@link SerializerEndpoint}.</p>
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
public class SerializerEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Bite
	private SerializerEndpoint serializerEndpoint;
	
	
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
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));

		expectedException.expect(Is.isA(InvocationException.class));
		
		serializerEndpoint.gsonUnavailable(new User(1, "Waver", "Velvet", 16, false));
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
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		serializerEndpoint.simpleXmlUnavailable(new User(1, "Emiya", "Kiritsugu", 31, false));
	}
}
