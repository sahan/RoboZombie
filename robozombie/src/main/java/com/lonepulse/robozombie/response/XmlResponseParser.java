package com.lonepulse.robozombie.response;

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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.lonepulse.robozombie.inject.InvocationContext;

/**
 * <p>This is an extension of {@link AbstractResponseParser} which allows an <b>XML response entity</b> 
 * to be <i>deserialized</i> into an instance of its model.</p>
 * 
 * <p><b>Note</b> that this parser uses <a href="http://www.oracle.com/technetwork/articles/javase/index-140168.html">
 * JAXB</a> and is dependent upon the model being annotated with @{@link XmlRootElement} at minimum.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class XmlResponseParser extends AbstractResponseParser<Object> {

	
	/**
	 * <p>Creates a new instance of {@link XmlResponseParser} and register the generic 
	 * type {@link Object} as the entity which results from its <i>parse</i> operation.
	 *
	 * @since 1.2.4
	 */
	public XmlResponseParser() {
		
		super(Object.class);
	}
	
	/**
     * <p>Parses the XML content in the {@link HttpResponse} entity using JAXB and returns the constructed 
     * instance of the model.</p>
     * 
     * <p>See {@link AbstractResponseParser#processResponse(HttpResponse, InvocationContext)}.
     * 
     * @throws JAXBException 
     * 			if the response XML content failed to be deserialized into the specified model
     * 
     * @since 1.2.4
	 */
	@Override
	protected Object processResponse(HttpResponse httpResponse, InvocationContext config) 
	throws Exception {
		
		JAXBContext jaxbContext = JAXBContext.newInstance(config.getRequest().getReturnType());
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		InputStream is = new ByteArrayInputStream(EntityUtils.toByteArray(httpResponse.getEntity()));
		
		return jaxbUnmarshaller.unmarshal(is);
	}
}
