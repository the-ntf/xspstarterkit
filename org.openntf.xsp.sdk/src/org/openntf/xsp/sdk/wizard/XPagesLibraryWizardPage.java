package org.openntf.xsp.sdk.wizard;

import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.SelectionButtonDialogFieldGroup;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

@SuppressWarnings("restriction")
public class XPagesLibraryWizardPage extends NewClassWizardPage {
	private static final String PAGE_NAME = "NewXspLibraryWizardPage";
	private SelectionButtonDialogFieldGroup StubsButtonPlaceHolder;

	public XPagesLibraryWizardPage(IStructuredSelection paramIStructuredSelection) {
		super();

		setTitle("New XPages Library");
		setDescription("Create a new XPages Library Class");

		String[] arrayOfString = { NewWizardMessages.NewClassWizardPage_methods_main,
				NewWizardMessages.NewClassWizardPage_methods_constructors, NewWizardMessages.NewClassWizardPage_methods_inherited };
		this.StubsButtonPlaceHolder = new SelectionButtonDialogFieldGroup(32, arrayOfString, 1);
		this.StubsButtonPlaceHolder.setLabelText(NewWizardMessages.NewClassWizardPage_methods_label);

		init(paramIStructuredSelection);

	}

	@Override
	protected void createSuperClassControls(Composite composite, int nColumns) {
		// TODO Auto-generated method stub
		super.createSuperClassControls(composite, nColumns);
	}

	@Override
	public void createControl(Composite paramComposite) {
		initializeDialogUnits(paramComposite);

		Composite localComposite = new Composite(paramComposite, 0);
		localComposite.setFont(paramComposite.getFont());

		int i = 4;

		GridLayout localGridLayout = new GridLayout();
		localGridLayout.numColumns = i;
		localComposite.setLayout(localGridLayout);

		createPackageControls(localComposite, i);
		createEnclosingTypeControls(localComposite, i);

		createSeparator(localComposite, i);

		createTypeNameControls(localComposite, i);
		createModifierControls(localComposite, i);

		createSuperClassControls(localComposite, i);
		createSuperInterfacesControls(localComposite, i);

		// createMethodStubSelectionControls(localComposite, i);

		createCommentControls(localComposite, i);
		enableCommentControl(true);

		setControl(localComposite);
		Dialog.applyDialogFont(localComposite);
		setSuperClass("com.ibm.xsp.library.AbstractXspLibrary", true);
		// org.eclipse.jface.operation.ModalContext mc;
	}

	@Override
	public void init(IStructuredSelection selection) {

		super.init(selection);
	}

	@Override
	public boolean isCreateMain() {
		return false;
	}

	@Override
	public boolean isCreateConstructors() {
		return true;
	}

	@Override
	public boolean isCreateInherited() {
		return true;
	}

}
