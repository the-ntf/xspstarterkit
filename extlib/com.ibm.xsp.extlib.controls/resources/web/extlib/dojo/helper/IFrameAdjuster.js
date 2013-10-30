/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */






dojo.provide("extlib.dojo.helper.IFrameAdjuster");

dojo.ready(function() {
	var target = document.getElementsByTagName('iframe'),
	    regex = /loadFirebugConsole/,
	    i;
	for(i = 0; i < target.length; i++) {
		// Don't remove, but adjust any occasional Firebug iframe inserted by dojox.html.metrics
		if(regex.test(target[i].src)) {
			//target[i].parentNode.removeChild(target[i]);
			if (!target[i].id) {target[i].id = "firebug" + i}
			if (!target[i].title) {target[i].title = "firebug" + i}
			target[i].setAttribute("role", "presentation");
		}
		// Add title if needed to an iframe created by dojo.hash
		if (target[i].id && target[i].id == "dojo-hash-iframe" && !target[i].title) {
			target[i].title = "dojo-hash-iframe" + i;
			target[i].setAttribute("role", "presentation");
		}
	}
});


