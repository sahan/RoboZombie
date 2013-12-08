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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


/**
 * <p>Performs <b>Unit Testing</b> on the {@link Zombie} and its infection capabilities.
 * 
 * @category test
 * <br><br>
 * @version 1.2.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@RunWith(RobolectricTestRunner.class)
public class ZombieTest {
	

	private BasicMockService mockService;
	
	private BasicMockService service1;
	private BasicMockService service2;
	private BasicMockService service3;
	
	private static SubMockService subMockService;
	

	@Before
	public void setUp() throws Exception {
		
		mockService = new BasicMockService();

		Zombie.infect(mockService);
		
		service1 = new BasicMockService();
		service2 = new BasicMockService();
		service3 = new BasicMockService();
		
		Zombie.infect(service1, service2, service3);
		
		subMockService = new SubMockService();
	}
	
	/**
	 * Test method for single injection using {@link Zombie#infect(Object, Object...)}
	 */
	@Test
	public final void testPropertyInjection() {
		
		assertNotNull(mockService.getDefaultMockEndpoint());
		assertNotNull(mockService.getForcedPrivateMockEndpoint());
		assertNotNull(mockService.getPrivateMockEndpoint());
		assertNotNull(mockService.getProtectedMockEndpoint());
		assertNotNull(mockService.getPublicMockEndpoint());
		assertNotNull(BasicMockService.getStaticMockEndpoint());
	}
	
	/**
	 * Test method for multiple injections using {@link Zombie#infect(Object, Object...)}
	 */
	@Test
	public final void testMultipleInjection() {
		
		assertNotNull(service1.getPrivateMockEndpoint());
		assertNotNull(service2.getPrivateMockEndpoint());
		assertNotNull(service3.getPrivateMockEndpoint());
	}
	
	/**
	 * Test method for endpoint injection on an object hierarchy.
	 */
	@Test
	public final void testHierarchicalInjection() {
		
		assertNotNull(subMockService.getSubEndpoint());
		assertNotNull(subMockService.getSuperEndpoint());
	}
	
	/**
	 * Test method for endpoint injection on an enum.
	 */
	@Test
	public final void testEnumInjection() {
		
		assertNotNull(EnumMockService.INSTANCE.getEndpoint());
		assertNotNull(EnumMockService.getStaticEndpoint());
	}
}
