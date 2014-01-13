package com.lonepulse.robozombie.annotation;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lonepulse.robozombie.annotation.Request.RequestMethod;

/**
 * <p>This annotation identifies an <b>HTTP POST</b> request.</p>
 * 
 * <p>See <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html">section 9</a> 
 * of the HTTP/1.1 specification.</p> 
 * <br>
 * <p>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre>@Serialize(JSON)
 *<b>@POST("/gists")</b>
 *void createGist(@Entity Gist gist);</pre>
 * </code>
 * </p>
 * <br>
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Request(method = RequestMethod.POST)
public @interface POST {
	
	/**
	 * <p>The sub-path (if any) which should be appended to the root path defined on the endpoint.</p> 
	 * 
	 * @return the path which extends from the root path defined on the endpoint
	 * <br><br>
	 * @since 1.3.0
	 */
	String value() default "";
}
