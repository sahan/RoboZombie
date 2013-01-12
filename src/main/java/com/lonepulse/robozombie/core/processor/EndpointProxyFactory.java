package com.lonepulse.robozombie.core.processor;

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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.core.AsyncHttpClientContract;
import com.lonepulse.robozombie.core.BasicHttpClient;
import com.lonepulse.robozombie.core.HttpClientContract;
import com.lonepulse.robozombie.core.MultiThreadedHttpClient;
import com.lonepulse.robozombie.core.RoboZombieIllegalAccessException;
import com.lonepulse.robozombie.core.RoboZombieInstantiationException;
import com.lonepulse.robozombie.core.annotation.Asynchronous;
import com.lonepulse.robozombie.core.annotation.Endpoint;
import com.lonepulse.robozombie.core.annotation.Parser;
import com.lonepulse.robozombie.core.request.AbstractRequestBuilder;
import com.lonepulse.robozombie.core.request.EndpointComponentFactory;
import com.lonepulse.robozombie.core.request.RequestBuilderFactory;
import com.lonepulse.robozombie.core.response.AsyncHandler;
import com.lonepulse.robozombie.core.response.ObjectResponseParser;
import com.lonepulse.robozombie.core.response.ResponseParser;
import com.lonepulse.robozombie.core.response.ResponseParserUndefinedException;
import com.lonepulse.robozombie.core.response.StringResponseParser;
import com.lonepulse.robozombie.rest.response.JsonResponseParser;

/**
 * <p>This factory is used for creating dynamic proxies for communication 
 * endpoint interfaces that are annotated with {@link Endpoint}.</p>
 * 
 * @version 1.2.1 - Asynchronous Execution was handled 
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class EndpointProxyFactory implements ProxyFactory {


	/**
	 * <p>The instance of {@link EndpointValidator} which is used to validate 
	 * the endpoint interface.
	 */
	private EndpointValidator endpointValidator;
	
	/**
	 * <p>The instance of a {@link EndpointComponentFactory} which creates 
	 * {@link AbstractRequestBuilder}s.
	 */
	private EndpointComponentFactory<AbstractRequestBuilder> endpointComponentFactory;
	
	/**
	 * <p>This is the instance of {@link HttpClientContract} which is used to the 
	 * perform the network IO. By default a {@link BasicHttpClient} is used.   
	 */
	private final AsyncHttpClientContract communicator;
	

	{ 
		endpointValidator = new BasicEndpointValidator();
		endpointComponentFactory = RequestBuilderFactory.newInstance();
	}
	
	
	/**
	 * <p>Default constructor overridden to provided an implementation 
	 * of {@link AsyncHttpClientContract}. 
	 */
	public EndpointProxyFactory() {
	
		this.communicator = new MultiThreadedHttpClient();
	}
	
	/**
	 * <p>Parameterized constructor to provide an implementation of 
	 * {@link HttpClientContract}.
	 */
	private EndpointProxyFactory(AsyncHttpClientContract communicator) {
		
		this.communicator = communicator;
	}
	
	/**
	 * <p>Configures a new instance of this factory using the default 
	 * {@link BasicHttpClient}.
	 * 
	 * @return an instance of {@link EndpointProxyFactory} configured 
	 * 		   with defaults
	 * <br><br>
     * @since 1.1.5
	 */
	public static ProxyFactory newInstance() {

		return EndpointProxyFactory.newInstance(new MultiThreadedHttpClient());
	}
	
	/**
	 * <p>Configures a new instance of this factory using the provided instance of {@link HttpClientContract}.
	 * 
	 * @param communicator
	 * 			the {@link HttpClientContract} which is used for request execution
	 * <br><br>
	 * @return an instance of {@link EndpointProxyFactory} configured with defaults
	 * <br><br> 
     * @since 1.1.5
	 */
	public static ProxyFactory newInstance(AsyncHttpClientContract communicator) {

		return new EndpointProxyFactory(communicator);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized <T> T create(final Class<T> endpointClass) {

		try {
		
			//1.Validate the endpoint interface
			final URI uri = endpointValidator.validate(endpointClass);
			
			@SuppressWarnings("unchecked")
			T endpointProxy = (T)Proxy.newProxyInstance(endpointClass.getClassLoader(), new  Class<?>[] {endpointClass} , new InvocationHandler() {
				
				@Override
				public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
					
					//1.Construct the ProxyInvocationConfiguration
					ProxyInvocationConfiguration.Builder configBuilder = new  ProxyInvocationConfiguration.Builder();
					configBuilder.setEndpointClass(endpointClass).setUri(uri).setProxy(proxy).setRequest(method).setRequestArgs(args);
					ProxyInvocationConfiguration config = configBuilder.build();
					
					//2.Create the request
					final HttpRequestBase httpRequestBase = endpointComponentFactory.create(config).build(config); //TODO Check for exception!
					
					//3.Execute the HttpRequestBase either asynchronously or synchronously
					if(endpointClass.isAnnotationPresent(Asynchronous.class)) {
						
						executeAsyncRequest(httpRequestBase, config);
						
						return null; //asynchronous requests are not expected to return synchronously (or return Void.class.newInstance())
					}
					else {
						 
						HttpResponse httpResponse = executeRequest(httpRequestBase);

						//4.Parse the received HttpResponse and return the entity
						return parseResponse(httpResponse, config);
					}
				}
			});
			
			return endpointProxy;
		}
		catch(Exception e) {
			
			throw new ProxyFactoryException(getClass(), e);
		}
	}
	
	/**
	 * <p>Executes the {@link HttpRequestBase} and return the result as the type specified 
	 * by the {@link InvocationHandler#invoke(Object, Method, Object[])}'s generic objects 
	 * {@link Class} type.</p> 
	 * 
	 * @param httpRequestBase
	 * 				the instance of {@link HttpRequestBase} to execute
	 * <br><br>
	 * @return the {@link HttpResponse} which was received as a result of the execution.
	 * <br><br>
	 * @since 1.1.1
	 */
	private HttpResponse executeRequest(HttpRequestBase httpRequestBase) {

		try {
		
			HttpResponse response = communicator.executeRequest(httpRequestBase);
			
			if(!(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK))
				throw new IOException("HTTP request for " + httpRequestBase.getURI() + " failed with status code " + 
									   response.getStatusLine().getStatusCode() + ", " + response.getStatusLine().getReasonPhrase());
			
			return response;
		}
		catch(Exception e) {
			
			throw new ProxyFactoryException(getClass(), e);
		}
	}
	
	/**
	 * <p>Takes an {@link HttpRequestBase} and executes it <i>asynchronously<i> using 
	 * an {@link AsyncHttpClientContract}. If no {@link AsyncHandler} is found in the supplied 
	 * {@link ProxyInvocationConfiguration#getRequestArgs()}, then it is assumed that 
	 * response handling is not required.</p>
	 * 
	 * @param httpRequestBase
	 * 			the {@link HttpRequestBase} which is to be executed asynchronously
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} of the current request
	 * <br><br>
	 * @since 1.2.1
	 */
	private void executeAsyncRequest(HttpRequestBase httpRequestBase, ProxyInvocationConfiguration config) {
		
		communicator.executeAsyncRequest(httpRequestBase, config);
	}
	
	/**
	 * <p>Takes the {@link HttpResponse} received from request execution and 
	 * returns the parsed response as an instance of the type defined on the 
	 * request method.</p>
	 * 
	 * @param httpResponse
	 * 				the {@link HttpResponse} whose content needs to be parsed
	 * 
	 * @param config
	 * 				the {@link ProxyInvocationConfiguration} which supplies the parser any parameters
	 * <br><br>
	 * @return the parsed response as an instance of the user desired type
	 * <br><br>
	 * @throws RoboZombieInstantiationException
	 * 			if the {@link ResponseParser} cannot be instantiated
	 * <br><br>
	 * @throws RoboZombieIllegalAccessException
	 * 			if the {@link ResponseParser} cannot be instantiated
	 * <br><br>
	 * @since 1.1.2
	 */
	private Object parseResponse(HttpResponse httpResponse, ProxyInvocationConfiguration config) {

		Class<?> typeClass = config.getEndpointClass();
		Method method = config.getRequest();
		
		if(!method.getReturnType().equals(Void.class)) { //i.e. a response is expected
	
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
	
			try {
					
				ResponseParser<?> responseParser = parserType.newInstance();
				return responseParser.parse(httpResponse, config);
			} 
			catch (InstantiationException ie) {

				throw new RoboZombieInstantiationException(parserType, ie);
			} 
			catch (IllegalAccessException iae) {

				throw new RoboZombieIllegalAccessException(parserType, iae);
			}
		}
			
		return null;
	}
}
