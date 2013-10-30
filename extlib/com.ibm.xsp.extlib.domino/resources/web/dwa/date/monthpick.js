/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */






dojo.provide("dwa.date.monthpick");

dojo.require("dwa.date.calendarlistpick");
dojo.require("dwa.date.dateFormatter");

var D_DateFmt_ShortMonth4YrOnly = 17;

dojo.declare(
	"dwa.date.monthpick",
	dwa.date.calendarlistpick,
{
	sClass: "com_ibm_dwa_ui_monthpick",
	nEntries: 12,
	asActions: [],
	
	getFormatter: function(){
		if(!this.oFormatter)
			this.oFormatter = new dwa.date.dateFormatter(D_DateFmt_ShortMonth4YrOnly);
		return this.oFormatter;
	},
	adjustCalendar: function(oCalendar, idx){
		return oCalendar.adjustDays(0, idx, 0);
	},
	needUpdate: function(){
		return (!this.oPrevCalendar || (this.oPrevCalendar.nYear != this.oCalendar.nYear) || (this.oPrevCalendar.nMonth != this.oCalendar.nMonth));
	}
});
