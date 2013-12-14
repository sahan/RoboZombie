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
import com.lonepulse.robozombie.annotation.Async;
import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.model.User;
import com.lonepulse.robozombie.proxy.InvocationException;
import com.lonepulse.robozombie.proxy.Zombie;
import com.lonepulse.robozombie.response.AsyncHandler;

/**
 * <p>Performs unit testing on the proxy of {@link AsyncEndpoint}.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @category test
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@RunWith(RobolectricTestRunner.class)
public class AsyncEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Bite
	private AsyncEndpoint asyncEndpoint;
	
	
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Tests asynchronous request execution with @{@link Async} and 
	 * {@link AsyncHandler#onSuccess(HttpResponse, Object)}.</p>
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testAsyncSuccess() throws InterruptedException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		successScenario();
	}
	
	/**
	 * <p>See {@link #testAsyncSuccess()}.</p>
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
			public void onSuccess(HttpResponse httpResponse, String deserializedContent) {

				lock.lock();
				
				content[0] = httpResponse;
				content[1] = deserializedContent;
				
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
	 * <p>Tests asynchronous request execution with @{@link Async} and 
	 * {@link AsyncHandler#onFailure(HttpResponse)}.</p>
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
	 * <p>Tests asynchronous request execution with @{@link Async} and 
	 * {@link AsyncHandler#onError(Exception)}.</p>
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testAsyncError() throws InterruptedException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/asyncerror", body = "non-JSON-content";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));
		
		final Object[] content = new Object[1];
		
		final Lock lock = new ReentrantLock();
		final Condition condition = lock.newCondition();
		
		asyncEndpoint.asyncError(new AsyncHandler<User>() {
			
			@Override
			public void onSuccess(HttpResponse httpResponse, User user) {}
			
			@Override
			public void onError(InvocationException error) {
				
				lock.lock();
				
				content[0] = error;
				condition.signal();
				
				lock.unlock();
			}
		});
		
		lock.lock();
		condition.await();
		lock.unlock();
		
		verify(getRequestedFor(urlEqualTo(subpath)));
		
		assertTrue(content[0] != null);
		assertTrue(content[0] instanceof InvocationException);
	}
	
	/**
	 * <p>Tests an asynchronous request execution with @{@link Async} which does not expect the 
	 * response to be handled.</p> 
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
	 * {@link AsyncHandler#onSuccess(HttpResponse, Object)} callback throws an exception.</p> 
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
	 * {@link AsyncHandler#onFailure(HttpResponse)} callback throws an exception.</p>
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
	
	/**
	 * <p>Tests an erroneous asynchronous request where the implementation of the 
	 * {@link AsyncHandler#onError(Exception)} callback throws an exception.</p>
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testAsyncErrorCallbackError() throws InterruptedException {
		
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/errorcallbackerror", body = "non-JSON-content";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));
		
		final Lock lock = new ReentrantLock();
		final Condition condition = lock.newCondition();
		
		asyncEndpoint.asyncErrorCallbackError(new AsyncHandler<User>() {
			
			@Override
			public void onSuccess(HttpResponse httpResponse, User user) {}
			
			@Override
			public void onError(InvocationException error) {
				
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
	 * <p>Tests a synchronous request which has detached the inherited {@link Async} annotation.</p>
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testAsyncDetached() {
	
		Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
		
		String subpath = "/asyncdetached", body = "hello";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));
		
		String response = asyncEndpoint.asyncDetached();

		verify(getRequestedFor(urlEqualTo(subpath)));
		assertTrue(response.equals(body));
	}
}
