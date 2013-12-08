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

import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.PathParam;
import com.lonepulse.robozombie.annotation.Request;

/**
 * <p>An interface which represents a dummy endpoint with request definition using mock paths.
 * 
 * @category test
 * <br><br> 
 * @version 1.1.1
 * <br><br> 
 * @since 1.2.4
 * <br><br> 
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Endpoint(host = "0.0.0.0", port = 8080)
public interface PathEndpoint {
	
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
	 * <p>Sends a request for a RESTful subpath with a path parameter.
	 * 
	 * @param id
	 * 			the restful path parameter
	 * 
	 * @return the response for the RESTful request
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/restfulsubpathwithparam/{id}")
	public String restfulSubpathWithParam(@PathParam("id") String id);
	
	/**
	 * <p>Sends a request for a RESTful subpath with a path parameter 
	 * of an illegal type.
	 * 
	 * @param id
	 * 			the restful path parameter of the illegal type {@link Long}
	 * 
	 * @return the deserialized response content, which in this case should not be available
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/restfulsubpathwithillegalparamtype/{id}")
	public String restfulSubpathWithIllegalParamType(@PathParam("id") Long id);
}
