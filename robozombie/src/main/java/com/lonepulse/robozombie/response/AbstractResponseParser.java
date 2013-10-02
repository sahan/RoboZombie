package com.lonepulse.robozombie.response;

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
import org.apache.http42.util.EntityUtils;

import com.lonepulse.robozombie.ProxyInvocationConfiguration;

/**
 * <p>This is an implementation of {@link ResponseParser} which defines and executes the 
 * steps in <i>parsing</i>.</p>
 * 
 * <p>User defined {@link ResponseParser}s must extend this class and override the 
 * {@link AbstractResponseParser#processResponse(HttpResponse)} and {@link AbstractResponseParser#getType()} 
 * methods.</p>
 * <br><br>
 * <ul>
 *  <li>
 *  <b>{@link AbstractResponseParser#processResponse(HttpResponse)}</b><br>
 *  Retrieves the necessary information from {@link HttpResponse} and returns an instance of 
 *  a custom {@link ResponseParser}.
 *  <br><br><b>
 *  Sample Code from {@link StringResponseParser}:<br><br></b>
 *  <font color="#2E2E2E">
 *  <code>
 *  String responseString = EntityUtils.toString(httpResponse.getEntity());
 *	return responseString.subSequence(0, responseString.length());
 *  </code>
 *  </font><br><br>
 *  <p>The request's return type can be obtained by calling {@link #getRequestReturnType()}. 
 *  This may be used within the {@link #processResponse(HttpResponse)} as necessary.</p> 
 *  </li>
 *  <br><br><br>
 *  <li>
 *  <b>{@link AbstractResponseParser#getType()}</b><br>
 *  Returns the {@link Class} of the type handled by the custom {@link ResponseParser}. 
 *  <br><br><b>
 *  Sample Code from {@link StringResponseParser}:<br><br></b>
 *  <font color="#2E2E2E">
 *  <code>
 *  return CharSequence.class;
 *  </code>
 *  </font>
 *  </li>
 * </ul> 
 * <br>
 * 
 * @version 1.1.6
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public abstract class AbstractResponseParser<T> implements ResponseParser<T> {

	
	private Class<T> parserType;
	
	
	/**
	 * <p>Initializes a new {@link AbstractResponseParser} with the given {@link Class} which 
	 * represents the output of this parser.
	 *
	 * @param parserType
	 * 			the {@link Class} type of the entity which is produced by this parser
	 *
	 * @since 1.2.4
	 */
	protected AbstractResponseParser(Class<T> parserType) {
		
		this.parserType = parserType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final T parse(HttpResponse httpResponse, ProxyInvocationConfiguration config) {
		
		Class<?> requestReturnType = config.getRequest().getReturnType();
		
		try {
			
			throwIfNotAssignable(requestReturnType);
			return processResponse(httpResponse, config);
		}
		catch(Exception e) {
		
			throw new ResponseParserException(e);
		}
		finally {
			
			EntityUtils.consumeQuietly(httpResponse.getEntity());
		}
	}
	
	/**
	 * <p>Checks if the desired request return type can be instantiated from 
	 * an instance of the parser's return type.</p>
	 * 
	 * @param requestReturnType
	 * 				the {@link Class} of the request return type
	 */
	private void throwIfNotAssignable(Class<? extends Object> requestReturnType) {
		
		if(!void.class.isAssignableFrom(requestReturnType)
		   && !Void.class.isAssignableFrom(requestReturnType)
		   && !getType().isAssignableFrom(requestReturnType)) {
			
			throw new ResponseParserNotAssignableException(getType(), requestReturnType);
		}
	}

	/**
	 * <p>Allows any {@link ResponseParser} extension to determine the type 
	 * {@link Class} of the instantiated {@link ResponseParser}.</p>
	 * 
	 * @return the type {@link Class} of the instantiated {@link ResponseParser}
	 * <br><br>
	 * @since 1.1.4
	 */
	protected final Class<T> getType() {
		
		return this.parserType;
	}
	
	/**
	 * <p>Takes in the {@link HttpResponse} returned from the request execution 
	 * and parses the content within the response into and instance of the 
	 * specified type.</p>
	 * 
	 * @param httpResponse
	 * 				the {@link HttpResponse} from which the content is extracted
	 * 
	 * @param config
	 * 				the {@link ProxyInvocationConfiguration} which supplies all information 
	 * 				regarding the request and it's invocation
     * <br><br>
	 * @return the entity which is created after parsing the output
	 * <br><br>
	 * @throws Exception 
	 * 				Parsing failures may occur due to many reasons
	 * <br><br>
	 * @since 1.1.4
	 */
	protected abstract T processResponse(HttpResponse httpResponse, ProxyInvocationConfiguration config) 
	throws Exception;
}
