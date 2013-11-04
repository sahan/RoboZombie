package com.lonepulse.robozombie.annotation;

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

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;

/**
 * <p>This annotation is used to mark a method which initiates an HTTP request.</p>
 * 
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre>@Request(path = "/search")</b>
 *public abstract String search(@QueryParam(name = "q") String searchTerm);
 * </pre>
 * </code>
 * </p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Request {
	
	
	/**
	 * <p>Identifies the <i>method</i> of a request as specified in <a href="">
	 * Section 9</a> of the HTTP 1.1 RFC.</p>
	 * 
	 * <p>The default request method used by an @{@link Request} annotation is 
	 * {@link RequestMethod#GET}. To alter this, use {@link Request#method()}</p>
	 * 
	 * @version 1.1.0
	 * <br><br>
	 * @since 1.2.4
	 * <br><br>
	 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	public static enum RequestMethod {

		/**
		 * <p>Identifies an {@link HttpGet} request.</p>
		 * 
		 * @since 1.2.4
		 */
		GET,
		
		/**
		 * <p>Identifies an {@link HttpPost} request.</p>
		 * 
		 * @since 1.2.4
		 */
		POST,
		
		/**
		 * <p>Identifies an {@link HttpPut} request.</p>
		 * 
		 * @since 1.2.4
		 */
		PUT,
		
		/**
		 * <p>Identifies an {@link HttpDelete} request.</p>
		 * 
		 * @since 1.2.4
		 */
		DELETE,
		
		/**
		 * <p>Identifies an {@link HttpHead} request.</p>
		 * 
		 * @since 1.2.4
		 */
		HEAD,
		
		/**
		 * <p>Identifies an {@link HttpTrace} request.</p>
		 * 
		 * @since 1.2.4
		 */
		TRACE,
		
		/**
		 * <p>Identifies an {@link HttpOptions} request.</p>
		 * 
		 * @since 1.2.4
		 */
		OPTIONS;
	}
	
	/**
	 * <p>The type of the HTTP request which can be indicated using 
	 * {@link RequestMethod}.</p>
	 * <br>
	 * <p>The default method type is {@link RequestMethod#GET}.</p>
	 * 
	 * @return the type of the HTTP request
	 * <br><br>
	 * @since 1.1.0
	 */
	public RequestMethod method() default RequestMethod.GET;
	
	/**
	 * <p>A sub-path which which continues from the root hierarchy of the uri 
	 * If no sub path is given, resource is assumed to be found at the root 
	 * path given on the {@link Endpoint}.    
	 * 
	 * @return the sub-path on which the resource is located 
	 * <br><br>
	 * @since 1.1.0
	 */
	public String path() default "";
}
