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

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;

import com.lonepulse.robozombie.core.annotation.Endpoint;

/**
 * <p>This is a wrapper class which encompasses the <i>configuration</i> of a particular 
 * proxy invocation. It serves to consolidate method parameters for the functionality 
 * in {@link EndpointProxyFactory}.</p>
 * 
 * @version 1.1.2
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class ProxyInvocationConfiguration {
	
	/**
	 * <p>This builder can be used to create a {@link ProxyInvocationConfiguration} 
	 * instance.</p>
	 * 
	 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 * <br><br>
	 * @since 1.1.1
	 */
	public static class Builder {
		
		/**
		 * @see ProxyInvocationConfiguration#endpointClass
		 */
		private Class<?> endpointClass;
		
		/**
		 * @see ProxyInvocationConfiguration#uri
		 */
		private URI uri;
		
		/**
		 * @see ProxyInvocationConfiguration#proxy
		 */
		private Object proxy;
		
		/**
		 * @see ProxyInvocationConfiguration#request
		 */
		private Method request; 
		
		/**
		 * @see ProxyInvocationConfiguration#requestArgs
		 */
		private Object[] requestArgs;
		
		
		/**
		 * <p>{@link ProxyInvocationConfiguration.Builder} made accessible for creation of 
		 * {@link ProxyInvocationConfiguration} instances.</p>
		 */
		public Builder() {
			//TODO consider including essential builder parameters in here
		}

		public Builder setEndpointClass(Class<?> endpointClass) {
			this.endpointClass = endpointClass;
			return this;
		}

		public Builder setUri(URI uri) {
			this.uri = uri;
			return this;
		}

		public Builder setProxy(Object proxy) {
			this.proxy = proxy;
			return this;
		}

		public Builder setRequest(Method request) {
			this.request = request;
			return this;
		}

		public Builder setRequestArgs(Object[] requestArgs) {
			this.requestArgs = requestArgs;
			return this;
		}
		
		/**
		 * <p>Once the {@link ProxyInvocationConfiguration.Builder} instance is configured, 
		 * this can be called to build an instance of {@link ProxyInvocationConfiguration}.</p> 
		 * 
		 * @return the {@link ProxyInvocationConfiguration} built to reflect the configured 
		 * 		   {@link ProxyInvocationConfiguration.Builder}
		 */
		public ProxyInvocationConfiguration build() {
			
			return new ProxyInvocationConfiguration(this);
		}

		@Override
		public String toString() {
			
			StringBuilder builder = new StringBuilder();
			
			builder.append("Builder [endpointClass=");
			builder.append(endpointClass);
			builder.append(", uri=");
			builder.append(uri);
			builder.append(", proxy=");
			builder.append(proxy);
			builder.append(", request=");
			builder.append(request);
			builder.append(", requestArgs=");
			builder.append(Arrays.toString(requestArgs));
			builder.append("]");
			
			return builder.toString();
		}
	}
	
	
	/**
	 * <p>The {@link Class} of the interface which model an {@link Endpoint}.
	 */
	private Class<?> endpointClass;
	
	/**
	 * <p>The base {@link URI} which was constructed from the endpoint.
	 */
	private URI uri;
	
	/**
	 * <p>The proxy instance of the endpoint interface on which the request was invoked.
	 */
	private Object proxy;
	
	/**
	 * <p>The {@link Method} of the invoked request.
	 */
	private Method request; 
	
	/**
	 * <p>The parameters supplied to {@link #request}.
	 */
	private Object[] requestArgs;


	/**
	 * <p>Instantiation prevented; instead use {@link ProxyInvocationConfiguration.Builder} 
	 * to create an instance. 
	 */
	private ProxyInvocationConfiguration(Builder builder) {
		
		this.endpointClass = builder.endpointClass;
		this.uri = builder.uri;
		this.proxy = builder.proxy;
		this.request = builder.request;
		this.requestArgs = builder.requestArgs;
	}

	public Class<?> getEndpointClass() {
		return endpointClass;
	}

	public URI getUri() {
		return uri;
	}

	public Object getProxy() {
		return proxy;
	}

	public Method getRequest() {
		return request;
	}

	public Object[] getRequestArgs() {
		return requestArgs;
	}

	@Override
	public String toString() {
		
		StringBuilder builder2 = new StringBuilder();
		
		builder2.append("ProxyInvocationConfiguration [endpointClass=");
		builder2.append(endpointClass);
		builder2.append(", uri=");
		builder2.append(uri);
		builder2.append(", proxy=");
		builder2.append(proxy);
		builder2.append(", request=");
		builder2.append(request);
		builder2.append(", requestArgs=");
		builder2.append(Arrays.toString(requestArgs));
		builder2.append("]");
		
		return builder2.toString();
	}
}
