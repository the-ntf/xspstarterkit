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





dojo.provide("extlib.dijit.Stack");
	
dojo.require("dijit.layout.StackContainer");

dojo.declare("extlib.dijit.StackContainer",dijit.layout.StackContainer, 
	{
		selectChild: function(page) {
			this.inherited(arguments)
			var form = XSP.findForm(this.id)
			if(form&&form[this.id+"_sel"]) {
				form[this.id+"_sel"].value = page ? page.id.substring(page.id.lastIndexOf(':')+1) : ""
			}
		}
	}
);
