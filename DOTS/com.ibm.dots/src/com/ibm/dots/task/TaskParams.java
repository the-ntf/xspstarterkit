/*
 * © Copyright IBM Corp. 2009,2010
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

import java.lang.reflect.Method;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

import com.ibm.dots.Activator;

/**
 * @author dtaieb
 *
 */
class TaskParams extends HashMap<String, String> {

	private static final HashMap<String, IArgumentResolver> argResolvers = new HashMap<String, IArgumentResolver>();
	static{
		initArgResolver();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -980502787598444476L;
	private TaskParams parentParams;

	/**
	 * 
	 */
	public TaskParams() { 
	}

	/**
	 * 
	 */
	private static void initArgResolver() { 
		IExtensionPoint extPt = Platform.getExtensionRegistry().getExtensionPoint( Activator.PLUGIN_ID + ".argResolver" ); // $NON-NLS-1$

		IExtension[] extensions = extPt.getExtensions();
		for ( IExtension extension : extensions ){
			IConfigurationElement[] elements = extension.getConfigurationElements();
			for ( IConfigurationElement element : elements ){
				if ( "argResolver".equals( element.getName() )){ // $NON-NLS-1$
					try{
						String id = element.getAttribute( "id" ); // $NON-NLS-1$
						IArgumentResolver resolver = (IArgumentResolver)element.createExecutableExtension( "class" ); // $NON-NLS-1$
						argResolvers.put( id, resolver );
					}catch( Throwable t ){
						t.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * @param initialCapacity
	 */
	public TaskParams(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * @param m
	 */
	public TaskParams(Map<? extends String, ? extends String> m) {
		super(m);
	}

	/**
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public TaskParams(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	@Override
	public String put(String key, String value) {
		return super.put(key, resolveValue( value ));
	}

	/**
	 * @param value
	 * @return
	 */
	private String resolveValue(String value) {
		if ( value == null ){
			return "";
		}

		StringBuilder sb = new StringBuilder( value.length() );
		StringCharacterIterator charIt = new StringCharacterIterator( value );
		char c = charIt.first();
		StringBuilder var = null;
		while ( c != CharacterIterator.DONE ){
			if ( c == '{' ){
				if  ( var != null ){
					//Invalid variable
					return value;
				}
				var = new StringBuilder();
			}else if ( c == '}' ){
				if ( var != null ){
					sb.append( resolveVar( var.toString() ) );
					var = null;
				}else{
					sb.append( c );
				}
			}else{
				if ( var != null ){
					var.append( c );
				}else{
					sb.append( c );
				}
			}
			c = charIt.next();
		}
		return sb.toString();
	}

	/**
	 * @param args
	 * @return
	 */
	public String _GENID( String arg ){
		return UUID.randomUUID().toString();
	}

	/**
	 * @param var
	 * @return
	 */
	private String resolveVar(String var) {
		if ( var.startsWith( "#" ) ){   //Reference to an existing variable
			var = var.substring( 1 );
			if ( containsKey( var ) ){
				var = get( var );
			}else if ( parentParams != null && parentParams.containsKey( var )){
				var = parentParams.get( var );
			}
			return var;
		}

		int paramIndex = var.indexOf( ":" );
		String methodName = paramIndex > 0 ? var.substring( 0, paramIndex ) : var;
		//Do we have a method with that name
		try {
			Method m = null;
			for ( java.util.Map.Entry<String, IArgumentResolver> entry : argResolvers.entrySet() ){
				try{
					m = entry.getValue().getClass().getMethod( "_" + methodName, String.class );
					String arg = null;
					if ( paramIndex > 0 ){
						arg = var.substring( paramIndex + 1 );
					}
					return (String)m.invoke( entry.getValue(), arg );
				}catch( Throwable t ){
					//Ignore
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return var;
	}

	/**
	 * @param params
	 */
	public void setParentParams(TaskParams parentParams ) {
		this.parentParams = parentParams;
	}

}
