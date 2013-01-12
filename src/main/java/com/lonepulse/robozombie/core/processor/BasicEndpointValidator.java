package com.lonepulse.robozombie.core.processor;

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

/**
 * <p>Validates the structure of an endpoint interface.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class BasicEndpointValidator implements EndpointValidator {


	/**
	 * {@inheritDoc}
	 */
	@Override
	public URI validate(Class<?> endpointInterface) {
		
		try {
			
			if(!endpointInterface.isAnnotationPresent(Endpoint.class))
				throw new EndpointMissingAnnotationException(endpointInterface, Endpoint.class);
			
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
