/* ***************************************************************** */
/* Licensed Materials - Property of IBM                              */
/*                                                                   */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */
/*                                                                   */
/* US Government Users Restricted Rights - Use, duplication or       */
/* disclosure restricted by GSA ADP Schedule Contract with           */
/* IBM Corp.                                                         */
/*                                                                   */
/* ***************************************************************** */

package com.ibm.xsp.extlib.library;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * Extlib Bundle Activator.
 * 
 * @author priand
 */
public class ExtlibActivator extends Plugin {

	public static ExtlibActivator instance;

	public ExtlibActivator() {
		instance = this;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("Starting custom Extension Library for 9.0.1");
	}
}
