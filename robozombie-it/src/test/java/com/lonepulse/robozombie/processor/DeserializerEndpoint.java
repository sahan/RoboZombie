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

import static com.lonepulse.robozombie.annotation.Entity.ContentType.JSON;
import static com.lonepulse.robozombie.annotation.Entity.ContentType.PLAIN;
import static com.lonepulse.robozombie.annotation.Entity.ContentType.XML;

import org.apache.http.HttpResponse;
import org.apache.http42.util.EntityUtils;

import com.google.gson.Gson;
import com.lonepulse.robozombie.annotation.Deserializer;
import com.lonepulse.robozombie.annotation.Detach;
import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.inject.InvocationContext;
import com.lonepulse.robozombie.model.User;
import com.lonepulse.robozombie.response.AbstractDeserializer;

/**
 * <p>An interface which represents a dummy endpoint with request method definitions 
 * that use various pre-fabricated and custom deserializers.
 * 
 * @category test
 * <br><br> 
 * @version 1.1.1
 * <br><br> 
 * @since 1.2.4
 * <br><br> 
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Deserializer(JSON)
@Endpoint(host = "0.0.0.0", port = "8080")
public interface DeserializerEndpoint {
	
	/**
	 * <p>A mock request which receives a response with a code that signals a failure. 
	 * Expects a domain specific exception to be thrown rather than the deserialized result.  
	 *
	 * @return the deserialized response content, which in this case should not be available
	 * 
	 * @since 1.2.4
	 */
	@Deserializer(PLAIN)
	@Request(path = "/responseerror")
	String responseError();
	
	/**
	 * <p>A mock request which receives a JSON response that is deserialized to it model.
	 * 
	 * @return the deserialized response entity
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/json")
	User parseJson();
	
	/**
	 * <p>A mock request which receives an XML response that is deserialized to it model.
	 * 
	 * @return the deserialized response entity
	 * 
	 * @since 1.2.4
	 */
	@Deserializer(XML) 
	@Request(path = "/xml")
	User parseXml();
	
	/**
	 * <p>A mock request which does not use an @{@link Deserializer} definition and defers to 
	 * the <i>raw deserializer</i> which simple retrieves the response content as a String.</p>
	 *
	 * @return the deserializer <b>raw</b> response content
	 * 
	 * @since 1.2.4
	 */
	@Deserializer(PLAIN)
	@Request(path = "/raw")
	String raw();
	
	
	static final class Redactor extends AbstractDeserializer<User> {
		
		
		public Redactor() {
			
			super(User.class);
		}

		@Override
		protected User deserialize(HttpResponse httpResponse, InvocationContext context) 
		throws Exception {

			String json = EntityUtils.toString(httpResponse.getEntity());
			
			User user = new Gson().fromJson(json, User.class);
			user.setFirstName("<redacted>");
			user.setLastName("<redacted>");
			
			return user;
		}
	}
	
	/**
	 * <p>A mock request with a response which should be deserialized by a custom deserializer.</p>
	 * 
	 * @return the deserialized response entity
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/custom")
	@Deserializer(type = Redactor.class) 
	User parseCustom();
	
	/**
	 * <p>Sends a request which detaches the inherited deserializer defined on the endpoint.</p>
	 *
	 * @return the response which should not be processed by a deserializer
	 *
	 * @since 1.2.4
	 */
	@Detach(Deserializer.class)
	@Request(path = "/detach")
	String detachDeserializer();
}
