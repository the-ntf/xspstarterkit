/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */






dojo.provide("dwa.lv.readJsonListener");

dojo.require("dwa.common.listeners");

dojo.declare(
	"dwa.lv.readJsonListener",
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
			if (!this.checkHttpStatus(["application/json","application/octet-stream"]))
				return;
			this.oRequest.oJsonRoot = eval('(' + this.oHttpRequest.responseText + ')');
		} catch (e) {	
			if(this.oCallback)
				this.oCallback(null,e.message);
			this.release();
			return;
		}
	
		if(this.oCallback)
			this.oCallback(this.oRequest.oJsonRoot, null);
		this.release();
	}
});
