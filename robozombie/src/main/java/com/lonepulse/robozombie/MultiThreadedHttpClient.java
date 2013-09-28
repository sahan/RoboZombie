package com.lonepulse.robozombie;

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
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

/**
 * <p>A concrete implementation of {@link HttpClient} which provides network 
 * interfacing over a thread-safe, <b>asynchronous</b> HTTP client.</p>
 * 
 * @version 2.2.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum MultiThreadedHttpClient implements HttpClientContract {

	
	/**
	 * <p>Offers an {@link HttpClient} instantiated with a thread-safe 
	 * {@link PoolingClientConnectionManager}.
	 * 
	 * @since 2.1.0
	 */
	INSTANCE;
	
	
	/**
	 * <p>The multi-threaded implementation of {@link HttpClient} which is used to 
	 * execute requests in parallel.</p>
	 * <br><br>
	 * @since 1.1.1
	 */
	private final HttpClient httpClient;
	
	
	/**
	 * <p>Default constructor overridden to provide an implementation of 
	 * {@link HttpClient} which can handle <i>multi-threaded request execution</i>.</p> 
	 * 
	 * <p>This implementation uses a {@link PoolingClientConnectionManager} with a 
	 * {@link SchemeRegistry} which has default port registrations of <b>HTTP</b> 
	 * and <b>HTTPS</b>.</p> 
	 * <br><br>
	 * @since 1.1.1
	 */
	private MultiThreadedHttpClient() {
		
		HttpParams params = new BasicHttpParams();
		HttpClientParams.setRedirecting(params, true);
		HttpConnectionParams.setSoTimeout(params, 30 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 12000);
        HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
        HttpProtocolParams.setUserAgent(params, System.getProperty("http.agent"));
        
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);

        httpClient = new DefaultHttpClient(manager, params);
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() { //HttpClient considered to be "out of scope" only on VM exit
				
				httpClient.getConnectionManager().shutdown();
			}
		}));
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public <T extends HttpRequestBase> HttpResponse executeRequest(T httpRequestBase) 
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
}
