package com.lonepulse.robozombie.test.endpoint;

/*
 * #%L
 * ZombieLink
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

import com.lonepulse.robozombie.core.annotation.Endpoint;
import com.lonepulse.robozombie.core.annotation.Header;
import com.lonepulse.robozombie.core.annotation.Parser;
import com.lonepulse.robozombie.core.annotation.Parser.PARSER_TYPE;
import com.lonepulse.robozombie.core.annotation.Request;
import com.lonepulse.robozombie.core.annotation.Stateful;

/**
 * <p>An interface which represents a dummy endpoint with mock paths.
 * 
 * @category test
 * <br><br> 
 * @since 1.2.4
 * <br><br> 
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Stateful
@Parser(PARSER_TYPE.STRING)
@Endpoint(host = "0.0.0.0", port = "8080")
public interface MockEndpoint {
	
	/**
	 * <p>A mock request for which initiates a stateful connection.
	 * 
	 * @param cookieHeader
	 * 			a response header which the server sets with a cookie
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/stateful")
	public String stateful(@Header("Set-Cookie") StringBuilder cookieHeader);
}
