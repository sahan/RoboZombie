package com.lonepulse.robozombie.proxy;

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

import com.lonepulse.robozombie.RoboZombieRuntimeException;

/**
 * <p>This runtime exception is thrown when a <b>proxy invocation</b> was unsuccessful due to an error in 
 * any of the following aspects:</p>
 * 
 * <ol>
 * 	<li>Request Processing</li>
 * 	<li>Request Execution</li>
 * 	<li>Response Processing</li>
 * </ol>
 *
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public final class InvocationException extends RoboZombieRuntimeException {
	

	private static final long serialVersionUID = -6306853618935833711L;
	
	private final HttpResponse response;
	
	private final InvocationContext context;
	
	
	/**
	 * <p>Creates a new instance of {@link InvocationException} with the {@link HttpResponse} (with 
	 * the failed status code) and the {@link InvocationContext} which resulted in this response.<p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which initiated the failed request
	 * <br><br>
	 * @param response
	 * 			the {@link HttpResponse} which contains the failure status code 
	 * <br><br>
	 * @since 1.3.0
	 */
	public static InvocationException newInstance(InvocationContext context, HttpResponse response) {
		
		return new InvocationException(context, response);
	}
	
	/**
	 * <p>Creates a new instance of {@link InvocationException} with the {@link HttpResponse} (with 
	 * the failed status code) and the {@link InvocationContext} which resulted in this response, while 
	 * preserving the stacktrace. If the given root cause is already a {@link InvocationException}, 
	 * it's returned as it is.<p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which initiated the failed request
	 * <br><br>
	 * @param response
	 * 			the {@link HttpResponse} which contains the failure status code 
	 * <br><br>
	 * @param rootCause
	 * 			the root {@link Throwable} which resulted in an execution failure 
	 * <br><br>
	 * @since 1.3.0
	 */
	public static InvocationException newInstance(InvocationContext context, HttpResponse response, Throwable rootCause) {
		
		if(rootCause instanceof InvocationException) {
			
			return (InvocationException)rootCause;
		}
		
		return new InvocationException(context, response, rootCause);
	}
	
	/**
	 * <p>Creates a new instance of {@link InvocationException} for a failed request <b>execution</b> 
	 * with the {@link InvocationContext} which was used to construct the request. If the given root 
	 * cause is already a {@link InvocationException}, it's returned as it is.<p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which initiated the failed request
	 * <br><br>
	 * @param rootCause
	 * 			the root {@link Throwable} which resulted in an execution failure 
	 * <br><br>
	 * @since 1.3.0
	 */
	public static InvocationException newInstance(InvocationContext context, Throwable rootCause) {
		
		if(rootCause instanceof InvocationException) {
			
			return (InvocationException)rootCause;
		}
		
		return new InvocationException(context, rootCause);
	}
	
	private InvocationException(InvocationContext context, HttpResponse response) {
		
		super(new StringBuilder().append("Request <").append(context.getRequest().getName())
			  .append("> failed with the status code ").append(response.getStatusLine().getStatusCode())
			  .append(", ").append(response.getStatusLine().getReasonPhrase()).toString());
		
		this.context = context;
		this.response = response;
	}
	
	private InvocationException(InvocationContext context, HttpResponse response, Throwable rootCause) {
		
		super(new StringBuilder().append("Request <").append(context.getRequest().getName())
			  .append("> failed with the status code ").append(response.getStatusLine().getStatusCode())
			  .append(", ").append(response.getStatusLine().getReasonPhrase()).toString(), rootCause);
		
		this.context = context;
		this.response = response;
	}
	
	private InvocationException(InvocationContext context, Throwable rootCause) {
		
		super(new StringBuilder().append("Failed to execute request <")
			  .append(context.getRequest().getName()).append(">").toString(), rootCause);
		
		this.context = context;
		this.response = null;
	}

	/**
	 * <p>Retrieves the {@link HttpResponse} which was returned for the request.</p>
	 * 
	 * <p><b>Note</b> that if this instance of {@link InvocationException} signals an <b>error</b> 
	 * which resulted before or during request execution, an {@link HttpResponse} will not be available. 
	 * Use {@link #hasResponse()} to determine if a response is available.</p> 
	 *
	 * @return the {@link HttpResponse} for the failed request, else {@code null} if this instance 
	 * 		   signals an <b>error</b> which occurred before or during request execution
	 * <br><br>
	 * @since 1.3.0
	 */
	public HttpResponse getResponse() {
		
		return response;
	}
	
	/**
	 * <p>Determines if this instance represents a failure which resulted after request execution; 
	 * in which case {@link #getResponse()} will return the recieved {@link HttpResponse}.</p> 
	 *
	 * @return {@code true} if this instance represents a <b>request failure</b> and the received 
	 * 		   {@link HttpResponse} is available, else {@code false} if it represents an <b>execution 
	 * 		   error</b> and {@link #getResponse()} returns {@code null} 
	 * <br><br>
	 * @since 1.3.0
	 */
	public boolean hasResponse() {
		
		return response != null;
	}

	/**
	 * <p>Retrieves the {@link InvocationContext} which initiated the failed request.</p>
	 *
	 * @return the {@link InvocationContext} for the failed request
	 * <br><br>
	 * @since 1.3.0
	 */
	public InvocationContext getContext() {
		
		return context;
	}
}
