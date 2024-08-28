package com.ericsson.nms.sso.taf.getters.rest;

import com.ericsson.cifwk.taf.RestGetter;

public class SsoRestGetter implements RestGetter {

	//private static Logger log = Logger.getLogger(SsoRestGetter.class);

    public static String getSessionTokenUrl(){
    	return "/heimdallr/identity/authenticate";
    }
    
    public static String getApplicationWithTokenUrl(){
    	return "/heimdallr/identity/authorize";
    }
    
    public static String getCitrixLoginWindowProcName(){
    	return "\"Login - \\\\Remote\"";
    }

}