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





dojo.provide("extlib.dijit.OneUIv302MenuItem");
dojo.require("dijit.MenuItem");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");

dojo.declare(
	"extlib.dijit.OneUIv302MenuItem",
	[dijit.MenuItem],
	{
		templateString: dojo.cache("extlib.dijit", "templates/OneUIv302MenuItem.html")
	}
);

