package org.openntf.xsp.sdk.launcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.internal.core.LaunchConfiguration;
import org.openntf.xsp.sdk.Activator;

public class DesignerLaunchConfiguration extends LaunchConfiguration {
	public DesignerLaunchConfiguration() throws CoreException {
		super(Activator.getDefault().getBundle().getResource("Designer Full Plugins.launch").toExternalForm());
	}

}
