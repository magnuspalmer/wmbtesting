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

import javax.jms.JMSException;

import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

@Configuration
@PropertySource("classpath:/wmq.properties")
public class WmqConfig {

	@Autowired
	private Environment env;

	@Bean(name = "wmq")
	public JmsComponent wmq() throws JMSException {
		return JmsComponent.jmsComponent(createWMQ1());
	}

	/**
	 * To override properties, add beanID.property in the properties file
	 * 
	 * @return a configured MQConnectionFactory
	 * @throws JMSException
	 */
	@Bean(name = "qmgr")
	public com.ibm.mq.jms.MQConnectionFactory createWMQ1() throws JMSException {
		MQConnectionFactory mqConnectionFactory = new MQConnectionFactory();
		mqConnectionFactory.setHostName(env.getProperty("qmgr.hostName",
				"localhost"));
		mqConnectionFactory.setPort(Integer.parseInt(env.getProperty(
				"qmgr.port", "1414")));
		mqConnectionFactory.setQueueManager(env.getProperty(
				"qmgr.queueManager", ""));
		mqConnectionFactory.setChannel(env.getProperty("qmgr.channel", ""));
		mqConnectionFactory.setTransportType(Integer.parseInt(env.getProperty(
				"qmgr.transportType",
				String.valueOf(WMQConstants.WMQ_CM_BINDINGS_THEN_CLIENT))));

		return mqConnectionFactory;
	}

}
