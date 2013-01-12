package com.lonepulse.robozombie.rest.request;

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

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.core.annotation.Param;
import com.lonepulse.robozombie.core.processor.AnnotationExtractor;
import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.core.request.AbstractRequestBuilder;
import com.lonepulse.robozombie.core.request.RequestMethod;
import com.lonepulse.robozombie.rest.annotation.Rest;

/**
 * <p>This is an implementation of {@link AbstractRequestBuilder} which handles the 
 * request creation for RESTful requests.
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class RestfulRequestBuilder extends AbstractRequestBuilder {

	
	@Override
	protected URI buildPath(ProxyInvocationConfiguration config) throws Exception {

		return config.getUri(); //root path return as it is
	}
	
	@Override
	protected HttpRequestBase buildRequestWithParameters(URI uri, ProxyInvocationConfiguration config) throws Exception {
		
		Method request = config.getRequest();
		
		Rest restfulRequest = request.getAnnotation(Rest.class);
		String subpath = restfulRequest.path();
		
		Map<Object, Param> annotatedParams = AnnotationExtractor.<Param>extractWithParameterValues(Param.class, request, config.getRequestArgs());
		Set<Object> methodParams = annotatedParams.keySet();
		
		for (Object paramValue : methodParams) {
			
			if(!(paramValue instanceof String))
				throw new IllegalArgumentException("Parameters for RESTful requests can only be of type " + String.class.getName()); 

			subpath = subpath.replaceAll(":" + annotatedParams.get(paramValue).value(), (String)paramValue); //replace param place-holders with param value
		}
		
		URI uriWithParameters = new URI(uri.toASCIIString() + subpath);
		
		RequestMethod httpMethod = (restfulRequest == null)? RequestMethod.HTTP_GET :restfulRequest.method();
		
		switch (httpMethod) {
		
			case HTTP_GET: { 
				
				return new HttpGet(uriWithParameters);
			}
			case HTTP_POST: { 
				
				return new HttpPost(uriWithParameters);
			}
			case HTTP_PUT: { 
				
				return new HttpPut(uriWithParameters);
			}
			case HTTP_DELETE: { 
				
				return new HttpDelete(uriWithParameters);
			}
			
			default:
				return null;
		}
	}

	@Override
	protected HttpRequestBase buildHeader(HttpRequestBase httpRequestBase, ProxyInvocationConfiguration config) throws Exception {

		//TODO so RESTful requests, the header is built in the same "basic" way? 
		
		return null;
	}

}
