package org.openntf.xsp.sdk.templates;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.ui.templates.ITemplateSection;
import org.eclipse.pde.ui.templates.NewPluginTemplateWizard;

public class XPagesLibraryPluginWizard extends NewPluginTemplateWizard {
	private static class ManifestListener implements IResourceChangeListener {
		public void resourceChanged(IResourceChangeEvent event) {
			try {
				event.getDelta().accept(new XPagesLibraryPluginWizard.ManifestModifier(this));
			} catch (CoreException cex) {
				// TemplateUtil.log(cex.getStatus());
			}
		}

		ManifestListener(ManifestListener paramManifestListener) {
		}
	}

	private static final class ManifestModifier implements IResourceDeltaVisitor {
		private static final String MANIFEST_FILE = "MANIFEST.MF";
		private static final String NL = "\r\n";
		private final XPagesLibraryPluginWizard.ManifestListener listener;
		private boolean isDone = false;

		public ManifestModifier(XPagesLibraryPluginWizard.ManifestListener manifestListener) {
			this.listener = manifestListener;
		}

		public boolean visit(IResourceDelta delta) throws CoreException {
			String name = delta.getResource().getName();
			if (("MANIFEST.MF".equals(name)) && (1 == delta.getKind()) && (!(this.isDone))) {
				this.isDone = true;
				IWorkspace ws = ResourcesPlugin.getWorkspace();
				ws.removeResourceChangeListener(this.listener);
				modifyManifest(delta.getResource());
			}
			return (!(this.isDone));
		}

		private void modifyManifest(IResource resource) throws CoreException {
			IFile file = (IFile) resource;
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(file.getContents()));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try {
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));
					try {
						String line = reader.readLine();
						while (line != null) {
							String result = line + "\r\n";
							if (" org.eclipse.core.runtime,".equals(line)) {
								result = "Require-Bundle: com.ibm.xsp.core\r\n, org.eclipse.code.runtime\r\n";
							}
							if (result != null) {
								writer.write(result);
							}
							line = reader.readLine();
						}
						// writer.write("Import-Package: javax.servlet;version=\"2.4.0\",\r\n");
						// writer.write(" javax.servlet.http;version=\"2.4.0\"\r\n");
					} finally {
						writer.close();
					}
				} finally {
					reader.close();
				}
				// scheduleJob(file, baos);
			} catch (IOException ioe) {
				IStatus status = new Status(4, "org.openntf.xsp.sdk.templates", 0, "Could not process MANIFEST.MF", ioe);
				throw new CoreException(status);
			}
		}

		private void scheduleJob(IFile file, ByteArrayOutputStream baos) {
			IResourceRuleFactory ruleFactory = ResourcesPlugin.getWorkspace().getRuleFactory();
			ISchedulingRule rule = ruleFactory.createRule(file);
			String jobName = NLS.bind("Modifing {0}", "MANIFEST.MF");
			// Job job = new XPagesLibraryPluginWizard.1(this, jobName, baos, file);
			//
			// job.setRule(rule);
			// job.schedule(1000L);
		}
	}

	@Override
	public ITemplateSection[] createTemplateSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean performFinish(IProject project, IPluginModelBase model, IProgressMonitor monitor) {
		boolean result = super.performFinish(project, model, monitor);
		if (result) {
			// copyLaunchConfig(project, model);
			IResourceChangeListener listener = new ManifestListener(null);
			ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
			// handleRapTargetVerification();
		}
		return result;
	}
}
