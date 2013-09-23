package com.lonepulse.robozombie.test.processor;

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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.lonepulse.robozombie.annotation.Asynchronous;
import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.inject.Zombie;
import com.lonepulse.robozombie.response.AsyncHandler;

/**
 * <p>Performs <b>Unit Testing</b> on the proxy of {@link AsyncEndpoint}.
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
public class AsyncEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Bite
	private AsyncEndpoint asyncEndpoint;
	
	
	/**
	 * <p>Sets up the test case by performing endpoint injection on {@link #asyncEndpoint}.
	 * 
	 * @throws java.lang.Exception
	 * 			if the test case setup or endpoint injection failed
	 */
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Tests asynchronous request execution with @{@link Asynchronous} and 
	 * {@link AsyncHandler#onSuccess(HttpResponse, Object)}.
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testAsyncSuccess() throws InterruptedException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		successScenario();
	}
	
	/**
	 * <p>See {@link #testAsyncSuccess()}. 
	 */
	private void successScenario() throws InterruptedException {
		
		String subpath = "/asyncsuccess", body = "hello";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));

		final Object[] content = new Object[2];
		
		final Lock lock = new ReentrantLock();
		final Condition condition = lock.newCondition();
		
		String result = asyncEndpoint.asyncSuccess(new AsyncHandler<String>() {
			
			@Override
			public void onSuccess(HttpResponse httpResponse, String parsedContent) {

				lock.lock();
				
				content[0] = httpResponse;
				content[1] = parsedContent;
				
				condition.signal();
				lock.unlock();
			}
		});

		lock.lock();
		condition.await();
		lock.unlock();

		verify(getRequestedFor(urlEqualTo(subpath)));
		
		assertTrue(content[0] != null);
		assertTrue(content[1] != null);
		assertTrue(content[1].equals(body));
		
		assertNull(result);
	}
	
	/**
	 * <p>Tests asynchronous request execution with @{@link Asynchronous} and 
	 * {@link AsyncHandler#onFailure(HttpResponse)}.
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testAsyncFailure() throws InterruptedException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/asyncfailure", body = "hello";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(403)
				.withBody(body)));
		
		final Object[] content = new Object[1];
		
		final Lock lock = new ReentrantLock();
		final Condition condition = lock.newCondition();
		
		asyncEndpoint.asyncFailure(new AsyncHandler<String>() {
			
			@Override
			public void onSuccess(HttpResponse httpResponse, String e) {}
			
			@Override
			public void onFailure(HttpResponse httpResponse) {
			
				lock.lock();
				
				content[0] = httpResponse;
				condition.signal();
				
				lock.unlock();
			}
		});
		
		lock.lock();
		condition.await();
		lock.unlock();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		
		assertTrue(content[0] != null);
	}
	
	/**
	 * <p>Tests an asynchronous request execution with @{@link Asynchronous} which does 
	 * not expect the response to be handled. 
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testAsyncNoHandling() throws InterruptedException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/asyncnohandling", body = "hello";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));
		
		asyncEndpoint.asyncNoHandling();
		
		TimeUnit.SECONDS.sleep(2);
		
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
	
	/**
	 * <p>Tests a successful asynchronous request where the implementation of the 
	 * {@link AsyncHandler#onSuccess(HttpResponse, Object)} callback throws an exception. 
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testAsyncSuccessCallbackError() throws InterruptedException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/successcallbackerror";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		final Lock lock = new ReentrantLock();
		final Condition condition = lock.newCondition();
		
		asyncEndpoint.asyncSuccessCallbackError(new AsyncHandler<String>() {

			@Override
			public void onSuccess(HttpResponse httpResponse, String e) {

				try {
				
					throw new IllegalStateException();
				}
				finally {
					
					lock.lock();
					condition.signal();
					lock.unlock();
				}
			}
		});

		lock.lock();
		condition.await();
		lock.unlock();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		
		successScenario(); //verify that the asynchronous request executor has survived the exception
	}
	
	/**
	 * <p>Tests a failed asynchronous request where the implementation of the 
	 * {@link AsyncHandler#onFailure(HttpResponse)} callback throws an exception. 
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testAsyncFailureCallbackError() throws InterruptedException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/failurecallbackerror";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(404)));
		
		final Lock lock = new ReentrantLock();
		final Condition condition = lock.newCondition();
		
		asyncEndpoint.asyncFailureCallbackError(new AsyncHandler<String>() {
			
			@Override
			public void onSuccess(HttpResponse httpResponse, String e) {}
			
			@Override
			public void onFailure(HttpResponse httpResponse) {
				
				try {
					
					throw new IllegalStateException();
				}
				finally {
					
					lock.lock();
					condition.signal();
					lock.unlock();
				}
			}
		});
		
		lock.lock();
		condition.await();
		lock.unlock();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		
		successScenario(); //verify that the asynchronous request executor has survived the exception
	}
}