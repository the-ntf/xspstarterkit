package org.openntf.xsp.sdk.jre;

import java.io.File;

import org.eclipse.jdt.internal.launching.StandardVM;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.openntf.xsp.sdk.preferences.XPagesSDKPreferences;

public class XPagesVMSetup {
	public final static String NOTES_VM_ID = "org.openntf.xsp.notes.jre";
	public final static String DOMINO_VM_ID = "org.openntf.xsp.domino.jre";
	public final static String NOTES_VM_NAME = "XPages Notes JRE";
	public final static String DOMINO_VM_NAME = "XPages Domino JRE";
	public final static String NOTES_VM_ARGS = "-Djava.library.path=${notes_install} -Xj9";
	public final static String DOMINO_VM_ARGS = "-Djava.library.path=${domino_install} -Xj9";

	public XPagesVMSetup() {

	}

	@SuppressWarnings("restriction")
	public static void setupJRE() {
		// check that the appropriate JRE is configured for this version of Notes
		// System.out.println("JRE Setup Activated!");
		String jvmPath = XPagesSDKPreferences.getJvmPath();

		String vmID = NOTES_VM_ID;
		IVMInstallType installType = JavaRuntime.getVMInstallType("org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType");
		IVMInstall install = installType.findVMInstall(vmID);

		if (install == null) {
			install = installType.createVMInstall(vmID);
			install.setName(NOTES_VM_NAME);
			install.setInstallLocation(new File(jvmPath));
			((StandardVM) install).setVMArgs(NOTES_VM_ARGS);
			JavaRuntime.fireVMAdded(install);

		}
	}

	@SuppressWarnings("restriction")
	public static void setupJRE(String jvmPath) {
		// check that the appropriate JRE is configured for this version of Notes
		// System.out.println("JRE Setup Activated!");
		String vmID = NOTES_VM_ID;
		IVMInstallType installType = JavaRuntime.getVMInstallType("org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType");
		IVMInstall install = installType.findVMInstall(vmID);

		if (install == null) {
			install = installType.createVMInstall(vmID);
			install.setName(NOTES_VM_NAME);
			install.setInstallLocation(new File(jvmPath));
			((StandardVM) install).setVMArgs(NOTES_VM_ARGS);
			JavaRuntime.fireVMAdded(install);
		}
	}

	@SuppressWarnings("restriction")
	public static void setupDominoJRE(String jvmPath) {
		// check that the appropriate JRE is configured for this version of Notes
		// System.out.println("JRE Setup Activated!");
		String vmID = DOMINO_VM_ID;
		IVMInstallType installType = JavaRuntime.getVMInstallType("org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType");
		IVMInstall install = installType.findVMInstall(vmID);

		if (install == null) {
			install = installType.createVMInstall(vmID);
			install.setName(DOMINO_VM_NAME);
			install.setInstallLocation(new File(jvmPath));
			((StandardVM) install).setVMArgs(DOMINO_VM_ARGS);
			JavaRuntime.fireVMAdded(install);
		}
	}
}
