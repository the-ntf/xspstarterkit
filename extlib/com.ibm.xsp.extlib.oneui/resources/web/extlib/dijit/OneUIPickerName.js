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





dojo.provide("extlib.dijit.OneUIPickerName");

dojo.require("extlib.dijit.OneUIDialog")
dojo.require("extlib.dijit.PickerName")

dojo.declare(
	'extlib.dijit.OneUIPickerName',
	[extlib.dijit.PickerName],
	{
		// Should not be set for a 2 lists dialog
        //listWidth: "100%",
		templateString: dojo.cache("extlib.dijit", "templates/OneUIPickerName.html")
	}
);
