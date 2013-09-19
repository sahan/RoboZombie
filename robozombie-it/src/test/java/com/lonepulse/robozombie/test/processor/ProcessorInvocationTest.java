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

import org.apache.http.client.methods.HttpGet;
import org.hamcrest.core.Is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.lonepulse.robozombie.processor.ChainCreationException;
import com.lonepulse.robozombie.processor.Processor;
import com.lonepulse.robozombie.processor.ProcessorChainFactory;
import com.lonepulse.robozombie.processor.ProcessorChainLink;
import com.lonepulse.robozombie.processor.Processors;
import com.lonepulse.robozombie.processor.ProxyInvocationConfiguration;

/**
 * <p>Performs unit testing for <i>illegal invocation</i> of <b>processor chains</b>.
 * 
 * @category test
 * <br><br>
 * @version 1.1.1
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class ProcessorInvocationTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	
	/**
	 * <p>Tests {@link Processors#REQUEST} invocation with an inadequate argument count.
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testRequestArgumentCount() {
		
		expectedException.expectCause(Is.isA(IllegalArgumentException.class));
		Processors.REQUEST.run(new Object());
	}
	
	/**
	 * <p>Tests {@link Processors#REQUEST} invocation with an illegal request base type.
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testRequestArgumentRequestBaseType() {
		
		expectedException.expectCause(Is.isA(IllegalArgumentException.class));
		Processors.REQUEST.run(new Object(), new ProxyInvocationConfiguration.Builder().build());
	}
	
	/**
	 * <p>Tests {@link Processors#REQUEST} invocation with an illegal proxy configuration type.
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testRequestArgumentProxyConfigType() {
		
		expectedException.expectCause(Is.isA(IllegalArgumentException.class));
		Processors.REQUEST.run(new HttpGet(), new Object());
	}
	
	/**
	 * <p>Tests {@link Processors#RESPONSE} invocation with an inadequate argument count.
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testResponseArgumentCount() {
		
		expectedException.expectCause(Is.isA(IllegalArgumentException.class));
		Processors.RESPONSE.run(new Object());
	}
	
	/**
	 * <p>Tests {@link Processors#RESPONSE} invocation with an illegal proxy configuration type.
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testResponseArgumentProxyConfigType() {
		
		expectedException.expectCause(Is.isA(IllegalArgumentException.class));
		Processors.RESPONSE.run(new HttpGet(), new Object());
	}
	
	/**
	 * <p>Tests processor-chain construction using a {@link ProcessorChainFactory} by supplying 
	 * a {@code null} {@link ProcessorChainLink} root. 
	 *  
	 * @since 1.2.4
	 */
	@Test @SuppressWarnings("unchecked") //an intentional array of NULLs
	public final void testProcessorChainFactoryRoot() {
		
		expectedException.expect(IllegalArgumentException.class);
		new ProcessorChainFactory<Void, Throwable>().newInstance(null, null, null);
	}
	
	/**
	 * <p><p>Tests processor-chain construction using a {@link ProcessorChainFactory} by supplying 
	 * a {@code null} {@link ProcessorChainLink} successor. 
	 *  
	 * @since 1.2.4
	 */
	@Test @SuppressWarnings("unchecked") //an intentional array of NULLs
	public final void testProcessorChainFactorySuccessor() {
		
		expectedException.expect(ChainCreationException.class);
		new ProcessorChainFactory<Void, Throwable>().newInstance(new Processor<Void, Throwable>() {

			public Void run(Object... args) throws Throwable { 
				return null; 
			}
		}, null, null);
	}
}
