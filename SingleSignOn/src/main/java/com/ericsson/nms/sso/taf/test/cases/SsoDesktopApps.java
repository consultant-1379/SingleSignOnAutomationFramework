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
import com.ericsson.nms.sso.taf.operators.ui.SsoUiCitrixOperator;
import com.ericsson.nms.sso.taf.operators.ui.SsoUiOperator;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;

/**
*
*	Class to execute tests against SSODesktopApps
**/
public class SsoDesktopApps extends TorTestCaseHelper implements TestCase{

	SsoUiOperator uiOperator = new SsoUiOperator();
	SsoUiCitrixOperator uiCitrixOperator = new SsoUiCitrixOperator();
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
	/* SSO_DesktopApps_Func_1
	/* @DESCRIPTION Desktop Apps single login
	/* @PRE Need valid COM-INF account
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"desktop"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_DesktopApps_Func_1(Host host){
		setTestcase("TORFTUISSO-259_Func_1","SSO_DesktopApps_Func_1");

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		
		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
	
        setTestStep("Verify user is logged into Launcher");
        assertEquals(launcherPage.getTitle(),SsoTestDataProvider.getLauncherPageTitle());


		setTestStep("Launch UAS desktop app");
		launcherPage.clickCIFAMLinkLink();


		setTestStep("Verify the app launches without prompting for credentials");
		setTestStep("Verify No login screen is presented.");
		assertEquals(false,uiCitrixOperator.isCitrixLoginScreenPresent());
		
		setTestStep("Clean up - exit citrix application.");
		assertTrue(uiCitrixOperator.exitCitrixApp());

	}

	@Setup 
	void prepareTestCaseForTORFTUISSO259_Func_2(){
		//TODO Need valid COM-INF account
	} 

	/**
	/* SSO_DesktopApps_Func_2
	/* @DESCRIPTION Desktop Apps Non-TOR login
	/* @PRE Need valid COM-INF account
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"desktop"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_DesktopApps_Func_2(Host host){
		setTestcase("TORFTUISSO-259_Func_2","SSO_DesktopApps_Func_2");

		setTestStep("Attempt to login to TOR Web application");
		uiOperator.gotoPage(host, SsoTestDataProvider.getCitrixAppURL());

		setTestStep("Verify that user is brought to Launcher Login Page.");
		assertEquals(uiOperator.getHTMLTitle(),SsoTestDataProvider.getLauncherLoginPageTitle());
	}


	/**
	/* SSO_DesktopApps_Func_3
	/* @DESCRIPTION Desktop Apps Single logout
	/* @PRE
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (enabled=false, groups = {"desktop"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_DesktopApps_Func_3(Host host){
		setTestcase("TORFTUISSO-259_Func_3","SSO_DesktopApps_Func_3");

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		
		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
	
        setTestStep("Verify user is logged into Launcher");
        assertEquals(launcherPage.getTitle(),SsoTestDataProvider.getLauncherPageTitle());


		setTestStep("Launch UAS desktop app");
		launcherPage.clickCIFAMLinkLink();

		setTestStep("User clicks sign out in the TOR Launcher");
		launcherPage.clickSignOutButton();


		setTestStep("Verify user is logged out");
		//TODO Verify user is logged out	


		setTestStep("User attempts to interact with the citrix session");
		//TODO User attempts to interact with the citrix session	


		setTestStep("Verify user is presented with normal citrix login screen");
		//TODO Verify user is presented with normal citrix login screen	


		setTestStep("User attempts to launch the Citrix session manually with the same .ica file and token");
		//TODO User attempts to launch the Citrix session manually with the same .ica file and token	


		setTestStep("Verify user is presented with normal citrix login screen");
		//TODO Verify user is presented with normal citrix login screen	


		throw new TestCaseNotImplementedException();
	}

	/**
	/* SSO_DesktopApps_Func_4
	/* @DESCRIPTION Desktop Apps Non-Single logout - logging out of citrix does not invalidate the Launcher session
	/* @PRE User has launched a Citrix session through the TOR Launcher, user's session has not timed out
	/* @PRIORITY HIGH
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.UI})
	@Test (groups = {"desktop"},dataProvider="LauncherHost",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_DesktopApps_Func_4(Host host){
		setTestcase("TORFTUISSO-259_Func_4","SSO_DesktopApps_Func_4");

		setTestStep("Go to Launcher Page");  
		LoginPage loginPage = uiOperator.gotoLoginPage(host);
		
		
		setTestStep("Enter username and password"); 
		LauncherPage launcherPage = loginPage.loginAs(host.getUser(UserType.WEB), host.getPass(UserType.WEB));
	
		setTestStep("Launch UAS desktop app");
		launcherPage.clickCIFAMLinkLink();
		

		setTestStep("Wait till after login screen presented.");
		uiCitrixOperator.isCitrixLoginScreenPresent();
		
		setTestStep("Clean up - exit citrix application.");
		uiCitrixOperator.exitCitrixApp();


		setTestStep("Verify user is still logged into TOR Launcher");
        assertEquals(launcherPage.getTitle(),SsoTestDataProvider.getLauncherPageTitle());	


		setTestStep("User launches another (or the same) Citrix app from the TOR  Launcher");
		launcherPage.clickHCAppLink();	


		setTestStep("Wait till after login screen presented.");
		uiCitrixOperator.isCitrixLoginScreenPresent();
		
		setTestStep("Clean up - exit citrix application.");
		assertTrue(uiCitrixOperator.exitCitrixApp());

	}

}