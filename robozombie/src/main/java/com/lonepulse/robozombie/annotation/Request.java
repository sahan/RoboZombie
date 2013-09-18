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

import com.lonepulse.robozombie.request.RequestMethod;

/**
 * <p>This annotation is used to mark a method which initiates an HTTP request.</p>
 * 
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre>@Request(path = "/search")</b>
 *public abstract String search(@Param(name = "q") String searchTerm);
 * </pre>
 * </code>
 * </p>
 * 
 * @version 1.1.3
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Request {
	
	
	/**
	 * <p>Specifies a query parameter name and value pair which is common 
	 * to all submissions of a particular request. 
	 * 
	 * @version 1.1.1
	 * <br><br> 
	 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	public @interface Param { 
		
		/**
		 * <p>The name of the request parameter.
		 * 
		 * @return the name of the request parameter
		 * <br><br>
		 * @since 1.1.1
		 */
		public String name();
		
		/**
		 * <p>The serialized {@link String} value of the request parameter.</p>
		 * 
		 * @return a serialized {@link String} value for the parameter
		 * <br><br>
		 * @since 1.1.1
		 */
		public String value();
	}
	
	/**
	 * <p>The type of the HTTP request which can be indicated using 
	 * {@link RequestMethod}.</p>
	 * <br>
	 * <p>The default method type is {@link RequestMethod#HTTP_GET}.
	 * 
	 * @return the type of the HTTP request
	 * <br><br>
	 * @since 1.1.1
	 */
	public RequestMethod method() default RequestMethod.HTTP_GET;
	
	/**
	 * <p>A sub-path which which continues from the root hierarchy of the uri 
	 * If no sub path is given, resource is assumed to be found at the root 
	 * path given on the {@link Endpoint}.    
	 * 
	 * @return the sub-path on which the resource is located 
	 * <br><br>
	 * @since 1.1.2
	 */
	public String path() default "";
	
	/**
	 * <p>An array of {@link Request.Param}s which indicate query parameters that 
	 * never change for a particular request.
	 * 
	 * @return an array of {@link Request.Param}s for this particular request
	 * <br><br>
	 * @since 1.1.3
	 */
	public Request.Param[] params() default {};
}
