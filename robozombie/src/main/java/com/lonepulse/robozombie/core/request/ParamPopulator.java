package com.lonepulse.robozombie.core.request;

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


import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;

/**
 * <p>This contract specifies a strategy for populating the <i>parameters</i> on an HTTP request.</p> 
 * 
 * <p>All implementations must bear in mind that a <b>generic</b> model is used for indicating the 
 * <b>request parameters</b> for any <b>Request-Method</b>. This is in the form of an annotation named 
 * {@code @Param} on an argument to an endpoint interface method.</p>
 * 
 * <p>It is advised to follow <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html">Section 
 * 9</a> of the <b>HTTP 1.1</b> RFC when designing an implementation for a Request-Method.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface ParamPopulator {

	/**
	 * <p>Takes the {@link ProxyInvocationConfiguration} for a request and creates an {@link HttpRequestBase} 
	 * <i>implementation</i> which coincides with its {@link RequestMethod}. Next, it discovers any <b>Request 
	 * Parameters</b> in the configuration and inserts them into the {@link HttpRequest} in a format which 
	 * conforms with the specification for each {@link RequestMethod}.</p>
	 *
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} which is used to discover the request's 
	 * 			{@link RequestMethod} and any <b>request parameters</b> to be inserted
 	 *
	 * @return an {@link HttpRequestBase} which coincides with the request's {@link RequestMethod}, along 
	 * 		   with all <b>request parameters</b> which were to be inserted
	 * 
	 * @throws ParamPopulatorException
	 * 			if a {@link HttpRequestBase} failed to be created or if a request parameter failed to be inserted 
	 * 
	 * @since 1.2.4
	 */
	HttpRequestBase populate(ProxyInvocationConfiguration config) throws ParamPopulatorException;
}
