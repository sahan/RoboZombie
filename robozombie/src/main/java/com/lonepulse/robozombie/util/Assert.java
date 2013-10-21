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


import com.lonepulse.robozombie.Validator;

/**
 * <p>A collection of utility services which eases certain trivial validations.</p>
 * 
 * <p><b>Note</b> that while most of the services are generic, some assertions may be coupled to certain 
 * modules. For example, the exceptions which are thrown due to assertion failures may not be a good fit 
 * for all scenarios. Consult the documentation for further information.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class Assert {
	
	
	private Assert() {}
	
	
	/**
	 * <p>Asserts that the given argument is <b>{@code not null}</b>. If the arguement is {@code null} 
	 * a {@link NullPointerException} will be thrown with the message, <i>"The supplied argument was 
	 * found to be &lt;null&gt;"</i>.</p>
	 *
	 * @param arg
	 * 			the argument to be asserted as being {@code not null}
	 * <br><br>
	 * @return the argument which was asserted to be {@code not null}
	 * <br><br>
	 * @throws NullPointerException
	 * 			if the supplied argument was found to be {@code null}
	 * <br><br>
	 * @since 1.2.4
	 */
	public static final <T extends Object> T assertNotNull(T arg) {
		
		if(arg == null) {
		
			throw new NullPointerException("The supplied argument was found to be <null>");
		}
		
		return arg;
	}
	
	/**
	 * <p>Asserts that the given argument is <b>{@code not null}</b>. If the arguement is {@code null} 
	 * a {@link NullPointerException} will be thrown with the message, <i>"The supplied &lt;type&gt; was 
	 * found to be &lt;null&gt;"</i>.</p>
	 *
	 * @param arg
	 * 			the argument to be asserted as being {@code not null}
	 * <br><br>
	 * @param type
	 * 			the {@link Class} of the argument to be asserted
	 * <br><br>
	 * @return the argument which was asserted to be {@code not null}
	 * <br><br>
	 * @throws NullPointerException
	 * 			if the supplied argument was found to be {@code null}
	 * <br><br>
	 * @since 1.2.4
	 */
	public static final <T extends Object> T assertNotNull(T arg, Class<T> type) {
		
		if(arg == null) {
			
			throw new NullPointerException(new StringBuilder("The supplied ")
				.append(type == null? "argument" : type.getName()).append(" was found to be <null>").toString());
		}
		
		return arg;
	}
	
	/**
	 * <p>Asserts that the given context is valid using the supplied {@link Validator} along with a few 
	 * other trivial valiations such as a {@code null} check.</p>
	 *
	 * @param context
	 * 			the context to be validated using the given {@link Validator}
	 * <br><br>
	 * @param validator
	 * 			the {@link Validator} to be used on the given context
	 * <br><br>
	 * @return the given context which was asserted to be valid
	 * <br><br>
	 * @since 1.2.4
	 */
	public static final <T> T assertValid(T context, Validator<T> validator) {
	
		assertNotNull(context);
		
		if(validator != null) {
				
			validator.validate(context);
		}
		
		return context;
	}
}
