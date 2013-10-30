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





/*
* Author: Maire Kehoe (mkehoe@ie.ibm.com)
* Date: 18 Jan 2012
* ExtlibVersion.java
*/
package com.ibm.xsp.extlib.version;

import com.ibm.xsp.core.Version;
import com.ibm.xsp.library.XspLibrary;
import com.ibm.xsp.registry.FacesDefinition;

/**
 * The Extension Library tag version, used with {@link XspLibrary#getTagVersion()} 
 * and {@link FacesDefinition#getSince()}, will be in the form:
 * "a.a.abcdd"
 * where
 * "a.a.a" is the Notes/Domino version (like 8.5.3)
 * "b" is the Domino upgrade pack level / stream in active development 
 *      e.g. Upgrade Pack 1 or Upgrade Pack 2, or 0 - no upgradePack.
 * "c" is usually 0, reserved for fix pack levels (FixPack1 or FixPack2)
 * "dd" is the release number on openNTF - which may be incremented 
 *      with every openNTF release or upgrade pack release (or may not, if no tags are added)
 * Note, that version scheme doesn't work when the micro number is 0 (like 9.1.0),
 * so from 9.0 there are plans to move to a datestamped version 
 * (XspLibrary has been updated to allow compare datestamps),
 * so the version would be like "9.0.0.20120930".
 * So examples of the tag versions so far are:
 * null is the 8.5.3 Upgrade Pack 1 release, or any openNTF release prior to the introduction of versioning.
 * "8.5.32001" is an openNTF release 
 *             ExtensionLibraryOpenNTF-853.20120126-0415.zip
 *             Released on: 26 Jan 2012
 * "8.5.32002" is an openNTF release
 *             ExtensionLibraryOpenNTF-853.20120320-1003.zip
 *             Released on: 3 Apr 2012
 * "8.5.32003" was a planned openNTF release that was never published.
 *             Tags added in that release were first made available
 *             on openNTF in the following "8.5.32004" release.
 * "8.5.32004" is an openNTF release
 *             ExtensionLibraryOpenNTF-853.20120605-0921.zip
 *             Released on: 6 Jun 2012
 * "8.5.32005" is an openNTF release
 *             ExtensionLibraryOpenNTF-853.20121022-1354.zip
 *             Released on: 26 Oct 2012
 * "8.5.32006" is an openNTF release
 *             XPagesExtensionLibraryOpenNTF-853.20121217-1354.zip
 *             Released on: 20 Dec 2012
 * "8.5.32007" is an openNTF release 
 *             ExtensionLibraryOpenNTF-853.20130315-0724.zip
 *             Released on: 4 Apr 2013.
 * 
 * 9.0.0.v00_00 this is a Notes/Domino 9.0 release or the Beta version of that release
 *              or any early pre-release code drops.
 * 
 * 9.0.0.v00_01 is an OpenNTF 9.0 release
 *              ExtensionLibraryOpenNTF-900v00_01.20130415-0518.zip
 *              Released on: 22 Apr 2013.
 * 
 * 9.0.0.v00_02 is an OpenNTF 9.0 release
 *              ExtensionLibraryOpenNTF-900v00_02.20130515-2200.zip
 *              Released on: 17 May 2013.
 * 
 * 9.0.0.v00_03 this will be the next OpenNTF 9.0 release.
 * 
 * @author Maire Kehoe (mkehoe@ie.ibm.com)
 */
public class ExtlibVersion {
    private static String s_currentVersion;
    public static String getCurrentVersionString(){
        if( null == s_currentVersion ){
            Version currentVersionObj = computeCurrentVersion();
            s_currentVersion = currentVersionObj.toString();
        }
        return s_currentVersion;
    }
    private static Version computeCurrentVersion(){
        String versionStr;
        /* Note this value is different in the openNTF stream 
         * from Notes/Domino in-development stream. */
        boolean isOpenNTFRelease = false;
        if( isOpenNTFRelease ){
            versionStr = "9.0.0.v00_03"; // $NON-NLS-1$
        }else{
            versionStr = "9.0.1.v00_00"; // $NON-NLS-1$
        }
        return Version.parseVersion(versionStr);
    }
}
