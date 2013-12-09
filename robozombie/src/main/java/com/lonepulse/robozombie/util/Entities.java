package com.lonepulse.robozombie.util;

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

import static com.lonepulse.robozombie.util.Assert.assertNotNull;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.http.HttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;

/**
 * <p>A collection of utility services for translating and managing {@link HttpEntity} instances.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class Entities {

	
	 private Entities() {}
	 
	 
	 /**
	  * <p>Discovers which implementation of {@link HttpEntity} is suitable for wrapping the given object. 
	  * This discovery proceeds in the following order by checking the runtime-type of the object:</p> 
	  *
	  * <ol>
	  * 	<li>org.apache.http.{@link HttpEntity} --&gt; returned as-is.</li> 
	  * 	<li>{@code byte[]}, {@link Byte}[] --&gt; {@link ByteArrayEntity}</li> 
	  *  	<li>java.io.{@link File} --&gt; {@link FileEntity}</li>
	  * 	<li>java.io.{@link InputStream} --&gt; {@link BufferedHttpEntity}</li>
	  * 	<li>{@link CharSequence} --&gt; {@link StringEntity}</li>
	  * 	<li>java.io.{@link Serializable} --&gt; {@link SerializableEntity} (with an internal buffer)</li>
	  * </ol>
	  *
	  * @param genericEntity
	  * 			a generic reference to an object whose concrete {@link HttpEntity} is to be resolved 
	  * <br><br>
	  * @return the resolved concrete {@link HttpEntity} implementation
	  * <br><br>
	  * @throws NullPointerException
	  * 			if the supplied generic type was {@code null}
	  * <br><br>
	  * @throws EntityResolutionFailedException
	  * 			if the given generic instance failed to be translated to an {@link HttpEntity} 
	  * <br><br>
	  * @since 1.2.4
	  */
	 public static final HttpEntity resolve(Object genericEntity) {
		
		 assertNotNull(genericEntity);
		
		 try {
		
			 if(genericEntity instanceof HttpEntity) {
				
				 return (HttpEntity)genericEntity;
			 }
			 else if(byte[].class.isAssignableFrom(genericEntity.getClass())) {
				
				 return new ByteArrayEntity((byte[])genericEntity);
			 }
			 else if(Byte[].class.isAssignableFrom(genericEntity.getClass())) {
				
				 Byte[] wrapperBytes = (Byte[])genericEntity;
				 byte[] primitiveBytes = new byte[wrapperBytes.length];
				
				 for (int i = 0; i < wrapperBytes.length; i++) {
					
					 primitiveBytes[i] = wrapperBytes[i].byteValue();
				 }
				
				 return new ByteArrayEntity(primitiveBytes);
			 }
			 else if(genericEntity instanceof File) {
				
				 return new FileEntity((File)genericEntity, null);
			 }
			 else if(genericEntity instanceof InputStream) {
				
				 BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
				 basicHttpEntity.setContent((InputStream)genericEntity);
				
				 return new BufferedHttpEntity(basicHttpEntity);
			 }
			 else if(genericEntity instanceof CharSequence) {
				
				 return new StringEntity(((CharSequence)genericEntity).toString());
			 }
			 else if(genericEntity instanceof Serializable) {
				
				 return new SerializableEntity((Serializable)genericEntity, true);
			 }
			 else {
				
				 throw new EntityResolutionFailedException(genericEntity);
			 }
		 }
		 catch(Exception e) {

			 throw (e instanceof EntityResolutionFailedException)?
					 (EntityResolutionFailedException)e :new EntityResolutionFailedException(genericEntity, e);
		 }
	 }
	 
	 /**
	  * <p>Discovers the {@link HttpEntity} which is suitable for wrapping an instance of the given {@link Class}. 
	  * This discovery proceeds in the following order by checking the provided generic type:</p> 
	  *
	  * <ol>
	  * 	<li>org.apache.http.{@link HttpEntity} --&gt; returned as-is.</li> 
	  * 	<li>{@code byte[]}, {@link Byte}[] --&gt; {@link ByteArrayEntity}</li> 
	  *  	<li>java.io.{@link File} --&gt; {@link FileEntity}</li>
	  * 	<li>java.io.{@link InputStream} --&gt; {@link BufferedHttpEntity}</li>
	  * 	<li>{@link CharSequence} --&gt; {@link StringEntity}</li>
	  * 	<li>java.io.{@link Serializable} --&gt; {@link SerializableEntity} (with an internal buffer)</li>
	  * </ol>
	  *
	  * @param genericType
	  * 			a generic {@link Class} to be translated to an {@link HttpEntity} type 
	  * <br><br>
	  * @return the {@link Class} of the translated {@link HttpEntity}
	  * <br><br>
	  * @throws NullPointerException
	  * 			if the supplied generic type was {@code null}
	  * <br><br>
	  * @throws EntityResolutionFailedException
	  * 			if the given generic {@link Class} failed to be translated to an {@link HttpEntity} type 
	  * <br><br>
	  * @since 1.2.4
	  */
	 public static final Class<?> resolve(Class<?> genericType) {
		 
		 assertNotNull(genericType);
		 
		 try {
			 
			 Class<?> entityType = HttpEntity.class.isAssignableFrom(genericType)? HttpEntity.class :
				 				   (byte[].class.isAssignableFrom(genericType) 
							 	   || Byte[].class.isAssignableFrom(genericType))? ByteArrayEntity.class:
						 		   File.class.isAssignableFrom(genericType)? FileEntity.class :
						 		   InputStream.class.isAssignableFrom(genericType)? BufferedHttpEntity.class :
						 		   CharSequence.class.isAssignableFrom(genericType)? StringEntity.class :
						 		   Serializable.class.isAssignableFrom(genericType)? SerializableEntity.class: null;
			 
			 if(entityType == null) {
				 
				 throw new EntityResolutionFailedException(genericType);
			 }
			 
			 return entityType;
		 }
		 catch(Exception e) {
			 
			 throw (e instanceof EntityResolutionFailedException)? 
					 (EntityResolutionFailedException)e :new EntityResolutionFailedException(genericType, e);
		 }
	 }
}
