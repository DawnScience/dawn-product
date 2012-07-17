package org.dawnsci.product.plugin;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple class which deletes the JRE preference at the end of a session, so that when users switch
 * os's or go 64bit to 32bit with the same workspace, the jre is not cached.
 * @author ssg37927
 *
 */
public class JRECleanup implements IStartup, IWorkbenchListener{

	transient private static final Logger logger = LoggerFactory.getLogger(JRECleanup.class);
	
	// IStartup Overrides
	@Override
	public void earlyStartup() {
		PlatformUI.getWorkbench().addWorkbenchListener(this);
		logger.debug("Registered JRECleanup with workbench");
	}

	// IWorkbenchListener Overrides
	@Override
	public boolean preShutdown(IWorkbench workbench, boolean forced) {
		// Nothing to do in the pre shutdown.
		return true;
	}

	@Override
	public void postShutdown(IWorkbench workbench) {
		try {
			IPath rootPath = ResourcesPlugin.getWorkspace().getRoot().getLocation();
			IPath fullpath = rootPath.append(".metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.jdt.launching.prefs");
			File pref = new File(fullpath.toOSString());
			if (pref.exists()) {
				pref.delete();
			}
			logger.debug("JRECleanup completed");
		} catch (Exception e) {
			logger.warn("Failed to delete the JRE preference for clean multiplatform startup",e);
		}	
	}
}
