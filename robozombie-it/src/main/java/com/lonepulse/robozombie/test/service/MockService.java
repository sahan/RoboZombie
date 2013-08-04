package com.lonepulse.robozombie.test.service;

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


import com.lonepulse.robozombie.core.annotation.Bite;
import com.lonepulse.robozombie.core.inject.Zombie;
import com.lonepulse.robozombie.test.endpoint.MockEndpoint;

/**
 * <p>Emulates a service which works with an {@link MockEndpoint}.</p>
 * 
 * <p>Contains serveral {@link MockEndpoint} members with various 
 * accessibility qualifiers to test the {@link Zombie}'s property injection 
 * capabilities.</p>
 * 
 * <p>Also contains a mutator and a parameterized contructor to test 
 * setter and contructor injection.</p>
 * 
 * @category test
 * <br><br> 
 * @version 1.0.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class MockService {
	
	
	/** <p>A public instance of {@link MockEndpoint} */
	@Bite
	public MockEndpoint publicMockEndpoint;
	
	/** <p>A protected instance of {@link MockEndpoint} */
	@Bite
	protected MockEndpoint protectedMockEndpoint;
	
	/** <p>A package private instance of {@link MockEndpoint} */
	@Bite
	MockEndpoint defaultMockEndpoint;
	
	/** 
	 * <p>A private instance of {@link MockEndpoint} which is 
	 * to be injected via it's mutator.
	 */
	@Bite
	private MockEndpoint privateMockEndpoint;
	
	/**  
	 * <p>A private instance of {@link MockEndpoint} which is 
	 * to be injected via the constructor.
	 */
	@Bite
	private MockEndpoint constructedMockEndpoint;
	
	/**  
	 * <p>A private instance of {@link MockEndpoint} which is 
	 * to be injected by changing the <b>accessibility properties</b>.
	 */
	@Bite
	private MockEndpoint forcedPrivateMockEndpoint;
	
	
	/**
	 * <p>Default constructor.
	 */
	public MockService() {}
	
	/**
	 * <p>Facilitates constructor injection. 
	 */
	@Bite
	public MockService(MockEndpoint icndbEndpoint) {
		
		this.constructedMockEndpoint = icndbEndpoint;
	}

	/**
	 * <p>Accessor for publicMockEndpoint.
	 *
	 * @return the publicMockEndpoint
	 */
	public MockEndpoint getPublicMockEndpoint() {
		return publicMockEndpoint;
	}

	/**
	 * <p>Mutator for publicMockEndpoint.
	 *
	 * @param publicMockEndpoint 
	 *			the publicMockEndpoint to set
	 */
	public void setPublicMockEndpoint(MockEndpoint publicMockEndpoint) {
		this.publicMockEndpoint = publicMockEndpoint;
	}

	/**
	 * <p>Accessor for protectedMockEndpoint.
	 *
	 * @return the protectedMockEndpoint
	 */
	public MockEndpoint getProtectedMockEndpoint() {
		return protectedMockEndpoint;
	}

	/**
	 * <p>Mutator for protectedMockEndpoint.
	 *
	 * @param protectedMockEndpoint 
	 *			the protectedMockEndpoint to set
	 */
	public void setProtectedMockEndpoint(MockEndpoint protectedMockEndpoint) {
		this.protectedMockEndpoint = protectedMockEndpoint;
	}

	/**
	 * <p>Accessor for defaultMockEndpoint.
	 *
	 * @return the defaultMockEndpoint
	 */
	public MockEndpoint getDefaultMockEndpoint() {
		return defaultMockEndpoint;
	}

	/**
	 * <p>Mutator for defaultMockEndpoint.
	 *
	 * @param defaultMockEndpoint 
	 *			the defaultMockEndpoint to set
	 */
	public void setDefaultMockEndpoint(MockEndpoint defaultMockEndpoint) {
		this.defaultMockEndpoint = defaultMockEndpoint;
	}

	/**
	 * <p>Accessor for privateMockEndpoint.
	 *
	 * @return the privateMockEndpoint
	 */
	public MockEndpoint getPrivateMockEndpoint() {
		return privateMockEndpoint;
	}

	/**
	 * <p>Mutator for privateMockEndpoint.
	 *
	 * @param privateMockEndpoint 
	 *			the privateMockEndpoint to set
	 */
	public void setPrivateMockEndpoint(MockEndpoint privateMockEndpoint) {
		this.privateMockEndpoint = privateMockEndpoint;
	}

	/**
	 * <p>Accessor for constructedMockEndpoint.
	 *
	 * @return the constructedMockEndpoint
	 */
	public MockEndpoint getConstructedMockEndpoint() {
		return constructedMockEndpoint;
	}

	/**
	 * <p>Mutator for constructedMockEndpoint.
	 *
	 * @param constructedMockEndpoint 
	 *			the constructedMockEndpoint to set
	 */
	public void setConstructedMockEndpoint(MockEndpoint constructedMockEndpoint) {
		this.constructedMockEndpoint = constructedMockEndpoint;
	}

	/**
	 * <p>Accessor for forcedPrivateMockEndpoint.
	 *
	 * @return the forcedPrivateMockEndpoint
	 */
	public MockEndpoint getForcedPrivateMockEndpoint() {
		return forcedPrivateMockEndpoint;
	}

	/**
	 * <p>Mutator for forcedPrivateMockEndpoint.
	 *
	 * @param forcedPrivateMockEndpoint 
	 *			the forcedPrivateMockEndpoint to set
	 */
	public void setForcedPrivateMockEndpoint(MockEndpoint forcedPrivateMockEndpoint) {
		this.forcedPrivateMockEndpoint = forcedPrivateMockEndpoint;
	}
}
