package com.lonepulse.robozombie.request;

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

import java.util.Map;

import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.annotation.Header;
import com.lonepulse.robozombie.annotation.Headers;
import com.lonepulse.robozombie.proxy.InvocationContext;
import com.lonepulse.robozombie.util.Metadata;

/**
 * <p>This {@link AbstractRequestProcessor} populates the <i>request-headers</i> of an {@link HttpRequestBase} 
 * for an endpoint request definition. It targets both <b>static headers</b> identified by @{@link Headers} 
 * and <b>dynamic headers</b> identified by @{@link Header} (used on the request method arguments).</p>
 * 
 * <p><b>Note</b> that all headers will be <b>added</b> and <b>not overwritten</b>. This allows multiple 
 * headers with the same name (having the same or different values) to be used in the same HTTP request.</p> 
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
final class HeaderProcessor extends AbstractRequestProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} along with an {@link HttpRequestBase} and populates the 
	 * HTTP headers which are discovered in the request definition. These might be <i>static headers</i> 
	 * or <i>dynamic headers</i>. <i>Static-headers</i> are specified directly on a request method with 
	 * @{@link Headers}, whereas <i>dynamic-headers</i> are specified on the method argument which provides 
	 * the dynamic header using @{@link Header}.</p>
	 * 
	 * <p>All discovered headers are <b>added</b> to the request - if two headers exists with the same 
	 * name (with the same or different values) both are included in the final {@link HttpRequestBase}.</p>
	 * 
	 * <p><b>Note</b> that header arguments which are {@code null} (or are <i>empty strings</i>) are 
	 * ignored. It is assumed that these headers are ignored for the current request invocation.</p>
	 * 
	 * <p>See {@link RequestUtils#findStaticHeaders(InvocationContext)}.</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which is used to discover any @{@link Headers} and 
	 * 			@{@link Header} metadata in its <i>request</i> and <i>args</i> 
	 * <br><br>
	 * @param request
	 * 			the {@link HttpRequestBase} whose headers are to be populated by reading the metadata 
	 * 			available in @{@link Headers} and @{@link Header} annotations 
	 * <br><br>
 	 * @return the same instance of {@link HttpRequestBase} which was given for processing headers
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if the request-header discovery or population failed due to an unrecoverable error
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	protected HttpRequestBase process(InvocationContext context, HttpRequestBase request) {

		try {
			
			for (Map.Entry<String, Object> header : RequestUtils.findStaticHeaders(context)) {
				
				addHeader(request, header.getKey(), header.getValue());
			}
			
			for (Map.Entry<Header, Object> header : Metadata.onParams(Header.class, context)) {
				
				addHeader(request, header.getKey().value(), header.getValue());
			}
			
			return request;
		}
		catch(Exception e) {
			
			throw new RequestProcessorException(context, getClass(), e);
		}
	}
	
	private void addHeader(HttpRequestBase request, String name, Object value) {
		
		if(value != null && !value.equals("")) {
			
			if(!(value instanceof CharSequence)) {
				
				StringBuilder errorContext = new StringBuilder()
				.append("Header values can only be of type ")
				.append(CharSequence.class.getName())
				.append(". Please consider using an implementation of CharSequence for the header <")
				.append(name)
				.append("> and providing a meaningful toString() implementation for the header-value. ")
				.append("Furthermore, response headers should be of the specialized type ")
				.append(StringBuilder.class.getName());
				
				throw new IllegalArgumentException(errorContext.toString());
			}
			
			request.addHeader(name, value.toString());
		}
	}
}
