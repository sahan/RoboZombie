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

import static com.lonepulse.robozombie.util.Is.successful;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import com.lonepulse.robozombie.annotation.Stateful;
import com.lonepulse.robozombie.proxy.InvocationContext;
import com.lonepulse.robozombie.proxy.InvocationException;

/**
 * <p>An implementation of {@link RequestExecutor} which isolates responsibilities that are common to 
 * all concrete {@link RequestExecutor}s.</p> 
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class BasicRequestExecutor implements RequestExecutor {
	
	
	private final ExecutionHandler executionHandler;
	
	
	/**
	 * <p>Creates a new {@link BasicRequestExecutor} with the given {@link ExecutionHandler}.</p>
	 *
	 * @param executionHandler
	 * 			the {@link ExecutionHandler} which will be invoked during request execution
	 * <br><br>
	 * @since 1.2.4
	 */
	BasicRequestExecutor(ExecutionHandler executionHandler) {
		
		this.executionHandler = executionHandler;
	}
	
	/**
	 * <p>Performs the actual request execution with the {@link HttpClient} to be used for the endpoint 
	 * (fetched using the {@link HttpClientDirectory}). See {@link HttpClient#execute(HttpUriRequest)}.</p>
	 * 
	 * <p>If the endpoint is annotated with @{@link Stateful}, the relevant {@link HttpContext} from the 
	 * {@link HttpContextDirectory} is used. See {@link HttpClient#execute(HttpUriRequest, HttpContext)}</p>
	 *
	 * @param context
	 * 			the {@link InvocationContext} used to discover information about the proxy invocation
	 * <br><br>
	 * @param request
	 * 			the {@link HttpRequestBase} to be executed using the endpoint's {@link HttpClient}
	 * <br><br>
	 * @return the {@link HttpResponse} which resulted from the execution
	 * <br><br>
	 * @since 1.2.4
	 */
	protected HttpResponse fetchResponse(InvocationContext context, HttpRequestBase request) {

		try {
		
			Class<?> endpoint = context.getEndpoint();
			
			HttpClient httpClient = HttpClientDirectory.INSTANCE.lookup(endpoint);
			
			return endpoint.isAnnotationPresent(Stateful.class)? 
					httpClient.execute(request, HttpContextDirectory.INSTANCE.lookup(endpoint)) 
					:httpClient.execute(request);
		}
		catch(Exception e) {
			
			throw new RequestExecutionException(context.getRequest(), context.getEndpoint(), e);
		}
	}
	
	/**
	 * <p>Executes an {@link HttpRequestBase} using the endpoint's {@link HttpClient} and handles the 
	 * resulting {@link HttpResponse} using this executor's {@link ExecutionHandler}.</p>
	 * 
	 * <p>See {@link #fetchResponse(InvocationContext, HttpRequestBase)}</p>
	 * 
	 * <p>See {@link Is#successful(HttpResponse)}</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} used to discover information about the proxy invocation
	 * <br><br>
	 * @param request
	 * 			the {@link HttpRequestBase} to be executed using the endpoint's {@link HttpClient}
	 * <br><br>
	 * @throws InvocationException
	 * 			if the HTTP request responded with a failure status code or if request execution failed
	 * <br><br>
	 * @since 1.1.0
	 */
	@Override
	public HttpResponse execute(InvocationContext context, HttpRequestBase request) {
		
		HttpResponse response = null;
		
		try {
			
			 response = fetchResponse(context, request);
		}
		catch(RequestExecutionException ree) {
			
			executionHandler.onError(context, ree);
		}
		
		if(response != null) {
			
			if(successful(response)) {
				
				executionHandler.onSuccess(context, response);	
			}
			else {
				
				executionHandler.onFailure(context, response);
			}
		}
		
		return response;
	}
}
