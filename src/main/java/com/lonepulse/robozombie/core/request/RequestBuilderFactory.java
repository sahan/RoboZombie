package com.lonepulse.robozombie.core.request;

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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.lonepulse.robozombie.core.annotation.Request;
import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.rest.annotation.Rest;
import com.lonepulse.robozombie.rest.request.RestfulRequestBuilder;

/**
 * <p>This request factory is reponsible for creating instances of {@link AbstractRequestBuilder}s.
 * 
 * @version 1.1.2 - use of {@link ProxyInvocationConfiguration}
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class RequestBuilderFactory implements EndpointComponentFactory<AbstractRequestBuilder> {

		
	/**
	 * <p>Access qualifier set to <i>private</i> to prevent external instantiation.
	 * <br><br>
	 * @since 1.1.1
	 */
	private RequestBuilderFactory() {
	}
	
	/**
	 * <p>Configures a new instance of the {@link RequestBuilderFactory} and returns it.
	 * 
	 * @return the new instance of {@link RequestBuilderFactory}
	 * <br><br>
	 * @since 1.1.2
	 */
	public static RequestBuilderFactory newInstance() {
		
		return new RequestBuilderFactory();
	}
	
	/**
	 * <p>Takes the request {@link Annotation} and returns an instance of the {@link AbstractRequestBuilder} 
	 * associated with it.
	 * 
	 * @param proxyInvocationConfiguration 
	 * 			Used to determine the appropriate {@link AbstractRequestBuilder} which is to be created.
	 * 
	 * @return
	 * 			The instance of {@link AbstractRequestBuilder} associated with the request type
	 * <br><br>
	 * @since 1.1.2
	 * 
	 * @see EndpointComponentFactory#create(ProxyInvocationConfiguration)
	 */
	@Override
	public AbstractRequestBuilder create(ProxyInvocationConfiguration proxyInvocationConfiguration) {
		
		Method request = proxyInvocationConfiguration.getRequest();
		
		return
				(request.isAnnotationPresent(Request.class))? new BasicRequestBuilder():
				(request.isAnnotationPresent(Rest.class))? new RestfulRequestBuilder(): null;
	}
}
