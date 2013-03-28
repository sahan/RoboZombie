package com.lonepulse.robozombie.test.model;

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


import java.io.Serializable;
import java.util.List;

import com.lonepulse.robozombie.test.endpoint.ICNDBEndpoint;

/**
 * <p>This entity represents a <b>response</b> which is received from the 
 * ICNDB API via the {@link ICNDBEndpoint}.
 * 
 * @category test
 * <br><br> 
 * @version 1.0.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class ICNDBResponse implements Serializable {


	private static final long serialVersionUID = -1575987881009903297L;

	/**
	 * <p>An indication as to whether the request was executed successfully.</p>
	 * <p>A value of {@code success} implies a successful execution.</p>
	 */
	private String type;
	
	/**
	 * <p>The {@link NorrisJoke} which was requested. 
	 */
	private NorrisJoke value;
	
	/**
	 * <p>The categories, such as <b>nerdy</b> and <b>explicit</b>, to which 
	 * this {@link NorrisJoke} belongs.
	 */
	private List<String> categories;


	/**
	 * <p>Accessor for type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * <p>Mutator for type.
	 *
	 * @param type 
	 *			the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * <p>Accessor for value.
	 *
	 * @return the value
	 */
	public NorrisJoke getValue() {
		return value;
	}

	/**
	 * <p>Mutator for value.
	 *
	 * @param value 
	 *			the value to set
	 */
	public void setValue(NorrisJoke value) {
		this.value = value;
	}

	/**
	 * <p>Accessor for categories.
	 *
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * <p>Mutator for categories.
	 *
	 * @param categories 
	 *			the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("ICNDBResponse [type=");
		builder.append(type);
		builder.append(", value=");
		builder.append(value);
		builder.append(", categories=");
		builder.append(categories);
		builder.append("]");
		
		return builder.toString();
	}
}
