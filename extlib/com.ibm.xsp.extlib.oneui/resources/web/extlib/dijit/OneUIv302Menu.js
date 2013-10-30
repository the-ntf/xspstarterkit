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





dojo.provide("extlib.dijit.OneUIv302Menu");
dojo.require("extlib.dijit.Menu");
dojo.require("dijit.Menu");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");

dojo.declare(
	"extlib.dijit.OneUIv302Menu",
	[dijit._Widget, dijit._Templated,dijit.Menu],
	{
		templateString: dojo.cache("extlib.dijit", "templates/OneUIv302Menu.html")
	}
);

