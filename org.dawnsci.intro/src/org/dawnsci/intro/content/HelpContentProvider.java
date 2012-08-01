/*-
 * Copyright (c) 2012 European Synchrotron Radiation Facility,
 *                    Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.dawnsci.intro.content;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.spi.RegistryContributor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.intro.config.IIntroContentProviderSite;
import org.eclipse.ui.intro.config.IIntroXHTMLContentProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Content provider for the Help intro page
 * 
 * @author Baha El Kassaby
 * 
 */
public class HelpContentProvider implements IIntroXHTMLContentProvider {

	private Logger logger = LoggerFactory.getLogger(HelpContentProvider.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.intro.config.IIntroContentProvider#init(org.eclipse.ui
	 * .intro.config.IIntroContentProviderSite)
	 */
	@Override
	public void init(IIntroContentProviderSite site) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.intro.config.IIntroContentProvider#createContent(java.
	 * lang.String, java.io.PrintWriter)
	 */
	@Override
	public void createContent(String id, PrintWriter out) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.intro.config.IIntroContentProvider#createContent(java.
	 * lang.String, org.eclipse.swt.widgets.Composite,
	 * org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	public void createContent(String id, Composite parent, FormToolkit toolkit) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.intro.config.IIntroContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	private static final String ATT_ID = "id"; //$NON-NLS-1$
	private static final String ATT_NAME = "name"; //$NON-NLS-1$
	private static final String ATT_CAT = "category";
	private static final String CATEGORY = "category";
	private static final String CHEATSHEET = "cheatsheet";

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.intro.config.IIntroXHTMLContentProvider#createContent(
	 * java.lang.String, org.w3c.dom.Element)
	 */
	@Override
	public void createContent(String id, Element parent) {
		buildOpenCheatsheetsGroup();
		
		Document dom = parent.getOwnerDocument();
		

		//parent.setAttribute("class", "treeMenu");
		
		Element ul = dom.createElement("ul");
		//ul.setAttribute("id", "help");
		ul.setAttribute("class", "treeMenu");
		Iterator<IConfigurationElement> iter = introActions.iterator();
		
		List<T<String, String, String>> categories = new ArrayList<T<String, String, String>>();
		List<T<String, String, String>> cheatsheets = new ArrayList<T<String, String, String>>();
		while (iter.hasNext()) {
			IConfigurationElement config = iter.next();
			String type = config.getName();
			String ID = config.getAttribute(ATT_ID);
			String name = config.getAttribute(ATT_NAME);
			String category = config.getAttribute(ATT_CAT);
			IContributor contrib = config.getContributor();
			String contribName = contrib instanceof RegistryContributor ? ((RegistryContributor) contrib)
					.getActualName() : contrib.getName();
			
			// category
			if(type.equals(CATEGORY)){
				if(ID!=null && name != null && contribName != null){
					T <String, String, String> sheet = new T<String, String, String>(ID, name, "");
					categories.add(sheet);
				}
			}
			//cheatsheets
			if(type.equals(CHEATSHEET)) {
				if(ID!=null && name != null && contribName != null){
					T <String, String, String> sheet = new T<String, String, String>(ID, name, category);
					cheatsheets.add(sheet);

				}
			}
		}
		
		categories = orderElements(categories);
		//cheatsheets = orderElements(cheatsheets);
	
		for (int i = 0; i < categories.size(); i++) {
			
			//category
			Element li = dom.createElement("li");
			Element input = dom.createElement("input");
			Element label = dom.createElement("label");
			Element img = dom.createElement("img");
			Element a = dom.createElement("a");
			
			input.setAttribute("name", "tree");
			input.setAttribute("id", "t"+i);
			input.setAttribute("type", "checkbox");
			
			label.setAttribute("for", "t"+i);
			label.setAttribute("class", "open");
			
			img.setAttribute("src", "images/trans.gif");
			img.setAttribute("alt", "");
			
			a.setAttribute("href", "http://org.eclipse.ui.intro/showPage?id=help");
			//a.setAttribute("href", "http://org.eclipse.ui.intro/runAction?pluginId=org.dawnsci.intro&class=org.dawnsci.intro.actions.OpenCheatsheetAction&id="+categories.get(i).getID());

			a.appendChild(dom.createTextNode(categories.get(i).getName()));
			
			label.appendChild(img);
			li.appendChild(input);
			li.appendChild(label);
			li.appendChild(a);
			ul.appendChild(li);
			
			Element ul2 = dom.createElement("ul");
			
			for(int j = 0; j < cheatsheets.size(); j++){
				String category = cheatsheets.get(j).getCategory();
				if(category.equals(categories.get(i).getID())){
					//cheatsheets
					Element li2 = dom.createElement("li");
					Element a2 = dom.createElement("a");
					
					a2.setAttribute("href", "http://org.eclipse.ui.intro/runAction?pluginId=org.dawnsci.intro&class=org.dawnsci.intro.actions.OpenCheatsheetAction&id="+cheatsheets.get(j).getID());
					a2.appendChild(dom.createTextNode(cheatsheets.get(j).getName()));
					li2.appendChild(a2);
					ul2.appendChild(li2);
				}
				

			}
			li.appendChild(ul2);
			ul.appendChild(li);
		}
		
		parent.appendChild(ul);
		
		logger.debug("HTML content generated.");
	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		// windows
		return (os.indexOf("win") >= 0);
	}

	private LinkedList<IConfigurationElement> introActions;
	private static final String CHEATSHEET_EXTENSION_CONTENT = "org.eclipse.ui.cheatsheets.cheatSheetContent";

	private void buildOpenCheatsheetsGroup() {
		introActions = new LinkedList<IConfigurationElement>();

		final IExtension[] extensions = getExtensions(CHEATSHEET_EXTENSION_CONTENT);
		for (int i = 0; i < extensions.length; i++) {

			IExtension extension = extensions[i];
			IConfigurationElement[] configElements = extension
					.getConfigurationElements();

			for (int j = 0; j < configElements.length; j++) {
				IConfigurationElement config = configElements[j];
				introActions.add(config);
			}
		}
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

	/**
	 * Sorts the LinkedList of IConfigurationElements according to their
	 * ATT_NAME
	 * 
	 * @param elems
	 * @return orderedElems
	 */
	private List<T<String, String, String>> orderElements(
			List<T<String, String, String>> elems) {
		List<T<String, String, String>> orderedElems = new LinkedList<T<String, String, String>>();
		
		List<String> items = new LinkedList<String>();

		for (T<String, String, String> t : elems) {
			items.add(t.getName()); 
		}
		Collections.sort(items);
		
		for (String string : items) {
			for (T<String, String, String> t : elems) {
				if(t.getName().equals(string)){
					orderedElems.add(new T<String, String, String>(t.getID(), t.getName(), t.getCategory()));
					break;
				}
			}
		}

		return orderedElems;
	}

	class T<L, R, B> {
		private L id;
		private R name;
		private B category;

		public T(L id, R name, B category) {
			this.id = id;
			this.name = name;
			this.category = category;
		}

		public L getID() {
			return id;
		}

		public R getName() {
			return name;
		}

		public B getCategory(){
			return category;
		}
	}

}
