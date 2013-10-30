/* ***************************************************************** */
/* Licensed Materials - Property of IBM                              */
/*                                                                   */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */
/*                                                                   */
/* US Government Users Restricted Rights - Use, duplication or       */
/* disclosure restricted by GSA ADP Schedule Contract with           */
/* IBM Corp.                                                         */
/*                                                                   */
/* ***************************************************************** */






package com.ibm.xsp.extlib.component.calendar;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.ibm.xsp.extlib.component.domino.UIURLDataStoreComponent;

/**
 * @author akosugi
 * 
 *        ui component handler for iCalendar read store control
 */
public class UIiCalReadStore extends UIURLDataStoreComponent {
    
    public static final String COMPONENT_TYPE = "com.ibm.xsp.extlib.calendar.iCalReadStore"; // $NON-NLS-1$
    public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.calendar.CalendarStore"; // $NON-NLS-1$
    public static final String RENDERER_TYPE = "com.ibm.xsp.extlib.calendar.iCalReadStore"; //$NON-NLS-1$

    // TODO it would be best if these were styles/styleClasses, instead of explicit colors
    //_ak//this is not to define the outline style of the widget,
    // but is somewhat a color rule definition for each part of event cell regarding to the type of the events.
    // and actually, there are more properties(e.g. fontColorAppointment) which are not exposed here..
    private String fontColorMeeting;
    private String bgColorMeeting;
    private String borderColorMeeting;

    public UIiCalReadStore() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getFontColorMeeting() {
        if (fontColorMeeting != null)
            return fontColorMeeting;
        ValueBinding _vb = getValueBinding("fontColorMeeting"); // $NON-NLS-1$
        if (_vb != null)
            return (String) _vb.getValue(getFacesContext());
        else
            return null;
    }

    public void setFontColorMeeting(String fontColorMeeting) {
        this.fontColorMeeting = fontColorMeeting;
    }

    public String getBgColorMeeting() {
        if (bgColorMeeting != null)
            return bgColorMeeting;
        ValueBinding _vb = getValueBinding("bgColorMeeting"); // $NON-NLS-1$
        if (_vb != null)
            return (String) _vb.getValue(getFacesContext());
        else
            return null;
    }

    public void setBgColorMeeting(String bgColorMeeting) {
        this.bgColorMeeting = bgColorMeeting;
    }

    public String getBorderColorMeeting() {
        if (borderColorMeeting != null)
            return borderColorMeeting;
        ValueBinding _vb = getValueBinding("borderColorMeeting"); // $NON-NLS-1$
        if (_vb != null)
            return (String) _vb.getValue(getFacesContext());
        else
            return null;
    }

    public void setBorderColorMeeting(String borderColorMeeting) {
        this.borderColorMeeting = borderColorMeeting;
    }
    @Override
    public Object saveState(FacesContext context) {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = fontColorMeeting;
        values[2] = bgColorMeeting;
        values[3] = borderColorMeeting;
        return values;
    }
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        this.fontColorMeeting = (String) values[1];
        this.bgColorMeeting = (String) values[2];
        this.borderColorMeeting = (String) values[3];
    }

}
