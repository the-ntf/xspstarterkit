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






package com.ibm.xsp.extlib.renderkit.html_extended.oneui.data;

import com.ibm.xsp.extlib.renderkit.html_extended.data.ForumPostRenderer;



/**
 * One UI data view renderer.
 */
public class OneUIForumPostRenderer extends ForumPostRenderer {

    @Override
    protected Object getProperty(int prop) {
        switch(prop) {
            case PROP_MAINCLASS:                return "lotusForum lotusPost"; // $NON-NLS-1$
            case PROP_MAINSTYLEVIEWFIX:         return "margin: 0"; // $NON-NLS-1$
            case PROP_AUTHORCLASS:              return "lotusPostAuthorInfo";    // $NON-NLS-1$
            case PROP_AUTHORAVATARCLASS:        return "lotusPostAvatar";    // $NON-NLS-1$
            case PROP_AUTHORNAMECLASS:          return "lotusPostName";  // $NON-NLS-1$
            case PROP_AUTHORMETACLASS:          return "lotusMeta";  // $NON-NLS-1$
            case PROP_POSTCLASS:                return "lotusPostContent"; // $NON-NLS-1$
            //case PROP_POSTTITLECLASS:         return "";  
            case PROP_POSTMETACLASS:            return "lotusMeta";  // $NON-NLS-1$
            case PROP_POSTDETAILSCLASS:         return "lotusPostDetails";   // $NON-NLS-1$
        }
        return super.getProperty(prop);
    }
}
