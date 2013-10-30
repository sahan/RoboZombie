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

import static com.lonepulse.robozombie.util.Assert.assertAssignable;
import static com.lonepulse.robozombie.util.Assert.assertNotEmpty;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.annotation.Entity;
import com.lonepulse.robozombie.annotation.FormParam;
import com.lonepulse.robozombie.annotation.PathParam;
import com.lonepulse.robozombie.annotation.QueryParam;
import com.lonepulse.robozombie.inject.InvocationContext;
import com.lonepulse.robozombie.processor.AbstractProcessorChain;
import com.lonepulse.robozombie.processor.ProcessorChainFactory;
import com.lonepulse.robozombie.processor.ProcessorChainLink;

/**
 * <p>This is a concrete implementation of {@link AbstractProcessorChain} which creates a sequentially executed 
 * series of {@link AbstractRequestProcessor}s responsible for building the {@link HttpRequestBase} for a request 
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
 * <p><b>Note</b> that this processor-chain requires a single {@link InvocationContext} to be {@link #run(Object...)}} 
 * and returns the {@link HttpRequestBase} which was processed through the entire chain.</p>
 * 
 * <p><b>Note</b> that a chain-wide failure is <b>NOT recoverable</b>. All failures are of type {@link RequestProcessorException} 
 * which may be thrown from any arbitrary {@link ProcessorChainLink}. Any changes made on the arguments to the chain 
 * are <b>NOT rolled back</b>.</p> 
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class RequestProcessorChain extends AbstractProcessorChain<HttpRequestBase, RequestProcessorException> {
	
	
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
		
		super(new ProcessorChainFactory<HttpRequestBase, RequestProcessorException>().newInstance(
			  new UriProcessor(), 
			  new HeaderProcessor(),
			  new PathParamProcessor(), 
			  new QueryParamProcessor(), 
			  new FormParamProcessor(), 
			  new EntityProcessor()));
	}

	/**
	 * <p>Accepts the {@link InvocationContext} given to {@link #run(Object...)}} the {@link RequestProcessorChain} 
	 * and translates the request metadata to a concrete instance of {@link HttpRequestBase}. The {@link HttpRequestBase}, 
	 * together with the {@link InvocationContext} is then given to the root link which runs the {@link UriProcessor} 
	 * and returns the resulting {@link HttpRequestBase}.</p> 
	 * 
	 * <p>See {@link AbstractRequestProcessor}.</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected HttpRequestBase onInitiate(ProcessorChainLink<HttpRequestBase, RequestProcessorException> root, Object... args) {
		
		InvocationContext context = assertAssignable(assertNotEmpty(args)[0], InvocationContext.class);
		
		HttpRequestBase httpRequestBase = RequestUtils.translateRequestMethod(context);
		
		return root.getProcessor().run(httpRequestBase, context); //allow any exceptions to elevate to a chain-wide failure
	}

	/**
	 * <p>Executed for each "link-traversal" from the root {@link UriProcessor} onwards. Takes the <b>successor</b> 
	 * and invokes it with the argument array which was provided in {@link #run(Object...)} and returns the resulting 
	 * {@link HttpRequestBase}.</p>
	 * 
	 * <p>See {@link AbstractRequestProcessor}.</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected HttpRequestBase onTraverse(HttpRequestBase result, ProcessorChainLink<HttpRequestBase, RequestProcessorException> successor, Object... args) {
		
		return successor.getProcessor().run(result, args[0]);  //allow any exceptions to elevate to a chain-wide failure
	}

	/**
	 * <p>No terminal conditions are performed.</p>
	 * 
	 * <p>See {@link AbstractRequestProcessor}.</p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected void onTerminate(HttpRequestBase result, Object... args) {}
}
