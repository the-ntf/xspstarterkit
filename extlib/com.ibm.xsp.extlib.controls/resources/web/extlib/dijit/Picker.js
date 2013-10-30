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





dojo.provide("extlib.dijit.Picker");

XSP.selectValue = function xe_svpk(type,params) {
	XSP.djRequire(type)
	dojo.addOnLoad(function(){
		var clazz = dojo.getObject(type)
	    var d = new clazz(params);
	    d.show();
	});
	 //PHAN8YWEJZ fix IE namepicker beforeunload event occurring
	return false;
}
