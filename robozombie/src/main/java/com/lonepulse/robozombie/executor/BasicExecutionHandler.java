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
	 * <p>Throws a {@link RequestFailedException} with the {@link HttpResponse} and {@link InvocationContext}.</p> 
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

		throw RequestFailedException.newInstance(response, context);
	}

	/**
	 * <p><b>No special action is taken for successful responses. This callback is mute.</b></p>
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public void onSuccess(HttpResponse response, InvocationContext context) {}

	/**
	 * <p>Throws a {@link RequestFailedException} with the {@link InvocationContext}.</p>
	 * 
	 * <p>See {@link ExecutionHandler#onError(InvocationContext, Exception)}</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} with information on the proxy invocation 
	 * <br><br>
	 * @param error
	 * 			the root {@link Throwable} cause for the errored request execution  
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public void onError(InvocationContext context, Exception error) {
		
		throw RequestFailedException.newInstance(context, error);
	}
}
