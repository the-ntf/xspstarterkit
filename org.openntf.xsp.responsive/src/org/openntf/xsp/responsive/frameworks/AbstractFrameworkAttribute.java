/**
 * 
 */
package org.openntf.xsp.responsive.frameworks;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.complex.ValueBindingObjectImpl;

/**
 * @author nfreeman
 * 
 */
public class AbstractFrameworkAttribute extends ValueBindingObjectImpl implements Serializable {
	private static final Logger log_ = Logger.getLogger(AbstractFrameworkAttribute.class.getName());
	private static final long serialVersionUID = 1L;

	private String name;
	private String value;

	public String getName() {
		if (this.name != null) {
			return this.name;
		}
		ValueBinding localValueBinding = getValueBinding("name");
		if (localValueBinding != null) {
			return ((String) localValueBinding.getValue(FacesContext.getCurrentInstance()));
		}
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		if (this.value != null) {
			return this.value;
		}
		ValueBinding localValueBinding = getValueBinding("value");
		if (localValueBinding != null) {
			return ((String) localValueBinding.getValue(FacesContext.getCurrentInstance()));
		}
		return null;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
