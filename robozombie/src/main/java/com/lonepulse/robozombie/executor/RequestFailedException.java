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

import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This runtime exception is thrown when a <b>synchronous request invocation</b> failed to result 
 * in a response with a successful status code (of type <b>2xx</b>).</p>
 *
 * @category API
 * <br><br>
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class RequestFailedException extends RequestExecutionException {
	

	private static final long serialVersionUID = -6306853618935833711L;
	
	private HttpResponse response;
	
	private InvocationContext context;
	
	
	/**
	 * <p>Creates a new instance of {@link RequestFailedException} with the {@link HttpResponse} 
	 * with the failed status code and the {@link InvocationContext} which resulted in this response.<p>
	 * 
	 * @param response
	 * 			the {@link HttpResponse} returned for the failed request
	 * <br><br>
	 * @param context
	 * 			the {@link InvocationContext} which initiated the failed request
	 * <br><br>
	 * @since 1.2.4
	 */
	static RequestFailedException newInstance(HttpResponse response, InvocationContext context) {
		
		RequestFailedException rfe = new RequestFailedException(response, context);
		rfe.setResponse(response);
		rfe.setContext(context);
		
		return rfe;
	}
	
	/**
	 * <p>Creates a new instance of {@link RequestFailedException} for a failed request <b>execution</b> 
	 * with the {@link InvocationContext} which was used to construct the request.<p>
	 * 
	 * <br><br>
	 * @param context
	 * 			the {@link InvocationContext} which initiated the failed request
	 * @param rootCause
	 * 			the root {@link Throwable} which resulted in an execution failure 
	 * <br><br>
	 * @since 1.2.4
	 */
	static RequestFailedException newInstance(InvocationContext context, Throwable rootCause) {
		
		RequestFailedException rfe = new RequestFailedException(context, rootCause);
		rfe.setContext(context);
		
		return rfe;
	}
	
	private RequestFailedException(HttpResponse response, InvocationContext context) {
		
		super(new StringBuilder().append("Request <").append(context.getRequest().getName())
		.append("> failed with the status code ").append(response.getStatusLine().getStatusCode())
		.append(", ").append(response.getStatusLine().getReasonPhrase()).toString());
	}
	
	private RequestFailedException(InvocationContext context, Throwable rootCause) {
		
		super(new StringBuilder().append("Failed to execute request <")
			  .append(context.getRequest().getName()).append(">").toString(), rootCause);
	}

	/**
	 * <p>Retrieves the {@link HttpResponse} which was returned for the request.</p>
	 * 
	 * <p><b>Note</b> that if this instance of {@link RequestFailedException} signals an <b>error</b> 
	 * which resulted before request execution, an {@link HttpResponse} will not be available. Use 
	 * {@link #hasResponse()} to determine if a response is available.</p> 
	 *
	 * @return the {@link HttpResponse} for the failed request, else {@code null} if this instance 
	 * 		   signals an <b>error</b> which occurred before request execution
	 * <br><br>
	 * @since 1.2.4
	 */
	public HttpResponse getResponse() {
		
		return response;
	}
	
	private void setResponse(HttpResponse response) {
		
		this.response = response;
	}
	
	/**
	 * <p>Determines if this instance represents a failure which resulted after request execution; 
	 * in which case {@link #getResponse()} will return the recieved {@link HttpResponse}.</p> 
	 *
	 * @return {@code true} if this instance represents a <b>request failure</b> and the received 
	 * 		   {@link HttpResponse} is available, else {@code false} if it represents an <b>execution 
	 * 		   error</b> and {@link #getResponse()} returns {@code null} 
	 * <br><br>
	 * @since 1.2.4
	 */
	public boolean hasResponse() {
		
		return response != null;
	}

	/**
	 * <p>Retrieves the {@link InvocationContext} which initiated the failed request.</p>
	 *
	 * @return the {@link InvocationContext} for the failed request
	 * <br><br>
	 * @since 1.2.4
	 */
	public InvocationContext getContext() {
		
		return context;
	}
	
	private void setContext(InvocationContext context) {
		
		this.context = context;
	}
}
