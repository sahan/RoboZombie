package com.lonepulse.robozombie.core.processor.validator;

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


import java.net.URI;

import org.apache.http.client.utils.URIUtils;

import com.lonepulse.robozombie.core.annotation.Endpoint;
import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;

/**
 * <p>A concrete implementation of {@link EndpointValidator} which 
 * validates endpoints. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class EndpointValidator implements Validator<URI> {

	/**
	 * <p>Validates an endpoint annotated with {@link Endpoint}.</p>
	 * 
	 * <p>See {@link Validator#validate(ProxyInvocationConfiguration)}.
	 * 
	 * @since 1.1.0
	 */
	@Override
	public URI validate(ProxyInvocationConfiguration config) throws ValidationFailedException {

		Class<?> endpointInterface = config.getEndpointClass();
		
		try {
			
			if(!endpointInterface.isAnnotationPresent(Endpoint.class))
				throw new MissingEndpointAnnotationException(endpointInterface, Endpoint.class);
			
			String scheme = endpointInterface.getAnnotation(Endpoint.class).scheme();
			String host = endpointInterface.getAnnotation(Endpoint.class).value();
			String port = endpointInterface.getAnnotation(Endpoint.class).port();
			String path = endpointInterface.getAnnotation(Endpoint.class).path();
			
			return URIUtils.createURI(scheme, host, (port.equals("")? 80 :Integer.parseInt(port)), path, "", "");
		}
		catch(Exception e) {
			
			throw new EndpointValidationFailedException(endpointInterface, e);
		}
	}
}
