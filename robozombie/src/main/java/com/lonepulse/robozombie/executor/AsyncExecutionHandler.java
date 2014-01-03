package com.lonepulse.robozombie.executor;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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

import org.apache.http.HttpResponse;

import android.util.Log;

import com.lonepulse.robozombie.annotation.Async;
import com.lonepulse.robozombie.processor.Processors;
import com.lonepulse.robozombie.proxy.InvocationContext;
import com.lonepulse.robozombie.proxy.InvocationException;
import com.lonepulse.robozombie.response.AsyncHandler;

/**
 * <p>This is an implementation of {@link ExecutionHandler} which manages {@link AsyncHandler}s that 
 * may be used in <b>asynchronous requests</b>. It should be used with {@link RequestExecutor}s that 
 * support asynchronous request execution (i.e. those requests annotated with @{@link Async}).</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public final class AsyncExecutionHandler implements ExecutionHandler {
	

	@SuppressWarnings("unchecked") //safe cast from Object to AsyncHandler
	private static AsyncHandler<Object> getAsyncHandler(InvocationContext context) {
		
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
	 * <p>The given {@link HttpResponse} with a successful status code is processed using the response 
	 * processor chain ({@link Processors#RESPONSE} and if an {@link AsyncHandler} is defined, the result 
	 * of the processor chain is submitted to the <i>onSuccess</i> callback. If response processing 
	 * resulted in an error, execution defers to {@link #onError(InvocationContext, Exception)} instead 
	 * and the success callback of the {@link AsyncHandler} is skipped.</p>
	 * 
	 * <p>See {@link ExecutionHandler#onSuccess(InvocationContext, HttpResponse)}</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} with information on the proxy invocation 
	 * <br><br>
	 * @param response
	 * 			the resulting {@link HttpResponse} with a successful status code 
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	public void onSuccess(InvocationContext context, HttpResponse response) {
		
		Object reponseEntity = null;
		
		try {
		
			reponseEntity = Processors.RESPONSE.run(context, response); //process, regardless of an AsyncHandler definition
		}
		catch(Exception e) {
			
			onError(context, InvocationException.newInstance(context, response, e));
			return;
		}
		
		AsyncHandler<Object> asyncHandler = getAsyncHandler(context);
		
		if(asyncHandler != null) {
			
			try {
				
				asyncHandler.onSuccess(response, reponseEntity);
			}
			catch (Exception e) {
				
				Log.e(getClass().getSimpleName(), "Callback \"onSuccess\" aborted with an exception.", e);
			}
		}
	}

	/**
	 * <p>The given {@link HttpResponse} with a successful status code is processed using the response 
	 * processor chain ({@link Processors#RESPONSE} and if an {@link AsyncHandler} is defined, the given 
	 * {@link HttpResponse} with a failed status code is submitted to the <i>onFailure</i> callback. If 
	 * response processing resulted in an error, execution defers to {@link #onError(InvocationContext, 
	 * Exception)} instead and the success callback of the {@link AsyncHandler} is skipped.</p>
	 * 
	 * <p>See {@link ExecutionHandler#onFailure(InvocationContext, HttpResponse)}</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} with information on the proxy invocation 
	 * <br><br>
	 * @param response
	 * 			the resulting {@link HttpResponse} with a failed status code
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	public void onFailure(InvocationContext context, HttpResponse response) {

		try {
			
			Processors.RESPONSE.run(context, response); //process, regardless of a failed response
		}
		catch(Exception e) {
			
			onError(context, InvocationException.newInstance(context, response, e));
			return;
		}
		
		AsyncHandler<Object> asyncHandler = getAsyncHandler(context);
		
		if(asyncHandler != null) {
		
			try {
				
				asyncHandler.onFailure(response);
			}
			catch (Exception e) {
				
				Log.e(getClass().getSimpleName(), "Callback \"onFailure\" aborted with an exception.", e);
			}
		}
	}

	/**
	 * <p>If an {@link AsyncHandler} is defined, any exception which resulted in an error will be 
	 * available via the <i>onError</i> callback.</p>
	 * 
	 * <p>See {@link ExecutionHandler#onError(InvocationContext, Exception)}</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} with information on the proxy invocation 
	 * <br><br>
	 * @param error
	 * 			the exception which resulted in a request execution failure 
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	public void onError(InvocationContext context, Exception error) {
		
		AsyncHandler<Object> asyncHandler = getAsyncHandler(context);
		
		if(asyncHandler != null) {
			
			try {
			
				asyncHandler.onError(error instanceof InvocationException? 
						(InvocationException)error :InvocationException.newInstance(context, error));
				}
			catch(Exception e) {
				
				Log.e(getClass().getSimpleName(), "Callback \"onError\" aborted with an exception.", e);
			}
		}
	}
}
