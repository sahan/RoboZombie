package com.lonepulse.robozombie.executor;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.robozombie.proxy.InvocationContext;
import com.lonepulse.robozombie.proxy.Zombie;

/**
 * <p>This contract defines the services offered for executing requests which were created for an 
 * endpoint's proxy invocation.</p> 
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public interface RequestExecutor {

	/**
	 * <p>Executes the given {@link HttpRequestBase} using the {@link HttpClient} which is configured 
	 * for use with the associated endopint and returns the resulting {@link HttpResponse}.</p>
	 * 
	 * <p>See {@link Zombie.Configuration}</p>
	 * 
	 * @param request
	 * 			the {@link HttpRequestBase} which should be executed using the endpoint's {@link HttpClient}
	 * <br><br>
	 * @return the {@link HttpResponse} which result from successful execution of the request 
	 * <br><br>
	 * @since 1.1.0
	 */
	HttpResponse execute(InvocationContext context, HttpRequestBase request);
}
