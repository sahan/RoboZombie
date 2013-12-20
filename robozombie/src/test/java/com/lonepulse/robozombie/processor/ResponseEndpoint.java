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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.GET;
import com.lonepulse.robozombie.model.User;

/**
 * <p>An endpoint with request definitions to test generic response handling.</p>
 * 
 * @version 1.1.0
 * <br><br> 
 * @since 1.3.0
 * <br><br> 
 * @category test
 * <br><br> 
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Endpoint("http://0.0.0.0:8080")
public interface ResponseEndpoint {
	
	/**
	 * <p>A mock request which receives a response with a failure code.</p>
	 * 
	 * @since 1.3.0
	 */
	@GET("/failure")
	public String failure();
	
	/**
	 * <p>A mock request which receives a 204 response code.</p>
	 * 
	 * @since 1.3.0
	 */
	@GET("/nocontent")
	public String noContent();
	
	/**
	 * <p>A mock request which receives a 205 response code.</p>
	 * 
	 * @since 1.3.0
	 */
	@GET("/resetcontent")
	public String resetContent();
	
	/**
	 * <p>A mock request expects the raw {@link HttpResponse}.</p>
	 *
	 * @since 1.3.0
	 */
	@GET("/rawresponse")
	public HttpResponse rawResponse();
	
	/**
	 * <p>A mock request expects the raw {@link HttpEntity}.</p>
	 *
	 * @since 1.3.0
	 */
	@GET("/rawentity")
	public HttpEntity rawEntity();
	
	/**
	 * <p>A mock request without an attached serializer.</p>
	 *
	 * @since 1.3.0
	 */
	@GET("/nodeserializer")
	public User noDeserializer();
}
