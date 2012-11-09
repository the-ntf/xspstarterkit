/**
 * 
 */
package org.openntf.xsp.starter.ssjs;

import org.openntf.xsp.starter.Activator;

import com.ibm.commons.util.StringUtil;
import com.ibm.jscript.InterpretException;
import com.ibm.jscript.JSContext;
import com.ibm.jscript.JavaScriptException;
import com.ibm.jscript.engine.IExecutionContext;
import com.ibm.jscript.types.BuiltinFunction;
import com.ibm.jscript.types.FBSDefaultObject;
import com.ibm.jscript.types.FBSGlobalObject;
import com.ibm.jscript.types.FBSObject;
import com.ibm.jscript.types.FBSString;
import com.ibm.jscript.types.FBSUtility;
import com.ibm.jscript.types.FBSValue;
import com.ibm.jscript.types.FBSValueVector;

/**
 * @author nfreeman
 * 
 */
public class StarterJavaScriptFunctionsEx extends FBSDefaultObject {
	private static final int FCT_GET_EXTENDED_CONTEXT = 1;
	private static final int FCT_CHARLIE = 2;
	private final static boolean _debug = Activator._debug;
	static {
		if (_debug)
			System.out.println(StarterJavaScriptFunctionsEx.class.getName() + " loaded");
	}

	public StarterJavaScriptFunctionsEx(JSContext jsContext) {
		super(jsContext, null, false);
		addFunction(FCT_GET_EXTENDED_CONTEXT, "@GetExecutionContext", "():Lcom.ibm.jscript.engine.IExecutionContext");
		addFunction(FCT_CHARLIE, "@CharlieSheen", "():T");
	}

	private void addFunction(int index, String functionName, String... params) {
		createMethod(functionName, FBSObject.P_NODELETE | FBSObject.P_READONLY, new NotesFunction(getJSContext(), index, functionName,
				params));
	}

	@Override
	public boolean hasInstance(FBSValue v) {
		return v instanceof FBSGlobalObject;
	}

	@Override
	public boolean isJavaNative() {
		return false;
	}

	// =================================================================================
	// Functions implementation
	// For optimization reasons, there is one NotesFunction instance per
	// function,
	// instead of one class (this avoids loading to many classes). To then
	// distinguish
	// the actual function, it uses an index member.
	// =================================================================================
	public static class NotesFunction extends BuiltinFunction {
		private String functionName;
		private int index;
		private String[] params;

		NotesFunction(JSContext jsContext, int index, String functionName, String[] params) {
			super(jsContext);
			this.functionName = functionName;
			this.index = index;
			this.params = params;
		}

		/**
		 * Index of the function.
		 * <p>
		 * There must be one instanceof this class per index.
		 * </p>
		 */
		public int getIndex() {
			return this.index;
		}

		/**
		 * Return the list of the function parameters.
		 * <p>
		 * Note that this list is not used at runtime, at least for now, but consumed by Designer code completion.<br>
		 * A function can expose multiple parameter sets.
		 * </p>
		 */
		@Override
		protected String[] getCallParameters() {
			return this.params;
		}

		/**
		 * Function name, as exposed by Designer and use at runtime.
		 * <p>
		 * This function is exposed in the JavaScript global namespace, so you should be careful to avoid any name conflict.
		 * </p>
		 */
		@Override
		public String getFunctionName() {
			return this.functionName;
		}

		/**
		 * Actual code execution.
		 * <p>
		 * The JS runtime calls this method when the method is executed within a JavaScript formula.
		 * </p>
		 * 
		 * @param context
		 *            the JavaScript execution context (global variables, function...)
		 * @param args
		 *            the arguments passed to the function
		 * @params _this the "this" object when the method is called as a "this" member
		 */
		@Override
		public FBSValue call(IExecutionContext context, FBSValueVector args, FBSObject _this) throws JavaScriptException {
			try {
				// Else execute the formulas
				switch (index) {
				case FCT_GET_EXTENDED_CONTEXT: {
					return FBSUtility.wrapAsObject(getJSContext(), context);
				}

				case FCT_CHARLIE: {
					return fct_charlie(context, args);
				}

				default: {
					throw new InterpretException(null, StringUtil.format("Internal error: unknown function \'{0}\'", functionName));
				}
				}
			} catch (Exception e) {
				throw new InterpretException(e, StringUtil.format("Error while executing function \'{0}\'", functionName));
			}
		}

		private FBSValue fct_charlie(IExecutionContext context, FBSValueVector args) {
			FBSString result = (FBSString) FBSUtility.wrap("DUH, WINNING!");
			return result;
		}

	}
}
