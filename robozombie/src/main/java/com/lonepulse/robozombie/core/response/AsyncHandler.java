package com.lonepulse.robozombie.core.response;

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

import java.lang.reflect.Type;

import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.core.annotation.Parser;
import com.lonepulse.robozombie.core.response.parser.ResponseParser;

/**
 * <p>This is the <i>strategy</i> for a handler which can be used to 
 * process an HTTP request <b>asynchronously</b>.
 * 
 * <p>Executes HTTP request on a separate thread and invokes either 
 * {@link AsyncHandler#onSuccess(HttpResponse, Object)} 
 * or {@link AsyncHandler#onFailure(HttpResponse, Object)} depending 
 * on whether the request executed successfully or not.</p>
 * 
 * @version 1.1.3
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public abstract class AsyncHandler<E extends Object> {

	
	/**
	 * <p>Callback method to handle the a <i>successful request execution</i>.
	 * 
	 * <p>Generic {@code E} is used to identify the {@link Type} of the response 
	 * which is returned via the designated {@link ResponseParser}.
	 *  
	 * @param httpResponse
	 * 			the original instance {@link HttpResponse} returned as a result of 
	 * 			the execution
	 * 
	 * @param e
	 * 			an instance of the response type ({@link HttpResponse} processed 
	 * 			using the specified {@link Parser})
	 * <br><br>
	 * @since 1.1.1
	 */
	public abstract void onSuccess(HttpResponse httpResponse, E e);

	/**
	 * <p>Callback method to handle a <i>failed request execution</i>.
	 * 
	 * <p>Generic {@code E} is used to identify the {@link Type} of the response 
	 * which is returned via the designated {@link ResponseParser}. 
	 *  
	 * <p>Note that the default implementation does absolutely nothing. A minimal 
	 * usage would be to override this method and log the HTTP status code.
	 * 
	 * @param httpResponse
	 * 			the original instance {@link HttpResponse} returned as a result of 
	 * 			the execution
	 * <br><br>
	 * @since 1.1.3
	 */
	public void onFailure(HttpResponse httpResponse){}
}