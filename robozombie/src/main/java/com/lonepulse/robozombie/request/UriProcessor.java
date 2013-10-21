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


import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http42.client.utils.URIBuilder;

import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This is a concrete implementation of {@link RequestProcessor} which extracts the root path of an 
 * endpoint, appends the subpath of the request and creates the complete URI for the proxy invocation. 
 * Requests and their subpaths are identified using the annotation @{@link Request}.</p>
 * 
 * <p><b>Note</b> that this processor is a prerequisite for any other processors which extract information 
 * from the <i>complete</i> request URI or manipulate in some way.</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class UriProcessor extends AbstractRequestProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} along with the {@link HttpRequestBase} and forms 
	 * the complete request URI by appending the request subpath to the root path defined on the endpoint. 
	 * If no subpath is found the root path is set without any alterations. Requests and their subpaths are 
	 * identified using the annotation @{@link Request}.</p>
	 * 
	 * <p>Any processors which extract information from the <i>complete</i> request URI or those which seek 
	 * to manipulate the URI should use this processor as a prerequisite.</p>
	 * 
	 * <p>See {@link RequestProcessor#process(HttpRequestBase, InvocationContext)}.</p>
	 * 
	 * @param httpRequestBase
	 * 			the {@link HttpRequestBase} whose URI will be initialized to the complete URI formualted using 
	 * 			the endpoint's root path and the request's subpath
	 * <br><br>
	 * @param config
	 * 			an immutable instance of {@link InvocationContext} which has its Sendpoint and request 
	 * 			properties correctly populated  
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if an {@link HttpEntityEnclosingRequestBase} was discovered and yet the entity failed to be resolved 
	 * 			and inserted into the request body
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	protected void process(HttpRequestBase httpRequestBase, InvocationContext config) 
	throws RequestProcessorException {

		try {
			
			Endpoint endpoint = config.getEndpoint().getAnnotation(Endpoint.class);
			String value = endpoint.value();
			String host = (value == null || value.isEmpty())? endpoint.host() :value;
			
			String scheme = endpoint.scheme();
			String port = endpoint.port();
			String path = endpoint.path();
			
			Request request = config.getRequest().getAnnotation(Request.class);
			
			URIBuilder uriBuilder = new URIBuilder();
			uriBuilder.setScheme(scheme).setHost(host).setPath(path + request.path());
			
			if(!port.equals("")) {
				
				uriBuilder.setPort(Integer.parseInt(port));
			}
			
			httpRequestBase.setURI(uriBuilder.build());
		}
		catch(Exception e) {
			
			throw new RequestProcessorException(getClass(), config, e);
		}
	}
}
