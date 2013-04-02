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

package org.eventfully.wmbtesting
import static org.custommonkey.xmlunit.XMLAssert.*

import org.apache.camel.CamelContext
import org.apache.camel.ProducerTemplate
import org.custommonkey.xmlunit.*
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration

import spock.lang.*

@ContextConfiguration(classes = EmptyRouteCamelConfiguration.class)
class WmqRequestReplySpecificationTest extends Specification {

	@Autowired
	CamelContext camelContext

	ProducerTemplate producer

	@Before
	public void setup() {
		XMLUnit.setIgnoreWhitespace(true)
		producer = camelContext.createProducerTemplate()
	}

	@Ignore
	def "Using Camel producer template with JMS for request/reply"() {

		given: "A XML payload to send"
		def testPayload = new File('src/test/resources/data/request1.xml')
		and: "A producer template from an autowired Camel Contect"
		ProducerTemplate producer = camelContext.createProducerTemplate()
		and: "Request and reply queue names"
		String requestQ = "GET_REQREP_IN"
		String replyQ = "GET_REQREP_OUT"

		when: "The request is sent"
		def reply = producer.requestBody("wmq:$requestQ?replyTo=$replyQ&replyToType=Exclusive&useMessageIDAsCorrelationID=true",
				testPayload, String.class)

		then: "A reply is received"
		assertNotNull(reply)
		and: "The payload contains the expected value"
		assertXpathEvaluatesTo("Braithwaite", "/SaleEnvelope/SaleList/Invoice/Surname", reply)
	}

	@Ignore
	@Unroll
	def "Using Camel producer template with JMS for request/reply :: test input file #testFileName should return expected surname #expectedSurname"() {

		given: "A XML payload to send"
		def testPayload = new File("src/test/resources/data/$testFileName")
		and: "Request and reply queue names"
		String requestQ = "GET_REQREP_IN"
		String replyQ = "GET_REQREP_OUT"

		when: "The request is sent"
		def reply = producer.requestBody("wmq:$requestQ?replyTo=$replyQ&replyToType=Shared&useMessageIDAsCorrelationID=true&jmsMessageType=Text",
				testPayload, String.class)

		then: "A reply is received"
		assertNotNull(reply)
		and: "The payload contains the expected value"
		assertXpathEvaluatesTo(expectedSurname, "/SaleEnvelope/SaleList/Invoice/Surname", reply)

		where:
		testFileName | expectedSurname
		'request1.xml' | 'Braithwaite'
		'request2.xml' | 'Palmer'
	}

	@Unroll
	def "Using Camel producer template with JMS for request/reply :: test input file #testFileName should return similar XML"() {

		given: "A XML payload to send"
		def testPayload = new File("src/test/resources/data/$testFileName")

		and: "An expected reply XML payload"
		def expectedPayload = new File("src/test/resources/data/$expectedFileName")

		and: "Ignoring differences for configured element names"
		DifferenceListener diffListener = new IgnoreNamedElementsDifferenceListener(ignoreNamedElementsNames.toListString())

		when: "The request is sent"
		def reply = producer.requestBody("wmq:$requestQ?replyTo=$replyQ&replyToType=Shared&useMessageIDAsCorrelationID=true&jmsMessageType=Text",
				testPayload, String.class)

		then: "A reply is received"
		assertNotNull(reply)

		and: "The reply payload contains similar XML"
		Diff myDiff = new Diff(expectedPayload.getText(), reply)
		myDiff.overrideDifferenceListener(diffListener)
		assertXMLEqual("Same elements/attributes sequences, but some values may be ignored, normally time and dates.", myDiff, myDiff.similar())

		where:
		testFileName   | expectedFileName | requestQ 		| replyQ			| ignoreNamedElementsNames
		'request1.xml' | 'reply1.xml'     | 'GET_REQREP_IN'	| 'GET_REQREP_OUT'	|  ['CompletionTime', 'SomeOtherElement']
		'request2.xml' | 'reply2.xml'     | 'GET_REQREP_IN'	| 'GET_REQREP_OUT'	|  ['CompletionTime', 'SomeOtherElement']
		
	}
}
