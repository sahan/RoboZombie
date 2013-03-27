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

import com.lonepulse.robozombie.core.response.parser.ResponseParser;
import com.lonepulse.robozombie.core.response.parser.StringResponseParser;

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
 *&#064;Parser(PARSER_TYPE.STRING)</b><br>public interface TwitterEndpoint {<br>}</b>
 * </pre>
 * </code>
 * </li>
 * <li>
 * <p>At <b>method-level</b> on an endpoint <i>request</i>.</p><br>
 * <code>
 * <pre>@Request("/license.txt")<br><b>@Parser(PARSER_TYPE.STRING)</b>
 *public abstract String getLicense();</b></b></pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * 
 * @version 1.1.2
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Parser {


	/**
	 * <p>Indicates the type of the parser to be used for parsing the content 
	 * of the response.</p>
	 * 
	 * @version 1.1.1
	 * <br><br> 
	 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	public static enum PARSER_TYPE {
		
		/**
		 * <p>Identifies a response parser which makes the response 
		 * available as a string content..</p>
		 * 
		 * @since 1.1.1
		 */
		STRING,
		
		/**
		 * <p>Identifies a response parser which parses JSON 
		 * serialized objects.</p>
		 * 
		 * @since 1.1.1
		 */
		JSON,
		
		/**
		 * <p>Identifies a response parser which deserializes binary 
		 * serialized objects.</p>
		 * 
		 * @since 1.1.1
		 */
		OBJECT,
		
		/**
		 * <p>The default value which indicates that the {@link Class} 
		 * set in the {@link Parser#value()} property should be used.</p>
		 * 
		 * @since 1.1.1
		 */
		UNDEFINED;
	};
	
	
	/**
	 * <p>An instance of the {@link PARSER_TYPE} enum which is used to identify 
	 * a pre-packaged {@link ResponseParser} available in the library.</p> 
	 * 
	 * @return an instance of the {@link PARSER_TYPE}. 
	 * <br><br>
	 * @since 1.1.2
	 */
	public PARSER_TYPE value() default PARSER_TYPE.UNDEFINED;
	
	/**
	 * <p>The {@link Class} of the {@link ResponseParser} to be used. Users can 
	 * create their own response parsers by extending {@link ResponseParser} and 
	 * use them in this context.</p>
	 * 
	 * <p>By default, a {@link StringResponseParser} is used.</p>
	 * 
	 * <code>
     * <pre>@Request("/license.txt")<br><b>@Parser(type = CustomParser.class)</b>
     *public abstract String getLicense();</b></b></pre>
     * </code>
	 * 
	 * @return the {@link Class} of the {@link ResponseParser} to be used
	 * <br><br>
	 * @since 1.1.1
	 */
	public Class<? extends ResponseParser<?>> type() default StringResponseParser.class;
}
