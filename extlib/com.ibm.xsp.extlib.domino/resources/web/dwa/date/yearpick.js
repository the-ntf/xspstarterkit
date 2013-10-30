/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */






dojo.provide("dwa.date.yearpick");

dojo.require("dwa.date.calendarlistpick");
dojo.require("dwa.date.dateFormatter");

var D_DTFMT_YEAR = "yyyy";

dojo.declare(
	"dwa.date.yearpick",
	dwa.date.calendarlistpick,
{
	sClass: "com_ibm_dwa_ui_yearpick",
	nEntries: 8,
	asActions: [],
	
	getFormatter: function(){
		if(!this.oFormatter)
			this.oFormatter = new dwa.date.dateFormatter(D_DTFMT_YEAR);
		return this.oFormatter;
	},
	adjustCalendar: function(oCalendar, idx){
		return oCalendar.adjustDays(idx, 0, 0);
	},
	needUpdate: function(){
		return (!this.oPrevCalendar || (this.oPrevCalendar.nYear != this.oCalendar.nYear));
	}
});

