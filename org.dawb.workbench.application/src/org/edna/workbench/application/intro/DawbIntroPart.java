/*
 * Copyright (c) 2012 European Synchrotron Radiation Facility,
 *                    Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */ 
package org.edna.workbench.application.intro;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.intro.IIntroSite;
import org.eclipse.ui.part.IntroPart;

/**
 *   DawbIntroPart
 *
 *   @author gerring
 *   @date Jul 16, 2010
 *   @project org.edna.workbench.application
 **/
public class DawbIntroPart extends IntroPart {

	private FormToolkit toolkit;
	private ScrolledForm form;

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void createPartControl(Composite parent) {

        setTitle("Data Analysis Workbench");
		toolkit = new FormToolkit(parent.getDisplay());
		toolkit.setOrientation(SWT.LEFT_TO_RIGHT);
		toolkit.setBorderStyle(SWT.BORDER);
		form = toolkit.createScrolledForm(parent);
		form.setText("Welcome to Data Analysis Workbench");
		toolkit.paintBordersFor(form);
		form.getBody().setLayout(new GridLayout(1, true));
		
		Composite main = toolkit.createComposite(form.getBody(), SWT.NONE);
		GridData gd_main = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
		gd_main.widthHint = 700;
		main.setLayoutData(gd_main);
		
		main.setLayout(new TableWrapLayout());
		toolkit.paintBordersFor(main);

		final Section sctnWorkbench = toolkit.createSection(main, Section.TWISTIE | Section.TITLE_BAR);
		sctnWorkbench.setLayoutData( new TableWrapData(TableWrapData.LEFT, TableWrapData.FILL_GRAB, 1, 1));
		sctnWorkbench.setBounds(0, 10, 679, 23);
		toolkit.paintBordersFor(sctnWorkbench);
		sctnWorkbench.setText("Workbench");
		
		FormText formText = toolkit.createFormText(sctnWorkbench, false);
		formText.setWhitespaceNormalized(false);
		toolkit.paintBordersFor(formText);
		sctnWorkbench.setClient(formText);
		formText.setText("This workbench is based on Passerelle, Eclipse, GDA and the EDNA project. It is an RCP application so it's made up of simple views which can be repositioned and maximized or dragged to a separate window. For instance plots, file trees, data trees and workflow diagrams. The views are contained in perspectives which can be reset to return the look and feel of the application to normal.\nThe workbench has projects which can be used to edit workflows, results data and python scripts. There are initially three projects, although more can be created as required. The first project allows you to import data which you would like to visualise, the second contains an edna workflow and the third is for editing and running python.\n", false, false);
		
		Section sctnResultsVisualisation = toolkit.createSection(main, Section.TWISTIE | Section.TITLE_BAR);
		sctnResultsVisualisation.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.FILL_GRAB, 1, 1));
		toolkit.paintBordersFor(sctnResultsVisualisation);
		sctnResultsVisualisation.setText("Data Visualization");
		
		FormText dataDescription = toolkit.createFormText(sctnResultsVisualisation, false);
		dataDescription.setWhitespaceNormalized(false);
		toolkit.paintBordersFor(dataDescription);
		sctnResultsVisualisation.setClient(dataDescription);
		dataDescription.setText("To use data visualization only, go to the 'data' perspective. Here you will see a file tree where you can create folders and drag and drop data files. The files are not normally copied to the data project but are referenced as links. This avoids copying large files when opening new data but if data moves you will need to fix the link.\nThis perspective can read and plot a wide range of data and allows comparison between that data.\n", false, false);

		Section sctnPythonEditing = toolkit.createSection(main, Section.TWISTIE | Section.TITLE_BAR);
		sctnPythonEditing.setLayoutData(new TableWrapData(TableWrapData.LEFT, TableWrapData.FILL_GRAB, 1, 1));
		toolkit.paintBordersFor(sctnPythonEditing);
		sctnPythonEditing.setText("Python Editing");
		
		FormText pythonDescription = toolkit.createFormText(sctnPythonEditing, false);
		pythonDescription.setWhitespaceNormalized(false);
		toolkit.paintBordersFor(pythonDescription);
		sctnPythonEditing.setClient(pythonDescription);
		pythonDescription.setText("When Data Analysis Workbench starts it detects the python interpereter. You can use the Python perspective to write and run python code that can interact with your workflows and data. The editor in this perspective checks syntax, gives advanced refactoring options and provides a debugger.\n\nNOTE: The python interpreter is determined in this order:\n1. If in your 'PYTHON' environment variable is defined this is used, otherwise;\n2. If your system returns a path from the command 'which python' this is used, otherwise;\n3. A standard Jython interpreter will be used.\n", false, false);

		Hyperlink hprlnkCloseIntroductionAnd = toolkit.createHyperlink(form.getBody(), "Get Started", SWT.NONE);
		toolkit.paintBordersFor(hprlnkCloseIntroductionAnd);
		hprlnkCloseIntroductionAnd.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				PlatformUI.getWorkbench().getIntroManager().closeIntro( DawbIntroPart.this );
			}
		});
		
		sctnWorkbench.layout();
		sctnResultsVisualisation.layout();
		sctnPythonEditing.layout();
	}

	@Override
	public void setFocus() {
		form.setFocus();

	}

	@Override
	public void dispose() {
		toolkit.dispose();		
		super.dispose();
	}

	@Override
	public void standbyStateChanged(boolean standby) {
		// TODO Auto-generated method stub
		
	}
	
    protected void setSite(IIntroSite site) {
        super.setSite(site);
        setTitle("vvf");
    }
    
}
