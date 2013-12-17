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
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Detaches any inherited &#064;{@link Serialize}, &#064;{@link Deserialize}, &#064;{@link Async} and 
 * &#064;{@link Intercept} annotations.</p>
 * <br>
 * <b>Usage</b> (assuming the endpoint is asynchronous and a type-level interceptor is attached):
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre>@GET("/meta")
 *<b>@Detach({Async.class, Intercept.class})</b>
 *Meta getMetaInfo();
 * </pre>
 * </code>
 * </p>
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Detach {
	
	
	/**
	 * <p>The {@link Class}es which identify the inherited annotations to be detached from the request.</p> 
	 * 
	 * @return the {@link Class}es for the annotations to be detached
	 * <br><br>
	 * @since 1.3.0
	 */
	Class<? extends Annotation>[] value();
}
