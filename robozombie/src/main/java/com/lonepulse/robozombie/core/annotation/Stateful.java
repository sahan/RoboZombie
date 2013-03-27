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
 * <p>Marks the policy of a <b>remote endpoint</b> as being <i>stateful</i>, i.e. 
 * per user sesssion management is performed.</p>
 *
 * <b>Usage:</b>
 * <br>
 * <br>
 * <ol>
 * <li>
 * <p>At <b>type-level</b> on an endpoint <i>interface</i> for all requests.</p><br>
 * <code>
 * <pre><b> @Stateful</b>&nbsp;@Endpoint("accounts.google.com")
 * public interface GoogleAccountsEndpoint {<br> }
 * </pre>
 * </code>
 * </li>
 * <li>
 * <p>At <b>method-level</b> on an endpoint <i>request</i>.</p><br>
 * <code>
 * <pre><b> @Stateful </b>@Request("/addressbook")
 * public abstract AddressBook getAddressBook();</b></b></pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Stateful {}
