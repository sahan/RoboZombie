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

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.annotation.Entity.ContentType;
import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>Exposes all available {@link AbstractDeserializer}s, resolves concrete instances of their deserializer 
 * types and mediates communication.</p> 
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public enum Deserializers implements Deserializer<Object> {
	
	
	/**
	 * <p>See {@link PlainDeserializer}.</p>
	 * 
	 * @since 1.3.0
	 */
	RAW(new PlainDeserializer()),
	
	/**
	 * <p>See {@link JsonDeserializer}.</p>
	 * 
	 * @since 1.3.0
	 */
	JSON(new JsonDeserializer()),
	
	/**
	 * <p>See {@link XmlDeserializer}.</p>
	 * 
	 * @since 1.3.0
	 */
	XML(new XmlDeserializer());
	
	
	
	private static final Map<String, AbstractDeserializer<?>> DESERIALIZERS = new HashMap<String, AbstractDeserializer<?>>();
	
	private final AbstractDeserializer<?> deserializer;
	
	
	private Deserializers(AbstractDeserializer<?> deserializer) {
	
		this.deserializer = deserializer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object run(InvocationContext context, HttpResponse response) {
		
		return this.deserializer.run(context, response);
	}
	
	/**
	 * <p>Retrieves the {@link AbstractDeserializer} which is identified by the given {@link ContentType}.</p>
	 * 
	 * @param context
	 * 			the {@link ContentType} whose associated {@link AbstractDeserializer} should be retrieved
	 * <br><br>
	 * @return the implementation of {@link AbstractDeserializer} which serves the given {@link ContentType}
	 * <br><br>
	 * @since 1.3.0
	 */
	public static final AbstractDeserializer<?> resolve(ContentType deserializerType) {
		
		switch (deserializerType) {
		
			case JSON:
				return Deserializers.JSON.deserializer;
					
			case XML:
				return Deserializers.XML.deserializer;
				
			case PLAIN: case UNDEFINED: default:
				return Deserializers.RAW.deserializer;
		}
	}
	
	/**
	 * <p>Retrieves the {@link AbstractDeserializer} which is defined for the given {@link Class}.</p>
	 * 
	 * @param deserializerType
	 * 			the {@link Class} whose implementation of {@link AbstractDeserializer} is retrieved
	 * <br><br>
	 * @return the implementation of {@link AbstractDeserializer} for the given {@link Class}
	 * <br><br>
	 * @throws DeserializerInstantiationException
	 * 			if a custom deserializer failed to be instantiated using its <b>default constructor</b> 
	 * <br><br>
	 * @since 1.3.0
	 */
	public static final AbstractDeserializer<?> resolve(Class<? extends AbstractDeserializer<?>> deserializerType) {
		
		try {
			
			synchronized(DESERIALIZERS) {
				
				String key = deserializerType.getName();
				
				AbstractDeserializer<?> deserializer = DESERIALIZERS.get(key);
				
				if(deserializer == null) {
					
					deserializer = deserializerType.newInstance();
					DESERIALIZERS.put(key, deserializer);
				}
				
				return deserializer;
			}
		}
		catch(Exception e) {
			
			throw new DeserializerInstantiationException(deserializerType, e);
		}
	}
}
