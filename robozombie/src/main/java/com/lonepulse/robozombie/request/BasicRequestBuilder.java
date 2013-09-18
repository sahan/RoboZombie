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

import java.lang.reflect.Method;
import java.net.URI;

import org.apache.http.client.methods.HttpRequestBase;

import android.net.Uri;

import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.processor.ProxyInvocationConfiguration;

/**
 * <p>This is an implementation of {@link AbstractRequestBuilder} which handles the 
 * request creation for the basic HTTP method types GET, POST, PUT, DELETE.</p>
 * 
 * @version 1.1.3
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class BasicRequestBuilder extends AbstractRequestBuilder {
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected URI buildPath(ProxyInvocationConfiguration config) throws Exception {
		
		Method request = config.getRequest();
		Request webRequest = request.getAnnotation(Request.class); //annotation may not exist
		
		if(webRequest == null)
			throw new MissingRequestAnnotationException(request, Request.class);
		
		String subpath = (webRequest == null)? "" :webRequest.path();
		URI uri =  config.getUri();
		
		Uri.Builder uriBuilder = Uri.parse(uri.toASCIIString()).buildUpon()
		.appendPath((subpath.startsWith("/"))? subpath.replaceFirst("/", "") :subpath);
		
		return URI.create(uriBuilder.build().toString());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected HttpRequestBase buildRequestWithParameters(URI uri, ProxyInvocationConfiguration config) throws Exception {
		
		return RequestUtils.build(uri, config);
	}
}
