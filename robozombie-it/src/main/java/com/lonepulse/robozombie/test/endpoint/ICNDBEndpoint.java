package com.lonepulse.robozombie.test.endpoint;

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


import com.lonepulse.robozombie.core.annotation.Asynchronous;
import com.lonepulse.robozombie.core.annotation.Endpoint;
import com.lonepulse.robozombie.core.annotation.Header;
import com.lonepulse.robozombie.core.annotation.Param;
import com.lonepulse.robozombie.core.annotation.Parser;
import com.lonepulse.robozombie.core.annotation.Parser.PARSER_TYPE;
import com.lonepulse.robozombie.core.annotation.Request;
import com.lonepulse.robozombie.core.response.AsyncHandler;
import com.lonepulse.robozombie.rest.annotation.PathParam;
import com.lonepulse.robozombie.rest.annotation.Rest;
import com.lonepulse.robozombie.test.model.ICNDBResponse;
import com.lonepulse.robozombie.test.model.ICNDBResponseArray;
import com.lonepulse.robozombie.test.model.NorrisJoke;

/**
 * <p>An endpoint interface which defines the contract to the 
 * <a href="http://www.icndb.com/api/">ICNDB API</a>.
 * 
 * @category test
 * <br><br> 
 * @version 1.1.3
 * <br><br> 
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Endpoint("api.icndb.com/jokes")
@Parser(PARSER_TYPE.JSON)
public interface ICNDBEndpoint {
	
	/**
	 * <p>Retrieves a random {@link NorrisJoke}.
	 * 
	 * @return a random {@link NorrisJoke}.
	 * 
	 * @since 1.0.0
	 */
	@Request(path = "/random")
	public ICNDBResponse random();
	
	/**
	 * <p>Retrieves mutiple random {@link NorrisJoke}s.
	 * 
	 * @param amount
	 * 			the number of {@link NorrisJoke}s to retrieve
	 * 
	 * @return multile {@link NorrisJoke}s.
	 * 
	 * @since 1.0.0
	 */
	@Rest(path = "/random/:amount")
	public ICNDBResponseArray random(@PathParam("amount") String amount);
	
	/**
	 * <p>Retrieves a random {@link NorrisJoke} with the name 
	 * <i>Chuck Norris</i> replaced by the given parameters.
	 * 
	 * @param firstName
	 * 			the name to replace <i>Chuck</i>
	 * 
	 * @param lastName
	 * 			the name to replace <i>Norris</i>
	 * 
	 * @return a random {@link NorrisJoke}
	 * 
	 * @since 1.0.0
	 */
	@Request(path = "/random")
	public ICNDBResponse random(@Param("firstName") String firstName, 
								@Param("lastName") String lastName);
	
	/**
	 * <p>Retrieves a random {@link NorrisJoke} with the name 
	 * <i>John Doe</i>.
	 * 
	 * @param firstName
	 * 			the name to replace <i>Chuck</i>
	 * 
	 * @param lastName
	 * 			the name to replace <i>Norris</i>
	 * 
	 * @return a random {@link NorrisJoke}
	 * 
	 * @since 1.0.0
	 */
	@Request(path = "/random", params = {@Request.Param(name = "firstName", value = "John"), 
										 @Request.Param(name = "lastName", value = "Doe")})
	public ICNDBResponse randomJohnDoeJoke();

	/**
	 * <p>Retrieves mutiple random {@link NorrisJoke}s asynchronously 
	 * and processes them in the supplied {@link AsyncHandler}.
	 * 
	 * @param asyncHandler
	 * 			the {@link AsyncHandler} which handles the results of 
	 * 			the asynchronous request execution
	 * 
	 * @since 1.1.1
	 */
	@Asynchronous @Rest(path = "/random/10")
	public ICNDBResponseArray randomAsync(AsyncHandler<ICNDBResponseArray> asyncHandler);
	
	/**
	 * <p>Retrieves a random {@link NorrisJoke} and populates the 
	 * response <i>server</i> response header.
	 * 
	 * @return a random {@link NorrisJoke}.
	 * 
	 * @since 1.1.1
	 */
	@Rest(path = "/random")
	public ICNDBResponse random(@Header("Server") StringBuilder server); 
}
