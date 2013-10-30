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





dojo.provide("extlib.dijit.OneUIDialog");

dojo.require("extlib.dijit.Dialog");

dojo.declare(
	"extlib.dijit.OneUIDialog",
	extlib.dijit.Dialog,
	{
		baseClass: "",
		templateString: dojo.cache("extlib.dijit", "templates/OneUIDialog.html")
	}
);

// This is used by the picker dialog to grab the correct UI
XSP._dialog_type="extlib.dijit.OneUIDialog";
