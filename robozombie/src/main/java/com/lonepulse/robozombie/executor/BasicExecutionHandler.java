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


import java.io.IOException;

import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This is a minimal implementation of {@link ExecutionHandler} for use with {@link RequestExecutor}s.</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class BasicExecutionHandler implements ExecutionHandler {


	/**
	 * <p>A {@link RequestExecutionException} is thrown with information on the failed {@link HttpResponse}.</p> 
	 * 
	 * <p>See {@link ExecutionHandler#onFailure(HttpResponse, InvocationContext)}</p>
	 * 
	 * @param response
	 * 			the resulting {@link HttpResponse} with a failed status code
	 * <br><br>
	 * @param context
	 * 			the {@link InvocationContext} with information on the proxy invocation 
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public void onFailure(HttpResponse response, InvocationContext context) {
		
		StringBuilder errorContext = new StringBuilder()
		.append("HTTP request for ")
		.append(context.getRequest().getName())
		.append(" failed with status code ")
		.append(response.getStatusLine().getStatusCode())
		.append(", ")
		.append(response.getStatusLine().getReasonPhrase());
		
		throw new RequestExecutionException(errorContext.toString(), new IOException(errorContext.toString()));
	}

	/**
	 * <p><b>No special action is taken for successful responses. This callback is mute.</b></p>
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public void onSuccess(HttpResponse response, InvocationContext context) {}

	/**
	 * <p><b>No special action is taken upon erroneous executions. This callback is mute.</b></p>
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public void onError(InvocationContext context, Exception error) {}
}
