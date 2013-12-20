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

import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>This contract defines the policy for <b>intercepting</b> a request and processing it just before 
 * it's submitted for execution.</p>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <ol>
 * <li>
 * <p>At <b>type-level</b> on an endpoint; attaches the interceptor(s) for all requests.</p>
 * <code>
 * <pre>@Endpoint("https://api.github.com")<b>
 *&#064;Intercept({CommonInterceptor1.class, CommonInterceptor2.class})</b><br>public interface GitHubEndpoint {<br>&nbsp;...<br>}</b>
 * </pre>
 * </code>
 * </li>
 * <li>
 * <p>At <b>method-level</b> on a request.</p>
 * <code>
 * <pre>@Deserialize(JSON)
 *&#064;GET("/users/{user}/gists")
 *<b>@Intercept(SpecificInterceptor.class)</b>
 *List&lt;Gist&gt; getGists(@PathParam("user") String user);</pre>
 * </code>
 * </li>
 * <li>
 * <p>As a <b>request parameter</b>.</p>
 * <code>
 * <pre>@Deserialize(JSON)</b>&nbsp;&nbsp;@GET("/users/{user}/gists")
 *List&lt;Gist&gt; getGists(@PathParam("user") String user, <b>Interceptor</b> interceptor);</b></b></pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * <br>
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
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
	 * @since 1.3.0
	 */
	void intercept(InvocationContext context, HttpRequestBase request);
}
