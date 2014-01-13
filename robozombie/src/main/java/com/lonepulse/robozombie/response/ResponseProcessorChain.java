package com.lonepulse.robozombie.response;

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

import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.processor.AbstractProcessorChain;
import com.lonepulse.robozombie.processor.ProcessorChainFactory;
import com.lonepulse.robozombie.processor.ProcessorChainLink;

/**
 * <p>This is a concrete implementation of {@link AbstractProcessorChain} which creates a sequentially 
 * executed series of {@link AbstractResponseProcessor}s responsible for handling the {@link HttpResponse} 
 * of an endpoint request invocation.</p>
 * 
 * <p>This chain consists of the {@link AbstractResponseProcessor}s listed below in the given order:</p>
 * 
 * <ol>
 * 	<li>{@link HeaderProcessor} - retrieves the response headers and makes them available</li>
 * 	<li>{@link EntityProcessor} - deserializes and returns the content of the response body</li>
 * </ol>
 * 
 * <p><b>Note</b> that this processor-chain <b>may or may not</b> return the deserialized response entity 
 * depending on the availability of response content.</p>
 * 
 * <p><b>Note</b> that a chain-wide failure is <b>NOT recoverable</b>. All failures are of type 
 * {@link ResponseProcessorException} which may be thrown from any arbitrary {@link ProcessorChainLink}. 
 * Any changes made on the arguments to the chain are <b>NOT rolled back</b>.</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public final class ResponseProcessorChain extends AbstractProcessorChain<Object, ResponseProcessorException> {
	
	
	/**
	 * <p>Creates a new {@link ResponseProcessorChain} with the {@link AbstractResponseProcessor}s linked 
	 * in the following sequence:</p>
	 * 
	 * <ol>
	 * 	<li>{@link HeaderProcessor} - retrieves the response headers and makes them available</li>
	 * 	<li>{@link EntityProcessor} - deserializes and returns the content of the response body</li>
	 * </ol>
	 * <br><br>
	 * @since 1.3.0
	 */
	@SuppressWarnings("unchecked") //safe generic array of Processor<Object, ResponseProcessorException> for varargs (see http://tinyurl.com/coc4om)
	public ResponseProcessorChain() {
		
		super(new ProcessorChainFactory<Object, ResponseProcessorException>().newInstance(
			  new HeaderProcessor(), 
			  new EntityProcessor()));
	}

	/**
	 * <p>Executed for the root link which runs the {@link HeaderProcessor}. Takes the argument array which 
	 * was provided in {@link #run(Object...)} and invokes the root link, i.e. the {@link HeaderProcessor} 
	 * and returns the deserialized response content passed down from the successive processor.</p>
	 * 
	 * <p>See {@link AbstractResponseProcessor}.</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected Object onInitiate(ProcessorChainLink<Object, ResponseProcessorException> root, Object... args) {
		
		return root.getProcessor().run(args); //allow any exceptions to elevate to a chain-wide failure
	}

	/**
	 * <p>Executed for each "link-crossing" from the root {@link HeaderProcessor} onwards. Takes the 
	 * <b>successor</b> and invokes it with the argument array which was provided in {@link #run(Object...)} 
	 * and returns the deserialized response content (if any).</p>
	 * 
	 * <p>See {@link AbstractResponseProcessor}.</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected Object onTraverse(Object result, ProcessorChainLink<Object, ResponseProcessorException> successor, Object... args) {
	
		return successor.getProcessor().run(args[0], args[1], result);  //allow any exceptions to elevate to a chain-wide failure
	}

	/**
	 * <p>No terminal conditions are performed.</p>
	 * 
	 * <p>See {@link AbstractResponseProcessor}.</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected void onTerminate(Object result, Object... args) {}
}
