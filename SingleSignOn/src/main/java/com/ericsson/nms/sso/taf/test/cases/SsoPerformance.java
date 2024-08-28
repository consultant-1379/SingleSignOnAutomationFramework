package com.ericsson.nms.sso.taf.test.cases;

import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.exceptions.TestCaseNotImplementedException;
import com.ericsson.nms.sso.taf.data.SsoDataProvider;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher.LauncherPage;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher.LoginPage;
import com.ericsson.nms.sso.taf.operators.SsoOperator;
import com.ericsson.nms.sso.taf.operators.rest.SsoRestOperator;
import com.ericsson.nms.sso.taf.operators.ui.SsoUiOperator;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;

/**
*
*	Class to execute tests against sso
**/
public class SsoPerformance extends TorTestCaseHelper implements TestCase {

	SsoUiOperator uiOperator = new SsoUiOperator();
	SsoDataProvider dataProv = new SsoDataProvider();
	SsoRestOperator RestOperator = new SsoRestOperator();
	SsoOperator genOperator = new SsoOperator();

	/**
	/* SSO_LoginWeb_Perf_1
	/* @DESCRIPTION Verify that 500 users can log in to the launcher in parallel.
	/* @PRE Get User
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1000})
	@Context(context = {Context.REST})
	@Test (groups = {"performance"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_Perf_1(Host host){
		setTestcase("TORFTUISSO-174_Perf_5","SSO_LoginWeb_Perf_1");

		setTestStep("Launch 1000 parallel login sessions to Launcher");
		
		RestOperator.setup(host);
		
/*		setTestStep("Go to Launcher Page");   
		List<String> htmlTitle = RestOperator.makeGETRequest(SsoTestDataProvider.getLauncherURI());
		assertTrue(RestOperator.verifyLauncherLoginPageTitle(htmlTitle));
*/		
		setTestStep("Verify request returns anthentication token.");
		RestOperator.makePOSTRequest(SsoTestDataProvider.getLauncherURI(),RestOperator.setWebAuthenticateParams(host.getUser(UserType.WEB), host.getPass(UserType.WEB)));
		assertTrue(RestOperator.verifyAuthTokenPresent());
		
		//Now save execution times to file and create graph.
		genOperator.createExecutionTimesGraph(RestOperator.getExecutionTimes());
	}

	
	/**
	/* SSO_LoginWeb_Robust_1
	/* @DESCRIPTION Verify continuous login and logout to the launcher over a 7 day period. 
	/* @PRE Get User credentials
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"performance"},dataProvider="stressTester",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_Robust_1(Host host, int iterations){
			
			setTestcase("TORFTUISSO-174_Rbst_2","SSO_LoginWeb_Robust_1_Iteration:"+iterations);
			uiOperator.startBrowser();
			
			setTestStep("Go to Launcher Page");  
			LoginPage loginPage = uiOperator.gotoLoginPage(host);
			
			setTestStep("Enter username and password"); 
			LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
			
	        setTestStep("Verify user is logged into Launcher");
	        assertEquals(launcherPage.getTitle(),SsoTestDataProvider.getLauncherPageTitle());
	        
			setTestStep("Logout of application");
			launcherPage.signOut();
			
			uiOperator.cleanUp();		

	}
	
	/**
	/* SSO_LoginWeb_Robust_4
	/* @DESCRIPTION Max number of open sessions - 100000
	/* @PRE Get User credentials
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.REST})
	@Test (groups = {"performance"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_Robust_4(Host host){
			setTestcase("TORFTUISSO-174_Robu_4","SSO_LoginWeb_Robust_4");

			List<Long> execTime=new ArrayList<>();
			
			setTestStep("Launch login request");
			RestOperator.setup(host);
			
            // Leave one session for ssoadm!
			for(int i=1;i<100000;i++) {
				setTestStep("Go to Launcher Page");   
				
				RestOperator.makePOSTRequest(SsoTestDataProvider.getLauncherURI(),RestOperator.setWebAuthenticateParams(host.getUser(), host.getPass()));
				assertTrue(RestOperator.verifyAuthTokenPresent());

				execTime.add(RestOperator.getExecutionTimes().get(0));
			}
			
			//Now save execution times to file and create graph.
			genOperator.createExecutionTimesGraph(execTime);
			
	
	}


	/**
	/* SSO_LoginWeb_Robust_2
	/* @DESCRIPTION Hit OpenAM directly - Continuous login and logout to OpenAM server over a 7 day period.
	/* @PRE Get User
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.REST})
	@Test (groups = {"performance"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_Robust_2(Host host){
		setTestcase("TORFTUISSO-174_Robu_2","SSO_LoginWeb_Robust_2");

		setTestStep("Make direct request to OpenAM with Username and password.");
		
		RestOperator.setup(host);
		
		List<String> responses=RestOperator.makePOSTRequest(SsoTestDataProvider.getOpenAMAuthURL(),RestOperator.setOpenAMAuthenticateParams(host.getUser(), host.getPass()));
		
		setTestStep("Verify Token is returned.");
		assertTrue(RestOperator.verifyTokenPresent(responses));
		
		setTestStep("Send response back to logout.");
		String token=RestOperator.getTokens().get(0);
		RestOperator.makePOSTRequest(SsoTestDataProvider.getOpenAMLogoutURL(), RestOperator.setOpenAMLogoutParams(token));
		
		//Now save execution times to file and create graph.
		genOperator.createExecutionTimesGraph(RestOperator.getExecutionTimes());
	}
	
	/**
	/* SSO_LoginWeb_Robust_3
	/* @DESCRIPTION Hit OpenAM directly - Continuous login and logout of 100 users to OpenAm server over hour - high rate
	/* @PRE Get User
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {100})
	@Context(context = {Context.REST})
	@Test (enabled=false, groups = {"performance"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_Robust_3(Host host){
		setTestcase("TORFTUISSO-174_Robu_3","SSO_LoginWeb_Robust_3");

		RestOperator.setup(host);
		
		List<String> responses=RestOperator.makeGETRequest(SsoTestDataProvider.getOpenAMAuthURL(),RestOperator.setOpenAMAuthenticateParams(host.getUser(), host.getPass()));
		
		setTestStep("Verify Token is returned.");
		assertTrue(RestOperator.verifyTokenPresent(responses));
		
		setTestStep("Send response back to logout.");
		String token=RestOperator.getTokens().get(0);
		RestOperator.makeGETRequest(SsoTestDataProvider.getOpenAMLogoutURL(), RestOperator.setOpenAMLogoutParams(token));
	}

	/**
	/* SSO_Timeout_Perf.1
	/* @DESCRIPTION Verify 200 sessions timing out
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {200})
	@Context(context = {Context.REST,Context.UI})
	@Test (enabled=false, groups = {"performance"})
	void sSO_Timeout_Perf1(){
		setTestcase("TORFTUISSO-178_Robu_1","SSO_Timeout_Perf.1");

		setTestStep("<test step goes here>");
		//TODO <test step goes here>	


		setTestStep("<verify step goes here>");
		//TODO <verify step goes here>	


		throw new TestCaseNotImplementedException();
	}
	
	/**
	/* SSO_TORTimeout_Perf.1
	/* @DESCRIPTION Verify 500 sessions timing out
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.REST})
	@Test (enabled=false, groups = {"timeout"})
	void sSO_TORTimeout_Perf1(){
		setTestcase("TORFTUISSO-127_Robu_1","SSO_TORTimeout_Perf.1");

		setTestStep("<test step goes here>");
		//TODO <test step goes here>	


		setTestStep("<verify step goes here>");
		//TODO <verify step goes here>	


		throw new TestCaseNotImplementedException();
	}

}