/*-
 * Copyright (c) 2012 European Synchrotron Radiation Facility,
 *                    Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */ 
package org.dawnsci.intro.content.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.dawnsci.intro.Activator;
import org.dawnsci.intro.util.FileUtil;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 *
 */
public class GetRemoteRssJob extends Job {

	private String url;
	private File outputFile;

	public GetRemoteRssJob(String url,File outputFile) {
		super(GetRemoteRssJob.class.getName());
		this.url = url ;
		this.outputFile = outputFile ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		InputStream stream =null ;
		FileOutputStream out = null ;
		try{
			stream = new URL(url).openStream();
			out = new FileOutputStream(outputFile);
			FileUtil.copy(stream, out);
			out.close();
			stream.close();
		}catch (Exception e) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e) ;
		}finally{
			if(stream != null){
				try {
					stream.close();
				} catch (IOException e) {
					//TODO logger
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					//TODO logger
				}
			}
		}
		return Status.OK_STATUS;
	}

}
