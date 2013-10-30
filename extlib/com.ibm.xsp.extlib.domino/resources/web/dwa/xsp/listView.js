/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */






dojo.provide("dwa.xsp.listView");
dojo.require("dwa.lv.listView");

dojo.declare(
	"dwa.xsp.listView",
	dwa.lv.listView,
{
	postCreate: function(){
		this.inherited(arguments);

		var id = this.id;
		for(var n = this.domNode; n.tagName != "BODY"; n = n.parentNode){
			if(n.tagName == "FORM"){
				id = n.id + ":" + this.id + ":_sel";
				break;
			}
		}

		var input1 = dojo.doc.createElement("INPUT");
		input1.type = "hidden";
		input1.name = input1.id = this._selInputId = id;
		this.domNode.parentNode.insertBefore(input1, this.domNode);

		this.connect(dojo.global, "onresize", "resize");
	},
	createVirtualList:function(){
		var vlist = this.inherited(arguments);
		vlist.isCategory = function(oArgs){
			return (oArgs.oDataItem && (oArgs.v$.oDataStore.getAttribute(oArgs.oDataItem, 'category')=='true')) || oArgs.oColInfo.bCategory;
		};
		vlist.getIndent = function(oArgs){
			var jMax = ( oArgs.oColInfo.bResponse ? oArgs.v$.oDataStore.getAttribute(oArgs.oXmlEntry,'indent') : undefined);
			return jMax || parseInt( this.oDataStore.getAttribute(oArgs.oDataItem, 'indent'),10 );
		};
		vlist.needHidden = function(store, entry, colinfo, prevcolinfo){
			var indent = store.getAttribute(entry, 'indent');
			return entry && (
				(!store.getAttribute(entry, 'category') && colinfo.bCategory)
				|| (colinfo.bResponse  && 1 == indent)
				|| (prevcolinfo && prevcolinfo.bResponse && 1 < indent)
				);
		};
		return vlist;
	},
	selectEntryAction: function(oItems){
		if(!oItems){ return; }
		var asIds = [];
		for (var i = 0; i < oItems.length;i++){
			var sVal = this.store.getValue(oItems[i], "@noteid");
			if(sVal)
				asIds.push(sVal);
		}
		dojo.byId(this._selInputId).value = asIds.join(",");
	}
});
