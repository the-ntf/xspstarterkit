dojo.provide("extlib.dijit.outline.accordion");
dojo.require("extlib.dijit.outline.base");

dojo.declare("extlib.dijit.outline.accordion",
[extlib.dijit.outline.base],{
	isRoot:true,
	createMenuObject:function(){
		var containerDomNode=new dojox.mobile.EdgeToEdgeList();
		
		if(!this.isRoot){
			dojo.addClass(containerDomNode.domNode,"menuHidden");
			dojo.addClass(containerDomNode.domNode,"menuCreated");
			dojo.removeClass(containerDomNode.domNode, "mblEdgeToEdgeList");
			dojo.addClass(containerDomNode.domNode,"mblRoundRectList");
		}
		else{
			this.isRoot=false;
		}
		
		return(containerDomNode);
	},
	
	addSubMenu:function(menuItem,menu,subMenuObj){
		var itemDomNode=new dojox.mobile.ListItem({
			moveTo:menuItem.href,
			label:menuItem.label
		});
		
		dojo.connect(itemDomNode.domNode,"onclick",dojo.hitch(this,"_toggleMenu",{menuItem:itemDomNode}));
		
		//add the arrow down
		var arrow=dojo.create("div",null,itemDomNode.domNode,"first");
		dojo.addClass(arrow,"mblArrowDown");
		
		//append the new node to the menu
		itemDomNode.placeAt(menu);
		subMenuObj.placeAt(itemDomNode.domNode);
		
		//item styling
		dojo.addClass(itemDomNode.domNode,menuItem.containerClass);
		dojo.style(itemDomNode.domNode,menuItem.containerStyle);
		dojo.style(itemDomNode.domNode,"height","100%");
		
	},
	
	_toggleMenu: function(args){
		dojo.stopEvent(window.event);
		var menuItem = args.menuItem;
		
		dojo.toggleClass(menuItem.domNode.firstChild, "mblArrowDown");
		dojo.toggleClass(menuItem.domNode.firstChild, "mblArrowUp");
		//I test the arrowUp because it has just changed
		if(dojo.hasClass(menuItem.domNode.firstChild, "mblArrowUp")) {
			var toggleNode = dojo.query('.menuHidden', menuItem.domNode)[0];
			//calculate height of the menuHidden and apply it
			if(!menuItem.accordionHeight) {
				menuItem.accordionHeight = toggleNode.offsetHeight;
			}
			dojo.style(toggleNode, 'height', menuItem.accordionHeight + 'px');
			
			//show the first submenu
			dojo.toggleClass(toggleNode, "menuShown");
			dojo.toggleClass(toggleNode, "menuHidden");
			if(dojo.hasClass(toggleNode, "menuCreated")) {
				dojo.removeClass(toggleNode, "menuCreated");
			}
		}
		else{
			//closing down open submenus
			dojo.forEach(dojo.query('.menuShown', menuItem.domNode),
					function(node) {
						var arrowContainer = dojo.query('.mblArrowUp', node)[0];
						if(arrowContainer) {
							dojo.toggleClass(arrowContainer, "mblArrowDown");
							dojo.toggleClass(arrowContainer, "mblArrowUp");
						}
						dojo.style(node, 'height', '0px');
						dojo.toggleClass(node, "menuHidden");
						dojo.toggleClass(node, "menuShown");
					}
			);
		}
	},
	
	addMenuItem:function(menuItem,menu)
	{
		var itemDomNode=new dojox.mobile.ListItem({
			moveTo:menuItem.href,
			label:menuItem.label
		});
		
		dojo.addClass(itemDomNode,menuItem.itemClass);
		dojo.style(itemDomNode,menuItem.itemStyle);
		//append the new node to the menu and call startup to create the control
		itemDomNode.placeAt(menu);
		itemDomNode.startup();
	},
	
	renderMenu:function(menu){
		//this.srcNodeRef.appendChild(menu);
		menu.placeAt(this.srcNodeRef);
	}
});