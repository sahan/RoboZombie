package com.lonepulse.robozombie.test.processor;

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
import com.lonepulse.robozombie.processor.ProxyInvocationConfiguration;
import com.lonepulse.robozombie.processor.validator.Validators;

/**
 * <p>Performs unit testing for <b>endpoint validation</b>.
 * 
 * @category test
 * <br><br>
 * @version 1.1.1
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class EndpointValidationTest {

	/**
	 * <p>Represents an invalid endpoint definition with a missing @{@link Endpoint} annotation.
	 */
	private interface MissingEndpointAnnotationEndpoint {}
	
	/**
	 * <p>Represents an invalid endpoint definition with missing host information.
	 */
	@Endpoint
	private interface MissingEndpointHostEndpoint {}
	
	/**
	 * <p>The {@link Class} of a package-private exception which is thrown due to a missing 
	 * @{@link Endpoint} annotation on an endpoint definition. 
	 */
	private Class<Throwable> missingEndpointAnnotationExceptionClass;
	
	/**
	 * <p>The {@link Class} of a package-private exception which is thrown due to missing 
	 * host information on an endpoint definition. 
	 */
	private Class<Throwable> missingEndpointHostExceptionClass;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	
	/**
	 * <p>Sets up the test case by reflectively accessing the {@link Class}es of the expected exceptions.
	 * 
	 * @throws java.lang.Exception
	 * 			if reflection falied to retrieve the necessary {@link Class}es
	 */
	@Before @SuppressWarnings("unchecked") //safe case to Class<Throwable> from known exceptions
	public void setUp() throws Exception {
		
		missingEndpointAnnotationExceptionClass = (Class<Throwable>) 
			Class.forName("com.lonepulse.robozombie.processor.validator.MissingEndpointAnnotationException");
		
		missingEndpointHostExceptionClass = (Class<Throwable>) 
			Class.forName("com.lonepulse.robozombie.processor.validator.MissingEndpointHostException");
	}
	
	/**
	 * <p>Tests endpoint validation for a missing @{@link Endpoint} annotation.
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testMissingEndpointAnnotation() {
		
		expectedException.expectCause(Is.isA(missingEndpointAnnotationExceptionClass));

		ProxyInvocationConfiguration.Builder builder = new  ProxyInvocationConfiguration.Builder()
		.setEndpointClass(MissingEndpointAnnotationEndpoint.class);
		
		Validators.ENDPOINT.validate(builder.build());
	}
	
	/**
	 * <p>Tests endpoint validation for a missing host in an @{@link Endpoint} annotation.
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testMissingEndpointHost() {
		
		expectedException.expectCause(Is.isA(missingEndpointHostExceptionClass));
		
		ProxyInvocationConfiguration.Builder builder = new  ProxyInvocationConfiguration.Builder()
		.setEndpointClass(MissingEndpointHostEndpoint.class);
		
		Validators.ENDPOINT.validate(builder.build());
	}
}
