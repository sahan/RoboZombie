package com.lonepulse.robozombie.inject;

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


import static com.lonepulse.robozombie.util.Assert.assertValid;

import java.lang.reflect.Method;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.RoboZombieRuntimeException;
import com.lonepulse.robozombie.executor.RequestExecutors;
import com.lonepulse.robozombie.processor.Processors;

/**
 * <p>This is a concrete implementation of {@link Invocation} which models a command for a request invocation 
 * on the proxy of an endpoint definition. It accepts an {@link InvocationContext} and uses its information to 
 * direct the invocation as defined on an {@link ProxyInvocation.Template}. Instances of {@link ProxyInvocation} 
 * may be used for deferred request execution using a queuing strategy, reuse via caching, etc.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class ProxyInvocation implements Invocation {

	
	/**
	 * <p>This is template defines the stages in a request invocation on an endpoint proxy. Custom extensions 
	 * of this template can be used to manage the processing of request execution in alternate ways. Below is 
	 * an ordered list of the stages defined by this default template: </p>
	 * 
	 * <ol>
	 * 	<li>{@link #buildRequest(InvocationContext)} - creates a request using the metadata and arguments</li>
	 * 	<li>{@link #executeRequest(HttpRequestBase, InvocationContext)} - executes it using an {@link HttpClient}</li>
	 * 	<li>{@link #handleResponse(HttpResponse, InvocationContext)} - transforms the response to a consumable</li>
	 * </ol>
	 * 
	 * @version 1.1.0
	 * <br><br>
	 * @since 1.2.4
	 * <br><br>
	 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	static abstract class Template {
		
		
		private final Class<?> endpoint;
		
		
		/**
		 * <p>Creates a new {@link ProxyInvocation.Template} which defines the stages in request invocation 
		 * and execution along with the response handling.</p> 
		 * 
		 * <p>Each template focuses on a single endpoint and as such the provision of an endpoint interface 
		 * is mandatory. The provided interface will be validated against a set of predetermined rules to 
		 * ensure its integrity. See {@link Validators#ENDPOINT}.</p>
		 * 
		 * <p>Any custom {@link Zombie.Configuration} on the endpoint will be registered after validation.</p>
		 * 
		 * @param endpoint
		 * 			the endpoint definition interface for which {@link ProxyInvocation}s are to be made
		 * <br><br>
		 * @throws EndpointValidationFailedException
		 * 			if the given endpoint failed to be validated against the set of predetermined rules
		 * <br><br>
		 * @since 1.2.4
		 */
		public Template(Class<?> endpoint) {
			
			this.endpoint = assertValid(endpoint, Validators.ENDPOINT);
			RequestExecutors.CONFIGURATION.register(this.endpoint);
		}

		/**
		 * <p>Responsible for accepting an {@link InvocationContext} and constructing an {@link HttpRequestBase} 
		 * which conforms with the metadata for the invoked request.</p>
		 *
		 * @param context
		 * 			the {@link InvocationContext} containing the metadata on the invoked request
		 * <br><br>
		 * @return an <b>extension</b> of {@link HttpRequestBase} which is grown to conform with the metadata 
		 * 		   found on the given {@link InvocationContext} 
		 * <br><br>
		 * @since 1.2.4
		 */
		protected HttpRequestBase buildRequest(InvocationContext context) {
			
			return (HttpRequestBase) Processors.REQUEST.run(context);
		}
		
		/**
		 * <p>Responsible for executing a request using the designated {@link HttpClient} and returning the 
		 * resulting {@link HttpResponse}, if any.</p>
		 * 
		 * @param request
		 * 			the extension of {@link HttpRequestBase} which is to be executed
		 * <br><br>
		 * @param context
		 * 			the {@link InvocationContext} associated with the current request execution
		 * <br><br>
		 * @return the {@link HttpResponse} which resulted from the request execution; else {@code null} 
		 * 		   if the execution did not produce any response 
		 * <br><br>
		 * @since 1.2.4
		 */
		protected HttpResponse executeRequest(HttpRequestBase request, InvocationContext context) {
			
			return RequestExecutors.RESOLVER.resolve(context).execute(request, context);
		}
		
		/**
		 * <p>Responsible for parsing the response content to produce a <i>meaningful</i> result which 
		 * can the readily consumed by clients.</p>
		 *
		 * @param response
		 * 			the {@link HttpResponse} containing the raw unprocessed response content
		 * <br><br>
		 * @param context
		 * 			the {@link InvocationContext} associated with the current response processing 
		 * <br><br>
		 * @return the processed response content in the form of a generic object
		 * <br><br>
		 * @since 1.2.4
		 */
		protected Object handleResponse(HttpResponse response, InvocationContext context) {
			
	        return Processors.RESPONSE.run(response, context);
		}
	}
	
	
	private final Template template;
	private final InvocationContext context;
	
	
	/**
	 * <p>Creates a new instance of {@link ProxyInvocation} which models a command for request invocation 
	 * on the proxy of an endpoint definition.</p>
	 *
	 * @param template
	 * 			the instance of {@link ProxyInvocation.Template} which defines the stages in invocation
	 * <br><br>
	 * @param proxy
	 * 			the instance of the proxy for the endpoint definition on which the request was invoked
	 * <br><br>
	 * @param method
	 * 			the {@link Method} on the endpoint definition interface representing the invoked request
	 * <br><br>
	 * @param args
	 * 			the runtime method arguments which were passed to the request invoked on the proxy
	 * <br><br>
	 * @return an instance of {@link ProxyInvocation} which can be used to perform the request execution
	 * <br><br>
	 * @since 1.2.4
	 */
	public static final ProxyInvocation newInstance(
		ProxyInvocation.Template template, Object proxy, Method method, Object[] args) {
		
		InvocationContext context = InvocationContext.newBuilder()
		.setEndpoint(template.endpoint)
		.setProxy(proxy)
		.setRequest(method)
		.setArguments(args)
		.build();
		
		return new ProxyInvocation(context, template);
	}
	
	private ProxyInvocation(InvocationContext context, Template template) {
		
		this.template = template;
		this.context = context;
	}
	
	/**
	 * <p>Allows the request invocation to progress by directing each stage of the process from context 
	 * instantiation to request processing, onto request execution and finally response handling.</p>
	 *
	 * @return the result of the invocation as specified by the request definition on the endpoint
	 * <br><br>
	 * @throws RoboZombieRuntimeException
	 * 			(or any extension therein) if the request invocation failed on the proxy instance
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public Object invoke() {
		
		HttpRequestBase request = template.buildRequest(context); 
		HttpResponse response = template.executeRequest(request, context);
		return response == null? null :template.handleResponse(response, context);
	}
}
