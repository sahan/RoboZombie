package com.lonepulse.robozombie.test.endpoint;

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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.lonepulse.robozombie.core.annotation.Asynchronous;
import com.lonepulse.robozombie.core.annotation.Endpoint;
import com.lonepulse.robozombie.core.annotation.Header;
import com.lonepulse.robozombie.core.annotation.Param;
import com.lonepulse.robozombie.core.annotation.Request;
import com.lonepulse.robozombie.core.inject.Zombie;
import com.lonepulse.robozombie.core.request.RequestMethod;
import com.lonepulse.robozombie.core.response.AsyncHandler;
import com.lonepulse.robozombie.rest.annotation.PathParam;
import com.lonepulse.robozombie.rest.annotation.Rest;
import com.lonepulse.robozombie.rest.response.parser.JsonResponseParser;
import com.lonepulse.robozombie.test.model.ICNDBResponse;
import com.lonepulse.robozombie.test.model.ICNDBResponseArray;
import com.lonepulse.robozombie.test.model.NorrisJoke;
import com.lonepulse.robozombie.test.service.ICNDBService;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

/**
 * <p>Performs <b>Unit Testing</b> on the proxy of {@link ICNDBEndpoint}.
 * 
 * @category test
 * <br><br>
 * @version 1.0.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@RunWith(RobolectricTestRunner.class)
public class ICNDBEndpointTest {

	
	/**
	 * <p>A typical {@link ICNDBEndpoint} instance.
	 */
	private static ICNDBEndpoint icndbEndpoint;
	
	/**
	 * <p>A single thread executor which performs 
	 * RoboZombie network calls off-of the main thread.
	 */
	private static ExecutorService executorService;
	
	
	/**
	 * <p>Sets up the test case by performing endpoint injection on 
	 * {@link #icndbEndpoint}.
	 * 
	 * @throws java.lang.Exception
	 * 			if the endpoint injection failed
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		
		executorService = Executors.newSingleThreadExecutor();
		icndbEndpoint = Zombie.infect(ICNDBService.class).getConstructedICNDBEndpoint();
	}

	/**
	 * <p>Test method for {@link Endpoint#path()} and {@link Request#path()} 
	 * with {@link JsonResponseParser}.
	 * 
	 * @throws Exception
	 * 			if test terminated with an error
	 */
	@Test
	public final void testPath() throws Exception {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		Future<ICNDBResponse> future = executorService.submit(new Callable<ICNDBResponse>() {

			public ICNDBResponse call() throws Exception {

				return icndbEndpoint.random();
			}
		});
		
		ICNDBResponse icndbResponse = future.get();
			
		assertNotNull(icndbResponse);
		assertNotNull(icndbResponse.getValue());
	}
	
	/**
	 * <p>Test method for {@link Param} with {@link RequestMethod#HTTP_GET}.
	 * 
	 * @throws Exception
	 * 			if test terminated with an error
	 */
	@Test
	public final void testGETParams() throws Exception {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		final String firstName = "John";
		final String lastName = "Doe";
		
		Future<NorrisJoke> future = executorService.submit(new Callable<NorrisJoke>() {

			public NorrisJoke call() throws Exception {

				return icndbEndpoint.random(firstName, lastName).getValue();
			}
		});
		
		NorrisJoke norrisJoke = future.get();

		assertTrue(norrisJoke.getJoke().contains(firstName));
		assertTrue(norrisJoke.getJoke().contains(lastName));
	}
	
	/**
	 * <p>Test method for {@link Request.Param}.
	 * 
	 * @throws Exception
	 * 			if test terminated with an error
	 */
	@Test
	public final void testConstantParams() throws Exception  {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		Future<NorrisJoke> future = executorService.submit(new Callable<NorrisJoke>() {

			public NorrisJoke call() throws Exception {

				return icndbEndpoint.randomJohnDoeJoke().getValue();
			}
		});
		
		NorrisJoke norrisJoke = future.get();
		
		assertTrue(norrisJoke.getJoke().contains("John"));
		assertTrue(norrisJoke.getJoke().contains("Doe"));
	}
	
	/**
	 * <p>Test method for {@link Rest} and {@link PathParam}.
	 * 
	 * @throws Exception
	 * 			if test terminated with an error
	 */
	@Test
	public final void testPathParam() throws Exception {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		Future<ICNDBResponseArray> future = executorService.submit(new Callable<ICNDBResponseArray>() {

			public ICNDBResponseArray call() throws Exception {

				return icndbEndpoint.random("4");
			}
		});
		
		ICNDBResponseArray icndbResponse = future.get();
		
		assertNotNull(icndbResponse);
		assertTrue(icndbResponse.getValue().size() == 4);
	}
	
	/**
	 * <p>Test method for {@link Asynchronous} and {@link AsyncHandler}.
	 * 
	 * @throws Exception
	 * 			if test terminated with an error
	 */
	@Test
	public final void testAsyncRequest() throws Exception {

		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		final BlockingQueue<ICNDBResponseArray> queueParsedResponse = new ArrayBlockingQueue<ICNDBResponseArray>(1);
		final BlockingQueue<HttpResponse> queueResponse = new ArrayBlockingQueue<HttpResponse>(1);
		
		ICNDBResponseArray synchronousResult = icndbEndpoint.randomAsync(new AsyncHandler<ICNDBResponseArray>() {

			@Override
			public void onSuccess(HttpResponse httpResponse, ICNDBResponseArray icndbResponseArray) {

				queueResponse.offer(httpResponse);
				queueParsedResponse.offer(icndbResponseArray);
			}
		});
		
		assertNull(synchronousResult);
		
		HttpResponse httpResponse = queueResponse.take();
		assertNotNull(httpResponse);
		
		ICNDBResponseArray icndbResponseArray = queueParsedResponse.take();
		assertNotNull(icndbResponseArray);
		assertNotNull(icndbResponseArray.getValue());
		assertTrue(icndbResponseArray.getValue().size() == 10);
	}
	
	/**
	 * <p>Test method for {@link Header}.
	 * 
	 * @throws Exception
	 * 			if test terminated with an error
	 */
	@Test
	public final void testResponseHeader() throws Exception {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		final StringBuilder header = new StringBuilder();
		
		Future<ICNDBResponse> future = executorService.submit(new Callable<ICNDBResponse>() {

			public ICNDBResponse call() throws Exception {

				return icndbEndpoint.random(header);
			}
		});
		
		ICNDBResponse icndbResponse = future.get();
		
		assertNotNull(icndbResponse);
		
		String server = header.toString();
		assertTrue(server != null);
		assertTrue(!server.equals(""));
	}
	
	/**
	 * <p>Tears down the test case by shutting down the 
	 * {@link #executorService}.
	 * 
	 * @throws java.lang.Exception
	 * 			if the tear-down failed
	 */
	@AfterClass
	public static void tearDown() throws Exception {
		
		executorService.shutdownNow();
	}
}
