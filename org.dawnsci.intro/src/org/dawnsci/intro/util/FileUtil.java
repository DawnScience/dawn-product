/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */ 
package org.dawnsci.intro.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

public static int bufferSize = 2*8192;
	
	public static void copy(InputStream in, OutputStream out)throws IOException {
		copy(in,out,-1);
	}

	public static void copy(InputStream in, OutputStream out, long byteCount)throws IOException{
		byte buffer[] = new byte[bufferSize];
		int len=bufferSize;
		if (byteCount>=0){
			while (byteCount>0){
				int max = byteCount<bufferSize?(int)byteCount:bufferSize;
				len=in.read(buffer,0,max);
				if (len==-1)
					break;
				byteCount -= len;
				out.write(buffer,0,len);
			}
		}else{
			while (true){
				len=in.read(buffer,0,bufferSize);
				if (len<0 )
					break;
				out.write(buffer,0,len);
			}
		}
		out.close();
		in.close();
	}
}
