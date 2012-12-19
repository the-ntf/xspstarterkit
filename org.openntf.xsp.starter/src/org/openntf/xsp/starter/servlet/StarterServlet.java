package org.openntf.xsp.starter.servlet;

import org.openntf.xsp.starter.Activator;

import com.ibm.xsp.webapp.DesignerFacesServlet;

public class StarterServlet extends DesignerFacesServlet {
	private final static boolean _debug = Activator.isDebug();
	static {
		if (_debug)
			System.out.println(StarterServlet.class.getName() + " loaded");
	}

	public StarterServlet() {
		if (_debug) {
			System.out.println(getClass().getName() + " created");
		}
	}

}
