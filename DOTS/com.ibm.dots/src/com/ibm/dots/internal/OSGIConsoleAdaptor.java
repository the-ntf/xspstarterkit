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
package com.ibm.dots.internal;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Dictionary;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

import com.ibm.dots.Activator;

/**
 * @author dtaieb
 * Provide OSGI Console access
 */
public abstract class OSGIConsoleAdaptor implements CommandInterpreter{

	private String[] args;
	private int counter;

	private static final CommandProvider commandProvider = new CommandProvider() {

		/**
		 * @param ci
		 * Emulation: Short status command 
		 */
		@SuppressWarnings("unused") // $NON-NLS-1$
		public void _ss( CommandInterpreter ci ){
			Bundle[] bundles = Activator.getDefault().getBundle().getBundleContext().getBundles();
			for ( int i = 0 ; bundles != null && i < bundles.length; i++ ){
				ci.println( bundles[i].getSymbolicName() + " : " + getState( bundles[i] ) ); //$NON-NLS-1$
			}
		}

		/**
		 * @param ci
		 */
		@SuppressWarnings("unused") // $NON-NLS-1$
		public void _stop( CommandInterpreter ci ){
			String pluginId = ci.nextArgument();
			if ( pluginId == null ){
				ci.println( "Argument required: pluginId"); //$NON-NLS-1$
				return;
			}

			Bundle bundle = Platform.getBundle( pluginId );
			if ( bundle == null ){
				ci.println( MessageFormat.format( "Unknown plugin {0}", pluginId )); //$NON-NLS-1$
				return;
			}

			try {
				bundle.stop();
				ci.println("Bundle successfully stopped"); //$NON-NLS-1$
			} catch (BundleException e) {
				ci.printStackTrace( e );
			}
		}

		/**
		 * @param ci
		 * start a plugin
		 */
		@SuppressWarnings("unused") // $NON-NLS-1$
		public void _start( CommandInterpreter ci ){
			String pluginId = ci.nextArgument();
			if ( pluginId == null ){
				ci.println( "Argument required: pluginId"); //$NON-NLS-1$
				return;
			}

			Bundle bundle = Platform.getBundle( pluginId );
			if ( bundle == null ){
				ci.println( MessageFormat.format( "Unknown plugin {0}", pluginId )); //$NON-NLS-1$
				return;
			}

			try {
				bundle.start();
				ci.println("Bundle successfully started"); //$NON-NLS-1$
			} catch (BundleException e) {
				ci.printStackTrace( e );
			}
		}

		/**
		 * @param bundle
		 * @return
		 */
		private String getState(Bundle bundle) {
			switch (bundle.getState()) {
			case Bundle.INSTALLED:
				return "Installed"; //$NON-NLS-1$
			case Bundle.RESOLVED:
				return "Resolved"; //$NON-NLS-1$
			case Bundle.STARTING:
				return "Starting"; //$NON-NLS-1$
			case Bundle.STOPPING:
				return "Stopping";  //$NON-NLS-1$
			case Bundle.UNINSTALLED:
				return "Uninstalled"; //$NON-NLS-1$
			case Bundle.ACTIVE:
				return "Active";  //$NON-NLS-1$
			default:
				return "Unknown";  //$NON-NLS-1$
			}
		}
		
	    /**
	     * @return
	     */
	    @SuppressWarnings("unused")
		public String getHelpCategory(){
	    	return "osgi";	//$NON-NLS-1$
	    }

		/* (non-Javadoc)
		 * @see org.eclipse.osgi.framework.console.CommandProvider#getHelp()
		 */
		public String getHelp() {
			StringBuilder sb = new StringBuilder();
			sb.append( "---OSGi core commands---\n"); // $NON-NLS-1$
			sb.append( "\tss : display installed bundles (short status)\n"); // $NON-NLS-1$
			sb.append( "\tstart - start the specified bundle(s)\n"); // $NON-NLS-1$
			sb.append( "\tstop - stop the specified bundle(s)\n"); // $NON-NLS-1$
			return sb.toString();
		}
	};

	/**
	 * 
	 */
	protected OSGIConsoleAdaptor() {
	}

	/**
	 * @param helpCategory
	 * @return
	 */
	private static Object[] getServices( String helpCategory ) {
		BundleContext context = Activator.getDefault().getBundle().getBundleContext();
		ServiceReference[] serviceRefs = Activator.getCommandProviderTracker().getServiceReferences();
		if (serviceRefs == null){
			return new Object[0];
		}

		//sort the list in descending order
		Arrays.sort( serviceRefs, new Comparator< ServiceReference>() {
			public int compare(ServiceReference o1, ServiceReference o2) {
				int comp = o1.compareTo( o2 );
				if ( comp == 0 ){
					return 0;
				}else if ( comp > 0 ){
					return -1;
				}else{
					return 1;
				}
			}
			@Override
			public boolean equals(Object o) {
				if ( o == this ){
					return true;
				}
				return false;
			}
		});

		ArrayList< Object > services = new ArrayList<Object>();
		for ( ServiceReference serviceRef : serviceRefs ){
			Object service = context.getService(serviceRef );
			if ( helpCategory == null || helpCategory.equalsIgnoreCase( getHelpCategory( service ) ) ){
				services.add ( service );
			}
		}

		if ( helpCategory == null || helpCategory.equalsIgnoreCase( getHelpCategory( commandProvider ) )) {
			services.add( commandProvider );
		}

		return services.toArray();
	}
	
	/**
	 * @param service
	 * @return
	 */
	private static String getHelpCategory(Object service) {
		if ( service == null ){
			return null;
		}
		
		try{
			return (String)service.getClass().getMethod( "getHelpCategory" ).invoke( service );
		}catch( Throwable t ){
			return null;
		}
	}

	/**
	 * @param ci 
	 * 
	 */
	public static void displayHelp(CommandInterpreter ci) {
		Object[] commandProviders = getServices( ci.nextArgument() );
		for ( Object commandProvider : commandProviders ){
			if ( commandProvider instanceof CommandProvider ){
				String help = ((CommandProvider)commandProvider).getHelp();
				ci.println();
				ci.print( help );
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandInterpreter#execute(java.lang.String)
	 */
	public Object execute(String cmd) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandInterpreter#nextArgument()
	 */
	public String nextArgument() {
		if ( args == null || args.length <= counter ){
			return null;
		}
		return args[counter++];
	}


	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandInterpreter#printBundleResource(org.osgi.framework.Bundle, java.lang.String)
	 */
	public void printBundleResource(Bundle bundle, String resource) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandInterpreter#printDictionary(java.util.Dictionary, java.lang.String)
	 */
	@SuppressWarnings("rawtypes") // $NON-NLS-1$
	public void printDictionary(Dictionary dic, String title) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.osgi.framework.console.CommandInterpreter#println(java.lang.Object)
	 */
	public void println(Object o) {
		print( o );
		println();
	}

	/**
	 * @param args
	 * @return
	 */
	public boolean runCommand(String[] args) {
		this.args = args;
		this.counter = 1;
		try{
			Object[] commandProviders = getServices( null );
			for ( Object commandProvider : commandProviders ){
				try {
					Method method = commandProvider.getClass().getMethod("_" + args[0], new Class[] {CommandInterpreter.class});
					method.invoke(commandProvider, new Object[] {this});
					return true;
				} catch (NoSuchMethodException e) {
				} catch (Throwable e) {
					printStackTrace( e );
					return true;
				}
			}
			return false;
		}finally{
			this.args = null;
			this.counter = 0;
		}
	}
}
