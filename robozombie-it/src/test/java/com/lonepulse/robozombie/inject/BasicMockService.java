package com.lonepulse.robozombie.inject;

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

/**
 * <p>Emulates a service which works with an {@link MockEndpoint}.</p>
 * 
 * <p>Contains several {@link MockEndpoint} members with various accessibility qualifiers to test the 
 * {@link Zombie}'s property injection capabilities.</p>
 * 
 * <p>Also contains a mutator and a parameterized constructor to test setter and constructor injection.</p>
 * 
 * @category test
 * <br><br> 
 * @version 1.0.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class BasicMockService {
	
	
	@Bite
	private static MockEndpoint staticMockEndpoint;
	
	@Bite
	public MockEndpoint publicMockEndpoint;
	
	@Bite
	protected MockEndpoint protectedMockEndpoint;
	
	@Bite
	MockEndpoint defaultMockEndpoint;
	
	@Bite
	private MockEndpoint privateMockEndpoint;
	
	@Bite
	private MockEndpoint forcedPrivateMockEndpoint;
	
	
	public MockEndpoint getPublicMockEndpoint() {
		
		return publicMockEndpoint;
	}

	public MockEndpoint getProtectedMockEndpoint() {
		
		return protectedMockEndpoint;
	}
	
	public MockEndpoint getDefaultMockEndpoint() {
		
		return defaultMockEndpoint;
	}

	public MockEndpoint getPrivateMockEndpoint() {
		
		return privateMockEndpoint;
	}

	public void setPrivateMockEndpoint(MockEndpoint privateMockEndpoint) {
		
		this.privateMockEndpoint = privateMockEndpoint;
	}

	public MockEndpoint getForcedPrivateMockEndpoint() {
		
		return forcedPrivateMockEndpoint;
	}

	public static MockEndpoint getStaticMockEndpoint() {
		
		return staticMockEndpoint;
	}
}
