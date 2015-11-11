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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.dawb.common.util.eclipse.BundleUtils;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.dawb.workbench.application";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		
		File appHome = BundleUtils.getBundleLocation(context.getBundle());

		createLoggingProperties(appHome);
		createJavaProperties(appHome);
		createJythonProperties(appHome);
		
		super.start(context);
		plugin = this;
	}

	private static void createJythonProperties(File appHome) throws IOException {
		if (System.getProperty("python.cachedir")!=null) return;
		
		final File cacheDir  = new File(appHome.getParentFile(), "python-cache");
		if (!cacheDir.exists()) cacheDir.mkdirs();
		System.setProperty("python.cachedir", cacheDir.getAbsolutePath());
	}

	/**
	 * Simply sets the configuration property. For some reason even though we have logback.xml
	 * in the class path, we need to set the property.
	 * @param appHome
	 * @throws IOException 
	 */
	private static void createLoggingProperties(File appHome) throws IOException {
		
		if (System.getProperty("logback.configurationFile")!=null) return;
		
		final File propsFile = new File(new File(appHome, "config"), "logback.xml");
		System.setProperty("logback.configurationFile", propsFile.getAbsolutePath());
		
		// For some reason slf4j still gives log4j errors, so we also configure log4j here:
		//PropertyConfigurator.configure(context.getBundle().getResource("log4j.properties"));

	}

	/**
	 * Adds to the system properties from a file called java.properties.
	 * @param appHome
	 * @throws IOException
	 */
	private static void createJavaProperties(File appHome) throws IOException {
		
		// See if we have a java.properties and load it if do
		final File propsFile = new File(new File(appHome, "config"), "java.properties");
		if (propsFile.exists()) {
			// TODO Find PropUtils that was a trusty old class for doing this.
			final Properties existingProps = System.getProperties();
			final Properties fromFile      = new Properties();
			final InputStream in           = new FileInputStream(propsFile);
			try {
				fromFile.load(in);
				existingProps.putAll(fromFile);
				System.setProperties(existingProps);
			} catch (Exception ne) {
				in.close();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
