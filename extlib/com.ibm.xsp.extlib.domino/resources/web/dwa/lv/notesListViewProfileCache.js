/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */






dojo.provide("dwa.lv.notesListViewProfileCache");

dojo.require("dwa.lv.widgetListener");
dojo.require("dwa.lv.globals");

dojo.declare(
	"dwa.lv.notesListViewProfileCache",
	null,
{
	constructor: function(){
	    this.oProfileCache = {};
	},
	get: function(sItemName){
	    return this.oProfileCache[sItemName] ? this.oProfileCache[sItemName] : '';
	},
	set: function(sItemName, sValue, bUpdateProfile, sId){
	    if(typeof(this.oProfileCache[sItemName]) != 'undefined' && this.oProfileCache[sItemName] != sValue && bUpdateProfile && sId) {
	        dwa.lv.widgetListener.prototype.oClasses["com_ibm_dwa_io_actionStoreProfileField"] = ['Common'];
	        dwa.lv.globals.invokeActionDummy(null, sId, 'com_ibm_dwa_io_actionStoreProfileField', {sProfile:"iNotesViewProfile", sField:sItemName, sValue:sValue, oListener:null, bTextList:false});
	    }
	    this.oProfileCache[sItemName] = sValue;
	}
});
