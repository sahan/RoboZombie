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


import static com.lonepulse.robozombie.util.Assert.assertNotEmpty;
import static com.lonepulse.robozombie.util.Assert.assertNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <p>Fluent filtering for a collection of {@link Field}s.</p> 
 * 
 * <p><b>Note</b> that two {@link Field}s are determined to be equal of and only if they were declared 
 * in the same {@link Class}, using the same name and type.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @category utility
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class Fields implements Iterable<Field> {
	
	
	/**
	 * <p>This contract defines a strategy for filtering the {@link Field}s within an instance 
	 * of {@link Fields} by evaluating each {@link Field} to determine if it should be included 
	 * in the filtered result.</p>
	 * 
	 * <p>Instances of {@link Criterion} can be used via {@link Fields#filter(Criterion)}.</p>
	 * 
	 * @version 1.1.0
	 * <br><br>
	 * @since 1.2.4
	 * <br><br>
	 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	public static interface Criterion {
		
		boolean evaluate(Field field);
	}
	
	private List<Field> filter(Criterion criterion) {
		
		List<Field> filteredFields = new ArrayList<Field>();
		
		for (Field field : fields) {
			
			if(criterion.evaluate(field)) {
				
				filteredFields.add(field);
			}
		}
		
		return filteredFields;
	}
	
	
	private Collection<Field> fields = null;
	
	
	/**
	 * <p>Creates a new instance of {@link Fields} from the given <b>object</b> by extracting all its 
	 * member {@link Field}s.</p> 
	 * 
	 * <p>See {@link Object#getClass()} and {@link Class#getDeclaredFields()}.</p>
	 *
	 * @param target
	 * 			the object whose fields are to extracted
	 * <br><br>
	 * @return a new instance of {@link Fields} for the {@link Field}s on the given object
	 * <br><br>
	 * @since 1.2.4
	 */
	public static Fields in(Object target) {
		
		return new Fields(target.getClass().getDeclaredFields());
	}
	
	/**
	 * <p>Creates a new instance of {@link Fields} from the given <b>{@link Class}</b> by extracting 
	 * all its member {@link Field}s.</p> 
	 * 
	 * <p>See {@link Class#getDeclaredFields()}.</p>
	 *
	 * @param target
	 * 			the {@link Class} whose fields are to extracted
	 * 
	 * @return a new instance of {@link Fields} for the {@link Field}s on the given {@link Class}
	 * 
	 * @since 1.2.4
	 */
	public static Fields in(Class<? extends Object> target) {
		
		return new Fields(target.getDeclaredFields());
	}
	
	private Fields(Field[] fields) {
		
		assertNotNull(fields, Field[].class);
		this.fields = Collections.unmodifiableList(Arrays.asList(fields));
	}
	
	private Fields(Collection<Field> fields) {
		
		assertNotNull(fields, Collection.class);
		this.fields = Collections.unmodifiableCollection(fields);
	}
	
	/**
	 * <p>Filters the {@link Field}s which are annotated with the given annotation and returns a new 
	 * instance of {@link Fields} that wrap the filtered collection.</p>
	 * 
	 * @param annotation
	 * 			the {@link Field}s annotated with this type will be filtered
	 * <br><br>
	 * @return a <b>new instance</b> of {@link Fields} which wraps the filtered {@link Field}s
	 * <br><br>
	 * @since 1.2.4
	 */
	public Fields annotatedWith(final Class<? extends Annotation> annotation) {
		
		assertNotNull(annotation, Class.class);
		
		return new Fields(filter(new Criterion() {

			@Override
			public boolean evaluate(Field field) {
				
				return field.isAnnotationPresent(annotation);
			}
		}));
	}
	
	/**
	 * <p>Filters the {@link Field}s which are annotated with <b>all</b> the given annotations and 
	 * returns a new instance of {@link Fields} that wrap the filtered collection.</p>
	 * 
	 * @param annotation
	 * 			the {@link Field}s annotated with <b>all</b> these types will be filtered
	 * <br><br>
	 * @return a <b>new instance</b> of {@link Fields} which wraps the filtered {@link Field}s
	 * <br><br>
	 * @since 1.2.4
	 */
	public Fields annotatedWithAll(final Class<? extends Annotation>... annotations) {
		
		assertNotNull(annotations, Class[].class);
		
		return new Fields(filter(new Criterion() {

			@Override
			public boolean evaluate(Field field) {
				
				boolean hasAllAnnotations = true;
				
				for (Class<? extends Annotation> annotation : annotations) {
				
					if(!field.isAnnotationPresent(annotation)) {
						
						hasAllAnnotations = false;
						break;
					}
				}
				
				return hasAllAnnotations;
			}
		}));
	}
	
	/**
	 * <p>Filters the {@link Field}s which are annotated with <b>any</b> of the given annotations and 
	 * returns a new instance of {@link Fields} that wrap the filtered collection.</p>
	 * 
	 * @param annotation
	 * 			the {@link Field}s annotated with <b>any</b> of these types will be filtered
	 * <br><br>
	 * @return a <b>new instance</b> of {@link Fields} which wraps the filtered {@link Field}s
	 * <br><br>
	 * @since 1.2.4
	 */
	public Fields annotatedWithAny(final Class<? extends Annotation>... annotations) {
		
		assertNotNull(annotations, Class[].class);
		
		return new Fields(filter(new Criterion() {

			@Override
			public boolean evaluate(Field field) {
				
				for (Class<? extends Annotation> annotation : annotations) {
					
					if(field.isAnnotationPresent(annotation)) {
						
						return true;
					}
				}
				
				return false;
			}
		}));
	}
	
	/**
	 * <p>Filters the {@link Field}s whose name equals (case-sensitive) the given name and returns 
	 * a new instance of {@link Fields} that wrap the filtered collection.</p> 
	 *
	 * @param fieldName
	 * 			the {@link Field}s having this name will be filtered
	 * <br><br>
	 * @return a <b>new instance</b> of {@link Fields} which wraps the filtered {@link Field}s
	 * <br><br>
	 * @since 1.2.4
	 */
	public Fields named(final String fieldName) {
		
		assertNotEmpty(fieldName);
		
		return new Fields(filter(new Criterion() {

			@Override
			public boolean evaluate(Field field) {
				
				return field.getName().equals(fieldName);
			}
		}));
	}
	
	/**
	 * <p>Filters the {@link Field}s whose <b>case insensitive</b> name equals the given name and 
	 * returns a new instance of {@link Fields} that wrap the filtered collection. 
	 *
	 * @param fieldName
	 * 			the {@link Field}s having this case insensitive name will be filtered
	 * 
	 * @return a <b>new instance</b> of {@link Fields} which wraps the filtered {@link Field}s
	 * 
	 * @since 1.2.4
	 */
	public Fields strictlyNamed(final String fieldName) {
		
		assertNotEmpty(fieldName);
		
		return new Fields(filter(new Criterion() {

			@Override
			public boolean evaluate(Field field) {
				
				return field.getName().equalsIgnoreCase(fieldName);
			}
		}));
	}
	
	/**
	 * <p>Filters the {@link Field}s whose type <b>can be assigned</b> to the given type and 
	 * returns a new instance of {@link Fields} that wrap the filtered collection.</p> 
	 *
	 * @param type
	 * 			the {@link Class} type of the {@link Field}s to be filtered 
	 * <br><br>
	 * @return a <b>new instance</b> of {@link Fields} which wraps the filtered {@link Field}s
	 * <br><br>
	 * @since 1.2.4
	 */
	public Fields ofType(final Class<?> type) {
		
		assertNotEmpty(type);
		
		return new Fields(filter(new Criterion() {

			@Override
			public boolean evaluate(Field field) {
				
				return type.isAssignableFrom(field.getType());
			}
		}));
	}
	
	/**
	 * <p>Filters the {@link Field}s whose type <b>equals</b> the given type and returns a new 
	 * instance of {@link Fields} that wrap the filtered collection.</p> 
	 *
	 * @param type
	 * 			the {@link Class} type of the {@link Field}s to be filtered 
	 * <br><br>
	 * @return a <b>new instance</b> of {@link Fields} which wraps the filtered {@link Field}s
	 * <br><br>
	 * @since 1.2.4
	 */
	public Fields strictlyOfType(final Class<?> type) {
		
		assertNotEmpty(type);
		
		return new Fields(filter(new Criterion() {

			@Override
			public boolean evaluate(Field field) {
				
				return field.getType().equals(type);
			}
		}));
	}
	
	/**
	 * <p>Finds the <b>difference</b> between this instance and a supplied instance. Returns a new 
	 * instance of {@link Fields} with only those {@link Field}s which unique to this instance.</p> 
	 * 
	 * <p><pre>
	 * Difference can be expressed as, 
	 * <code>
	 * A - B = { x &isin; A and x &notin; B }
	 * </code> 
	 * where,
	 * 
	 * A = this Fields instance
	 * B = supplied Fields instance
	 * x = any field from the resulting Fields instance 
	 * </pre></p>
	 *
	 * @param fields
	 * 			the instance of {@link Fields} whose common items are subtracted from this instance
	 * <br><br>
	 * @return a new instance of {@link Fields} which wraps only the only those {@link Field}s which 
	 * 		   are unique to this instance
	 * <br><br>
	 * @since 1.2.4
	 */
	public Fields difference(Fields fields) {
		
		assertNotNull(fields);
		
		Set<Field> view = new HashSet<Field>(this.fields);
		view.removeAll(new HashSet<Field>(fields.fields));
		
		return new Fields(view);
	}
	
	/**
	 * <p>Finds the <b>union</b> between this instance and a supplied instance. Reeturns a new 
	 * instance of {@link Fields} which contains a <b>set of all</b> the {@link Field}s which in 
	 * both this instance and the supplied instance.</p>
	 * 
	 * <p><pre>
	 * Union can be expressed as, 
	 * <code>
	 * A &cup; B = { x : x &isin; A or x &isin; B }
	 * </code> 
	 * where,
	 * 
	 * A = this Fields instance
	 * B = supplied Fields instance
	 * x = any field from the resulting Fields instance 
	 * </pre></p>
	 * 
	 * @param fields
	 * 			the instance of {@link Fields} whose {@link Field}s are added to this instance
	 * <br><br>
	 * @return a new instance of {@link Fields} which wraps all the unique {@link Field}s in this 
	 * 		   instance and the given instance
	 * <br><br>
	 * @since 1.2.4
	 */
	public Fields union(Fields fields) {
		
		assertNotNull(fields);
		
		Set<Field> view = new HashSet<Field>(this.fields);
		view.addAll(new HashSet<Field>(fields.fields));
		
		return new Fields(view);
	}
	
	/**
	 * <p>Finds the <b>intersection</b> between this instance and a supplied instance. Returns a new 
	 * instance of {@link Fields} which contains all the {@link Field}s <b>common</b> to both instances.</b>
	 * 
	 * <p><pre>
	 * Intersection can be expressed as, 
	 * <code>
	 * A &cap; B = { x : x &isin; A and x &isin; B }
	 * </code> 
	 * where,
	 * 
	 * A = this Fields instance
	 * B = supplied Fields instance
	 * x = any field from the resulting Fields instance 
	 * </pre></p>
	 * 
	 * @param fields
	 * 			the instance of {@link Fields} whose common items are discovered
	 * <br><br>
	 * @return a new instance of {@link Fields} which wraps only the only those {@link Field}s which 
	 * 		   are common between this instance and the passed instance
	 * <br><br>
	 * @since 1.2.4
	 */
	public Fields intersection(Fields fields) {
		
		assertNotNull(fields);
		
		Set<Field> view = new HashSet<Field>(this.fields);
		view.retainAll(new HashSet<Field>(fields.fields));
		
		return new Fields(view);
	}
	
	/**
	 * <p>Filters the {@link Field}s which match the given {@link Criterion} and returns a new instance 
	 * of {@link Fields} that wrap the filtered collection.</p>
	 *
	 * @param criterion
	 * 			the {@link Criterion} whose evaluation determines the filtered field
	 * <br><br>
	 * @return a <b>new instance</b> of {@link Fields} which wraps the filtered {@link Field}s
	 * <br><br>
	 * @since 1.2.4
	 */
	public Fields matching(Criterion criterion) {
		
		return new Fields(filter(criterion));
	}
	
	/**
	 * <p>Allows the {@link Field}s envelopped by this instance of {@link Fields} to be traversed sequentially 
	 * using the returned {@link Iterator}.</p> 
	 * 
	 * <p><b>Note</b> this {@link Iterator} does not allow the underlying {@link Field}s to be modified, for 
	 * example using {@link Iterator#remove()}. Doing so will result in an {@link UnsupportedOperationException}.</p>
	 *
	 * @return the iterator which allows the enclosed {@link Field}s to traversed sequentially
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public Iterator<Field> iterator() {
		
		return this.fields.iterator();
	}
}
