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
 * <p>This contract defines a strategy which accepts data, acts on them (based on predefined instructions) 
 * and returns a result. It's designed to be as generic as possible; any concrete or abstract implementation 
 * may override {@link Processor#run(Object...)} to suit itself.</p>
 * 
 * <p><b>Note</b> that implementations are discouraged from using this contract to model <i>command objects</i>.</p> 
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface Processor<RESULT, FAILURE extends Throwable> {

	/**
	 * <p>Accepts a generic argument array (which is expected to contain <i>all the required information</i>), 
	 * executes the strategy and returns a result of <i>the assigned type</i>. Ensure that you validate the 
	 * incoming the argument array and wrap any disparate exceptions in instance of the assigned exception type. 
	 * All exceptions which indicate a failed precondition (e.g. {@link IllegalArgumentException}) should be 
	 * properly documented.</p>
	 *
	 * @param args
	 * 			a generic array of arguments which is expected to contain <i>all the required information</i> 
	 * <br><br>
	 * @return an instance of the </i>assigned type</i> if processing completed successfully; else {@code null} 
	 * 		   if this strategy is not expected to return a result - such processors should define {@link Void} 
	 * 		   for the generic RESULT type
	 * <br><br>
	 * @throws FAILURE
	 * 			if the strategy failed to complete successfully; the type of the {@link Throwable} should be 
	 * 			strictly indicative of a failure in the strategy and not of a failure in the preconditions; 
	 * 			{@link Processor}s which are recoverable should <b>not</b> define a {@link RuntimeException} 
	 * 			for the generic FAILURE type
	 * <br><br>
	 * @since 1.3.0
	 */
	RESULT run(Object... args) throws FAILURE;
}
