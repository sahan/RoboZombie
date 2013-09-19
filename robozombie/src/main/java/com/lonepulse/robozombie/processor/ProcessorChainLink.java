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


/**
 * <p>This entity represents a <b>single link</b> in a <i>processor chain</i>. It wraps the {@link Processor} 
 * which should be executed and accepts another {@link ProcessorChainLink} as its <b>successor</b>. All 
 * implementations are expected to check for the availability of a successor and invoke it with the results 
 * of the current link's execution and return the <i>successive results</i>.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class ProcessorChainLink<RESULT, FAILURE extends Throwable> {
	

	private final Processor<RESULT, FAILURE> processor;
	
	private ProcessorChainLink<RESULT, FAILURE> successor;
	
	
	/**
	 * <p>Instantiates a new instance of {@link ProcessorChainLink} and wraps the given {@link Processor} 
	 * which handles the <i>execution</i> for this link in the chain.</p>
	 * 
	 * <p><b>Restricted</b>, use {@link #from(Processor)} instead.</p>
	 * 
	 * @param processor
	 * 			the {@link Processor} to be wrapped by this instance of {@link ProcessorChainLink}
	 * <br><br>
	 * @since 1.2.4
	 */
	private ProcessorChainLink(Processor<RESULT, FAILURE> processor) {
	
		this.processor = processor;
	}
	
	/**
	 * <p>Creates a new instance of {@link ProcessorChainLink} and wraps the given {@link Processor} which handles 
	 * the <i>execution</i> for this link in the chain.</p>
	 *
	 * @param processor
	 * 			the {@link Processor} to be wrapped by this instance of {@link ProcessorChainLink}
	 * <br><br>
	 * @return a new instance of {@link ProcessorChainLink} which wraps the provided {@link Processor} 
	 * <br><br>
	 * @throws IllegalStateException
	 * 			if the given {@link Processor} is {@code null}; a {@link ProcessorChainLink} must always enclose 
	 * 			a {@link Processor} which is invoked when the execution reaches this link in a chain
	 * <br><br>
	 * @since 1.2.4
	 */
	public static final <RESULT, FAILURE extends Throwable> 
	ProcessorChainLink<RESULT, FAILURE> from(Processor<RESULT, FAILURE> processor) {
		
		if(processor == null) {
			
			StringBuilder errorContext = new StringBuilder("A ")
			.append(ProcessorChainLink.class.getName())
			.append(" cannot be instantiated with a <null> ")
			.append(Processor.class.getName());
			
			throw new IllegalStateException(errorContext.toString());
		}
		
		return new ProcessorChainLink<RESULT, FAILURE>(processor);
	}
	
	/**
	 * <p><b>Assigns</b> the given {@link ProcessorChainLink} as the <b>successor</b> which has been designated 
	 * to this link.</p>
	 * 
	 * <p>Once the {@link Processor} instance which is wrapped by this link produces it result, they are to be fed 
	 * to the successor and the resulting <i>successive result</i> is the <i>output</i> of this link.</p>
	 * 
	 * @param successor
	 * 			the {@link ProcessorChainLink} to be designated as the <b>successor</b> to this link
	 * <br><br>
	 * @return the {@link Processor} which was provided as the <b>successor</b> to this {@link ProcessorChainLink} 
	 * <br><br>
	 * @since 1.2.4
	 */
	public ProcessorChainLink<RESULT, FAILURE> setSuccessor(ProcessorChainLink<RESULT, FAILURE> successor) {
		
		if(successor == null) {
			
			StringBuilder errorContext = new StringBuilder("The ")
			.append(ProcessorChainLink.class.getName())
			.append(" which is designated to be the successor cannot be <null>");
			
			throw new IllegalStateException(errorContext.toString());
		}
		
		return (this.successor = successor);
	}
	
	/**
	 * <p>Retrieves the {@link ProcessorChainLink} which has been designated as the <b>successor</b> of this link.</p> 
	 * 
	 * <p>Once the {@link Processor} instance which is wrapped by this link produces it result, they are to be fed 
	 * to the successor and the resulting <i>successive result</i> is the <i>output</i> of this link.</p> 
	 *
	 * @return the {@link ProcessorChainLink} designated as the <b>successor</b> to this link
	 * <br><br>
	 * @since 1.2.4
	 */
	public ProcessorChainLink<RESULT, FAILURE> getSuccessor() {
		
		return successor;
	}
	
	/**
	 * <p>Checks whether this {@link ProcessorChainLink} is the last link in its chain and ensures that there are 
	 * <b>no more successors</b> to traverse.</p> 
	 *
	 * @return {@code true} if this is indeed the terminal link
	 * <br><br>
	 * @since 1.2.4
	 */
	public boolean isTerminalLink() {
		
		return successor == null;
	}

	/**
	 * <p>Retrieves the {@link Processor} which is wrapped by this instance of {@link ProcessorChainLink}. 
	 *
	 * @return the {@link Processor} which is wrapped by this instance of {@link ProcessorChainLink}
	 * <br><br>
	 * @since 1.2.4
	 */
	public Processor<RESULT, FAILURE> getProcessor() {
		
		return processor;
	}
}
