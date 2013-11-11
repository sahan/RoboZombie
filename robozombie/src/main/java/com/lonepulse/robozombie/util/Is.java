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

import org.apache.http.HttpResponse;

/**
 * <p>A collection of utility services for common conditional checks.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @category utility
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
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
	 * @since 1.2.4
	 */
	public static final boolean successful(HttpResponse response) {
		
		int status = assertNotNull(response).getStatusLine().getStatusCode();
		return status > 199 && status < 300;
	}
}
