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

import com.lonepulse.robozombie.annotation.DELETE;
import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Entity;
import com.lonepulse.robozombie.annotation.FormParam;
import com.lonepulse.robozombie.annotation.GET;
import com.lonepulse.robozombie.annotation.HEAD;
import com.lonepulse.robozombie.annotation.Header;
import com.lonepulse.robozombie.annotation.Headers;
import com.lonepulse.robozombie.annotation.OPTIONS;
import com.lonepulse.robozombie.annotation.POST;
import com.lonepulse.robozombie.annotation.PUT;
import com.lonepulse.robozombie.annotation.PathParam;
import com.lonepulse.robozombie.annotation.QueryParam;
import com.lonepulse.robozombie.annotation.TRACE;

/**
 * <p>An endpoint with request definitions which use all the supported HTTP method types.</p>
 * 
 * @version 1.1.1
 * <br><br> 
 * @since 1.3.0
 * <br><br> 
 * @category test
 * <br><br> 
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Endpoint("http://0.0.0.0:8080")
public interface HttpMethodEndpoint {
	
	/**
	 * <p>A mock request which uses the HTTP method GET.</p>
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
	 * @since 1.3.0
	 */
	@GET("/getrequest")
	public String getRequest(@QueryParam("name") String name, 
							 @QueryParam("age") String age,
							 @QueryParam("location") String location);
	
	/**
	 * <p>A mock request which uses the HTTP method POST.</p>
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
	 * @since 1.3.0
	 */
	@POST("/postrequest")
	public String postRequest(@FormParam("name") String name, 
							  @FormParam("age") String age,
							  @FormParam("location") String location);
	
	/**
	 * <p>A mock request which uses the HTTP method PUT.</p>
	 * 
	 * @param user
	 * 			the JSON string which represents the entity 
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.3.0
	 */
	@PUT("/putrequest")
	public String putRequest(@Entity String user);
	
	/**
	 * <p>A mock request which uses the HTTP method DELETE.</p>
	 * 
	 * @param id
	 * 			the id of the entity to delete 
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.3.0
	 */
	@DELETE("/deleterequest/{id}")
	public String deleteRequest(@PathParam("id") String id);
	
	/**
	 * <p>A mock request which uses the HTTP method HEAD.</p>
	 * 
	 * @param proxyAuthenticate
	 * 			retrieves meta-information about any required authentication 
	 * 
	 * @since 1.3.0
	 */
	@HEAD("/headrequest")
	public void headRequest(@Header("Proxy-Authenticate") StringBuilder proxyAuthenticate);
	
	/**
	 * <p>A mock request which uses the HTTP method HEAD.</p>
	 * 
	 * @since 1.3.0
	 */
	@TRACE("/tracerequest")
	@Headers({@Headers.Header(name = "Via", value = "1.0 example1.com, 1.1 example2.com"),
			  @Headers.Header(name = "Max-Forwards", value = "6")})
	public void traceRequest();
	
	/**
	 * <p>A mock request which uses the HTTP method OPTIONS.</p>
	 * 
	 * @param contentType
	 * 			queries the content type for an enclosed entity 
	 * 
	 * @since 1.3.0
	 */
	@OPTIONS("/optionsrequest")
	public void optionsRequest(@Header("Content-Type") StringBuilder contentType);
}
