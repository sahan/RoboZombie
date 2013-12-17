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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * <p>Identifies multiple static or dynamic <b>query parameters</b> which are to be included in 
 * a <a href="http://en.wikipedia.org/wiki/Query_string">query string</a>.</p>
 * 
 * <p>This annotation can be used on the request definition to identify static query parameters 
 * as well as on a request parameter of type {@link Map}&lt;String, String&gt; to identify a set 
 * of dynamic query parameters.</p> 
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code><pre>@GET("/users/{user}/repos")
 *<b>@QueryParams</b>(&#123;@Param(name = "per_page", value = "5")&#125;)
 *List&lt;Repo&gt; getReposPaginated(<b>@QueryParams</b> Map&lt;String, String&gt; pageParams, ...);
 * </code></pre>
 * </p>
 * <br>
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface QueryParams {

	/**
	 * <p>An array of {@link Param}s which identifies a set of query parameters which are constant 
	 * for every invocation of this request.</p> 
	 * 
	 * @return an array of static query {@link Param}s for every invocation of this request
	 * <br><br>
	 * @since 1.3.0
	 */
	Param[] value() default {};
}
