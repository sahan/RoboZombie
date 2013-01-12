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

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.core.annotation.Header;
import com.lonepulse.robozombie.core.annotation.HeaderSet;
import com.lonepulse.robozombie.core.processor.AnnotationExtractor;
import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;

/**
 * <p>This is abstract implementation declares the policy for a factory which creates an 
 * {@link HttpRequestBase} using the information in an instance of {@link ProxyInvocationConfiguration}.</p> 
 * 
 * @version 1.1.4
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public abstract class AbstractRequestBuilder {


	/**
	 * <p>Creates an {@link HttpRequestBase} and configures it using the information in 
	 * the given {@link ProxyInvocationConfiguration} instance.</p> 
	 * 
	 * @param config
	 * 			the instance of {@link ProxyInvocationConfiguration} which supplies the parameters
	 * <br><br>
	 * @return the {@link HttpRequestBase} with the populated parameters
	 * <br><br>
	 * @throws {@link RequestBuilderException}
	 * 				thrown if the {@link HttpRequestBase} cannot be built	
	 * <br><br>
	 * @since 1.1.2
	 */
	public final HttpRequestBase build(ProxyInvocationConfiguration config) {
		
		try {
		
			URI uri = buildPath(config);
			HttpRequestBase httpRequestBase = null;
			
			httpRequestBase = buildRequestWithParameters(uri, config); 
			httpRequestBase = buildHeader(httpRequestBase, config);
			
			return httpRequestBase;
		}
		catch (Exception e) {
			
			throw new RequestBuilderException(this.getClass(), config, e); 
		}
	}
	
	/**
	 * <p>Populates the created {@link HttpRequestBase} with any headers.</p> 
	 * 
	 * @param httpRequestBase
	 * 			the {@link HttpRequestBase} which is created by this instance of {@link AbstractRequestBuilder}
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} which supplies the parameters
	 * <br><br>
	 * @return the {@link HttpRequestBase} with the headers populated
	 * <br><br>
	 * @throws Exception
	 * 			a generic exception is thrown in case operation failed
	 * <br><br>
	 * @since 1.1.3
	 */
	protected HttpRequestBase buildHeader(HttpRequestBase httpRequestBase, ProxyInvocationConfiguration config)
	throws Exception {
		
		HeaderSet headerSet = config.getRequest().getAnnotation(HeaderSet.class);
		
		List<HeaderSet.Header> staticHeaderParams = (headerSet == null)? new ArrayList<HeaderSet.Header>()
				:Arrays.asList(headerSet.value());
		
		Map<StringBuilder, Header> variableHeaderParams = AnnotationExtractor.extractHeaders(config.getRequest(), config.getRequestArgs());
		
		for (HeaderSet.Header param : staticHeaderParams)
			httpRequestBase.setHeader(param.name(), param.value());
		
		Set<Map.Entry<StringBuilder, Header>> variableEntries = variableHeaderParams.entrySet();
		
		for (Map.Entry<StringBuilder, Header> entry : variableEntries) 
			httpRequestBase.addHeader(entry.getValue().value(), entry.getKey().toString()); //TODO addHeader because there may be "same header name, different value" scenarios
		
		return httpRequestBase;
	}
	
	/**
	 * <p>Takes an instance of a {@link ProxyInvocationConfiguration} and creates the 
	 * {@link URI} using the root path and any sub-paths.</p>
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} which supplies the parameters
	 * <br><br>
	 * @return the {@link URI} built using information from the {@link ProxyInvocationConfiguration}
	 * <br><br>
	 * @throws Exception
	 * 			a generic exception is thrown in case operation failed
	 * <br><br>
	 * @since 1.1.4
	 */
	protected abstract URI buildPath(ProxyInvocationConfiguration config) 
	throws Exception;
	
	/**
	 * <p>Takes the base URI from a {@link URI} and parameters from the 
	 * {@link ProxyInvocationConfiguration} to create a {@link HttpRequestBase} 
	 * of the designated HTTP method type.</p>
	 * 
	 * @param uri
	 * 			the {@link URI} which locates the target of the request
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} which supplies the parameters
	 * <br><br>
	 * @return the created {@link HttpRequestBase} with the request parameters populated
	 * <br><br>
	 * @throws Exception
	 * 			a generic exception is thrown in case operation failed
	 * <br><br>
	 * @since 1.1.4
	 */
	protected abstract HttpRequestBase buildRequestWithParameters(URI uri, ProxyInvocationConfiguration config) 
	throws Exception;
	
}
