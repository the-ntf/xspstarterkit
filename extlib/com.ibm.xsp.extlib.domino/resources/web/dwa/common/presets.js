/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */






dojo.provide("dwa.common.presets");

dojo.require("dwa.common.notesValue");
dojo.require("dwa.date.calendar");

dojo.declare(
	"dwa.common.presets",
	null,
{

	constructor: function(oValues, fPost){
		 this.oValues = oValues ? oValues : {}; this.fPost = fPost; 
	},

	toString: function(){
		var sString = '';
		var sListSep = !this.fPost ? ',' : '&';
		var sPairSep = !this.fPost ? ';' : '=';
	
		for (var s in this.oValues) {
			var vValue = this.oValues[s];
	
			if (typeof(vValue) != 'undefined') {
				var sValue = (new dwa.common.notesValue(vValue)).getString(dwa.date.zoneInfo.prototype.oUTC);
				var sEncValue = !vValue.bNoURIencode ? encodeURIComponent(sValue) : sValue;
	
				// SPR KKOO7TZESU
				// The separator (comma) of the multiple mail addresses for mailto link need be encoded to
				// "%252C" instead of "%2C" in order to work with NotesDictionary.
				// Refer to MailToProtocolHandler() in haiku\haikuclient\upload\upload.cpp
				if (!this.fPost && sValue.indexOf(',') != -1)
					sEncValue = sEncValue.replace(/%2C/gi, '%252C');
				sString += (sString ? sListSep : '') + encodeURIComponent(s) + sPairSep + sEncValue;
			}
		}

		return sString;
	},

	fromString: function(sURIString, bNoDecodeURIComponent){
		if (!sURIString)
			return;
	
		var sListSep = !this.fPost ? ',' : '&';
		var sPairSep = !this.fPost ? ';' : '=';
		var asValues = sURIString.split(sListSep);
	
		for (var i = 0; i < asValues.length; i++){
			var asPair = asValues[i].split(sPairSep);
	
			if (!asPair[0] || !asPair[1])
				continue;
	
			if(bNoDecodeURIComponent)
				this.oValues[asPair[0]]  = asPair[1];
			else {
				try {
					this.oValues[asPair[0]] = decodeURIComponent(asPair[1].replace(/%252c/gi, '%2c'));
				} catch(e) {
					// KYOE85UCR3 : to use value directly in the url argument if failed to be decoded
					var oStr = new String(asPair[1].replace(/%252c/gi, '%2c'));
					oStr.bNoURIencode = true;
					this.oValues[asPair[0]] = oStr;
				}
			}
		}
	}
});
