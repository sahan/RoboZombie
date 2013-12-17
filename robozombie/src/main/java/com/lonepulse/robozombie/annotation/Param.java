package com.lonepulse.robozombie.annotation;

import java.lang.annotation.Documented;

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

/**
 * <p>Allows a basic name and value pair to be provided as annotated metadata.</p>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre>@POST(path = "/emails/send")
 *&#064;FormParams({<b>@Param(name = "from", value = "someone@example.com"),
 *             &#064;Param(name = "to", value = "support@example.com")</b>})
 *void contactSupport(<b>@FormParams</b> Map&lt;String, String&gt; subjectAndBody);</pre>
 * </code>
 * </p>
 * <br>
 * @version 1.1.0
 * <br><br> 
 * @since 1.3.0
 * <br><br> 
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Documented
public @interface Param { 
	
	
	/**
	 * <p>The <b>name</b> of the name and value pair.</p>  
	 * 
	 * @return the name of the name and value pair
	 * <br><br>
	 * @since 1.3.0
	 */
	String name();
	
	/**
	 * <p>The <b>value</b> of the name and value pair.</p>  
	 * 
	 * @return the value of the name and value pair
	 * <br><br>
	 * @since 1.3.0
	 */
	String value();
}