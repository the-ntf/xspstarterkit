/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */






dojo.provide("dwa.lv.NarrowModeMenu");

dojo.require("dijit._Widget");

dojo.declare(
	"dwa.lv.NarrowModeMenu",
	dijit._Widget,
{
	lvid: null,
	vl: null,
	checkedMenuItem: null,
	sortBy: -1,
	designRead: false,
	bindState: false,

	postCreate: function(){
		this.inherited(arguments);

		// set listview widget reference
		var lvWidget = dijit.byId( this.lvid );
		if( !lvWidget ) return;
		this.vl = lvWidget.oVL;
	},

	updateDesign: function(){
		this.updateState();
	},
	updateState: function(){
	},

	_onChange: function( checked, index, menuItem, vl, id ){
	},
	_resetColWidth: function(vl, id){
	}
});

dwa.lv.NarrowModeMenu.create = function( lvWidgetId ){
	var menu = new dwa.lv.NarrowModeMenu( { lvid: lvWidgetId } );

	//menu.updateDesign();
	return menu;
};
