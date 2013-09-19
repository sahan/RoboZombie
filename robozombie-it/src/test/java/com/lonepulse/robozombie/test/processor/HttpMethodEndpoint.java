package com.lonepulse.robozombie.test.processor;

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
import com.lonepulse.robozombie.annotation.Entity;
import com.lonepulse.robozombie.annotation.FormParam;
import com.lonepulse.robozombie.annotation.Header;
import com.lonepulse.robozombie.annotation.HeaderSet;
import com.lonepulse.robozombie.annotation.Parser;
import com.lonepulse.robozombie.annotation.Parser.ParserType;
import com.lonepulse.robozombie.annotation.PathParam;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.request.RequestMethod;

/**
 * <p>An interface which represents a dummy endpoint with request method definitions which 
 * represent the supported HTTP method types.
 * 
 * @category test
 * <br><br> 
 * @version 1.1.1
 * <br><br> 
 * @since 1.2.4
 * <br><br> 
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Parser(ParserType.STRING)
@Endpoint(host = "0.0.0.0", port = "8080")
public interface HttpMethodEndpoint {
	
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
	@Request(path = "/postrequest", method = RequestMethod.POST)
	public String postRequest(@FormParam("name") String name, 
							  @FormParam("age") String age,
							  @FormParam("location") String location);
	
	/**
	 * <p>A mock request which uses the HTTP method PUT.
	 * 
	 * @param user
	 * 			the JSON string which represents the entity 
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/putrequest", method = RequestMethod.PUT)
	public String putRequest(@Entity String user);
	
	/**
	 * <p>A mock request which uses the HTTP method DELETE.
	 * 
	 * @param id
	 * 			the id of the entity to delete 
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/deleterequest/:id", method = RequestMethod.DELETE)
	public String deleteRequest(@PathParam("id") String id);
	
	/**
	 * <p>A mock request which uses the HTTP method HEAD.
	 * 
	 * @param proxyAuthenticate
	 * 			retrieves meta-information about any required authentication 
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/headrequest", method = RequestMethod.HEAD)
	public void headRequest(@Header("Proxy-Authenticate") StringBuilder proxyAuthenticate);
	
	/**
	 * <p>A mock request which uses the HTTP method HEAD.
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/tracerequest", method = RequestMethod.TRACE)
	@HeaderSet({@HeaderSet.Header(name = "Via", value = "1.0 example1.com, 1.1 example2.com"),
				@HeaderSet.Header(name = "Max-Forwards", value = "6")})
	public void traceRequest();
	
	/**
	 * <p>A mock request which uses the HTTP method OPTIONS.
	 * 
	 * @param contentType
	 * 			queries the content type for an enclosed entity 
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/optionsrequest", method = RequestMethod.OPTIONS)
	public void optionsRequest(@Header("Content-Type") StringBuilder contentType);
}
