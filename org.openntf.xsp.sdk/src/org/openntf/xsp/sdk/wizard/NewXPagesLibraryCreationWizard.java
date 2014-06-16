package org.openntf.xsp.sdk.wizard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.wizards.NewElementWizard;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.plugin.IFragment;
import org.eclipse.pde.core.plugin.IPlugin;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginLibrary;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.core.plugin.ISharedPluginModel;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.internal.core.TargetPlatformHelper;
import org.eclipse.pde.internal.core.bundle.WorkspaceBundleFragmentModel;
import org.eclipse.pde.internal.core.bundle.WorkspaceBundlePluginModel;
import org.eclipse.pde.internal.core.ibundle.IBundle;
import org.eclipse.pde.internal.core.ibundle.IBundlePluginBase;
import org.eclipse.pde.internal.core.ibundle.IBundlePluginModelBase;
import org.eclipse.pde.internal.core.plugin.WorkspaceFragmentModel;
import org.eclipse.pde.internal.core.plugin.WorkspacePluginModel;
import org.eclipse.pde.internal.core.plugin.WorkspacePluginModelBase;
import org.eclipse.pde.internal.ui.search.dependencies.AddNewBinaryDependenciesOperation;
import org.eclipse.pde.internal.ui.wizards.plugin.AbstractFieldData;
import org.eclipse.pde.internal.ui.wizards.plugin.LibraryPluginFieldData;
import org.eclipse.pde.ui.IFieldData;
import org.eclipse.pde.ui.IFragmentFieldData;
import org.eclipse.pde.ui.IPluginFieldData;
import org.eclipse.pde.ui.templates.PluginReference;

@SuppressWarnings("restriction")
public class NewXPagesLibraryCreationWizard extends NewElementWizard {
	// private final NewClassCreationWizard _delegate;
	private XPagesLibraryWizardPage fPage;

	public NewXPagesLibraryCreationWizard() {
		super();
		// _delegate = new NewClassCreationWizard(getPage(), true);
	}

	// protected XPagesLibraryWizardPage getPage() {
	// if (fPage == null) {
	// addPages();
	// }
	// return fPage;
	// }

	@Override
	public void addPages() {
		super.addPages();
		if (this.fPage == null) {
			this.fPage = new XPagesLibraryWizardPage(getSelection());
		}
		addPage(this.fPage);
	}

	@Override
	public boolean performFinish() {
		// TODO create extension point
		// TODO create services file
		// org.eclipse.ui.actions.WorkspaceModifyOperation wmo;
		warnAboutTypeCommentDeprecation();
		boolean res = super.performFinish();

		if (res) {
			IResource resource = this.fPage.getModifiedResource();

			IJavaProject project = this.fPage.getJavaProject();
			try {
				String fqn = fPage.getPackageText() + "." + fPage.getTypeName();
				for (IClasspathEntry entry : project.getRawClasspath()) {
					if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
						for (IPackageFragmentRoot frag : project.findPackageFragmentRoots(entry)) {
							IFolder srcFolder = (IFolder) frag.getCorrespondingResource();
							IFolder metaFolder = srcFolder.getFolder("META-INF");
							if (!(metaFolder.exists())) {
								metaFolder.create(true, true, null);
							}
							IFolder servicesFolder = metaFolder.getFolder("services");
							if (!(servicesFolder.exists())) {
								servicesFolder.create(true, true, null);
							}
							IFile libFile = servicesFolder.getFile("com.ibm.xsp.Library");
							if (!(libFile.exists())) {
								// IType type = (IType) project.findElement(resource.getLocation());
								// String fqn = type.getFullyQualifiedName('.');

								StringBuilder builder = new StringBuilder();
								builder.append(fqn);
								InputStream is = new ByteArrayInputStream(builder.toString().getBytes());
								libFile.create(is, true, null);
							}
						}
					}
				}
				WorkspacePluginModelBase model = new WorkspacePluginModel(project.getProject().getFile("plugin.xml"), false);
				/*
				 * <extension point="com.ibm.commons.Extension"> <service class="org.openntf.xsp.starter.library.StarterLibrary"
				 * type="com.ibm.xsp.Library"> </service> </extension>
				 */
				IPluginBase base = model.getPluginBase();
				IPluginExtension ext = model.createExtension();
				base.add(ext);
				ext.setPoint("com.ibm.commons.Extension");
				IPluginElement elem = model.createElement(ext);
				ext.add(elem);
				elem.setName("service");
				elem.setAttribute("class", fqn);
				elem.setAttribute("type", "com.ibm.xsp.Library");
				model.setDirty(true);
				model.save();
			} catch (JavaModelException e) {
				throw new RuntimeException(e);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}

			if (resource != null) {
				selectAndReveal(resource);
				openResource((IFile) resource);

			}
		}
		return res;
	}

	@SuppressWarnings({ "restriction", "unused" })
	private void addDependencies(IProject project, ISharedPluginModel model, LibraryPluginFieldData fData, IProgressMonitor monitor) {
		if (!(model instanceof IBundlePluginModelBase)) {
			monitor.done();
			return;
		}

		try {
			final boolean unzip = fData.isUnzipLibraries();
			new AddNewBinaryDependenciesOperation(project, (IBundlePluginModelBase) model) {
				@SuppressWarnings("rawtypes")
				@Override
				protected String[] findSecondaryBundles(IBundle bundle, Set ignorePkgs) {
					IPluginModelBase[] bases = PluginRegistry.getActiveModels();
					String[] ids = new String[bases.length];
					for (int i = 0; i < bases.length; ++i) {
						BundleDescription desc = bases[i].getBundleDescription();
						if (desc == null)
							ids[i] = bases[i].getPluginBase().getId();
						else
							ids[i] = desc.getSymbolicName();
					}
					return ids;
				}

				@SuppressWarnings({ "unchecked", "rawtypes" })
				@Override
				protected void addProjectPackages(IBundle bundle, Set ignorePkgs) {
					if (!(unzip))
						super.addProjectPackages(bundle, ignorePkgs);
					Stack<IResource> stack = new Stack<IResource>();
					stack.push(this.fProject);
					try {
						while (!(stack.isEmpty())) {
							IContainer folder = (IContainer) stack.pop();
							IResource[] children = folder.members();
							for (int i = 0; i < children.length; ++i)
								if (children[i] instanceof IContainer) {
									stack.push(children[i]);
								} else if ("class".equals(((IFile) children[i]).getFileExtension())) {
									String path = folder.getProjectRelativePath().toString();
									ignorePkgs.add(path.replace('/', '.'));
								}
						}
					} catch (CoreException localCoreException) {
					}
				}
			}.run(monitor);
		} catch (InvocationTargetException localInvocationTargetException) {
		} catch (InterruptedException localInterruptedException) {
		}
	}

	protected void createManifest(IProject project, IFieldData fData) throws CoreException {
		WorkspacePluginModelBase fModel;
		if (fData.hasBundleStructure()) {
			if (fData instanceof IFragmentFieldData)
				fModel = new WorkspaceBundleFragmentModel(project.getFile("META-INF/MANIFEST.MF"), project.getFile("fragment.xml"));
			else {
				fModel = new WorkspaceBundlePluginModel(project.getFile("META-INF/MANIFEST.MF"), project.getFile("plugin.xml"));
			}
		} else if (fData instanceof IFragmentFieldData)
			fModel = new WorkspaceFragmentModel(project.getFile("fragment.xml"), false);
		else {
			fModel = new WorkspacePluginModel(project.getFile("plugin.xml"), false);
		}
		IPluginBase pluginBase = fModel.getPluginBase();
		String targetVersion = ((AbstractFieldData) fData).getTargetVersion();
		pluginBase.setSchemaVersion(TargetPlatformHelper.getSchemaVersionForTargetVersion(targetVersion));
		pluginBase.setId(fData.getId());
		pluginBase.setVersion(fData.getVersion());
		pluginBase.setName(fData.getName());
		pluginBase.setProviderName(fData.getProvider());
		if (fModel instanceof IBundlePluginModelBase) {
			IBundlePluginModelBase bmodel = (IBundlePluginModelBase) fModel;
			((IBundlePluginBase) bmodel.getPluginBase()).setTargetVersion(targetVersion);
			bmodel.getBundleModel().getBundle().setHeader("Bundle-ManifestVersion", "2");
		}
		if (pluginBase instanceof IFragment) {
			IFragment fragment = (IFragment) pluginBase;
			IFragmentFieldData data = (IFragmentFieldData) fData;
			fragment.setPluginId(data.getPluginId());
			fragment.setPluginVersion(data.getPluginVersion());
			fragment.setRule(data.getMatch());
		} else if (((IPluginFieldData) fData).doGenerateClass()) {
			((IPlugin) pluginBase).setClassName(((IPluginFieldData) fData).getClassname());
		}
		if (!(fData.isSimple())) {
			setPluginLibraries(fModel, fData);
		}

		IPluginReference[] dependencies = getDependencies();
		for (int i = 0; i < dependencies.length; ++i) {
			IPluginReference ref = dependencies[i];
			IPluginImport iimport = fModel.getPluginFactory().createImport();
			iimport.setId(ref.getId());
			iimport.setVersion(ref.getVersion());
			iimport.setMatch(ref.getMatch());
			pluginBase.add(iimport);
		}

	}

	protected void setPluginLibraries(WorkspacePluginModelBase model, IFieldData fData) throws CoreException {
		String libraryName = fData.getLibraryName();
		if ((libraryName == null) && (!(fData.hasBundleStructure()))) {
			libraryName = ".";
		}
		if (libraryName != null) {
			IPluginLibrary library = model.getPluginFactory().createLibrary();
			library.setName(libraryName);
			library.setExported(!(fData.hasBundleStructure()));
			model.getPluginBase().add(library);
		}
	}

	public IPluginReference[] getDependencies() {
		ArrayList<PluginReference> result = new ArrayList<PluginReference>();
		result.add(new PluginReference("com.ibm.xsp.core", null, 0));
		return result.toArray(new IPluginReference[result.size()]);
	}

	@Override
	protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException {
		this.fPage.createType(monitor);
	}

	@Override
	public IJavaElement getCreatedElement() {
		return this.fPage.getCreatedType();
	}

	@Override
	protected boolean canRunForked() {
		return (!(this.fPage.isEnclosingTypeSelected()));
	}

}
