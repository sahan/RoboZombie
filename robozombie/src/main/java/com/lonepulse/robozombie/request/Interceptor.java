package com.lonepulse.robozombie.request;

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

import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This contract defines the policy for <b>intercepting</b> a request and processing it just before 
 * it's submitted for execution.</p>
 * 
 * <b>Usage:</b>
 * <br>
 * <br>
 * <ol>
 * <li>
 * <p>At <b>type-level</b> on an endpoint <i>interface</i>; attaches this interceptor for all requests.</p><br>
 * <code>
 * <pre>@Endpoint(scheme = "https", host = "api.github.com")<b>
 *&#064;Intercept(RequestInterceptor.class)</b><br>public interface GitHubEndpoint {<br>}</b>
 * </pre>
 * </code>
 * </li>
 * <li>
 * <p>At <b>method-level</b> on an endpoint <i>request</i>.</p><br>
 * <code>
 * <pre>@Request("/users/{id}")<br><b>@Intercept(RequestInterceptor.class)</b>
 *public abstract GitHubUser getUser(@PathParam("id") String id);</b></b></pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * <p>As a <b>request parameter</b>.</p><br>
 * <code>
 * <pre>@Request("/users/{id}")<br>
 *public abstract GitHubUser getUser(@PathParam("id") String id, Interceptor requestInterceptor);</b></b></pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @category API
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface Interceptor {
	
	/**
	 * <p>Accepts the {@link HttpRequestBase} which was <b>intercepted</b> just before execution and 
	 * supplies the {@link InvocationContext} to discover additional information about the invocation.</p>
	 *
	 * @param context
	 *			the {@link InvocationContext} which provides information about the proxy invocation
	 * <br><br>
	 * @param request
	 * 			the {@link HttpRequestBase} which was intercepted just before execution
	 * <br><br>
	 * @since 1.2.4
	 */
	void intercept(InvocationContext context, HttpRequestBase request);
}
