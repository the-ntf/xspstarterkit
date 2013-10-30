/* ***************************************************************** */
/* Licensed Materials - Property of IBM                              */
/*                                                                   */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */
/*                                                                   */
/* US Government Users Restricted Rights - Use, duplication or       */
/* disclosure restricted by GSA ADP Schedule Contract with           */
/* IBM Corp.                                                         */
/*                                                                   */
/* ***************************************************************** */





dojo.provide("extlib.dijit.ImageSelect");

dojo.require("dojo.cache")
dojo.require("dijit.form._FormWidget")
dojo.require("dijit.form._FormValueWidget")

dojo.declare(
	'extlib.dijit.ImageSelect',
	dijit.form._FormValueWidget,
	{
		listStyle: '',
		listClass: '',
		itemStyle: '',
		itemClass: '',
		valueList: {},
		templateString: dojo.cache("extlib.dijit", "templates/ImageSelect.html"),
		postCreate: function() {
			this.inherited(arguments)
			var _this = this
			var _pos = "last";
			if(!this.isLeftToRight()){
				_pos = "first";
			}
			for(var i=0; i<this.valueList.length; i++) {
				var v = this.valueList[i];
				var c = dojo.create("span", {}, this.list, _pos);
				if(this.itemClass) dojo.attr(c,"className",this.itemClass);
				if(this.itemStyle) c.style.cssText = this.itemStyle;
				var a = dojo.create("a", {
					href: 'javascript:;',
					onclick: function(e) {
						if(!_this.readOnly) {
							_this._setCurrent(parseInt(dojo.attr(this.firstChild,'idx')),this);
						}
					}
				}, c);
				var img = dojo.create("img", {
					val:v.v,
					alt:v.a,
					title:v.t,
					idx:i
				}, a);
			}
			this._setValueAttr(this.value);
		},
		_setValueAttr: function(value, priorityChange){
			this.textbox.value = this.value = value;
			this.inherited(arguments);
			dojo.query("img",this.list).forEach(function(n) {
				var sel = dojo.attr(n,"val")==value;
				var v = this.valueList[parseInt(dojo.attr(n,"idx"))];
				dojo.attr(n,"src",sel?v.si:v.i)
				dojo.attr(n,"className",sel?v.sc:v.c)
				n.style.cssText = sel?v.ss:v.s;
			},this);
		},
		_setCurrent: function(idx,a){
			this._handleOnChange(this.valueList[idx].v);
			this._setValueAttr(idx>=0?this.valueList[idx].v:'');
			a.focus();
		},
		_onTextChange: function(e){
			this._setValueAttr(this.textbox.value)
		},
		_onMouse: function(e){ // WHY THIS?? where is the issue?
			this.inherited(arguments)
		}
	}
);
