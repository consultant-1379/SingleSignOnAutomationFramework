package com.ericsson.nms.sso.taf.handlers;

import java.io.BufferedReader;

import java.io.InputStreamReader;

public class LocalCommandExecutor  {

	String stdOut="";
	String errOut;
	int exitCode;
	
	//@Override
	public String simplExec(String cmdWithArgs) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public String simplExec(String cmdWithOutArgs, String... arguments) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public boolean execute(String cmdWithArgs) {

		 try {
		      String line;
		      Process p = Runtime.getRuntime().exec("cmd /c "+cmdWithArgs);
		      BufferedReader bri = new BufferedReader
		        (new InputStreamReader(p.getInputStream()));
		      BufferedReader bre = new BufferedReader
		        (new InputStreamReader(p.getErrorStream()));
		      
		      while ((line = bri.readLine()) != null) {
		    	  stdOut += line +"##";
		      }
		      bri.close();
		      while ((line = bre.readLine()) != null) {
		        errOut += line;
		      }
		      bre.close();
		      p.waitFor();

		    }
		    catch (Exception err) {
		      err.printStackTrace();
		    }
		return false;
	}

	//@Override
	public boolean execute(String cmdWithOutArgs, String... arguments) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public String getStdOut() {
		return stdOut;
	}

	//@Override
	public String getErrOut() {	
		return errOut;
	}

	//@Override
	public int getExitCode() {
		return 0;
	}

}
