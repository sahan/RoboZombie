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
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.head;
import static com.github.tomakehurst.wiremock.client.WireMock.headRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.options;
import static com.github.tomakehurst.wiremock.client.WireMock.optionsRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.trace;
import static com.github.tomakehurst.wiremock.client.WireMock.traceRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.proxy.Zombie;

/**
 * <p>Performs unit testing on {@link HttpMethodEndpoint}.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @category test
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@RunWith(RobolectricTestRunner.class)
public class HttpMethodEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Bite
	private HttpMethodEndpoint httpMethodEndpoint;
	
	@Bite
	private HttpBinEndpoint httpBinEndpoint;
	
	
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for the request method GET.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testGetMethod() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String name = "James-Howlett", age = "116", location = "X-Mansion";
		String path = "/getrequest?name=" + name + "&age=" + age + "&location=" + location;
		
		stubFor(get(urlEqualTo(path))
				.willReturn(aResponse()
				.withStatus(200)));
		
		httpMethodEndpoint.getRequest(name, age, location);
		
		List<LoggedRequest> requests = findAll(getRequestedFor(urlEqualTo(path)));
		assertFalse(requests == null);
		assertFalse(requests.isEmpty());
		
		LoggedRequest request = requests.get(0);
		assertTrue(request.getMethod().equals(RequestMethod.GET));
	}
	
	/**
	 * <p>Test for the request method POST.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testPostMethod() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String path = "/postrequest";
		
		stubFor(post(urlEqualTo(path))
				.willReturn(aResponse()
				.withStatus(200)));
		
		String name = "DoctorWho", age = "953", location = "Tardis";
		
		httpMethodEndpoint.postRequest(name, age, location);
		
		List<LoggedRequest> requests = findAll(postRequestedFor(urlMatching(path)));
		assertFalse(requests == null);
		assertFalse(requests.isEmpty());
		
		LoggedRequest request = requests.get(0);
		assertTrue(request.getMethod().equals(RequestMethod.POST));
		
		String body = request.getBodyAsString();
		assertTrue(body.contains("name=" + name));
		assertTrue(body.contains("age=" + age));
		assertTrue(body.contains("location=" + location));
	}
	
	/**
	 * <p>Test for the request method PUT.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testPutMethod() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String path = "/putrequest";
		
		stubFor(put(urlEqualTo(path))
				.willReturn(aResponse()
				.withStatus(200)));
		
		String user = "{ '_id':1, 'alias':'Black Bolt' }";
		
		httpMethodEndpoint.putRequest(user);
		
		List<LoggedRequest> requests = findAll(putRequestedFor(urlMatching(path)));
		assertFalse(requests == null);
		assertFalse(requests.isEmpty());
		
		LoggedRequest request = requests.get(0);
		assertTrue(request.getMethod().equals(RequestMethod.PUT));
		
		String body = request.getBodyAsString();
		assertTrue(body.contains(user));
	}
	
	/**
	 * <p>Test for the request method PATCH.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testPatchMethod() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String user = "{ '_id':1, 'alias':'Thanos' }";
		
		String response = httpBinEndpoint.patchRequest(user);
		
		assertFalse(response == null);
		assertFalse(response.isEmpty());
		assertTrue(response.contains("\"data\": \"" + user + "\""));
	}
	
	/**
	 * <p>Test for the request method DELETE.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testDeleteMethod() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String id = "doctorwho", uri = "/deleterequest/" + id;
		
		stubFor(delete(urlMatching(uri))
				.willReturn(aResponse()
				.withStatus(200)));
		
		httpMethodEndpoint.deleteRequest(id);
		verify(deleteRequestedFor(urlEqualTo(uri)));
	}
	
	/**
	 * <p>Test for the request method HEAD.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testHeadMethod() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String uri = "/headrequest", header = "Proxy-Authenticate", 
			   headerValue = "Basic";
		
		stubFor(head(urlMatching(uri))
				.willReturn(aResponse()
				.withStatus(200)
				.withHeader(header, headerValue)));
		
		StringBuilder responseHeader = new StringBuilder();
		
		httpMethodEndpoint.headRequest(responseHeader);
		
		verify(headRequestedFor(urlEqualTo(uri)));
		
		String authenticationType = responseHeader.toString();
		assertTrue(authenticationType != null);
		assertTrue(authenticationType.equals(headerValue));
	}
	
	/**
	 * <p>Test for the request method TRACE.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testTraceMethod() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String uri = "/tracerequest", headerVia = "Via", 
			   headerViaValue = "1.0 example1.com, 1.1 example2.com", 
			   headerMaxForwards = "Max-Forwards", 
			   headerMaxForwardsValue = "6";
		
		stubFor(trace(urlMatching(uri))
				.willReturn(aResponse()
				.withStatus(200)));
		
		httpMethodEndpoint.traceRequest();
		
		verify(traceRequestedFor(urlEqualTo(uri))
			   .withHeader(headerVia, matching(headerViaValue))
			   .withHeader(headerMaxForwards, matching(headerMaxForwardsValue)));
	}
	
	/**
	 * <p>Test for the request method OPTIONS.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testOptionsMethod() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String uri = "/optionsrequest", header = "Content-Type", 
			   headerValue = "application/json";
		
		stubFor(options(urlMatching(uri))
				.willReturn(aResponse()
				.withStatus(200)
				.withHeader(header, headerValue)));
		
		StringBuilder responseHeader = new StringBuilder();
		
		httpMethodEndpoint.optionsRequest(responseHeader);
		
		verify(optionsRequestedFor(urlEqualTo(uri)));
		
		String contentType = responseHeader.toString();
		assertTrue(contentType != null);
		assertTrue(contentType.equals(headerValue));
	}
}
