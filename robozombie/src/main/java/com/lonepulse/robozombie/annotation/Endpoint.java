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

/**
 * <p>Identifies an interface to be an endpoint definition and accepts key information such as the 
 * endpoint's host or the scheme and port to be used for communication. A bare minimum declaration 
 * would be <code>@Endpoint("example.com")</code> which is {@code http} on port {@code 80}.</p>
 * </p>
 * <br>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre><b>@Endpoint(scheme = "https", host = "api.github.com")</b>
 *public interface GitHubEndpoint {<br>&nbsp;...<br>}</pre>
 * </code>
 * </p>
 * 
 * @version 1.1.2
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Endpoint {
	

	/**
	 * <p>The scheme of the protocol used for communication. The default value for the scheme 
	 * is <code>http</code>.</p>
	 * 
	 * @return the protocol scheme
	 * <br><br>
	 * @since 1.1.2
	 */
	String scheme() default "http";
	
	/**
	 * <p>The hostname of the endpoint.</p>
	 * 
	 * <p>Synonymous to {@link #host()}.</p>
	 * 
	 * @return the endpoint's hostname
	 * <br><br>
	 * @since 1.1.2
	 */
	String value() default "";
	
	/**
	 * <p>The hostname of the endpoint.</p> 
	 * 
	 * <p>Synonymous to {@link #value()} which could be used as <code>@Endpoint("example.com")</code>.</p>
	 * 
	 * @return the endpoint's hostname
	 * <br><br>
	 * @since 1.1.2
	 */
	String host() default "";
	
	/**
	 * <p>The <b>port</b> through which a channel is opened for communication with the endpoint.</p> 
	 * 
	 * <p>The default ports are assumed to be <code>80</code> for <code>HTTP</code> and port 
	 * <code>443</code> for <code>HTTPS</code>.</p>  
	 * 
	 * @return the port used for communication
	 * <br><br>
	 * @since 1.1.2
	 */
	int port() default -1;

	/**
	 * <p>The root path to be immediately appended to the hostname.</p>    
	 * 
	 * @return the root path which extends immediately after the hostname
	 * <br><br>
	 * @since 1.1.2
	 */
	String path() default "";
}
