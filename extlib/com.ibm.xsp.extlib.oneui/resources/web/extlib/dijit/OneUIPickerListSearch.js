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





dojo.provide("extlib.dijit.OneUIPickerListSearch");

dojo.require("extlib.dijit.OneUIDialog")
dojo.require("extlib.dijit.PickerListSearch")

dojo.declare(
	'extlib.dijit.OneUIPickerListSearch',
	[extlib.dijit.PickerListSearch],
	{
        listWidth: "100%",
		templateString: dojo.cache("extlib.dijit", "templates/OneUIPickerListSearch.html")
	}
);
