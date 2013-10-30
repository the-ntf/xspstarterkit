/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */






dojo.provide("dwa.data.XPagesRestStore");
dojo.require("dwa.data._DominoStoreBase");
dojo.require("dwa.common.notesValue");

dojo.declare("dwa.data.XPagesRestStore",
			 dwa.data._DominoStoreBase,
{
	//	summary:
	//		A data store for XPages REST service

	type: "json",
	notesValue: new dwa.common.notesValue,

	_getCellValue: function(){
		//	summary:
		//		Get cell value from a cell object.
		//	description:
		//		Cell object is an attribute value of an item, which represents a row.
		//		It can be any type of data structure, which may include various meta data.
		//		This method will become a toString() for the cell object.
		//		'this' points to the cell object.
		var value = this["@value"];
		return ( typeof(value) == "undefined" ) ? "" : value.toString();
	},

	format: function(data){
		if(!data || !data.items){ return data; }
		var items = data.items;
		this._numRows = data["@topLevelEntries"];
		return items;
	}
});
