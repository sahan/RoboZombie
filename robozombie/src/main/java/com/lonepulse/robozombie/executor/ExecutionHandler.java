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


import org.apache.http.HttpResponse;

import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This contract defines the callbacks offered for customizing stages in a request execution. It is 
 * to be used with a {@link RequestExecutor} which directs the execution. <b>Note that all implementations 
 * are expected to be stateless</b>.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
interface ExecutionHandler {
	
	/**
	 * <p>This callback is invoked after a request execution which resulted in an {@link HttpResponse} 
	 * with a <b>successful status code</b>.</p>
	 *
	 * @param response
	 * 			the resulting {@link HttpResponse} with a successful status code
	 * <br><br>
	 * @param context
	 * 			the {@link InvocationContext} with information on the proxy invocation 
	 * <br><br>
	 * @since 1.2.4
	 */
	void onSuccess(HttpResponse response, InvocationContext context);
	
	/**
	 * <p>This callback is invoked after a request execution which resulted in an {@link HttpResponse} 
	 * with a <b>failed status code</b>.</p>
	 *
	 * @param response
	 * 			the resulting {@link HttpResponse} with a failed status code
	 * <br><br>
	 * @param context
	 * 			the {@link InvocationContext} with information on the proxy invocation 
	 * <br><br>
	 * @since 1.2.4
	 */
	void onFailure(HttpResponse response, InvocationContext context);
	
	/**
	 * <p>This callback is invoked upon a request execution which <b>resulted in an error</b>.</p>
	 * 
	 * <p><b>Note</b> that customized runtime exceptions may be thrown using any useful information 
	 * discovered from the {@link InvocationContext}.</p>
	 *
	 * @param context
	 * 			the {@link InvocationContext} with information on the proxy invocation 
	 * <br><br>
	 * @param error
	 * 			the exception which resulted in a request execution failure 
	 * <br><br>
	 * @since 1.2.4
	 */
	void onError(InvocationContext context, Exception error);
}
