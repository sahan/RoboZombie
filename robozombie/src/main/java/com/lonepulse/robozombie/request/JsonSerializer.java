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

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>An extension of {@link AbstractSerializer} which converts an object to a JSON string.</p>
 * 
 * <p><b>Note</b> that this serializer requires the <a href="http://code.google.com/p/google-gson">GSON</a> 
 * library to be available on the classpath to be active. If GSON is not detected, this serializer will 
 * be disabled and any attempt to use it will result in an {@link IllegalStateException}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
final class JsonSerializer extends AbstractSerializer<Object, String> {
	
	
	private static final Logger LOGGER = Logger.getLogger(JsonSerializer.class.getName());
	
	private static final String ERROR_CONTEXT_UNAVAILABLE = new StringBuilder()
	.append("\n\nGSON (gson-2.2.4.jar) was not detected on the classpath. ")
	.append("To enable JSON serialization with @Serialize(ContentType.JSON) ")
	.append("add the following dependency to your build configuration.\n\n")
	.append("Maven:\n")
	.append("<dependency>\n")
	.append("  <groupId>com.google.code.gson</groupId>\n")
	.append("  <artifactId>gson</artifactId>\n")
	.append("  <version>2.2.4</version>\n")
	.append("</dependency>\n\n")
	.append("Scala SBT:\n")
	.append("libraryDependencies += \"com.google.code.gson\" % \"gson\" % \"2.2.4\"\n\n")
	.append("Gradle:\n")
	.append("compile 'com.google.code.gson:gson:2.2.4'\n\n")
	.append("...or grab the JAR from ")
	.append("http://code.google.com/p/google-gson/downloads/list \n\n").toString();
	
	private static final String ERROR_CONTEXT_INCOMPATIBLE = new StringBuilder()
	.append("\n\nFailed to initialize JsonSerializer; use of @Serialize(ContentType.JSON) is disabled.\n")
	.append("Please make sure that you are using version 2.2.4 of GSON.\n\n").toString();
	
	
	private static Class<?> Gson;
	private static Class<?> TypeToken;
	
	private static Method Gson_toJson;
	private static Method TypeToken_GET;
	private static Method TypeToken_getType;
	
	private static Object gson; //thread-safe, as proven by http://goo.gl/RUyPdn
	
	private static boolean unavailable;
	private static boolean incompatible;
	
	static {
		
		try {
			
			Gson = Class.forName("com.google.gson.Gson");
			Gson_toJson = Gson.getDeclaredMethod("toJson", Object.class, Type.class);
			
			TypeToken = Class.forName("com.google.gson.reflect.TypeToken");
			TypeToken_GET = TypeToken.getDeclaredMethod("get", Class.class);
			TypeToken_getType = TypeToken.getDeclaredMethod("getType");
			
			gson = Gson.newInstance();
		}
		catch (ClassNotFoundException cnfe) { 
			
			unavailable = true;
			LOGGER.log(Level.WARNING, ERROR_CONTEXT_UNAVAILABLE);
		}
		catch(Exception e) {
			
			incompatible = true;
			LOGGER.log(Level.WARNING, ERROR_CONTEXT_INCOMPATIBLE);
		}
	}
	
	
	/**
	 * <p>Creates a new instance of {@link JsonSerializer} and registers the output {@link String} class as 
	 * the content-type which results from serialization.</p>
	 *
	 * @since 1.3.0
	 */
	public JsonSerializer() {
		
		super(String.class);
	}

	/**
     * <p>Serializes the given model using <b>GSON</b> and returns the resulting JSON string.</p>
     * 
     * <p>See {@link AbstractSerializer#serialize(InvocationContext, Object)}.</p>
     * 
     * @param context
     * 			the {@link InvocationContext} to discover information on the proxy invocation
     * <br><br>
	 * @param input
	 * 			the input model to be serialized to a JSON string
	 * <br><br>
	 * @return the JSON {@link String} which represents the provided input model, else {@code null} if the 
	 * 		   provided input was {@code null}
	 * <br><br>
	 * @throws IllegalStateException 
	 * 			if the <b>GSON library</b> was not found on the classpath or if an incompatible version 
	 * 			of the library is being used
	 * <br><br>
	 * @throws SerializerException
	 * 			if JSON serialization failed for the given entity using the Gson library 
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	protected String serialize(InvocationContext context, Object input) {
		
		if(unavailable || incompatible) {
			
			throw new IllegalStateException(unavailable? ERROR_CONTEXT_UNAVAILABLE :ERROR_CONTEXT_INCOMPATIBLE);
		}
		
		try {
			
			return input == null? null :(String) Gson_toJson.invoke(
					gson, input, TypeToken_getType.invoke(TypeToken_GET.invoke(null, input.getClass())));
		} 
		catch(Exception e) {
			
			throw new SerializerException(new StringBuilder("JSON serialization failed for request <")
			.append(context.getRequest().getName())
			.append("> on endpoint <")
			.append(context.getEndpoint().getName())
			.append(">").toString(), e);
		}
	}
}
