/**
 * 
 */
package org.openntf.xsp.starter.ssjs;

import com.ibm.designer.runtime.extensions.JavaScriptProvider;
import com.ibm.jscript.JSContext;

/**
 * @author nfreeman
 * 
 */
public class StarterJavaScriptProvider implements JavaScriptProvider {
	public void registerWrappers(JSContext jsContext) {
		jsContext.getRegistry().registerGlobalPrototype("OpenNTF Starter Kit Functions", new StarterJavaScriptFunctionsEx(jsContext));
	}
}
