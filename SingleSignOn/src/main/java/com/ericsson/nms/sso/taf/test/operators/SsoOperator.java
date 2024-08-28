package com.ericsson.nms.sso.taf.test.operators;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.GenericOperator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.utils.csv.CsvWriter;
import com.ericsson.nms.sso.taf.getters.rest.SsoRestGetter;
import com.ericsson.nms.sso.taf.handlers.LocalCommandExecutor;
import com.ericsson.nms.sso.taf.utils.ssoUtils;

public class SsoOperator implements GenericOperator {
	
	static Logger log = Logger.getLogger(SsoOperator.class);
	LocalCommandExecutor cmdExe = new LocalCommandExecutor();
	 
	public boolean isCitrixLoginWindowProcRunning(){
		return isCitrixAppProcRunning(SsoRestGetter.getCitrixLoginWindowProcName());
	}

	public boolean isCitrixAppProcRunning(String appName) {
		List<String> procslist=getCitrixProcsRunning();
		Iterator<String> iterator = procslist.iterator(); 
		
		while(iterator.hasNext()){
			if(iterator.next().equals(appName)) {
				log.info("Citrix process "+appName+" running.");
				return true;
			}
		}
		
		log.info("Citrix process "+appName+" not found.");
		return false;
	}

	
	/**
	 * @return List of windows titles of running citrix applications.
	 */
	private List<String> getCitrixProcsRunning() {
		List<String> procslist = new ArrayList<String>();
		
		cmdExe.execute("tasklist /v /fo CSV /nh /fi \"IMAGENAME eq wfica32.exe\"");
		String output=cmdExe.getStdOut();
		String procs[]=output.split("##");
		log.debug("Number of citrix apps:"+procs.length);
		
		for(int i=0; i<procs.length; i++) {
			String vals[]=procs[i].split(",");
			procslist.add(vals[9]);
			log.debug("Window Title of citrix app:"+vals[9]);
		}
		return procslist;
	}

	
	public void createExecutionTimesGraph(List<Long> executionTimes) {
		
		File chartFile = new File(DataHandler.getAttribute("logdir") + "/chartOfResponseTimes_"+ssoUtils.generateRandomString(8));
		log.info("Creating chartCSV file: "+chartFile.getAbsolutePath());
		
		CsvWriter writer = new CsvWriter(chartFile.getAbsolutePath());
		writer.write(executionTimes.toString());

		//log.debug("Creating chart.");
		//ChartWriter chart = new ChartWriter(chartFile);
		//chart.saveToHtmlLog(chart.plot());
		log.info("Not Creating chart.");
		
	}

	public String createLabel() {
		return "TestBackup_"+ssoUtils.generateRandomString(5);
	}

	
}
