/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.dawnsci.internal.intro.universal;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.intro.config.IntroElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PageData {
	public static final String P_TOP_LEFT = "page-content/top-left"; //$NON-NLS-1$
	public static final String P_TOP_RIGHT = "page-content/top-right"; //$NON-NLS-1$
	public static final String P_BOTTOM_LEFT = "page-content/bottom-left"; //$NON-NLS-1$
	public static final String P_BOTTOM_RIGHT = "page-content/bottom-right"; //$NON-NLS-1$

	private String id;
	private ArrayList<GroupData> groups = new ArrayList<GroupData>();
	private GroupData hidden=null;
	
	public PageData(String id) {
		this.id = id;
	}
	
	public PageData(Element page) {
		this.id = page.getAttribute("id"); //$NON-NLS-1$
		NodeList children = page.getChildNodes();
		for (int i=0; i<children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType()==Node.ELEMENT_NODE) {
				Element element = (Element)child;
				if (element.getNodeName().equals("group")) { //$NON-NLS-1$
					addGroup(element, false);
				}
				else if (element.getNodeName().equals(IUniversalIntroConstants.HIDDEN)) {
					addGroup(element, true);
				}
			}
		}
	}
	
	public void add(GroupData gd) {
		if (gd.isHidden())
			hidden = gd;
		else
			groups.add(gd);
	}

	public void addAnchors(List<IntroElement> result, String groupId) {
		GroupData group = findGroup(groupId);
		if (group==null) return;
		group.addAnchors(result);
	}

	public String resolvePath(String extensionId) {
		if (isHidden(extensionId))
			return null;
		for (int i=0; i<groups.size(); i++) {
			GroupData gdata = (GroupData)groups.get(i);
			if (gdata.contains(extensionId)) {
				IPath resolvedPath = new Path(id);
				resolvedPath = resolvedPath.append(gdata.getPath());
				resolvedPath = resolvedPath.append(extensionId);
				return resolvedPath.toString();
			}
		}
		return null;
	}

	public String resolveDefaultPath() {
		for (int i=0; i<groups.size(); i++) {
			GroupData gdata = (GroupData)groups.get(i);
			if (gdata.isDefault()) {
				IPath resolvedPath = new Path(id).append(gdata.getPath());
				resolvedPath = resolvedPath.append(IUniversalIntroConstants.DEFAULT_ANCHOR);
				return resolvedPath.toString();
			}
		}
		return null;
	}

	public boolean isHidden(String extensionId) {
		return hidden!=null && hidden.contains(extensionId);
	}
	
	public GroupData findGroup(String groupId) {
		if (groupId.equals(IUniversalIntroConstants.HIDDEN))
			return hidden;
		for (int i=0; i<groups.size(); i++) {
			GroupData gdata = (GroupData)groups.get(i);
			IPath path = new Path(gdata.getPath());
			if (path.lastSegment().equals(groupId))
				return gdata;
		}
		return null;
	}
	
	private void addGroup(Element element, boolean hide) {
		GroupData gd = new GroupData(element);
		if (hide) hidden = gd;
		else
			groups.add(gd);
	}
	
	public void addImplicitExtension(String extensionId, String name) {
		ExtensionData ed = findExtension(extensionId, true);
		if (ed!=null) {
			// see if name needs to be supplied
			if (ed.getName()==null || ed.getName().length()==0)
				ed.setName(name);
			return;
		}
		GroupData gd = findDefaultGroup();
		if (gd==null && groups.size()==0) {
			// add bottoms as the default group
			gd = new GroupData(P_BOTTOM_LEFT, true);
			groups.add(gd);
			groups.add(new GroupData(P_BOTTOM_RIGHT, true));
		}
		gd.addImplicitExtension(extensionId, name);
	}

	private GroupData findDefaultGroup() {
		GroupData defaultGroup = null;
		for (int i=0; i<groups.size(); i++) {
			GroupData gd = (GroupData)groups.get(i);
			if (gd.isDefault()) {
				if (defaultGroup==null)
					defaultGroup = gd;
				else
					if (defaultGroup.getExtensionCount()>gd.getExtensionCount())
						defaultGroup = gd;
			}
		}
		return defaultGroup;
	}
	
	public String getId() {
		return id;
	}
	
	public ExtensionData findExtension(String extensionId, boolean checkHidden) {
		for (int i=0; i<groups.size(); i++) {
			GroupData gdata = (GroupData)groups.get(i);
			ExtensionData ed = find(gdata, extensionId);
			if (ed!=null)
				return ed;
		}
		// check the hidden
		if (checkHidden && hidden!=null)
			return find (hidden, extensionId);
		return null;
	}
		
	private ExtensionData find(GroupData gd, String extensionId) {
		BaseData bd = gd.find(extensionId);
		if (bd!=null && bd instanceof ExtensionData)
			return (ExtensionData)bd;
		return null;
	}

	public String resolveExtension(String extensionId) {
		// check the explicit groups
		for (int i=0; i<groups.size(); i++) {
			GroupData gdata = (GroupData)groups.get(i);
			if (gdata.contains(extensionId)) {
				return id+"/"+gdata.getPath()+"/"+extensionId;  //$NON-NLS-1$//$NON-NLS-2$
			}
		}
		// check the hidden
		if (hidden!=null && hidden.contains(extensionId))
			return null;
		// create the default: pick the last group
		if (groups.size()==0) return null;
		GroupData last = (GroupData)groups.get(groups.size()-1);
		return id + "/" + last.getPath() + "/" + IUniversalIntroConstants.DEFAULT_ANCHOR;  //$NON-NLS-1$//$NON-NLS-2$
	}
	
	public void write(PrintWriter writer, String indent) {
		writer.print(indent);
		String indent2 = indent+"   "; //$NON-NLS-1$
		writer.println("<page id=\""+id+"\">");  //$NON-NLS-1$//$NON-NLS-2$
		for (int i=0; i<groups.size(); i++) {
			GroupData gd = (GroupData)groups.get(i);
			gd.write(writer, indent2);
		}
		if (hidden!=null)
			hidden.write(writer, indent2);
		writer.print(indent);
		writer.println("</page>"); //$NON-NLS-1$
	}
}