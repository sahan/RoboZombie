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

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This is an extension of {@link AbstractDeserializer} which parses <b>JSON response content</b> 
 * to an instance of the model specified on the endpoint definition.</p>
 * 
 * <p><b>Note</b> that this deserializer requires the <a href="http://code.google.com/p/google-gson">GSON</a> 
 * library to be available on the classpath to be active. If GSON is not detected, this deserializer will 
 * be disabled and any attempt to use it will result in an {@link IllegalStateException}.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
final class JsonDeserializer extends AbstractDeserializer<Object> {
	
	
	private static final String ERROR_CONTEXT_UNAVAILABLE = new StringBuilder()
	.append("\n\nGSON (gson-2.2.4.jar) was not detected on the classpath. ")
	.append("To enable JSON deserialization with @Deserializer(ContentType.JSON) ")
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
	.append("\n\nFailed to initialize JsonDeserializer; use of @Deserializer(ContentType.JSON) is disabled.\n")
	.append("Please make sure that you are using version 2.2.4 of GSON.\n\n").toString();
	
	
	private static Class<?> Gson;
	private static Class<?> TypeToken;
	
	private static Method Gson_fromJson;
	private static Method TypeToken_GET;
	private static Method TypeToken_getType;
	
	private static Object gson; //thread-safe, as proven by http://goo.gl/RUyPdn
	
	private static boolean unavailable;
	private static boolean incompatible;
	
	static {
		
		try {
			
			Gson = Class.forName("com.google.gson.Gson");
			Gson_fromJson = Gson.getDeclaredMethod("fromJson", String.class, Type.class);
			
			TypeToken = Class.forName("com.google.gson.reflect.TypeToken");
			TypeToken_GET = TypeToken.getDeclaredMethod("get", Class.class);
			TypeToken_getType = TypeToken.getDeclaredMethod("getType");
			
			gson = Gson.newInstance();
		}
		catch (ClassNotFoundException cnfe) { 
			
			unavailable = true;
			Log.w(JsonDeserializer.class.getSimpleName(), ERROR_CONTEXT_UNAVAILABLE);
		}
		catch(Exception e) {
			
			incompatible = true;
			Log.w(JsonDeserializer.class.getSimpleName(), ERROR_CONTEXT_INCOMPATIBLE);
		}
	}
	
	
	/**
	 * <p>Creates a new instance of {@link JsonDeserializer} and register the generic type {@link Object} 
	 * as the entity which results from its <i>parse</i> operation.</p>
	 *
	 * @since 1.2.4
	 */
	public JsonDeserializer() {
		
		super(Object.class);
	}
	
	/**
     * <p>Parses the JSON String in the {@link HttpResponse} using <b>GSON</b> and returns the entity modeled 
     * by the JSON data.</p>
     * 
     * <p>See {@link AbstractDeserializer#deserialize(HttpResponse, InvocationContext)}.
     * 
	 * @param httpResponse
	 * 				the {@link HttpResponse} which contains the JSON content to be deserialized to a model
	 * <br><br>
	 * @param context
	 * 				the {@link InvocationContext} which is used to discover further information regarding 
	 * 				the proxy invocation
     * <br><br>
	 * @return the model which was deserialized from the JSON response content, else {@code null} if the 
	 * 		   given {@link HttpResponse} did not contain an {@link HttpEntity}
	 * <br><br>
	 * @throws IllegalStateException 
	 * 				if the <b>GSON library</b> was not found on the classpath or if an incompatible version 
	 * 				of the library is being used
	 * <br><br>
	 * @throws DeserializerException
	 * 			if JSON deserialization failed for the given entity using the Gson library 
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	protected Object deserialize(HttpResponse httpResponse, InvocationContext context) {
		
		if(unavailable || incompatible) {
			
			throw new IllegalStateException(unavailable? ERROR_CONTEXT_UNAVAILABLE :ERROR_CONTEXT_INCOMPATIBLE);
		}
		
		HttpEntity entity = httpResponse.getEntity();
		
		try {
			
			return entity == null? null :Gson_fromJson.invoke(gson, EntityUtils.toString(entity), 
					TypeToken_getType.invoke(TypeToken_GET.invoke(null, context.getRequest().getReturnType())));
		} 
		catch(Exception e) {
			
			throw new DeserializerException(new StringBuilder("JSON deserialization failed for request <")
			.append(context.getRequest().getName())
			.append("> on endpoint <")
			.append(context.getEndpoint().getName())
			.append(">").toString(), e);
		}
	}
}
