package org.openntf.xsp.sdk;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.openntf.xsp.sdk.utils.XspProjectUtils;

public class XspNature implements IProjectNature {
	public final static String XSP_NATURE_ID = "org.openntf.xsp.sdk.XspNature";
	public final static String COMPONENT_BUILDER_ID = "org.openntf.xsp.sdk.components.XspComponentBuilder";
	private IProject project;

	public XspNature() {
		// TODO Auto-generated constructor stub
	}

	public void configure() throws CoreException {
		XspProjectUtils.addBuilder(getProject(), COMPONENT_BUILDER_ID);
	}

	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject arg0) {
		project = arg0;
	}
}
