/**
 * 
 */
package org.openntf.xsp.responsive.components;

import java.util.List;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import com.ibm.xsp.page.FacesComponentBuilder;
import com.ibm.xsp.util.FacesUtil;
import com.ibm.xsp.util.StateHolderUtil;

/**
 * @author nfreeman Yes, it's final. Extending this class is missing the point, since it would require another tag
 * 
 */
public final class RInput extends UIInput implements RComponent {
	private static final Logger log_ = Logger.getLogger(RInput.class.getName());
	private static final long serialVersionUID = 1L;
	private static final int stateSize = 5;	//should be number of local fields + 1 for super

	private String label;
	private String inputType;
	private List<Object> uiChoices;
	private List<Object> backendChoices;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.component.FacesComponent#initBeforeContents(javax.faces.context.FacesContext)
	 */
	@Override
	public void initBeforeContents(FacesContext paramFacesContext) throws FacesException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.component.FacesComponent#buildContents(javax.faces.context.FacesContext, com.ibm.xsp.page.FacesComponentBuilder)
	 */
	@Override
	public void buildContents(FacesContext ctx, FacesComponentBuilder builder) throws FacesException {
		builder.buildAll(ctx, this, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.component.FacesComponent#initAfterContents(javax.faces.context.FacesContext)
	 */
	@Override
	public void initAfterContents(FacesContext paramFacesContext) throws FacesException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.stylekit.ThemeControl#getStyleKitFamily()
	 */
	@Override
	public String getStyleKitFamily() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.faces.component.UIInput#saveState(javax.faces.context.FacesContext)
	 */
	@Override
	public Object saveState(FacesContext context) {
		 Object[] result = new Object[stateSize];
		 int i=0;
		 result[i++] = super.saveState(context);
		 result[i++] = this.label;
		 result[i++] = this.inputType;
		 result[i++] = StateHolderUtil.saveList(context, this.uiChoices);
		 result[i++] = StateHolderUtil.saveList(context, this.backendChoices);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see javax.faces.component.UIInput#restoreState(javax.faces.context.FacesContext, java.lang.Object)
	 */
	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[])state;
		int i=0;
	  super.restoreState(context, values[i++]);
	  this.label = (String)values[i++];
	  this.inputType = (String)values[i++];
	  this.uiChoices = StateHolderUtil.restoreList(context, this, values[i++]);
	  this.backendChoices = StateHolderUtil.restoreList(context, this, values[i++]);
	}
	
}
