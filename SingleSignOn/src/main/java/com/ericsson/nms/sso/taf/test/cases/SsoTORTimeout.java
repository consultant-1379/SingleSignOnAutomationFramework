package com.ericsson.nms.sso.taf.test.cases;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.TestCase;
import se.ericsson.jcat.fw.annotations.Setup;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.exceptions.TestCaseNotImplementedException;
import com.ericsson.nms.sso.taf.data.SsoDataProvider;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher.LoginPage;
import com.ericsson.nms.sso.taf.operators.SsoadmOperator;
import com.ericsson.nms.sso.taf.operators.ui.SsoUiOperator;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;



/**
*
*	Class to execute tests against SsoTORTimeout
**/
public class SsoTORTimeout extends TorTestCaseHelper implements TestCase{

	SsoUiOperator uiOperator = new SsoUiOperator();
	SsoDataProvider dataProv = new SsoDataProvider();
	SsoadmOperator admOper = new SsoadmOperator();
	static Logger log = Logger.getLogger(SsoTORTimeout.class);
	
	@Setup 
	void prepareTestCaseForTORFTUISSO127_Func_1(){
		uiOperator.startBrowser();
		//uiOperator.setTimeoutToTestDefault();
	} 

	@AfterMethod
	void cleanup() {
		uiOperator.cleanUp();
		admOper.setDefaultTimeouts();
	}
	
	/**
	/* SSO_TORTimeout.6
	/* @DESCRIPTION Verify that the time of the session time out is configurable.
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_TORTimeout6(Host host){
		setTestcase("TORFTUISSO-127_Func_6","SSO_TORTimeout.6");

		setTestStep("Set Test Timeouts."); 
		admOper.setHost(host);
		assertTrue(admOper.setTimeoutsForTest());
		
		setTestStep("Verify they have been set");
		assertEquals(Integer.parseInt(SsoadmOperator.TESTMAXIDLETIME), admOper.getInactivityPeriod());
		assertEquals(Integer.parseInt(SsoadmOperator.TESTMAXSESSIONTIME), admOper.getTimeoutPeriod());
		
		setTestStep("Set Timeouts to default."); 
		assertTrue(admOper.setDefaultTimeouts());
		
		setTestStep("Verify they have been set to default");
		assertEquals(Integer.parseInt(SsoadmOperator.DEFAULTMAXIDLETIME), admOper.getInactivityPeriod());
		assertEquals(Integer.parseInt(SsoadmOperator.DEFAULTMAXSESSIONTIME), admOper.getTimeoutPeriod());

	}
	
	/**
	/* SSO_TORTimeout.1
	/* @DESCRIPTION Verify that sessions are invalidated after a set amount of time regardless of activity
	/* @PRE Use ssoadm command line tool to set session timeout to a testable time (not too long)
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_TORTimeout1(Host host){
		setTestcase("TORFTUISSO-127_Func_1","SSO_TORTimeout.1");
		
		setTestStep("Set Test Timeouts.");  
		admOper.setHost(host);
		assertTrue(admOper.setTimeoutsForTest());

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		
		setTestStep("Enter username and password"); 
		loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
		
		setTestStep("Interact with application for half the timeout period time");
		uiOperator.interact((int) Math.floor(admOper.getTimeoutPeriod() / 2));
		
		setTestStep("Sleep for another 15 seconds to ensure timings.");
		sleep(15);

		setTestStep("Reload the page");
		uiOperator.refreshPage();

		setTestStep("Verify that the session is not yet invalidated");
		assertEquals(SsoTestDataProvider.getLauncherPageTitle(),uiOperator.getHTMLTitle());

		setTestStep("Interact with application for the rest of the timeout period time");
		uiOperator.interact((int) Math.floor(admOper.getTimeoutPeriod() / 2) + 1);
		
		setTestStep("Sleep for another 15 seconds to ensure timings.");
		sleep(15);

		setTestStep("Verify that user is brought to Launcher Login page.");
		assertEquals(SsoTestDataProvider.getLauncherLoginPageTitle(),uiOperator.getHTMLTitle());
	}



	/**
	/* SSO_TORTimeout.2
	/* @DESCRIPTION Verify that sessions are invalidated after a set amount of inactivity (idle)
	/* @PRE Use ssoadm command line tool to set idle timeout to a testable time (not too long)
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_TORTimeout2(Host host){
		setTestcase("TORFTUISSO-127_Func_2","SSO_TORTimeout.2");
		
		setTestStep("Set Test Timeouts.");  
		admOper.setHost(host);
		assertTrue(admOper.setTimeoutsForTest());

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		
		setTestStep("Enter username and password"); 
		loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
		
		setTestStep("Wait timeout period time");
		sleep(admOper.getInactivityPeriod()*60);

		setTestStep("Sleep for another 15 seconds to ensure timings.");
		sleep(15);

		setTestStep("Verify that the session is invalidated");
		uiOperator.refreshPage();
		
		setTestStep("Verify that user is brought to Launcher Login page.");
		assertEquals(SsoTestDataProvider.getLauncherLoginPageTitle(),uiOperator.getHTMLTitle());
	}


	/**
	/* SSO_TORTimeout.3
	/* @DESCRIPTION Verify that sessions are not invalidated if user interacts with session within the idle timeout time
	/* @PRE Use ssoadm command line tool to set idle timeout to a testable time (not too long)
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_TORTimeout3(Host host){
		setTestcase("TORFTUISSO-127_Func_3","SSO_TORTimeout.3");

		setTestStep("Set Test Timeouts.");  
		admOper.setHost(host);
		assertTrue(admOper.setTimeoutsForTest());
		
		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		setTestStep("Enter username and password"); 
		loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
		
		setTestStep("Interact with TOR application within the inactivity period");
		sleep(admOper.getInactivityPeriod()*60/2);
		uiOperator.refreshPage();

		setTestStep("Interact with TOR application again to make sure");
		uiOperator.refreshPage();

		setTestStep("Verify that the session is still valid");
		assertEquals(SsoTestDataProvider.getLauncherPageTitle(),uiOperator.getHTMLTitle());
		
		setTestStep("Now sleep for the rest of the inactivity period");
		sleep( ( admOper.getInactivityPeriod()*60/2) - 40 );
		
		
		setTestStep("Refresh page to ensure not logged out.");
		uiOperator.refreshPage();
		
		setTestStep("Verify that the session is not invalidated");
		assertEquals(SsoTestDataProvider.getLauncherPageTitle(),uiOperator.getHTMLTitle());


	}


	/**
	/* SSO_TORTimeout.4
	/* @DESCRIPTION Verify that clicking on a link/button or refreshing the page of  TOR application that has timed out will redirect the user to the login page
	/* @PRE Use ssoadm command line tool to set idle timeout to a testable time (not too long)
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_TORTimeout4(Host host){
		setTestcase("TORFTUISSO-127_Func_4","SSO_TORTimeout.4");

		setTestStep("Set Test Timeouts."); 
		admOper.setHost(host);
		assertTrue(admOper.setTimeoutsForTest());
		
		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		
		setTestStep("Enter username and password"); 
		loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
		
		setTestStep("Wait timeout period time");
		log.info("Sleeping for " + ( admOper.getInactivityPeriod()*60 ) + " seconds" );
		sleep(admOper.getInactivityPeriod()*60);

		setTestStep("Sleep another 60 to ensure timings.");
		sleep(60);


		setTestStep("Verify that the session is invalidated");
		uiOperator.refreshPage();
		
		setTestStep("Verify that user is brought to Launcher Login page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
		
		
	}



	/**
	/* SSO_TORTimeout.5
	/* @DESCRIPTION Verify that when timeout settings are set that it only applies to new sessions and that previous settings apply to current session.
	/* @PRE Use ssoadm command line tool to set idle timeout to a testable time (not too long)
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (enabled=false, groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_TORTimeout5(Host host){
		setTestcase("TORFTUISSO-127_Func_5","SSO_TORTimeout.5");

		setTestStep("Set Test Timeouts."); 
		admOper.setHost(host);
		assertTrue(admOper.setTimeoutsForTest());
		
		
		setTestStep("Wait timeout period time");
		//TODO Wait timeout period time	


		setTestStep("Verify that the session is invalidated");
		//TODO Verify that the session is invalidated	


		throw new TestCaseNotImplementedException();
	}


	/**
	/* SSO_TORTimeout.7
	/* @DESCRIPTION Verify that a session timeout for one session does not affect another session
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (enabled=false, groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_TORTimeout7(Host host){
		setTestcase("TORFTUISSO-127_Func_7","SSO_TORTimeout.7");
		SsoUiOperator uiOperator2 = new SsoUiOperator();
		uiOperator2.startBrowser();
		
		setTestStep("Set Test Timeouts."); 
		admOper.setHost(host);
		assertTrue(admOper.setTimeoutsForTest());
		
		setTestStep("Go to Launcher Page: Browser 1");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		
		setTestStep("Enter username and password: Browser 1"); 
		loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
	

		setTestStep("Go to Launcher Page: Browser 2");  
		LoginPage loginPage2 = uiOperator2.gotoLoginPage(host);
		
		
		setTestStep("Enter username and password: Browser 2"); 
		loginPage2.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));

		setTestStep("Interact with TOR application within the inactivity period");
		sleep(admOper.getInactivityPeriod()*60/2);
		uiOperator.refreshPage();

	}
    
}