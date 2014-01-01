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

import java.net.URI;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.PathParam;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.proxy.InvocationContext;
import com.lonepulse.robozombie.util.Metadata;

/**
 * <p>This is a concrete implementation of {@link AbstractRequestProcessor} which extracts the root 
 * path of an endpoint, appends the subpath of the request and creates the complete URI for the proxy 
 * invocation. Requests and their subpaths are identified by @{@link Request} or any other annotation 
 * which uses it as a meta-annotation.</p>
 * 
 * <p>In addition, it discovers <b>path parameters</b> in a request URI by searching for arguments 
 * annotated with @{@link PathParam}. The placeholders that identify these are then replaced by the 
 * runtime values of the path parameters. This may be used in scenarios where the same contextual 
 * URI must be manipulated several times over the same session - e.g for RESTful service endpoints 
 * (e.g. <code>example.com/users/update/{username}, example.com/users/delete/{username}</code> ...etc). 
 * For request URIs which bear a resemblance but are <i>contextually different</i> it is advised to 
 * isolated them in their own request definitions and treat them separately.</p>
 * 
 * <p><i>Prefers</i> that only the subpath of a request contains path parameters. Although the root 
 * path defined on the endpoint is processed just the same, <i>variant roots</i> should use unique 
 * endpoint definitions.</p>
 * 
 * <p><b>Note</b> that this processor is a prerequisite for all other processors which extract 
 * information from the <i>complete</i> request URI or manipulates it in additional ways.</p> 
 * 
 * @version 1.3.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
class UriProcessor extends AbstractRequestProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} along with the {@link HttpRequestBase} and forms 
	 * the complete request URI by appending the request subpath to the root path defined on the 
	 * endpoint. Any placeholders in the URI are replaced with their matching path parameters found 
	 * in the request arguments annotated with @{@link PathParam}.</p>
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
	 * 			using the endpoint's root path, the request's subpath and any path parameters
	 * <br><br>
 	 * @return the same instance of {@link HttpRequestBase} which was given for processing the URI
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if a URI failed to be created using the information found on the endpoint definition
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	protected HttpRequestBase process(InvocationContext context, HttpRequestBase request) {

		try {
			
			Endpoint endpoint = context.getEndpoint().getAnnotation(Endpoint.class);
			String path = endpoint.value() + Metadata.findPath(context.getRequest());
			
			List<Entry<PathParam, Object>> pathParams = Metadata.onParams(PathParam.class, context);
			
			for (Entry<PathParam, Object> entry : pathParams) {
				
				String name = entry.getKey().value();
				Object value = entry.getValue();
				
				if(!(value instanceof CharSequence)) {
				
					StringBuilder errorContext = new StringBuilder()
					.append("Path parameters can only be of type ")
					.append(CharSequence.class.getName())
					.append(". Please consider implementing CharSequence ")
					.append("and providing a meaningful toString() representation for the ")
					.append("<name> of the path parameter. ");
					
					throw new RequestProcessorException(new IllegalArgumentException(errorContext.toString()));
				}
				
				path = path.replaceAll(Pattern.quote("{" + name + "}"), ((CharSequence)value).toString());
			}
			
			request.setURI(URI.create(path));
			
			return request;
		}
		catch(Exception e) {
			
			throw new RequestProcessorException(context, getClass(), e);
		}
	}
}
