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





dojo.provide("extlib.dijit.AccordionPane");

dojo.require("dijit.layout.AccordionPane");

dojo.declare("extlib.dijit.AccordionPane",dijit.layout.AccordionPane, 
	{
		_setContent: function(cont) {
			if(typeof(cont)=="string") {
				var extract = function(markStart,markEnd) {
					var startIndex = cont.indexOf(markStart);
					if( startIndex >= 0 ){
						var endIndex = cont.lastIndexOf(markEnd);
						if( endIndex >= 0 ) {
							var script = cont.substring(startIndex+markStart.length, endIndex);
							cont = cont.substring(0, startIndex) + cont.substring(endIndex+markEnd.length);
							return script;
						}
					}
				};
				// Execute the script inthe header first
				var header = extract("<!-- XSP_UPDATE_HEADER_START -->\n","<!-- XSP_UPDATE_HEADER_END -->\n");
				if(header) {
					XSP.execScripts(XSP.processScripts(header,true));
				}
				//var scripts = XSP.processScripts(cont,true);
				//cont = XSP.processScripts(cont,false);
				this.inherited("_setContent", arguments);
				//XSP.execScripts(scripts);
			} else {
				this.inherited("_setContent", arguments);
			}
		}
	}
);
