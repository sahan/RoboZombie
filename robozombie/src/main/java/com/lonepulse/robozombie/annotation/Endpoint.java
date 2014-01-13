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

/**
 * <p>Identifies an interface as an endpoint definition and accepts the URI which can be used to reach 
 * the services offered by the endpoint.</p>
 * 
 * <p><b>Note</b> that the given URI may be <i>parameterized</i> to accept @{@link PathParam}s and all 
 * subpaths are directly appended to this root URI.</p>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre><b>@Endpoint("https://api.github.com")</b>
 *public interface GitHubEndpoint {<br>&nbsp;&nbsp;...<br>}</pre>
 * </code>
 * </p>
 * <br>
 * @version 1.2.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Endpoint {
	
	/**
	 * <p>The endpoint <a href="http://en.wikipedia.org/wiki/Uniform_resource_identifier">URI</a> which 
	 * requires a minimum of <i>scheme</i>://<i>domain</i></p>
	 * 
	 * @return the endpoint URI
	 * <br><br>
	 * @since 1.1.2
	 */
	String value();
}
