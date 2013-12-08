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
 * <p>A generic contract for validating metadata and runtime information. Each {@link Validator} 
 * operates on a given context which provides information for executing the validation rules.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface Validator<CONTEXT extends Object> {

	/**
	 * <p>Accepts a given context and validates the contained information using predetermined rules. 
	 * A validation failure is signaled at runtime using an instance of {@link ValidationFailedException}. 
	 * Custom extensions of {@link ValidationFailedException} may be used for failure specificity.</p>
	 * 
	 * @param context
	 * 			the context which provides the information to be validated
	 * <br><br>
	 * @throws ValidationFailedException
	 * 			(or any extension) if validation failed for the given context
	 * <br><br>
	 * @since 1.2.4
	 */
	void validate(CONTEXT context);
}
