/**
 * 
 */
package org.openntf.xsp.responsive.frameworks.sencha;

import java.util.List;

/**
 * @author nfreeman
 * 
 */
public interface ISenchaComponent {
	public abstract String getSenchaType();

	public abstract List<SenchaAttribute> getSenchaAttributes();
}
