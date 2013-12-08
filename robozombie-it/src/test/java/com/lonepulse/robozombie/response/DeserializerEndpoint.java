package com.lonepulse.robozombie.response;

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

import static com.lonepulse.robozombie.annotation.Entity.ContentType.JSON;
import static com.lonepulse.robozombie.annotation.Entity.ContentType.XML;

import com.lonepulse.robozombie.annotation.Deserializer;
import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.model.User;

/**
 * <p>An interface which represents a dummy endpoint with request method definitions which tests 
 * integration with <b>Gson</b> and <b>Simple XML</b>.</p>
 * 
 * @category test
 * <br><br> 
 * @version 1.1.0
 * <br><br> 
 * @since 1.2.4
 * <br><br> 
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Endpoint(host = "0.0.0.0", port = 8080)
public interface DeserializerEndpoint {
	
	/**
	 * <p>A mock request which uses the <b>Gson</b> library which is found to be unavailable.</p>
	 * 
	 * @return the deserialized model based on the returned response content
	 * <br><br>
	 * @since 1.2.4
	 */
	@Deserializer(JSON) 
	@Request(path = "/gsonunavailable")
	User gsonUnavailable();
	
	/**
	 * <p>A mock request which uses the <b>Simple-XML</b> library which is found to be unavailable.</p>
	 * 
	 * @return the deserialized model based on the returned response content
	 * <br><br>
	 * @since 1.2.4
	 */
	@Deserializer(XML) 
	@Request(path = "/simplexmlunavailable")
	User simpleXmlUnavailable();
}
