/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */






dojo.provide("dwa.common.contextMenu");

dojo.require("dwa.common.dropdownManager");
dojo.require("dwa.common.menu");

dojo.declare(
	"dwa.common.contextMenu",
	null,
{
	menuInfo: null,
	defaultImageModule: "dwa.common",
	defaultConsolidatedImage: "images/basicicons.gif",
	focusMethod: null,
	activeIndex: 0,
	id: "",
	
	_menu: null,
	_dropdownManager: null,
	_started: false,
	
	constructor: function(/*Object*/args) {
		if (args) {
			dojo.mixin(this, args);
		}
		this._dropdownManager = dwa.common.dropdownManager.get("root");
		
		var menuNode = dojo.doc.createElement("div");
		if (this.id) {
			menuNode.id = this.id;
		}
		dojo.addClass(menuNode, "s-hidden");
		dojo.doc.body.appendChild(menuNode);
		var props = {
			menuInfo: this.menuInfo,
			dropdownManagerId: "root",
			defaultImageModule: this.defaultImageModule,
			defaultConsolidatedImage: this.defaultConsolidatedImage,
			focusMethod: this.focusMethod,
			activeIndex: this.activeIndex
		};
		this._menu = new dwa.common.menu(props, menuNode);
	},
	
	show: function(/*Object*/ev) {
		dojo.stopEvent(ev);
		if (!this._started) {
			this._menu.startup();
			dojo.removeClass(this._menu.domNode, "s-hidden");
			this._started = true;
		} else {
			this._menu.refresh();
		}
		this._dropdownManager.nContainerWidth = this._dropdownManager.nContainerHeight = 0;
		var x = dojo._isBodyLtr() ? ev.clientX : dojo.doc.body.clientWidth - ev.clientX;
		var y = ev.clientY;
		this._dropdownManager.oPos = new dwa.common.utils.pos(x, y);
		this._dropdownManager.show(ev, this._menu.id);
	},
	
	hide: function() {
		this._menu.deactivate(true);
	},
	
	destroy: function() {
		this._menu.destroy();
	}
});
