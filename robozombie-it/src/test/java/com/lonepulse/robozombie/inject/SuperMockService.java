package com.lonepulse.robozombie.inject;

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


import com.lonepulse.robozombie.annotation.Bite;

/**
 * <p>Emulates a super-type which requires an endpoint injection.</p>
 * 
 * @category test
 * <br><br> 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class SuperMockService {
	
	
	@Bite
	private MockEndpoint superEndpoint;
	{
		Zombie.infect(this); //infects all children which may extend this type 
	}
	
	
	public MockEndpoint getSuperEndpoint() {
		
		return superEndpoint;
	}
}
