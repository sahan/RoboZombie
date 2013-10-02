package com.lonepulse.robozombie;

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


import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Header;
import com.lonepulse.robozombie.annotation.HeaderSet;
import com.lonepulse.robozombie.annotation.Parser;
import com.lonepulse.robozombie.annotation.Parser.ParserType;
import com.lonepulse.robozombie.annotation.Request;

/**
 * <p>An interface which represents a dummy endpoint with mock endpoint method definitions 
 * which process request and response headers.
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
public interface HeaderEndpoint {
	
	/**
	 * <p>A mock request which inserts a request header.
	 * 
	 * @param userAgent
	 * 			a variable header - <i>User-Agent</i> in this case
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/requestheader")
	public String requestHeader(@Header("User-Agent") String userAgent);
	
	/**
	 * <p>Retrieves a response header from a request using {@link Header}.
	 * 
	 * @param server
	 * 			the {@link StringBuilder} which is annotated with {@code @Header} 
	 * 			to treat it as an in-out variable for retrieving the response header
	 * 
	 * @return a response whose header was retrieved 
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/responseheader")
	public String responseHeader(@Header("Server") StringBuilder server);
	
	/**
	 * <p>A mock request which expects a request header value but instead receives 
	 * none form the current invocation.</p>
	 * 
	 * @param userAgent
	 * 			a variable header - <i>From</i> in this case
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/requestheaderskip")
	public String requestHeaderSkip(@Header("From") String email);
	
	/**
	 * <p>A mock request which expects a response header value form the server 
	 * but instead receives none.</p>
	 * 
	 * @param server
	 * 			the {@link StringBuilder} which is annotated with {@code @Header} 
	 * 			to treat it as an in-out variable for retrieving the response header
	 * 
	 * @return a response whose header was expected to be retrieved 
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/responseheaderskip")
	public String responseHeaderSkip(@Header("Expires") String expires);
	
	/**
	 * <p>A mock request which inserts a header that of an illegal type. This invocation 
	 * should be unsuccessful and should result in an error. </p>
	 * 
	 * @param contentLength
	 * 			a variable header of the illegal type {@code int} 
	 * 
	 * @return the parsed response content, which in this case should not be available
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/requestheadertypeerror")
	public String requestHeaderTypeError(@Header("Content-Length") int contentLength);
	
	/**
	 * <p>A mock request which inserts a constant set of headers.</p>
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/headerset")
	@HeaderSet({@HeaderSet.Header(name = "Accept", value = "application/json"),
				@HeaderSet.Header(name = "Accept-Charset", value = "utf-8")})
	public String headerSet();
}
