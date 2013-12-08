package com.lonepulse.robozombie.request;

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

import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This contract defines the policy of a <b>serializer</b> which is responsible for translating a model 
 * into a content-type suitable for network transmission.</p>  
 * 
 * <p><b>Note</b> that, ideally, the serialized content should capture all information on the model.</p>
 *  
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface Serializer<INPUT, OUTPUT> {

	/**
	 * <p>Serializes the given model by performing the following steps. 
	 * <br><br>
	 * <ol>
	 * 	 <li>Verifies the type compatibility of the annotated model</li>
	 *   <li>Initiates serialization as specified by the implementation</li>
	 * </ol>
	 * <br><br>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which can be used to discover information about the 
	 * 			current proxy invocation
	 * <br><br>
	 * @param input
	 * 			the <b>model</b> to be converted to a serialized representation which <b>captures 
	 * 			all its information</b>
	 * <br><br>
	 * @return the serialized content which captures all the information the provided model
	 * <br><br>
	 * @since 1.2.4
	 */
	OUTPUT run(InvocationContext context, INPUT input);
}