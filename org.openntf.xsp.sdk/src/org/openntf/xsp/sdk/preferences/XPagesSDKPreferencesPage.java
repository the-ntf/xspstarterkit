package org.openntf.xsp.sdk.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.openntf.xsp.sdk.Activator;
import org.openntf.xsp.sdk.jre.XPagesVMSetup;

public class XPagesSDKPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private DirectoryFieldEditor _installEditor;
	private DirectoryFieldEditor _dataEditor;
	private DirectoryFieldEditor _dominoInstallEditor;
	private DirectoryFieldEditor _dominoDataEditor;
	private Label _notesInstallDisplay;
	private Label _rcpTargetDisplay;
	private Label _rcpDataDisplay;
	private Label _rcpBaseDisplay;
	private BooleanFieldEditor _autoJREEditor;

	public XPagesSDKPreferencesPage() {
		super(GRID);
	}

	public void init(IWorkbench arg0) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());

		setDescription("Preferences for the OpenNTF XPages SDK for Eclipse");
	}

	@Override
	protected void createFieldEditors() {
		_autoJREEditor = new BooleanFieldEditor(XPagesSDKPreferences.AUTO_JRE, "&Automatically create JRE", getFieldEditorParent());
		_installEditor = new DirectoryFieldEditor(XPagesSDKPreferences.NOTES_INSTALL, "&Notes Installation Folder:", getFieldEditorParent());
		_dataEditor = new DirectoryFieldEditor(XPagesSDKPreferences.NOTES_DATA, "&Notes Data Folder:", getFieldEditorParent());
		_dominoInstallEditor = new DirectoryFieldEditor(XPagesSDKPreferences.DOMINO_INSTALL, "&Domino Installation Folder:",
				getFieldEditorParent());
		_dominoDataEditor = new DirectoryFieldEditor(XPagesSDKPreferences.DOMINO_DATA, "&Domino Data Folder:", getFieldEditorParent());
		addField(_autoJREEditor);
		addField(_installEditor);
		addField(_dataEditor);
		addField(_dominoInstallEditor);
		addField(_dominoDataEditor);
		// _notesInstallDisplay = new Label(getFieldEditorParent().getParent(), SWT.HORIZONTAL + SWT.LEFT);
		// _notesInstallDisplay.setText(XPagesSDKPreferences.getNotesInstall());
		// _rcpTargetDisplay = new Label(getFieldEditorParent().getParent(), SWT.HORIZONTAL + SWT.LEFT);
		// _rcpTargetDisplay.setText(XPagesSDKPreferences.getRcpTarget());
		// _rcpDataDisplay = new Label(getFieldEditorParent().getParent().getParent(), SWT.HORIZONTAL + SWT.LEFT);
		// _rcpDataDisplay.setText(XPagesSDKPreferences.getRcpData());
		//
		// _rcpBaseDisplay = new Label(getFieldEditorParent().getParent().getParent(), SWT.HORIZONTAL + SWT.LEFT);
		// try {
		// _rcpBaseDisplay.setText(XPagesSDKPreferences.getRcpBase());
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	@Override
	public boolean performOk() {
		setupVMs();
		return super.performOk();
	}

	@Override
	public void performApply() {
		setupVMs();
		super.performApply();
	}

	private void setupVMs() {
		Boolean autoJRE = _autoJREEditor.getBooleanValue();
		if (autoJRE) {
			String installPath = _installEditor.getStringValue();
			if (installPath != null && installPath.length() > 0) {
				XPagesVMSetup.setupJRE(XPagesSDKPreferences.getJvmPath(installPath));
			}
			String dominoPath = _dominoInstallEditor.getStringValue();
			if (dominoPath != null && dominoPath.length() > 0) {
				XPagesVMSetup.setupDominoJRE(XPagesSDKPreferences.getJvmPath(dominoPath));
			}
		}
	}
}
