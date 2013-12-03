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
 * <p>This contract defines a strategy which accepts data, acts on them based on a given set of instructions 
 * and returns a result. Its designed to be as generic as possible and any concrete or abstract implementation 
 * should override {@link Processor#run(Object...)} to suit itself.</p>
 * 
 * <p><b>Note</b> that implementations are discouraged from using this contract to model <i>command objects</i>.</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface Processor<RESULT, FAILURE extends Throwable> {

	/**
	 * <p>Accepts a generic array of arguments, which is expected to contain <i>all the required information</i>, 
	 * and executes the strategy and returns a result of <i>the assigned type</i>. Ensure that you validate the 
	 * incoming the argument array and wrap any disparate exceptions in instance of the assigned exception type. 
	 * Exceptions which indicate a precondition failure (such as {@link IllegalArgumentException}) must be properly 
	 * documented.</p>
	 *
	 * @param args
	 * 			a generic array of arguments which is expected to contain <i>all the required information</i> 
	 * 
	 * @return an instance of the </i>assigned type</i> if processing completed successfully; else {@code null} if 
	 * 		   this strategy is not expected to return a result - such processors should define {@link Void} for the 
	 * 		   generic RESULT type
	 * 
	 * @throws FAILURE
	 * 			if the strategy failed to complete successfully; the type of the {@link Throwable} should be strictly 
	 * 			indicative of a failure in the strategy and not of a failure in the preconditions; {@link Processor}s 
	 * 			which are recoverable should <b>not</b> define a subclass of {@link RuntimeException} for the generic 
	 * 			FAILURE type
	 * 
	 * @since 1.2.4
	 */
	RESULT run(Object... args) throws FAILURE;
}
