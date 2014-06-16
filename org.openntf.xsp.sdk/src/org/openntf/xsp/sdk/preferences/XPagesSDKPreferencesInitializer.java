package org.openntf.xsp.sdk.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.openntf.xsp.sdk.Activator;
import org.osgi.service.prefs.Preferences;

public class XPagesSDKPreferencesInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		preferences.put(XPagesSDKPreferences.NOTES_INSTALL, XPagesSDKPreferences.NOTES_INSTALL_DEFAULT);
		preferences.put(XPagesSDKPreferences.NOTES_DATA, XPagesSDKPreferences.NOTES_DATA_DEFAULT);
		preferences.put(XPagesSDKPreferences.DOMINO_INSTALL, XPagesSDKPreferences.DOMINO_INSTALL_DEFAULT);
		preferences.put(XPagesSDKPreferences.DOMINO_DATA, XPagesSDKPreferences.DOMINO_DATA_DEFAULT);
		preferences.putBoolean(XPagesSDKPreferences.AUTO_JRE, XPagesSDKPreferences.AUTO_JRE_DEFAULT);

		// IPreferencesService service = Platform.getPreferencesService();
		//
		// service.setDefault(Activator.PLUGIN_ID, XPagesSDKPreferences.NOTES_INSTALL, XPagesSDKPreferences.NOTES_INSTALL_DEFAULT);
	}

}
