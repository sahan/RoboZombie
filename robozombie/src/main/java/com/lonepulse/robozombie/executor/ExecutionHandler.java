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

import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>This contract defines the callbacks offered for customizing the stages in a request execution. 
 * It should be used with a {@link RequestExecutor} which directs the execution.</p> 
 * 
 * <p><b>Note</b> that all implementations are expected to be stateless</b>.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
interface ExecutionHandler {
	
	/**
	 * <p>This callback is invoked after a request execution which resulted in an {@link HttpResponse} 
	 * with a <b>successful status code</b>. Successful status codes fall into the range <b>2xx</b>.</p>
	 *
	 * <p><b>Note</b> that responses with the status codes <b>204</b> and <b>205</b> do not contain any 
	 * response content.</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} with information on the proxy invocation 
	 * <br><br>
	 * @param response
	 * 			the resulting {@link HttpResponse} with a successful status code
	 * <br><br>
	 * @since 1.3.0
	 */
	void onSuccess(InvocationContext context, HttpResponse response);
	
	/**
	 * <p>This callback is invoked after a request execution which resulted in an {@link HttpResponse} 
	 * with a <b>failure status code</b>, i.e. those which do not fall into the range <b>2xx</b>.</p>
	 *
	 * @param context
	 * 			the {@link InvocationContext} with information on the proxy invocation 
	 * <br><br>
	 * @param response
	 * 			the resulting {@link HttpResponse} with a failed status code
	 * <br><br>
	 * @since 1.3.0
	 */
	void onFailure(InvocationContext context, HttpResponse response);
	
	/**
	 * <p>This callback is invoked when a request execution <b>results in an error</b>.</p>
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
	 * @since 1.3.0
	 */
	void onError(InvocationContext context, Exception error);
}
