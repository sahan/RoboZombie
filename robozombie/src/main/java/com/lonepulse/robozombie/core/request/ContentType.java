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
 * <p>Defines the MIME types which HTTP content values can take.
 * 
 * @version 1.1.0
 * 
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum ContentType {

	/**
	 * <p>MIME type for request parameters other than those of method GET. 
	 * 
	 * @since 1.1.0
	 */
	APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded");
	
	
	
	/**
	 * <p>The MIME string which identifies this content type. 
	 */
	private String mimeType;
	
	
	/**
	 * <p>Initializes {@link #mimeType}.
	 * 
	 * @param mimeType
	 * 			the MIME type string which identifies this 
	 * 			content type
	 */
	private ContentType(String mimeType) {
		
		this.mimeType = mimeType;
	}
	
	/**
	 * <p>Accessor for {@link #mimeType}.
	 * 
	 * @return {@link #mimeType}
	 */
	public String getMimeType() {
		
		return this.mimeType;
	}

	/**
	 * <p>Accessor for {@link #mimeType}.
	 * 
	 * @return {@link #mimeType}
	 */
	@Override
	public String toString() {
		
		return getMimeType();
	}
}
