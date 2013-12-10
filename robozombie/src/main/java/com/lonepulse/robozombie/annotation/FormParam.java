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

/**
 * <p>Identifies a parameter to be sent as a <a href="http://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms">
 * form-urlencoded</a> list of <b>name-value</b> pairs.
 * <br>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code><pre>
 * @POST(path = "/emails/send")
 *void sendEmail(<b>@FormParam("from")</b> String from, <b>@FormParam("to")</b> String to, 
 *               <b>@FormParam("subject")</b> String subject, <b>@FormParam("body")</b> String body);</pre>
 * </code>
 * </p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormParam {
	
	
	/**
	 * <p>The name of the form parameter which identifies this value in a list of name-value pairs.</p>
	 * 
	 * @return the name of the form parameter
	 * <br><br>
	 * @since 1.1.0
	 */
	String value();
}
