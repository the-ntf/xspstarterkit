/* ***************************************************************** */
/*                                                                   */
/* IBM Confidential                                                  */
/*                                                                   */
/* OCO Source Materials                                              */
/*                                                                   */
/* Copyright IBM Corporation 2004, 2013                              */
/*                                                                   */
/* The source code for this program is not published or otherwise    */
/* divested of its trade secrets, irrespective of what has           */
/* been deposited with the U.S. Copyright Office.                    */
/* ***************************************************************** */

package com.ibm.xsp.extlib.device.extensions;

import java.lang.reflect.Method;
import java.util.Set;

import com.ibm.xsp.designer.context.XSPUserAgent;
import com.ibm.xsp.extlib.device.impl.AbstractDeviceDataProvider;
import com.ibm.xsp.extlib.device.impl.DeviceImpl;

public class XspUserAgentDataProvider extends AbstractDeviceDataProvider {
    private Method[] methods = XSPUserAgent.class.getMethods();
    private static String[] PREFIXES = {"is","get"}; // $NON-NLS-1$ $NON-NLS-2$
    
    public XspUserAgentDataProvider() {
        
    }
           
    private Method retrieveMethod(String name)
    {
        for(String prefix:PREFIXES)
        {
            for(Method method:methods)
            {
                if(method.getName().equalsIgnoreCase(prefix+name))
                {
                    Class[] parameters = method.getParameterTypes();
                    if(parameters.length == 0)
                    {
                        Object[] params = new Object[] {};
                        return method;
                    }
                }
            }
        }
        return null;
    }

    
    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.AbstractResourceProvider#isDefaultProvider()
     */
    @Override
    public boolean isDefaultProvider() {
        return true;
    }

    @Override
    protected String getDefaultCacheScope() {
        return "global"; // $NON-NLS-1$
    }
    @Override
    protected int getDefaultCacheSize() {
        return 300;
    }

    
    @Override
    public Class<?> getType(DeviceImpl device, Object prop) {
        
        if(prop instanceof String)
        {
            Method met = retrieveMethod((String)prop);
            if(met != null)
            {
               return met.getReturnType();
            }
        }
        return null;
    }
    

    @Override
    public Object getValue(DeviceImpl device, Object prop) {
         Method met = retrieveMethod((String)prop);
        if(met != null)
        {
            try {
                
                Class[] parameters = met.getParameterTypes();
                if(parameters.length == 0)
                {
                    XSPUserAgent xspUA = new XSPUserAgent(device.getId());
                    Object[] params = new Object[] {};
                    return met.invoke(xspUA, params);
                }
                
            }
            catch (Exception e) {
                // Ignore exception and return null
            }
        }
        return null;
    }




    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.device.impl.AbstractDeviceDataProvider#readValues(com.ibm.xsp.extlib.device.impl.DeviceImpl[])
     */
    @Override
    public void readValues(DeviceImpl[] devices) {
        // not Supported yet
        
    }

   
}