package com.lonepulse.robozombie.test.inject;

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


import com.lonepulse.robozombie.annotation.Bite;
import com.lonepulse.robozombie.inject.Zombie;

/**
 * <p>Emulates a service on which constructor injection should be performed. It depends on an instance of 
 * {@link MockEndpoint} which is annotated with @{@link Bite}. However no constructor is exposed for injection. 
 * Injection falls back to property injection, however no injectable property is discovered. This service should 
 * be tested for an exceptional condition which <b>returns null</b> using {@link Zombie#infect(Class)}.</p>
 * 
 * @category test
 * <br><br>
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class FallbackErrorMockService {
	
	/** 
	 * <p>A private instance of {@link MockEndpoint} which is to be injected but is missing a @{@link Bite} annotation.
	 */
	private MockEndpoint mockEndpoint;
	
	
	/**
	 * <p>Prevents constructor injection by not declaring a constructor argument of type {@link MockEndpoint}.
	 * 
	 * @param nonEndpointArg
	 * 			represents a constructor argument which is not of type {@link MockEndpoint}
	 */
	@Bite
	public FallbackErrorMockService(Void nonEndpointArg) {}
	
	
	/**
	 * <p>Accessor for mockEndpoint.
	 *
	 * @return the mockEndpoint
	 */
	public MockEndpoint getMockEndpoint() {
		
		return mockEndpoint;
	}
}
