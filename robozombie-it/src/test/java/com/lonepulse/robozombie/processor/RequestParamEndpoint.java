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

import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;

import com.lonepulse.robozombie.annotation.Endpoint;
import com.lonepulse.robozombie.annotation.Entity;
import com.lonepulse.robozombie.annotation.Parser;
import com.lonepulse.robozombie.annotation.Parser.ParserType;
import com.lonepulse.robozombie.annotation.QueryParam;
import com.lonepulse.robozombie.annotation.Request;
import com.lonepulse.robozombie.model.User;
import com.lonepulse.robozombie.request.RequestMethod;

/**
 * <p>An interface which represents a dummy endpoint with request method definitions 
 * that accept request parameters.
 * 
 * @category test
 * <br><br> 
 * @version 1.1.1
 * <br><br> 
 * @since 1.2.4
 * <br><br> 
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Parser(ParserType.STRING)
@Endpoint(host = "0.0.0.0", port = "8080")
public interface RequestParamEndpoint {
	
	/**
	 * <p>Sends a request with the given query parameters.
	 * 
	 * @param firstName
	 * 			the first query param
	 * 
	 * @param lastName
	 * 			the second query param
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/queryparams")
	public String queryParams(@QueryParam("firstName") String firstName, 
							  @QueryParam("lastName") String lastName);
	
	/**
	 * <p>Sends a request with a set of constant query paramers.
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/constantqueryparams", 
			 params = {@Request.Param(name = "firstName", value = "Doctor"), 
					   @Request.Param(name = "lastName", value = "Who")})
	public String constantQueryParams();

	/**
	 * <p>Sends a request with a {@code byte[]} which should be resolved to an 
	 * instance of {@link ByteArrayEntity}.
	 * 
	 * @param entity
	 * 			the {@code byte[]} to be converted to a {@link ByteArrayEntity}
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/primitivebytearrayentity", method = RequestMethod.PUT)
	public void primitiveByteArrayEntity(@Entity byte[] entity);
	
	/**
	 * <p>Sends a request with a {@code Byte}[] which should be resolved to an 
	 * instance of {@link ByteArrayEntity}.
	 * 
	 * @param entity
	 * 			the {@code Byte}[] to be converted to a {@link ByteArrayEntity}
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/wrapperbytearrayentity", method = RequestMethod.PUT)
	public void wrapperByteArrayEntity(@Entity Byte[] entity);
	
	/**
	 * <p>Sends a request with a {@link File} which should be resolved to an 
	 * instance of {@link FileEntity}.
	 * 
	 * @param entity
	 * 			the {@link File} to be converted to a {@link FileEntity}
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/fileentity", method = RequestMethod.PUT)
	public void fileEntity(@Entity File entity);
	
	/**
	 * <p>Sends a request with an {@link InputStream} which should be resolved 
	 * to an instance of {@link BufferedHttpEntity}.
	 * 
	 * @param entity
	 * 			the {@link InputStream} to be converted to a {@link BufferedHttpEntity}
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/bufferedhttpentity", method = RequestMethod.PUT)
	public void bufferedHttpEntity(@Entity InputStream entity);
	
	/**
	 * <p>Sends a request with a {@link String} which should be resolved to an 
	 * instance of {@link StringEntity}.
	 * 
	 * @param entity
	 * 			the {@link String} to be converted to a {@link StringEntity}
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/stringentity", method = RequestMethod.PUT)
	public void stringEntity(@Entity String entity);
	
	/**
	 * <p>Sends a request with a {@link Serializable} which should be resolved to 
	 * an instance of {@link SerializableEntity}.
	 * 
	 * @param entity
	 * 			the {@link Serializable} instance of {@link User} to be converted 
	 * 			to a {@link SerializableEntity}
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/serializableentity", method = RequestMethod.PUT)
	public void serializableEntity(@Entity User entity);
	
	/**
	 * <p>Sends a PUT request without an entity. This should generate an exception 
	 * which signifies a violation of the HTTP 1.1 specification.
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/missingentity", method = RequestMethod.PUT)
	public void missingEntity();
	
	/**
	 * <p>Sends a PUT request with multiple entities. This should generate an exception 
	 * which signifies a violation of the HTTP 1.1 specification.
	 * 
	 * @param entity1
	 * 			the first entity which is designated to be sent
	 * 
	 * @param entity2
	 * 			the second entity which is designated to be sent
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/multipleentity", method = RequestMethod.PUT)
	public void multipleEntity(@Entity String entity1, @Entity String entity2);
	
	/**
	 * <p>Sends a PUT request with an argument which cannot be resolved to an entity. 
	 * This should generate an exception which signifies a violation of the HTTP 1.1 
	 * specification.
	 * 
	 * @param unresolvableEntity
	 * 			an generic object which cannot be resolved to an entity 
	 * 
	 * @since 1.2.4
	 */
	@Request(path = "/resolutionfailedentity", method = RequestMethod.PUT)
	public void resolutionFailedEntity(@Entity Object unresolvableEntity);
}
