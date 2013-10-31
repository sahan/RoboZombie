package com.lonepulse.robozombie.processor;

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


import static com.lonepulse.robozombie.util.Assert.assertNotNull;

import com.lonepulse.robozombie.AbstractGenericFactory;

/**
 * <p>This is a concrete implementation of {@link AbstractGenericFactory} which creates a <b>processor-chain</b> 
 * using the given array of {@link Processor}s by linking them using {@link ProcessorChainLink}s. The sequence 
 * in the given array is preserved in the resulting chain.</p>
 * 
 * <p>Supports the following factory services:</p>
 * <ul>
 * 	<li>{@link ProcessorChainFactory#newInstance(Processor, Processor...)}</i>
 * </ul>
 * 
 * <p><b>Note</b> that all other service methods will throw an {@link UnsupportedOperationException}.<p>
 * <br><br>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class ProcessorChainFactory<RESULT, FAILURE extends Throwable> 
extends AbstractGenericFactory <Processor<RESULT, FAILURE>, ProcessorChainLink<RESULT, FAILURE>, ChainCreationException> {


	/**
	 * <p>Creates a <b>processor-chain</b> using the given array of {@link Processor}s by wrapping each of them in 
	 * an instance of {@link ProcessorChainLink} and linking them in the sequence reflected in the array.</p>
	 * 
	 * @param root
	 * 			an instance of {@link Processor} which will become the first (root) link in the processor-chain
	 * 
	 * @param successors
	 * 			further instances of {@link Processor} which will form the successive <i>links</i> of the processor-chain
	 * 
	 * @return the root {@link ProcessorChainLink} of the processor-chain which can be used to follow all successive 
	 * 		   links by invoking {@link ProcessorChainLink#getSuccessor()}
	 *
	 * @throws IllegalArgumentException
	 * 			if the given <b>root</b> {@link Processor} is {@code null}
	 *
	 * @throws ChainCreationException
	 * 			a {@link ChainCreationException} if a processor-chain failed to be created using the given {@link Processor}s
	 * 
	 * @since 1.2.4
	 */
	@Override
	public ProcessorChainLink<RESULT, FAILURE> newInstance(
		Processor<RESULT, FAILURE> root, Processor<RESULT, FAILURE>... successors) throws ChainCreationException {
		
		assertNotNull(root, new StringBuilder("The root ")
		.append(Processor.class.getName())
		.append(" cannot be <null>. This is used to construct the first ")
		.append(ProcessorChainLink.class.getName())
		.append(" in the processor-chain. ").toString());
		
		try {
		
			ProcessorChainLink<RESULT, FAILURE> chain = ProcessorChainLink.from(root);
			
			if(successors != null) {
				
				ProcessorChainLink<RESULT, FAILURE> current = chain;
				
				for (Processor<RESULT, FAILURE> successor : successors) {
					
					current = current.setSuccessor(ProcessorChainLink.from(successor));
				}
			}
		
			return chain;
		}
		catch(Exception e) {
			
			throw (e instanceof ChainCreationException)? 
					(ChainCreationException)e :new ChainCreationException(e);
		}
	}
}
