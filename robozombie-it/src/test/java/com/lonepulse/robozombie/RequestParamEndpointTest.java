package com.lonepulse.robozombie;

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
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
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
import com.lonepulse.robozombie.Zombie;
import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.annotation.QueryParam;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.test.model.User;

/**
 * <p>Performs <b>Unit Testing</b> on the proxy of {@link RequestParamEndpoint}.
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
public class RequestParamEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Bite
	private RequestParamEndpoint requestEndpoint;
	
	
	/**
	 * <p>Sets up the test case by performing endpoint injection on {@link #requestEndpoint}.
	 * 
	 * @throws java.lang.Exception
	 * 			if the test case setup or endpoint injection failed
	 */
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for a {@link Request} with a subpath having {@link QueryParam}s.
	 * 
	 * @since 1.2.4
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
	 * <p>Test for a {@link Request} with a subpath having constant {@link Request.Param}s.
	 * 
	 * @since 1.2.4
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
	 * <p>Test for a {@link Request} with a {@code byte[]} entity.
	 * 
	 * @since 1.2.4
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
	 * <p>Test for a {@link Request} with a {@code Byte}[] entity.
	 * 
	 * @since 1.2.4
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
	 * <p>Test for a {@link Request} with a {@link File} entity.
	 * 
	 * @since 1.2.4
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
	 * <p>Test for a {@link Request} with a <b>buffered</b> entity.
	 * 
	 * @since 1.2.4
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
	 * <p>Test for a {@link Request} with a {@link String} entity.
	 * 
	 * @since 1.2.4
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
	 * <p>Test for a {@link Request} with a {@link Serializable} entity.
	 * 
	 * @since 1.2.4
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
	 * @since 1.2.4
	 */
	@Test @SuppressWarnings("unchecked") //safe cast to Class<Throwable>
	public final void testMissingEntity() throws ClassNotFoundException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		expectedException.expectCause(Is.isA((Class<Throwable>)
			Class.forName("com.lonepulse.robozombie.request.RequestProcessorException")));
		
		requestEndpoint.missingEntity();
	}
	
	/**
	 * <p>Test for a multiple entities in an entity-enclosing request.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test @SuppressWarnings("unchecked") //safe cast to Class<Throwable>
	public final void testMultipleEntity() throws ClassNotFoundException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		expectedException.expectCause(Is.isA((Class<Throwable>)
			Class.forName("com.lonepulse.robozombie.request.RequestProcessorException")));
		
		requestEndpoint.multipleEntity("entity1", "entity2");
	}
	
	/**
	 * <p>Test for an unresolvable entity in an entity-enclosing request.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test @SuppressWarnings("unchecked") //safe cast to Class<Throwable>
	public final void testResolutionFailedEntity() throws IOException, ClassNotFoundException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		expectedException.expectCause(Is.isA((Class<Throwable>)
			Class.forName("com.lonepulse.robozombie.request.RequestProcessorException")));
		
		requestEndpoint.resolutionFailedEntity(new Object());
	}
}
