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

import com.lonepulse.robozombie.annotation.Asynchronous;
import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Header;
import com.lonepulse.robozombie.annotation.HeaderSet;
import com.lonepulse.robozombie.annotation.Param;
import com.lonepulse.robozombie.annotation.Parser;
import com.lonepulse.robozombie.annotation.PathParam;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.annotation.Rest;
import com.lonepulse.robozombie.annotation.Stateful;
import com.lonepulse.robozombie.annotation.Parser.PARSER_TYPE;
import com.lonepulse.robozombie.request.RequestMethod;
import com.lonepulse.robozombie.response.AsyncHandler;

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
	 * <p>Sends a request with a subpath.
	 * 
	 * @return a response for the request with a subpath
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/subpath")
	public String subpath();
	
	/**
	 * <p>Sends a request with the given query parameters.
	 * 
	 * @param firstName
	 * 			the first query param
	 * 
	 * @param lastName
	 * 			the second query param
	 * 
	 * @return a response for the request with query params
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/subpathwithparams")
	public String subpathWithParams(@Param("firstName") String firstName, 
									@Param("lastName") String lastName);
	
	/**
	 * <p>Sends a RESTful request with a subpath.
	 * 
	 * @return a response for the RESTful request with a subpath
	 * 
	 * @since 1.2.4
	 */
	@Rest(path = "/restfulsubpath")
	public String restfulSubpath();
	
	/**
	 * <p>Sends a request for a RESTful subpath.
	 * 
	 * @param id
	 * 			the restful path parameter
	 * 
	 * @return the response for the RESTful request
	 * 
	 * @since 1.2.4
	 */
	@Rest(path = "/restfulsubpathwithparam/:id")
	public String restfulSubpathWithParam(@PathParam("id") String id);
	
	/**
	 * <p>Sends a request with a set of constant query paramers.
	 * 
	 * @return a response for the request with constant params
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/subpathwithconstparams", params = {@Request.Param(name = "firstName", value = "Doctor"), 
										 				 @Request.Param(name = "lastName", value = "Who")})
	public String subpathWithConstParams();
	
	/**
	 * <p>Sends a request asynchronously using {@link Asynchronous} and {@link AsyncHandler}. 
	 * 
	 * @param asyncHandler
	 * 			the {@link AsyncHandler} which handles the results of the asynchronous request execution
	 * 
	 * @return a response for the asynchronous request
	 * 
	 * @since 1.2.4
	 */
	@Asynchronous @Rest(path = "/async")
	public String async(AsyncHandler<String> asyncHandler);
	
	/**
	 * <p>Retrieves a response header from a request using {@link Header}.
	 * 
	 * @param server
	 * 			the {@link StringBuilder} which is annotated with {@code @Header} to 
	 * 			treat it as an in-out variable for retrieving the response header
	 * 
	 * @return a response whose header was retrieved 
	 * 
	 * @since 1.2.4
	 */
	@Rest(path = "/responseheader")
	public String responseHeader(@Header("Server") StringBuilder server); 
	
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
	 * <p>A mock request which uses the HTTP method DELETE.
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/deleterequest", method = RequestMethod.HTTP_DELETE)
	public String deleteRequest();
	
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
