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


import java.util.Collection;
import java.util.Map;

import com.lonepulse.robozombie.Validator;

/**
 * <p>A collection of utility services which eases certain trivial validations.</p>
 * 
 * <p><b>Note</b> that while most of the services are generic, some assertions may be coupled to 
 * certain modules. For example, the exceptions which are thrown due to assertion failures may not 
 * be a good fit for all scenarios. Consult the documentation for further information.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @category utility
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class Assert {
	
	
	private Assert() {}
	
	
	/**
	 * <p>Asserts that the given Object is assignable to the specified type. If either the generic Object 
	 * or the {@link Class} is {@code null} a {@link NullPointerException} will be thrown with the message, 
	 * <i>"The supplied argument was found to be &lt;null&gt;"</i>. If the object was not found to be in 
	 * conformance with the specified type, a {@link ClassCastException} will be thrown with the message, 
	 * <i>"The instance of type &lt;argument-type&gt; cannot be assigned to a &lt;specified-type&gt;"</i>.</p>
	 *
	 * @param arg
	 * 			the argument to be asserted for type conformance 
	 * <br><br>
	 * @param type
	 * 			the {@link Class} type to which the argument must conform 
	 * <br><br>
	 * @return the argument which was asserted to conform to the specified type
	 * <br><br>
	 * @throws ClassCastException
	 * 			if the supplied argument does not conform to the specified type
	 * <br><br>
	 * @since 1.2.4
	 */
	public static <T extends Object> T assertAssignable(Object arg, Class<T> type) {
		
		assertNotNull(arg);
		assertNotNull(type);
		
		if(!type.isAssignableFrom(arg.getClass())) {
		
			throw new ClassCastException(new StringBuilder("The instance of type ")
			.append(arg.getClass().getName()).append(" cannot be assigned to a ")
			.append(type.getName()).toString());
		}
		
		return type.cast(arg);
	}
	
	/**
	 * <p>Asserts that the given argument is <b>{@code not null}</b>. If the argument is {@code null} 
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
	public static <T extends Object> T assertNotNull(T arg) {
		
		if(arg == null) {
		
			throw new NullPointerException("The supplied argument was found to be <null>");
		}
		
		return arg;
	}
	
	/**
	 * <p>Asserts that the given argument is <b>{@code not null}</b>. If the argument is {@code null} 
	 * a {@link NullPointerException} will be thrown with the provided message.</p>
	 *
	 * @param arg
	 * 			the argument to be asserted as being {@code not null}
	 * <br><br>
	 * @param arg
	 * 			the message to be provided with the {@link NullPointerException} if the assertion fails  
	 * <br><br>
	 * @return the argument which was asserted to be {@code not null}
	 * <br><br>
	 * @throws NullPointerException
	 * 			if the supplied argument was found to be {@code null}
	 * <br><br>
	 * @since 1.2.4
	 */
	public static <T extends Object> T assertNotNull(T arg, String message) {
		
		if(arg == null) {
			
			throw new NullPointerException(message);
		}
		
		return arg;
	}
	
	/**
	 * <p>Asserts that the given argument is <b>{@code not null}</b>. If the argument is {@code null} 
	 * a {@link NullPointerException} will be thrown with the message, <i>"The supplied &lt;type&gt; 
	 * was found to be &lt;null&gt;"</i>.</p>
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
	public static <T extends Object> T assertNotNull(T arg, Class<T> type) {
		
		if(arg == null) {
			
			throw new NullPointerException(new StringBuilder("The supplied ")
				.append(type == null? "argument" : type.getName()).append(" was found to be <null>").toString());
		}
		
		return arg;
	}

	/**
	 * <p>Asserts that the given argument is neither <b>{@code null} nor {@code empty}</b>. The null 
	 * check is performed using {@link #assertNotNull(Object)}. If the argument is {@code empty} a 
	 * {@link NullPointerException} will be thrown with the message, <i>"The supplied argument was 
	 * found to be &lt;empty&gt;"</i>.</p>
	 * 
	 * <p><b>Note</b> that only the following types are accepted: {@link CharSequence}, {@link Collection}, 
	 * {@link Map}, {@code Object[]}, {@code boolean[]}, {@code char[]}, {@code byte[]}, {@code short[]}, 
	 * {@code int[]}, {@code long[]}, {@code float[]}, {@code double[]}. <b>All other types will manage to 
	 * pass this assertion</b> (granted the argument is {@code not null}).</p>
	 *
	 * @param arg
	 * 			the argument to be asserted as being {@code not empty}
	 * <br><br>
	 * @return the argument which was asserted to be {@code not empty}
	 * <br><br>
	 * @throws NullPointerException
	 * 			if the supplied argument was found to be {@code null}
	 * <br><br>
	 * @throws IllegalArgumentException
	 * 			if the supplied argument was found to be {@code empty}
	 * <br><br>
	 * @since 1.2.4
	 */
	public static <T extends Object> T assertNotEmpty(T arg) {
		
		assertNotNull(arg);
		
		boolean isEmpty = (arg instanceof CharSequence && ((CharSequence)arg).length() == 0) || 
						  (arg instanceof Collection<?> && ((Collection<?>)arg).size() == 0) || 
						  (arg instanceof Map<?, ?> && ((Map<?, ?>)arg).size() == 0) || 
						  (arg instanceof Object[] && ((Object[])arg).length == 0) || 
						  (arg instanceof boolean[] && ((boolean[])arg).length == 0) || 
						  (arg instanceof char[] && ((char[])arg).length == 0) || 
						  (arg instanceof byte[] && ((byte[])arg).length == 0) || 
						  (arg instanceof short[] && ((short[])arg).length == 0) || 
						  (arg instanceof int[] && ((int[])arg).length == 0) || 
						  (arg instanceof long[] && ((long[])arg).length == 0) || 
						  (arg instanceof float[] && ((float[])arg).length == 0) || 
						  (arg instanceof double[] && ((double[])arg).length == 0);
		
		if(isEmpty) {
			
			throw new IllegalArgumentException("The supplied argument was found to be <empty>."); 
		}
		
		return arg;
	}
	
	/**
	 * <p>Asserts that the given argument is not <b>{@code null} and has the specified number of elements. 
	 * The null check is performed using {@link #assertNotNull(Object)}. If the argument does not contain 
	 * the specified number of elements {@link IllegalArgumentException} will be thrown with the message, 
	 * <i>"The supplied argument did not contain the required number of elements"</i>.</p>
	 * 
	 * <p><b>Note</b> that only the following types are accepted: {@link CharSequence}, {@link Collection}, 
	 * {@link Map}, {@code Object[]}, {@code boolean[]}, {@code char[]}, {@code byte[]}, {@code short[]}, 
	 * {@code int[]}, {@code long[]}, {@code float[]}, {@code double[]}. <b>All other types will manage to 
	 * pass this assertion</b> (granted the argument is {@code not null}).</p>
	 *
	 * @param arg
	 * 			the argument to be asserted as being {@code not empty}
	 * <br><br>
	 * @param length
	 * 			the acceptable number of required elements
	 * @param optional
	 * 			any optional lengths which might be acceptable 
	 * <br><br>
	 * @return the argument which was asserted to be of the required length
	 * <br><br>
	 * @throws NullPointerException
	 * 			if the supplied argument was found to be {@code null}
	 * <br><br>
	 * @throws IllegalArgumentException
	 * 			if the supplied argument did not contain the required number of arguments
	 * <br><br>
	 * @since 1.2.4
	 */
	public static <T extends Object> T assertLength(T arg, long length, long... optional) {
		
		assertNotNull(arg);
		
		long actualLength = arg instanceof CharSequence? ((CharSequence)arg).length() : 
							arg instanceof Collection<?>? ((Collection<?>)arg).size() : 
						    arg instanceof Map<?, ?>? ((Map<?, ?>)arg).size() : 
						    arg instanceof Object[]? ((Object[])arg).length : 
						    arg instanceof boolean[]? ((boolean[])arg).length : 
						    arg instanceof char[]? ((char[])arg).length : 
						    arg instanceof byte[]? ((byte[])arg).length : 
						    arg instanceof short[]? ((short[])arg).length : 
						    arg instanceof int[]? ((int[])arg).length : 
						    arg instanceof long[]? ((long[])arg).length : 
						    arg instanceof float[]? ((float[])arg).length : 
						    arg instanceof double[]? ((double[])arg).length :0;
						    
		boolean illegal = actualLength != length;
		
		if(illegal == true && optional != null && optional.length > 0) {
			
			for (long optionalLength : optional) {
				
				if(optionalLength == actualLength) {
					
					return arg;
				}
			}
		}
		
		if(illegal) {
			
			throw new IllegalArgumentException("The supplied argument did not contain the required number of elements."); 
		}
		
		return arg;
	}
	
	/**
	 * <p>Asserts that the given context is valid using the supplied {@link Validator} along with 
	 * a few other trivial validations such as a {@code null} check.</p>
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
	public static <T> T assertValid(T context, Validator<T> validator) {
	
		assertNotNull(context);
		
		if(validator != null) {
				
			validator.validate(context);
		}
		
		return context;
	}
}
