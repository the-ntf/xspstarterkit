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

dojo.declare(
	"extlib.dijit.TooltipDialog",
	[dijit.TooltipDialog],
	{
		keepComponents: false
	}
);

XSP.openTooltipDialog = function xe_otd(dialogId,_for,options,params) {
	//XSP.djRequire("dijit.TooltipDialog")

	dojo.addOnLoad(function(){
		var created = false
		var dlg = dijit.byId(dialogId)
		if(!dlg) {
			options = dojo.mixin({dojoType:"extlib.dijit.TooltipDialog"},options)
			dojo.parser.instantiate([dojo.byId(dialogId)],options);
			dlg = dijit.byId(dialogId)
			created = true;
		} else {
			if(dlg.keepComponents) {
				dijit.popup.open({
					popup: dlg, 
					around: dojo.byId(_for)
				});
				return;
			}
		}
		if(created) {
			dojo.connect(dlg, 'onBlur', function(){
				dijit.popup.close(dlg);
			})
		}
		dlg.attr("content", "<div id='"+dialogId+":_content'></div>");
		var onComplete = function() {
			dijit.popup.open({
				popup: dlg, 
				around: dojo.byId(_for)
			});
			dlg.focus();
		}
		var axOptions = {
			"params": dojo.mixin({'$$showdialog':true,'$$created':created},params),
			"onComplete": onComplete,
			"formId": dialogId
		}
		XSP.partialRefreshGet(dialogId+":_content",axOptions)
	})
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
