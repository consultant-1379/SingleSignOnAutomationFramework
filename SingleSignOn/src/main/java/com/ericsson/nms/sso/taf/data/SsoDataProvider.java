package com.ericsson.nms.sso.taf.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.DataProvider;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.HostType;
import com.ericsson.nms.sso.taf.getters.rest.SsoRestGetter;



public class SsoDataProvider implements DataProvider {
    
	static Logger log = Logger.getLogger(SsoDataProvider.class);
	private static List<Host> hosts = DataHandler.getHosts();
	
	public static List<Host> getHosts() {
		return hosts;
	}
	
	public static Host getSC1() {	
		return DataHandler.getHostByName("sc1");
	}
	
	public static Host getSC2() {
		return DataHandler.getHostByName("sc2");
	}
	
	public static Host getPeerNode1() {	
		return DataHandler.getHostByName("peer1");
	}
	
	public static Host getPeerNode2() {
		return DataHandler.getHostByName("peer2");
	}
	
    public String getSessionTokenUrl(){
    	return SsoRestGetter.getSessionTokenUrl();
    }
    
    public String getApplicationWithTokenUrl(){
    	return SsoRestGetter.getApplicationWithTokenUrl();
    }
	
	public static List<String> getCitrixAppList() {
		List<String> list = new ArrayList<String>(Arrays.asList(SsoDataProvider.getProperty("CitrixAppList").split(",")));
		return list;
	}

	
	@SuppressWarnings("unchecked")
	public static String getProperty(String prop) {
		Map<Object,Object> mp= DataHandler.getAttributes();
		String value=(String) mp.get(prop);
		if(value == null) {
			log.warn("No value for property: "+prop);
			return null;
		}
		log.debug("Property: "+prop+" - Value: "+value);
		return value;
	}

	public static String generateRandomString(int length) {
		return RandomStringUtils.randomAlphabetic(length);
	}


}
