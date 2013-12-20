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
import com.lonepulse.robozombie.request.AbstractSerializer;
import com.lonepulse.robozombie.request.PlainSerializer;

/**
 * <p>Attaches a serialize for converting models to the format consumed by endpoints.</p>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <ol>
 * <li>
 * <p>At <b>type-level</b> on an endpoint <i>definition</i>; attaches this serializer for all requests.</p>
 * <code>
 * <pre><b>@Serialize(JSON)</b>
 *&#064;Endpoint("https://api.github.com")
 *public interface GitHubEndpoint {<br>&nbsp;&nbsp;...<br>}</b>
 * </pre>
 * </code>
 * </li>
 * <li>
 * <p>At <b>method-level</b> on an endpoint <i>request</i>.</p>
 * <code>
 * <pre><b>@Serialize(JSON)</b>
 *&#064;POST(path = "/gists")
 *void createGist(<b>@Entity</b> Gist gist);</pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * <br>
 * @version 1.1.2
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Serialize {


	/**
	 * <p>The {@link ContentType} which identifies a pre-fabricated serializer, which is responsible for 
	 * converting model to the accepted request content.</p>
	 * 
	 * @return the {@link ContentType} which identifies an out-of-the-box serializer
	 * <br><br>
	 * @since 1.3.0
	 */
	ContentType value() default ContentType.UNDEFINED;
	
	/**
	 * <p>The {@link Class} of a custom {@link AbstractSerializer} which should be attached.</p> 
	 * 
	 * <code>
	 * <pre><b>@Serialize(type = GistSerializer.class)</b>&nbsp;&nbsp;@POST(path = "/gists")&nbsp;&nbsp;<b>@Serialize(JSON)</b>
	 *void createGist(<b>@Entity</b> Gist gist);</pre>
	 * </code>
	 * 
	 * @return the {@link Class} of the custom {@link AbstractSerializer} to be used
	 * <br><br>
	 * @since 1.3.0
	 */
	Class<? extends AbstractSerializer<?,?>> type() default PlainSerializer.class;
}
