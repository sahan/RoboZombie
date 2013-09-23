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


import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Parser;
import com.lonepulse.robozombie.annotation.Parser.ParserType;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.test.model.User;

/**
 * <p>An interface which represents a dummy endpoint with request method definitions 
 * that use various pre-fabricated and custom response parsers.
 * 
 * @category test
 * <br><br> 
 * @version 1.1.1
 * <br><br> 
 * @since 1.2.4
 * <br><br> 
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Parser(ParserType.STRING)
@Endpoint(host = "0.0.0.0", port = "8080")
public interface ParserEndpoint {
	
	/**
	 * <p>A mock request which receives a response with a code that signals a failure. 
	 * Expects a domain specific exception to be thrown rather than the parsed result.  
	 *
	 * @return the parsed response content, which in this case should not be available
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/responseerror")
	public String responseError();
	
	/**
	 * <p>A mock request which receives a JSON response that is parsed to it model.
	 * 
	 * @return the parsed response entity
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/json")
	@Parser(ParserType.JSON) 
	public User parseJson();
	
	/**
	 * <p>A mock request which receives an XML response that is parsed to it model.
	 * 
	 * @return the parsed response entity
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/xml")
	@Parser(ParserType.XML) 
	public User parseXml();
}