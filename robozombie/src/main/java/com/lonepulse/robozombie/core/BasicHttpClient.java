package com.lonepulse.robozombie.core;

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

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

/**
 * <p>A primitive implementation of {@link HttpClientContract} which provides the 
 * bare essentials of network interfacing.</p>
 * 
 * @version 1.1.3
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class BasicHttpClient implements HttpClientContract {

	
	/**
	 * <p>The implementation of {@link HttpClient} which is used to execute 
	 * the requests.</p>
	 */
	private HttpClient httpClient;
	
	
	/**
	 * <p>Default constructor overridden to provide an implementation of 
	 * {@link HttpClient}.</p> 
	 * <br><br>
	 * @since 1.1.2
	 */
	public BasicHttpClient() {
		
		this(new DefaultHttpClient());
	}
	
	/**
	 * <p>Parameterized constructor takes an implementation of {@link HttpClient}.</p> 
	 * 
	 * @param httpClient 	
	 * 				the {@link HttpClient} which is to be used
	 * <br><br>
	 * @since 1.1.1
	 */
	public BasicHttpClient(HttpClient httpClient) {

		this.httpClient = httpClient;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse executeRequest(HttpRequestBase httpRequestBase) 
	throws ClientProtocolException, IOException {

		return httpClient.execute(httpRequestBase);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends HttpRequestBase> HttpResponse executeRequest(T httpRequestBase, HttpContext httpContext)
	throws ClientProtocolException, IOException {

		return httpClient.execute(httpRequestBase, httpContext);
	}
	
	/**
	 * <p>Retrieves the instance of {@link HttpClient} which is being used.</p>
	 * 
	 * @return the {@link HttpClientContract}
	 * <br><br>
	 * @since 1.1.1
	 */
	protected HttpClient getHttpClient() {
		
		return httpClient;
	}
}
