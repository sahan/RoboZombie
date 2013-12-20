package com.lonepulse.robozombie.processor;

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

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;

import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Entity;
import com.lonepulse.robozombie.annotation.FormParam;
import com.lonepulse.robozombie.annotation.FormParams;
import com.lonepulse.robozombie.annotation.GET;
import com.lonepulse.robozombie.annotation.POST;
import com.lonepulse.robozombie.annotation.PUT;
import com.lonepulse.robozombie.annotation.Param;
import com.lonepulse.robozombie.annotation.QueryParam;
import com.lonepulse.robozombie.annotation.QueryParams;
import com.lonepulse.robozombie.model.User;

/**
 * <p>An endpoint with request definitions which accept request parameters.</p>
 * 
 * @version 1.1.1
 * <br><br> 
 * @since 1.3.0
 * <br><br> 
 * @category test
 * <br><br> 
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Endpoint("http://0.0.0.0:8080")
public interface RequestParamEndpoint {
	
	/**
	 * <p>Sends a request with the given query parameters.</p>
	 * 
	 * @param firstName
	 * 			the first query param
	 * 
	 * @param lastName
	 * 			the second query param
	 * 
	 * @since 1.3.0
	 */
	@GET("/queryparams")
	public void queryParams(@QueryParam("firstName") String firstName, 
							@QueryParam("lastName") String lastName);
	
	/**
	 * <p>Sends a request with the given form parameters.</p>
	 * 
	 * @param firstName
	 * 			the first form param
	 * 
	 * @param lastName
	 * 			the second form param
	 * 
	 * @since 1.3.0
	 */
	@POST("/formparams")
	public void formParams(@FormParam("firstName") String firstName, 
						   @FormParam("lastName") String lastName);
	
	/**
	 * <p>Sends a request with an illegal query parameter.</p>
	 * 
	 * @param user
	 * 			an illegal query parameter of type {@link User}
	 * 
	 * @since 1.3.0
	 */
	@GET("/queryparamsfail")
	public void queryParamsFail(@QueryParam("user") User user);
	
	/**
	 * <p>Sends a request with an illegal form parameter.</p>
	 * 
	 * @param user
	 * 			an illegal form parameter of type {@link User}
	 * 
	 * @since 1.3.0
	 */
	@POST("/formparamsfail")
	public void formParamsFail(@FormParam("user") User user);
	
	/**
	 * <p>Sends a request with a set of query parameters provided as a batch.</p>
	 * 
	 * @param params
	 * 			the map of basic name and value pairs 
	 * 
	 * @since 1.3.0
	 */
	@GET("/queryparamsbatch")
	public void queryParamsBatch(@QueryParams Map<String, String> params);
	
	/**
	 * <p>Sends a request with a set of form parameters provided as a batch.</p>
	 * 
	 * @param params
	 * 			the map of basic name and value pairs 
	 * 
	 * @since 1.3.0
	 */
	@POST("/formparamsbatch")
	public void formParamsBatch(@FormParams Map<String, String> params);
	
	/**
	 * <p>Sends a request with an illegal query parameter batch type.</p>
	 * 
	 * @param params
	 * 			an illegal batch query parameter of type {@link List}
	 * 
	 * @since 1.3.0
	 */
	@GET("/queryparamsbatchtypefail")
	public void queryParamsBatchTypeFail(@QueryParams List<String> params);
	
	/**
	 * <p>Sends a request with an illegal form parameter batch type.</p>
	 * 
	 * @param params
	 * 			an illegal batch form parameter of type {@link List}
	 * 
	 * @since 1.3.0
	 */
	@POST("/formparamsbatchtypefail")
	public void formParamsBatchTypeFail(@FormParams List<String> params);
	
	/**
	 * <p>Sends a request with an illegal batch query param element.</p>
	 * 
	 * @param params
	 * 			an illegal query parameter element of type {@link User}
	 * 
	 * @since 1.3.0
	 */
	@GET("/queryparamsbatchelementfail")
	public void queryParamsBatchElementFail(@QueryParams Map<String, User> params);
	
	/**
	 * <p>Sends a request with an illegal batch form param element.</p>
	 * 
	 * @param params
	 * 			an illegal form parameter element of type {@link User}
	 * 
	 * @since 1.3.0
	 */
	@POST("/formparamsbatchelementfail")
	public void formParamsBatchElementFail(@FormParams Map<String, User> params);
	
	/**
	 * <p>Sends a request with a multivalued query parameter.</p>
	 * 
	 * @param params
	 * 			the map of with an entry for a multivalued query parameter
	 * 
	 * @since 1.3.0
	 */
	@GET("/queryparamsmultivalued")
	public void queryParamsMultivalued(@QueryParams Map<String, List<String>> params);
	
	/**
	 * <p>Sends a request with a multivalued form parameter.</p>
	 * 
	 * @param params
	 * 			the map of with an entry for a multivalued form parameter
	 * 
	 * @since 1.3.0
	 */
	@POST("/formparamsmultivalued")
	public void formParamsMultivalued(@FormParams Map<String, List<String>> params);
	
	/**
	 * <p>Sends a request with an illegal multivalued query parameter.</p>
	 * 
	 * @param params
	 * 			the map with an illegal multivalued query parameter of type {@link User}
	 * 
	 * @since 1.3.0
	 */
	@GET("/queryparamsmultivaluedfail")
	public void queryParamsMultivaluedFail(@QueryParams Map<String, List<User>> params);
	
	/**
	 * <p>Sends a request with an illegal multivalued form parameter.</p>
	 * 
	 * @param params
	 * 			the map with an illegal multivalued form parameter of type {@link User}
	 * 
	 * @since 1.3.0
	 */
	@POST( "/formparamsmultivaluedfail")
	public void formParamsMultivaluedFail(@FormParams Map<String, List<User>> params);
	
	/**
	 * <p>Sends a request with a set of constant query parameters.</p>
	 * 
	 * @since 1.3.0
	 */
	@GET("/constantqueryparams")
	@QueryParams({@Param(name = "firstName", value = "Doctor"),
				  @Param(name = "lastName", value = "Who")})
	public void constantQueryParams();
	
	/**
	 * <p>Sends a request with a set of constant query parameters defined inline.</p>
	 * 
	 * @since 1.3.0
	 */
	@GET("/inlineconstantqueryparams")
	public void inlineConstantQueryParams(
		@QueryParams({@Param(name = "class", value = "omega")}) Map<String, String> params);
	
	/**
	 * <p>Sends a request with a set of constant form parameters.</p>
	 * 
	 * @since 1.3.0
	 */
	@POST("/constantformparams")
	@FormParams({@Param(name = "firstName", value = "Beta-Ray"),
				 @Param(name = "lastName", value = "Bill")})
	public void constantFormParams();
	
	/**
	 * <p>Sends a request with a set of constant form parameters defined inline.</p>
	 * 
	 * @since 1.3.0
	 */
	@POST("/inlineconstantformparams")
	public void inlineConstantFormParams(
		@FormParams({@Param(name = "class", value = "omega")}) Map<String, String> params);
	
	/**
	 * <p>Sends a request with a {@code byte[]} which should be resolved to an 
	 * instance of {@link ByteArrayEntity}.
	 * 
	 * @param entity
	 * 			the {@code byte[]} to be converted to a {@link ByteArrayEntity}
	 * 
	 * @since 1.3.0
	 */
	@PUT("/primitivebytearrayentity")
	public void primitiveByteArrayEntity(@Entity byte[] entity);
	
	/**
	 * <p>Sends a request with a {@code Byte}[] which should be resolved to an instance 
	 * of {@link ByteArrayEntity}.</p>
	 * 
	 * @param entity
	 * 			the {@code Byte}[] to be converted to a {@link ByteArrayEntity}
	 * 
	 * @since 1.3.0
	 */
	@PUT("/wrapperbytearrayentity")
	public void wrapperByteArrayEntity(@Entity Byte[] entity);
	
	/**
	 * <p>Sends a request with a {@link File} which should be resolved to an instance 
	 * of {@link FileEntity}.</p>
	 * 
	 * @param entity
	 * 			the {@link File} to be converted to a {@link FileEntity}
	 * 
	 * @since 1.3.0
	 */
	@PUT("/fileentity")
	public void fileEntity(@Entity File entity);
	
	/**
	 * <p>Sends a request with an {@link InputStream} which should be resolved to an 
	 * instance of {@link BufferedHttpEntity}.</p>
	 * 
	 * @param entity
	 * 			the {@link InputStream} to be converted to a {@link BufferedHttpEntity}
	 * 
	 * @since 1.3.0
	 */
	@PUT("/bufferedhttpentity")
	public void bufferedHttpEntity(@Entity InputStream entity);
	
	/**
	 * <p>Sends a request with a {@link String} which should be resolved to an instance 
	 * of {@link StringEntity}.</p>
	 * 
	 * @param entity
	 * 			the {@link String} to be converted to a {@link StringEntity}
	 * 
	 * @since 1.3.0
	 */
	@PUT("/stringentity")
	public void stringEntity(@Entity String entity);
	
	/**
	 * <p>Sends a request with a {@link Serializable} which should be resolved to an 
	 * instance of {@link SerializableEntity}.</p>
	 * 
	 * @param entity
	 * 			the {@link Serializable} instance of {@link User} to be converted 
	 * 			to a {@link SerializableEntity}
	 * 
	 * @since 1.3.0
	 */
	@PUT("/serializableentity")
	public void serializableEntity(@Entity User entity);
	
	/**
	 * <p>Sends a PUT request without an entity. This should generate an exception 
	 * which signifies a violation of the HTTP 1.1 specification.</p>
	 * 
	 * @since 1.3.0
	 */
	@PUT("/missingentity")
	public void missingEntity();
	
	/**
	 * <p>Sends a PUT request with multiple entities. This should generate an exception 
	 * which signifies a violation of the HTTP 1.1 specification.</p>
	 * 
	 * @param entity1
	 * 			the first entity which is designated to be sent
	 * 
	 * @param entity2
	 * 			the second entity which is designated to be sent
	 * 
	 * @since 1.3.0
	 */
	@PUT("/multipleentity")
	public void multipleEntity(@Entity String entity1, @Entity String entity2);
	
	/**
	 * <p>Sends a PUT request with an argument which cannot be resolved to an entity. This 
	 * should generate an exception which signifies a violation of the HTTP 1.1 specification.</p>
	 * 
	 * @param unresolvableEntity
	 * 			an generic object which cannot be resolved to an entity 
	 * 
	 * @since 1.3.0
	 */
	@PUT("/resolutionfailedentity")
	public void resolutionFailedEntity(@Entity Object unresolvableEntity);
}
