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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

/**
 * <p>Allows session management to be performed using {@link Cookie}s together with a {@link CookieStore}.</p>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <code>
 * <pre><b>@Stateful</b>
 *&#064;Endpoint("https://api.github.com")
 *public interface GitHubEndpoint {<br>&nbsp;&nbsp;...<br>}</b>
 * </pre>
 * </code>
 * </p>
 * <br>
 * @version 1.1.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Stateful {}
