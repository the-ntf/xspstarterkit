/**
 * 
 */
package org.openntf.xsp.responsive.frameworks.angularjs;

import java.util.List;

/**
 * @author nfreeman
 * 
 */
public interface IAngularComponent {
	public abstract String getAngularType();

	public abstract List<AngularAttribute> getAngularAttributes();
}
