/* ***************************************************************** */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */






dojo.provide("dwa.lv.msgs");

dojo.require("dojo.i18n");
dojo.requireLocalization("dwa.lv", "listview_c");
dojo.requireLocalization("dwa.lv", "listview_s");

dwa.lv.msgs = {
  listview_cmsgs: 0,
  listview_smsgs: 0
};

dwa.lv.msgs.checkLang = function(lang){
  if( !dwa.lv.msgs.msglang ){
    dwa.lv.msgs.msglang = lang;
  }else if( lang && dwa.lv.msgs.msglang != lang ){
    dwa.lv.msgs.msglang = lang;
    // flush cached msgdata
    dwa.lv.msgs.listview_cmsgs = 0;
    dwa.lv.msgs.listview_smsgs = 0;
  }
}

dwa.lv.msgs.getListViewCMsg = function(key,lang){
  dwa.lv.msgs.checkLang(lang);
  if( dwa.lv.msgs.listview_cmsgs == 0){
    dwa.lv.msgs.listview_cmsgs = dojo.i18n.getLocalization("dwa.lv", "listview_c", lang);
  }
  var msg = dwa.lv.msgs.listview_cmsgs[key];

  //if( !msg ){
  //  alert('undefined key=' + key + 'cmsg=' + dwa.lv.msgs.listview_cmsgs);
  //}

  return msg;
};

dwa.lv.msgs.getListViewSMsg = function(key,lang){
  dwa.lv.msgs.checkLang(lang);
  if( dwa.lv.msgs.listview_smsgs == 0){
    dwa.lv.msgs.listview_smsgs = dojo.i18n.getLocalization("dwa.lv", "listview_s", lang);
  }

  //var msg = dwa.lv.msgs.listview_smsgs[key];
  //if( !msg ){
  //  alert('undefined key=' + key + 'smsg=' + dwa.lv.msgs.listview_smsgs );
  //}

  return dwa.lv.msgs.listview_smsgs[key];
};
