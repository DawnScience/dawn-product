/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */ 
package org.dawnsci.intro.content;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.Path;
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
 * Content provider for the xhtml page describing a specific perspective
 * TODO
 * @author Baha El Kassaby
 */
public class ASinglePerspectiveContentProvider implements IIntroXHTMLContentProvider {

	private Logger logger = LoggerFactory.getLogger(ASinglePerspectiveContentProvider.class);
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.intro.config.IIntroContentProvider#init(org.eclipse.ui.intro.config.IIntroContentProviderSite)
	 */
	@Override
	public void init(IIntroContentProviderSite site) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.intro.config.IIntroContentProvider#createContent(java.lang.String, java.io.PrintWriter)
	 */
	@Override
	public void createContent(String id, PrintWriter out) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.intro.config.IIntroContentProvider#createContent(java.lang.String, org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	public void createContent(String id, Composite parent, FormToolkit toolkit) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.intro.config.IIntroContentProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	private static final String ATT_CLASS = "class"; //$NON-NLS-1$
	private static final String ATT_NAME= "name"; //$NON-NLS-1$
	private static final String ATT_ICON= "icon"; //$NON-NLS-1$
	private LinkedList<IConfigurationElement> introActions;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.intro.config.IIntroXHTMLContentProvider#createContent(java.lang.String, org.w3c.dom.Element)
	 */
	@Override
	public void createContent(String id, Element parent) {

		//String[] imageURLs = buildOpenPerspectiveGroup();
		System.out.println("AsinglePerspective ID:"+id);
		System.out.println("parent:"+parent.getLocalName());

		Document dom = parent.getOwnerDocument();
		Element div = dom.createElement("div");
		div.setAttribute("class", "grid_"+introActions.size());
		parent.appendChild(div);

		Iterator<IConfigurationElement> iter = introActions.iterator();
		while (iter.hasNext()) {
			try {
			IConfigurationElement config = iter.next();
			//String description = config.getAttribute(ATT_DESCRIPTION);
			//String image = config.getAttribute(ATT_ICON);
			String name = config.getAttribute(ATT_NAME);
			String classname = config.getAttribute(ATT_CLASS);
			IContributor contrib = config.getContributor();
			String contribName = contrib instanceof RegistryContributor ? ((RegistryContributor) contrib).getActualName() : contrib.getName();
			URL bundleURL = FileLocator.find(Platform.getBundle(contribName), new Path(config.getAttribute(ATT_ICON)), null);
			URL imgURL = null;
			
				imgURL = FileLocator.toFileURL(bundleURL);
			
			//Element li = dom.createElement("li");
			Element a = dom.createElement("a");
			Element span = dom.createElement("span");
			Element img = dom.createElement("img");
			a.setAttribute("href", "http://org.eclipse.ui.intro/runAction?pluginId=org.dawnsci.intro&;class=org.dawnsci.intro.actions.OpenPerspectiveAction&name="+classname);
			a.setAttribute("class", "portfolio_item float alpha");
			span.appendChild(dom.createTextNode(name));
			span.setTextContent("testtest");
			img.setAttribute("class", "");

			img.setAttribute("src", imgURL.getPath());
			
			img.setAttribute("alt", "");
			
			//img.appendChild(doc.createTextNode(name));
			a.appendChild(span);
			a.appendChild(img);
			//li.appendChild(a);
			//ul.appendChild(li);
			div.appendChild(a);
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger .error("TODO put description of error here", e);
			}
		}
	}
}
