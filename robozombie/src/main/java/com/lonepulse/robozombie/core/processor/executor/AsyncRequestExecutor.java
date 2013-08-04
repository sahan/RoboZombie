package com.lonepulse.robozombie.core.processor.executor;

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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.net.ParseException;
import android.util.Log;

import com.lonepulse.robozombie.core.MultiThreadedHttpClient;
import com.lonepulse.robozombie.core.annotation.Stateful;
import com.lonepulse.robozombie.core.cookie.HttpContextDirectory;
import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.core.response.AsyncHandler;
import com.lonepulse.robozombie.core.response.ResponseHandlers;

/**
 * <p>A concrete implementation of {@link RequestExecutor} which executes 
 * {@link HttpRequest}s asynchronously.</p>
 * 
 * <p>The thread pool is managed by {@link MultiThreadedHttpClient} using 
 * a {@link PoolingClientConnectionManager}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class AsyncRequestExecutor implements RequestExecutor {

	
	/**
	 * <p>A cached thread pool which will be used to execute asynchronous requests.
	 */
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
						Log.i(AsyncRequestExecutor.class.getSimpleName(), 
							  pendingRequests.size() + " asynchronous requests aborted.");
						
						if(!ASYNC_EXECUTOR_SERVICE.awaitTermination(10, TimeUnit.SECONDS))
							Log.e(AsyncRequestExecutor.class.getSimpleName(), pendingRequests.size() + 
								  " failed to shutdown the cached thread pool for asynchronous requests.");
					}
				}
				catch (InterruptedException ie) {

					List<Runnable> pendingRequests = ASYNC_EXECUTOR_SERVICE.shutdownNow();
					Log.i(AsyncRequestExecutor.class.getSimpleName(), pendingRequests.size() + 
						  " asynchronous requests aborted.");
					
					Thread.currentThread().interrupt();
				}
			}
		}));
	}
	
	
	/**
	 * <p>Takes an {@link HttpRequestBase} and executes it asynchronously. 
	 * This method returns {@code null} immediately.
	 * 
	 * @param httpRequestBase
	 * 			the {@link HttpRequestBase} to be executed
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} associated with 
	 * 			the current request
	 * 
	 * @since 1.1.0
	 */
	@Override
	public HttpResponse execute(final HttpRequestBase httpRequestBase, final ProxyInvocationConfiguration config)
	throws RequestExecutionException {

		ASYNC_EXECUTOR_SERVICE.execute(new Runnable() {
			
			@SuppressWarnings("unchecked") //type-safe cast from Object to AsyncHandler
			@Override
			public void run() {
		
				String errorContext = "Asynchronous request execution failed. ";
				String errorResponseIO = "Failed to close IO stream. Response content may have already been consumed.";

				Class<?> endpointClass = config.getEndpointClass();
				HttpResponse httpResponse;
				
				try {
					
					if(endpointClass.isAnnotationPresent(Stateful.class)) {
						
						HttpContext httpContext = HttpContextDirectory.INSTANCE.get(endpointClass);
						httpResponse = MultiThreadedHttpClient.INSTANCE.executeRequest(httpRequestBase, httpContext);
					}
					else {
						
						httpResponse = MultiThreadedHttpClient.INSTANCE.executeRequest(httpRequestBase);
					}
				} 
				catch (ClientProtocolException cpe) {

					Log.e(AsyncRequestExecutor.class.getSimpleName(), 
						  errorContext + "Protocol cannot be resolved.", cpe);
					return;
				} 
				catch (IOException ioe) {

					Log.e(AsyncRequestExecutor.class.getSimpleName(), errorContext + "IO failure.", ioe);
					return;
				} 
				catch (Exception e) {
					
					Log.e(AsyncRequestExecutor.class.getSimpleName(), errorContext, e);
					return;
				}
					
				Object[] requestArgs = config.getRequestArgs();
				
				if(requestArgs == null || requestArgs.length == 0) {
					
					try {
					
						EntityUtils.toString(httpResponse.getEntity());
					}
					catch (Exception e) {
						
						Log.i(AsyncRequestExecutor.class.getSimpleName(), errorResponseIO, e);
					}
					
					return;
				}
				
				AsyncHandler<Object> asyncHandler = null;
				
				for (Object object : requestArgs) { //find the provided AsyncHandler (if any)
						
					if(object instanceof AsyncHandler) {
						
						asyncHandler = AsyncHandler.class.cast(object);
						break;
					}
				}
				
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				boolean successful = statusCode > 199 && statusCode < 300;
				
				if(asyncHandler != null) { //response handling has to commence
					
					if(successful) {
						
						try {
							
							Object reponseEntity = ResponseHandlers.BASIC.handle(httpResponse, config);
							asyncHandler.onSuccess(httpResponse, reponseEntity);
						}
						catch (Exception e) {
							
							Log.e(AsyncRequestExecutor.class.getSimpleName(), 
								  "Callback \"onSuccess\" aborted with an exception.", e);
						}
					}
					else { 
						
						try {
							
							EntityUtils.toString(httpResponse.getEntity());
							asyncHandler.onFailure(httpResponse);
						}
						catch (IOException ioe) {
							
							Log.i(AsyncRequestExecutor.class.getSimpleName(), errorResponseIO, ioe);
						}
						catch (ParseException pe) {

							Log.i(AsyncRequestExecutor.class.getSimpleName(), errorResponseIO, pe);
						}
						catch (Exception e) {
							
							Log.e(AsyncRequestExecutor.class.getSimpleName(), 
								  "Callback \"onFailure\" aborted with an exception.", e);
						}
					}
				}
			}
		});
		
		return null;
	}
}
