/*
 * Copyright (c) 2012 European Synchrotron Radiation Facility,
 *                    Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */ 
package org.edna.workbench.application.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 *   EdnaPreferencePage
 *
 *   @author gerring
 *   @date Jul 26, 2010
 *   @project org.edna.workbench.application
 **/
public class EdnaPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	public EdnaPreferencePage() {
	}

	public EdnaPreferencePage(String title) {
		super(title);
	}

	public EdnaPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	protected Control createContents(Composite parent) {
		return parent;
	}

	@Override
	public void init(IWorkbench workbench) {
        
	}

}
