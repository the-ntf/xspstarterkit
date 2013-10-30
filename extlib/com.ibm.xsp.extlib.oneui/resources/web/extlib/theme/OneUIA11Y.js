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
dojo.provide("extlib.theme.OneUIA11Y");

XSP.addOnLoad(function() {
        var bodyElem = document.getElementsByTagName("body")[0];
        if (dojo.hasClass(bodyElem, "dijit_a11y")) {            
            dojo.addClass(bodyElem, "lotusImagesOff");
        }
});
