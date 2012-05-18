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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;

import org.dawnsci.intro.util.FileUtil;

import org.dawnsci.intro.content.job.GetRemoteRssJob;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.intro.config.IIntroContentProviderSite;
import org.eclipse.ui.intro.config.IIntroXHTMLContentProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//import com.sun.syndication.feed.synd.SyndEntry;
//import com.sun.syndication.feed.synd.SyndFeed;
//import com.sun.syndication.io.SyndFeedInput;
//import com.sun.syndication.io.XmlReader;

/**
 *
 */
public class RssContentProvider implements IIntroXHTMLContentProvider {

	
	class ConnectionThread extends Thread {
		private URL rssUrl;

		public ConnectionThread() {
			super();
		}

		@Override
		public void run() {
			try {
				File xmlFile = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile(),xmlName + ".xml");
				boolean isFirstRun = false;
				if (!xmlFile.exists()) {
					isFirstRun = true;
					FileOutputStream out = new FileOutputStream(xmlFile);
					final InputStream initialValue = getClass().getResourceAsStream(xmlName + ".xml");
					FileUtil.copy(initialValue, out);
					out.close();
					initialValue.close();
					rssUrl = xmlFile.toURL();
				}
				
				long millisecondsSinceRefresh = System.currentTimeMillis() - xmlFile.lastModified();
				if (isFirstRun || millisecondsSinceRefresh > ONE_DAY) {
					Job job = new GetRemoteRssJob(url, xmlFile) ;
					job.setSystem(true) ;
					job.setPriority(Job.INTERACTIVE) ;
					job.schedule() ;
				}
				rssUrl = xmlFile.toURL();
			} catch (Exception e) {
				//BonitaStudioLog.log(e) ;
			}
		}
		
		public URL getRss(){
			return rssUrl ;
		}
	}

	private static final long ONE_DAY = 24L * 60 * 60 * 1000L;
	private int nbNews;
	private String url;
	private String xmlName;
	private static DateFormat dateInstance = DateFormat.getDateInstance(DateFormat.SHORT);
	
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
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.intro.config.IIntroContentProvider#createContent(java.lang.String, org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
	 */
	@Override
	public void createContent(String id, Composite parent, FormToolkit toolkit) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.intro.config.IIntroContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.intro.config.IIntroXHTMLContentProvider#createContent(java.lang.String, org.w3c.dom.Element)
	 */
	@Override
	public void createContent(String id, Element parent) {
		try {
			read(id);
			URL fileUrl = getRSS();
			Document dom = parent.getOwnerDocument();
			
//	        SyndFeedInput input = new SyndFeedInput();
//	        SyndFeed feed = input.build(new XmlReader(fileUrl));
//	        Node ul = parent.appendChild(dom.createElement("ul"));
//	        int i = 0;
//	        for (Object entry : feed.getEntries()) {
//	        	SyndEntry syndEntry = (SyndEntry)entry;
//	        	Element li = createLiRssEntryNode(dom, syndEntry);
//	        	ul.appendChild(li);
//	        	i++;
//	        	if (i >= nbNews) {
//	        		break;
//	        	}
//	        }
		} catch (Exception ex) {
			//BonitaStudioLog.log(ex);
		}

	}

	/**
	 * @param id
	 */
	private void read(String id) {
		String[] segments = id.split(",");
		this.nbNews = Integer.parseInt(segments[0].trim());
		this.xmlName = segments[1].trim();
		this.url = segments[2].trim();
	}

	/**
	 * @param dom
	 * @param syndEntry
	 * @return Element
	 */
//	private Element createLiRssEntryNode(Document dom, SyndEntry syndEntry) {
//		/*
//		 * <li>
//		 * 		<a class="rssLink" hef="[link]">
//		 * 			<span class="rssPubDate">[pubDate] - </span>
//		 * 			<span class="rssTitle">[title]</span>
//		 *		</a>
//		 *		<span class="rssDesc"><br />[description]
//		 * </li>
//		 */
//		Element li = dom.createElement("li");
//		Element a = dom.createElement("a");
//		a.setAttribute("href", syndEntry.getLink());
//		a.setAttribute("class", "rssLink");
//		li.appendChild(a);
//		{
//			Element dateSpan = dom.createElement("span");
//			dateSpan.setAttribute("class", "rssPubDate");
//			dateSpan.appendChild( dom.createTextNode(dateInstance.format(syndEntry.getPublishedDate()) + " - " ));
//			a.appendChild(dateSpan);
//		}
//		{
//			Element titleSpan = dom.createElement("span");
//			titleSpan.setAttribute("class", "rssTitle");
//			titleSpan.appendChild(dom.createTextNode(syndEntry.getTitle()));
//			a.appendChild(titleSpan);
//		}
//		{
//			Element descriptionSpan = dom.createElement("span");
//			descriptionSpan.setAttribute("class", "rssDesc");
//			descriptionSpan.appendChild(dom.createElement("br"));
//			descriptionSpan.appendChild(dom.createTextNode(getDesc(syndEntry)));
//			li.appendChild(descriptionSpan);
//		}
//		return li;
//	}
//
//	/**
//	 * @param syndEntry
//	 * @return String
//	 */
//	private String getDesc(SyndEntry syndEntry) {
//		String res = syndEntry.getDescription().getValue();
//		res = res.replace("<br/>", " ");
//		res = res.replace("<br>", " ");
//		res = res.replace("<BR>", " ");
//		res = res.replace("<BR/>", " ");
//		return res;
//	}

	private URL getRSS() throws Exception {
		ConnectionThread thread = new ConnectionThread() ;
		thread.run() ;
		int total = 0 ;
		while (thread.isAlive()) {
	
			if (total > 4000) {
				thread.stop();
				break;
			}
			total = total + 100 ;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				return null; 
			}
		}
		
		return thread.getRss() ;
	}
}
