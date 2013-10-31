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

import com.lonepulse.robozombie.response.AbstractResponseParser;
import com.lonepulse.robozombie.response.RawResponseParser;
import com.lonepulse.robozombie.response.ResponseParser;

/**
 * <p>Identifies the {@link ResponseParser} which is to be used to parse 
 * the output of the HTTP request execution.</p>
 * 
 * <b>Usage:</b>
 * <br>
 * <br>
 * <ol>
 * <li>
 * <p>At <b>type-level</b> on an endpoint <i>interface</i>; attaches this parser for all requests.</p><br>
 * <code>
 * <pre>@Endpoint(scheme = "https", host = "api.twitter.com/1")<b>
 *&#064;Parser(ParserType.JSON)</b><br>public interface TwitterEndpoint {<br>}</b>
 * </pre>
 * </code>
 * </li>
 * <li>
 * <p>At <b>method-level</b> on an endpoint <i>request</i>.</p><br>
 * <code>
 * <pre>@Request("/users/{id}")<br><b>@Parser(ParserType.JSON)</b>
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
public @interface Parser {


	/**
	 * <p>Indicates the type of the parser to be used for parsing the response content.</p>
	 * 
	 * @version 1.2.0
	 * <br><br> 
	 * @since 1.1.0
	 * <br><br> 
	 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	public static enum ParserType {
		
		
		/**
		 * <p>This response parser makes the response content available as <b>raw text</b>.</p>
		 * 
		 * @since 1.1.0
		 */
		RAW,
		
		/**
		 * <p>This response parser deserializes JSON content into the model specified on the request 
		 * definition.</p>
		 * 
		 * <p><b>Note</b> that this parser requires the <a href="http://code.google.com/p/google-gson">GSON</a> 
		 * library to be available on the classpath to be active. If GSON is not detected, this parser will 
		 * be disabled and any attempt to use it will result in an {@link IllegalStateException}.</p>
		 * 
		 * @since 1.1.0
		 */
		JSON,
		
		/**
		 * <p>This response parser deserializes XML content into the model specified on the request 
		 * definition.</p>
		 * 
		 * <p><b>Note</b> that this parser requires the <a href="http://simple.sourceforge.net">Simple-XML</a> 
		 * library to be available on the classpath to be active. If Simple-XML is not detected, this parser will 
		 * be disabled and any attempt to use it will result in an {@link IllegalStateException}.</p>
		 * 
		 * @since 1.1.0
		 */
		XML,
		
		/**
		 * <p>The default value which indicates that a custom {@link AbstractResponseParser} extendsion is being 
		 * used, whose {@link Class} is specified via {@link Parser#type()}.</p>
		 * 
		 * @since 1.1.0
		 */
		UNDEFINED;
	};
	
	
	/**
	 * <p>The prefabricated instance of ResponseParser<?> to be used, which can be identified using {@link ParserType}.</p>
	 * 
	 * @return the selected {@link ParserType} or the {@link Class} of a custom {@link ResponseParser}
	 * <br><br>
	 * @since 1.2.4
	 */
	public ParserType value() default ParserType.UNDEFINED;
	
	/**
	 * <p>The {@link Class} of the custom {@link AbstractResponseParser} extension to be used.</p> 
	 * 
	 * <code>
     * <pre>@Request("/license.txt")<br><b>@Parser(type = CustomParser.class)</b>
     *public abstract License getLicense();</b></b></pre>
     * </code>
	 * 
	 * @return the {@link Class} of the custom {@link AbstractResponseParser} to be used
	 * <br><br>
	 * @since 1.1.1
	 */
	public Class<? extends ResponseParser<?>> type() default RawResponseParser.class;
}
