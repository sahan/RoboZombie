package com.lonepulse.robozombie.validator;

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


import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.executor.HttpClientContract;

/**
 * <p>Specifies the contract for validating endpoints and their 
 * request methods. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface Validator<Result extends Object> {

	/**
	 * <p>Executes the given {@link HttpRequestBase} using a 
	 * suitable {@link HttpClientContract} implementation.
	 * 
	 * @param config
	 * 			the {@link ProxyInvocationConfiguration} which 
	 * 			contains the parameters to perform the validation
	 * 			
	 * @return the result of the validation 
	 * 
	 * @throws ValidationFailedException
	 * 			if the validation failed with the give parameters
	 * <br><br>
	 * @since 1.1.0
	 */
	Result validate(ProxyInvocationConfiguration config) throws ValidationFailedException;
}
