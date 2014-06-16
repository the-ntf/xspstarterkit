package org.openntf.xsp.sdk.components;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.text.Document;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.openntf.xsp.sdk.Activator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;

public class XspComponentBuilder extends IncrementalProjectBuilder {
	private static final Logger log = Logger.getLogger(XspComponentBuilder.class.getName());

	private JavaProject javaProject;
	private ITypeBinding uiComponentTypeBinding;

	public XspComponentBuilder() {

	}

	@Override
	protected IProject[] build(int arg0, Map arg1, IProgressMonitor arg2) throws CoreException {
		// IWorkbench workbench = PlatformUI.getWorkbench();

		// boolean saveAllEditors = workbench.saveAllEditors(true);
		try {

			IStatus status;
			if (javaProject == null) {
				javaProject = ((JavaProject) JavaCore.create(getProject()));
			}
			BundleContext ctx = Activator.getDefault().getBundle().getBundleContext();
			IPluginModelBase model = PluginRegistry.findModel(getProject());
			BundleDescription desc = model.getBundleDescription();
			List<String> reqNames = new ArrayList<String>();
			List<Bundle> reqBundles = new ArrayList<Bundle>();
			for (BundleSpecification requiredBundle : desc.getRequiredBundles()) {
				reqNames.add(requiredBundle.getName());
				status = new Status(Status.INFO, Activator.PLUGIN_ID, "Required: " + requiredBundle.getName());
				Activator.getDefault().getLog().log(status);
			}
			Map<String, Version> versionMap = new HashMap<String, Version>();
			Map<String, Object> bundleMap = new HashMap<String, Object>();
			for (Bundle bundle : ctx.getBundles()) {
				String name = bundle.getSymbolicName();
				versionMap.put(name, Version.parseVersion(bundle.getHeaders().get("Bundle-version")));
				bundleMap.put(name, bundle);
			}
			for (IPluginModelBase base : PluginRegistry.getExternalModels()) {
				BundleDescription descr = base.getBundleDescription();
				String name = descr.getSymbolicName();
				Version version = base.getBundleDescription().getVersion();
				Object val = bundleMap.get(name);
				if (val == null || !(val instanceof Bundle)) {
					// candidate for replacement
					Version v = versionMap.get(name);
					if (v == null || (version.compareTo(v)) > 0) {
						versionMap.put(name, version);
						bundleMap.put(name, base);
					}
				}

			}
			for (Map.Entry<String, Object> entry : bundleMap.entrySet()) {
				if (entry.getValue() instanceof IPluginModelBase) {
					IPluginModelBase base = (IPluginModelBase) entry.getValue();
					String installLoc = base.getInstallLocation();
					try {
						Bundle bundle = ctx.installBundle("file:" + installLoc);
						// status = new Status(Status.INFO, Activator.PLUGIN_ID, "Installed " + bundle.getSymbolicName());
						// Activator.getDefault().getLog().log(status);
						if (reqNames.contains(bundle.getSymbolicName())) {
							reqBundles.add(bundle);
							status = new Status(Status.WARNING, Activator.PLUGIN_ID, "Bundle " + bundle.getSymbolicName() + " is REQUIRED");
							Activator.getDefault().getLog().log(status);
						}
						if (bundle.getSymbolicName().equals("org.openntf.xsp.comanche.source")) {
							status = new Status(Status.WARNING, Activator.PLUGIN_ID, "Comanche FOUND at " + installLoc);
							Activator.getDefault().getLog().log(status);
						}
					} catch (Exception e) {
						status = new Status(Status.WARNING, Activator.PLUGIN_ID, "unable to install " + installLoc);
						Activator.getDefault().getLog().log(status);
					}
				}
			}
			Class uic = null;

			ServiceReference ref = ctx.getServiceReference(PackageAdmin.class.getName());
			PackageAdmin packageAdmin = (PackageAdmin) ctx.getService(ref);
			// status = new Status(Status.WARNING, Activator.PLUGIN_ID, "PackageAdmin " + packageAdmin.getClass().getName());
			// Activator.getDefault().getLog().log(status);
			Bundle[] b = { ctx.getBundle() };
			// packageAdmin.resolveBundles(reqBundles.toArray(b));
			for (Bundle bundle : reqBundles) {
				try {

					ExportedPackage[] exportedPackages = packageAdmin.getExportedPackages(bundle);
					status = new Status(Status.INFO, Activator.PLUGIN_ID, "Headers for " + bundle.getSymbolicName() + ": "
							+ StringUtils.join(exportedPackages, ','));
					Activator.getDefault().getLog().log(status);
					// for (ExportedPackage ePackage : exportedPackages) {
					//
					// if (ePackage.getName().equals("javax.faces.component")) {
					// uic = bundle.loadClass(ePackage.getName() + ".UIComponent");
					// status = new Status(Status.INFO, Activator.PLUGIN_ID, "Loaded class " + uic.getCanonicalName()
					// + " from bundle " + bundle.getSymbolicName());
					// Activator.getDefault().getLog().log(status);
					//
					// }
					// }
					bundle.start();
				} catch (Exception e) {
					status = new Status(Status.ERROR, Activator.PLUGIN_ID, "Oops on " + bundle.getSymbolicName(), e);
					Activator.getDefault().getLog().log(status);
				}
			}
			ExportedPackage ep = packageAdmin.getExportedPackage("javax.faces.component");
			Bundle jsfCore = ep.getExportingBundle();
			uic = jsfCore.loadClass("javax.faces.component.UIComponent");
			InputStream uicStream = uic.getResourceAsStream("javax.faces.component.UIComponent".replace('.', '/') + ".class");
			Enumeration<URL> e = jsfCore.findEntries("/", "UIComponent" + javaProject.SUFFIX_STRING_class, true);
			//
			// if (e.hasMoreElements()) {
			// URL url = e.nextElement();
			// jsfCore.getResource(url);
			// }
			status = new Status(Status.INFO, Activator.PLUGIN_ID, "Loaded class " + uic.getCanonicalName() + " from bundle "
					+ jsfCore.getSymbolicName());

			Activator.getDefault().getLog().log(status);
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			// parser.
			Map options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_5, options);
			parser.setCompilerOptions(options);
			parser.setProject(javaProject);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setResolveBindings(true);

			IPackageFragment[] packages = javaProject.getPackageFragments();
			for (IPackageFragment mypackage : packages) {
				if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
					for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
						Document doc = new Document(unit.getSource());
						IType[] allTypes = unit.getAllTypes();
						for (IType type : allTypes) {
							IMethod[] methods = type.getMethods();
							for (IMethod method : methods) {

							}
						}
						parser.setSource(unit);
						CompilationUnit cunit = (CompilationUnit) parser.createAST(null);
						ClassVisitor cVisitor = new ClassVisitor();
						// cunit.accept(cVisitor);
						for (TypeDeclaration td : cVisitor.getClasses()) {
							status = new Status(Status.INFO, Activator.PLUGIN_ID, "Found class declaration in " + unit.getElementName()
									+ ": " + td.toString());
							Activator.getDefault().getLog().log(status);
							FieldVisitor fVisitor = new FieldVisitor();
							td.accept(fVisitor);
							for (FieldDeclaration fd : fVisitor.getFields()) {
								status = new Status(Status.INFO, Activator.PLUGIN_ID, "Found field declaration in " + unit.getElementName()
										+ ": " + fd.toString());
								Activator.getDefault().getLog().log(status);

							}
						}

					}
				}

			}
		} catch (Exception e) {
			IStatus status = new Status(Status.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			Activator.getDefault().getLog().log(status);
		}

		return null;
	}

	class CompUnitVisitor extends ASTVisitor {
		@Override
		public boolean visit(CompilationUnit node) {

			return super.visit(node);
		}
	}

	class ClassVisitor extends ASTVisitor {
		final List<TypeDeclaration> classes = new ArrayList<TypeDeclaration>();

		@Override
		public boolean visit(TypeDeclaration node) {
			if (node.isPackageMemberTypeDeclaration() && !node.isInterface()) {
				// node.resolveBinding().isCastCompatible(null);
				Type superclass = node.getSuperclassType();

				ITypeBinding iType = superclass.resolveBinding();
				if (iType != null) {
					String fqn = iType.getQualifiedName();
					if ("com.ibm.xsp.component.UIInputEx".equals(fqn)) {
						classes.add(node);
					} else {
						IStatus status = new Status(Status.INFO, Activator.PLUGIN_ID, "Found fqn of " + fqn);
						Activator.getDefault().getLog().log(status);
					}
				} else {
					IStatus status = new Status(Status.INFO, Activator.PLUGIN_ID, "Unable to resolve superclass for " + node.toString());
					Activator.getDefault().getLog().log(status);
				}

			}
			return super.visit(node);
		}

		public List<TypeDeclaration> getClasses() {
			return classes;
		}

	}

	class MethodVisitor extends ASTVisitor {

	}

	class FieldVisitor extends ASTVisitor {
		final List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();

		@Override
		public boolean visit(FieldDeclaration node) {
			List<IExtendedModifier> mods = node.modifiers();
			boolean add = true;
			for (IExtendedModifier mod : mods) {
				if (mod.isModifier()) {
					Modifier m = (Modifier) mod;
					if (m.isFinal() || m.isNative() || m.isStatic() || m.isTransient()) {
						add = false;
						continue;
					}
				}
			}
			if (add)
				fields.add(node);
			return super.visit(node);
		}

		public List<FieldDeclaration> getFields() {
			return fields;
		}
	}
}
