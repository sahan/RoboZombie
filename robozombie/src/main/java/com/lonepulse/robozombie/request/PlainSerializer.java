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

import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>An {@link AbstractSerializer} which simply invokes {@code String#valueOf(...)} on a model.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public class PlainSerializer extends AbstractSerializer<Object, String> {

	
	/**
	 * <p>Creates a new {@link PlainSerializer} and registers {@code Class<String>} as the output 
	 * content-type which results from serialization.</p>
	 *
	 * @since 1.3.0
	 */
	public PlainSerializer() {
	
		super(String.class);
	}
	
	/**
	 * <p>Converts a model to a {@link String} by simply invoking {@code String#valueOf(...)} on it.</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which is used to discover further information regarding 
	 * 			the proxy invocation
	 * <br><br>
	 * @param input
	 * 			the model to be serialized into a {@link String}
	 * <br><br>
	 * @return the {@link String} which represents the provided input model, else an empty string if the 
	 * 		   provided input was {@code null}
	 * <br><br>
	 * @since 1.3.0
	 */
	@Override
	public String serialize(InvocationContext context, Object input) {

		return input == null? "" :String.valueOf(input);
	}
}
