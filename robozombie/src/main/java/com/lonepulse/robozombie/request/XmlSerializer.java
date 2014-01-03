package com.lonepulse.robozombie.request;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>This {@link AbstractSerializer} allows an object to be converted to its XML representation.</p>
 * 
 * <p><b>Note</b> that this serializer requires the <a href="http://simple.sourceforge.net">Simple-XML</a> 
 * library to be available on the classpath to be active. If Simple-XML is not detected, this serializer 
 * will be disabled and any attempt to use it will result in an {@link IllegalStateException}.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
final class XmlSerializer extends AbstractSerializer<Object, String> {

	
	private static final Logger LOGGER = Logger.getLogger(XmlSerializer.class.getName());
	
	private static final String ERROR_CONTEXT_UNAVAILABLE = new StringBuilder()
	.append("\n\nSimple-XML (simple-xml-2.7.1.jar) was not detected on the classpath. ")
	.append("To enable XML serialization with @Serialize(ContentType.XML) ")
	.append("add the following dependency to your build configuration.\n\n")
	.append("Maven:\n")
	.append("<dependency>\n")
	.append("  <groupId>org.simpleframework</groupId>\n")
	.append("  <artifactId>simple-xml</artifactId>\n")
	.append("  <version>2.7.1</version>\n")
	.append("</dependency>\n\n")
	.append("Scala SBT:\n")
	.append("libraryDependencies += \"org.simpleframework\" % \"simple-xml\" % \"2.7.1\"\n\n")
	.append("Gradle:\n")
	.append("compile 'org.simpleframework:simple-xml:2.7.1'\n\n")
	.append("...or grab the JAR from ")
	.append("http://simple.sourceforge.net/download.php \n\n").toString();
	
	private static final String ERROR_CONTEXT_INCOMPATIBLE = new StringBuilder()
	.append("\n\nFailed to initialize XmlSerializer; use of @Serialize(ContentType.XML) is disabled.\n")
	.append("Please make sure that you are using version 2.7.1 of Simple-XML.\n\n").toString();
	

	private static Class<?> Persister; 
	private static Method Persister_write;
	
	private static Object persister; //thread-safe, as specified at http://goo.gl/WSXO5z
	
	private static boolean unavailable;
	private static boolean incompatible;
	
	static {
		
		try {
			
			Persister = Class.forName("org.simpleframework.xml.core.Persister");
			Persister_write = Persister.getDeclaredMethod("write", Object.class, OutputStream.class);
			
			persister = Persister.newInstance(); 
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
	 * <p>Creates a new instance of {@link XmlSerializer} and registers the output {@link String} class as 
	 * the content-type which results from serialization.</p>
	 *
	 * @since 1.3.0
	 */
	public XmlSerializer() {
		
		super(String.class);
	}
	
	/**
     * <p>Serializes the given model using <b>Simple-XML</b> and returns the resulting XML {@link String}.</p>
     * 
     * <p>See {@link AbstractSerializer#serialize(InvocationContext, Object)}.</p>
     * 
	 * @param input
	 * 			the input model to be serialized into an XML {@link String}
	 * <br><br>
	 * @param context
	 * 			the {@link InvocationContext} which is used to discover further information regarding 
	 * 			the proxy invocation
     * <br><br>
	 * @return the XML {@link String} which represents the provided input model, else {@code null} if the 
	 * 		   provided input was {@code null}
	 * <br><br>
	 * @throws IllegalStateException 
	 * 			if the <b>Simple-XML library</b> was not found on the classpath or if an incompatible version 
	 * 			of the library is being used
	 * <br><br>
	 * @throws SerializerException
	 * 			if JSON serialization failed for the given entity using the Simple-XML library 
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	protected String serialize(InvocationContext context, Object input) {
		
		if(unavailable || incompatible) {
			
			throw new IllegalStateException(unavailable? ERROR_CONTEXT_UNAVAILABLE :ERROR_CONTEXT_INCOMPATIBLE);
		}
		
		if(input == null) {
			
			return null;
		}
		
		try {
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Persister_write.invoke(persister, input, baos);
			
			return baos.toString();
		} 
		catch (Exception e) {
			
			throw new SerializerException(new StringBuilder("XML serialization failed for request <")
			.append(context.getRequest().getName())
			.append("> on endpoint <")
			.append(context.getEndpoint().getName())
			.append(">").toString(), e);
		}
	}
}
