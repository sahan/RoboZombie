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

import com.lonepulse.robozombie.annotation.Entity.ContentType;
import com.lonepulse.robozombie.response.AbstractDeserializer;
import com.lonepulse.robozombie.response.PlainDeserializer;

/**
 * <p>Attaches a {@link Deserializer} for converting response content of an identified {@link ContentType} 
 * into consumable models.</p>
 * <br>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <ol>
 * <li>
 * <p>At <b>type-level</b> on an endpoint <i>definition</i>; attaches this deserializer for all requests.</p><br>
 * <code>
 * <pre>@Endpoint(scheme = "https", host = "api.github.com")<b>
 *&#064;Deserializer(JSON)</b><br>public interface GitHubEndpoint {<br>&nbsp;...<br>}</b>
 * </pre>
 * </code>
 * </li>
 * <li>
 * <p>At <b>method-level</b> on an endpoint <i>request</i>.</p><br>
 * <code>
 * <pre><b>@Deserializer(JSON)</b>&nbsp;&nbsp;@GET("/users/{user}/gists")
 *List&lt;Gist&gt; getGists(@PathParam("user") String user);</b></b></pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * 
 * @version 1.1.2
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Deserializer {


	/**
	 * <p>The {@link ContentType} which identifies a pre-fabricated deserializer, which is responsible for 
	 * converting response content of the specified type to a model.</p>
	 * 
	 * @return the {@link ContentType} which identifies an out-of-the-box deserializer
	 * <br><br>
	 * @since 1.2.4
	 */
	ContentType value() default ContentType.UNDEFINED;
	
	/**
	 * <p>The {@link Class} of a custom {@link AbstractDeserializer} which should be attached.</p> 
	 * 
	 * <code>
	 * <pre><b>@Deserializer(type = RawGistDeserializer.class)</b>&nbsp;&nbsp;@GET("/users/{user}/gists")<br>
	 *RawGist[] getRawGists(@PathParam("user") String user);</b></b></pre>
	 * </code>
	 * 
	 * @return the {@link Class} of the custom {@link AbstractDeserializer} to be used
	 * <br><br>
	 * @since 1.1.1
	 */
	Class<? extends AbstractDeserializer<?>> type() default PlainDeserializer.class;
}
