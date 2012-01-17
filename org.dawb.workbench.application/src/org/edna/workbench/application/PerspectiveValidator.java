/*
 * Copyright (c) 2012 European Synchrotron Radiation Facility,
 *                    Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */ 
package org.edna.workbench.application;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IPerspectiveDescriptor;

/**
 *   PerspectiveValidator
 *
 *   @author gerring
 *   @date Jul 1, 2010
 *   @project org.edna.workbench.application
 **/
public class PerspectiveValidator {

	/**
	 * 
	 * @param des
	 * @return
	 */
	public static boolean isPerspectivePublic(final IPerspectiveDescriptor des) {

		final String id = des.getId();
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor("org.edna.workbench.application.perspectives");
        for (int i = 0; i < config.length; i++) {
        	if (id.indexOf(config[i].getAttribute("id_fragment"))>-1) return true;
		}
		
		return false;
	}
}
