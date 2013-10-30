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





dojo.provide("extlib.dijit.OneUIv302Dialog");

dojo.require("extlib.dijit.Dialog");

dojo.declare(
	"extlib.dijit.OneUIv302Dialog",
	extlib.dijit.Dialog,
	{
		baseClass: "",
		templateString: dojo.cache("extlib.dijit", "templates/OneUIv302Dialog.html")
	}
);

// This is used by the picker dialog to grab the correct UI
XSP._dialog_type="extlib.dijit.OneUIv302Dialog";
