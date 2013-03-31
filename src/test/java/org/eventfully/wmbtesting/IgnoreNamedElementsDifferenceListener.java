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

import java.util.HashSet;
import java.util.Set;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

public final class IgnoreNamedElementsDifferenceListener implements
		DifferenceListener {
	private static final Logger logger = LoggerFactory
			.getLogger(IgnoreNamedElementsDifferenceListener.class);
	private Set<String> blackList = new HashSet<String>();

	public IgnoreNamedElementsDifferenceListener(String... elementNames) {
		for (String name : elementNames) {
			blackList.add(name);
		}
	}

	public int differenceFound(Difference difference) {
		if (difference.getId() == DifferenceConstants.TEXT_VALUE_ID) {
			if (blackList.contains(difference.getControlNodeDetail().getNode()
					.getParentNode().getNodeName())) {
				return DifferenceListener.RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
			}
		}

		return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
	}

	public void skippedComparison(Node node, Node node1) {
		logger.debug("Skipping comparison between: " + node.getNodeName()
				+ " and node: " + node1);
	}
}