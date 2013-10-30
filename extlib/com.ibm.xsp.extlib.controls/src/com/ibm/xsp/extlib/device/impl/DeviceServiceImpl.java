/* ***************************************************************** */
/*                                                                   */
/* IBM Confidential                                                  */
/*                                                                   */
/* OCO Source Materials                                              */
/*                                                                   */
/* Copyright IBM Corporation 2004, 2011                              */
/*                                                                   */
/* The source code for this program is not published or otherwise    */
/* divested of its trade secrets, irrespective of what has           */
/* been deposited with the U.S. Copyright Office.                    */
/* ***************************************************************** */
/*
* Date: 25 Jul 2013
* DeviceServiceImpl.java
*/

package com.ibm.xsp.extlib.device.impl;

import com.ibm.xsp.extlib.device.Device;
import com.ibm.xsp.extlib.device.DeviceService;
import com.ibm.xsp.extlib.social.impl.ServiceImpl;



public class DeviceServiceImpl extends ServiceImpl implements DeviceService{
    public DeviceServiceImpl(DeviceDataProvider[] dataProviders) {
        super(dataProviders);  
    }

    /* (non-Javadoc)
     * @see com.ibm.xsp.extlib.device.DeviceService#getDevice()
     */
    public Device getDevice(String userAgent) {

        return new DeviceImpl(this, userAgent);
    }
}
