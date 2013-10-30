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





dojo.provide("extlib.dijit.DynamicContent");

XSP.showContent = function xe_sct(panelid,content,params) {
	params = dojo.mixin(params,{content:content})
	if(XSP._hashContentId==panelid) {
		XSP.updateHash(dojo.objectToQuery(params))
		XSP.partialRefreshGet(panelid,{params:XSP._hash})
	} else {
		XSP.partialRefreshGet(panelid,{params:params})
	}
}

XSP.registerHash = function xe_rhs(panelid) {
	XSP._hashContentId=panelid
	if(!XSP._hashCallback) {
		dojo.require("dojo.hash");
		XSP._hashCallback = function(hash) {
			// Only refresh if the has is different from the last one
			if(XSP._hash!=hash) {
				XSP._hash=hash
				XSP.partialRefreshGet(XSP._hashContentId,{params:hash})
			}
		}
		dojo.addOnLoad( function() {
			dojo.subscribe("/dojo/hashchange", XSP._hashCallback);
			if(dojo.hash()) {
				XSP._hashCallback(dojo.hash())
			}
		})
	}
}

// Update the copy of the hash while preventing the event processing
XSP.updateHash = function xs_uhs(h) {
	XSP._hash=h
	dojo.hash(h)
}
