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
 * <p>This annotation is used to tag a method which initiates a <i>RESTFul request</i>.
 * 
 * The {@link Rest#path()} may be marked with parameter place-holders having the
 * format <i>:<parameter_name></i> as shown below:</p>
 * 
 * <p><code>@Rest(path = "/:artist/:song")</code></p>
 * 
 * <p>The interface method parameters may then be annotated with {@link PathParam} and
 * the {@link PathParam#value()} given the same value as those in the place-holders:</p>
 * <p><code>
 * <pre>
 * String getLyrics(@PathParam("artist") String artist,@PathParam("song") String song);
 * </pre>
 * </code></p>
 * 
 * <p><b>Parameter values that never change could be coded into the path itself.</b></p>  
 * 
 * @version 1.2.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Rest {
	
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
	 * <p>An array of {@link Request} Params which indicate parameters that 
	 * never change for a particular RESTful request.
	 * 
	 * @return an array of {@link Request} Params for this particular request
	 * <br><br>
	 * @since 1.2.0
	 */
	public Request.Param[] params() default {};
}
