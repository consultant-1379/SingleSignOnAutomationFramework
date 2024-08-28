package com.ericsson.nms.sso.taf.handlers;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.Handler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.implementation.SshRemoteCommandExecutor;
import com.ericsson.nms.sso.taf.data.SsoDataProvider;
import com.ericsson.nms.sso.taf.utils.ssoUtils;
public class SsoadmHandler implements Handler {

	static Logger log = Logger.getLogger(SsoadmHandler.class);
	
	private static final String USERARG=" -u ";
	private static final String PASSWDFILEARG=" -f ";
	//private static final String HOSTARG=" -f ";
	private static final String SERVICEARG=" -s ";
	private static final String REALMARG=" -e ";
	private static final String ARGS=" -a ";
	private static final String GETREALMATTRS=" get-realm-svc-attrs ";
	private static final String SETREALMATTRS=" set-realm-svc-attrs ";
	
	private String ssoadm = SsoDataProvider.getProperty("ssoadmPath");
	private String passwdFile = SsoDataProvider.getProperty("ssoadmPasswdFile");
	private String user = SsoDataProvider.getProperty("ssoadmUser");
	
	SshRemoteCommandExecutor remExec = new SshRemoteCommandExecutor();
	String service;
	
	public SsoadmHandler(Host host, String service) {
		this.service=service;
		remExec.setHost(ssoUtils.getAdminUserHost(host));
		log.info("Created SsoadmHandler");
	}
	
	public boolean runCmd(String cmd) {
		String command=ssoadm+cmd+USERARG+user+PASSWDFILEARG+passwdFile;
		return parseReturn(execCmd(command));
	}
	
	private String execCmd(String cmdWithArgs) {
		log.debug("Running:"+cmdWithArgs);
		return remExec.simplExec(cmdWithArgs);
	}
	
	public Map<String,String> getRealmSvcAttrs() {
		String command=ssoadm+GETREALMATTRS+SERVICEARG+service+REALMARG+"/"+USERARG+user+PASSWDFILEARG+passwdFile;
		Map<String, String> result= parseMap(execCmd(command));
		return result;
	}
	
	private Map<String, String> parseMap(String result) {
		Map<String,String> mp=new HashMap<String, String>();
		String[] temp = result.split("\n");
		for(int i = 0 ; i < temp.length; i++)
		{
			if(!temp[i].isEmpty()) {
				String[] pairs=temp[i].split("=");
				log.trace("Adding to map:"+pairs[0]+"="+pairs[1]);
				mp.put(pairs[0], pairs[1]);
			}
		}
		return mp;
	}

	public boolean setRealmSvcAttrs(Map<String,String> map) {
		String command=ssoadm+SETREALMATTRS+SERVICEARG+service+REALMARG+"/"+USERARG+user+PASSWDFILEARG+passwdFile+ARGS;
		String params="";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			params = params+" \""+ entry.getKey() +"="+ entry.getValue()+"\"";
		}
		log.debug("Params added ="+params);
		command=command+params;
		return parseReturn(execCmd(command));
	}
	
	private boolean parseReturn(String returned) {
		
		if(returned.contains("Attribute values were set.")) {
			log.debug("Parameters successfully set:"+returned);
			return true;
		}
		log.error("Failed to set parameters:"+returned);
		return false;
	}
	
	
}
