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
import com.lonepulse.robozombie.request.AbstractSerializer;
import com.lonepulse.robozombie.request.PlainSerializer;

/**
 * <p>Identifies the {@link Serializer} to be used for translating a model into a content-type 
 * suitable for network transmission.</p>
 * 
 * <b>Usage:</b>
 * <br>
 * <br>
 * <ol>
 * <li>
 * <p>At <b>type-level</b> on an endpoint <i>interface</i>; attaches this serializer for all requests.</p><br>
 * <code>
 * <pre>@Endpoint(scheme = "https", host = "api.twitter.com/1")<b>
 *&#064;Serializer(ContentType.JSON)</b><br>public interface TwitterEndpoint {<br>}</b>
 * </pre>
 * </code>
 * </li>
 * <li>
 * <p>At <b>method-level</b> on an endpoint <i>request</i>.</p><br>
 * <code>
 * <pre>@Request("/users/{id}")<br><b>@Serializer(ContentType.JSON)</b>
 *public abstract String getUser();</b></b></pre>
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
public @interface Serializer {


	/**
	 * <p>The prefabricated serializer to be used, which can be identified using {@link ContentType}.</p>
	 * 
	 * @return the {@link ContentType} which identifies the prefabricated serializer
	 * <br><br>
	 * @since 1.2.4
	 */
	public ContentType value() default ContentType.UNDEFINED;
	
	/**
	 * <p>The {@link Class} of the custom {@link AbstractSerializer} extension to be used.</p> 
	 * 
	 * <code>
     * <pre>@Request("/articles", method = RequestMethod.PUT)<br><b>@Serializer(type = ArticleSerializer.class)</b>
     *public abstract void submitArticle(Article article);</b></b></pre>
     * </code>
	 * 
	 * @return the {@link Class} of the custom {@link AbstractSerializer} to be used
	 * <br><br>
	 * @since 1.2.4
	 */
	public Class<? extends AbstractSerializer<?,?>> type() default PlainSerializer.class;
}
