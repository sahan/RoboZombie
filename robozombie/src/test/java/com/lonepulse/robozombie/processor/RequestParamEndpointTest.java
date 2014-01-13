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
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
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
import com.lonepulse.robozombie.annotation.FormParam;
import com.lonepulse.robozombie.annotation.FormParams;
import com.lonepulse.robozombie.annotation.QueryParam;
import com.lonepulse.robozombie.annotation.QueryParams;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.model.User;
import com.lonepulse.robozombie.proxy.InvocationException;
import com.lonepulse.robozombie.proxy.Zombie;

/**
 * <p>Performs unit testing on {@link RequestParamEndpoint}.</p>
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
public class RequestParamEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Bite
	private RequestParamEndpoint requestEndpoint;
	
	
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for a {@link Request} having {@link QueryParam}s.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test 
	public final void testQueryParams() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/queryparams\\?\\S+",
			   firstName = "Doctor", lastName = "Who",
			   url = "/queryparams?firstName=" + firstName + "&lastName=" + lastName;
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));

		requestEndpoint.queryParams(firstName, lastName);
		
		verify(getRequestedFor(urlEqualTo(url)));
	}
	
	/**
	 * <p>Test for a {@link Request} having {@link FormParam}s.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test 
	public final void testFormParams() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/formparams",
				firstName = "Doctor", lastName = "Who",
				body = "firstName=" + firstName + "&lastName=" + lastName;
		
		stubFor(post(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.formParams(firstName, lastName);
		
		verify(postRequestedFor(urlEqualTo(subpath))
			   .withRequestBody(equalTo(body)));
	}
	
	/**
	 * <p>Test for a {@link Request} having illegal {@link QueryParam}s.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testQueryParamsFail() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/queryparamsfail";
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		requestEndpoint.queryParamsFail(new User(1, "Ra's", "al Ghul", 47, false));
	}
	
	/**
	 * <p>Test for a {@link Request} having illegal {@link FormParam}s.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testFormParamsFail() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/formparamsfail";
		
		stubFor(post(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		requestEndpoint.formParamsFail(new User(1, "Ra's", "al Ghul", 47, false));
	}
	
	/**
	 * <p>Test for a {@link Request} having batch {@link QueryParams}.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testQueryParamsBatch() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/queryparamsbatch\\?\\S+",
			   fnKey = "firstName", lnKey = "lastName",
			   firstName = "Bucky", lastName = "Barnes",
			   url = "/queryparamsbatch?" + fnKey + "=" + firstName + "&" + lnKey + "=" + lastName;
		
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put(fnKey, firstName); 
		params.put(lnKey, lastName); 
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.queryParamsBatch(params);
		
		verify(getRequestedFor(urlEqualTo(url)));
	}

	/**
	 * <p>Test for a {@link Request} having batch {@link QueryParams}.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testFormParamsBatch() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/formparamsbatch",
			   fnKey = "firstName", lnKey = "lastName",
			   firstName = "Franklin", lastName = "Richards",
			   body = fnKey + "=" + firstName + "&" + lnKey + "=" + lastName;
		
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put(fnKey, firstName); 
		params.put(lnKey, lastName); 
		
		stubFor(post(urlMatching(subpath))
				.willReturn(aResponse()
				.withBody(body)
				.withStatus(200)));
		
		requestEndpoint.formParamsBatch(params);
		
		verify(postRequestedFor(urlEqualTo(subpath))
			  .withRequestBody(equalTo(body)));
	}
	
	/**
	 * <p>Test for a {@link Request} having illegal batch {@link QueryParams}.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testQueryParamsBatchTypeFail() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/queryparamsbatchtypefail";
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		requestEndpoint.queryParamsBatchTypeFail(new ArrayList<String>());
	}
	
	/**
	 * <p>Test for a {@link Request} having illegal batch {@link FormParams}.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testFormParamsBatchTypeFail() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/formparamsbatchtypefail";
		
		stubFor(post(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		requestEndpoint.formParamsBatchTypeFail(new ArrayList<String>());
	}
	
	/**
	 * <p>Test for a {@link Request} having illegal batch {@link QueryParams} elements.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testQueryParamsBatchElementFail() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/queryparamsbatchelementfail";
		
		Map<String, User> params = new HashMap<String, User>();
		params.put("subject", new User(1, "Kurt", "Wagner" , 32, false));
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		requestEndpoint.queryParamsBatchElementFail(params);
	}
	
	/**
	 * <p>Test for a {@link Request} having illegal batch {@link FormParams} elements.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testFormParamsBatchElementFail() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/formparamsbatchelementfail";
		
		Map<String, User> params = new HashMap<String, User>();
		params.put("subject", new User(1, "Kurt", "Wagner" , 32, false));
		
		stubFor(post(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		requestEndpoint.formParamsBatchElementFail(params);
	}
	
	/**
	 * <p>Test for a request which sends a multivalued query parameter.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testQueryParamsMultivalued() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/queryparamsmultivalued\\?\\S+", key = "mutant-powers";
		
		List<String> multivalue = new ArrayList<String>();
		multivalue.add("invulnerability");
		multivalue.add("teleportation");
		multivalue.add("precognition");
		
		String url = "/queryparamsmultivalued?" + key + "=invulnerability&" + 
					 key + "=teleportation&" + key + "=precognition";
		
		Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
		params.put(key, multivalue);
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.queryParamsMultivalued(params);
		
		verify(getRequestedFor(urlEqualTo(url)));
	}
	
	/**
	 * <p>Test for a request which send a multivalued query parameter.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testFormParamsMultivalued() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/formparamsmultivalued", key = "mutant-powers";
		
		List<String> multivalue = new ArrayList<String>();
		multivalue.add("shapeshifting");
		multivalue.add("rapid-healing");
		multivalue.add("longevity");
		
		String body = key + "=shapeshifting&" + key + "=rapid-healing&" + key + "=longevity";
		
		Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
		params.put(key, multivalue);
		
		stubFor(post(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.formParamsMultivalued(params);
		
		verify(postRequestedFor(urlEqualTo(subpath))
			   .withRequestBody(equalTo(body)));
	}
	
	/**
	 * <p>Test for a {@link Request} having illegal multivalued query parameters.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testQueryParamsMultivaluedFail() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/queryparamsmultivaluedfail";
		
		Map<String, List<User>> params = new HashMap<String, List<User>>();
		
		List<User> subjects = new ArrayList<User>();
		subjects.add(new User(1, "(En) Sabah", "Nur", 5236, true));
		subjects.add(new User(2, "Stephen", "Strange", 41, false)); 
		
		params.put("subjects", subjects);
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		requestEndpoint.queryParamsMultivaluedFail(params);
	}
	
	/**
	 * <p>Test for a {@link Request} having illegal multivalued query parameters.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testFormParamsMultivaluedFail() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/formparamsmultivaluedfail";
		
		Map<String, List<User>> params = new HashMap<String, List<User>>();
		
		List<User> subjects = new ArrayList<User>();
		subjects.add(new User(1, "(En) Sabah", "Nur", 5236, true));
		subjects.add(new User(2, "Stephen", "Strange", 41, false)); 
		
		params.put("subjects", subjects);
		
		stubFor(post(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		requestEndpoint.formParamsMultivaluedFail(params);
	}
	
	/**
	 * <p>Test for a {@link Request} having constant query parameters.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testConstantQueryParams() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/constantqueryparams\\?\\S+", 
			   firstName = "Doctor", lastName = "Who",
			   url = "/constantqueryparams?firstName=" + firstName + "&lastName=" + lastName;
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.constantQueryParams();
		
		verify(getRequestedFor(urlEqualTo(url)));
	}
	
	/**
	 * <p>Test for a {@link Request} having constant inline {@link QueryParams}.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testInlineConstantQueryParams() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/inlineconstantqueryparams\\?\\S+",
			   key1 = "class", value1 = "omega",
			   key2 = "name", value2 = "Legion",
			   url = "/inlineconstantqueryparams?" + key1 + "=" + value1 + "&" + key2 + "=" + value2;
		
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put(key2, value2); 
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.inlineConstantQueryParams(params);
		
		verify(getRequestedFor(urlEqualTo(url)));
	}
	
	/**
	 * <p>Test for a {@link Request} having constant form parameters.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testConstantFormParams() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/constantformparams", 
			   firstName = "Beta-Ray", lastName = "Bill",
			   body = "firstName=" + firstName + "&lastName=" + lastName;
		
		stubFor(post(urlMatching(subpath))
				.willReturn(aResponse()
				.withBody(body)
				.withStatus(200)));
		
		requestEndpoint.constantFormParams();
		
		verify(postRequestedFor(urlEqualTo(subpath))
			  .withRequestBody(equalTo(body)));
	}
	
	/**
	 * <p>Test for a {@link Request} having constant inline {@link FormParams}.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testInlineConstantFormParams() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/inlineconstantformparams",
			   key1 = "class", value1 = "omega",
			   key2 = "name", value2 = "Chamber",
			   body = key1 + "=" + value1 + "&" + key2 + "=" + value2;
		
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put(key2, value2); 
		
		stubFor(post(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.inlineConstantFormParams(params);
		
		verify(postRequestedFor(urlEqualTo(subpath))
			   .withRequestBody(equalTo(body)));
	}
	
	/**
	 * <p>Test for a {@link Request} with a {@code byte[]} entity.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testPrimitiveByteArrayEntity() throws ParseException, IOException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/primitivebytearrayentity";
		byte[] bytes = new byte[] {1, 1, 1, 1, 1, 1, 1, 1};
		ByteArrayEntity bae = new ByteArrayEntity(bytes);
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.primitiveByteArrayEntity(bytes);
		
		verify(putRequestedFor(urlEqualTo(subpath))
			   .withRequestBody(equalTo(EntityUtils.toString(bae))));
	}
	
	/**
	 * <p>Test for a {@link Request} with a {@code Byte}[] entity.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testWrapperByteArrayEntity() throws ParseException, IOException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/wrapperbytearrayentity";
		Byte[] bytes = new Byte[] {1, 1, 1, 1, 1, 1, 1, 1};
		ByteArrayEntity bae = new ByteArrayEntity(new byte[] {1, 1, 1, 1, 1, 1, 1, 1});
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.wrapperByteArrayEntity(bytes);
		
		verify(putRequestedFor(urlEqualTo(subpath))
				.withRequestBody(equalTo(EntityUtils.toString(bae))));
	}
	
	/**
	 * <p>Test for a {@link Request} with a {@link File} entity.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testFileEntity() throws ParseException, IOException, URISyntaxException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/fileentity";
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		File file = new File(classLoader.getResource("LICENSE.txt").toURI());
		FileEntity fe = new FileEntity(file, null);
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.fileEntity(file);
		
		verify(putRequestedFor(urlEqualTo(subpath))
			   .withRequestBody(equalTo(EntityUtils.toString(fe))));
	}
	
	/**
	 * <p>Test for a {@link Request} with a <b>buffered</b> entity.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testBufferedHttpEntity() throws ParseException, IOException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/bufferedhttpentity";
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("LICENSE.txt");
		InputStream parallelInputStream = classLoader.getResourceAsStream("LICENSE.txt");
		BasicHttpEntity bhe = new BasicHttpEntity();
		bhe.setContent(parallelInputStream);
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.bufferedHttpEntity(inputStream);
		
		verify(putRequestedFor(urlEqualTo(subpath))
			   .withRequestBody(equalTo(EntityUtils.toString(new BufferedHttpEntity(bhe)))));
	}
	
	/**
	 * <p>Test for a {@link Request} with a {@link String} entity.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testStringEntity() throws ParseException, IOException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/stringentity";
		String entity = "haganenorenkinjutsushi";
		StringEntity se = new StringEntity(entity);
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.stringEntity(entity);
		
		verify(putRequestedFor(urlEqualTo(subpath))
			   .withRequestBody(equalTo(EntityUtils.toString(se))));
	}
	
	/**
	 * <p>Test for a {@link Request} with a {@link Serializable} entity.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testSerializableEntity() throws ParseException, IOException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/serializableentity";
		User entity = new User(1L, "Eren", "Yeager", 15, false);
		SerializableEntity se = new SerializableEntity(entity, true);
		
		stubFor(put(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		requestEndpoint.serializableEntity(entity);
		
		verify(putRequestedFor(urlEqualTo(subpath))
			   .withRequestBody(equalTo(EntityUtils.toString(se))));
	}

	/**
	 * <p>Test for a non-POST entity-enclosing request without a supplied entity.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testMissingEntity() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		requestEndpoint.missingEntity();
	}
	
	/**
	 * <p>Test for a multiple entities in an entity-enclosing request.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testMultipleEntity() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		requestEndpoint.multipleEntity("entity1", "entity2");
	}
	
	/**
	 * <p>Test for an unresolvable entity in an entity-enclosing request.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testResolutionFailedEntity() {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		requestEndpoint.resolutionFailedEntity(new Object());
	}
}
