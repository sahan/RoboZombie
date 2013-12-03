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
 * <p>Identifies the {@link Deserializer} which is to be used to parse 
 * the output of the HTTP request execution.</p>
 * 
 * <b>Usage:</b>
 * <br>
 * <br>
 * <ol>
 * <li>
 * <p>At <b>type-level</b> on an endpoint <i>interface</i>; attaches this deserializer for all requests.</p><br>
 * <code>
 * <pre>@Endpoint(scheme = "https", host = "api.twitter.com/1")<b>
 *&#064;Deserializer(ContentType.JSON)</b><br>public interface TwitterEndpoint {<br>}</b>
 * </pre>
 * </code>
 * </li>
 * <li>
 * <p>At <b>method-level</b> on an endpoint <i>request</i>.</p><br>
 * <code>
 * <pre>@Request("/users/{id}")<br><b>@Deserializer(ContentType.JSON)</b>
 *public abstract String getUser();</b></b></pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * 
 * @version 1.1.2
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Deserializer {


	/**
	 * <p>The prefabricated instance of Deserializer<?> to be used, which can be identified using {@link ContentType}.</p>
	 * 
	 * @return the selected {@link ContentType} or the {@link Class} of a custom {@link Deserializer}
	 * <br><br>
	 * @since 1.2.4
	 */
	ContentType value() default ContentType.UNDEFINED;
	
	/**
	 * <p>The {@link Class} of the custom {@link AbstractDeserializer} extension to be used.</p> 
	 * 
	 * <code>
     * <pre>@Request("/license.txt")<br><b>@Deserializer(type = CustomDeserializer.class)</b>
     *public abstract License getLicense();</b></b></pre>
     * </code>
	 * 
	 * @return the {@link Class} of the custom {@link AbstractDeserializer} to be used
	 * <br><br>
	 * @since 1.1.1
	 */
	Class<? extends AbstractDeserializer<?>> type() default PlainDeserializer.class;
}
