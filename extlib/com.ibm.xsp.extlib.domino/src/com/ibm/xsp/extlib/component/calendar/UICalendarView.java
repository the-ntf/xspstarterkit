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

import java.io.IOException;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.component.domino.UIViewComponent;

/**
 * @author akosugi
 * 
 *        ui component handler for calendar view control
 */
public class UICalendarView extends UIViewComponent {
    
    public static final String COMPONENT_TYPE = "com.ibm.xsp.extlib.calendar.CalendarView"; // $NON-NLS-1$
    public static final String COMPONENT_FAMILY = "com.ibm.xsp.extlib.calendar.CalendarView"; // $NON-NLS-1$
    public static final String RENDERER_TYPE = "com.ibm.xsp.extlib.calendar.CalendarView"; //$NON-NLS-1$

    // TODO how to put title for comboParameterEditor values?
    private String type = null;
    private Boolean summarize = null;
    private Date date = null;

    //event handler properties//
    private String onRescheduleEntry;
    private String onChangeView;
    
    public UICalendarView() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getType() {
        if (type != null)
            return type;
        ValueBinding _vb = getValueBinding("type"); // $NON-NLS-1$
        if (_vb != null)
            return (String) _vb.getValue(getFacesContext());
        else
            return null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSummarize() {
        if (summarize != null)
            return summarize;
        ValueBinding vb = getValueBinding("summarize"); // $NON-NLS-1$
        if (vb != null){
            Object value = vb.getValue(getFacesContext());
            if( value instanceof Boolean ){
                return (Boolean) value;
            }
        }
        return Boolean.FALSE;
    }

    public void setSummarize(boolean summarize) {
        this.summarize = summarize;
    }

    public Date getDate() {
        if (date != null)
            return date;
        ValueBinding _vb = getValueBinding("date"); // $NON-NLS-1$
        if (_vb != null)
            return (Date) _vb.getValue(getFacesContext());
        else
            return null;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public String getOnRescheduleEntry() {
        if (onRescheduleEntry != null)
            return onRescheduleEntry;
        ValueBinding _vb = getValueBinding("onRescheduleEntry"); // $NON-NLS-1$
        if (_vb != null)
            return (String) _vb.getValue(getFacesContext());
        else
            return null;
    }

    public void setOnRescheduleEntry(String rescheduleEntryAction) {
        this.onRescheduleEntry = rescheduleEntryAction;
    }

    public String getOnChangeView() {
        if (onChangeView != null)
            return onChangeView;
        ValueBinding _vb = getValueBinding("onChangeView"); // $NON-NLS-1$
        if (_vb != null)
            return (String) _vb.getValue(getFacesContext());
        else
            return null;
    }

    public void setOnChangeView(String changeViewAction) {
        this.onChangeView = changeViewAction;
    }

    @Override
    protected void writeHandleContextMenuScript(ResponseWriter writer)
            throws IOException {
        String value = this.getOnContextMenu();
        if(StringUtil.isNotEmpty(value)){
            this.writeActionHandlerScript(writer, "handleContextMenu", "ev,oCalendar,items", value); // $NON-NLS-1$ $NON-NLS-2$
        }   
    }

    @Override
    public void writeActionHandlerScripts(ResponseWriter writer) throws IOException {
        super.writeActionHandlerScripts(writer);
        String value = this.getOnRescheduleEntry();
        if(StringUtil.isNotEmpty(value)){
            this.writeActionHandlerScript(writer, "rescheduleEntryAction", "item,calendar", value); // $NON-NLS-1$ $NON-NLS-2$
        }
        value = this.getOnChangeView();
        if(StringUtil.isNotEmpty(value)){
            this.writeActionHandlerScript(writer, "changeViewAction", "type", value); // $NON-NLS-1$ $NON-NLS-2$
        }
    }
    @Override
    public Object saveState(FacesContext context) {
        Object values[] = new Object[6];
        values[0] = super.saveState(context);
        values[1] = type;
        values[2] = summarize;
        values[3] = date;
        values[4] = onRescheduleEntry;
        values[5] = onChangeView;
        return values;
    }
    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        this.type = (String) values[1];
        this.summarize = (Boolean) values[2];
        this.date = (Date) values[3];
        this.onRescheduleEntry = (String) values[4];
        this.onChangeView = (String) values[5];
    }

}
