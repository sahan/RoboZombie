package com.lonepulse.robozombie.response;

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


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.processor.Processor;
import com.lonepulse.robozombie.processor.ProxyInvocationConfiguration;

/**
 * <p>This is an abstract implementation of {@link Processor} which specifies a template for processing the 
 * <i>response</i> of a request execution by referencing the <i>metadata</i> on a proxy endpoint <b>request</b>. 
 * It includes an implementation of {@link Processor#run(Object...)} that checks the preconditions for executing 
 * {@link #process(HttpRequestBase, ProxyInvocationConfiguration)}.</p>
 * 
 * <p>All implementations must be aware of the {@link ProxyInvocationConfiguration} which can be used to discover 
 * information about the endpoint and the request declaration. This information can be queried based on the 
 * <i>targeting criteria</i> for this response processor and the resulting information should be used to <i>parse</i> 
 * the given {@link HttpResponse}.</p>
 * 
 * <p><b>Note that all implementations must account for a {@code null} {@link HttpResponse} in the arguments list.</b></p>
 * 
 * <p>It is advised to adhere to <a href="www.w3.org/Protocols/rfc2616/rfc2616.html‎">RFC 2616</a> of <b>HTTP 1.1</b> 
 * when designing an implementation.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public abstract class AbstractResponseProcessor implements Processor<Object, ResponseProcessorException> {

	
	/**
	 * <p>Accepts an {@link HttpResponse} and a {@link ProxyInvocationConfiguration}, validates all preconditions 
	 * and uses the metadata contained within the configuration to process and subsequently parse the request. Any 
	 * implementations that wish to check additional preconditions or those that wish to alter this basic approach 
	 * should override this method.</p>
	 * 
	 * <p><b>Note</b> that this method is expected to return the <i>parsed response entity</i> where an endpoint 
	 * request definition specifies a return type. This should then be passed along in the processor arguments.</p>
	 * 
	 * <p>Delegates to {@link #process(HttpResponse, ProxyInvocationConfiguration, Object)}.</p>
	 * 
	 * <p>See {@link Processor#run(Object...)}.</p>
	 *
	 * @param args
	 * 			a array of <b>length 2 or more</b> with an {@link HttpResponse}, a {@link ProxyInvocationConfiguration} 
	 * 			and possible the result of the parsed response entity 
	 * <br><br>
	 * @return the parsed response entity, which may be {@code null} for endpoint request definitions which do not declare 
	 * 		   a return type or for those which the return type is {@link Void}
	 * <br><br>
	 * @throws IllegalArgumentException
	 * 			if the supplied arguments array is {@code null} or if the number of arguments is less that 2, 
	 * 			or if the arguments are not of the expected type
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if {@link #process(HttpResponse, ProxyInvocationConfiguration, Object)} failed for the given 
	 * 			{@link HttpResponse}, {@link ProxyInvocationConfiguration} and possible for the parsed response entity
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public Object run(Object... args) throws ResponseProcessorException {

		if(args == null || args.length < 2) {
			
			StringBuilder errorContext = new StringBuilder("An ")
			.append(AbstractResponseProcessor.class.getName())
			.append(" requires at least two arguments: the ")
			.append(HttpResponse.class.getName())
			.append(" which it should process and the ")
			.append(ProxyInvocationConfiguration.class.getName())
			.append(" which provides the data and metadata for processing.");
			
			throw new IllegalArgumentException(errorContext.toString());
		}
		
		StringBuilder accumulatedContext = new StringBuilder();
		boolean hasIllegalArguments = false;
		
		if(args[1] == null || !(args[1] instanceof ProxyInvocationConfiguration)) {
			
			accumulatedContext.append("The second argument to should be an instance of ")
			.append(ProxyInvocationConfiguration.class.getName())
			.append(" which cannot be <null>. ");
			
			hasIllegalArguments = true;
		}
		
		if(hasIllegalArguments) {
			
			throw new IllegalArgumentException(accumulatedContext.toString());
		}
		
		return process((HttpResponse)args[0], (ProxyInvocationConfiguration)args[1], (args.length > 2)? args[2] :null);
	}
	
	/**
	 * <p>Takes the {@link ProxyInvocationConfiguration} for the given {@link HttpResponse} and uses the 
	 * metadata contained within the configuration to <i>parse</i> the <i>response body</i> and perform 
	 * additional processing based on the <i>response headers</i>.</p>
	 * 
	 * <p>The provided {@link HttpResponse} may contain a response entity which should be parsed to the 
	 * correct type and it may contain certain essential response headers which should be processed. Any 
	 * implementation may wish to perform processing conditionally based on the response code. Refer 
	 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html">Section 9</a> of the <b>HTTP 1.1</b> 
	 * for more information.</p>
	 * 
	 * @param HttpResponse
	 * 			the {@link HttpResponse} received as result of a request execution; the response body should 
	 * 			be parsed to the correct type and the response headers should be processed if required 
	 * <br><br>
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} which is used to discover any annotated metadata 
	 * 			for the request declarion which may help in processing the response and making the necessary 
	 * 			information available for the result-map
	 * <br><br>
	 * @param results
	 * 			the result-map which contains an aggregation of all the results produced by the chain insofar 
	 * 			and serves as the container for the results produced by this processor
 	 * <br><br>
 	 * @return the parsed response entity of the type associated with the endpoint request definition
 	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if the processor finds an {@link HttpResponse} <i>which it should act upon</i> and yet fails 
	 * 			to perform the necessary processing
	 * <br><br>
	 * @since 1.2.4
	 */
	protected abstract Object process(
		HttpResponse HttpResponse, ProxyInvocationConfiguration config, Object parsedResponse) 
		throws ResponseProcessorException;
}
