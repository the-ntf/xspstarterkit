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





dojo.provide("extlib.dijit.ExtLib");

XSP.axGetRequestUrl = function xe_axurl(id,params) {
	var form = XSP.findForm(id);
	if(form) {
		var url = form.action
		url += (url.match(/\?/) ? '&' : '?') + "$$axtarget="+encodeURIComponent(id);
		if(form["$$viewid"]) url += "&" + "$$viewid="+form["$$viewid"].value		
		if(params) url += "&" + dojo.objectToQuery(params)
		return url;
	}
}

XSP.extractCN = function xe_ecn(s) {
	if(!s) return s
	// email, cn=xs, a/b/c 
	if(s.match(/"(.+)".*/i)) return RegExp.$1
	if(s.match(/.*cn=([^\/]+).*/i)) return RegExp.$1
	if(s.match(/([^\/]+).*/i)) return RegExp.$1
	return s 
}

XSP.concatStyle = function xe_cst(s1,s2) {
	if(s1&&s2) return s1+','+s2;
	return s1 || s2;
}

XSP.concatClass = function xe_ccl(s1,s2) {
	if(s1&&s2) return s1+' '+s2;
	return s1 || s2;
}

XSP.removeChildren = function xe_rmn(node) {
	while(node.firstChild) {
		node.removeChild(node.firstChild);
	}
}

XSP.setNodeText = function xe_snt(node,value) {
	XSP.removeChildren(node);
	if(value){
		node.appendChild(dojo.doc.createTextNode(value));
	}
}

XSP._cpOnLoad = function xe_cpol(node) {
	if(!XSP._cpOnLoadScript) {
    	var scriptNodes = dojo.query("script", dojo.byId(node));
    	var f = function xe_psc1(scriptNode){
    		if( scriptNode.type = "text/javascript" ){
    			dojo.eval(scriptNode.innerHTML);
    		}
        }
        scriptNodes.forEach(f);
    }
	delete XSP._cpOnLoadScript; 
	XSP._loaded();
}
