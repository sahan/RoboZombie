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
import java.lang.reflect.Method;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

import com.lonepulse.robozombie.core.annotation.Parser;
import com.lonepulse.robozombie.core.processor.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.core.response.AsyncHandler;
import com.lonepulse.robozombie.core.response.ObjectResponseParser;
import com.lonepulse.robozombie.core.response.ResponseParser;
import com.lonepulse.robozombie.core.response.ResponseParserUndefinedException;
import com.lonepulse.robozombie.core.response.StringResponseParser;
import com.lonepulse.robozombie.rest.response.JsonResponseParser;

/**
 * <p>A concrete implementation of {@link HttpClient} which provides network 
 * interfacing over an <b>asynchronous</b> HTTP client.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class MultiThreadedHttpClient implements AsyncHttpClientContract {

	
	/**
	 * <p>The multi-threaded implementation of {@link HttpClient} which is used to 
	 * execute requests in parallel.</p>
	 * <br><br>
	 * @since 1.1.1
	 */
	private HttpClient httpClient; 
	
	
	/**
	 * <p>Default constructor overridden to provide an implementation of 
	 * {@link HttpClient} which can handle <i>multi-threaded request execution</i>.</p> 
	 * 
	 * <p>This implementation uses a {@link MultiThreadedHttpClient} with a 
	 * {@link SchemeRegistry} which has default port registrations of <b>HTTP</b> 
	 * and <b>HTTPS</b>.</p> 
	 * <br><br>
	 * @since 1.1.1
	 */
	public MultiThreadedHttpClient() {

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

		HttpParams httpParams = new BasicHttpParams();
		
		ThreadSafeClientConnManager tsccm 
			= new ThreadSafeClientConnManager(httpParams, schemeRegistry);
	
		this.httpClient = new DefaultHttpClient(tsccm, httpParams);
	}
	
	/**
	 * <p>Parameterized constructor takes an implementation of {@link HttpClient} 
	 * which can handle <i>multi-threaded request execution</i>.</p>
	 * 
	 * <p>See {@link #MultiThreadedHttpClient()} for default configuration.</p>
	 * 
	 * @param httpClient 	
	 * 				the {@link HttpClient} which is to be used
	 * <br><br>
	 * @since 1.1.1
	 */
	public MultiThreadedHttpClient(HttpClient httpClient) {

		this.httpClient = httpClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends HttpRequestBase> void executeAsyncRequest(final T httpRequestBase, final ProxyInvocationConfiguration config) { //TODO more detailed logs
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
		
				String errorContext = "Asynchronous request execution failed. ";

				HttpResponse httpResponse;
				
				try {
				
					httpResponse = httpClient.execute(httpRequestBase); //execute the request on the multi-threaded client
				} 
				catch (ClientProtocolException cpe) {

					Log.e(getClass().getSimpleName(), errorContext + "Protocol cannot be resolved.", cpe);
					return;
				} 
				catch (IOException ioe) {

					Log.e(getClass().getSimpleName(), errorContext + "IO failure.", ioe);
					return;
				}  
					
				AsyncHandler asyncHandler = null;
					
				for (Object object : config.getRequestArgs()) { //find the provided AsyncHandler (if any)
						
					if(object instanceof AsyncHandler)
						asyncHandler = (AsyncHandler) object;
				}
					
				if(asyncHandler != null) { //response handling has to commence
					
					Class<?> typeClass = config.getEndpointClass();
					Method method = config.getRequest();
					
					Parser parser = null;	
					Class<? extends ResponseParser<?>> parserType = null;
					
					if(method.isAnnotationPresent(Parser.class)) //check parser definition at method level first
						parser = method.getAnnotation(Parser.class);
					
					else if(typeClass.isAnnotationPresent(Parser.class))
						parser = method.getAnnotation(Parser.class);
						
					else
						throw new ResponseParserUndefinedException(typeClass, method);
					
					switch (parser.value()) {
					
						case STRING:
							parserType = StringResponseParser.class;
							break;
							
						case JSON:
							parserType = JsonResponseParser.class;
								
						case OBJECT:
							parserType = ObjectResponseParser.class;
							break;
								
						case UNDEFINED:
							parserType = parser.typeClass();
					}
			
					ResponseParser<? extends Object> responseParser = null;
					
					try {
									
						responseParser = parserType.newInstance();
					} 
					catch (InstantiationException ie) {

						Log.e(getClass().getSimpleName(), errorContext + "Response-parser instantiation failed.", ie);
						return;
					} 
					catch (IllegalAccessException iae) {

						Log.e(getClass().getSimpleName(), errorContext + "Response-parser invocation failed.", iae);
						return;
					}
						
					Object reponseEntity = responseParser.parse(httpResponse, config);
						
					if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
						asyncHandler.onSuccess(httpResponse, reponseEntity); 
						
					else 
						asyncHandler.onFailure(httpResponse, reponseEntity);
				}
			}
		}).start();
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public <T extends HttpRequestBase> HttpResponse executeRequest(T httpRequestBase) throws ClientProtocolException, IOException {

		return this.httpClient.execute(httpRequestBase);
	}
	
	/**
	 * <p>Retrieves the instance of {@link HttpClient} which is being used.
	 * 
	 * @return the {@link HttpClient}
	 * <br><br>
	 * @since 1.1.1
	 */
	protected HttpClient getHttpClient() {
		
		return httpClient;
	}
}
