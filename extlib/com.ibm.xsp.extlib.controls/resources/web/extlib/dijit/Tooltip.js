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






dojo.provide("extlib.dijit.Tooltip");
	
dojo.require("dijit.Tooltip");

dojo.declare(
		"extlib.dijit.Tooltip",
		dijit.Tooltip,
		{
			ajaxParams: "",
			postCreate: function(){
				this.inherited(arguments);
				this.ctid = this.attr("id")+":_content"
				this.domNode.innerHTML = "<div id='"+this.ctid+"'></div>"
			}, 
			open: function(/*DomNode*/ target){
				// Delay the opening after partial refresh had been processed
				var _this = this; var _args = arguments;
				var options = {
					"params": this.ajaxParams,
					"formId": this.attr("id"),
					onComplete: function() {
						_this.inherited(_args);
					}
				};
				XSP.partialRefreshGet(this.ctid,options);
			}
		}
	);
