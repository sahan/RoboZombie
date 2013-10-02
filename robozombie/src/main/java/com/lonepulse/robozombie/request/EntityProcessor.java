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
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http42.HttpHeaders;
import org.apache.http42.entity.ContentType;

import com.lonepulse.robozombie.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.annotation.Entity;

/**
 * <p>This is a concrete implementation of {@link RequestProcessor} which resolves and inserts the enclosing 
 * entity for an {@link HttpEntityEnclosingRequest} into the body of the request.</p>
 * 
 * <p>It identifies an @{@link Entity} annotation on a parameter of an endpoint interface method and inserts 
 * the value as a form entity in the resulting {@link HttpEntityEnclosingRequest}.
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class EntityProcessor extends AbstractRequestProcessor {

	
	/**
	 * <p>Accepts the {@link ProxyInvocationConfiguration} of an {@link HttpEntityEnclosingRequest} and inserts 
	 * any <i>the</i> request parameter which was annotated with @{@link Entity} into its body.</p>
	 * 
	 * <p><b>Note</b> that it makes no sense to scope multiple entities within the same entity enclosing request 
	 * (HTTP/1.1 <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html">RFC-2616</a>). This processor 
	 * fails for the following scenarios:</p>
	 * 
	 * <ul>
	 * 	<li><b>No entity</b> was found in the endpoint method definition.</li>
	 * 	<li><b>Multiple entities</b> were found in the endpoint method definition.</li>
	 * 	<li>The annotated entity <b>failed to be resolved</b> to a matching {@link HttpEntity}.</li>
	 * </ul>
	 * 
	 * <p>Parameter types are resolved to their {@link HttpEntity} as specified in 
	 * {@link RequestUtils#resolveHttpEntity(Object)}.</p>
	 * 
	 * <p>See {@link RequestProcessor#process(HttpRequestBase, ProxyInvocationConfiguration)}.</p>
	 *
	 * @param httpRequestBase
	 * 			an instance of {@link HttpEntityEnclosingRequestBase} which allows the inclusion of an 
	 * 			{@link HttpEntity} in its body
	 * <br><br>
	 * @param config
	 * 			an immutable instance of {@link ProxyInvocationConfiguration} which is used to retrieve the entity
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if an {@link HttpEntityEnclosingRequestBase} was discovered and yet the entity failed to be resolved 
	 * 			and inserted into the request body
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	protected void process(HttpRequestBase httpRequestBase, ProxyInvocationConfiguration config) 
	throws RequestProcessorException {

		try {

			if(httpRequestBase instanceof HttpEntityEnclosingRequestBase) {
				
				HttpEntity httpEntity = RequestUtils.findAndResolveEntity(config);
				
				((HttpEntityEnclosingRequestBase)httpRequestBase).setHeader(
					HttpHeaders.CONTENT_TYPE, ContentType.getOrDefault(httpEntity).getMimeType());
				
				((HttpEntityEnclosingRequestBase)httpRequestBase).setEntity(httpEntity);
			}
		}
		catch(MissingEntityException mee) { //violates HTTP 1.1 specification, be more verbose 
			
			if(!(httpRequestBase instanceof HttpPost)) { //allow leeway for POST requests
			
				StringBuilder errorContext = new StringBuilder("It is imperative that this request encloses an entity.")
				.append(" Identify exactly one entity by annotating an argument with @")
				.append(Entity.class.getSimpleName());
				
				throw new RequestProcessorException(errorContext.toString(), mee);
			}
		}
		catch(MultipleEntityException mee) { //violates HTTP 1.1 specification, be more verbose 
			
			StringBuilder errorContext = new StringBuilder("This request is only able to enclose exactly one entity.")
			.append(" Remove all @")
			.append(Entity.class.getSimpleName())
			.append(" annotations except for a single entity which is identified by this URI. ");
			
			throw new RequestProcessorException(errorContext.toString(), mee);
		}
		catch (EntityResolutionFailedException erfe) { //violates HTTP 1.1 specification, be more verbose
			
			StringBuilder errorContext = new StringBuilder("This request cannot proceed without an enclosing entity.")
			.append(" Ensure that the entity which is annotated with ")
			.append(Entity.class.getSimpleName())
			.append(" complies with the supported types as documented in ")
			.append(RequestUtils.class.getName())
			.append("#resolveHttpEntity(Object)");
			
			throw new RequestProcessorException(errorContext.toString(), erfe); 
		}
		catch(Exception e) {
			
			throw new RequestProcessorException(getClass(), config, e);
		}
	}
}
