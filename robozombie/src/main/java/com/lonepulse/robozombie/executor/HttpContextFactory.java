package com.lonepulse.robozombie.executor;

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


import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.lonepulse.robozombie.RoboZombieRuntimeException;
import com.lonepulse.robozombie.util.AbstractGenericFactory;
import com.lonepulse.robozombie.util.GenericFactory;

/**
 * <p>Follows the {@link GenericFactory} policy to create local {@link HttpContext}s 
 * for a given endpoint {@link Class} which are reused for each endpoint invocation. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 * 
 * TODO revise HttpContextFactory
 */
class HttpContextFactory extends AbstractGenericFactory<Void, HttpContext, RoboZombieRuntimeException> {
	
	
	/**
	 * <p>Creates a local instance of {@link HttpContext} and with a new 
	 * {@link CookieStore} to be used for each subsequent request invocation. 
	 */
	@Override
	public HttpContext newInstance() {
		
		try {
		
			CookieStore cookieStore = new BasicCookieStore();
			HttpContext httpContext = new BasicHttpContext();
			
			httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			
			return httpContext;
		}
		catch(Exception e) {
			
			throw new RoboZombieRuntimeException(e);
		}
	}
}
