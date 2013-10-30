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





dojo.provide("extlib.dijit.Loading");
dojo.requireLocalization("dijit", "loading");

XSP.startAjaxLoading = function xe_ldsl(message) {
	dojo.require("dijit.Dialog");
	dojo.addOnLoad(function() {
		if (!message) {
			message = "Please wait...";
		}
		var ct = "<span class='dijitContentPaneLoading'>"+message+"</span>";
		XSP._axdlg = new dijit.Dialog({ title: "", content: ct });
		XSP._axdlg.titleBar.style.display='none';
		XSP._axdlg.show();
	});
}

XSP.endAjaxLoading = function xe_ldel() {
	if(XSP._axdlg) {
		XSP._axdlg.hide();
		XSP._axdlg = null;
	}
}

XSP.animateLoading = function xe_ldal(id,message) {
	var messages = dojo.i18n.getLocalization("dijit", "loading");
	message = message || messages["loadingState"];
	var he = "<span class='dijitContentPaneLoading'>"+message+"</span>";
	dojo.place(he,id,"first");
}
