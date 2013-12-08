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
import java.net.URLDecoder;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.annotation.PathParam;
import com.lonepulse.robozombie.inject.InvocationContext;
import com.lonepulse.robozombie.util.Metadata;

/**
 * <p>This is a concrete implementation of {@link AbstractRequestProcessor} which discovers <b>path 
 * parameters</b> in a request URI by searching for arguments annotated with @{@link PathParam}. The 
 * placeholders that identify these are then replaced by the runtime values of the path parameters. 
 * This may be used in scenarios where the same contextual URI must be manipulated several times over 
 * the same session - e.g for RESTful service endpoints (e.g. <code>example.com/users/update/{username}, 
 * example.com/users/delete/{username}</code> ...etc). For request URIs which bear a resemblance but 
 * are <i>contextually different</i> it is advised to isolated them in their own request definitions 
 * and treat them separately.</p>
 * 
 * <p><i>Prefers</i> that only the subpath of a request contains path parameters. Although the root 
 * path defined on the endpoint is processed just the same, <i>variant roots</i> should use unique 
 * endpoint definitions.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class PathParamProcessor extends AbstractRequestProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} along with an {@link HttpRequestBase} and recreates 
	 * the URI by replacing all path parameter placeholders with the runtime value of their associated 
	 * arguments that are annotated with @{@link PathParam}.</p> 
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which is used to discover any arguments which are annotated 
	 * 			with @{@link PathParam}
	 * <br><br>
	 * @param request
	 * 			the {@link HttpRequestBase} whose path parameters will be used to reconstruct the URI
	 * <br><br>
 	 * @return the same instance of {@link HttpRequestBase} which was given for processing path parameters
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if an unrecoverable error occurred when recreating the URI using path parameters
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	protected HttpRequestBase process(InvocationContext context, HttpRequestBase request) {

		try {
			
			List<Entry<PathParam, Object>> queryParams = Metadata.onParams(PathParam.class, context);
			
			String path = URLDecoder.decode(request.getURI().toString(), "UTF-8");
			
			for (Entry<PathParam, Object> entry : queryParams) {
				
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
			
			throw (e instanceof RequestProcessorException)? 
					(RequestProcessorException)e :new RequestProcessorException(context, getClass(), e);
		}
	}
}
