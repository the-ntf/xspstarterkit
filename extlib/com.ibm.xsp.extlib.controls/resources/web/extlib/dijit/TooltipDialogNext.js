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





dojo.provide("extlib.dijit.TooltipDialog");

dojo.require("dijit.TooltipDialog");
dojo.require("dijit.layout.ContentPane");

dojo.declare(
	"extlib.dijit.TooltipDialog",
	[dijit.TooltipDialog],
	{
		keepComponents: false,
		onBlur: function() {
			dijit.popup.close(this);
		}
	}
);

dojo.declare(
		"extlib.dijit._TooltipDialogWrapper",
		[dijit.layout.ContentPane],
		{
			dialogId: "",
			destroy: function() {
				// Destroy the associated dialog
				var dlg=dijit.byId(this.dialogId);
				if(dlg) {
					dlg.destroy();
				}
	        }
		}
	);

XSP.openTooltipDialog = function xe_otd(dialogId,_for,options,params) {
	var dlg = dijit.byId(dialogId)
	if(dlg) {
		// If the dialog exists, then keep if if asked for
		if(dlg.keepComponents) {
			dijit.popup.open({
				popup: dijit.byId(dialogId), 
				around: dojo.byId(_for)
			});
			return;
		}
	} else {
		// Move the tag that will be partial refreshed to the body tag
		// Else, the form it contains won't be created as it will be within 
		// the main form
		dojo.place(dojo.byId(dialogId),dojo.body());
	}
	var onComplete = function() {
		dojo.parser.parse(dojo.byId(dialogId));
		dijit.popup.open({
			popup: dijit.byId(dialogId), 
			around: dojo.byId(_for)
		});
	}
	var axOptions = {
		"params": dojo.mixin({'$$showdialog':true},params),
		"onComplete": onComplete,
		"formId": dialogId
	}
	XSP.partialRefreshGet(dialogId,axOptions)		
}

XSP.closeTooltipDialog = function xe_ctd(dialogId,refreshId,params){
	var dlg = dijit.byId(dialogId);
	if(dlg){
		dijit.popup.close(dlg);
		if(refreshId) {
			// As closeTooltipDialog is generally called from a partial refresh event, we have
			// to manually ensure that it can be submitted
			XSP.allowSubmit()
			var axOptions = {"params": params}
			XSP.partialRefreshGet(refreshId,axOptions)
		}
	}
}
