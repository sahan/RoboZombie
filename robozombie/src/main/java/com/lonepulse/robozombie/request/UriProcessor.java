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

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http42.client.utils.URIBuilder;

import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.proxy.InvocationContext;
import com.lonepulse.robozombie.util.Metadata;

/**
 * <p>This is a concrete implementation of {@link AbstractRequestProcessor} which extracts the root path 
 * of an endpoint, appends the subpath of the request and creates the complete URI for the proxy invocation. 
 * Requests and their subpaths are identified using the annotation @{@link Request}.</p>
 * 
 * <p><b>Note</b> that this processor is a prerequisite for any other processors which extract information 
 * from the <i>complete</i> request URI or manipulate in some way.</p> 
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class UriProcessor extends AbstractRequestProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} along with the {@link HttpRequestBase} and forms the 
	 * complete request URI by appending the request subpath to the root path defined on the endpoint. 
	 * If no subpath is found the root path is set without any alterations. Requests and their subpaths 
	 * are identified using the annotation @{@link Request} or any other meta-annotation which uses it.</p>
	 * 
	 * <p>Any processors which extract information from the <i>complete</i> request URI or those which 
	 * seek to manipulate the URI should use this processor as a prerequisite.</p>
	 * 
	 * <p>See {@link AbstractRequestProcessor#process(InvocationContext, HttpRequestBase)}.</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} used to discover root and subpath information  
	 * <br><br>
	 * @param request
	 * 			the {@link HttpRequestBase} whose URI will be initialized to the complete URI formulated 
	 * 			using the endpoint's root path and the request's subpath
	 * <br><br>
 	 * @return the same instance of {@link HttpRequestBase} which was given for processing the URI
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if a URI failed to be created using the information found on the endpoint definition
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	protected HttpRequestBase process(InvocationContext context, HttpRequestBase request) {

		try {
			
			Endpoint endpoint = context.getEndpoint().getAnnotation(Endpoint.class);
			
			String scheme = endpoint.scheme();
			String value = endpoint.value();
			String host = (value == null || value.isEmpty())? endpoint.host() :value;
			int port = endpoint.port();
			String path = endpoint.path();
			
			URIBuilder uriBuilder = new URIBuilder()
			.setScheme(scheme).setHost(host).setPath(path + Metadata.findPath(context.getRequest()));

			if(port >= 0) {
				
				uriBuilder.setPort(port);
			}
			
			request.setURI(uriBuilder.build());
			
			return request;
		}
		catch(Exception e) {
			
			throw new RequestProcessorException(context, getClass(), e);
		}
	}
}
