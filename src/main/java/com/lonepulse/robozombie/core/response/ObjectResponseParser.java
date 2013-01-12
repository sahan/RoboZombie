package com.lonepulse.robozombie.core.response;

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

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.lonepulse.robozombie.core.processor.AbstractResponseParser;

/**
 * <p>This is an extension of {@link AbstractResponseParser} which allows the parsing 
 * of object data. 
 * 
 * @version 1.1.4
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class ObjectResponseParser extends AbstractResponseParser<Object> {

	/**
	 * <p> Parses the content in the {@link HttpResponse} to any type which is assignable 
	 * to a generic {@link Object} that has implemented {@link Serializable}.
	 * 
	 * @see AbstractResponseParser#parse(HttpResponse, com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration)
	 */
	@Override
	public Object processResponse(HttpResponse httpResponse) throws Exception {

		byte[] responseBytes = EntityUtils.toByteArray(httpResponse.getEntity());
			
		ByteArrayInputStream bais = new ByteArrayInputStream(responseBytes);
		ObjectInput oin = new ObjectInputStream(bais); //A StreamCorruptedException is common if the magic number is not found
		
		try {
			
			return oin.readObject();
		}
		finally {
				
			if(bais != null) bais.close();
			if(oin != null) oin.close();
		}
	}

	/**
	 * @see com.lonepulse.robozombie.core.processor.AbstractResponseParser#getType()
	 */
	@Override
	public Class<Object> getType() {

		return Object.class;
	}
}
