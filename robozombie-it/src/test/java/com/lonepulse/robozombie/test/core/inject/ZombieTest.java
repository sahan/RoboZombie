package com.lonepulse.robozombie.test.core.inject;

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

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.lonepulse.robozombie.core.inject.com;
import com.lonepulse.robozombie.inject.Zombie;
import com.lonepulse.robozombie.test.service.MockService;


/**
 * <p>Performs <b>Unit Testing</b> on the {@link Zombie}.
 * 
 * @category test
 * <br><br>
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class ZombieTest {
	

	/**
	 * <p>A typical {@link MockService} instance.
	 */
	private static MockService mockService;
	
	/**
	 * <p>An {@link MockService} instance instantiated via the {@link Zombie}. 
	 */
	private static MockService mockServiceInstantiated;
	
	
	/**
	 * <p>Sets up the test case by performing endpoint injection on 
	 * {@link #mockService} and {@link #mockServiceInstantiated}.
	 * 
	 * @throws java.lang.Exception
	 * 			if the setup failed
	 */
	@BeforeClass
	public static void setUp() throws Exception {

		//perform property and setter injection
		mockService = new MockService();
		Zombie.infect(mockService);
		
		//perform constructor injection
		mockServiceInstantiated = Zombie.infect(MockService.class);
	}
	

	/**
	 * Test method for {@link com.lonepulse.zombielink.core.inject.Zombie#infect(java.lang.Object)} 
	 * with forced private endpoint injections.
	 */
	@Test
	public final void testForcedPrivateEndpointInjection() {
		
		assertNotNull(mockService.getForcedPrivateMockEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.zombielink.core.inject.Zombie#infect(java.lang.Object)} 
	 * with package private endpoints injections.
	 */
	@Test
	public final void testDefaultEndpointInjection() {
		
		assertNotNull(mockService.getDefaultMockEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.zombielink.core.inject.Zombie#infect(java.lang.Object)} 
	 * with protected endpoints injections.
	 */
	@Test
	public final void testProtectedEndpointInjection() {
		
		assertNotNull(mockService.getProtectedMockEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.zombielink.core.inject.Zombie#infect(java.lang.Object)} 
	 * with public endpoints injections.
	 */
	@Test
	public final void testPublicEndpointInjection() {
		
		assertNotNull(mockService.getPublicMockEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.zombielink.core.inject.Zombie#infect(java.lang.Object)} 
	 * for injection via the setter of a private endpoint.
	 */
	@Test
	public final void testMutatorInjection() {
		
		assertNotNull(mockService.getPrivateMockEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.zombielink.core.inject.Zombie#infect(java.lang.Object)} 
	 * for injection via the constructor.
	 */
	@Test
	public final void testConstructorInjection() {
		
		assertNotNull(mockService.getConstructedMockEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.zombielink.core.inject.Zombie#infect(java.lang.Class)}.
	 */
	@Test
	public final void testInjecteeInstantiation() {
		
		assertNotNull(mockServiceInstantiated);
		assertNotNull(mockServiceInstantiated.getConstructedMockEndpoint());
		assertNotNull(mockServiceInstantiated.getDefaultMockEndpoint());
		assertNotNull(mockServiceInstantiated.getForcedPrivateMockEndpoint());
		assertNotNull(mockServiceInstantiated.getPrivateMockEndpoint());
		assertNotNull(mockServiceInstantiated.getProtectedMockEndpoint());
		assertNotNull(mockServiceInstantiated.getPublicMockEndpoint());
	}
}
