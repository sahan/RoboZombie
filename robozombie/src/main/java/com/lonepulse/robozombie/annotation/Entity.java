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
 * <p>Identifies an entity/content to be included in the body of a request.</p>
 * <br>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre>@POST(path = "/gists")&nbsp;&nbsp;@Serializer(JSON)
 *void createGist(<b>@Entity</b> Gist gist);</pre>
 * </code>
 * </p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * 
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
	
	
	/**
	 * <p>Identifies a specific format for <i>serialized</i> content. These {@link ContentType}s mirror 
	 * the sub-category of <a href="http://en.wikipedia.org/wiki/Internet_media_type">MIME</a> types.</p>
	 * 
	 * @version 1.1.0
	 * <br><br> 
	 * @since 1.2.4
	 * <br><br> 
	 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	public static enum ContentType {
		
		
		/**
		 * <p>Identifies <b>raw textual content</b> <i>("text/plain")</i>, which is already assumed to be 
		 * in its serialized format.</p>
		 * 
		 * @since 1.2.4
		 */
		PLAIN,
		
		/**
		 * <p>Identifies <a href="www.json.org">JSON</a> content <i>("application/json")</i>.</p>
		 * 
		 * <p><b>Note</b> that activation of the <i>out-of-the-box</i> serializer and deserializer for JSON 
		 * requires the <a href="http://code.google.com/p/google-gson">GSON</a> library to be available on 
		 * the classpath. If GSON is not detected, both the serializer and deserializer will be disabled and 
		 * any attempt to use them will result in an {@link IllegalStateException}.</p>
		 * 
		 * @since 1.2.4
		 */
		JSON,
		
		/**
		 * <p>Identifies <a href="http://en.wikipedia.org/wiki/XML">XML</a> content <i>("application/xml")</i>.</p>
		 * 
		 * <p><b>Note</b> that activation of the <i>out-of-the-box</i> serializer and deserializer for XML 
		 * requires the <a href="http://simple.sourceforge.net">Simple-XML</a> library to be available on 
		 * the classpath. If Simple-XML is not detected, both the serializer and deserializer will be disabled 
		 * and any attempt to use them will result in an {@link IllegalStateException}.</p>
		 * 
		 * @since 1.2.4
		 */
		XML,
		
		/**
		 * <p>Indicates the inapplicability of a standardized content type.</p>  
		 * 
		 * <p>When used with {@code @Serializer} or {@code @Deserializer}, it indicates the use of a custom 
		 * serializer or deserializer.</p>
		 * 
		 * @since 1.2.4
		 */
		UNDEFINED;
	};
}
