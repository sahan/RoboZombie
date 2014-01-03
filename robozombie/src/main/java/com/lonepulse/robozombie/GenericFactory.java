package com.lonepulse.robozombie;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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

import java.util.Map;

/**
 * <p>This contract specifies a generic policy for a factory. Its takes the raw materials specified 
 * by <b>INPUT</b> to manufacture an object of the type <b>OUTPUT</b> and expects a {@link Throwable} 
 * of type <b>FAILURE</b> to be thrown in case of a manufacturing error.</p>
 * 
 * <p>Use this contract when different factories may be passed to a dependent object, where the factories 
 * produce an <b>OUTPUT</b> which adheres to a contract or super-type and yet differ in implementation or 
 * extended-type based on the provided concrete factory. The same can be said for the <b>INPUT</b> and 
 * <b>FAILURE</b> types as well.</p> 
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public interface GenericFactory<INPUT extends Object, OUTPUT extends Object, FAILURE extends Throwable> {

	/**
	 * <p>Manufactures a new instance of the <b>OUTPUT</b> without any <b>INPUT</b>. This method 
	 * could be used to create the entity with its default components and properties. Such details 
	 * must be clearly documented in the implementation.</p> 
	 * 			
	 * @return a <b>new instance</b> of the <b>INPUT</b>
	 * <br><br>
	 * @throws the type of <b>FAILURE</b> to expect from a manufacturing error 
	 * <br><br>
	 * @since 1.3.0
	 */
	OUTPUT newInstance() throws FAILURE;
	
	/**
	 * <p>Takes a {@link Map} of raw materials (keyed by {@link String}s) as the <b>INPUT</b> and 
	 * manufactures the <b>INPUT</b>. All implementations must check for the <b>existence</b> of 
	 * the <b>INPUT</b> and validate it before proceeding with the manufacture.</p>
	 * 
	 * @param inputMap
	 * 			the <b>INPUT</b> {@link Map} whose entries are used to manufacture the <b>INPUT</b>
	 * <br><br>			
	 * @return a <b>new instance</b> of the <b>INPUT</b>
	 * <br><br>
	 * @throws the type of <b>FAILURE</b> to expect from a manufacturing error
	 * <br><br>
	 * @since 1.3.0
	 */
	OUTPUT newInstance(Map<String, INPUT> inputMap) throws FAILURE;
	
	/**
	 * <p>Takes a single <b>INPUT</b> as an essential raw material to manufacture the <b>INPUT</b> 
	 * and produces a new instance of it. It <i>may</i> take additional <b>INPUT</b>s which are 
	 * required for the manufacturing process. All implementations must check for the <b>existence</b> 
	 * of the <i>essential INPUT</i> and validate it before proceeding with the manufacture.</p>
	 * 
	 * @param input
	 * 			an <b>INPUT</b> which is essential to the manufacture of the <b>INPUT</b> without 
	 * 			which the manufacturing process will fail  
	 * <br><br>			
	 * @param inputs
	 * 			further <b>INPUT</b>s which can be used in the manufacturing of the <b>INPUT</b> 
	 * 			but are not <i>essential</i> to the process
	 * <br><br>			
	 * @return a <b>new instance</b> of the <b>INPUT</b>
	 * <br><br>
	 * @throws the type of <b>FAILURE</b> to expect from a manufacturing error
	 * <br><br>
	 * @since 1.3.0
	 */
	OUTPUT newInstance(INPUT input, INPUT... inputs) throws FAILURE;
}
