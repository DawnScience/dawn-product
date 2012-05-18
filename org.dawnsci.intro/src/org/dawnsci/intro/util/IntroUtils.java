/*-
 * Copyright (c) 2012 European Synchrotron Radiation Facility,
 *                    Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */ 

package org.dawnsci.intro.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

public class IntroUtils {

	public static boolean isProjectExisting(final String natureId) {
		
		// Show the create a workflows project creator is there are no projects already
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		boolean        isProject = false;
		final IProject[] projects = root.getProjects();	
		for (IProject iProject : projects) {
			try {
				if (iProject.getNature(natureId)!=null) {
					isProject = true;
					break;
				}
			} catch (CoreException e) {
				continue;
			}
		}

		return isProject;
	}
	
	/**
	 * Open a wizard for id
	 * @param id
	 */
	public static void openWizard(String id) {
		
		// First see if this is a "new wizard".
		IWizardDescriptor descriptor = PlatformUI.getWorkbench()
				.getNewWizardRegistry().findWizard(id);
		// If not check if it is an "import wizard".
		if  (descriptor == null) {
			descriptor = PlatformUI.getWorkbench().getImportWizardRegistry()
					.findWizard(id);
		}
		// Or maybe an export wizard
		if  (descriptor == null) {
			descriptor = PlatformUI.getWorkbench().getExportWizardRegistry()
					.findWizard(id);
		}
		try  {
			// Then if we have a wizard, open it.
			if  (descriptor != null) {
				IWizard wizard = descriptor.createWizard();
				WizardDialog wd = new  WizardDialog(Display.getCurrent().getActiveShell(), wizard);
				wd.setTitle(wizard.getWindowTitle());
				wd.open();
			}
		} catch  (CoreException e) {
			e.printStackTrace();
		}
	}

}
