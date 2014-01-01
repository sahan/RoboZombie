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
import com.lonepulse.robozombie.annotation.Intercept;
import com.lonepulse.robozombie.annotation.Skip;
import com.lonepulse.robozombie.processor.InterceptorEndpoint.CommonInterceptor;
import com.lonepulse.robozombie.processor.InterceptorEndpoint.CustomInterceptor;
import com.lonepulse.robozombie.proxy.InvocationContext;
import com.lonepulse.robozombie.request.Interceptor;

/**
 * <p>An endpoint which uses {@link Interceptor}s defined at multiple levels.</p>
 * 
 * @version 1.1.0
 * <br><br> 
 * @since 1.3.0
 * <br><br> 
 * @category test
 * <br><br> 
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
@Endpoint("http://0.0.0.0:8080")
@Intercept({CustomInterceptor.class, CommonInterceptor.class})
public interface InterceptorEndpoint {
	
	
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface XHeader {
		
		String value();
	}

	class CustomInterceptor implements Interceptor {

		@Override
		public void intercept(InvocationContext context, HttpRequestBase request) {
			
			XHeader xHeader = context.getRequest().getAnnotation(XHeader.class);
			request.addHeader("X-Header", xHeader != null? xHeader.value() :"endpoint");
		}
	}
	
	class CommonInterceptor implements Interceptor {
		
		@Override
		public void intercept(InvocationContext context, HttpRequestBase request) {
			
			request.addHeader("Accept-Charset", "utf-8");
		}
	}
	
	class RequestInterceptor implements Interceptor {
		
		@Override
		public void intercept(InvocationContext context, HttpRequestBase request) {
			
			request.addHeader("X-Header", "request");
		}
	}
	
	
	/**
	 * <p>A mock request which uses an interceptor defined at the endpoint-level.</p>
	 *
	 * @since 1.3.0
	 */
	@GET("/endpoint")
	void endpointInterceptor();
	
	/**
	 * <p>A mock request which uses custom metadata on the request definition.</p>
	 *
	 * @since 1.3.0
	 */
	@GET("/metadata")
	@XHeader("X-Value") 
	void metadataInterceptor();
	
	/**
	 * <p>A mock request which uses interceptors defined at both endpoint and request levels.</p>
	 *
	 * @since 1.3.0
	 */
	@GET("/request")
	@Intercept(RequestInterceptor.class)
	void requestInterceptor();
	
	/**
	 * <p>A mock request which uses interceptors defined at endpoint, request and param levels.</p>
	 *
	 * @param interceptor
	 * 			the interceptor instance which is passed along with the parameters for execution
	 *
	 * @since 1.3.0
	 */
	@GET("/param")
	@Intercept(RequestInterceptor.class)
	void paramInterceptor(Interceptor interceptor);
	
	/**
	 * <p>A mock request which detaches the interceptors defined at the endpoint level.</p>
	 *
	 * @since 1.3.0
	 */
	@GET("/detach") 
	@Detach(Intercept.class)
	void detachInterceptor();
	
	/**
	 * <p>A mock request which skips a single interceptor defined at the endpoint level.</p>
	 *
	 * @since 1.3.0
	 */
	@GET("/skip") 
	@Skip(CustomInterceptor.class)
	void skipInterceptor();
}
