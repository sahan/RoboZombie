package com.lonepulse.robozombie.core.request.header;

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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.core.annotation.Header;
import com.lonepulse.robozombie.core.annotation.HeaderSet;
import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.util.AnnotationExtractor;

/**
 * <p>A concrete implementation of {@link HeaderBuilder} which creates the 
 * headers in an HTTP requests based on the provided parameters. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class BasicHeaderBuilder implements HeaderBuilder {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpRequestBase build(HttpRequestBase httpRequestBase, ProxyInvocationConfiguration config) 
	throws Exception {

		try {
		
			HeaderSet headerSet = config.getRequest().getAnnotation(HeaderSet.class);
			
			List<HeaderSet.Header> staticHeaderParams = (headerSet == null)? new ArrayList<HeaderSet.Header>()
					:Arrays.asList(headerSet.value());
			
			Map<StringBuilder, Header> variableHeaderParams = AnnotationExtractor.extractHeaders(config.getRequest(), config.getRequestArgs());
			
			for (HeaderSet.Header param : staticHeaderParams)
				httpRequestBase.setHeader(param.name(), param.value());
			
			Set<Map.Entry<StringBuilder, Header>> variableEntries = variableHeaderParams.entrySet();
			
			for (Map.Entry<StringBuilder, Header> entry : variableEntries) 
				httpRequestBase.addHeader(entry.getValue().value(), entry.getKey().toString());
			
			return httpRequestBase;
		}
		catch (Exception e) {
			
			throw new HeaderException(config.getRequest(), config.getEndpointClass(), e);
		}
		
	}
}
