package org.openntf.xsp.sdk.jre;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;
import org.openntf.xsp.sdk.preferences.XPagesSDKPreferences;

public class DynamicVariableResolver implements IDynamicVariableResolver {
	public String resolveValue(IDynamicVariable arg0, String arg1) throws CoreException {
		String ref = XPagesSDKPreferences.resolveConstant(arg0.getName());
		if (XPagesSDKPreferences.NOTES_INSTALL.equals(ref))
			return XPagesSDKPreferences.getNotesInstall();
		if (XPagesSDKPreferences.RCP_BASE.equals(ref))
			try {
				return XPagesSDKPreferences.getRcpBase();
			} catch (Exception e) {
				throw new CoreException(Status.CANCEL_STATUS);
			}
		if (XPagesSDKPreferences.RCP_DATA.equals(ref))
			return XPagesSDKPreferences.getRcpData();
		if (XPagesSDKPreferences.RCP_TARGET.equals(ref))
			return XPagesSDKPreferences.getRcpTarget();
		if (XPagesSDKPreferences.DOMINO_INSTALL.equals(ref))
			return XPagesSDKPreferences.getDominoInstall();
		if (XPagesSDKPreferences.DOMRCP_BASE.equals(ref))
			try {
				return XPagesSDKPreferences.getDomRcpBase();
			} catch (Exception e) {
				throw new CoreException(Status.CANCEL_STATUS);
			}
		if (XPagesSDKPreferences.DOMRCP_DATA.equals(ref))
			return XPagesSDKPreferences.getDomRcpData();
		if (XPagesSDKPreferences.DOMRCP_TARGET.equals(ref))
			return XPagesSDKPreferences.getDomRcpTarget();
		if (XPagesSDKPreferences.DOMSHARED_TARGET.equals(ref))
			return XPagesSDKPreferences.getDomSharedTarget();

		if (XPagesSDKPreferences.DOTSRCP_BASE.equals(ref))
			try {
				return XPagesSDKPreferences.getDotsRcpBase();
			} catch (Exception e) {
				throw new CoreException(Status.CANCEL_STATUS);
			}
		if (XPagesSDKPreferences.DOTSRCP_DATA.equals(ref))
			return XPagesSDKPreferences.getDotsRcpData();
		if (XPagesSDKPreferences.DOTSRCP_TARGET.equals(ref))
			return XPagesSDKPreferences.getDotsRcpTarget();
		if (XPagesSDKPreferences.DOTSSHARED_TARGET.equals(ref))
			return XPagesSDKPreferences.getDotsSharedTarget();

		return "";
	}
}
