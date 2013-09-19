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

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;

import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.processor.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.util.Resolver;

/**
 * <p>This enum is used to identify the request types as specified in <a href="">Section 9</a> of the HTTP 
 * 1.1 RFC. These request methods are common for both common and RESTful requests.
 * 
 * @version 1.1.4
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum RequestMethod {

	/**
	 * <p>Identifies an {@link HttpGet} request.
	 * 
	 * @since 1.1.0
	 */
	GET,
	
	/**
	 * <p>Identifies an {@link HttpPost} request.
	 * 
	 * @since 1.1.0
	 */
	POST,
	
	/**
	 * <p>Identifies an {@link HttpPut} request.
	 * 
	 * @since 1.1.0
	 */
	PUT,
	
	/**
	 * <p>Identifies an {@link HttpDelete} request.
	 * 
	 * @since 1.1.0
	 */
	DELETE,
	
	/**
	 * <p>Identifies an {@link HttpHead} request.
	 * 
	 * @since 1.1.0
	 */
	HEAD,
	
	/**
	 * <p>Identifies an {@link HttpTrace} request.
	 * 
	 * @since 1.1.0
	 */
	TRACE,
	
	/**
	 * <p>Identifies an {@link HttpOptions} request.
	 * 
	 * @since 1.1.0
	 */
	OPTIONS;
	
	
	/**
	 * <p>Resolves the {@link RequestMethod} for the given {@link ProxyInvocationConfiguration}.</p>
	 * 
	 * <p>This implementation assumes that a {@link ProxyInvocationConfiguration} will never be 
	 * constructed for an endpoint request method without an @{@link Request} annotation.</p>
	 * 
	 * @since 1.2.4
	 */
	public static final Resolver<ProxyInvocationConfiguration, RequestMethod> RESOLVER 
	= new Resolver<ProxyInvocationConfiguration, RequestMethod>() {

		@Override
		public RequestMethod resolve(ProxyInvocationConfiguration config) {
			
			Method request = config.getRequest();
			return request.getAnnotation(Request.class).method();
		}
	};
	
	/**
	 * <p>Translates a given {@link ProxyInvocationConfiguration} to its {@link RequestMethod}.</p>
	 * 
	 * <p>This implementation is solely dependent upon the {@link RequestMethod} property in the 
	 * annotated metdata of the endpoint method definition.</p>
	 * 
	 * @since 1.2.4
	 */
	public static final RequestTranslator TRANSLATOR 
	= new RequestTranslator() {
		
		@Override
		public HttpRequestBase translate(ProxyInvocationConfiguration config) throws RequestTranslationException {
			
			RequestMethod requestMethod = RESOLVER.resolve(config);
			
			switch (requestMethod) {
			
				case GET: return new HttpGet();
				case POST: return new HttpPost();
				case PUT: return new HttpPut();
				case DELETE: return new HttpDelete();
				case HEAD: return new HttpHead();
				case TRACE: return new HttpTrace();
				case OPTIONS: return new HttpOptions();
				
				default: return new HttpGet();
			}
		}
	};
}
