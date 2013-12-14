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

import com.google.gson.Gson;
import com.lonepulse.robozombie.annotation.Detach;
import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Entity;
import com.lonepulse.robozombie.annotation.PUT;
import com.lonepulse.robozombie.annotation.Serialize;
import com.lonepulse.robozombie.model.User;
import com.lonepulse.robozombie.proxy.InvocationContext;
import com.lonepulse.robozombie.request.AbstractSerializer;

/**
 * <p>An interface which represents a dummy endpoint with request method definitions that 
 * use various pre-fabricated and custom serializers.</p>
 * 
 * @version 1.1.0
 * <br><br> 
 * @since 1.2.4
 * <br><br> 
 * @category test
 * <br><br> 
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Serialize(JSON)
@Endpoint(host = "0.0.0.0", port = 8080)
public interface SerializerEndpoint {
	
	
	/**
	 * <p>A mock request which sends a JSON serialized model using the inherited serializer.</p>
	 * 
	 * @param user
	 * 			the {@link User} model to be serialized to a JSON string
	 * 
	 * @since 1.2.4
	 */
	@PUT("/json")
	void serializeJson(@Entity User user);
	
	/**
	 * <p>A mock request which sends an XML serialized model.</p>
	 * 
	 * @param user
	 * 			the {@link User} model to be serialized to an XML string
	 * 
	 * @since 1.2.4
	 */
	@PUT("/xml") @Serialize(XML)
	void serializeXml(@Entity User user);
	
	/**
	 * <p>A mock request which specifies the use of a plain serializer which 
	 * does a trivial attempt at converting the model to a string.</p>
	 *
	 * @param user
	 * 			the {@link User} model to be serialized to a simple string
	 * 
	 * @since 1.2.4
	 */
	@PUT("/plain") @Serialize(PLAIN)
	void plainString(@Entity User user);

	
	static final class Redactor extends AbstractSerializer<User, String> {
		
		public Redactor() {
			
			super(String.class);
		}

		@Override
		protected String serialize(InvocationContext context, User user) {

			user.setFirstName("<redacted>");
			user.setLastName("<redacted>");
			
			return new Gson().toJson(user);
		}
	}
	
	/**
	 * <p>A mock request with a model which should be serialized using a custom serializer.</p>
	 * 
	 * @param user
	 * 			the model which should be serialized using a custom serializer
	 * 
	 * @since 1.2.4
	 */
	@PUT("/custom") @Serialize(type = Redactor.class) 
	void serializeCustom(@Entity User user);
	
	/**
	 * <p>Sends a request which detaches the inherited serializer defined on the endpoint.</p>
	 *
	 * @param user
	 * 			the model which should not be processed by a serializer
	 *
	 * @since 1.2.4
	 */
	@PUT("/detach") @Detach(Serialize.class)
	void detachSerializer(@Entity User user);
	
	
	static final class UninstantiableSerializer extends AbstractSerializer<String, String> {
		
		public UninstantiableSerializer(String illegalParam) { //illegal parameterized constructor
			
			super(String.class);
		}
		
		@Override
		protected String serialize(InvocationContext context, String entity) {
			
			return entity;
		}
	}
	
	/**
	 * <p>A mock request which uses a custom serializer that cannot be instantiated.</p>
	 * 
	 * @param user
	 * 			the model which should be serialized using a custom serializer
	 * 
	 * @since 1.2.4
	 */
	@PUT("/uninstantiableserializer") 
	@Serialize(type = UninstantiableSerializer.class)
	String uninstantiableSerializer(@Entity String entity);
	
	
	static final class IllegalSerializer extends AbstractSerializer<String, Object> {
		
		public IllegalSerializer(String param) {
			
			super(Object.class); //illegal type
		}
		
		@Override
		protected String serialize(InvocationContext context, String entity) {
			
			return entity;
		}
	}
	
	/**
	 * <p>A mock request which uses a custom serializer that cannot be instantiated.</p>
	 * 
	 * @param user
	 * 			the model which should be serialized using a custom serializer
	 * 
	 * @since 1.2.4
	 */
	@PUT("/illegalSerializerserializer") 
	@Serialize(type = IllegalSerializer.class)
	String illegalSerializer(@Entity String entity);
}
