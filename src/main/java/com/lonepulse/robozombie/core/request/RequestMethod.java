package com.lonepulse.robozombie.core.request;

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

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;

/**
 * <p>This enum is used to identify the request type. This may be a basic HTTP request 
 * or it might be a JSON based web service request...etc
 * 
 * @version 1.1.3
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum RequestMethod {

	/**
	 * <p>Identifies an {@link HttpGet} request.
	 */
	HTTP_GET,
	
	/**
	 * <p>Identifies an {@link HttpPost} request.
	 */
	HTTP_POST,
	
	/**
	 * <p>Identifies an {@link HttpPut} request.
	 */
	HTTP_PUT,
	
	/**
	 * <p>Identifies an {@link HttpDelete} request.
	 */
	HTTP_DELETE,
	
	/**
	 * <p>Identifies an {@link HttpHead} request.
	 */
	HTTP_HEAD,
	
	/**
	 * <p>Identifies an {@link HttpTrace} request.
	 */
	HTTP_TRACE,
	
	/**
	 * <p>Identifies an {@link HttpOptions} request.
	 */
	HTTP_OPTIONS;
}
