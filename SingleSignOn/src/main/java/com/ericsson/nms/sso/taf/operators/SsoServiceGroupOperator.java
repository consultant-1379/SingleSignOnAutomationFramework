package com.ericsson.nms.sso.taf.operators;

import java.util.List;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.GenericOperator;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.nms.sso.taf.handlers.ServiceGroupHandler;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;
import com.ericsson.nms.sso.taf.utils.ssoUtils;

public class SsoServiceGroupOperator implements GenericOperator{

	static Logger log = Logger.getLogger(SsoServiceGroupOperator.class);
	String lockedSi="NONE";
	
	public boolean lockApaches(Host host) {
		log.info("Locking both apaches.");
		boolean result=true;
		ServiceGroupHandler srcHandler = new ServiceGroupHandler(host);
		
		List<String> suNames=srcHandler.listServiceUnits(SsoTestDataProvider.getApacheSuShortName());
		
		for(String su : suNames) {
			if(!srcHandler.lockSu(su)) {
				log.debug("Locking Apache failed: "+su+":"+srcHandler.getLastResult());
				result=false;
			} 
		}
		return result;
	}

	public boolean lockSsoJboss(Host host) {
		log.info("Locking Sso Jboss");
		ServiceGroupHandler srcHandler = new ServiceGroupHandler(host);
	
		String siName=srcHandler.getSiActiveOnNode(SsoTestDataProvider.getSsoSiShortName());
		
		if(!srcHandler.lockSi(siName)) {
			log.debug("Locking Sso Jboss failed: "+srcHandler.getLastResult());
			return false;
		} else {
			lockedSi=siName;
			return true;
		}
	
	}

	public boolean unlockApaches(Host host) {
		log.info("Unlocking both apaches.");
		boolean result=true;
		ServiceGroupHandler srcHandler = new ServiceGroupHandler(host);
		
		List<String> suNames=srcHandler.listServiceUnits(SsoTestDataProvider.getApacheSuShortName());
		
		for(String su : suNames) {
			if(!srcHandler.unlockSu(su)) {
				log.debug("Unlocking Apache failed: "+su+":"+srcHandler.getLastResult());
				result=false;
			} 
		}
		return result;
	}

	public boolean unlockSsoJboss(Host host) {
		log.info("Unlocking Sso Jboss");
		ServiceGroupHandler srcHandler = new ServiceGroupHandler(host);
	
		if(lockedSi.equals("NONE")) {
			log.warn("No locked Si so nothing to unlock.");
			return false;
		}
		String siName=lockedSi;
		
	
		if(!srcHandler.unlockSi(siName)) {
			log.debug("Unlocking Sso Jboss failed: "+srcHandler.getLastResult());
			return false;
		} else {
			lockedSi="NONE";
			waitForJbossSsoStart();
			return true;
		}
	
	}

	private void waitForJbossSsoStart() {
		log.debug("Wating for jboss to start and to deploy heimdallr");
		//TODO make better - curl -L http://ip-address:8080/heimdallr/isAlive.jsp
		ssoUtils.sleep(30);
	}

}
