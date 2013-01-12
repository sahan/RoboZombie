package com.lonepulse.robozombie.core.request;

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


/**
 * <p>Defines a list of legal header types which can be included in HTTP requests.</p>
 * 
 * @version 1.0.0
 * 
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum HttpHeaders {
	
	
	/**
	 * <p>This header identifies the type of the content in the HTTP request. 
	 * 
	 * @since 1.0.0
	 */
	CONTENT_TYPE("Content-Type");
	

	/**
	 * <p>The MIME string which identifies this content type. 
	 */
	private String name;
	
	
	/**
	 * <p>Initializes {@link #name}.
	 * 
	 * @param name
	 * 			the MIME type string which identifies this 
	 * 			content type
	 */
	private HttpHeaders(String name) {
		
		this.name = name;
	}
	
	/**
	 * <p>Accessor for {@link #name}.
	 * 
	 * @return {@link #name}
	 */
	public String getHeaderName() {
		
		return this.name;
	}

	/**
	 * <p>Accessor for {@link #name}.
	 * 
	 * @return {@link #name}
	 */
	@Override
	public String toString() {
	
		return getHeaderName();
	}
}
