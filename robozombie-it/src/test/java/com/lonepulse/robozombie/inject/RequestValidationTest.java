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

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Request;

/**
 * <p>Performs unit testing for <b>request validation</b>.
 * 
 * @category test
 * <br><br>
 * @version 1.1.1
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class RequestValidationTest {

	/**
	 * <p>Represents an endpoint definition with a missing @{@link Request} annotation on 
	 * one of its request definitions.
	 */
	@Endpoint(host = "0.0.0.0", port = "8080")
	private interface MissingRequestAnnotationEndpoint {
		
		String invalidRequest();
	}
	
	/**
	 * <p>The {@link Class} of a package-private exception which is thrown due to a missing @{@link Request} 
	 * annotation on an endpoint request definition. 
	 */
	private Class<Throwable> strayRequestException;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	
	/**
	 * <p>Sets up the test case by reflectively accessing the {@link Class}es of the expected exceptions.
	 * 
	 * @throws java.lang.Exception
	 * 			if reflection failed to retrieve the necessary {@link Class}es
	 */
	@Before @SuppressWarnings("unchecked") //safe case to Class<Throwable> from a known exception
	public void setUp() throws Exception {
		
		strayRequestException = (Class<Throwable>) 
			Class.forName("com.lonepulse.robozombie.inject.StrayEndpointRequestException");
	}
	
	/**
	 * <p>Tests endpoint validation for a missing host in an @{@link Endpoint} annotation.
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testMissingRequestAnnotation() {
		
		expectedException.expect(Is.isA(strayRequestException));
		EndpointProxyFactory.INSTANCE.create(MissingRequestAnnotationEndpoint.class);
	}
}
