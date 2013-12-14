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

import static com.lonepulse.robozombie.util.Is.async;
import static com.lonepulse.robozombie.util.Is.detached;
import static com.lonepulse.robozombie.util.Is.status;
import static com.lonepulse.robozombie.util.Is.successful;

import java.lang.reflect.Method;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http42.util.EntityUtils;

import com.lonepulse.robozombie.annotation.Deserialize;
import com.lonepulse.robozombie.annotation.Entity.ContentType;
import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>This {@link AbstractResponseProcessor} retrieves the {@link HttpEntity} of an {@link HttpResponse} 
 * and deserializes its content using the defined deserializer. Deserializers are defined using 
 * @{@link Deserialize} either at the endpoint level or at the request level. All endpoint request 
 * declarations which define a return type should be associated with a deserializer. Custom deserializers 
 * may be used by extending {@link AbstractDeserializer} and defining its type at {@link Deserialize#type()}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class EntityProcessor extends AbstractResponseProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} along with the {@link HttpResponse} and retrieves the 
	 * {@link HttpEntity} form the response. This is then converted to an instance of the required request 
	 * return type by consulting the @{@link Deserialize} metadata on the endpoint definition.</p>
	 * 
	 * <p>If the desired return type is {@link HttpResponse} or {@link HttpEntity} the response or entity 
	 * is simply returned without any further processing.</p>
	 * 
	 * <p><b>Note</b> that this processor returns {@code null} for successful responses with the status 
	 * codes {@code 205} or {@code 205}.</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which is used to discover deserializer metadata 
	 * <br><br>
	 * @param response
	 * 			the {@link HttpResponse} whose response content is deserialized to the desired output type
	 * <br><br>
	 * @return the deserialized response content which conforms to the expected type
	 * <br><br> 
	 * @throws ResponseProcessorException
	 * 			if deserializer instantiation or execution failed for the response entity
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	protected Object process(InvocationContext context, HttpResponse response, Object content) {

		if(response.getEntity() == null) {
			
			return content;
		}
		
		HttpEntity entity = response.getEntity();
		
		Method request = context.getRequest();
		Class<?> responseType = request.getReturnType();
		
		try {
			
			if(successful(response) && !status(response, 204, 205)) { //omit successful status codes without response content 
				
				if(HttpResponse.class.isAssignableFrom(responseType)) {
					
					return response;
				}
				
				if(HttpEntity.class.isAssignableFrom(responseType)) {
					
					return response.getEntity();
				}
			
				boolean responseExpected = !(responseType.equals(void.class) || responseType.equals(Void.class));
				boolean handleAsync = async(context);
				
				if(handleAsync || responseExpected) {
					
					Class<?> endpoint = context.getEndpoint();
					AbstractDeserializer<?> deserializer = null;
			
					Deserialize metadata = (metadata = 
						request.getAnnotation(Deserialize.class)) == null? 
							endpoint.getAnnotation(Deserialize.class) :metadata;
					
					if(metadata != null & !detached(context, Deserialize.class)) {
						
						deserializer = (metadata.value() == ContentType.UNDEFINED)? 
							Deserializers.resolve(metadata.type()) :Deserializers.resolve(metadata.value()); 
					}
					else if(handleAsync || CharSequence.class.isAssignableFrom(responseType)) {
						
						deserializer = Deserializers.resolve(ContentType.PLAIN);     
					}
					else {
						
						throw new DeserializerUndefinedException(endpoint, request);
					}
					
					return deserializer.run(context, response);
				}
			}
		}
		catch(Exception e) {
			
			throw (e instanceof ResponseProcessorException)? 
					(ResponseProcessorException)e :new ResponseProcessorException(getClass(), context, e);
		}
		finally {
			
			if(!(HttpResponse.class.isAssignableFrom(responseType) ||
				 HttpEntity.class.isAssignableFrom(responseType))) {
			
				EntityUtils.consumeQuietly(entity);
			}
		}
		
		return content;
	}
}
