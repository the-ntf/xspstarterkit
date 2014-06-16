package org.openntf.xsp.sdk.preferences;

import java.io.File;
import java.io.FilenameFilter;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.openntf.xsp.sdk.Activator;
import org.openntf.xsp.sdk.exceptions.XPagesSDKException;

public class XPagesSDKPreferences {
	public static enum Target {
		NOTES, DOMINO, DOTS
	}

	public final static String AUTO_JRE = "AUTO_JRE";
	public final static Boolean AUTO_JRE_DEFAULT = true;
	public final static String NOTES_INSTALL = "NOTES_INSTALL";
	public final static String NOTES_INSTALL_DEFAULT = "C:/Program Files/IBM/Lotus/Notes";
	public final static String NOTES_DATA = "NOTES_DATA";
	public final static String NOTES_DATA_DEFAULT = "C:/Program Files/IBM/Lotus/Notes/Data";

	public final static String DOMINO_INSTALL = "DOMINO_INSTALL";
	public final static String DOMINO_INSTALL_DEFAULT = "C:/Program Files/IBM/Lotus/Domino";
	public final static String DOMINO_DATA = "DOMINO_DATA";
	public final static String DOMINO_DATA_DEFAULT = "C:/Program Files/IBM/Lotus/Domino/Data";

	public final static String RCP_BASE = "RCP_BASE";
	public final static String RCP_TARGET = "RCP_TARGET";
	public final static String RCP_DATA = "RCP_DATA";

	public final static String DOMRCP_BASE = "DOMRCP_BASE";
	public final static String DOMRCP_TARGET = "DOMRCP_TARGET";
	public final static String DOMSHARED_TARGET = "DOMSHARED_TARGET";
	public final static String DOMRCP_DATA = "DOMRCP_DATA";

	public final static String DOTSRCP_BASE = "DOTSRCP_BASE";
	public final static String DOTSRCP_TARGET = "DOTSRCP_TARGET";
	public final static String DOTSSHARED_TARGET = "DOTSSHARED_TARGET";
	public final static String DOTSRCP_DATA = "DOTSRCP_DATA";

	public final static String RCP_TARGET_FOLDER = "/framework/rcp/eclipse";
	public final static String RCP_PLUGIN_FOLDER = "/framework/rcp/eclipse/plugins";
	public final static String RCP_DATA_FOLDER = "/workspace";
	public final static String RCP_BASE_FOLDER_PREFIX = "com.ibm.rcp.base_";

	public final static String DOMRCP_TARGET_FOLDER = "/osgi/rcp/eclipse";
	public final static String DOMRCP_PLUGIN_FOLDER = "/osgi/rcp/eclipse/plugins";
	public final static String DOMSHARED_TARGET_FOLDER = "/osgi/shared/eclipse";
	public final static String DOMRCP_DATA_FOLDER = "/domino/workspace";
	public final static String DOMRCP_BASE_FOLDER_PREFIX = "com.ibm.rcp.base_";

	public final static String DOTSRCP_TARGET_FOLDER = "/osgi-dots/rcp/eclipse";
	public final static String DOTSRCP_PLUGIN_FOLDER = "/osgi-dots/rcp/eclipse/plugins";
	public final static String DOTSSHARED_TARGET_FOLDER = "/osgi-dots/shared/eclipse";
	public final static String DOTSRCP_DATA_FOLDER = "/domino/workspace-dots";

	public static String resolveConstant(String arg0) {
		if ("notes_install".equals(arg0))
			return NOTES_INSTALL;
		if ("notes_data".equals(arg0))
			return NOTES_DATA;
		if ("rcp_base".equals(arg0))
			return RCP_BASE;
		if ("rcp_target".equals(arg0))
			return RCP_TARGET;
		if ("rcp_data".equals(arg0))
			return RCP_DATA;
		if ("domino_install".equals(arg0))
			return DOMINO_INSTALL;
		if ("domino_data".equals(arg0))
			return DOMINO_DATA;
		if ("domino_rcp_base".equals(arg0))
			return DOMRCP_BASE;
		if ("domino_rcp_target".equals(arg0))
			return DOMRCP_TARGET;
		if ("domino_rcp_data".equals(arg0))
			return DOMRCP_DATA;
		if ("domino_shared_target".equals(arg0))
			return DOMSHARED_TARGET;

		if ("dots_data".equals(arg0))
			return DOMINO_DATA;
		if ("dots_rcp_base".equals(arg0))
			return DOTSRCP_BASE;
		if ("dots_rcp_target".equals(arg0))
			return DOTSRCP_TARGET;
		if ("dots_rcp_data".equals(arg0))
			return DOTSRCP_DATA;
		if ("dots_shared_target".equals(arg0))
			return DOTSSHARED_TARGET;

		return null;
	}

	private static IPreferenceStore getStore() {
		IPreferenceStore result = Activator.getDefault().getPreferenceStore();
		result.setDefault(NOTES_INSTALL, NOTES_INSTALL_DEFAULT);
		result.setDefault(NOTES_DATA, NOTES_DATA_DEFAULT);
		result.setDefault(DOMINO_INSTALL, DOMINO_INSTALL_DEFAULT);
		result.setDefault(DOMINO_DATA, NOTES_DATA_DEFAULT);
		return result;
	}

	public static String getNotesInstall() {
		return getStore().getString(NOTES_INSTALL);
		// Activator.getDefault().getPreferenceStore().getString(NOTES_INSTALL);
		// Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		// return preferences.get(NOTES_INSTALL, NOTES_INSTALL_DEFAULT);
	}

	public static String getNotesData() {
		return getStore().getString(NOTES_DATA);
		// Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		// return preferences.get(NOTES_DATA, NOTES_DATA_DEFAULT);
	}

	public static String getRcpBase() throws Exception {
		String rcpBaseFolder = findRcpBaseFolder(Target.NOTES);
		if (rcpBaseFolder == null) {
			IStatus status = new Status(Status.INFO, Activator.PLUGIN_ID, "Unable to find rcpBaseFolder!");
			Activator.getDefault().getLog().log(status);
			throw new XPagesSDKException("Unable to find rcpBaseFolder!");
		}
		return rcpBaseFolder.replace('\\', '/');

	}

	public static String getDominoInstall() {
		return getStore().getString(DOMINO_INSTALL);
		// Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		// return preferences.get(DOMINO_INSTALL, DOMINO_INSTALL_DEFAULT);
	}

	public static String getDominoData() {
		return getStore().getString(DOMINO_DATA);
		// Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		// return preferences.get(DOMINO_DATA, DOMINO_DATA_DEFAULT);
	}

	public static String getDomRcpBase() throws Exception {
		return findRcpBaseFolder(Target.DOMINO);
	}

	private static String findRcpBaseFolder(final Target target) throws Exception {
		IPath basePath;
		if (target == Target.DOMINO || target == Target.DOTS) {
			basePath = new Path(getDominoInstall());
		} else {
			basePath = new Path(getNotesInstall());
		}
		File install = new File(basePath.toOSString());

		if (install.isDirectory()) {
			IPath rcpPluginPath = null;
			if (target == Target.DOMINO) {
				rcpPluginPath = new Path(DOMRCP_PLUGIN_FOLDER);
			} else if (target == Target.DOTS) {
				rcpPluginPath = new Path(DOTSRCP_PLUGIN_FOLDER);
			} else {
				rcpPluginPath = new Path(RCP_PLUGIN_FOLDER);
			}
			IPath rcpPath = basePath.append(rcpPluginPath);
			File rcp = new File(rcpPath.toOSString());
			File[] baseFolders = rcp.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String prefix = "";
					if (target == Target.DOMINO) {
						prefix = DOMRCP_BASE_FOLDER_PREFIX;
					} else if (target == Target.DOTS) {
						prefix = DOMRCP_BASE_FOLDER_PREFIX;
					} else {
						prefix = RCP_BASE_FOLDER_PREFIX;
					}
					return name.startsWith(prefix);
				}
			});

			if (baseFolders.length >= 1) {
				return baseFolders[0].getAbsolutePath();
			} else {
				IStatus status = new Status(Status.WARNING, Activator.PLUGIN_ID, "Unable to find base folder " + RCP_BASE_FOLDER_PREFIX);
				Activator.getDefault().getLog().log(status);
				throw new XPagesSDKException("Unable to find base folder " + RCP_BASE_FOLDER_PREFIX);
			}
		} else {
			IStatus status = new Status(Status.INFO, Activator.PLUGIN_ID, "Notes container for location " + basePath
					+ " is NOT a folder from root ");
			Activator.getDefault().getLog().log(status);
			throw new XPagesSDKException("Notes container for location " + basePath + " is NOT a folder from root ");
		}
	}

	public static String getRcpTarget() {
		return (getNotesInstall() + RCP_TARGET_FOLDER).replace('\\', '/');
	}

	public static String getRcpData() {
		return (getNotesData() + RCP_DATA_FOLDER).replace('\\', '/');
	}

	public static String getJvmPath() {
		return getNotesInstall() + "/jvm";
	}

	public static String getDomRcpTarget() {
		return getDominoInstall() + DOMRCP_TARGET_FOLDER;
	}

	public static String getDomSharedTarget() {
		return getDominoInstall() + DOMSHARED_TARGET_FOLDER;
	}

	public static String getDomRcpData() {
		return getDominoData() + DOMRCP_DATA_FOLDER;
	}

	public static String getDotsRcpTarget() {
		return getDominoInstall() + DOTSRCP_TARGET_FOLDER;
	}

	public static String getDotsSharedTarget() {
		return getDominoInstall() + DOTSSHARED_TARGET_FOLDER;
	}

	public static String getDotsRcpBase() throws Exception {
		return findRcpBaseFolder(Target.DOTS);
	}

	public static String getDotsRcpData() {
		return getDominoData() + DOTSRCP_DATA_FOLDER;
	}

	public static String getDomJvmPath() {
		return getDominoInstall() + "/jvm";
	}

	public static String getJvmPath(String basePath) {
		return basePath + "/jvm";
	}

}
