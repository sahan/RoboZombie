package com.lonepulse.robozombie.core;

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


import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.protocol.HttpContext;

/**
 * <p>This contract declares the basic network communication capabilities of an HTTP client. 
 * It grows on the <a href="http://hc.apache.org">Apache HTTP Components library</a>.</p> 
 * 
 * @version 1.2.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface HttpClientContract {

	/**
	 * <p>Takes an {@link HttpRequestBase}, executes it and 
	 * returns the results as an {@link HttpResponse}.</p>
	 * 
	 * @param httpRequestBase 
	 * 			any request of type {@link HttpRequestBase}
	 * 
	 * @return the {@link HttpResponse} of the execution.
	 * <br><br>
	 * @since 1.1.0
	 */
	<T extends HttpRequestBase> HttpResponse executeRequest(T httpRequestBase) 
	throws ClientProtocolException, IOException;
	
	/**
	 * <p>Takes an {@link HttpRequestBase}, executes it with 
	 * a given {@link HttpContext}. and returns the results 
	 * as an {@link HttpResponse}.</p>
	 * 
	 * @param httpRequestBase 
	 * 			any request of type {@link HttpRequestBase}
	 * 
	 * @param httpContext
	 * 			the {@link HttpContext} with which the request 
	 * 			is executed
	 * 
	 * @return the {@link HttpResponse} of the execution.
	 * <br><br>
	 * @since 1.2.0
	 */
	<T extends HttpRequestBase> HttpResponse executeRequest(T httpRequestBase, HttpContext httpContext) 
	throws ClientProtocolException, IOException;
}
