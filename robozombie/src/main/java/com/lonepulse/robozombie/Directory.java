package com.lonepulse.robozombie;

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
 * <p>This contract defines a naming service which can be used for binding instances of a given 
 * type under an identifier and <i>looking them up</i> using the identifier at a later period.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public interface Directory<NAME, OBJECT> {

	/**
	 * <p>Retains the given object under the specified name. If a binding already exists for the 
	 * specified name the given object is ignored and the bound instance is returned. The object 
	 * can  be retrieved via {@link #lookup(Object)}.</p>
	 * 
	 * @param name
	 * 			the identifier to which the given object instance is bound
	 * <br><br>
	 * @param object
	 * 			the object instance to be retained under the specified identifier
	 * <br><br>
	 * @return object
	 * 			the object which was provided; else the previous instance if a binding existed   
	 * <br><br>
	 * @since 1.3.0
	 */
	OBJECT bind(NAME name, OBJECT object);
	
	/**
	 * <p>Retrieves the object which was bound under the given identifier. If no binding exists 
	 * for the given identifier, {@code null} is returned.</p>
	 * 
	 * @param name
	 * 			the identifier to which the required object is bound
	 * <br><br>
	 * @return the bound object instance; else {@code null} if no binding exists
	 * <br><br>
	 * @since 1.3.0
	 */
	OBJECT lookup(NAME name);
}
