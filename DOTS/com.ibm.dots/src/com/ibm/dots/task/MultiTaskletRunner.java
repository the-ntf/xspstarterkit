/*
 * © Copyright IBM Corp. 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.dots.task;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import lotus.domino.NotesException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author dtaieb
 * Class used to declaratively run multiple tasklets as one tasklet
 */
public class MultiTaskletRunner extends AbstractServerTask implements IExecutableExtension{

	private class TaskRef{
		String taskId;
		int runCount = 1;
		TaskParams params;
		private IConfigurationElement taskRefElement;
		public TaskRef(String taskId, IConfigurationElement taskRefElement) {
			this.taskId = taskId;
			this.taskRefElement = taskRefElement;
			if ( taskRefElement != null ){
				String sRunCount = taskRefElement.getAttribute( "runCount" ); // $NON-NLS-1$
				if ( sRunCount !=  null ){
					runCount = Integer.valueOf( sRunCount );
				}
			}
			resetParams();
		}
		/**
		 * 
		 */
		public void resetParams() {
			params = null;
			if ( taskRefElement != null ){
				IConfigurationElement[] children = taskRefElement.getChildren();
				for ( IConfigurationElement child : children ){
					if ( "param".equals( child.getName() )){ // $NON-NLS-1$
						String name = child.getAttribute( "name" ); // $NON-NLS-1$
						String value = child.getAttribute( "value" ); // $NON-NLS-1$
						if ( name != null && value != null ){
							if ( params == null ){
								params = new TaskParams();
								params.setParentParams( MultiTaskletRunner.this.params );
							}
							params.put( name, value );
						}
					}
				}
			}

		}
	}

	private ArrayList<TaskRef> taskRefList = new ArrayList<TaskRef>();
	private TaskParams params;

	/**
	 * 
	 */
	public MultiTaskletRunner() {
	}

	/**
	 * @param configurationElement
	 * constructor used by the multiTask extension point
	 */
	public MultiTaskletRunner(IConfigurationElement configurationElement) {
		IConfigurationElement[] taskRefs = configurationElement.getChildren();
		for ( IConfigurationElement taskRef : taskRefs ){
			if ( "taskRef".equals( taskRef.getName() )){ // $NON-NLS-1$
				String taskId = taskRef.getAttribute( "id" ); // $NON-NLS-1$
				if ( taskId != null ){
					taskRefList.add( new TaskRef( taskId, taskRef ) );
				}
			}else if ( "param".equals( taskRef.getName() )){ // $NON-NLS-1$
				String name = taskRef.getAttribute( "name" ); // $NON-NLS-1$
				String value = taskRef.getAttribute( "value" ); // $NON-NLS-1$
				if ( name != null && value != null ){
					if ( params == null ){
						params = new TaskParams();
					}
					params.put( name, value );
				}
			}
		}
	}

	/**
	 * @param params1
	 * @param params2
	 * @return
	 */
	private String[] mergeParams(Map<String, String> params1, String[] params2) {
		if ( params1 == null ){
			return params2;
		}
		int newSize = params1.size() + ( params2 == null ? 0 : params2.length );
		HashSet<String> newArgs = new HashSet<String>( newSize );
		for ( Entry<String,String> entry : params1.entrySet() ){
			String key = entry.getKey();
			String value = entry.getValue();
			newArgs.add( key + ( value == null ? "" : ( "=" + value ) ) );
		}

		if ( params2 != null ){
			for ( String arg : params2 ){
				newArgs.add( arg );
			}
		}
		return newArgs.toArray( new String[0] );
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.IServerTaskRunnable#dispose()
	 */
	public void dispose() throws NotesException {
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.IServerTaskRunnable#run(com.ibm.dots.task.RunWhen, java.lang.String[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(RunWhen runWhen, String[] args, IProgressMonitor monitor)throws NotesException {
		String[][] pargs = new String[][] {args};
		try{
			for ( TaskRef taskRef : taskRefList ){
				for ( int i = 0; i < taskRef.runCount; i++ ){
					if ( monitor.isCanceled() ){
						return;
					}
					taskRef.resetParams();
					ServerTaskInfo taskInfo = ServerTaskManager.getInstance().getServerTaskInfo( null, taskRef.taskId, pargs);
					if  ( taskInfo == null ){
						logMessage( MessageFormat.format( "Unable to get Server Task info for {0}", taskRef.taskId )); // $NON-NLS-1$
						return;
					}

					IServerTaskRunnable taskRunnable = taskInfo.getServerTaskRunnable();
					if ( taskRunnable == null ){
						return;
					}
					if ( taskRunnable instanceof AbstractServerTask ){
						((AbstractServerTask)taskRunnable).setSession( getSession() );
					}

					if ( params != null ){
						pargs[0] = mergeParams( this.params, pargs[0] );
					}
					taskRunnable.run( runWhen, mergeParams( taskRef.params, pargs[0] ), monitor);
				}
			}
		}catch( Throwable t ){
			logException( t );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		if ( data instanceof Hashtable<?, ?>){
			Hashtable<?, ?> args = (Hashtable<?, ?>)data;
			for ( Object key : args.keySet() ){
				if ( key instanceof String ){
					taskRefList.add( new TaskRef( (String)key, config) );
				}
			}
		}       
	}

}
