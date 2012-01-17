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

import org.dawb.common.util.eclipse.BundleUtils;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.ide.IDEActionFactory;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
	
	// File Actions
    private IContributionItem newWizardShortList;
    private IWorkbenchAction importAction;
    private IWorkbenchAction closeAction;
    private IWorkbenchAction createSubmodelAction;
    private IWorkbenchAction closeAllAction;
    private IWorkbenchAction saveAction;
    private IWorkbenchAction saveAsAction;
    private IWorkbenchAction saveAllAction;
    private IWorkbenchAction changeWorkspace;
    private IWorkbenchAction exitAction;
    // Edit Actions
    private IWorkbenchAction undoAction;
    private IWorkbenchAction redoAction;
    
    private IWorkbenchAction cutAction;
    private IWorkbenchAction copyAction;
    private IWorkbenchAction closeEditorAction;
    private IWorkbenchAction pasteAction;
    private IWorkbenchAction deleteAction;
	private IWorkbenchAction perspectiveCustomizeAction;
	private IWorkbenchAction perspectiveSaveAsAction;
	private IWorkbenchAction perspectiveResetAction;
	private IWorkbenchAction perspectiveCloseAction;
	private IWorkbenchAction perspectiveCloseAllAction;
     
    // Run Actions
    private IAction runConfigurationsAction;
    
    // Window Actions
    private IContributionItem perspectiveList;
    private IContributionItem viewList;
    private IAction preferencesAction;
    
    // Help Actions
	private IWorkbenchAction searchHelpAction; // NEW
	private IWorkbenchAction dynamicHelpAction; // NEW    
    private IWorkbenchAction aboutAction,contentsAction;
	private IWorkbenchWindow window;
	private IWorkbenchAction newWindowAction;
    

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
        this.window = configurer.getWindowConfigurer().getWindow();
    }
    
    /**
     * Creates the actions and registers them.
     * Registering is needed to ensure that key bindings work.
     * The corresponding commands keybindings are defined in the plugin.xml file.
     * Registering also provides automatic disposal of the actions when
     * the window is closed.
     * 
     */
    protected void makeActions(final IWorkbenchWindow window) {
    	// File
    	importAction = ActionFactory.IMPORT.create(window);
        register(importAction);
        
    	closeAction = ActionFactory.CLOSE.create(window);
        register(closeAction);
        closeAllAction = ActionFactory.CLOSE_ALL.create(window);
        register(closeAllAction); 
        
        /**
         * In order to use the open workspace we set the vm variable
         */
        if (System.getProperty("eclipse.vm")==null) {
        	System.setProperty("eclipse.vm", BundleUtils.getEclipseHome()+"/jre/bin/java/bin");
        }
        changeWorkspace = IDEActionFactory.OPEN_WORKSPACE.create(window);
        register(changeWorkspace);
        
        saveAction = ActionFactory.SAVE.create(window);
        register(saveAction);
        saveAsAction = ActionFactory.SAVE_AS.create(window);
        register(saveAsAction);
        saveAllAction = ActionFactory.SAVE_ALL.create(window);
        register(saveAllAction);
        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        
        createSubmodelAction = ActionFactory.EXPORT.create(window);
        register(createSubmodelAction);

        // Edit
    	undoAction = ActionFactory.UNDO.create(window);
        register(undoAction);
    	redoAction = ActionFactory.REDO.create(window);
        register(redoAction);
    	cutAction = ActionFactory.CUT.create(window);
        register(cutAction);
    	copyAction = ActionFactory.COPY.create(window);
        register(copyAction);
    	pasteAction = ActionFactory.PASTE.create(window);
        register(pasteAction);
    	deleteAction = ActionFactory.DELETE.create(window);
        register(deleteAction);

        closeEditorAction = ActionFactory.CLOSE.create(window);
        register(closeEditorAction);
        
        perspectiveCustomizeAction = ActionFactory.EDIT_ACTION_SETS
        .create(window);
        register(perspectiveCustomizeAction);

        perspectiveSaveAsAction = ActionFactory.SAVE_PERSPECTIVE.create(window);
        register(perspectiveSaveAsAction);

        perspectiveResetAction = ActionFactory.RESET_PERSPECTIVE.create(window);
        register(perspectiveResetAction);

        perspectiveCloseAction = ActionFactory.CLOSE_PERSPECTIVE.create(window);
        register(perspectiveCloseAction);

        perspectiveCloseAllAction = ActionFactory.CLOSE_ALL_PERSPECTIVES
        .create(window);
        register(perspectiveCloseAllAction);

        // Window
        newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
        perspectiveList = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
        // Modify these...
        
        viewList = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
        newWizardShortList = ContributionItemFactory.NEW_WIZARD_SHORTLIST.create(window);
        preferencesAction = ActionFactory.PREFERENCES.create(window);
        register(preferencesAction);
        
        // Help
        contentsAction = ActionFactory.HELP_CONTENTS.create(window);
        contentsAction.setText("Help Contents...");
        register(contentsAction);

		searchHelpAction = ActionFactory.HELP_SEARCH.create(window); // NEW
		register(searchHelpAction); // NEW

		dynamicHelpAction = ActionFactory.DYNAMIC_HELP.create(window); // NEW
		register(dynamicHelpAction); // NEW
        

        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
 
	}
    
    protected void fillMenuBar(final IMenuManager menuBar) {
    	
    	// TODO replace constants with Intl Strings Messages.getString("ApplicationActionBarAdvisor.window")
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager fileNewWizardMenu = new MenuManager("New");
		MenuManager editMenu = new MenuManager("Edit" , IWorkbenchActionConstants.M_EDIT);
		MenuManager toolsMenu = new MenuManager("&Tools" , "tools");
		MenuManager windowMenu = new MenuManager("&Window" , IWorkbenchActionConstants.M_WINDOW); //$NON-NLS-1$        
        MenuManager windowPerspectiveMenu = new MenuManager("Open Perspective");
        MenuManager windowShowViewMenu = new MenuManager("Show View");
        MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(toolsMenu);
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
 		menuBar.add(windowMenu);        
        menuBar.add(helpMenu);
        
        // File
        fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
        fileNewWizardMenu.add(newWizardShortList);
        fileMenu.add(fileNewWizardMenu);
		fileMenu.add(new Separator());
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.OPEN_EXT));
		fileMenu.add(new Separator());
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		fileMenu.add(importAction);
		fileMenu.add(new Separator());
		fileMenu.add(closeAction);
		fileMenu.add(closeAllAction);
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
 		fileMenu.add(new Separator());
		fileMenu.add(saveAction);
		fileMenu.add(saveAsAction);
		fileMenu.add(saveAllAction);
		fileMenu.add(new Separator());
        fileMenu.add(changeWorkspace);
		fileMenu.add(new Separator());
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.IMPORT_EXT));
		fileMenu.add(new Separator());
		fileMenu.add(ContributionItemFactory.REOPEN_EDITORS.create(getWindow()));
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.MRU));
		fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));

		// Edit
        editMenu.add(undoAction);
        editMenu.add(redoAction);
        editMenu.add(new Separator());
        editMenu.add(cutAction);
        editMenu.add(copyAction);
        editMenu.add(pasteAction);
        editMenu.add(new Separator());
        editMenu.add(deleteAction);
        
		// Tools
        toolsMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
       
		// Window
        windowPerspectiveMenu.add(perspectiveList);
        windowMenu.add(newWindowAction);
        windowMenu.add(windowPerspectiveMenu);
        windowShowViewMenu.add(viewList);
        windowMenu.add(windowShowViewMenu);
		windowMenu.add(new Separator());
	    MenuManager persMenu = new MenuManager("Perspective");
		addPerspectiveActions(persMenu);
		windowMenu.add(persMenu);
		windowMenu.add(new Separator());
		windowMenu.add(preferencesAction);
		windowMenu.add(ContributionItemFactory.OPEN_WINDOWS.create(getWindow()));
        
        // Help
        helpMenu.add(new GroupMarker(IWorkbenchActionConstants.GROUP_HELP));
        helpMenu.add(new Separator());
        helpMenu.add(contentsAction);
		helpMenu.add(searchHelpAction); // NEW
		helpMenu.add(dynamicHelpAction); // NEW
        helpMenu.add(new Separator());
        helpMenu.add(aboutAction);
       

    }
    
    protected void fillCoolBar(final ICoolBarManager coolBar) {
        // File
    	IToolBarManager fileToolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
    	fileToolbar.add(saveAction);
    	fileToolbar.add(saveAllAction);
        coolBar.add(new ToolBarContributionItem(fileToolbar, "file"));
    	
        // Edit
    	IToolBarManager editToolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
    	editToolbar.add(undoAction);
    	editToolbar.add(redoAction);
    	editToolbar.add(new Separator());
    	editToolbar.add(cutAction);
    	editToolbar.add(copyAction);
    	editToolbar.add(pasteAction);
    	editToolbar.add(new Separator());
    	editToolbar.add(deleteAction);
        coolBar.add(new ToolBarContributionItem(editToolbar, "edit"));
    	
        // Additions
        coolBar.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        coolBar.add(new Separator("workbenchAdditions"));

    }
    
    
    /**
     * Adds the perspective actions to the specified menu.
     */
    private void addPerspectiveActions(MenuManager menu) {
		
		menu.add(perspectiveCustomizeAction);
		menu.add(perspectiveSaveAsAction);
		menu.add(perspectiveResetAction);
		menu.add(perspectiveCloseAction);
		menu.add(perspectiveCloseAllAction);

    }

	private IWorkbenchWindow getWindow() {
		return window;
	}
	

}
