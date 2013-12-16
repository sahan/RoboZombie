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
 * <p>Identifies a constant set of headers which should be sent with every request.</p>
 * <br>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre>@GET("/feeds")
 *<b>@Headers({@Headers.Header(name = "</b>Accept<b>", value = "</b>application/json<b>"),
 *          &#064;Headers.Header(name = "</b>Accept-Charset<b>", value = "</b>utf-8<b>")})</b>
 *Feeds getFeeds();
 * </pre>
 * </code>
 * </p>
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Headers {

	
	/**
	 * <p>Marks a <b>constant</b> header parameter for a particular request.</p> 
	 * 
	 * @version 1.1.1
	 * <br><br>
	 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	public static @interface Header {
		
		/**
		 * <p>The name of the header parameter.</p>
		 * 
		 * @return the name of the header parameter
		 * <br><br>
		 * @since 1.1.1
		 */
		String name();
		
		/**
		 * <p>The value of the header parameter.</p>
		 * 
		 * @return the value for the header parameter
		 * <br><br>
		 * @since 1.1.1
		 */
		String value();
	}

	
	/**
	 * <p>The parameters to be included in this header.</p>
	 * 
	 * @return the array of {@link Headers.Header}s
	 * <br><br>
	 * @since 1.1.1
	 */
	Headers.Header[] value();
}
