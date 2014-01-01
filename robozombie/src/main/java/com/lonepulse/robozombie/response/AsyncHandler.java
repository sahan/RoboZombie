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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.proxy.InvocationException;

/**
 * <p>To be used with <b>asynchronous request execution</b> for retrieving the deserialized response content 
 * or for gaining insight into any failures and errors which might have occurred.</p>
 * 
 * <p>This contract defines the following callbacks:</p>
 * <ul>
 * 	<li>{@link #onSuccess(HttpResponse, Object)} - handle a successful execution</li>
 * 	<li>{@link #onFailure(HttpResponse)} - handle a failed response</li>
 * 	<li>{@link #onError(InvocationException)} - handle an erroneous execution</li>
 * </ul>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public abstract class AsyncHandler<RESPONSE> {

	/**
	 * <p>Use this callback to handle a <i>successful request execution</i>.</p> 
	 * 
	 * <p>Successful responses contain an <a href="http://en.wikipedia.org/wiki/List_of_HTTP_status_codes">
	 * HTTP status code</a> in the range <b>2xx</b>.</p>
	 * 
	 * @param response
	 * 			the {@link HttpResponse} which was returned for a successful request execution
	 * <br><br>
	 * @param content
	 * 			the deserialized response entity or an instance of the required type (e.g. the 
	 * 			{@link HttpEntity} as specified on the request definition   
	 * <br><br>
	 * @since 1.1.0
	 */
	public abstract void onSuccess(HttpResponse response, RESPONSE content);

	/**
	 * <p>Use this callback method to handle a <i>failed HTTP response</i>, in which the response code 
	 * does not fall into the category <b>2xx</b>.</p>
	 * 
	 * <p><b>Note</b> that the default implementation does absolutely nothing. A minimal usage would be 
	 * to override this callback, retrieve the actual status code and act upon it as necessary.</p>
	 * 
	 * @param response
	 * 			the {@link HttpResponse} with a failure status code signifying a failed request
	 * <br><br>
	 * @since 1.1.0
	 */
	public void onFailure(HttpResponse response){}
	
	/**
	 * <p>Use this callback to handle an <i>erroneous request execution</i>.</p>
	 * 
	 * <p>This callback will be invoked if request execution failed with an exception. It signifies a 
	 * failure to execute the request or handle the response; unlike {@link #onFailure(HttpResponse)} 
	 * which indicates a non-successful <a href="http://en.wikipedia.org/wiki/List_of_HTTP_status_codes">
	 * HTTP status code</a>.</p>
	 *  
	 * <p><b>Note</b> that the default implementation does absolutely nothing. A minimal usage would be to 
	 * override this callback and log any exceptions.</p>
	 * 
	 * @param errorContext
	 * 			the {@link InvocationException} with information on the failed request execution
	 * <br><br>
	 * @since 1.3.0
	 */
	public void onError(InvocationException errorContext){}
}