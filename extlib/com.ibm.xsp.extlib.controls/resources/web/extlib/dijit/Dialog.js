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





dojo.provide("extlib.dijit.Dialog");

dojo.require("dijit.Dialog");

dojo.declare(
	"extlib.dijit.Dialog",
	[dijit.Dialog],
	{
		keepComponents: false,
		iframePost: false,
		
		show: function() {
            this.inherited(arguments);
            if(this.iframePost) {
            	this.xhrPost = dojo.xhrPost;
        		dojo.xhrPost = function(o) {
        			dojo.require("dojo.io.iframe");
        			o.handleAs = "html";
        		    var load = o.load; 
        			o.load = function(response, ioArgs) {
        				load(response.body.innerHTML, ioArgs);
        			}
        			dojo.io.iframe.send(o)
        		}		
            }
		},
		hide: function() {
            if(this.iframePost) {
            	dojo.xhrPost = this.xhrPost;
            }
			this.inherited(arguments);
		}
	}
);

XSP.openDialog = function xe_od(dialogId,options,params) {
	dojo.addOnLoad(function(){
		// Should we here delete the dialog like in TemplateDialog instead of leaving it in the DOM?
		var created = false
		var dlg = dijit.byId(dialogId)
		if(!dlg) {
			options = dojo.mixin({dojoType:XSP._dialog_type||"extlib.dijit.Dialog"},options)
			dojo.parser.instantiate([dojo.byId(dialogId)],options);
			dlg = dijit.byId(dialogId)
			created = true
		} else {
			if(dlg.keepComponents) {
				dlg.show();
				return;
			}
		}
		var onComplete = function() {
			dlg.show()
		}
		var axOptions = {
			"params": dojo.mixin({'$$showdialog':true,'$$created':created},params),
			"onComplete": onComplete,
			"formId": dialogId
		}
		dlg.attr("content", "<div id='"+dialogId+":_content'></div>");
		XSP.partialRefreshGet(dialogId+":_content",axOptions);

        if(dojo.isIE < 8){   // Fix for SPR# PHAN8QSM7H, issue with min-width
                               // in a position: absolute context.
                               // Does not seem to occur in IE8 and above
            dojo.query('.lotusDialogBorder').style('width', '500px');
        }
	})
}

XSP.closeDialog = function xe_cd(dialogId,refreshId,params){
	var dlg = dijit.byId(dialogId);
	if(dlg){
		// As closeDialog can be called from partial refresh, we need to delay
		// this after partial refresh id completed
		setTimeout(dojo.hitch(this,function(){
			dlg.hide();
			if(refreshId) {
				var axOptions = {"params": params}
				XSP.partialRefreshGet(refreshId,axOptions)
			}
		}),0);
	}
}
