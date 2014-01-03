package com.lonepulse.robozombie.response;

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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>An abstract implementation of {@link Deserializer} which directs <i>deserialization</i> for 
 * all {@link Deserializer}s. To create a custom {@link Deserializer} extend this class and override 
 * {@link #deserialize(InvocationContext, HttpResponse)}.</p> 
 * 
 * <p><b>Note</b> that all implementations are expected to be <b>stateless</b>. If a state is 
 * incurred, proper {@link ThreadLocal} management should be performed.</p>
 * <br>
 * <br>
 * @param <OUTPUT>
 * 			the type which is taken up by the deserialized content
 * <br>
 * <br>
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public abstract class AbstractDeserializer<OUTPUT> implements Deserializer<OUTPUT> {

	
	private Class<OUTPUT> outputType;
	
	
	/**
	 * <p>Initializes a new {@link AbstractDeserializer} with the given {@link Class} which 
	 * identifies the output type of this deserializer.</p>
	 * 
	 * @param outputType
	 * 			the {@link Class} type of the entity which is produced by this deserializer
	 * <br><br>
	 * @since 1.3.0
	 */
	public AbstractDeserializer(Class<OUTPUT> outputType) {
		
		this.outputType = outputType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final OUTPUT run(InvocationContext context, HttpResponse response) {
		
		Class<?> requestReturnType = context.getRequest().getReturnType();
		
		try {
			
			throwIfNotAssignable(requestReturnType);
			return deserialize(context, response);
		}
		catch(Exception e) {
		
			throw new DeserializerException(e);
		}
	}
	
	private void throwIfNotAssignable(Class<? extends Object> requestReturnType) {
		
		if(!void.class.isAssignableFrom(requestReturnType)
		   && !Void.class.isAssignableFrom(requestReturnType)
		   && !outputType.isAssignableFrom(requestReturnType)) {
			
			throw new DeserializerNotAssignableException(outputType, requestReturnType);
		}
	}
	
	/**
	 * <p>Takes an {@link HttpResponse} which resulted from a successful request execution and deserializes 
	 * its content to an instance of the output type.</p>
	 * 
	 * <p>The response {@link HttpEntity} can be obtained using {@link HttpResponse#getEntity()} and its 
	 * content can be <i>stringified</i> with {@link EntityUtils#toString(HttpEntity)}.</p>
	 * 
	 * <p><b>Note</b> that certain {@link HttpResponse}s may not contain an {@link HttpEntity} which can 
	 * be confirmed by performing a null check on {@link HttpResponse#getEntity()}.</p>
	 * 
	 * <p><b>Note</b> that any runtime errors which might occur during serialization are caught, wrapped 
	 * in an instance of {@link DeserializerException} (stack-trace preserved) and allowed to bubble up.</p>
	 * 
	 * @param response
	 * 				the {@link HttpResponse} of a successful request execution
	 * <br><br>
	 * @param context
	 * 				the {@link InvocationContext} which supplies all information regarding the request and 
	 * 				it's invocation
     * <br><br>
	 * @return the content which resulted from deserializing the response entity
	 * <br><br>
	 * @since 1.1.4
	 */
	protected abstract OUTPUT deserialize(InvocationContext context, HttpResponse response);
}
