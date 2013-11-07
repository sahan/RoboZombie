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

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This is an extension of {@link AbstractDeserializer} which extracts the response 
 * data as a <b>raw String</b>. 
 * 
 * @version 1.1.4
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class RawResponseParser extends AbstractDeserializer<CharSequence> {

	
	/**
	 * <p>Creates a new instance of {@link RawResponseParser} and register the type 
	 * {@link CharSequence} as the entity which results from its <i>parse</i> operation.
	 *
	 * @since 1.2.4
	 */
	public RawResponseParser() {
	
		super(CharSequence.class);
	}
	
	/**
	 * <p> Parses the content in the {@link HttpResponse} to any type which is 
	 * assignable to a {@link CharSequence}.
	 * 
	 * @see AbstractDeserializer#run(HttpResponse, com.lonepulse.robozombie.inject.InvocationContext.processor.ProxyInvocationConfiguration)
	 */
	@Override
	public CharSequence deserialize(HttpResponse httpResponse, InvocationContext config) throws Exception {

		String responseString = EntityUtils.toString(httpResponse.getEntity());
		return responseString.subSequence(0, responseString.length());
	}
}
