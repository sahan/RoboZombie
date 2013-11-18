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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.annotation.Async;
import com.lonepulse.robozombie.inject.InvocationContext;
import com.lonepulse.robozombie.processor.Processors;
import com.lonepulse.robozombie.response.AsyncHandler;

/**
 * <p>This is an implementation of {@link ExecutionHandler} which manages {@link AsyncHandler}s that may be 
 * used in <b>asynchronous request execution</b>. It is to be used with {@link RequestExecutor}s that support 
 * asynchronous request execution (for endpoint requestst annotated with @{@link Async}).</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class AsyncExecutionHandler implements ExecutionHandler {
	

	private static final Log LOGGER = LogFactory.getLog(AsyncRequestExecutor.class);
	
	
	@SuppressWarnings("unchecked") //safe cast from Object to AsyncHandler
	private static final AsyncHandler<Object> getAsyncHandler(InvocationContext context) {
		
		AsyncHandler<Object> asyncHandler = null;
		
		List<Object> requestArgs = context.getArguments();
		
		if(requestArgs != null) {
		
			for (Object object : requestArgs) {
				
				if(object instanceof AsyncHandler) {
					
					asyncHandler = AsyncHandler.class.cast(object);
					break;
				}
			}
		}
		
		return asyncHandler;
	}
	
	
	/**
	 * <p>The given {@link HttpResponse} with a successful status code is processed using the response processor 
	 * chain ({@link Processors#RESPONSE} and if an {@link AsyncHandler} is defined, the result of the processor 
	 * chain is submitted to the <i>onSuccess</i> callback.</p>
	 *  
	 * <p>See {@link ExecutionHandler#onSuccess(HttpResponse, InvocationContext)}</p>
	 * 
	 * @param response
	 * 			the resulting {@link HttpResponse} with a successful status code
	 * <br><br>
	 * @param context
	 * 			the {@link InvocationContext} with information on the proxy invocation 
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public void onSuccess(HttpResponse response, InvocationContext context) {
		
		Object reponseEntity = Processors.RESPONSE.run(response, context); //process, regardless of an AsyncHandler definition
		
		AsyncHandler<Object> asyncHandler = getAsyncHandler(context);
		
		if(asyncHandler != null) {
			
			try {
				
				asyncHandler.onSuccess(response, reponseEntity);
			}
			catch (Exception e) {
				
				LOGGER.error("Callback \"onSuccess\" aborted with an exception.", e);
			}
		}
	}

	/**
	 * <p>If an {@link AsyncHandler} is defined, the given {@link HttpResponse} with a failed status code 
	 * is submitted to the <i>onFailure</i> callback.</p>
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

		AsyncHandler<Object> asyncHandler = getAsyncHandler(context);
		
		if(asyncHandler != null) {
		
			try {
				
				asyncHandler.onFailure(response);
			}
			catch (Exception e) {
				
				LOGGER.error("Callback \"onFailure\" aborted with an exception.", e);
			}
		}
		
		Processors.RESPONSE.run(response, context); //process, regardless of a failed response
	}

	/**
	 * <p>If an {@link AsyncHandler} is defined, the exception is submitted to the <i>onError</i> callback.</p>
	 *  
	 * <p>See {@link ExecutionHandler#onError(InvocationContext, Exception)}</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} with information on the proxy invocation 
	 * <br><br>
	 * @param error
	 * 			the exception which resulted in a request execution failure 
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public void onError(InvocationContext context, Exception error) {
		
		AsyncHandler<Object> asyncHandler = getAsyncHandler(context);
		
		if(asyncHandler != null) {
			
			try {
			
				asyncHandler.onError(error);
			}
			catch(Exception e) {
				
				LOGGER.error("Callback \"onError\" aborted with an exception.", e);
			}
		}
	}
}
