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

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Detaches any inherited <b>@{@link Serialize}, @{@link Deserialize}, @{@link Async} and 
 * @{@link Intercept}</b> annotations.</p>
 * <br>
 * <br>
 * <b>Usage (assuming the endpoint is asynchronous and a type-level interceptor is attached):</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre>@GET("/meta")&nbsp;&nbsp;<b>@Detach({Async.class, Intercept.class})</b>
 *Meta getMetaInfo();
 * </pre>
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
public @interface Detach {
	
	
	/**
	 * <p>The {@link Class}es which identify the inherited annotations to be detached from the request.</p> 
	 * 
	 * @return the {@link Class}es for the annotations to be detached
	 * <br><br>
	 * @since 1.2.4
	 */
	Class<? extends Annotation>[] value();
}
