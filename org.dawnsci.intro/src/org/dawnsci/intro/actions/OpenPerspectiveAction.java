/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.dawnsci.intro.actions;

import java.util.Properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroPart;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;

/**
 * Class to open a perspective through an org.eclipse.ui.intro/runAction
 * 
 * @author Baha El Kassaby
 * 
 */
public class OpenPerspectiveAction implements IIntroAction {

	private static final String INTROREGISTER_EXTENSION_ID = "org.dawnsci.introRegister"; //$NON-NLS-1$	
	private static final String ATT_CLASS = "class"; //$NON-NLS-1$

	@Override
	public void run(IIntroSite site, Properties params) {
		final IActionDelegate action = createActionDelegate(params.getProperty("name"));
		
		//close the intro part
		IIntroPart part = PlatformUI.getWorkbench().getIntroManager().getIntro();
		PlatformUI.getWorkbench().getIntroManager().closeIntro(part);
		//run the appropriate launcher
		Display.getDefault().asyncExec(new Runnable(){
			@Override
			public void run() {
				if (action != null)
					action.run(null);
			}
			
		});
	}

	/**
	 * Creates an action that will launch a perspective
	 * 
	 * @param name of the class used to launch a given perspective
	 * @return an action delegate
	 */
	private IActionDelegate createActionDelegate(String name) {
		IConfigurationElement introAction = null;

		final IExtension[] extensions = getExtensions(INTROREGISTER_EXTENSION_ID);
		for (int i = 0; i < extensions.length; i++) {

			IExtension extension = extensions[i];
			IConfigurationElement[] configElements = extension.getConfigurationElements();

			for (int j = 0; j < configElements.length; j++) {
				String configElement = configElements[j].getAttribute(ATT_CLASS);
				if(configElement.equals(name)){
					introAction = configElements[j];
					break;
				}
			}
		}

		IActionDelegate delegate = null;
		try {
			delegate = (IActionDelegate) introAction.createExecutableExtension(ATT_CLASS);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}

		return delegate;
	}

	/**
	 * Discover extensions for the given extensionPointId
	 * 
	 * @param extensionPointId
	 *            the extension point id
	 * @return an array of discovered extensions
	 */
	private IExtension[] getExtensions(String extensionPointId) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(extensionPointId);
		IExtension[] extensions = point.getExtensions();
		return extensions;
	}
}
