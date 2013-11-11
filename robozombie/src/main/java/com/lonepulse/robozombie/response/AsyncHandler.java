package com.lonepulse.robozombie.response;

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

import com.lonepulse.robozombie.annotation.Deserializer;

/**
 * <p>To be used with <b>asynchronous request execution</b> for retrieving the deserialized response content or 
 * for gaining insight into any failures and errors which might have occurred.</p>
 * 
 * <p>Note that <i>response handling</i> will not commence without an {@link AsyncHandler} in an endpoint 
 * request definition</p>.  
 * 
 * <p>This contract defines the following callbacks:</p>
 * <ul>
 * 	<li>{@link #onSuccess(HttpResponse, Object)} - handle a successful execution</li>
 * 	<li>{@link #onFailure(HttpResponse)} - handle a failed response</li>
 * 	<li>{@link #onError(Exception)} - handle an erroneous execution</li>
 * </ul>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public abstract class AsyncHandler<RESPONSE extends Object> {

	/**
	 * <p>Use this callback to handle a <i>successful request execution</i>.</p>
	 * 
	 * @param httpResponse
	 * 			the {@link HttpResponse} which was returned for a successful request execution
	 * <br><br>
	 * @param response
	 * 			the response content deserialized as specified by @{@link Deserializer}
	 * <br><br>
	 * @since 1.1.0
	 */
	public abstract void onSuccess(HttpResponse httpResponse, RESPONSE response);

	/**
	 * <p>Use this callback method to handle a <i>failed HTTP response</i>.</p>
	 * 
	 * <p>A successful response must contain an <a href="http://en.wikipedia.org/wiki/List_of_HTTP_status_codes">
	 * HTTP status code</a> of range <b>2xx</b>. Any other response code is considered to be failure and initiates 
	 * this callback.</p>
	 * 
	 * <p><b>Note</b> that the default implementation does absolutely nothing. A minimal usage would be to override 
	 * this callback, retrieve the HTTP status code and act upon it as necessary.</p>
	 * 
	 * @param httpResponse
	 * 			the resulting {@link HttpResponse} containing a failed status code 
	 * <br><br>
	 * @since 1.1.0
	 */
	public void onFailure(HttpResponse httpResponse){}
	
	/**
	 * <p>Callback to handle an <i>erroneous request execution</i>.</p>
	 * 
	 * <p>This callback will be invoked if request execution failed with an {@link Exception}. This signifies 
	 * a failure to execute the request or handle the response; unlike {@link #onFailure(HttpResponse)} which 
	 * indicates the return of a non-successful <a href="http://en.wikipedia.org/wiki/List_of_HTTP_status_codes">
	 * HTTP status code</a>.</p>
	 *  
	 * <p><b>Note</b> that the default implementation does absolutely nothing. A minimal usage would be to override 
	 * this callback and log any exceptions.</p>
	 * 
	 * @param error
	 * 			the {@link Exception} which caused a failure in asynchronous request execution
	 * <br><br>
	 * @since 1.2.4
	 */
	public void onError(Exception error){}
}