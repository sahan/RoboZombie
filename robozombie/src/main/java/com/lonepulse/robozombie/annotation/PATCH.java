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

import com.lonepulse.robozombie.annotation.Request.RequestMethod;

/**
 * <p>This annotation identifies an <b>HTTP PATCH</b> request.</p>
 * 
 * <p>See <a href="http://tools.ietf.org/html/rfc5789">HTTP PATCH</a>.</p> 
 * <br>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre><b>@PATCH(path = "/gists/{id}")</b>&nbsp;&nbsp;@Serializer(JSON)
 *void editGist(@PathParam("id") String id, &#064;Entity Gist gist);</pre>
 * </code>
 * </p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Request(method = RequestMethod.PATCH)
public @interface PATCH {
	
	/**
	 * <p>The sub-path (if any) which should be appended to the root path defined on the endpoint.</p> 
	 * 
	 * @return the path which extends from the root path defined on the endpoint
	 * <br><br>
	 * @since 1.2.4
	 */
	String value() default "";
}
