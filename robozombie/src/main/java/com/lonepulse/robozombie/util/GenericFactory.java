package com.lonepulse.robozombie.util;

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
 * <p>This contract specifies the policy for a generic entity factory.
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface GenericFactory<Input extends Object, Output extends Object> {

	/**
	 * <p>Builds a new instance of the output product. 
	 * 			
	 * @return a <b>new instance</b> of the the product
	 * 
	 * @since 1.1.0
	 */
	public abstract Output newInstance();
	
	/**
	 * <p>Takes a map of inputs (keyed by strings) as raw material and 
	 * builds the output which is designated as this factory's product.
	 * 
	 * @param input
	 * 			the raw material or key from which the product 
	 * 			is created 
	 * 
	 * @param inputs
	 * 			further raw materials from which the product 
	 * 			is created
	 * 			
	 * @return a <b>new instance</b> of the the product
	 * 
	 * @since 1.1.1
	 */
	public abstract Output newInstance(Map<String, Input> inputMap);
	
	/**
	 * <p>Takes an input as raw material or a key and builds 
	 * the output which is designated as this factory's product.
	 * 
	 * @param input
	 * 			the raw material or key from which the product 
	 * 			is created 
	 * 
	 * @param inputs
	 * 			further raw materials from which the product 
	 * 			is created
	 * 			
	 * @return a <b>new instance</b> of the the product
	 * 
	 * @since 1.1.0
	 */
	public abstract Output newInstance(Input input, Input... inputs);
}
