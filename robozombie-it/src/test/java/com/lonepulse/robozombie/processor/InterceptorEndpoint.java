package com.lonepulse.robozombie.processor;

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


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.annotation.Detach;
import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.GET;
import com.lonepulse.robozombie.annotation.Interceptor;
import com.lonepulse.robozombie.inject.InvocationContext;
import com.lonepulse.robozombie.processor.InterceptorEndpoint.EndpointInterceptor;

/**
 * <p>An interface which represents a dummy endpoint with request method 
 * definitions that use various pre-fabricated and custom serializers.</p>
 * 
 * @category test
 * <br><br> 
 * @version 1.1.0
 * <br><br> 
 * @since 1.2.4
 * <br><br> 
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Interceptor(EndpointInterceptor.class)
@Endpoint(host = "0.0.0.0", port = "8080")
public interface InterceptorEndpoint {
	
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface XHeader {
		
		String value();
	}

	class EndpointInterceptor implements com.lonepulse.robozombie.request.Interceptor {

		public void intercept(HttpRequestBase request, InvocationContext context) {
			
			XHeader xHeader = context.getRequest().getAnnotation(XHeader.class);
			request.addHeader("X-Header", xHeader != null? xHeader.value() :"endpoint");
		}
	}
	
	class RequestInterceptor implements com.lonepulse.robozombie.request.Interceptor {
		
		public void intercept(HttpRequestBase request, InvocationContext context) {
			
			request.addHeader("X-Header", "request");
		}
	}
	
	
	/**
	 * <p>A mock request which uses an interceptor defined at the endpoint-level.</p>
	 *
	 * @since 1.2.4
	 */
	@GET("/endpoint")
	void endpointInterceptor();
	
	/**
	 * <p>A mock request which uses custom metadata on the request definition.</p>
	 *
	 * @since 1.2.4
	 */
	@GET("/metadata")
	@XHeader("X-Value") 
	void metadataInterceptor();
	
	/**
	 * <p>A mock request which uses interceptors defined at both endpoint and request levels.</p>
	 *
	 * @since 1.2.4
	 */
	@GET("/request")
	@Interceptor(RequestInterceptor.class)
	void requestInterceptor();
	
	/**
	 * <p>A mock request which uses interceptors defined at endpoint, request and param levels.</p>
	 *
	 * @param interceptor
	 * 			the interceptor instance which is passed along with the parameters for execution
	 *
	 * @since 1.2.4
	 */
	@GET("/param")
	@Interceptor(RequestInterceptor.class)
	void paramInterceptor(com.lonepulse.robozombie.request.Interceptor interceptor);
	
	/**
	 * <p>A mock request which detaches the interceptor defined at the endpoint level.</p>
	 *
	 * @since 1.2.4
	 */
	@GET("/detach") 
	@Detach(Interceptor.class)
	void detachInterceptor();
}
