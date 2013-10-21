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

import static com.lonepulse.robozombie.util.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Represents a single <b>request invocation</b> on an endpoint proxy. The <i>context</i> comprises of both 
 * runtime information and request/endpoint metadata, as well as a reference to the proxy instance on which the 
 * request was invoked.</p>
 * 
 * <p>Each context is immutable and new instances can be created using {@link InvocationContext#newBuilder()}</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class InvocationContext {
	
	/**
	 * <p>This contract defines the services for creating an {@link InvocationContext}. It identifies all the 
	 * information which is governed by a single context and the accepted format therein.</p>
	 * 
	 * @version 1.1.0
	 * <br><br>
	 * @since 1.2.4
	 * <br><br>
	 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	static interface Builder {
		
		/**
		 * <p>Sets the endpoint interface definition whose request was invoked via the associated proxy.</p>
		 * 
		 * @param endpoint
		 * 			the endpoint {@link Class} to which invoked request belongs 
		 * <br><br>
		 * @return the current instance of the {@link Builder} with the endpoint assigned
		 * <br><br>
		 * @since 1.2.4
		 */
		Builder setEndpoint(Class<?> endpoint);

		/**
		 * <p>Sets the dynamic proxy for the endpoint on the which the request was invoked.</p>  
		 * 
		 * @param proxy
		 * 			the proxy on which the request was invoked 
		 * <br><br>
		 * @return the current instance of the {@link Builder} with the proxy assigned
		 * <br><br>
		 * @since 1.2.4
		 */
		Builder setProxy(Object proxy);

		/**
		 * <p>Sets the {@link Method} on the endpoint interface which defines the invoked request.</p>
		 * 
		 * @param request
		 * 			the {@link Method} which defines the request
		 * <br><br>
		 * @return the current instance of the {@link Builder} with the request assigned
		 * <br><br>
		 * @since 1.2.4
		 */
		Builder setRequest(Method request);

		/**
		 * <p>Sets the runtime arguments which were supplied to the endpoint proxy upon request invocation.</p>
		 * 
		 * @param arguments
		 * 			the runtime request arguments supplied to the endpoint proxy
		 * <br><br>
		 * @return the current instance of the {@link Builder} with the request assigned
		 * <br><br>
		 * @since 1.2.4
		 */
		Builder setArguments(Object[] arguments);
		
		/**
		 * <p>Takes the supplied information and creates a new instance of {@link InvocationContext}.</p> 
		 *  
		 * @return a new instance of {@link InvocationContext} containing the supplied information
		 * <br><br>
		 * @since 1.2.4
		 */
		InvocationContext build();
	}
	
	private static final class InvocationContextBuilder implements Builder {
		
		private Class<?> endpoint;
		private Object proxy;
		private Method request; 
		private List<Object> arguments;

		@Override
		public Builder setEndpoint(Class<?> endpoint) {
			
			this.endpoint = assertNotNull(endpoint);
			return this;
		}

		@Override
		public Builder setProxy(Object proxy) {
			
			this.proxy = assertNotNull(proxy);
			return this;
		}

		@Override
		public Builder setRequest(Method request) {
			
			this.request = assertNotNull(request);
			return this;
		}

		@Override
		public Builder setArguments(Object[] arguments) { //null args imply zero parameters
			
			this.arguments = Collections.unmodifiableList(
				arguments == null? new ArrayList<Object>() :Arrays.asList(arguments));
			
			return this;
		}

		@Override
		public InvocationContext build() {

			return new InvocationContext(this);
		}
	}
	
	/**
	 * <p>Returns an instance of {@link Builder} which can be used to construct a new {@link InvocationContext} 
	 * by supplying the necessary information.</p>
	 * 
	 * <p><b>Note</b> that concrete implementations of {@link Builder} might be wrapped in additional layers 
	 * which <b>validate</b> the given information before representing it in a new {@link InvocationContext}.</p>
	 * 
	 * @return a new instance of {@link Builder} for constructing an {@link InvocationContext}
	 * <br><br>
	 * @since 1.2.4
	 */
	static final Builder newBuilder() {
		
		return new InvocationContextBuilder();
	}
	
	
	private final Class<?> endpoint;
	private final Object proxy;
	private final Method request; 
	private final List<Object> arguments;
	

	private InvocationContext(InvocationContextBuilder builder) {
		
		this.endpoint = builder.endpoint;
		this.proxy = builder.proxy;
		this.request = builder.request;
		this.arguments = builder.arguments;
	}

	/**
	 * <p>Retrieves the endpoint interface definition on whose request was invoked.</p>
	 * 
	 * <p>See {@link Builder#setEndpoint(Class)}</p>
	 * 
	 * @return the {@link Class} of the request endpoint
	 * <br><br>
	 * @since 1.2.4
	 */
	public Class<? extends Object> getEndpoint() {
		
		return endpoint;
	}

	/**
	 * <p>Retrieves the dynamic proxy for the endpoint on the which the request was invoked.</p>
	 * 
	 * <p>See {@link Builder#setProxy(Object)}</p>
	 * 
	 * @return the endpoint proxy on which the request was invoked
	 * <br><br>
	 * @since 1.2.4
	 */
	public Object getProxy() {
		
		return proxy;
	}

	/**
	 * <p>Retrieves the {@link Method} on the endpoint interface which defines the invoked request.</p>
	 * 
	 * <p>See {@link Builder#setRequest(Method)}</p>
	 * 
	 * @return the definition for the invoked request
	 * <br><br>
	 * @since 1.2.4
	 */
	public Method getRequest() {
		
		return request;
	}

	/**
	 * <p>Retrieves the runtime arguments supplied to the endpoint proxy upon request invocation.</p>
	 * 
	 * <p>See {@link Builder#setArguments(Object[])}</p>
	 * 
	 * @return the arguments with which the request was invoked
	 * <br><br>
	 * @since 1.2.4
	 */
	public List<Object> getArguments() {
		
		return arguments;
	}
}
