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

import com.lonepulse.robozombie.core.RoboZombieRuntimeException;
import com.lonepulse.robozombie.core.annotation.Parser;

/**
 * <p>This is the <i>strategy</i> for a handler which can be used to process an HTTP request 
 * <b>asynchronously</b>.
 * 
 * <p>Executes HTTP request on a separate thread and invokes either {@link AsyncHandler#onSuccess(HttpResponse, Object)} 
 * or {@link AsyncHandler#onFailure(HttpResponse, Object)} depending on whether the request executed successfully or not.</p>
 * 
 * @version 1.1.2
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public abstract class AsyncHandler {

	
	/**
	 * <p>Callback method to handle the a <i>successful request execution</i>.
	 * 
	 * <p>Generic {@code E} is used to identify the {@link Type} of the response which is 
	 * returned via the designated {@link ResponseParser}.
	 *  
	 * @param httpResponse
	 * 			the original instance {@link HttpResponse} returned as a result of the execution
	 * 
	 * @param e
	 * 			an instance of the response type ({@link HttpResponse} processed using the specified {@link Parser})
	 * 
	 * @throws RoboZombieRuntimeException
	 * 			when processing fails due to some miscellaneous error
	 * <br><br>
	 * @since 1.1.1
	 */
	public abstract <E extends Object> void onSuccess(HttpResponse httpResponse, E e) throws RoboZombieRuntimeException;

	/**
	 * <p>Callback method to handle a <i>failed request execution</i>.
	 * 
	 * <p>Generic {@code E} is used to identify the {@link Type} of the response which is 
	 * returned via the designated {@link ResponseParser}. 
	 *  
	 * <p>Note that the default implementation does absolutely nothing. A minimal usage would 
	 * be to override this method and log the HTTP status code.
	 * 
	 * @param httpResponse
	 * 			the original instance {@link HttpResponse} returned as a result of the execution
	 * 
	 * @param e
	 * 			an instance of the response type ({@link HttpResponse} processed using the specified {@link Parser})
	 * 
	 * @throws RoboZombieRuntimeException
	 * 			when processing fails due to some miscellaneous error
	 * <br><br>
	 * @since 1.1.1
	 */
	public <E extends Object> void onFailure(HttpResponse httpResponse, E e) throws RoboZombieRuntimeException {
	}
}

/*
TODO Async Handler Implementation (NOTES BELOW)

Let them build async handlers by annotating their classes, then link them to the endpoint ""interface"" via another annotation

@AsyncHandler, @OnSuccess, @OnFailure

Or just letting them implement this on the spot is easier right?

Or @OnSuccess on any method, then @AsyncHandler(onSuccess = "processPerson")

@OnSuccess(binding = myCCEndpoint.getUserProfile())
processPerson(HttpRequest httpRequest, Person person)

@OnSuccess
processPerson(HttpRequest httpRequest)

@OnSuccess
processPerson(Person person)
*/
