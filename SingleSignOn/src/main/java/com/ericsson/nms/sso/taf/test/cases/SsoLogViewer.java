
package com.ericsson.nms.sso.taf.test.cases;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.TestCase;
import se.ericsson.jcat.fw.annotations.Setup;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.nms.sso.taf.data.SsoDataProvider;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher.LauncherPage;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher.LogViewerPage;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher.LoginPage;
import com.ericsson.nms.sso.taf.operators.SsoadmOperator;
import com.ericsson.nms.sso.taf.operators.ui.SsoUiOperator;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;



/**
*
*	Class to execute tests against SsoLogViewer
**/
public class SsoLogViewer extends TorTestCaseHelper implements TestCase{


	SsoUiOperator uiOperator = new SsoUiOperator();
	SsoDataProvider dataProv = new SsoDataProvider();
	SsoadmOperator admOper = new SsoadmOperator();

	@Setup 
	void prepareTestCaseForTORFTUISSO174_Func_1() {
		uiOperator.startBrowser();
	} 
	
	@AfterMethod
	void cleanup() {
		uiOperator.cleanUp();
	}
	/**
	/* sSO_LogViewer_1
	/* @DESCRIPTION Verify that when a user logs in to Launcher and launches LogViewer that no login screen is presented and that user can interact with the application.
	/* @PRE 
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"sso","regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LogViewer_1(Host host){
		setTestcase("TORFTUISSO-60_Func_1","sSO_LogViewer_1");

		setTestStep("Login to Launcher");
		LoginPage loginPage = uiOperator.gotoLoginPage(host);

		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));

		setTestStep("Verify that user is logged in");
		assertEquals(SsoTestDataProvider.getLauncherPageTitle(),launcherPage.getTitle());	


		setTestStep("Launch Logviewer");
		LogViewerPage logViewPage = launcherPage.clickLogViewerLink();


		//Switch to logviewer window
		setTestStep("Switching to Logviewer Window.");
		uiOperator.switchToLogviewerWindow();
		
		
		setTestStep("Verify that LogViewer is launched");
		assertEquals(SsoTestDataProvider.getLogViewerTitle(),logViewPage.getLogViewerTitle());

        setTestStep("Verify URL is using secure https protocol");
        assertTrue(uiOperator.getCurrentUrl().contains("https"));

	}



	/**
	/* sSO_LogViewer_2
	/* @DESCRIPTION Verify that when the user logs out of Launcher that they will be brought to login screen the next time they interact with the LogViewer
	/* @PRE 
	/* @PRIORITY <HIGH | MEDIUM | LOW
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"sso","regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LogViewer_2(Host host){
		setTestcase("TORFTUISSO-60_Func_2","sSO_LogViewer_2");

		setTestStep("Login to Launcher");
		LoginPage loginPage = uiOperator.gotoLoginPage(host);

		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));

		setTestStep("Verify that user is logged in");
		assertEquals(SsoTestDataProvider.getLauncherPageTitle(),launcherPage.getTitle());	

		setTestStep("Launch Logviewer");
		LogViewerPage logViewPage = launcherPage.clickLogViewerLink();

		setTestStep("Switching to Logviewer Window.");
		uiOperator.switchToLogviewerWindow();
		
		setTestStep("Verify that LogViewer is launched");
		assertEquals(SsoTestDataProvider.getLogViewerTitle(),logViewPage.getLogViewerTitle());
		
		setTestStep("Switching back to default Window.");
		uiOperator.switchToDefaultWindow();
		
		setTestStep("SignOut of Launcher");
		launcherPage.signOut();
		
		setTestStep("Switching to Logviewer Window.");
		uiOperator.switchToLogviewerWindow();
		
		setTestStep("Refesh Page");
		uiOperator.refreshPage();
		
		setTestStep("Verify that user is not logged in and brought to Launcher Login Page.");
		assertEquals(SsoTestDataProvider.getLauncherLoginPageTitle(),uiOperator.getHTMLTitle());
		
        setTestStep("Verify URL is using secure https protocol");
        assertTrue(uiOperator.getCurrentUrl().contains("https"));

	}


	/**
	/* sSO_LogViewer_3
	/* @DESCRIPTION Verify that when the user logs out of or exits LogViewer that they are also logged out of Launcher the next time the user interacts with Launcher.
	/* @PRE 
	/* @PRIORITY <HIGH | MEDIUM | LOW
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"sso","regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LogViewer_3(Host host){
		setTestcase("TORFTUISSO-60_Func_3","sSO_LogViewer_3");

		setTestStep("Login to Launcher");
		LoginPage loginPage = uiOperator.gotoLoginPage(host);

		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));

		setTestStep("Verify that user is logged in");
		assertEquals(SsoTestDataProvider.getLauncherPageTitle(),launcherPage.getTitle());	


		setTestStep("Launch Logviewer");
		LogViewerPage logViewPage = launcherPage.clickLogViewerLink();

		setTestStep("Switching to Logviewer Window.");
		uiOperator.switchToLogviewerWindow();
		
		setTestStep("Verify that LogViewer is launched");
		assertEquals(SsoTestDataProvider.getLogViewerTitle(),logViewPage.getLogViewerTitle());
		
		setTestStep("Logout of Logviewer.");
		logViewPage.signOut();
		
		setTestStep("Switching back to default Window.");
		uiOperator.switchToDefaultWindow();
	
		setTestStep("Refesh Page");
		uiOperator.refreshPage();
		
		setTestStep("Verify that user is not logged in and brought to Launcher Login Page.");
		assertEquals(SsoTestDataProvider.getLauncherLoginPageTitle(),uiOperator.getHTMLTitle());
		
        setTestStep("Verify URL is using secure https protocol");
        assertTrue(uiOperator.getCurrentUrl().contains("https"));

	}



	/**
	/* sSO_LogViewer_4
	/* @DESCRIPTION Verify that Logviewer session is not invalidated if user interacts with session within the idle timeout time.
	/* @PRE 
	/* @PRIORITY <HIGH | MEDIUM | LOW
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"sso","regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LogViewer_4(Host host){
		setTestcase("TORFTUISSO-60_Func_4","sSO_LogViewer_4");

		admOper.setHost(host);
		assertTrue(admOper.setTimeoutsForTest());

		setTestStep("Login to Launcher");
		LoginPage loginPage = uiOperator.gotoLoginPage(host);

		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));

		setTestStep("Verify that user is logged in");
		assertEquals(SsoTestDataProvider.getLauncherPageTitle(),launcherPage.getTitle());	

		setTestStep("Launch Logviewer");
		LogViewerPage logViewPage = launcherPage.clickLogViewerLink();

		setTestStep("Switching to Logviewer Window.");
		uiOperator.switchToLogviewerWindow();
		
		setTestStep("Verify that LogViewer is launched");
		assertEquals(SsoTestDataProvider.getLogViewerTitle(),logViewPage.getLogViewerTitle());
		
		setTestStep("Wait half idle timeout period time");
		sleep(admOper.getInactivityPeriod()*60/2);
		
		setTestStep("Interact with browser - refresh");
		uiOperator.refreshPage();
		
		setTestStep("Wait half idle timeout period time");
		sleep(admOper.getInactivityPeriod()*60/2);

		setTestStep("Sleep for another 15 seconds to ensure timings.");
		sleep(15);

		setTestStep("Verify that the session is not invalidated");
		uiOperator.refreshPage();
		
		setTestStep("Verify that user is brought to LogViewer page.");
		assertEquals(SsoTestDataProvider.getLogViewerTitle(),logViewPage.getLogViewerTitle());
		
		setTestStep("Reset timeout values to default.");
		admOper.setDefaultTimeouts();

	}


	/**
	/* sSO_LogViewer_5
	/* @DESCRIPTION Verify that sessions are invalidated after a set amount of time regardless of activity
	/* @PRE 
	/* @PRIORITY <HIGH | MEDIUM | LOW
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"sso","regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LogViewer_5(Host host){
		setTestcase("TORFTUISSO-60_Func_5","sSO_LogViewer_5");

		admOper.setHost(host);
		assertTrue(admOper.setTimeoutsForTest());

		setTestStep("Login to Launcher");
		LoginPage loginPage = uiOperator.gotoLoginPage(host);

		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));

		setTestStep("Verify that user is logged in");
		assertEquals(SsoTestDataProvider.getLauncherPageTitle(),launcherPage.getTitle());	

		setTestStep("Launch Logviewer");
		LogViewerPage logViewPage = launcherPage.clickLogViewerLink();

		setTestStep("Switching to Logviewer Window.");
		uiOperator.switchToLogviewerWindow();
		
		setTestStep("Verify that LogViewer is launched");
		assertEquals(SsoTestDataProvider.getLogViewerTitle(),logViewPage.getLogViewerTitle());
		
		setTestStep("Wait timeout period time");
		sleep(admOper.getTimeoutPeriod()*60);

		setTestStep("Sleep for another 15 seconds to ensure timings.");
		sleep(15);

		setTestStep("Verify that the session is invalidated");
		uiOperator.refreshPage();
		
		setTestStep("Verify that user is brought to Launcher Login page.");
		assertEquals(SsoTestDataProvider.getLauncherLoginPageTitle(),uiOperator.getHTMLTitle());
		
		setTestStep("Reset timeout values to default.");
		admOper.setDefaultTimeouts();
	}


	/**
	/* sSO_LogViewer_6
	/* @DESCRIPTION Verify that sessions are invalidated after a set amount of inactivity (idle)
	/* @PRE 
	/* @PRIORITY <HIGH | MEDIUM | LOW
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"sso","regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LogViewer_6(Host host){
		setTestcase("TORFTUISSO-60_Func_6","sSO_LogViewer_6");
		
		admOper.setHost(host);
		assertTrue(admOper.setTimeoutsForTest());

		setTestStep("Login to Launcher");
		LoginPage loginPage = uiOperator.gotoLoginPage(host);

		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));

		setTestStep("Verify that user is logged in");
		assertEquals(SsoTestDataProvider.getLauncherPageTitle(),launcherPage.getTitle());	

		setTestStep("Launch Logviewer");
		LogViewerPage logViewPage = launcherPage.clickLogViewerLink();

		setTestStep("Switching to Logviewer Window.");
		uiOperator.switchToLogviewerWindow();
		
		setTestStep("Verify that LogViewer is launched");
		assertEquals(SsoTestDataProvider.getLogViewerTitle(),logViewPage.getLogViewerTitle());
		
		setTestStep("Wait timeout period time");
		sleep(admOper.getInactivityPeriod()*60);

		setTestStep("Sleep for another 15 seconds to ensure timings.");
		sleep(15);

		setTestStep("Verify that the session is invalidated");
		uiOperator.refreshPage();
		
		setTestStep("Verify that user is brought to Launcher Login page.");
		assertEquals(SsoTestDataProvider.getLauncherLoginPageTitle(),uiOperator.getHTMLTitle());
		
		setTestStep("Reset timeout values to default.");
		admOper.setDefaultTimeouts();
	}
	
}