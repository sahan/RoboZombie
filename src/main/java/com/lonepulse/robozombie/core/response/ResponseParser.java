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
 * <p>This interface policy of a <i>response parser</i>. i.e. the unit which 
 * examines the content of a {@link HttpResponse} and parses that content 
 * into the desired entity.</p>
 * 
 * @version 1.1.4
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface ResponseParser<T extends Object> {

	/**
	 * <p>Executes the following steps for parsing in order. 
	 * <br><br>
	 * <ol>
	 * 	 <li>Check type compatibility</li>
	 *   <li>Process response headers</li>
	 *   <li>Process response body</li>
	 * </ol>
	 * 
	 * @param httpResponse
	 * 				the {@link HttpResponse} from which the content is extracted
	 * 
	 * @param config
	 * 				the {@link ProxyInvocationConfiguration} which supplies all information 
	 * 				regarding the request and it's invocation
	 * <br><br>
	 * @return the entity which is created after parsing the output
	 * <br><br>
	 * @since 1.1.1
	 */
	public T parse(HttpResponse httpResponse, ProxyInvocationConfiguration config);
}