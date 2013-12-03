package com.lonepulse.robozombie.annotation;

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
 * <p>Allows a basic name and value pair to be provided as annotated metadata.</p>
 * 
 * @version 1.1.0
 * <br><br> 
 * @since 1.2.4
 * <br><br> 
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public @interface Param { 
	
	/**
	 * <p>The <b>name</b> of the name and value pair.</p>  
	 * 
	 * @return the name of the name and value pair
	 * <br><br>
	 * @since 1.2.4
	 */
	String name();
	
	/**
	 * <p>The <b>value</b> of the name and value pair.</p>  
	 * 
	 * @return the value of the name and value pair
	 * <br><br>
	 * @since 1.2.4
	 */
	String value();
}