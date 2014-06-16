package org.openntf.xsp.sdk.launcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.pde.launching.AbstractPDELaunchConfiguration;

public class DesignerLaunchConfigurationDelegate extends AbstractPDELaunchConfiguration implements ILaunchConfigurationDelegate {

	public DesignerLaunchConfigurationDelegate() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void launch(ILaunchConfiguration arg0, String arg1, ILaunch arg2, IProgressMonitor arg3) throws CoreException {
		super.launch(arg0, arg1, arg2, arg3);
	}

}
