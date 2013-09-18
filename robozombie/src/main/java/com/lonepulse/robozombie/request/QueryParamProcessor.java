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
import org.apache.http.client.utils.URIBuilder;

import com.lonepulse.robozombie.annotation.QueryParam;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.processor.ProxyInvocationConfiguration;

/**
 * <p>This is a concrete implementation of {@link RequestProcessor} which discovers <i>query parameters</i> 
 * in a request declaration by searching for any arguments which are annotated with @{@link QueryParam} and 
 * constructs a <a href="http://en.wikipedia.org/wiki/Query_string">query string</a> which will be appended 
 * to the request URL.</p> 
 * 
 * <p>The @{@link QueryParam} annotation should be used on an implementation of {@link CharSequence} which 
 * provides the <i>value</i> for each <i>name-value</i> pair; and the supplied {@link QueryParam#value()} 
 * provides the <i>name</i>.</p>
 * 
 * <p>Processor Dependencies:</p>
 * <ul>
 * 	<li>{@link PathParamProcessor}</li>
 * </ul>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class QueryParamProcessor extends AbstractRequestProcessor {

	
	/**
	 * <p>Accepts the {@link ProxyInvocationConfiguration} along with an {@link HttpRequestBase} and creates 
	 * a <a href="http://en.wikipedia.org/wiki/Query_string">query string</a> using any arguments which were 
	 * annotated with @{@link QueryParam} and @{@link Request.Param} which is subsequently appended to the URI.</p>
	 * 
	 * <p><b>Note</b> that any constant request parameters which are annotated with @{@link Request.Param} 
	 * will be treated as <b>name-value</b> pairs to be used in the query string.</p>
	 * 
	 * <p>See {@link ParamPopulator#populate(ProxyInvocationConfiguration)}.</p>
	 * 
	 * @param httpRequestBase
	 * 			prefers an instance of {@link HttpGet} so as to conform with HTTP 1.1; however, other request 
	 * 			types will be entertained to allow compliance with unusual endpoint definitions 
	 * <br><br>
	 * @param config
	 * 			an immutable instance of {@link ProxyInvocationConfiguration} which is used to form the query 
	 * 			string and append it to the request URL
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if the creation of a query string failed due to an unrecoverable error
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	protected void process(HttpRequestBase httpRequestBase, ProxyInvocationConfiguration config) throws RequestProcessorException {

		try {
			
			URIBuilder uriBuilder = new URIBuilder(httpRequestBase.getURI());
			
			List<Request.Param> constantQueryParams = RequestUtils.findConstantRequestParams(config);
			
			for (Request.Param param : constantQueryParams) {
				
				uriBuilder.setParameter(param.name(), param.value());
			}
			
			Map<String, Object> queryParams = RequestUtils.findQueryParams(config);
			
			for (Entry<String, Object> entry : queryParams.entrySet()) {
				
				String name = entry.getKey();
				Object value = entry.getValue();
				
				if(!(value instanceof CharSequence)) {
				
					StringBuilder errorContext = new StringBuilder()
					.append("Query parameters can only be of type ")
					.append(CharSequence.class.getName())
					.append(". Please consider implementing CharSequence ")
					.append("and providing a meaningful toString() representation for the ")
					.append("<name> of the query parameter. ");
					
					throw new RequestProcessorException(new IllegalArgumentException(errorContext.toString()));
				}
			
				uriBuilder.setParameter(name, ((CharSequence)value).toString());
			}
			
			httpRequestBase.setURI(uriBuilder.build());
		}
		catch(Exception e) {
			
			throw (e instanceof RequestProcessorException)? 
					(RequestProcessorException)e :new RequestProcessorException(getClass(), config, e);
		}
	}
}
