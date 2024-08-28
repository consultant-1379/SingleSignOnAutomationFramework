package com.ericsson.nms.sso.taf.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.UserType;


public class ssoUtils {
	static Logger log = Logger.getLogger(ssoUtils.class);
	
	public static String generateRandomString(int length) {
		return RandomStringUtils.randomAlphabetic(length);
	}

	public static void sleep(int sec) {
		try {
			log.info("Sleeping for "+sec+" seconds");
			Thread.sleep(sec*1000);
		} 
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Host getAdminUserHost(Host host) {
		Host returnHost = new Host();
		
		returnHost.setIp(host.getIp());
		returnHost.setUser(host.getUser(UserType.ADMIN));
		returnHost.setPass(host.getPass(UserType.ADMIN));

		return returnHost;
	}

}
