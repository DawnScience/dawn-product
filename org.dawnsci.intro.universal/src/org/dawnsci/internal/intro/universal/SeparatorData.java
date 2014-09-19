/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.dawnsci.internal.intro.universal;

import java.io.PrintWriter;

public class SeparatorData extends BaseData {
	
	public SeparatorData() {
	}
	
	public SeparatorData(String id) {
		this.id = id;
	}

	public void write(PrintWriter writer, String indent) {
		writer.print(indent);
		writer.print("<separator id=\""); //$NON-NLS-1$
		writer.print(id);
		writer.println("\"/>"); //$NON-NLS-1$
	}
}