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

import static com.lonepulse.robozombie.util.Assert.assertAssignable;
import static com.lonepulse.robozombie.util.Assert.assertLength;
import static com.lonepulse.robozombie.util.Assert.assertNotNull;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.processor.Processor;
import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>This is an abstract implementation of {@link Processor} which specifies a template for processing 
 * the <i>response</i> of a request execution by referencing the <i>metadata</i> on a proxy endpoint 
 * <b>request</b>. It includes an implementation of {@link Processor#run(Object...)} that checks the 
 * preconditions for executing {@link #process(InvocationContext, HttpRequestBase)}.</p>
 * 
 * <p>All implementations must be aware of the {@link InvocationContext} which can be used to discover 
 * information about the endpoint and the request declaration. This information can be queried based on 
 * the <i>targeting criteria</i> for this response processor and the resulting information should be used 
 * to <i>deserialize</i> the given {@link HttpResponse}.</p>
 * 
 * <p>It is advised to adhere to <a href="www.w3.org/Protocols/rfc2616/rfc2616.html‎">RFC 2616</a> of 
 * <b>HTTP 1.1</b> when designing an implementation.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
abstract class AbstractResponseProcessor implements Processor<Object, ResponseProcessorException> {

	
	/**
	 * <p>Accepts an {@link InvocationContext} and an {@link HttpResponse}, validates all preconditions 
	 * and uses the metadata contained within the configuration to process and subsequently parse the 
	 * request. Any implementations that wish to check additional preconditions or those that wish to 
	 * alter this basic approach should override this method.</p>
	 * 
	 * <p><b>Note</b> that this method is expected to return the <i>deserialized response entity</i> of 
	 * the type specified by the request definition. This is passed along to all successive processors 
	 * in the chain via the processor arguments.</p>
	 * 
	 * <p>Delegates to {@link #process(InvocationContext, HttpResponse, Object)}.</p>
	 * 
	 * <p>See {@link Processor#run(Object...)}.</p>
	 *
	 * @param args
	 * 			a array of <b>length 2 or more</b> with an {@link HttpResponse}, an {@link InvocationContext} 
	 * 			and possibly the result of the deserialized response entity 
	 * <br><br>
	 * @return the deserialized response entity, which may be {@code null} for endpoint request definitions 
	 * 			which do not declare a return type or for those which the return type is {@link Void}
	 * <br><br>
	 * @throws IllegalArgumentException
	 * 			if the supplied arguments array is {@code null} or if the number of arguments is less than 2, 
	 * 			or if the arguments are not of the expected type
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if response processing failed for the given {@link InvocationContext} and {@link HttpResponse}
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	public Object run(Object... args) {

		assertLength(args, 2, 3);
		
		return process(assertAssignable(assertNotNull(args[0]), InvocationContext.class), 
					   assertAssignable(assertNotNull(args[1]), HttpResponse.class), 
					   (args.length > 2)? args[2] :null);
	}
	
	/**
	 * <p>Takes the {@link InvocationContext} for the given {@link HttpResponse} and uses the metadata 
	 * contained within the configuration to <i>deserialize</i> the <i>response body</i> and perform 
	 * additional processing based on the <i>response headers</i>.</p>
	 * 
	 * <p>The provided {@link HttpResponse} may contain a response entity which should be deserialized 
	 * to the correct type and it may contain certain essential response headers which should be processed. 
	 * Any implementation may wish to perform processing conditionally based on the response code. Refer 
	 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html">Section 9</a> of the <b>HTTP 1.1</b> 
	 * for more information.</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which is used to discover any annotated metadata for the 
	 * 			request declaration which may be required for response processing
	 * <br><br>
	 * @param response
	 * 			the {@link HttpResponse} received as result of a request execution; the response body 
	 * 			should be deserialized to the correct type and all response headers should be processed 
	 * <br><br>
	 * @param deserializedResponse
	 * 			the deserialized response content which will be passed along to all processors in the chain
 	 * <br><br>
 	 * @return the deserialized response entity of the type associated with the endpoint request definition
 	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if the processor finds an {@link HttpResponse} <i>which it should act upon</i> and yet fails 
	 * 			to perform the necessary processing
	 * <br><br>
	 * @since 1.3.0
	 */
	protected abstract Object process(InvocationContext context, HttpResponse response, Object deserializedResponse);
}
