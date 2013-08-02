package com.lonepulse.robozombie.test.endpoint;

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

import com.lonepulse.robozombie.core.annotation.Endpoint;
import com.lonepulse.robozombie.core.annotation.Header;
import com.lonepulse.robozombie.core.annotation.HeaderSet;
import com.lonepulse.robozombie.core.annotation.Param;
import com.lonepulse.robozombie.core.annotation.Parser;
import com.lonepulse.robozombie.core.annotation.Parser.PARSER_TYPE;
import com.lonepulse.robozombie.core.annotation.Request;
import com.lonepulse.robozombie.core.annotation.Stateful;
import com.lonepulse.robozombie.core.request.RequestMethod;

/**
 * <p>An interface which represents a dummy endpoint with mock paths.
 * 
 * @category test
 * <br><br> 
 * @version 1.1.1
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
	 * <p>A mock request which uses the HTTP method POST.
	 * 
	 * @param name
	 * 			the first request parameter
	 * 
	 * @param age
	 * 			the second request parameter
	 * 
	 * @param location
	 * 			the third request parameter
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/postrequest", method = RequestMethod.HTTP_POST)
	public String postRequest(@Param("name") String name, 
					   @Param("age") String age,
					   @Param("location") String location);
	
	/**
	 * <p>A mock request which uses the HTTP method PUT.
	 * 
	 * @param name
	 * 			the first request parameter
	 * 
	 * @param age
	 * 			the second request parameter
	 * 
	 * @param location
	 * 			the third request parameter
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/putrequest", method = RequestMethod.HTTP_PUT)
	public String putRequest(@Param("name") String name, 
							 @Param("age") String age,
							 @Param("location") String location);
	
	/**
	 * <p>A mock request which inserts a request header.
	 * 
	 * @param userAgent
	 * 			a variable header - <i>User-Agent</i> in this case
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/requestheader")
	public String requestHeader(@Header("User-Agent") StringBuilder userAgent);
	
	/**
	 * <p>A mock request which inserts a constant set of headers.
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/headerset")
	@HeaderSet({@HeaderSet.Header(name = "Accept", value = "application/json"),
				@HeaderSet.Header(name = "Accept-Charset", value = "utf-8")})
	public String headerSet();
	
	/**
	 * <p>A mock request which initiates a stateful connection.
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
