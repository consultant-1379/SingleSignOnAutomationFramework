package com.ericsson.nms.sso.taf.operators;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.GenericOperator;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.implementation.SshRemoteCommandExecutor;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;


public class SsoRmtCmdOperator implements GenericOperator {

	static Logger log = Logger.getLogger(SsoRmtCmdOperator.class);
	SshRemoteCommandExecutor remExec = new SshRemoteCommandExecutor();
	int exitCode=1;
	String lastResult;
	
	/**
	 * Healthcheck Script to facilitate upgrade. This script tells LITP whether its safe to upgrade or not
	 * @param host
	 * @return
	 */
	public boolean runHealthCheckScript(Host host) {
		log.debug("Running Health Check script");	
		runScript(host, SsoTestDataProvider.getHealthcheckScript());
		if(exitCode==0) {
			log.debug("Script returned 0 with result"+lastResult);
			return true;
		} else {
			log.debug("Script returned "+remExec.getExitCode()+" with result"+lastResult);
			return false;
		}
	}
	
	
	/**
	 * Healthcheck/heatbeat script that monitors current healtyh of SSO. This script is configured as a VCS
	 * 'monitorable' resource and if it detects an error it fails SSO over to teh redundant SC.
	 * @param host
	 * @return
	 */
	public boolean runFailoverHealthCheckScript(Host host) {
		log.debug("Running Health Check script");	
		runScript(host, SsoTestDataProvider.getFailoverHealtheheckScript());
		if(exitCode==0) {
			log.debug("Script returned 0 with result"+lastResult);
			return true;
		} else {
			log.debug("Script returned "+remExec.getExitCode()+" with result"+lastResult);
			return false;
		}
	}
	
	
	public String getLastResult() {
		return lastResult;
	}
	
	private void runScript(Host host, String script) {
		remExec.setHost(host);
		log.debug("Running script:"+script);
		lastResult=remExec.simplExec(script);
		exitCode=remExec.getExitCode();
		log.debug("Exit code:"+exitCode);
	}

	public boolean runBackupScript(Host host, String cmd, String label) {
		String fullCmd = SsoTestDataProvider.getBackupScript()+" "+cmd+" "+label;
		log.debug("Running Backup script");	
		runScript(host, fullCmd);
		if(exitCode==0) {
			log.debug("Script returned 0 with result"+lastResult);
			return true;
		} else {
			log.debug("Script returned "+remExec.getExitCode()+" with result"+lastResult);
			return false;
		}
	}
	
	public boolean runSwitchSsoScript(Host host, String action) {
		String fullCmd = SsoTestDataProvider.getSwitchSso()+" -action "+action;
		log.debug("Running SwitchSsoOnOff  script "+fullCmd);	
		runScript(host, fullCmd);
		if(exitCode==0) {
			log.debug("Script returned 0 with result"+lastResult);
			return true;
		} else {
			log.debug("Script returned "+remExec.getExitCode()+" with result"+lastResult);
			return false;
		}
	}
	
	public boolean runBackupScript(Host host, String cmd) {
		return runBackupScript(host, cmd, "");
	}

	public boolean runBackupScript(Host host) {
		return runBackupScript(host, "", "");
	}
	
	private void backUpEtcHostsFile(SshRemoteCommandExecutor remExec) {
		log.debug("Creating backup of /etc/hosts");
		remExec.simplExec("cp -f /etc/hosts /etc/hosts_bck");
	}

	public boolean disconnectJboss(Host host) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean disconnectLDAP(Host host) {
		
		SshRemoteCommandExecutor remExec = new SshRemoteCommandExecutor();
		remExec.setHost(host);
	
		//First create backup of etc/hosts
		backUpEtcHostsFile(remExec);
		
		//Find LDAP host
		String[] result= remExec.simplExec("grep COM_INF_LDAP_HOST_1 /cluster/parameters/global-SC-1.properties").split("=");
		log.debug("Found LDAP server name :"+result[1]);
		String[] ldapIp= remExec.simplExec("grep "+result[1]+" /etc/hosts").split(" ");
		log.debug("Found LDAP IP address in /etc/hosts :"+ldapIp[0]);
		
		//change etc/hosts file
		log.debug("Changing IP address of LDAP to 10.60.132.76");
		remExec.simplExec("sed -i 's/"+ldapIp[0]+"/10.60.132.76/g' /etc/hosts");
		
		return true;
	}

	public boolean reconnectJboss(Host host) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean reconnectLDAP(Host host) {
		SshRemoteCommandExecutor remExec = new SshRemoteCommandExecutor();
		remExec.setHost(host);
		return returnEtcHostToOrig(remExec);
	}

	private boolean returnEtcHostToOrig(SshRemoteCommandExecutor remExec) {
		log.debug("Returning /etc/hosts back to original");
		remExec.simplExec("cp -f /etc/hosts_bck /etc/hosts");
		
		if(remExec.getExitCode()==0) {
			log.debug("Returning /etc/hosts back to original worked so deleting backup");
			remExec.simplExec("rm -f /etc/hosts_bck");
			return true;
		} else {
			log.warn("Returning /etc/hosts back to original failed.");
			return false;
		}
	}

}

