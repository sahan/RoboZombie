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
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.entity.SerializableEntity;
import org.apache.http42.util.EntityUtils;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.simpleframework.xml.core.Persister;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;
import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.inject.InvocationException;
import com.lonepulse.robozombie.inject.Zombie;
import com.lonepulse.robozombie.model.User;
import com.lonepulse.robozombie.request.AbstractSerializer;
import com.lonepulse.robozombie.request.Serializers;

/**
 * <p>Performs <b>Unit Testing</b> on the proxy of {@link SerializerEndpoint}.
 * 
 * @category test
 * <br><br>
 * @version 1.1.1
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class SerializerEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Bite
	private SerializerEndpoint serializerEndpoint;
	
	
	/**
	 * <p>Sets up the test case by performing endpoint injection on {@link #serializerEndpoint}.
	 * 
	 * @throws java.lang.Exception
	 * 			if the test case setup or endpoint injection failed
	 */
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for {@link Serializers#JSON}.
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testSerializeJson() throws ParseException, IOException {
		
		String subpath = "/json";
		
		User user = new User(1, "Tenzen", "Yakushiji", 300, true);
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		serializerEndpoint.serializeJson(user);
		
		verify(putRequestedFor(urlEqualTo(subpath))
			  .withRequestBody(equalTo(new Gson().toJson(user))));
	}
	
	/**
	 * <p>Test for {@link Serializers#XML}.
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testSerializeXml() throws Exception {
		
		String subpath = "/xml";
		
		User user = new User(1, "Shiro", "Wretched-Egg", 17, true);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new Persister().write(user, baos);
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		serializerEndpoint.serializeXml(user);
		
		verify(putRequestedFor(urlEqualTo(subpath))
			  .withRequestBody(equalTo(baos.toString())));
	}
	
	/**
	 * <p>Test for {@link Serializers#PLAIN}.
	 *
	 * @since 1.2.4
	 */
	@Test  
	public final void testPlain() {

		String subpath = "/plain";
		
		User user = new User(1, "Inori", "Yuzuhira", 16, false);
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		serializerEndpoint.plainString(user);
		
		verify(putRequestedFor(urlEqualTo(subpath))
			   .withRequestBody(equalTo(user.toString())));
	}
	
	/**
	 * <p>Test for custom {@link AbstractSerializer}s.
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testSerializeCustom() {
		
		String subpath = "/custom", redacted = "<redacted>";
		
		User user = new User(1, "Felix", "Walken", 28, false);
		User redactedUser = new User(1, redacted, redacted, 28, false);
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		serializerEndpoint.serializeCustom(user);
		
		verify(putRequestedFor(urlEqualTo(subpath))
			   .withRequestBody(equalTo(new Gson().toJson(redactedUser))));
	}
	
	/**
	 * <p>Test for detachment of the inherited serializer.</p>
	 *
	 * @since 1.2.4
	 */
	@Test  
	public final void testDetachSerializer() throws ParseException, IOException {

		String subpath = "/detach";
		
		User user = new User(1, "Roy", "Mustang", 30, false);
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		serializerEndpoint.detachSerializer(user);
		
		verify(putRequestedFor(urlEqualTo(subpath))
			  .withRequestBody(equalTo(EntityUtils.toString(new SerializableEntity(user, true)))));
	}
	
	/**
	 * <p>Test for a custom serializer that cannot be instantiated.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testUninstantiableSerializer() {
		
		String subpath = "/uninstantiableserializer";
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		serializerEndpoint.uninstantiableSerializer("serialized");
	}
	
	/**
	 * <p>Test for a custom serializer that cannot be instantiated.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test 
	public final void testIllegalSerializer() {
		
		String subpath = "/illegalserializer";
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));

		expectedException.expect(Is.isA(InvocationException.class));
		
		serializerEndpoint.illegalSerializer("serialized");
	}
}
