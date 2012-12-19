/*
 * © Copyright IBM Corp. 2009,2011
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.NotesThread;
import lotus.domino.Session;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.framework.console.CommandInterpreter;

import com.ibm.dots.Activator;
import com.ibm.dots.internal.ServerTaskProgressMonitor;
import com.ibm.dots.task.ServerTaskManager.ITaskService;
import com.ibm.dots.utils.NotesUtils;

/**
 * @author dtaieb
 *
 */
class JobTaskService implements ITaskService, IJobChangeListener {

	private static final int MAX_RUNNING_TASKLET = 20;

	private String serviceName;
	private boolean bJobListenerInitialized;
	private volatile Set<ServiceTaskJob> runningServiceTaskJobs = Collections.synchronizedSet( new HashSet<ServiceTaskJob>() );
	private static Integer idCounter = new Integer( 1 );


	private static class ServiceTaskJob extends Job{

		private ServerTaskInfo serverTaskInfo;
		private RunWhen runWhen;
		private String[] args;
		private volatile IProgressMonitor currentProgressMonitor;
		private Object lock = new Object();
		private int jobId;

		public ServiceTaskJob( ServerTaskInfo serverTaskInfo, RunWhen runWhen, String[] args ) {
			super( getDisplayName( serverTaskInfo, runWhen ) );

			this.serverTaskInfo = serverTaskInfo;
			this.runWhen = runWhen;
			this.args = args;

			synchronized( idCounter ){
				if ( idCounter >= 1000 ){
					//reset
					idCounter = 1;
				}
				jobId = idCounter++;
			}
		}

		@Override
		protected IStatus run(IProgressMonitor monitor ) {
			try{
				synchronized( lock ){
					currentProgressMonitor = monitor;
				}

				//Initialize the thread
				NotesThread.sinitThread();
				Session session = null;

				try {
					//Create a session for the duration of this thread
					session = NotesFactory.createTrustedSession();

					if ( monitor instanceof ServerTaskProgressMonitor ) {
						((ServerTaskProgressMonitor)monitor).setJobId(jobId);
					}

					doRun( monitor, session );
				} catch (Throwable e) {
					e.printStackTrace();
					return new Status( Status.ERROR, Activator.PLUGIN_ID, "An unexpected error occured while running a tasklet", e );
				} finally {
					NotesUtils.recycle( session );
					NotesThread.stermThread();
				}

				return Status.OK_STATUS;
			}finally{
				synchronized( lock ) {
					currentProgressMonitor = null;
				}
			}
		}

		/**
		 * @param monitor
		 * @param session
		 */
		private void doRun(IProgressMonitor monitor, Session session) {
			IServerTaskRunnable taskRunnable = null;
			try{
				taskRunnable = serverTaskInfo.getServerTaskRunnable( true );
				if ( taskRunnable instanceof AbstractServerTask ){
					((AbstractServerTask)taskRunnable).setSession( session );
				}
				if ( runWhen.annotatedMethod != null ){
					if ( Arrays.equals( runWhen.annotatedMethod.getParameterTypes(), new Class<?>[]{IProgressMonitor.class} )){
						runWhen.annotatedMethod.invoke( taskRunnable, monitor );
					}else if ( Arrays.equals( runWhen.annotatedMethod.getParameterTypes(), new Class<?>[]{String[].class, IProgressMonitor.class} )){
						runWhen.annotatedMethod.invoke( taskRunnable, args, monitor );
					}else{
						throw new IllegalStateException("Cannot run annotated method"); // $NON-NLS-1$
					}
				}else{
					taskRunnable.run( runWhen, args, monitor );
				}
			} catch (Throwable e) {
				taskRunnable.logException( e );
			}finally{
				if ( taskRunnable != null ){
					//dispose the task
					try {
						taskRunnable.dispose();
						taskRunnable = null;
					} catch (NotesException e) {
						taskRunnable.logException( e );
					}

					if ( taskRunnable instanceof AbstractServerTask ){
						((AbstractServerTask)taskRunnable).setSession( null );
					}
				}
			}           
		}

		/**
		 * @param prefix
		 * @param ci
		 */
		public void displayStatus( String prefix, CommandInterpreter ci ) {
			ci.print( getStatusMessage( prefix ) );
		}

		/**
		 * @param prefix
		 * @return
		 */
		private String getStatusMessage(String prefix) {
			synchronized( lock ){
				if ( currentProgressMonitor instanceof ServerTaskProgressMonitor ){
					return 
					MessageFormat.format( 
							"{0}({1}) {2}",
							prefix,
							currentProgressMonitor.isCanceled() ? "canceled" : jobId, 
									((ServerTaskProgressMonitor)currentProgressMonitor).getStatus() ) ;
				}
			}

			return "";
		}       
	}

	/**
	 * @param serviceName 
	 * 
	 */
	public JobTaskService(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return serviceName;
	}

	/**
	 * @return
	 */
	private static String getDisplayName(ServerTaskInfo serverTaskInfo, RunWhen runWhen) {
		String retValue = serverTaskInfo.getId();
		if ( runWhen != null && runWhen.annotatedMethod != null ){
			retValue += "@" + runWhen.annotatedMethod.getName();
		}
		return retValue;
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.ServerTaskManager.ITaskService#getServiceName()
	 */
	public String getServiceName() {
		return serviceName;
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.ServerTaskManager.ITaskService#dispose()
	 */
	public void dispose() {

		//Wait for all the jobs to finish
		if ( !runningServiceTaskJobs.isEmpty() ){
			synchronized( runningServiceTaskJobs ){
				for ( ServiceTaskJob job : runningServiceTaskJobs ){
					ServerTaskManager.getInstance().logMessageText( 
							MessageFormat.format("Cancelling task {0}", job.getStatusMessage("") )
					);
					job.cancel();
				}
			}
			ServerTaskManager.getInstance().logMessageText( "Waiting for manual tasks to complete..." );
			long curTime = new Date().getTime();
			int numMessages = 0;
			while( !runningServiceTaskJobs.isEmpty() ){
				try {
					Thread.sleep( 1000L );
				} catch (InterruptedException e) {
				}

				long elapsedTime = new Date().getTime() - curTime;
				if ( elapsedTime > 2 * 60 * 1000L ){
					ServerTaskManager.getInstance().logMessageText( 
							MessageFormat.format( "{0} Task seem to not respond anymore (stack trace to follow).", runningServiceTaskJobs.size() )
					);
					if ( ServerTaskManager.getInstance() != null ){
						ServerTaskManager.getInstance().dumpStack( this );
					}
					curTime = new Date().getTime(); //Reset the time
				}else{
					if ( ++numMessages == 30 ){
						ServerTaskManager.getInstance().logMessageText( 
								MessageFormat.format( "Still Waiting for {0} manual tasks to complete", runningServiceTaskJobs.size() )
						);
						numMessages = 0;
					}
				}
			}
		}
		if ( bJobListenerInitialized ){
			Job.getJobManager().removeJobChangeListener( this );
			bJobListenerInitialized = false;
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.ServerTaskManager.ITaskService#postTask(com.ibm.dots.task.RunWhen, com.ibm.dots.task.ServerTaskInfo, java.lang.String[])
	 */
	public void postTask(RunWhen runWhen, ServerTaskInfo serverTaskInfo,String[] args) {
		if ( !bJobListenerInitialized ){
			//Set the Job Change Listener
			Job.getJobManager().addJobChangeListener( this );
			bJobListenerInitialized = true;
		}

		if ( runningServiceTaskJobs.size() >= MAX_RUNNING_TASKLET ){
			ServerTaskManager.getInstance().logMessageText( "Maximum running tasklets reached.");
			return;
		}
		new ServiceTaskJob( serverTaskInfo, runWhen, args ).schedule();
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.ServerTaskManager.ITaskService#displayStatus(java.lang.String, org.eclipse.osgi.framework.console.CommandInterpreter)
	 */
	public void displayStatus(String prefix, CommandInterpreter ci) {
		if ( runningServiceTaskJobs.isEmpty() ){
			ci.print( prefix + "No task running" );
		}else{
			for ( ServiceTaskJob job : runningServiceTaskJobs ){
				job.displayStatus(prefix, ci);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.ServerTaskManager.ITaskService#cancelTask(java.lang.String)
	 */
	public int cancelTask(String taskId) {
		int jobId = Integer.valueOf( taskId );
		synchronized (runningServiceTaskJobs ){
			for ( ServiceTaskJob job : runningServiceTaskJobs ){
				if ( job.jobId == jobId ){
					job.cancel();
					return 1;
				}
			}
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ibm.dots.task.ServerTaskManager.ITaskService#dumpStack(com.ibm.dots.task.ServerConsole)
	 */
	public void dumpStack(ServerConsole serverConsole) {
		synchronized( runningServiceTaskJobs){
			for ( ServiceTaskJob job : runningServiceTaskJobs ){
				Thread t = job.getThread();
				String threadName = t.getName();
				serverConsole.printAndLog( "StackTrace for thread " + threadName );
				StackTraceElement[] traces = t.getStackTrace();
				for ( StackTraceElement trace : traces ){
					serverConsole.printAndLog( "\t" + trace.toString() ); // $NON-NLS-1$
				}
				serverConsole.printAndLog("\n\n"); // $NON-NLS-1$
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#aboutToRun(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void aboutToRun(IJobChangeEvent jobEvent) {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#awake(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void awake(IJobChangeEvent jobEvent ) {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#done(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void done(IJobChangeEvent jobEvent ) {
		if ( jobEvent.getJob() instanceof ServiceTaskJob ){
			ServiceTaskJob stj = (ServiceTaskJob) jobEvent.getJob();
			synchronized( runningServiceTaskJobs ){
				runningServiceTaskJobs.remove( stj );           
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#running(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void running(IJobChangeEvent jobEvent) {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#scheduled(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void scheduled(IJobChangeEvent jobEvent ) {
		if ( jobEvent.getJob() instanceof ServiceTaskJob ){
			ServiceTaskJob stj = (ServiceTaskJob) jobEvent.getJob();
			synchronized( runningServiceTaskJobs ){
				runningServiceTaskJobs.add( stj );
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IJobChangeListener#sleeping(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void sleeping(IJobChangeEvent jobEvent ) {

	}

}
