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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lonepulse.robozombie.annotation.Detach;
import com.lonepulse.robozombie.annotation.Skip;
import com.lonepulse.robozombie.proxy.InvocationContext;

/**
 * <p>A collection of utility services for processing components attached to an endpoint contract.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @category utility
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class Components {
	
	
	private Components() {}
	
	
	/**
	 * <p>Determines whether the request definition has <i>detached</i> the given {@link Annotation} type. 
	 * This will mute any annotations of the given type which are defined on the endpoint.</p>
	 *
	 * <p>See {@link Detach}.</p>
	 *
	 * @param context
	 * 			the {@link InvocationContext} which provides the request definition
	 * <br><br>
	 * @param type
	 * 			the {@link Annotation} type whose detachment is to be determined
	 * <br><br>
	 * @return {@code true} if the given {@link Annotation} type has been detached from the request
	 * <br><br>
	 * @since 1.3.0
	 */
	public static boolean isDetached(InvocationContext context, Class<? extends Annotation> type) {
		
		Method request = context.getRequest();
		
		return request.isAnnotationPresent(Detach.class) && 
			   Arrays.asList(request.getAnnotation(Detach.class).value()).contains(type);
	}
	 
	/**
	 * <p>Accepts a list of components which are presumably attached to a request and removes those which 
	 * are skipped from execution by consulting an available <code>@Skip</code> annotation on the request.</p>
	 * 
	 * <p>See {@link Skip}.</p>
	 *
	 * @param context
	 * 			the {@link InvocationContext} used to discover request metadata
	 * <br><br> 
	 * @param components
	 * 			the {@link Class}es of the components to be filtered
	 * <br><br> 
	 * @return the filtered components list which excludes those which are skipped for the given request
	 *
	 * @since 1.2.4
	 */
	public static <E> List<Class<? extends E>> 
		filterSkipped(InvocationContext context, List<Class<? extends E>> components) {
		
		Method request = context.getRequest();
		
		if(!request.isAnnotationPresent(Skip.class)) {
			
			return components;
		}
		
		List<Class<?>> skippedComponents = Arrays.asList(request.getAnnotation(Skip.class).value());
		List<Class<? extends E>> filteredComponents = new ArrayList<Class<? extends E>>();

		for (Class<? extends E> component : components) {
			
			if(!skippedComponents.contains(component)) {
				
				filteredComponents.add(component);
			}
		}
		
		return filteredComponents;
	}
}
