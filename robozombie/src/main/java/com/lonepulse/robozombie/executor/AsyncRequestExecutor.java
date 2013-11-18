package com.lonepulse.robozombie.executor;

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


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import android.util.Log;

import com.lonepulse.robozombie.annotation.Async;
import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This is an extension of {@link BasicRequestExecutor} which is responsible for executing <b>asynchronous 
 * requests</b> identified by the @{@link Async} annotation placed on the endpoint or request method.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class AsyncRequestExecutor extends BasicRequestExecutor {

	
	private static final ExecutorService ASYNC_EXECUTOR_SERVICE;
	
	static
	{
		ASYNC_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
			
				ASYNC_EXECUTOR_SERVICE.shutdown(); //finish executing all pending asynchronous requests 
				
				try {
					
					if(!ASYNC_EXECUTOR_SERVICE.awaitTermination(60, TimeUnit.SECONDS)) {
						
						List<Runnable> pendingRequests = ASYNC_EXECUTOR_SERVICE.shutdownNow();
						Log.i(getClass().getSimpleName(), pendingRequests.size() + " asynchronous requests aborted.");
						
						if(!ASYNC_EXECUTOR_SERVICE.awaitTermination(10, TimeUnit.SECONDS)) {
							
							Log.e(getClass().getSimpleName(), 
									"Failed to shutdown the cached thread pool for asynchronous requests.");
						}
					}
				}
				catch (InterruptedException ie) {

					List<Runnable> pendingRequests = ASYNC_EXECUTOR_SERVICE.shutdownNow();
					Log.i(getClass().getSimpleName(), pendingRequests.size() + " asynchronous requests aborted.");
					
					Thread.currentThread().interrupt();
				}
			}
		}));
	}
	
	
	/**
	 * <p>Creates a new instance of {@link AsyncRequestExecutor} using the given {@link ExecutionHandler}.</p>
	 * 
	 * <p>See {@link BasicRequestExecutor#BasicRequestExecutor(ExecutionHandler)}</p>
	 *
	 * @param executionHandler
	 * 			the instance of {@link ExecutionHandler} which will be invoked during request execution
	 * <br><br>
	 * @since 1.2.4
	 */
	AsyncRequestExecutor(ExecutionHandler executionHandler) {
		
		super(executionHandler);
	}
	
	/**
	 * <p>Executes an {@link HttpRequestBase} <b>asynchronously</b> with the endpoint's {@link HttpClient}, 
	 * which causes it to return immediately with {@code null}. Directing the request execution is delegated 
	 * to the super class' implementation.</p> 
	 * 
	 * <p>See {@link BasicRequestExecutor#execute(HttpRequestBase, InvocationContext)}</p>
	 * 
	 * @param request
	 * 			the {@link HttpRequestBase} to be executed using the endpoint's {@link HttpClient}
	 * <br><br>
	 * @param context
	 * 			the {@link InvocationContext} used to discover information about the proxy invocation
	 * <br><br>
	 * @throws RequestExecutionException
	 * 			if the HTTP request execution failed
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public HttpResponse execute(final HttpRequestBase httpRequestBase, final InvocationContext context)
	throws RequestExecutionException {
		
		ASYNC_EXECUTOR_SERVICE.execute(new Runnable() {

			@Override
			public void run() {
				
				AsyncRequestExecutor.super.execute(httpRequestBase, context);
			}
		});
		
		return null;
	}
}
