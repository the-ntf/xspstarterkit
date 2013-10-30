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





dojo.provide("extlib.dijit.OneUIv302PickerList");

dojo.require("extlib.dijit.OneUIv302Dialog")
dojo.require("extlib.dijit.PickerList")

dojo.declare(
	'extlib.dijit.OneUIv302PickerList',
	[extlib.dijit.PickerList],
	{
        listWidth: "100%",
		templateString: dojo.cache("extlib.dijit", "templates/OneUIv302PickerList.html")
	}
);
