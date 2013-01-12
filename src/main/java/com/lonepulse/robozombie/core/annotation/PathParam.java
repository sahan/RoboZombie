package com.lonepulse.robozombie.core.annotation;

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
 * <p>This annotation marks a RESTful path parameter. The {@link Request#path()} 
 * may be marked with parameter place-holders having the format <i>:<parameter_name>
 * </i>.</p> 
 * 
 * <p>The interface method parameters may then be annotated with {@link PathParam} and 
 * the {@link PathParam#name()} given the same value as those in the place-holders.</p>
 *  
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre>
 * <br><b>@Request(path = "/:username/feed.json")</b>
 *public abstract String getFeed(<b>@Pathparam(name = "username", value = "Zombie")</b> String username);
 * </pre>
 * </code>
 * </p>
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PathParam {

	
	/**
	 * <p>The name of the RESTful request's path parameter.</p>
	 * 
	 * @return the name of the request parameter
	 * <br><br>
	 * @since 1.1.1
	 */
	public String name();
	
	/**
	 * <p>The value of the RESTful path parameter.</p>
	 * 
	 * @return a default value for the parameter (as a serialized 
	 * 		   {@link String})
	 * <br><br>
	 * @since 1.1.1
	 */
	public String value();
}
