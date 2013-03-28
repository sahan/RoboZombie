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
import org.junit.runner.RunWith;

import com.lonepulse.robozombie.core.inject.Zombie;
import com.lonepulse.robozombie.test.endpoint.ICNDBEndpoint;
import com.lonepulse.robozombie.test.service.ICNDBService;
import com.xtremelabs.robolectric.RobolectricTestRunner;


/**
 * <p>Performs <b>Unit Testing</b> on the {@link Zombie}.
 * 
 * @category test
 * <br><br>
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@RunWith(RobolectricTestRunner.class)
public class ZombieTest {
	

	/**
	 * <p>A typical {@link ICNDBEndpoint} instance.
	 */
	private static ICNDBService icndbService;
	
	/**
	 * <p>An {@link ICNDBEndpoint} instance instantiated via the {@link Zombie}. 
	 */
	private static ICNDBService icndbServiceInstantiated;
	
	
	/**
	 * <p>Sets up the test case by performing endpoint injection on 
	 * {@link #icndbService} and {@link #icndbServiceInstantiated}.
	 * 
	 * @throws java.lang.Exception
	 * 			if the setup failed
	 */
	@BeforeClass
	public static void setUp() throws Exception {

		//perform property and setter injection
		icndbService = new ICNDBService();
		Zombie.infect(icndbService);
		
		//perform constructor injection
		icndbServiceInstantiated = Zombie.infect(ICNDBService.class);
	}
	

	/**
	 * Test method for {@link com.lonepulse.robozombie.core.inject.Zombie#infect(java.lang.Object)} 
	 * with forced private endpoint injections.
	 */
	@Test
	public final void testForcedPrivateEndpointInjection() {
		
		assertNotNull(icndbService.getForcedPrivateICNDBEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.robozombie.core.inject.Zombie#infect(java.lang.Object)} 
	 * with package private endpoints injections.
	 */
	@Test
	public final void testDefaultEndpointInjection() {
		
		assertNotNull(icndbService.getDefaultICNDDBEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.robozombie.core.inject.Zombie#infect(java.lang.Object)} 
	 * with protected endpoints injections.
	 */
	@Test
	public final void testProtectedEndpointInjection() {
		
		assertNotNull(icndbService.getProtectedICNDDBEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.robozombie.core.inject.Zombie#infect(java.lang.Object)} 
	 * with public endpoints injections.
	 */
	@Test
	public final void testPublicEndpointInjection() {
		
		assertNotNull(icndbService.getPublicICNDDBEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.robozombie.core.inject.Zombie#infect(java.lang.Object)} 
	 * for injection via the setter of a private endpoint.
	 */
	@Test
	public final void testMutatorInjection() {
		
		assertNotNull(icndbService.getPrivateICNDDBEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.robozombie.core.inject.Zombie#infect(java.lang.Object)} 
	 * for injection via the constructor.
	 */
	@Test
	public final void testConstructorInjection() {
		
		assertNotNull(icndbService.getConstructedICNDBEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.robozombie.core.inject.Zombie#infect(java.lang.Class)}.
	 */
	@Test
	public final void testInjecteeInstantiation() {
		
		assertNotNull(icndbServiceInstantiated);
		assertNotNull(icndbServiceInstantiated.getConstructedICNDBEndpoint());
		assertNotNull(icndbServiceInstantiated.getDefaultICNDDBEndpoint());
		assertNotNull(icndbServiceInstantiated.getForcedPrivateICNDBEndpoint());
		assertNotNull(icndbServiceInstantiated.getPrivateICNDDBEndpoint());
		assertNotNull(icndbServiceInstantiated.getProtectedICNDDBEndpoint());
		assertNotNull(icndbServiceInstantiated.getPublicICNDDBEndpoint());
	}
}
