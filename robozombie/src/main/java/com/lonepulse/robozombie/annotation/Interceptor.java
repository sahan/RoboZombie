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
 * <p>Identifies an {@link com.lonepulse.robozombie.request.Interceptor} which will be used to process a 
 * request just before it's executed.</p>
 * 
 * <b>Usage:</b>
 * <br>
 * <br>
 * <ol>
 * <li>
 * <p>At <b>type-level</b> on an endpoint <i>interface</i>; attaches this interceptor for all requests.</p><br>
 * <code>
 * <pre>@Endpoint(scheme = "https", host = "api.twitter.com/1")<b>
 *&#064;Interceptor(RequestInterceptor.class)</b><br>public interface GitHubEndpoint {<br>}</b>
 * </pre>
 * </code>
 * </li>
 * <li>
 * <p>At <b>method-level</b> on an endpoint <i>request</i>.</p><br>
 * <code>
 * <pre>@Request("/users/{id}")<br><b>@Interceptor(RequestInterceptor.class)</b>
 *public abstract GitHubUser getUser(@PathParam("id") String id);</b></b></pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * 
 * @version 1.1.2
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Interceptor {
	
	
	/**
	 * <p>The {@link Class}es which identify the custom {@link com.lonepulse.robozombie.request.Interceptor}s 
	 * which will be use on the request before they are executed.</p> 
	 * 
	 * @return the {@link Class}es which identify the {@link com.lonepulse.robozombie.request.Interceptor}s 
	 * 		   to be used
	 * <br><br>
	 * @since 1.2.4
	 */
	public Class<? extends com.lonepulse.robozombie.request.Interceptor>[] value();
}
