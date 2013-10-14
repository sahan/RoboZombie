package com.lonepulse.robozombie.executor;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 Lonepulse
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
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

import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HttpContext;
import org.apache.http42.util.EntityUtils;

import android.util.Log;

import com.lonepulse.robozombie.annotation.Stateful;
import com.lonepulse.robozombie.inject.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.processor.Processors;
import com.lonepulse.robozombie.response.AsyncHandler;

/**
 * <p>A concrete implementation of {@link RequestExecutor} which executes 
 * {@link HttpRequest}s asynchronously.</p>
 * 
 * <p>The thread pool is managed by {@link MultiThreadedHttpClient} using 
 * a {@link PoolingClientConnectionManager}.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class AsyncRequestExecutor implements RequestExecutor {

	
	private static final String CONTEXT = "AsyncRequestExecutor";
	
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
						Log.i(CONTEXT, pendingRequests.size() + " asynchronous requests aborted.");
						
						if(!ASYNC_EXECUTOR_SERVICE.awaitTermination(10, TimeUnit.SECONDS)) {
							
							Log.e(CONTEXT, "Failed to shutdown the cached thread pool for asynchronous requests.");
						}
					}
				}
				catch (InterruptedException ie) {

					List<Runnable> pendingRequests = ASYNC_EXECUTOR_SERVICE.shutdownNow();
					Log.i(CONTEXT, pendingRequests.size() + " asynchronous requests aborted.");
					
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
		
				AsyncHandler<Object> asyncHandler = null;
				
				try {
					
					Object[] requestArgs = config.getRequestArgs();
					
					if(requestArgs != null) {
					
						for (Object object : requestArgs) {
							
							if(object instanceof AsyncHandler) {
								
								asyncHandler = AsyncHandler.class.cast(object);
								break;
							}
						}
					}
					
					Class<?> endpointClass = config.getEndpointClass();
					HttpResponse httpResponse;
					
					if(endpointClass.isAnnotationPresent(Stateful.class)) {
							
						HttpContext httpContext = HttpContextDirectory.INSTANCE.get(endpointClass);
						httpResponse = HttpClientDirectory.INSTANCE.get(endpointClass).execute(httpRequestBase, httpContext);
					}
					else {
							
						httpResponse = HttpClientDirectory.INSTANCE.get(endpointClass).execute(httpRequestBase);
					}
					
					if(asyncHandler == null) {
						
						EntityUtils.consumeQuietly(httpResponse.getEntity());
						return;
					}
					else {
					
						int statusCode = httpResponse.getStatusLine().getStatusCode();
						boolean successful = statusCode > 199 && statusCode < 300;
						
						if(successful) {
							
							Object reponseEntity = Processors.RESPONSE.run(httpResponse, config);
							
							try {
								
								asyncHandler.onSuccess(httpResponse, reponseEntity);
							}
							catch (Exception e) {
								
								Log.e(CONTEXT, "Callback \"onSuccess\" aborted with an exception.", e);
							}
						}
						else { 
							
							EntityUtils.consumeQuietly(httpResponse.getEntity());
							
							try {
								
								asyncHandler.onFailure(httpResponse);
							}
							catch (Exception e) {
								
								Log.e(CONTEXT, "Callback \"onFailure\" aborted with an exception.", e);
							}
						}
					}
				}
				catch(Exception error) {
					
					if(asyncHandler != null) {
						
						try {
						
							asyncHandler.onError(error);
						}
						catch(Exception e) {
							
							Log.e(CONTEXT, "Callback \"onError\" aborted with an exception.", e);
						}
					}
					else {
						
						Log.e(CONTEXT, "Asynchronous request execution failed. ", error);
					}
				}
			}
		});
		
		return null;
	}
}
