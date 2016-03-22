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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.internal.ide.model.WorkbenchAdapterBuilder;
import org.edna.pydev.extensions.utils.InterpreterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationWorkbenchWindowAdvisor.class);
	
	
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
        configurer.setTitle("Data Analysis Workbench   -  "+System.getProperty("dawb.workbench.name")+"  "+System.getProperty("dawb.workbench.version")+"-"+System.getProperty("dawb.workbench.build"));
   }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
     
    @Override
	public IStatus saveState(IMemento memento) {
		return super.saveState(memento);
	}
	
	/**
     * Configures the initial settings of the application window.
     */
    @Override
    public void preWindowOpen() {
    	        
    	IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(1024, 768));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);
        configurer.setShowPerspectiveBar(true);
     
        // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=36961
        // We want to use ResourceNavigator, so we have to introduce this
        // dependency to org.eclipse.ui.ide (otherwise we don't see our
        // Resources)
        WorkbenchAdapterBuilder.registerAdapters();
    }
    
    
    @Override
    public void postWindowCreate() {

    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void postWindowOpen() {
    	
		try {
//	    	InterpreterUtils.createJythonInterpreter("jython", new NullProgressMonitor());
	    	InterpreterUtils.createPythonInterpreter("python", new NullProgressMonitor());
		} catch (Exception e) {
			logger.error("Cannot create default projects or python interpreter.", e);
		}

		PlatformUI.getWorkbench().addWorkbenchListener(new IWorkbenchListener() {

			@Override
			public void postShutdown(final IWorkbench workbench) {
			}
			@Override
			public boolean preShutdown(final IWorkbench workbench, 
					final boolean forced) {
				// Remove consoles manually in time. Otherwise they are removed,
				// when the display is already disposed and this causes 
				// exceptions
				// this is a workaround for bug 
				// https://bugs.eclipse.org/bugs/show_bug.cgi?id=257970
				// reported here:
				// http://dev.eclipse.org/newslists/news.eclipse.platform.rcp/msg35729.html

				ConsolePlugin.getDefault().getConsoleManager().removeConsoles(
						ConsolePlugin.getDefault().getConsoleManager()
						.getConsoles());
				return true;
			}
		});

    }

}
