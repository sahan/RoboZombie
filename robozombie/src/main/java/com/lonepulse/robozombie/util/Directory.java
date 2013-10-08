package com.lonepulse.robozombie.util;

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
 * <p>This interface determines the policy of a <i>directory service</i> which 
 * can register and retrieve entities by their {@link Class} representations.
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface Directory<KEY, VALUE> {

	
	/**
	 * <p>Adds an entry to the class directory. If an entry is already defined 
	 * with this {@link Class} <i>the existing entry is retained</i> and the 
	 * <i>new entry is silently ignored</i>.  
	 * 
	 * @param entryKey
	 * 			the {@link Class} representation used as the <b>key</b>
	 * 
	 * @param entryValue
	 * 			the entity instance to be registered in the directory
	 * 
	 * @return the new entry value which was added
	 * 
	 * <br><br>
	 * @since 1.1.1
	 */
	public VALUE put(KEY entryKey, VALUE entryValue);
	
	/**
	 * <p>Adds an entry to the class directory. If an entry is already defined 
	 * with this {@link Class} <i>the existing entity is <b>replaced</b> and 
	 * <b>returned</b></i>.  
	 * 
	 * @param entryKey
	 * 			the {@link Class} representation used as the <b>key</b>
	 * 
	 * @param entryValue
	 * 			the entity instance to be registered in the directory
	 * 
	 * @return the previous entry's value
	 * <br><br>
	 * @since 1.1.1
	 */
	public VALUE post(KEY entryKey, VALUE entryValue);

	/**
	 * <p>Retrieves an entry from the directory using the {@link Class} key.
	 * 
	 * @param entryKey
	 * 			the {@link Class} which identifies the entity to retrieve
	 * 
	 * @return the entry <b>value</b> or <b>null</b> if the {@link Class} key is missing
	 * <br><br>
	 * @since 1.1.1
	 */
	public VALUE get(KEY entryKey);
	
	/**
	 * <p>Deletes an entry from the directory using the {@link Class} key.
	 * 
	 * @param entryKey
	 * 			the {@link Class} which identifies the entity to delete
	 * 
	 * @return the deleted entry <b>value</b> or <b>null</b> if the {@link Class} key is missing
	 * <br><br>
	 * @since 1.1.1
	 */
	public VALUE delete(KEY entryKey);
}
