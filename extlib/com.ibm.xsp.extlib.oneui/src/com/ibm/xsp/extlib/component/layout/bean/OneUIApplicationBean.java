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






package com.ibm.xsp.extlib.component.layout.bean;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.faces.context.FacesContext;

import com.ibm.commons.Platform;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.xsp.extlib.component.layout.OneUIApplicationConfiguration;
import com.ibm.xsp.extlib.component.layout.bean.config.OneUIJSONFactory;


/**
 * OneUI Layout Bean
 */
public class OneUIApplicationBean implements ApplicationBean {

    public static final String DEFAULT_CONFIGURATION_FILE = "/WEB-INF/OneUIApplication.json"; // $NON-NLS-1$
    
    private static final long serialVersionUID = 1L;
    
    private OneUIApplicationConfiguration configuration;
    
    public OneUIApplicationBean() {
    }
    
    
    //////////////////////////////////////////////////////////////////////////////
    // Manage search
    //////////////////////////////////////////////////////////////////////////////

    
    //////////////////////////////////////////////////////////////////////////////
    // Access the configuration
    //////////////////////////////////////////////////////////////////////////////

    public OneUIApplicationConfiguration getConfiguration() {
        if(configuration==null) {
            synchronized (this) {
                if(configuration==null) {
                    this.configuration = parseConfiguration(DEFAULT_CONFIGURATION_FILE);
                    if(configuration==null) {
                        configuration = createDefaultConfiguration();
                    }
                }
            }
        }
        return configuration;
    }
    
    protected OneUIApplicationConfiguration createDefaultConfiguration() {
        OneUIApplicationConfiguration conf = new OneUIApplicationConfiguration();
        return conf;
    }
    
    public synchronized void setConfigurationFile(String path) {
        this.configuration = parseConfiguration(path);
        if(configuration==null) {
            configuration = createDefaultConfiguration();
        }
    }
    
    protected static OneUIApplicationConfiguration parseConfiguration(String path) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        InputStream is = ctx.getExternalContext().getResourceAsStream(path);
        if(is!=null) {
            try {
                try {
                    Reader reader = new InputStreamReader(is,"UTF-8"); // $NON-NLS-1$
                    OneUIJSONFactory factory = new OneUIJSONFactory();
                    return (OneUIApplicationConfiguration)JsonParser.fromJson(factory, reader);
                } finally {
                    is.close();
                }
            } catch(Exception ex) {
                Platform.getInstance().log(ex);
            }
        }
        return null;
    }
}
