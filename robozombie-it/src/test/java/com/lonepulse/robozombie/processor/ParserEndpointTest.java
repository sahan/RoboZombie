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
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.simpleframework.xml.core.Persister;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.inject.Zombie;
import com.lonepulse.robozombie.model.User;
import com.lonepulse.robozombie.response.AbstractResponseParser;
import com.lonepulse.robozombie.response.ResponseParsers;

/**
 * <p>Performs <b>Unit Testing</b> on the proxy of {@link ParserEndpoint}.
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
public class ParserEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Bite
	private ParserEndpoint parserEndpoint;
	
	
	/**
	 * <p>Sets up the test case by performing endpoint injection on {@link #parserEndpoint}.
	 * 
	 * @throws java.lang.Exception
	 * 			if the test case setup or endpoint injection failed
	 */
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for {@link ParserEndpoint#responseError()}
	 *
	 * @since 1.2.4
	 */
	@Test @SuppressWarnings("unchecked") //safe cast to Class<Throwable> 
	public final void testResponseError() throws ClassNotFoundException {

		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/responseerror", body = "forbidden";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(403)
				.withBody(body)));
		
		expectedException.expect((Class<Throwable>)
			Class.forName("com.lonepulse.robozombie.executor.RequestExecutionException"));
		
		String parsedContent = parserEndpoint.responseError();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		assertNull(parsedContent);
	}
	
	/**
	 * <p>Test for {@link ResponseParsers#JSON}.
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testParseJson() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/json";
		
		User user = new User(1, "Tenzen", "Yakushiji", 300, true);
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(user.toString())));
		
		User parsedUser = parserEndpoint.parseJson();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		
		assertEquals(user.getId(), parsedUser.getId());
		assertEquals(user.getFirstName(), parsedUser.getFirstName());
		assertEquals(user.getLastName(), parsedUser.getLastName());
		assertEquals(user.getAge(), parsedUser.getAge());
		assertEquals(user.isImmortal(), parsedUser.isImmortal());
	}
	
	/**
	 * <p>Test for {@link ResponseParsers#XML}.
	 * 
	 * @throws Exception
	 * 			if the test terminated with an error
	 * 
	 * @since 1.2.4
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
		
		User parsedUser = parserEndpoint.parseXml();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		
		assertEquals(user.getId(), parsedUser.getId());
		assertEquals(user.getFirstName(), parsedUser.getFirstName());
		assertEquals(user.getLastName(), parsedUser.getLastName());
		assertEquals(user.getAge(), parsedUser.getAge());
		assertEquals(user.isImmortal(), parsedUser.isImmortal());
	}
	
	/**
	 * <p>Test for {@link ParserEndpoint#raw()}.
	 *
	 * @since 1.2.4
	 */
	@Test  
	public final void testRaw() throws ClassNotFoundException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/raw", body = "SAO Nerve Gear";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withBody(body)));
		
		String responseContent = parserEndpoint.raw();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		assertEquals(body, responseContent);
	}
	
	/**
	 * <p>Test for custom {@link AbstractResponseParser}s.
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testParseCustom() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/custom", redacted = "<redacted>";
		
		User user = new User(1, "Felix", "Walken", 28, false);
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(user.toString())));
		
		User parsedUser = parserEndpoint.parseCustom();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		
		assertEquals(user.getId(), parsedUser.getId());
		assertEquals(redacted, parsedUser.getFirstName());
		assertEquals(redacted, parsedUser.getLastName());
		assertEquals(user.getAge(), parsedUser.getAge());
		assertEquals(user.isImmortal(), parsedUser.isImmortal());
	}
}
