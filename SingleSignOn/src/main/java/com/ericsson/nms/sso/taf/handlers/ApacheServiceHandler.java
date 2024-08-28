package com.ericsson.nms.sso.taf.handlers;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.Handler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.implementation.SshRemoteCommandExecutor;
import com.ericsson.nms.sso.taf.utils.ssoUtils;

public class ApacheServiceHandler implements Handler {
	private static final String SERVICE_HTTPD_STATUS = "service httpd status";
	private static final String SERVICE_HTTPD_START = "service httpd start";
	private static final String SERVICE_HTTPD_STOP = "service httpd stop";
	
	static Logger log = Logger.getLogger(ApacheServiceHandler.class);
	
	
	
	public static Host getActiveApache(Host sc1, Host sc2) {
		SshRemoteCommandExecutor remExec = new SshRemoteCommandExecutor();
		remExec.setHost(ssoUtils.getAdminUserHost(sc1));
		remExec.simplExec(SERVICE_HTTPD_STATUS);
		if(remExec.getExitCode()==0) {
			log.info("SC-1 active :"+sc1.getHostname());
			return sc1;
		} else {
			remExec.setHost(ssoUtils.getAdminUserHost(sc1));
			remExec.simplExec(SERVICE_HTTPD_STATUS);
			if(remExec.getExitCode()==0) {
				log.info("SC-2 active :"+sc2.getHostname());
				return sc2;
			} else {
				log.error("No Active SC!");
				return null;
			}
		}
	}

	public static Host getInactiveApache(Host sc1, Host sc2) {
		SshRemoteCommandExecutor remExec = new SshRemoteCommandExecutor();
		remExec.setHost(ssoUtils.getAdminUserHost(sc1));
		remExec.simplExec(SERVICE_HTTPD_STATUS);
		if(remExec.getExitCode()==0) {
			log.info("SC-1 active :"+sc1.getHostname() + " - returning SC-2");
			return sc2;
		} else {
			remExec.setHost(ssoUtils.getAdminUserHost(sc1));
			remExec.simplExec(SERVICE_HTTPD_STATUS);
			if(remExec.getExitCode()==0) {
				log.info("SC-2 active :"+sc2.getHostname() + " - returning SC-1");
				return sc1;
			} else {
				log.error("No Active SC!");
				return null;
			}
		}
	}

    /**
	 * Stops Apache process httpd
	 * @return True if cmd ran OK. False otherwise. 
	 */
	public static boolean stopApacheService(Host host) {
		SshRemoteCommandExecutor remExec = new SshRemoteCommandExecutor();
		log.info("Stopping Apache on "+host.getHostname());
		remExec.setHost(ssoUtils.getAdminUserHost(host));
		String cmdOut=remExec.simplExec(SERVICE_HTTPD_STOP);
		return parseResponse(cmdOut);
	}

	/**
	 * Starts Apache process httpd
	 * @return True if cmd ran OK. False otherwise. 
	 */
	public static boolean startApacheService(Host host) {
		SshRemoteCommandExecutor remExec = new SshRemoteCommandExecutor();
		log.info("Starting Apache on"+host.getHostname());
		remExec.setHost(ssoUtils.getAdminUserHost(host));
		String cmdOut=remExec.simplExec(SERVICE_HTTPD_START);
		return parseResponse(cmdOut);
	}

	/**
	 * Get Apache status.
	 * @return - 
	 */
	public static String getApacheServiceStatus(Host host) {
		SshRemoteCommandExecutor remExec = new SshRemoteCommandExecutor();
		remExec.setHost(ssoUtils.getAdminUserHost(host));
		String cmdOut=remExec.simplExec(SERVICE_HTTPD_STATUS);
		return cmdOut;
	}
	
	private static boolean parseResponse(String cmdOut) {
		if(cmdOut.contains("OK")) {
			log.info("Command ran successfully.");
			return true;
		} else{
			log.info("Command fail. Returned:"+cmdOut);
			return false;
		}
		
	}
}
