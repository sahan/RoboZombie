package com.lonepulse.robozombie.request;

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

import com.lonepulse.robozombie.annotation.Entity.ContentType;
import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>Exposes all available {@link AbstractSerializer}s, resolves concrete instances of their serializer types 
 * and mediates communication.</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum Serializers implements Serializer<Object, Object> {
	
	/**
	 * See {@link PlainSerializer}.
	 * 
	 * @since 1.2.4
	 */
	PLAIN(new PlainSerializer()),
	
	/**
	 * See {@link JsonSerializer}.
	 * 
	 * @since 1.2.4
	 */
	JSON(new JsonSerializer()),
	
	/**
	 * See {@link XmlSerializer}.
	 * 
	 * @since 1.2.4
	 */
	XML(new XmlSerializer());
	
	
	
	private static final Map<String, AbstractSerializer<?,?>> SERIALIZERS = new HashMap<String, AbstractSerializer<?,?>>();
	
	private final AbstractSerializer<Object,?> serializer;
	
	
	private Serializers(AbstractSerializer<Object,?> serializer) {
	
		this.serializer = serializer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object run(Object input, InvocationContext context) {
	
		return this.serializer.run(input, context);
	}
	
	/**
	 * <p>Retrieves the {@link AbstractSerializer} which is identified by the given {@link ContentType}.</p>
	 * 
	 * @param contentType
	 * 			the {@link ContentType} whose implementation of {@link AbstractSerializer} is retrieved
	 * 
	 * @return the implementation of {@link AbstractSerializer} which serves the given {@link ContentType}
	 * <br><br>
	 * @since 1.2.4
	 */
	public static final AbstractSerializer<?,?> resolve(ContentType contentType) {
		
		switch (contentType) {
		
			case JSON:
				return Serializers.JSON.serializer;
					
			case XML:
				return Serializers.XML.serializer;
				
			case PLAIN: case UNDEFINED: default:
				return Serializers.PLAIN.serializer;
		}
	}
	
	/**
	 * <p>Retrieves the {@link AbstractSerializer} which is defined for the given {@link Class}.</p>
	 * 
	 * @param serializerType
	 * 			the {@link Class} whose implementation of {@link AbstractSerializer} is retrieved
	 * 
	 * @return the implementation of {@link AbstractSerializer} for the given {@link Class}
	 * <br><br>
	 * @throws SerializerInstantiationException
	 * 			if a custom serializer failed to be instantiated using its <b>default constructor</b> 
	 * <br><br>
	 * @since 1.2.4
	 */
	public static final AbstractSerializer<?,?> resolve(Class<? extends AbstractSerializer<?,?>> serializerType) {
		
		try {
			
			synchronized(SERIALIZERS) {
				
				String key = serializerType.getName();
				
				AbstractSerializer<?,?> serializer = SERIALIZERS.get(key);
				
				if(serializer == null) {
					
					serializer = serializerType.newInstance();
					SERIALIZERS.put(key, serializer);
				}
				
				return serializer;
			}
		}
		catch(Exception e) {
			
			throw new SerializerInstantiationException(serializerType);
		}
	}
}
