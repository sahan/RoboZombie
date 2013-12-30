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

import static com.lonepulse.robozombie.util.Components.filterSkipped;
import static com.lonepulse.robozombie.util.Components.isDetached;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.annotation.Intercept;
import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>This {@link AbstractRequestProcessor} executes any {@link Interceptor}s that fall within the current 
 * invocation scope.</p>
 * 
 * <p>{@link Interceptor}s may be attached to a proxy invocation using an {@code @Intercept} annotation 
 * at the endpoint or request level. An {@link Interceptor} may also be passed as an instance along with 
 * the request parameters.</p>   
 * 
 * <p>Processor Dependencies:</p>
 * <ul>
 * 	<li>All {@link AbstractRequestProcessor}s which make up the response processor chain.</li>
 * </ul>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class InterceptionProcessor extends AbstractRequestProcessor {

	
	private static final Map<String, Interceptor> INTERCEPTORS = new HashMap<String, Interceptor>();
	
	
	/**
	 * <p>Accepts the {@link InvocationContext} and executes any {@link Interceptor}s which fall within 
	 * the scope of the current proxy invocation by passing in the provided {@link HttpRequestBase}.</p> 
	 * 
	 * <p>See {@link AbstractRequestProcessor#process(InvocationContext, HttpRequestBase)}.</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which will be used to discover all {@link Interceptor}s 
	 * 			which are within the scope of the current request invocation
	 * <br><br>
	 * @param request
	 * 			the {@link HttpRequestBase} which will be processed by all discovered {@link Interceptor}s  
	 * <br><br>
 	 * @return the same instance of {@link HttpRequestBase} which was processed by the {@link Interceptor}s
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if any of the {@link Interceptor}s failed to process the {@link HttpRequestBase}
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	protected HttpRequestBase process(InvocationContext context, HttpRequestBase request) {

		try {
			
			List<Class<? extends Interceptor>> interceptors = new ArrayList<Class<? extends Interceptor>>();
			
			Intercept endpointMetadata = context.getEndpoint().getAnnotation(Intercept.class);
			Intercept requestMetadata = context.getRequest().getAnnotation(Intercept.class);
			
			if(isDetached(context, Intercept.class)) {
			
				return request;
			}
			
			if(endpointMetadata != null) {
				
				interceptors.addAll(filterSkipped(context, Arrays.asList(endpointMetadata.value())));
			}
				
			if(requestMetadata != null) {
				
				interceptors.addAll(Arrays.asList(requestMetadata.value()));
			}
			
			for (Class<? extends Interceptor> interceptorType : interceptors) {
				
				String key = interceptorType.getName();
				Interceptor interceptor = INTERCEPTORS.get(key);
				
				if(interceptor == null) {
					
					interceptor = interceptorType.newInstance();
					INTERCEPTORS.put(key, interceptor);
				}
				
				interceptor.intercept(context, request);
			}
			
			List<Object> requestArgs = context.getArguments();
			
			if(requestArgs != null) {
			
				for (Object arg : requestArgs) {
					
					if(arg instanceof Interceptor) {
						
						((Interceptor)arg).intercept(context, request);
					}
				}
			}
			
			return request;
		}
		catch(Exception e) {
			
			throw new RequestProcessorException(context, getClass(), e);
		}
	}
}
