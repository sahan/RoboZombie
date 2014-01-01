package com.lonepulse.robozombie.processor;

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

import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.GET;
import com.lonepulse.robozombie.annotation.Header;
import com.lonepulse.robozombie.annotation.Stateful;

/**
 * <p>An endpoint which uses cookies for state mangement.</p>
 * 
 * @version 1.1.1
 * <br><br> 
 * @since 1.3.0
 * <br><br> 
 * @category test
 * <br><br> 
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
@Stateful
@Endpoint("http://0.0.0.0:8080")
public interface StateEndpoint {
	
	/**
	 * <p>A mock request which initiates a stateful connection using cookies.</p>
	 * 
	 * @param cookieHeader
	 * 			a response header which the server sets with a cookie
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.3.0
	 */
	@GET("/stateful")
	public String stateful(@Header("Set-Cookie") StringBuilder cookieHeader);
}
