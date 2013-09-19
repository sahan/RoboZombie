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


import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;

import com.lonepulse.robozombie.annotation.FormParam;
import com.lonepulse.robozombie.annotation.PathParam;
import com.lonepulse.robozombie.annotation.QueryParam;
import com.lonepulse.robozombie.processor.AbstractProcessorChain;
import com.lonepulse.robozombie.processor.ProcessorChainFactory;
import com.lonepulse.robozombie.processor.ProcessorChainLink;

/**
 * <p>This is a concrete implementation of {@link AbstractProcessorChain} which creates a sequentially executed 
 * series of {@link AbstractRequestProcessor}s responsible for building the {@link HttpRequest} for a request 
 * invocation.</p>
 * 
 * <p>This chain consists of the {@link AbstractRequestProcessor}s listed below in the given order:  
 * 
 * <ol>
 * 	<li>{@link UriProcessor} - builds the complete URI from the root-path and the sub-path</li>
 * 	<li>{@link HeaderProcessor} - populates all static and dynamic HTTP headers</li>
 *  <li>{@link PathParamProcessor} - populates path parameters placeholders in the URI for any @{@link PathParam}s</li>
 *  <li>{@link QueryParamProcessor} - appends a query-string formulated for any @{@link QueryParam}s</li>
 *  <li>{@link FormParamProcessor} - inserts a form-url-encoded query-string for any @{@link FormParam}s</li>
 *  <li>{@link EntityProcessor} - inserts the {@link HttpEntity} identified using @{@link Entity}</li>
 * </ol>
 * 
 * <p><b>Note</b> that this processor-chain acts solely on the input arguments to {@link #run(Object...)} and returns 
 * {@code null} for all intents and purposes.</p>
 * 
 * <p><b>Note</b> that a chain-wide failure is <b>NOT recoverable</b>. All failures are of type {@link RequestProcessorException} 
 * which may be thrown from any arbitrary {@link ProcessorChainLink}. Any changes made on the arguments to the chain 
 * are <b>NOT rolled back</b>.</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class RequestProcessorChain extends AbstractProcessorChain<Void, RequestProcessorException> {
	
	
	/**
	 * <p>Creates a new instance of {@link RequestProcessorChain} with the {@link AbstractRequestProcessor}s 
	 * linked in the following sequence:</p>
	 * 
	 * <ol>
	 * 	<li>{@link UriProcessor} - builds the complete URI from the root-path and the sub-path</li>
	 *  <li>{@link HeaderProcessor} - populates all static and dynamic HTTP headers</li>
	 *  <li>{@link PathParamProcessor} - populates path parameters placeholders in the URI for any @{@link PathParam}s</li>
	 *  <li>{@link QueryParamProcessor} - appends a query-string formulated for any @{@link QueryParam}s</li>
	 *  <li>{@link FormParamProcessor} - inserts a form-url-encoded query-string for any @{@link FormParam}s</li>
	 *  <li>{@link EntityProcessor} - inserts the {@link HttpEntity} identified using @{@link Entity}</li>
	 * <ol>
	 * <br><br>
	 * @since 1.2.4
	 */
	@SuppressWarnings("unchecked") //safe generic array of Processor<Void, RequestProcessorException> for varargs (see http://tinyurl.com/coc4om)
	public RequestProcessorChain() {
		
		super(new ProcessorChainFactory<Void, RequestProcessorException>().newInstance(
			  new UriProcessor(), 
			  new HeaderProcessor(),
			  new PathParamProcessor(), 
			  new QueryParamProcessor(), 
			  new FormParamProcessor(), 
			  new EntityProcessor()));
	}

	/**
	 * <p>Executed for the root link which runs the {@link UriProcessor}. Takes the argument array which was provided 
	 * in {@link #run(Object...)} and invokes the root link, i.e. the {@link UriProcessor} and returns {@code null}.</p>
	 * 
	 * <p>See {@link AbstractRequestProcessor}.</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected Void onInitiate(ProcessorChainLink<Void, RequestProcessorException> root, Object... args) {
		
		root.getProcessor().run(args); //allow any exceptions to elevate to a chain-wide failure
		
		return null;
	}

	/**
	 * <p>Executed for each "link-crossing" from the root {@link UriProcessor} onwards. Takes the <b>successor</b> 
	 * and invokes it with the argument array which was provided in {@link #run(Object...)} and returns {@code null}.
	 * 
	 * <p>See {@link AbstractRequestProcessor}.</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected Void onTraverse(Void result, ProcessorChainLink<Void, RequestProcessorException> successor, Object... args) {
		
		successor.getProcessor().run(args);  //allow any exceptions to elevate to a chain-wide failure
		
		return null;
	}

	/**
	 * <p>No terminal conditions are performed.</p>
	 * 
	 * <p>See {@link AbstractRequestProcessor}.</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected void onTerminate(Void result, Object... args) {}
}
