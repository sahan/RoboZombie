package com.lonepulse.robozombie.annotation;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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
import java.util.Map;

/**
 * <p>Identifies multiple static or dynamic <b>form parameters</b> which are to be included as 
 * <a href="http://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms">form-urlencoded</a> 
 * <b>name-value</b> pairs.
 * 
 * <p>This annotation can be used on the request definition to identify static form parameters as 
 * well as on a request parameter of type {@link Map}&lt;String, String&gt; to identify a set of 
 * dynamic form parameters.</p> 
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre>@POST("/emails/send")<b>
 *&#064;FormParams</b>(&#123;@Param(name = "from", value = "someone@example.com"),
 *             &#064;Param(name = "to", value = "support@example.com")&#125;)
 *void contactSupport(<b>@FormParams</b> Map&lt;String, String&gt; subjectAndBody);</pre>
 * </code>
 * </p>
 * <br>
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface FormParams {

	/**
	 * <p>An array of {@link Param}s which identifies a set of form parameters which are constant for 
	 * every invocation of this request.</p> 
	 * 
	 * @return an array of static form {@link Param}s for every invocation of this request
	 * <br><br>
	 * @since 1.3.0
	 */
	Param[] value() default {};
}
