package com.lonepulse.robozombie.util;

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

import static com.lonepulse.robozombie.util.Assert.assertNotNull;
import static com.lonepulse.robozombie.util.Components.isDetached;

import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.annotation.Async;
import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>A collection of utility services for common conditional checks.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @category utility
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public final class Is {
	
	
	private Is() {}
	
	
	/**
	 * <p>Determines whether the {@link HttpResponse} signifies a successful request execution or not.</p>
	 *
	 * @param response
	 * 			the {@link HttpResponse} whose success status is to be determined
	 * <br><br>
	 * @return {@code true} if the {@link HttpResponse} signifies a successful request execution 
	 * <br><br>
	 * @since 1.3.0
	 */
	public static boolean successful(HttpResponse response) {
		
		int status = assertNotNull(response).getStatusLine().getStatusCode();
		return status > 199 && status < 300;
	}
	
	/**
	 * <p>Determines whether the {@link HttpResponse} returned any of the specified <i>status codes</i>.</p>
	 *
	 * @param response
	 * 			the {@link HttpResponse} whose status is to be matched
	 * <br><br>
	 * @param code
	 * 			the mandatory status code to match against
	 * <br><br>
	 * @param codes
	 * 			any additional codes to match against
	 * <br><br>
	 * @return {@code true} if the {@link HttpResponse}'s status matches <b>any</b> of the given codes
	 * <br><br>
	 * @since 1.3.0
	 */
	public static boolean status(HttpResponse response, int code, int... codes) {
		
		int status = assertNotNull(response).getStatusLine().getStatusCode();
		
		if(status == code) {
			
			return true;
		}
		
		if(codes != null && codes.length > 0) {
			
			for (int statusCode : codes) {
				
				if(status == statusCode) {
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * <p>Determines if a proxy invocation should be handled <b>asynchronously</b>.</p>
	 *
	 * @param context
	 * 			the {@link InvocationContext} which contains information about the proxy invocation
	 * <br><br>
	 * @return {@code true} if the proxy invocation should be handled asynchronously
	 * <br><br>
	 * @since 1.3.0
	 */
	public static boolean async(InvocationContext context) {
		
		return !isDetached(context, Async.class) && 
			   (context.getRequest().isAnnotationPresent(Async.class) || 
			   (context.getEndpoint().isAnnotationPresent(Async.class)));
	}
}
