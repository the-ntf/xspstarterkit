/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */





dojo.provide("extlib.dojo.data.XPagesRestStore");
dojo.require("dojox.data.JsonRestStore");

dojo.declare("extlib.dojo.data.XPagesRestStore",
	dojox.data.JsonRestStore,
	{
		extraArgs: "",
		fetch: function(args){
			var query = args.query || "";
			if(dojo.isObject(query)){
				query = dojo.objectToQuery(query);
				query = query ? "?" + query : "";
			}
			if (args.sort && args.sort.length) {
				query += (query.match(/\?/) ? '&' : '?') + "sortcolumn="+args.sort[0].attribute+"&sortorder="+(args.sort[0].descending?"descending":"ascending");
			}
			if(this.extraArgs) {
				query += (query.match(/\?/) ? '&' : '?') + this.extraArgs;
			}
			args.query = query;
			return this.inherited(arguments);
		},
		
		save: function(/* object */ keywordArgs){
			// Should we better use a custom Service object here?
			var plainXhr = dojo.xhr;
			var self = this;
			dojo.xhr = function(method,args){
				if(self.extraArgs) {
					args.url = args.url + (args.url.match(/\?/) ? '&' : '?') + self.extraArgs;
				}
				return plainXhr.apply(dojo,arguments);
			}
			this.inherited(arguments);
			dojo.xhr = plainXhr; 		
		}
	}
);
