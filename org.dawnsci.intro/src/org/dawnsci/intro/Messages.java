/*-
 * Copyright (c) 2012 European Synchrotron Radiation Facility,
 *                    Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.dawnsci.intro;

import java.lang.reflect.Field;

import org.eclipse.osgi.util.NLS;

/**
 * @author Baha El Kassaby
 */
public class Messages extends NLS {

	static {
		initializeMessages("messages", Messages.class);
	}
	
	public static String openExampleTitle;
	public static String openExampleMessage;
	public static String openProcessTitle;
	public static String openProcessMessage;
	public static String importProcessTitle;
	public static String importProcessMessage;
	public static String createProcessMessage;
	public static String createProcessTitle;
	public static String welcome;
	
	public static String newProcess;
	public static String openAProcess;
	public static String importAProcess;
	public static String recentlyModified;
	public static String run2;
	public static String examples;
	public static String resources2;
	
	/**
	 * @param id
	 * @return String
	 */
	public static String getMessage(String id) {
		try {
			final Field field = Messages.class.getField(id);
			return (String)field.get(null);
		} catch (NoSuchFieldException ex) {
			return "Field [" + id + "] does not exist";
		} catch (IllegalAccessException ex1) {
			return "Could not easily access to field [" + ex1 + "]\n" + ex1.getMessage();
		}
	}
}
