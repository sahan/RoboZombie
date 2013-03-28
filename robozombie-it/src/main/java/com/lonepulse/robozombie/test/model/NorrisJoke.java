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

import com.lonepulse.robozombie.test.endpoint.ICNDBEndpoint;

/**
 * <p>This entity represents a <b>Chuck Norris Joke</b> retrieved from 
 * the ICNDB API via the {@link ICNDBEndpoint}.
 * 
 * @category test
 * <br><br> 
 * @version 1.0.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class NorrisJoke implements Serializable {

	
	private static final long serialVersionUID = -6967272216854601497L;

	private long id;
	private String joke;
	
	/**
	 * <p>Accessor for id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * <p>Mutator for id.
	 *
	 * @param id 
	 *			the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * <p>Accessor for joke.
	 *
	 * @return the joke
	 */
	public String getJoke() {
		return joke;
	}
	
	/**
	 * <p>Mutator for joke.
	 *
	 * @param joke 
	 *			the joke to set
	 */
	public void setJoke(String joke) {
		this.joke = joke;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NorrisJoke other = (NorrisJoke) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("NorrisJoke [id=");
		builder.append(id);
		builder.append(", joke=");
		builder.append(joke);
		builder.append("]");

		return builder.toString();
	}
}
