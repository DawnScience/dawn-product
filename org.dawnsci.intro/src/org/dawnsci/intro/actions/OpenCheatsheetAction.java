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

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.cheatsheets.OpenCheatSheetAction;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.intro.config.IIntroAction;

/**
 * Class to open a cheatsheet through an org.eclipse.ui.intro/runAction
 * 
 * @author Baha El Kassaby
 * 
 */
public class OpenCheatsheetAction implements IIntroAction {

	private static final String ATT_ID = "id"; //$NON-NLS-1$

	@Override
	public void run(IIntroSite site, Properties params) {
		String id = params.getProperty(ATT_ID);
		final Action action = new OpenCheatSheetAction(id);
		
		//close the intro part
//		IIntroPart part = PlatformUI.getWorkbench().getIntroManager().getIntro();
//		PlatformUI.getWorkbench().getIntroManager().closeIntro(part);
		//run the appropriate launcher
		Display.getDefault().asyncExec(new Runnable(){
			@Override
			public void run() {
				if (action != null)
					action.run();
			}
			
		});
	}
}
