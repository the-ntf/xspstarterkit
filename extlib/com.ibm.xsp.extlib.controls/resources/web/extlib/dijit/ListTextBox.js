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





dojo.provide("extlib.dijit.ListTextBox");

dojo.require("extlib.dijit._ListTextBox")

dojo.declare(
	'extlib.dijit.ListTextBox',
	extlib.dijit._ListTextBox,
	{
		displayLabel:false,
		labels: {},
		_extractLabel: function(s){
			return this.labels && this.displayLabel ? this.labels[s]||s : s; 
		}
	}
);
