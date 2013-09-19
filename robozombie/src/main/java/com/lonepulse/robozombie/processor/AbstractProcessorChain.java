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
 * <p>This contract defines the services offered by a <i>chain</i> of sequentially executed {@link ProcessorChainLink}s. 
 * Each link in the chain may define an successor {@link ProcessorChainLink} - if no successor is defined it signifies 
 * the end of the chain.</p>
 * 
 * <p>The terminal results of the processor chain will be the results produced by the final link in the chain. However, 
 * note that a {@link Processor} may choose not to process the input based on the targeting criteria specified in its 
 * implementation.</p> 
 * 
 * <p>The execution of each {@link ProcessorChainLink} can be categorized as a <b>traversal</b> or a <b>termination</b>.</p>
 * <ol>
 * 	<li>Traversal: the execution of the current link followed by the progression to the next link. 
 * (see {@link #onTraverse(Object, ProcessorChainLink, Object...)})</li>
 * 
 * 	<li>Termination: the execution of the last link and subsequent return of the final result. 
 * (see {@link #onTerminate(Object, Object...)})</li>
 * </ol>
 * 
 * <p><b>Both of these services defer to the better judgment of the processor chain creator.</b></p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public abstract class AbstractProcessorChain<LINK_RESULT, LINK_FAILURE extends Throwable> 
implements Processor<LINK_RESULT, LINK_FAILURE> {

	
	private ProcessorChainLink<LINK_RESULT, LINK_FAILURE> root;

	
	/**
	 * <p>Instantiates a new instance of {@link AbstractProcessorChain} and assigns the given wraps the given 
	 * {@link Processor} that handles the <i>execution</i> for this link in the chain.</p>
	 * 
	 * @param root
	 * 			the initial {@link ProcessorChainLink} in this chain whose {@link Processor} which may have further 
	 * 			successors that contribute to the length of the chain
	 * <br><br>
	 * @throws IllegalStateException
	 * 			if the given {@link ProcessorChainLink} is {@code null}
	 * <br><br>
	 * @since 1.2.4
	 */
	public AbstractProcessorChain(ProcessorChainLink<LINK_RESULT, LINK_FAILURE> root) {
	
		if(root == null) {
			
			StringBuilder errorContext = new StringBuilder("A ")
			.append(AbstractProcessorChain.class.getName())
			.append(" cannot be constructed with a <null> root ")
			.append(ProcessorChainLink.class.getName());
			
			throw new IllegalStateException(errorContext.toString());
		}
		
		this.root = root;
	}
	
	/**
	 * <p>Directs the processing along the chain by executing the root {@link ProcessorChainLink} and delegating 
	 * all successors to {@link #onTraverse(Object, ProcessorChainLink, Object...)} and the terminal link to 
	 * {@link #onTerminate(Object, Object...)}.</p>
	 * 
	 * @param args
	 * 			the arguments to the root {@link ProcessorChainLink} which will server as the input to the 
	 * 			first {@link Processor} which produces the initial <i>RESULT</i>; these are passed along the 
	 * 			chain for each and every link
	 * <br><br>
	 * @return the result of the complete {@link AbstractProcessorChain} execution after been processed by 
	 * 		   {@link #onTerminate(Object, Object...)}
	 * <br><br>
	 * @throws ChainExecutionException
	 * 			if the {@link AbstractProcessorChain} halted due to an unrecoverable failure in one of its 
	 * 			{@link ProcessorChainLink}s; this signals a <b>chain-wide</b> failure; failures of individual 
	 * 			links may be handled in {@link #onInitiate(ProcessorChainLink, Object...)} and 
	 * 			{@link #onTraverse(Object, ProcessorChainLink, Object...)}   
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public LINK_RESULT run(Object... args) throws ChainExecutionException {
		
		try {
			
			ProcessorChainLink<LINK_RESULT, LINK_FAILURE> current = root;
			
			LINK_RESULT result = onInitiate(current, args);
			
			while (!current.isTerminalLink()) {
				
				current = current.getSuccessor();
				result = onTraverse(result, current, args);
			} 
			
			onTerminate(result, args);
			
			return result;
		}
		catch(Throwable t) { 
			
			throw new ChainExecutionException(t);
		}
	}
	
	/**
	 * <p>This callback is invoked for the very first {@link ProcessorChainLink} in this chain. This can be used to 
	 * implement any <i>pre-chain</i> processing and it <b>should execute the root link</b> which produces the first 
	 * <i>RESULT</i>.</p>
	 * 
	 * @param root
	 * 			the first {@link ProcessorChainLink} which produces the initial <i>RESULT</i>
	 * <br><br>
	 * @param args
	 * 			the arguments to the {@link AbstractProcessorChain} which are passed along to every {@link ProcessorChainLink}
	 * <br><br>
	 * @return the <i>RESULT</i> which is produced by executing the first {@link ProcessorChainLink}
	 * <br><br>
	 * @since 1.2.4
	 */
	protected abstract LINK_RESULT onInitiate(
		ProcessorChainLink<LINK_RESULT, LINK_FAILURE> root, Object... args); 

	/**
	 * <p>This callback is invoked when the execution of the chain will progress from the current link to the next 
	 * link; i.e. a {@link ProcessorChainLink} successor is available. All implementations are expected to handle 
	 * the process of <i>crossing links</i> by using the <i>RESULT</i> of the previous link's execution and invoking 
	 * the successor <b>as they see fit</b>. The <i>RESULT</i> of the current links execution should be  </p>
	 * 
	 * <p><b>Note</b> that failures of <i>recoverable</i> {@link Processor}s should be explicitly handled and recovered 
	 * from where possible</p>
	 * 
	 * <p>As an example usage, consider a {@link Processor} with a result type of {@link Void}. This signifies that 
	 * the {@link Processor} implementation does not return a result and its execution is based solely on the input 
	 * to the processor chain. In such situations the <i>RESULT</i> should be ignored original argument array should 
	 * be passed to the successor.</p>
	 * 
	 * <p>As an additional example, consider the task of prematurely terminating the chain for the current invocation 
	 * context. An additional {@code boolean} flag could be added to a copy of the argument array and this could then 
	 * be given to the successor. The implementation of {@code onTraverse()} and {@code onTerminate()} should then check 
	 * the existence of this additional parameter and skip invocation of the successor and the terminal processing.
	 * 
	 * <p>Although the {@link Processor} wrapped in the {@link ProcessorChainLink} may choose to skip the processing, 
	 * this decision is limited to the input it receives and it's unaware of the details of the chain in which it resides. 
	 * Hence this decision can only be made from a context which has a holistic view of the {@link AbstractProcessorChain}.</p>
	 *
	 * @param result
	 *			the result from the execution of the <b>current</b> {@link ProcessorChainLink} which should be passed 
	 *			to the successor link in this iteration of {@code onTraverse()} 
	 * <br><br>
	 * @param successor
	 * 			the next {@link ProcessorChainLink} which should be executed with the <i>RESULT</i> of the current link 
	 * 			and the subsequent <i>RESULT</i> of this successor should be returned in turn
	 * <br><br>
	 * @param args
	 * 			the arguments to the {@link AbstractProcessorChain} which are passed along to every {@link ProcessorChainLink}
	 * <br><br>
	 * @return the <i>RESULT</i> which was produced by executing the successor {@link ProcessorChainLink}
	 * <br><br>
	 * @since 1.2.4
	 */
	protected abstract LINK_RESULT onTraverse(
		LINK_RESULT result, ProcessorChainLink<LINK_RESULT, LINK_FAILURE> successor, Object... args);
		
	/**
	 * <p>This callback is invoked by the last link in this chain. This can be used to implement any <i>post-chain</i> 
	 * processing before allowing the chain to exit with the final <i>RESULT</i>. 
	 * 
	 * @param result
	 *			the result from the execution of the <b>final</b> {@link ProcessorChainLink}
	 * <br><br>
	 * @param args
	 * 			the arguments to the {@link AbstractProcessorChain} which are passed along to every {@link ProcessorChainLink}
	 * <br><br>
	 * @since 1.2.4
	 */
	protected abstract void onTerminate(LINK_RESULT result, Object... args); 
}
