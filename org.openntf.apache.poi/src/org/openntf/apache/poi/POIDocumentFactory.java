package org.openntf.apache.poi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.apache.poi.hwpf.HWPFDocument;
import org.osgi.framework.Bundle;

public class POIDocumentFactory {

	public POIDocumentFactory() {
		System.out.println("Why did you instantiate one of these? It's a static factory. *sigh*");
	}

	public static HWPFDocument createBlankHWPFDocument() {
		try {
			InputStream blank = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
				@Override
				public InputStream run() throws Exception {
					// InputStream result = Activator.class.getResourceAsStream("templates/blank.doc");
					Bundle bundle = Activator.getDefault().getBundle();
					URL url = bundle.getEntry("templates/blank.doc");
					InputStream result = url.openStream();
					return result;
				}
			});
			if (blank != null) {
				return new HWPFDocument(blank);
			} else {
				System.out.println("Got a null result back from the plugin");
			}
		} catch (AccessControlException e) {
			e.printStackTrace();
		} catch (PrivilegedActionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
