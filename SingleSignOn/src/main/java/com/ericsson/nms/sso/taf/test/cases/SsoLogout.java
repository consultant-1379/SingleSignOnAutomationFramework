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
import com.ericsson.cifwk.taf.exceptions.TestCaseNotImplementedException;
import com.ericsson.nms.sso.taf.data.SsoDataProvider;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher.LauncherPage;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher.LoginPage;
import com.ericsson.nms.sso.taf.operators.ui.SsoUiOperator;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;

/**
*
*	Class to execute tests against SsoLogout
**/
public class SsoLogout extends TorTestCaseHelper implements TestCase {

	SsoUiOperator uiOperator = new SsoUiOperator();
	SsoDataProvider dataProv = new SsoDataProvider();


	@Setup 
	void prepareTestCaseForTORFTUISSO174_Func_1() {
		uiOperator.startBrowser();
	} 
	
	@AfterMethod
	void cleanup() {
		uiOperator.cleanUp();
	}


	/**
	/* Sso_LogoutWeb.1
	/* @DESCRIPTION Verify that by logging out of one web oss application that the user is logged out of all the applications that he/she was logged in to.
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sso_LogoutWeb1(Host host){
		setTestCase("TORFTUISSO-176_Func_1","Sso_LogoutWeb.1");

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		
		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
	
        setTestStep("Verify user is logged into Launcher");
        assertEquals(launcherPage.getTitle(),SsoTestDataProvider.getLauncherPageTitle());
        
		setTestStep("Logout of application");
		launcherPage.signOut();

		setTestStep("Verify user is logged out of all applications.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
	}

	/**
	/* Sso_LogoutWeb.2
	/* @DESCRIPTION Verify that the launcher provides an option for the user to log out from all oss web applications from the same place
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (enabled=false, groups = {"regression"})
	public void sso_LogoutWeb2(){
		setTestCase("TORFTUISSO-176_Func_2","Sso_LogoutWeb.2");

		//setTestStep("Logout of Launcher application");
		//TODO Logout of Launcher application	


		//setTestStep("Verify user is logged out of all applications.");
		//TODO Verify user is logged out of all applications.	


		throw new TestCaseNotImplementedException();
	}


	/**
	/* Sso_LogoutWeb.3
	/* @DESCRIPTION Verify that clicking on log out will redirect me to the login screen.
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sso_LogoutWeb3(Host host){
		setTestCase("TORFTUISSO-176_Func_3","Sso_LogoutWeb.3");

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		
		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
	
        setTestStep("Verify user is logged into Launcher");
        assertEquals(launcherPage.getTitle(),SsoTestDataProvider.getLauncherPageTitle());
        
		setTestStep("Logout of application");
		launcherPage.signOut();
		
		setTestStep("Verify that user is brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
	}

	

	/**
	/* Sso_LogoutWeb.4
	/* @DESCRIPTION Verify that after logging out the additional web applications in other windows or tabs remain as is until user interaction.
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (enabled=false, groups = {"regression"})
	void sso_LogoutWeb4(){
		setTestCase("TORFTUISSO-176_Func_4","Sso_LogoutWeb.4");

		setTestStep("Logout of application");
		//TODO Logout of application	


		setTestStep("Verify other applications remain logged in.");
		//TODO Verify other applications remain logged in.	


		setTestStep("Interact with non logged out applications");
		//TODO Interact with non logged out applications	


		setTestStep("Verify user is then logged out of application.");
		//TODO Verify user is then logged out of application.	


		throw new TestCaseNotImplementedException();
	}

	
	/**
	/* Sso_LogoutWeb.5
	/* @DESCRIPTION Verify that If a user tries to use the back button to go back to the previous application views after logging out, that they will be brought to the login screen with an error message indicating: "Please log in in order to access this page".
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sso_LogoutWeb5(Host host){
		setTestCase("TORFTUISSO-176_Func_5","Sso_LogoutWeb.5");

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
	
        setTestStep("Verify user is logged into Launcher");
        assertEquals(launcherPage.getTitle(),SsoTestDataProvider.getLauncherPageTitle());
        
		setTestStep("Logout of application");
		launcherPage.signOut();
		sleep(2);

		setTestStep("Attempt to navigate Back Page.");
		uiOperator.navigateBack();
		
		setTestStep("Refresh to see if logged in.");
		uiOperator.refreshPage();

		setTestStep("Verify that user is brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
		
		sleep(2);

		setTestStep("Attempt to navigate Back Page again!");
		uiOperator.navigateBack();

		setTestStep("Refresh to see if logged in.");
		uiOperator.refreshPage();
		
		setTestStep("Verify that user is brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
	}
	
	/**
	/* Sso_LogoutWeb.6
	/* @DESCRIPTION 	Verify that If a user tries to navigate to Launcher Page e.g. Categories after logging out, that they will be brought to the login screen with an error message indicating: "Please log in in order to access this page".
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sso_LogoutWeb6(Host host){
		setTestCase("TORFTUISSO-176_Func_6","Sso_LogoutWeb.6");

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		
		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
	
        setTestStep("Verify user is logged into Launcher");
        assertEquals(launcherPage.getTitle(),SsoTestDataProvider.getLauncherPageTitle());
        
		setTestStep("Logout of application");
		launcherPage.signOut();
		sleep(2);

		setTestStep("Attempt to navigate to categories Page.");
		uiOperator.gotoPage(host, SsoTestDataProvider.getLauncherCategoriesURL());	

		setTestStep("Refresh to see if logged in.");
		uiOperator.refreshPage();

		setTestStep("Verify that user is brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
	}

}