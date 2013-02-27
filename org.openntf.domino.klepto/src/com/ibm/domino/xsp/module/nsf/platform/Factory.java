/**
 * 
 */
package com.ibm.domino.xsp.module.nsf.platform;

import lotus.notes.AgentSecurityManager;

/**
 * @author nfreeman
 * 
 */
class Factory {

	/**
	 * 
	 */
	public Factory() {
		// TODO Auto-generated constructor stub
	}

	public static void forcePlatform(AgentSecurityManager asm, AbstractNotesDominoPlatform platform) {
		asm.setAgentSecurityManagerExtender(platform);
	}

}
