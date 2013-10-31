package com.lonepulse.robozombie;

import java.util.Map;

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
 * <p>This contract specifies a generic policy for an entity factory. Its takes the raw materials 
 * specified by <i>INPUT</i> to manufacture an entity of the type specified by <i>OUTPUT</i> and 
 * expects a {@link Throwable} of the type specified by <i>FAILURE</i> to be thrown in case of a 
 * manufacturing error.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @version 1.2.4
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface GenericFactory<INPUT extends Object, OUTPUT extends Object, FAILURE extends Throwable> {

	/**
	 * <p>Manufactures a new instance of the <i>OUTPUT</i> product using no <i>INPUT</i>. This method 
	 * could be used to create the entity with its default components and properties. Such details must 
	 * be clearly documented in the implementation.</p> 
	 * 			
	 * @return a <b>new instance</b> of the <i>OUTPUT</i>
	 * <br><br>
	 * @throws the type of <i>FAILURE</i> to expect from a manufacturing error 
	 * <br><br>
	 * @since 1.2.4
	 */
	public abstract OUTPUT newInstance() throws FAILURE;
	
	/**
	 * <p>Takes a {@link Map} of raw materials (keyed by {@link String}s) as the <i>INPUT</i> and 
	 * manufactures the <i>OUTPUT</i>. All implementations must check for the <b>existence</b> of 
	 * the <i>INPUT</i> and validate it before proceeding with the manufacture.</p>
	 * 
	 * @param inputMap
	 * 			the <i>INPUT</i> {@link Map} whose entries are used to manufacture the <i>OUTPUT</i>
	 * <br><br>			
	 * @return a <b>new instance</b> of the <i>OUTPUT</i>
	 * <br><br>
	 * @throws the type of <i>FAILURE</i> to expect from a manufacturing error
	 * <br><br>
	 * @since 1.2.4
	 */
	public abstract OUTPUT newInstance(Map<String, INPUT> inputMap) throws FAILURE;
	
	/**
	 * <p>Takes a single <i>INPUT</i> as an essential raw material to manufacture the <i>OUTPUT</i> 
	 * and produces a new instance of it. It <i>may</i> take additional <i>INPUT</i>s which are 
	 * required for the manufacturing process. All implementations must check for the <b>existence</b> 
	 * of the <i>essential INPUT</i> and validate it before proceeding with the manufacture.</p>
	 * 
	 * @param input
	 * 			an <i>INPUT</i> which is essential to the manufacture of the <i>OUTPUT</i> without 
	 * 			which the manufacturing process will fail  
	 * 
	 * @param inputs
	 * 			further <i>INPUT</i>s which can be used in the manufacturing of the <i>OUTPUT</i> 
	 * 			but are not <i>essential</i> to the process
	 * <br><br>			
	 * @return a <b>new instance</b> of the <i>OUTPUT</i>
	 * <br><br>
	 * @throws the type of <i>FAILURE</i> to expect from a manufacturing error
	 * <br><br>
	 * @since 1.2.4
	 */
	public abstract OUTPUT newInstance(INPUT input, INPUT... inputs) throws FAILURE;
}
