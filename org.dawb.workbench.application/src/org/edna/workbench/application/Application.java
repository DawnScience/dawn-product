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

import java.io.File;
import java.net.InetAddress;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class controls all aspects of the application's execution
 * 
 * 
 * 
 
Version		Nick Name		Release
-------		---------		-------

0.8			Amoeba			June      2011
0.9			Tigermoth    	Autumn    2011
1.0 		Salamander   	-
2.0			Gila Monster    -
3.0			Raptor			-	
4.0			Cetacean		-
5.0			Hominid			-

 */
public class Application implements IApplication {

	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception {
		
		Display display = PlatformUI.createDisplay();
		
		try {	
			createDawbWorkspace(context);
			
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else
				return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
		
	}
	/**
	 * 
	 * @param workspacePath
	 * @throws Exception if the workspace failed to be set
	 */
	private void createDawbWorkspace(IApplicationContext context) throws Exception {

		// TODO - Fis fact that on restart -data is used and this should stop use from 
		// using our default workspace. Also this should be used from now on.
		if (context.getArguments().get("data")!=null) {
			logger.debug("Using workspace '"+context.getArguments().get("data")+"'");
			return;
		}
		
		Location instanceLocation = Platform.getInstanceLocation();
	    if (instanceLocation.isSet()) return;
		
	    // At ESRF no disk space - use /buffer/${machine_name}
	    String userHome = System.getProperty("org.dawb.workspace.dir");
	    if (userHome==null) userHome = getESRFBufferLocation();
	    if (userHome==null) userHome = System.getProperty("user.home");
	    logger.debug("Using workspace parent folder as '"+userHome+"'");
	    
		final File workspace = new File(userHome+"/dawb_workspace");
		if (!workspace.exists()) {
			if (!workspace.mkdirs()) {
				final String msg = "Cannot create workspace in " + workspace.getAbsolutePath() + " not setting workspace";
				throw new Exception(msg);
			}
		}
		workspace.setWritable(true);
		workspace.setReadable(true);
		

		if (!workspace.canRead() || !workspace.canWrite() || !workspace.canExecute()) {
			final String msg = "Not setting workspace to " + workspace.getAbsolutePath()
					+ " due to insufficient permissions.";
			throw new Exception(msg);
		}

		URL url  = workspace.toURI().toURL();
		instanceLocation = Platform.getInstanceLocation();
		instanceLocation.set(url, false);
	}

	/**
	 * Tries to get the /buffer part of the disk at the ESRF
	 * @return
	 */
	private String getESRFBufferLocation() {
		
		try { 
		    InetAddress addr    = InetAddress.getLocalHost();
		    final File  buffer  = new File("/buffer");
		    if (!buffer.exists() || !buffer.isDirectory()) return null;
		    
		    File  dir  = new File(buffer, addr.getHostName());
		    if (dir.exists() && dir.isDirectory()) {
		    	dir  = new File(dir, System.getProperty("user.name"));
		    	if (dir.exists() && dir.isDirectory()) return dir.getAbsolutePath();
		    }
		    
		    dir  = new File(buffer, addr.getHostName()+"1");
		    if (dir.exists() && dir.isDirectory()) {
		    	dir  = new File(dir, System.getProperty("user.name"));
		    	if (dir.exists() && dir.isDirectory()) return dir.getAbsolutePath();
		    }
		    
		    dir  = new File(buffer, System.getProperty("user.name"));
		    if (dir.exists() && dir.isDirectory()) return dir.getAbsolutePath();

		    final boolean worked = dir.mkdirs();
		    if (worked) return dir.getAbsolutePath();
		    
		    return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
