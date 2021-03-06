/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.dawnsci.internal.intro.universal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class WelcomeCustomizationPreferencePage extends PreferencePage implements IWorkbenchPreferencePage,
		IExecutableExtension {

    private CustomizationContentsArea contentsArea;

	public WelcomeCustomizationPreferencePage() {
	}

	public WelcomeCustomizationPreferencePage(String title) {
		super(title);
	}

	public WelcomeCustomizationPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}
	
	private CustomizationContentsArea getContentsArea() {
		if (contentsArea == null) {
			contentsArea = new CustomizationContentsArea();
		}
		return contentsArea;
	}

	protected Control createContents(Composite parent) {
		getContentsArea().setShell(getShell());
		return getContentsArea().createContents(parent);
	}

	public void dispose() {
		getContentsArea().dispose();
		super.dispose();
	}

	public boolean performOk() {
		return getContentsArea().performOk();
	}

	protected void performDefaults() {
		getContentsArea().performDefaults();
	}


	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
	}

	public void init(IWorkbench workbench) {
	}
	
	public void setCurrentPage(String pageId) {
		getContentsArea().setCurrentPage(pageId);
	}
}
