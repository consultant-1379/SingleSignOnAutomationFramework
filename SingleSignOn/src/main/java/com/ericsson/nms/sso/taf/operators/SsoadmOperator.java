package com.ericsson.nms.sso.taf.operators;


import java.util.HashMap;
import java.util.Map;
import org.jfree.util.Log;
import com.ericsson.cifwk.taf.GenericOperator;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.nms.sso.taf.handlers.SsoadmHandler;

public class SsoadmOperator implements GenericOperator {

	public static final String DEFAULTMAXSESSIONTIME = "600";
	public static final String DEFAULTMAXIDLETIME = "60";
	public static final String TESTMAXSESSIONTIME = "15";
	public static final String TESTMAXIDLETIME = "10";
	public static final String IPLANET_AM_SESSION_MAX_IDLE_TIME = "iplanet-am-session-max-idle-time";
	public static final String IPLANET_AM_SESSION_MAX_SESSION_TIME = "iplanet-am-session-max-session-time";
	String service="iPlanetAMSessionService";
	SsoadmHandler ssoadm;
	
	
	public void setHost(Host host) {
		ssoadm = new SsoadmHandler(host, service);
	}
	
	public Map<String,String> getRealmSvcAttrs() {
		return ssoadm.getRealmSvcAttrs();
	}
	
	public int getTimeoutPeriod() {
		int timeout=Integer.parseInt(ssoadm.getRealmSvcAttrs().get(IPLANET_AM_SESSION_MAX_SESSION_TIME));
		Log.info("Session Timeout period="+timeout);
		return timeout;
	}

	public int getInactivityPeriod() {
		int idle=Integer.parseInt(ssoadm.getRealmSvcAttrs().get(IPLANET_AM_SESSION_MAX_IDLE_TIME));
		Log.info("Session Timeout period="+idle);
		return idle;
	}

	public boolean setTimeoutsForTest() {
		Map<String,String> testParams = new HashMap<>();
		testParams.put(IPLANET_AM_SESSION_MAX_IDLE_TIME, TESTMAXIDLETIME);
		testParams.put(IPLANET_AM_SESSION_MAX_SESSION_TIME, TESTMAXSESSIONTIME);
		return ssoadm.setRealmSvcAttrs(testParams);
	}

	public boolean setDefaultTimeouts() {
		Map<String,String> testParams = new HashMap<>();
		testParams.put(IPLANET_AM_SESSION_MAX_IDLE_TIME, DEFAULTMAXIDLETIME);
		testParams.put(IPLANET_AM_SESSION_MAX_SESSION_TIME, DEFAULTMAXSESSIONTIME);
		return ssoadm.setRealmSvcAttrs(testParams);
	}

}
