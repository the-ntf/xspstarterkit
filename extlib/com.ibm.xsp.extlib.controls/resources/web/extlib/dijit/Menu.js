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





dojo.provide("extlib.dijit.Menu");

dojo.require("dijit.Menu");

XSP.openMenu = function xe_opnme(evt,menuCtor) {
	var menu=menuCtor();
	evt=dojo.fixEvent(evt);
	function closeAndRestoreFocus(){
		try {dijit.focus(evt.target);} catch(exception){}
		dijit.popup.close(menu);
		menu.destroy();
	}
	dijit.popup.open({popup:menu,around:evt.target/*,orient:dojo._isBodyLtr()...*/,onExecute:closeAndRestoreFocus,onCancel:closeAndRestoreFocus});
	menu.focus();
	dojo.connect(menu,"_onBlur",function(){
		dijit.popup.close(menu);
		menu.destroy();
	});
	dojo.stopEvent(evt);	
};
