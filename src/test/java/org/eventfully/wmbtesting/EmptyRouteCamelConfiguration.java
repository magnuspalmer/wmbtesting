/**
 *
 * Copyright (C)2013 - Magnus Palmér
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eventfully.wmbtesting;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Class that act as ApplicationContext that has an empty
 * SingleRouteCamelConfiguration so that the CamelContext gets created
 * appropriately.
 * 
 * @author Magnus Palmér
 * 
 */
@Configuration
@ComponentScan(basePackages = "org.eventfully.wmbtesting")
@Import(WmqConfig.class)
public class EmptyRouteCamelConfiguration extends SingleRouteCamelConfiguration {

	/**
	 * An empty route, returning null causes NPE in the abstract CamelConfiguration parent.
	 */
	@Override
	public RouteBuilder route() {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {

			}
		};
	}

}
