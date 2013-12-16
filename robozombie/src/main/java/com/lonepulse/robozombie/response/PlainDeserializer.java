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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>This {@link AbstractDeserializer} extracts the response data as a <b>raw String</b>. 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class PlainDeserializer extends AbstractDeserializer<CharSequence> {

	
	/**
	 * <p>Creates a new {@link PlainDeserializer} and register the type {@link CharSequence} as the entity 
	 * which results from its deserialization operation.</p>
	 *
	 * @since 1.3.0
	 */
	public PlainDeserializer() {
	
		super(CharSequence.class);
	}
	
	/**
	 * <p>Deserializes the response content to a type which is assignable to {@link CharSequence}.</p>
	 * 
	 * @see AbstractDeserializer#run(InvocationContext, HttpResponse)
	 */
	@Override
	public CharSequence deserialize(InvocationContext context, HttpResponse response) {

		try {
			
			HttpEntity entity = response.getEntity();
			return entity == null? "" :EntityUtils.toString(entity);
		} 
		catch(Exception e) {
			
			throw new DeserializerException(new StringBuilder("Plain deserialization failed for request <")
			.append(context.getRequest().getName())
			.append("> on endpoint <")
			.append(context.getEndpoint().getName())
			.append(">").toString(), e);
		}
	}
}
