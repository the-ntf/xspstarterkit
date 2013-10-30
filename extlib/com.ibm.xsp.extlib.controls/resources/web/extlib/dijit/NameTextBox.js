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





dojo.provide("extlib.dijit.NameTextBox");

dojo.require("extlib.dijit._ListTextBox")

dojo.declare(
	'extlib.dijit.NameTextBox',
	extlib.dijit._ListTextBox,
	{
		_extractLabel: function(s){
			return XSP.extractCN(s)
		}
	}
);
