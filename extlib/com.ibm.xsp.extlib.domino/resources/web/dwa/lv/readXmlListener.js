/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */






dojo.provide("dwa.lv.readXmlListener");

dojo.require("dwa.common.listeners");
dojo.require("dwa.lv.miscs");

dojo.declare(
	"dwa.lv.readXmlListener",
	dwa.common.xmlListener,
{
	constructor: function(){
		this.fLoadSync = false;
	},
	onReadyStateChange: function(){
	  return this.onDatasetComplete(arguments);
	},
	onDatasetComplete: function(){
		if(this.fLoadSync)
			return;
		if (this.oHttpRequest.readyState != 4)
			return;
		this.processResponse();
	},
	processResponse: function(){
		try {
			if (!this.checkHttpStatus("text/xml"))
				return;
		} catch (e) {	
			if(this.oCallback)
				this.oCallback(null,e.message);
			this.release();
			return;
		}
	
		this.oRequest.oXmlDoc = this.oHttpRequest.responseXML;
		if (this.oRequest.oXmlDoc && !this.oRequest.oXmlDoc.firstChild) {
			try {
				this.oRequest.oXmlDoc = dwa.lv.miscs.deserialize(this.oHttpRequest.responseText);
			} catch (exc) {
				if(this.oCallback)
					this.oCallback(null, exc.message);
				this.release();
				return;
			}
	    }
	
		if (this.oRequest.oXmlDoc.firstChild.tagName == 'parsererror') {
			if(this.oCallback)
				this.oCallback(null, this.oRequest.oXmlDoc.firstChild.textContent);
			this.release();
			return;
		}
	
		this.oRequest.oXmlRoot = this.oRequest.oXmlDoc.firstChild;
		// skip <?xml?> tag
		if(this.oRequest.oXmlRoot.nodeName == 'xml')
			this.oRequest.oXmlRoot = this.oRequest.oXmlRoot.nextSibling;
		
		if(this.oCallback)
			this.oCallback(this.oRequest.oXmlRoot, null);
		this.release();
	}
});
