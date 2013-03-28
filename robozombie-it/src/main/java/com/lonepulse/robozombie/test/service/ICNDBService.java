package com.lonepulse.robozombie.test.service;

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

import com.lonepulse.robozombie.core.annotation.Bite;
import com.lonepulse.robozombie.core.inject.Zombie;
import com.lonepulse.robozombie.test.endpoint.ICNDBEndpoint;

/**
 * <p>Emulates a service which works with an {@link ICNDBEndpoint}.</p>
 * 
 * <p>Contains serveral {@link ICNDBEndpoint} members with various 
 * accessibility qualifiers to test the {@link Zombie}'s property injection 
 * capabilities.</p>
 * 
 * <p>Also contains a mutator and a parameterized contructor to test 
 * setter and contructor injection.</p>
 * 
 * @category test
 * <br><br> 
 * @version 1.0.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class ICNDBService {
	
	
	/** <p>A public instance of {@link ICNDBEndpoint} */
	@Bite
	public ICNDBEndpoint publicICNDDBEndpoint;
	
	/** <p>A protected instance of {@link ICNDBEndpoint} */
	@Bite
	protected ICNDBEndpoint protectedICNDDBEndpoint;
	
	/** <p>A package private instance of {@link ICNDBEndpoint} */
	@Bite
	ICNDBEndpoint defaultICNDDBEndpoint;
	
	/** 
	 * <p>A private instance of {@link ICNDBEndpoint} which is 
	 * to be injected via it's mutator.
	 */
	@Bite
	private ICNDBEndpoint privateICNDDBEndpoint;
	
	/**  
	 * <p>A private instance of {@link ICNDBEndpoint} which is 
	 * to be injected via the constructor.
	 */
	@Bite
	private ICNDBEndpoint constructedICNDBEndpoint;
	
	/**  
	 * <p>A private instance of {@link ICNDBEndpoint} which is 
	 * to be injected by changing the <b>accessibility properties</b>.
	 */
	@Bite
	private ICNDBEndpoint forcedPrivateICNDBEndpoint;
	
	
	/**
	 * <p>Default constructor.
	 */
	public ICNDBService() {}
	
	/**
	 * <p>Facilitates constructor injection. 
	 */
	@Bite
	public ICNDBService(ICNDBEndpoint icndbEndpoint) {
		
		this.constructedICNDBEndpoint = icndbEndpoint;
	}
	
	/**
	 * <p>Accessor for privateICNDDBEndpoint.
	 *
	 * @return the privateICNDDBEndpoint
	 */
	public ICNDBEndpoint getPrivateICNDDBEndpoint() {
		return privateICNDDBEndpoint;
	}

	/**
	 * <p>Mutator for privateICNDDBEndpoint.
	 *
	 * @param privateICNDDBEndpoint 
	 *			the privateICNDDBEndpoint to set
	 */
	public void setPrivateICNDDBEndpoint(ICNDBEndpoint privateICNDDBEndpoint) {
		this.privateICNDDBEndpoint = privateICNDDBEndpoint;
	}

	/**
	 * <p>Accessor for publicICNDDBEndpoint.
	 *
	 * @return the publicICNDDBEndpoint
	 */
	public ICNDBEndpoint getPublicICNDDBEndpoint() {
		return publicICNDDBEndpoint;
	}

	/**
	 * <p>Accessor for protectedICNDDBEndpoint.
	 *
	 * @return the protectedICNDDBEndpoint
	 */
	public ICNDBEndpoint getProtectedICNDDBEndpoint() {
		return protectedICNDDBEndpoint;
	}

	/**
	 * <p>Accessor for defaultICNDDBEndpoint.
	 *
	 * @return the defaultICNDDBEndpoint
	 */
	public ICNDBEndpoint getDefaultICNDDBEndpoint() {
		return defaultICNDDBEndpoint;
	}

	/**
	 * <p>Accessor for constructedICNDBEndpoint.
	 *
	 * @return the constructedICNDBEndpoint
	 */
	public ICNDBEndpoint getConstructedICNDBEndpoint() {
		return constructedICNDBEndpoint;
	}

	/**
	 * <p>Accessor for forcedPrivateICNDBEndpoint.
	 *
	 * @return the forcedPrivateICNDBEndpoint
	 */
	public ICNDBEndpoint getForcedPrivateICNDBEndpoint() {
		return forcedPrivateICNDBEndpoint;
	}
}
