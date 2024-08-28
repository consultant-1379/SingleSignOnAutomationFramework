package com.ericsson.nms.sso.taf.test.cases;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import se.ericsson.jcat.fw.annotations.Setup;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.nms.sso.taf.data.SsoDataProvider;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher.LauncherPage;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher.LoginPage;
import com.ericsson.nms.sso.taf.operators.ui.SsoUiOperator;
import com.ericsson.nms.sso.taf.operators.ui.SsoUiOperator.Protocol;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;

/**
*
*	Class to execute tests against sso
**/
public class SsoLogin extends TorTestCaseHelper implements TestCase{

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
	/* SSO_LoginWeb_1
	/* @DESCRIPTION Verify that the user is brought to the login screen when they attempt to login to any web application when not logged in.
	/* @PRE Get name of TOR Web Application
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_1(Host host){
		setTestCase("TORFTUISSO-174_Func_1","SSO_LoginWeb_1");

		setTestStep("Attempt to login to TOR Web application");
		uiOperator.gotoPage(host,SsoTestDataProvider.getWebAppUrl());

		setTestStep("Verify that user is brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
	}

	/**
	/* SSO_LoginWeb_2
	/* @DESCRIPTION Verify that the user is brought to the login screen when they attempt to use the Launcher when not logged in.
	/* @PRE None
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_2(Host host){
		setTestCase("TORFTUISSO-174_Func_2","SSO_LoginWeb_2");

		setTestStep("Attempt to access launcher page when not logged into Launcher");
		uiOperator.gotoPage(host, SsoTestDataProvider.getLauncherCategoriesURL());

		setTestStep("Verify that user is brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
	}
	
	/**
	/* SSO_LoginWeb_5
	/* @DESCRIPTION Verify that the user is brought to the login screen when they attempt to access TOR web application when not logged in.
	/* @PRE 
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void SSO_LoginWeb_5(Host host){
		setTestCase("TORFTUISSO-174_Func_5","SSO_LoginWeb_5");

		setTestStep("Attempt to login to Launcher without login extension");
		uiOperator.gotoPage(host, SsoTestDataProvider.getLogViewerURL());

		setTestStep("Verify that user is brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
		
		setTestStep("Attempt to login to Launcher container");
		uiOperator.gotoPage(host, SsoTestDataProvider.getLauncherURI(), Protocol.HTTP);

		setTestStep("Verify that user is brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
	}

	/**
	/* SSO_LoginWeb_6
	/* @DESCRIPTION Verify that the user can login to Launcher using valid username and password.
	/* @PRE None
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_6(Host host){
		setTestCase("TORFTUISSO-174_Func_6","SSO_LoginWeb_6 on " + host);
	
		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		
		setTestStep("Enter username and password"); 
		//LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
		
        setTestStep("Verify user is logged into Launcher");
        assertEquals(launcherPage.getTitle(),SsoTestDataProvider.getLauncherPageTitle());
        
        setTestStep("Verify URL is using secure https protocol");
        assertTrue(uiOperator.getCurrentUrl().contains("https"));
        
	}
	
	/**
	/* SSO_LoginWeb_7
	/* @DESCRIPTION Verify that the user cannot login to Launcher using invalid username and password.
	/* @PRE None
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_7(Host host){
		setTestCase("TORFTUISSO-174_Func_7","SSO_LoginWeb_7");

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
	
		setTestStep("Enter invalid username and invalid password"); 
		loginPage.loginFail(SsoDataProvider.generateRandomString(8), SsoDataProvider.generateRandomString(8));
	
		setTestStep("Verify that user is not logged in and brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
	}
	
	/**
	/* SSO_LoginWeb_8
	/* @DESCRIPTION Verify that the user cannot login to Launcher using invalid username and valid password.
	/* @PRE None
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_8(Host host){
		setTestCase("TORFTUISSO-174_Func_8","SSO_LoginWeb_8");

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
	
		setTestStep("Enter invalid username and password"); 
		loginPage.loginFail(SsoDataProvider.generateRandomString(8), host.getPass());
	
		setTestStep("Verify that user is not logged in and brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
	}
	
	/**
	/* SSO_LoginWeb_9
	/* @DESCRIPTION Verify that the user cannot login to Launcher using valid username and invalid password.
	/* @PRE None
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_9(Host host){
		setTestCase("TORFTUISSO-174_Func_9","SSO_LoginWeb_9");

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
	
		setTestStep("Enter username and  invalid password"); 
		loginPage.loginFail(host.getUser(),SsoDataProvider.generateRandomString(8));
	
		setTestStep("Verify that user is not logged in and brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
	}
	
	/**
	/* SSO_LoginWeb_10
	/* @DESCRIPTION Verify that the user cannot login to Launcher using blank username and password.
	/* @PRE None
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_10(Host host){
		setTestCase("TORFTUISSO-174_Func_10","SSO_LoginWeb_10");

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
	
		setTestStep("Enter blank username and blank password"); 
		loginPage.loginFail("","");
	
		setTestStep("Verify that user is not logged in and brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
	}
	
	/**
	/* SSO_LoginWeb_Sec_1
	/* @DESCRIPTION Verify that the user cannot login using http
	/* @PRE None
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"regression"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_LoginWeb_Sec_1(Host host){
		setTestCase("TORFTUISSO-174_Secu_5","SSO_LoginWeb_Sec_1");

		setTestStep("Go to Launcher Page");  
		setTestStep("Attempt to access unsecure login page when not logged in");
		LoginPage loginPage = uiOperator.gotoLoginPage(host, SsoTestDataProvider.getLauncherURI(), Protocol.HTTP);
		
        setTestStep("Verify user cannot get to login screen");
        assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
        
        setTestStep("Verify URL is using secure https protocol");
        assertTrue(uiOperator.getCurrentUrl().contains("https"));

		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
		
        setTestStep("Verify user is logged into Launcher");
        assertEquals(launcherPage.getTitle(),SsoTestDataProvider.getLauncherPageTitle());
        
        setTestStep("Verify URL is using secure https protocol");
        assertTrue(uiOperator.getCurrentUrl().contains("https"));

	}

}