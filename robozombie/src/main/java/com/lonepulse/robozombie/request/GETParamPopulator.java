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


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import android.net.Uri;

import com.lonepulse.robozombie.annotation.Param;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.annotation.Rest;
import com.lonepulse.robozombie.core.request.ParamPopulator;
import com.lonepulse.robozombie.core.request.ParamPopulatorException;
import com.lonepulse.robozombie.core.request.RequestMethod;
import com.lonepulse.robozombie.core.request.RequestUtils;
import com.lonepulse.robozombie.processor.ProxyInvocationConfiguration;

/**
 * <p>This is a concrete implementation of {@link ParamPopulator} which serves {@link Request}s and 
 * {@link Rest}ful requests which uses {@link RequestMethod#HTTP_GET}.</p>
 * 
 * <p>It acts on @{@link Param} and @{@link Request.Param} annotations on an endpoint interface method 
 * and constructs a <a href="http://en.wikipedia.org/wiki/Query_string">query string</a> that's appended 
 * to the request URI.</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class GETParamPopulator implements ParamPopulator {

	
	/**
	 * <p>Accepts the {@link ProxyInvocationConfiguration} of a request which uses {@link RequestMethod#HTTP_GET} 
	 * and creates a <a href="http://en.wikipedia.org/wiki/Query_string">query string</a> using any arguments 
	 * which were annotated with @{@link Param} and @{@link Request.Param}. This is then appended to the URI and 
	 * a new instance of {@link HttpGet} is constructed, which is subsequently returned.</p>
	 * 
	 * <p>See {@link ParamPopulator#populate(ProxyInvocationConfiguration)}.</p>
	 * 
	 * @param config
	 * 			an immutable instance of {@link ProxyInvocationConfiguration} which is used to form the query 
	 * 			string and create the {@link HttpRequestBase}
	 * 
	 * @return an instance of {@link HttpGet} having a URI with an appended query string (if any request 
	 * 		   parameters were specified)
	 * 
	 * @throws ParamPopulatorException
	 * 			if an {@link HttpGet} instance failed to be created or if a query parameter failed to be inserted
	 * 
	 * @since 1.2.4
	 */
	@Override
	public HttpRequestBase populate(ProxyInvocationConfiguration config) throws ParamPopulatorException {

		try {
		
			Uri.Builder uriBuilder = Uri.parse(config.getUri().toASCIIString()).buildUpon();
			
			List<Request.Param> constantQueryParams = RequestUtils.findConstantRequestParams(config);
			Map<String, Object> queryParams = RequestUtils.findRequestParams(config);
			
			for (Request.Param param : constantQueryParams) {
				
				uriBuilder.appendQueryParameter(param.name(), param.value());
			}
			
			for (Entry<String, Object> entry : queryParams.entrySet()) {
				
				String name = entry.getKey();
				Object value = entry.getValue();
				
				if(!(value instanceof CharSequence)) {
				
					StringBuilder errorContext = new StringBuilder()
					.append("Parameters for GET requests can only be of type ")
					.append(CharSequence.class.getName())
					.append(". Please consider implementing CharSequence ")
					.append("and providing a meaningful toString() representation. ");
					
					throw new ParamPopulatorException(new IllegalArgumentException(errorContext.toString()));
				}
			
				uriBuilder.appendQueryParameter(name, ((CharSequence)value).toString());
			}
			
			return new HttpGet(uriBuilder.build().toString());
		}
		catch(Exception e) {
			
			throw (e instanceof ParamPopulatorException)? 
					(ParamPopulatorException)e :new ParamPopulatorException(e);
		}
	}
}
