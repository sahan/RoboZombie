package com.lonepulse.robozombie.processor;

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

import static com.lonepulse.robozombie.annotation.Entity.ContentType.JSON;

import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.annotation.Async;
import com.lonepulse.robozombie.annotation.Deserializer;
import com.lonepulse.robozombie.annotation.Detach;
import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.GET;
import com.lonepulse.robozombie.model.User;
import com.lonepulse.robozombie.response.AsyncHandler;

/**
 * <p>An endpoint which tests asynchronous request invocation.</p>
 * 
 * @version 1.2.0
 * <br><br> 
 * @since 1.2.4
 * <br><br> 
 * @category test
 * <br><br> 
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Async @Endpoint(host = "0.0.0.0", port = 8080)
public interface AsyncEndpoint {
	
	
	/**
	 * <p>Sends a request asynchronously using @{@link Async} and {@link AsyncHandler}.</p>
	 * 
	 * @param asyncHandler
	 * 			the {@link AsyncHandler} which handles the results of the asynchronous request
	 * 
	 * @return {@code null}, since the request is processed asynchronously
	 * 
	 * @since 1.2.4
	 */
	@GET("/asyncsuccess")
	String asyncSuccess(AsyncHandler<String> asyncHandler);
	
	/**
	 * <p>Sends a request asynchronously using @{@link Async} and {@link AsyncHandler} which returns response 
	 * code that signifies a failure. This should invoke {@link AsyncHandler#onFailure(HttpResponse)} on the 
	 * provided callback.</p> 
	 * 
	 * @param asyncHandler
	 * 			the {@link AsyncHandler} which handles the results of the asynchronous request
	 * 
	 * @since 1.2.4
	 */
	@GET("/asyncfailure")
	void asyncFailure(AsyncHandler<String> asyncHandler);
	
	/**
	 * <p>Sends a request asynchronously using @{@link Async} and {@link AsyncHandler} whose execution is 
	 * expected to fail with an exception and hence handled by the callback {@link AsyncHandler#onError(Exception)}.</p>
	 * 
	 * <p>The error is caused by the deserializer which attempts to parse the response content, which is 
	 * not JSON, into the {@link User} model.</p> 
	 * 
	 * @param asyncHandler
	 * 			the {@link AsyncHandler} which handles the results of the asynchronous request
	 * 
	 * @since 1.3.4
	 */
	@Deserializer(JSON)
	@GET("/asyncerror")
	void asyncError(AsyncHandler<User> asyncHandler);
	
	/**
	 * <p>Sends a request asynchronously using @{@link Async} but does not expect the response to be 
	 * handled using an {@link AsyncHandler}.</p>
	 * 
	 * @since 1.2.4
	 */
	@GET("/asyncnohandling")
	void asyncNoHandling();
	
	/**
	 * <p>Processes a successful execution, but the user provided implementation of the callback 
	 * {@link AsyncHandler#onSuccess(HttpResponse, Object)} throws an exception.</p>
	 * 
	 * @param asyncHandler
	 * 			the {@link AsyncHandler} which is expected to throw an exception in <i>onSuccess</i>
	 * 
	 * @since 1.2.4
	 */
	@GET("/successcallbackerror")
	void asyncSuccessCallbackError(AsyncHandler<String> asyncHandler);
	
	/**
	 * <p>Processes a failed execution, but the user provided implementation of the callback 
	 * {@link AsyncHandler#onFailure(HttpResponse)} throws an exception.</p>
	 * 
	 * @param asyncHandler
	 * 			the {@link AsyncHandler} which is expected to throw an exception in <i>onFailure</i>
	 * 
	 * @since 1.2.4
	 */
	@GET("/failurecallbackerror")
	void asyncFailureCallbackError(AsyncHandler<String> asyncHandler);
	
	/**
	 * <p>Processes an erroneous execution, but the user provided implementation of the callback 
	 * {@link AsyncHandler#onError(Exception)} throws an exception itself.</p>
	 * 
	 * @param asyncHandler
	 * 			the {@link AsyncHandler} which is expected to throw an exception in <i>onError</i>
	 * 
	 * @since 1.2.4
	 */
	@Deserializer(JSON)
	@GET("/errorcallbackerror")
	void asyncErrorCallbackError(AsyncHandler<User> asyncHandler);
	
	/**
	 * <p>Sends a request <b>synchronously</b> by detaching the inherited @{@link Async} annotation.</p> 
	 * 
	 * @return the response string which indicated a synchronous request
	 * 
	 * @since 1.2.4
	 */
	@Detach(Async.class) 
	@GET("/asyncdetached")
	String asyncDetached();
}
