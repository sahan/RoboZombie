package com.lonepulse.robozombie.request;

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

import com.lonepulse.robozombie.inject.ProxyInvocationConfiguration;

/**
 * <p>This contract defines the services for a strategy which creates the {@link HttpRequest} base 
 * for a proxy invocation. This may be based on the {@link RequestMethod}, any annotated metadata 
 * or other parameters on the endpoint method declaration.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface RequestTranslator {

	/**
	 * <p>Takes the {@link ProxyInvocationConfiguration} of a request and translates it to a <b>basic</b> 
	 * implementation of {@link HttpRequest}. No extra processing, such as header manipulation or parameter 
	 * population, will be done on the returned request. These will be completed at successive stages. 
	 *
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} whose <i>matching</i> {@link HttpRequest} is created 
	 * 
	 * @return the {@link HttpRequest} implementation which suits the given {@link ProxyInvocationConfiguration}
	 * 
	 * @throws RequestTranslationException
	 * 			if {@link ProxyInvocationConfiguration} to {@link HttpRequest} translation failed
	 * 
	 * @since 1.2.4
	 */
	HttpRequestBase translate(ProxyInvocationConfiguration config) throws RequestTranslationException;
}
