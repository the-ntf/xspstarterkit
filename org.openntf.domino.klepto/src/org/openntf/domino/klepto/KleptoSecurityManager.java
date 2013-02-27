/**
 * 
 */
package org.openntf.domino.klepto;

import com.ibm.domino.xsp.module.nsf.platform.DominoPlatform;

import lotus.notes.AgentSecurityContext;
import lotus.notes.AgentSecurityManager;

/**
 * @author nfreeman
 * 
 */
public class KleptoSecurityManager extends SecurityManager {

	/**
	 * 
	 */
	public KleptoSecurityManager() {
	}

	public static void force() {
		SecurityManager sm = System.getSecurityManager();
		AgentSecurityManager asm = (AgentSecurityManager) sm;
		LocalPlatform lp = new LocalPlatform();
		// asm.setAgentSecurityManagerExtender(lp);

		// com.ibm.domino.xsp.module.nsf.platform.Factory.force(asm, lp);

		// Class[] arrayOfClass = sm.getClassContext();
		// for (int i = 0; (i < arrayOfClass.length) &&
		// (!(arrayOfClass[i].getName().equals("com.ibm.domino.xsp.module.nsf.platform.KleptoSecurityManager"))); ++i)
		// ;
		// if ((i < arrayOfClass.length - 2)
		// && (arrayOfClass[(i + 1)].getName().equals("com.ibm.domino.xsp.module.nsf.platform.DominoPlatform"))
		// && (arrayOfClass[(i + 2)].getName().equals("com.ibm.domino.xsp.module.nsf.platform.Factory"))) {
		// this.asmExtender = paramAgentSecurityManagerExtender;
		// } else {
		// this.t.TRACE_MSG("SecurityException");
		// throw new SecurityException(JavaString.getString("security_exists"));
		// }

	}

	static class LocalPlatform extends DominoPlatform {

		public LocalPlatform() {
			super();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.domino.xsp.module.nsf.platform.AbstractNotesDominoPlatform#getSecurityContext()
		 */
		@Override
		public AgentSecurityContext getSecurityContext() {
			return null;
		}
	}

}
