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

import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>This contract defines the policy for a <i>deserializer</i> which reads the response content of a 
 * successful request execution and converts it to a desired entity type which can be processed in memory.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface Deserializer<OUTPUT> {

	/**
	 * <p>Executes the following steps for deserialization in order. 
	 * <br><br>
	 * <ol>
	 * 	 <li>Check type compatibility</li>
	 *   <li>Process response entity</li>
	 * </ol>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which supplies information on the proxy invocation
	 * <br><br>
	 * @param response
	 * 			the {@link HttpResponse} which resulted from a successful request execution
	 * <br><br>
	 * @return the response content after it has been deserialized to the desired output type
	 * <br><br>
	 * @since 1.2.4
	 */
	OUTPUT run(InvocationContext context, HttpResponse response);
}