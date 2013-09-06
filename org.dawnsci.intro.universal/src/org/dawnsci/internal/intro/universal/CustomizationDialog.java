/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.dawnsci.internal.intro.universal;

import org.dawnsci.internal.intro.universal.util.ImageUtil;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class CustomizationDialog extends TrayDialog {

    private CustomizationContentsArea contentsArea;
	private String pageId;
	
	private CustomizationContentsArea getContentsArea() {
		if (contentsArea == null) {
			contentsArea = new CustomizationContentsArea();
		}
		return contentsArea;
	}
	protected CustomizationDialog(Shell shell, String pageId) {
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.pageId = pageId;
	}
	
	protected Control createDialogArea(Composite parent) {
		CustomizationContentsArea contents = getContentsArea();
		contents.setShell(getShell());
		contents.setCurrentPage(pageId);
		Composite outerContainer = (Composite) super.createDialogArea(parent);
		Control inner = contents.createContents(outerContainer);
		inner.setLayoutData(new GridData(GridData.FILL_BOTH));
		return outerContainer;
	}
	
	protected void okPressed() {
		if (getContentsArea().performOk()) {
		    getContentsArea().dispose();
		}
		super.okPressed();
	}
	
	protected void cancelPressed() {
	    getContentsArea().dispose();
	    super.cancelPressed();
	}

    protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);	
		newShell.setText(Messages.WelcomeCustomizationPreferencePage_Customize); 
	    newShell.setImage(ImageUtil.createImage("full/elcl16/configure.gif")); //$NON-NLS-1$);
    }
	
}