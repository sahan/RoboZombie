package com.lonepulse.robozombie.request.header;

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


import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.processor.ProxyInvocationConfiguration;

/**
 * <p>Exposes all available {@link HeaderBuilder}s and delegates communication. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum HeaderBuilders implements HeaderBuilder {

	/**
	 * See {@link BasicHeaderBuilder}.
	 * 
	 * @since 1.1.0
	 */
	BASIC(new BasicHeaderBuilder());
	
	
	/**
	 * The exposed instance of {@link HeaderBuilder}.
	 */
	private HeaderBuilder headerBuilder;
	
	
	/**
	 * <p>Instantiates {@link #headerBuilder} with the give instance of 
	 * {@link HeaderBuilder}.
	 * 
	 * @param headerBuilder
	 * 			the associated instance of {@link HeaderBuilder}
	 */
	private HeaderBuilders(HeaderBuilder headerBuilder) {
		
		this.headerBuilder = headerBuilder;
	}

	/**
	 * See {@link HeaderBuilder#build(HttpRequestBase, ProxyInvocationConfiguration)}.
	 * 
	 * @since 1.1.0
	 */
	@Override
	public HttpRequestBase build(HttpRequestBase httpRequestBase, ProxyInvocationConfiguration config) 
	throws Exception {

		return this.headerBuilder.build(httpRequestBase, config);
	}
}
