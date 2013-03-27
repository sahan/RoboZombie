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


import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;

/**
 * <p>Specifies the contract for handling HTTP responses. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface ResponseHandler {

	/**
	 * <p>Processes the given {@link HttpResponse} and returns its results 
	 * for consumption.
	 * 
	 * @param httpResponse
	 * 			the {@link HttpResponse} to be processed
	 * 			
	 * @return the consumable contents of the response
	 * 
	 * @throws ResponseHandlerException
	 * 			if the response handling failed
	 */
	Object handle(HttpResponse httpResponse, ProxyInvocationConfiguration config) 
	throws ResponseHandlerException;
}
