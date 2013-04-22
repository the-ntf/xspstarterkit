/**
 * 
 */
package org.openntf.xsp.responsive.frameworks.bootstrap;

import java.util.List;

/**
 * @author nfreeman
 * 
 */
public interface IBootstrapComponent {
	public abstract String getBootstrapType();

	public abstract List<BootstrapAttribute> getBootstrapAttributes();
}
