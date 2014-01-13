package com.lonepulse.robozombie.processor;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.simpleframework.xml.core.Persister;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;
import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.model.User;
import com.lonepulse.robozombie.proxy.InvocationException;
import com.lonepulse.robozombie.proxy.Zombie;
import com.lonepulse.robozombie.response.AbstractDeserializer;
import com.lonepulse.robozombie.response.Deserializers;

/**
 * <p>Performs unit testing on {@link DeserializerEndpoint}.</p>
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
	 * <p>Test for {@link DeserializerEndpoint#responseError()}.</p>
	 *
	 * @since 1.3.0
	 */
	@Test 
	public final void testResponseError() {

		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/responseerror", body = "forbidden";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(403)
				.withBody(body)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		String deserializedContent = deserializerEndpoint.responseError();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		assertNull(deserializedContent);
	}
	
	/**
	 * <p>Test for {@link Deserializers#JSON}.
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testParseJson() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/json";
		
		User user = new User(1, "Tenzen", "Yakushiji", 300, true);
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(new Gson().toJson(user))));
		
		User deserializedUser = deserializerEndpoint.deserializeJson();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		
		assertEquals(user.getId(), deserializedUser.getId());
		assertEquals(user.getFirstName(), deserializedUser.getFirstName());
		assertEquals(user.getLastName(), deserializedUser.getLastName());
		assertEquals(user.getAge(), deserializedUser.getAge());
		assertEquals(user.isImmortal(), deserializedUser.isImmortal());
	}
	
	/**
	 * <p>Test for {@link Deserializers#JSON} with a generic type.</p>
	 * 
	 * @since 1.3.3
	 */
	@Test
	public final void testDeserializeJsonToGenericType() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/jsonarray";
		
		User user1 = new User(0, "Tenzen0", "Yakushiji0", 300, true);
		User user2 = new User(1, "Tenzen1", "Yakushiji1", 300, true);
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(new Gson().toJson(Arrays.asList(user1, user2)))));
		
		List<User> deserializedUsers = deserializerEndpoint.deserializeJsonToGenericType();
		
		verify(getRequestedFor(urlEqualTo(subpath)));

		for (int i = 0; i < deserializedUsers.size(); i++) {
			
			User user = deserializedUsers.get(i);
			
			assertEquals(i, user.getId());
			assertEquals("Tenzen" + String.valueOf(i), user.getFirstName());
			assertEquals("Yakushiji" + String.valueOf(i), user.getLastName());
			assertEquals(300, user.getAge());
			assertEquals(true, user.isImmortal());
		}
	}
	
	/**
	 * <p>Test for {@link Deserializers#XML}.
	 * 
	 * @throws Exception
	 * 			if the test terminated with an error
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testParseXml() throws Exception {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/xml";
		
		User user = new User(1, "Shiro", "Wretched-Egg", 17, true);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new Persister().write(user, baos);
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(baos.toString())));
		
		User deserializedUser = deserializerEndpoint.deserializeXml();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		
		assertEquals(user.getId(), deserializedUser.getId());
		assertEquals(user.getFirstName(), deserializedUser.getFirstName());
		assertEquals(user.getLastName(), deserializedUser.getLastName());
		assertEquals(user.getAge(), deserializedUser.getAge());
		assertEquals(user.isImmortal(), deserializedUser.isImmortal());
	}
	
	/**
	 * <p>Test for {@link DeserializerEndpoint#plain()}.
	 *
	 * @since 1.3.0
	 */
	@Test  
	public final void testPlain() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/raw", body = "SAO Nerve Gear";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withBody(body)));
		
		String responseContent = deserializerEndpoint.plain();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		assertEquals(body, responseContent);
	}
	
	/**
	 * <p>Test for custom {@link AbstractDeserializer}s.
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testParseCustom() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/custom", redacted = "<redacted>";
		
		User user = new User(1, "Felix", "Walken", 28, false);
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(new Gson().toJson(user))));
		
		User deserializedUser = deserializerEndpoint.deserializeCustom();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		
		assertEquals(user.getId(), deserializedUser.getId());
		assertEquals(redacted, deserializedUser.getFirstName());
		assertEquals(redacted, deserializedUser.getLastName());
		assertEquals(user.getAge(), deserializedUser.getAge());
		assertEquals(user.isImmortal(), deserializedUser.isImmortal());
	}
	
	/**
	 * <p>Test for detachment of the inherited deserializer.</p>
	 *
	 * @since 1.3.0
	 */
	@Test  
	public final void testDetachDeserializer() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		User user = new User(1, "Riza", "Hawkeye", 29, false);
		String subpath = "/detach", body = new Gson().toJson(user);
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withBody(body)));
		
		String responseContent = "";
		
		try {
			
			responseContent = deserializerEndpoint.detachDeserializer();
		}
		catch(Exception e) {
			
			fail("JSON deserialization was attempted.");
		}
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		assertEquals(body, responseContent);
	}
	
	
	/**
	 * <p>Test for a custom deserializer that cannot be instantiated.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test 
	public final void testUninstantiableDeserializer() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/uninstantiabledeserializer";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		deserializerEndpoint.uninstantiableDeserializer();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
	
	/**
	 * <p>Test for a custom serializer that cannot be instantiated.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test 
	public final void testIllegalDeserializer() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/illegaldeserializer";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));

		expectedException.expect(Is.isA(InvocationException.class));
		
		deserializerEndpoint.illegalDeserializer();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
}
