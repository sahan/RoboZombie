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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.lonepulse.robozombie.inject.Zombie;


/**
 * <p>Performs <b>Unit Testing</b> on the {@link Zombie} and its injection capabilities.
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
	 * <p>An instance of {@link BasicMockService} whose endpoint dependencies are satisfied 
	 * by injection.
	 */
	private BasicMockService propertyInjectedService;
	
	/**
	 * <p>An instance of {@link BasicMockService} which is instantiated by injecting all of 
	 * its endpoint dependencies. 
	 */
	private BasicMockService constructorInjectedService;
	
	
	/**
	 * <p>An instance of {@link FallbackMockService} which is intended to be instantiated with 
	 * all dependences satisfied and yet falls back to injection on an instance created using 
	 * the default constructor. 
	 */
	private FallbackMockService fallbackInjectedService;
	
	
	/**
	 * <p>Sets up the test case by performing endpoint injection on {@link #propertyInjectedService} 
	 * and {@link #constructorInjectedService}.
	 * 
	 * @throws Exception
	 * 			if test terminated with an error 
	 */
	@Before
	public void setUp() throws Exception {

		//perform property and setter injection
		propertyInjectedService = new BasicMockService();
		Zombie.infect(propertyInjectedService);
		
		//perform constructor injection
		constructorInjectedService = Zombie.infect(BasicMockService.class);
		
		//attempt constructor injection knowing that it will fail
		fallbackInjectedService = Zombie.infect(FallbackMockService.class);
	}
	
	/**
	 * Test method for {@link com.lonepulse.robozombie.inject.Zombie#infect(Object)}.
	 */
	@Test
	public final void testPropertyInjection() {
		
		assertNotNull(propertyInjectedService.getConstructedMockEndpoint());
		assertNotNull(propertyInjectedService.getDefaultMockEndpoint());
		assertNotNull(propertyInjectedService.getForcedPrivateMockEndpoint());
		assertNotNull(propertyInjectedService.getPrivateMockEndpoint());
		assertNotNull(propertyInjectedService.getProtectedMockEndpoint());
		assertNotNull(propertyInjectedService.getPublicMockEndpoint());
	}
	
	/**
	 * Test method for {@link com.lonepulse.robozombie.inject.Zombie#infect(java.lang.Class)}.
	 */
	@Test
	public final void testConstructorInstantiation() {
		
		assertNotNull(constructorInjectedService);
		assertNotNull(constructorInjectedService.getConstructedMockEndpoint());
		assertNotNull(constructorInjectedService.getDefaultMockEndpoint());
		assertNotNull(constructorInjectedService.getForcedPrivateMockEndpoint());
		assertNotNull(constructorInjectedService.getPrivateMockEndpoint());
		assertNotNull(constructorInjectedService.getProtectedMockEndpoint());
		assertNotNull(constructorInjectedService.getPublicMockEndpoint());
	}
	
	/**
	 * Test method for fallback from constructor to property injection.
	 */
	@Test
	public final void testFallbackInjection() {
		
		assertNotNull(fallbackInjectedService.getMockEndpoint());
	}
	
	/**
	 * Test method for a fallback error from constructor to property injection.
	 */
	@Test
	public final void testFallbackErrorInjection() {
		
		assertNull(Zombie.infect(FallbackErrorMockService.class));
	}
}
