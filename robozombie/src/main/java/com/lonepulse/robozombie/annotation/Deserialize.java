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

import com.lonepulse.robozombie.annotation.Entity.ContentType;
import com.lonepulse.robozombie.response.AbstractDeserializer;
import com.lonepulse.robozombie.response.PlainDeserializer;

/**
 * <p>Attaches a deserializer for converting response content of an identified {@link ContentType} 
 * into consumable models.</p>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <ol>
 * <li>
 * <p>At <b>type-level</b> on an endpoint; attaches this deserializer for all requests.</p>
 * <code>
 * <pre><b>@Deserialize(JSON)</b>
 *&#064;Endpoint("https://api.github.com")
 *public interface GitHubEndpoint {<br>&nbsp;&nbsp;...<br>}</b>
 * </pre>
 * </code>
 * </li>
 * <li>
 * <p>At <b>method-level</b> on a request.</p>
 * <code>
 * <pre><b>@Deserialize(JSON)</b>&nbsp;&nbsp;@GET("/users/{user}/gists")
 *List&lt;Gist&gt; getGists(@PathParam("user") String user);</pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * <br>
 * @version 1.1.2
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Deserialize {


	/**
	 * <p>The {@link ContentType} which identifies a pre-fabricated deserializer, which is responsible for 
	 * converting response content of the specified type to a model.</p>
	 * 
	 * @return the {@link ContentType} which identifies an out-of-the-box deserializer
	 * <br><br>
	 * @since 1.3.0
	 */
	ContentType value() default ContentType.UNDEFINED;
	
	/**
	 * <p>The {@link Class} of a custom {@link AbstractDeserializer} which should be attached.</p> 
	 * 
	 * <code>
	 * <pre>@GET("/users/{user}/gists")
	 *<b>@Deserialize(type = RawGistDeserializer.class)</b>
	 *List&lt;RawGist&gt; getRawGists(@PathParam("user") String user);</pre>
	 * </code>
	 * 
	 * @return the {@link Class} of the custom {@link AbstractDeserializer} to be used
	 * <br><br>
	 * @since 1.1.1
	 */
	Class<? extends AbstractDeserializer<?>> type() default PlainDeserializer.class;
}
