package com.lonepulse.robozombie.executor;

/*
 * #%L
 * RoboZombie
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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

import com.lonepulse.robozombie.proxy.Zombie;
import com.lonepulse.robozombie.proxy.Zombie.Configuration;

/**
 * <p>This contract defines the services for managing <b>request execution configurations</b>. These may 
 * govern aspects such as the schemes which are handled, or the number of pooled connections. Configurations 
 * may be applied when executing requests on discrete endpoints or on all endpoints in general.</p>
 * 
 * <p>See {@link Zombie.Configuration}</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public interface ConfigurationManager {
	
	/**
	 * <p>Creates a new instance of the <i>out-of-the-box</i> configuration which will be used by default 
	 * for executing all endpoint requests.</p>
	 *
	 * @return the default {@link Zombie.Configuration} used for all endpoints
	 * <br><br> 
	 * @since 1.3.0
	 */
	Configuration getDefault();
	
	/**
	 * <p>Takes the {@link Class} of an endpoint interface and registers the associated configurations. 
	 * This may be a custom configuration which is specified using the <b>@{@link Config}</b> annotation 
	 * or the default configuration if the annotation is not found. In either case, the configuration 
	 * will be instantiated, registered and returned.</p>
	 *
	 * @param endpointClass
	 * 			the {@link Class} of the endpoint whose {@link Zombie.Configuration} is registered
	 * <br><br>
	 * @return a <b>new instance</b> of the configuration which was instantiated and registered
	 * <br><br>
	 * @throws ConfigurationFailedException
	 * 			if the associated configuration failed to be instantiated or registered
	 * <br><br>
	 * @since 1.2.3
	 */
	Configuration register(Class<?> endpointClass);
}
