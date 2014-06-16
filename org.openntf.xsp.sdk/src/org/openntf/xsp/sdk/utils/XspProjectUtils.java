package org.openntf.xsp.sdk.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

public class XspProjectUtils {
	private static final Logger log = Logger.getLogger(XspProjectUtils.class.getName());

	public static boolean hasNature(IProject project, String natureId) {
		try {
			IProjectDescription description = project.getDescription();
			String[] ids = description.getNatureIds();
			for (String id : ids) {
				if (id.equals(natureId)) {
					return true;
				}
			}
		} catch (CoreException e) {
			log.log(Level.SEVERE, "Cannot read project natures. " + e.getClass().getSimpleName() + " See tracelog for more details.", e);
		}

		return false;
	}

	public static void addNature(IProject project, String natureId) {
		try {
			if (!hasNature(project, natureId)) {
				IProjectDescription description = project.getDescription();
				String[] ids = description.getNatureIds();
				String[] newIds = new String[ids.length + 1];
				System.arraycopy(ids, 0, newIds, 0, ids.length);
				newIds[ids.length] = natureId;
				description.setNatureIds(newIds);
				project.setDescription(description, null);
			}
		} catch (CoreException e) {
			log.log(Level.SEVERE, "Cannot add Evolution nature to project. " + e.getClass().getSimpleName()
					+ " See tracelog for more details.", e);
		}
	}

	public static void addBuilder(IProject project, String builderId) {
		try {
			IProjectDescription desc = project.getDescription();
			// get the description of the project basically .project file information
			ICommand[] commands = desc.getBuildSpec();

			for (ICommand command : commands) {
				// if (command.getBuilderName().equals(SampleBuilder.BUILDER_ID)) {
				if (command.getBuilderName().equals(builderId)) {
					// Do nothing if builder is already associated with project
					return;
				}
			}

			// TODO need to check if the builder is already on the project but disabled by the user?
			// The info about disabled builders is stored in the .project in buildCommand with name
			// org.eclipse.ui.externaltools.ExternalToolBuilder
			// Each of these builders will have an argument that contains the name of the disabled builder.

			List<ICommand> newCommands = new ArrayList<ICommand>();

			// Add our builder first in the list
			ICommand builderCommand = desc.newCommand();
			builderCommand.setBuilderName(builderId);
			newCommands.add(builderCommand);

			// Add the existing builders
			for (ICommand command : commands) {
				newCommands.add(command);
			}

			// Set the new builders
			desc.setBuildSpec(newCommands.toArray(new ICommand[newCommands.size()]));

			// write to .project file
			project.setDescription(desc, null);
		} catch (CoreException e) {
			log.log(Level.SEVERE, e.getClass().getSimpleName() + " See tracelog for more details.", e);
		}

	}

	public static List<IFile> getFiles(IProject project, String path, final String fileExtension) {
		IFolder folder = project.getFolder(path);
		final List<IFile> files = new ArrayList<IFile>();
		try {
			folder.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FILE) {
						IFile file = (IFile) resource;
						if (file.getFileExtension().equalsIgnoreCase(fileExtension)) {
							files.add(file);
						}
					}
					return true;
				}

			});
		} catch (CoreException e) {
			log.log(Level.SEVERE, e.getClass().getSimpleName() + " See tracelog for more details.", e);
		}

		return files;
	}
}
