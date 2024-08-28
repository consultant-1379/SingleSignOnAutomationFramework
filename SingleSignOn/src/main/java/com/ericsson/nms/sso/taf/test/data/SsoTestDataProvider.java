package com.ericsson.nms.sso.taf.test.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ericsson.cifwk.taf.TestData;
import org.testng.annotations.DataProvider;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.nms.sso.taf.data.SsoDataProvider;
import com.ericsson.nms.sso.taf.handlers.ApacheServiceHandler;

public class SsoTestDataProvider implements TestData {

	static SsoDataProvider dataprovider = new SsoDataProvider();
	
	@DataProvider(name="LauncherHost")
	public static Object[][] LauncherHost() {
		Object[][] activeHost = {{ ApacheServiceHandler.getActiveApache(SsoDataProvider.getSC1(),SsoDataProvider.getSC2()) }};
		return activeHost;
	}
	
	@DataProvider(name="InactiveLauncherHost")
	public static Object[][] InactiveLauncherHost() {
		Object[][] activeHost = {{ ApacheServiceHandler.getInactiveApache(SsoDataProvider.getSC1(),SsoDataProvider.getSC2()) }};
		return activeHost;
	}
		
	@DataProvider(name="ShelfControllers")
	public static Object[][] ShelfControllers() {
		Object[][] result = {{SsoDataProvider.getSC1(), SsoDataProvider.getSC2()}};
		return result;
	}
	
	@DataProvider(name="SC1WebHost")
	public static Object[][] SC1WebHost() {
		Object[][] result = {{SsoDataProvider.getSC1()}};
		return result;
	}
    
    @DataProvider(name="PeerNodes")
    public static Object[][] PeerNodes() {
//        Object[][] result = {{SsoDataProvider.getPeerNode1(), SsoDataProvider.getPeerNode2()}};
        Object[][] result = {{SsoDataProvider.getPeerNode1()}};
        return result;
    }
	
	@DataProvider(name="SC2WebHost")
	public static Object[][] SC2WebHost() {
		Object[][] result = {{SsoDataProvider.getSC2()}};
		return result;
	}
	
	@DataProvider(name="SsoTestData")
	public static Iterator<Object[]> allTestData() {
		List<Host> hosts = SsoDataProvider.getHosts();
		
		List<String> desktopApps = SsoDataProvider.getCitrixAppList();

		//We will be returning an iterator of Object arrays so create that first.
		List<Object[]> dataToBeReturned = new ArrayList<Object[]>();

		//Populate our List of Object arrays with the file content.
		for (String apps : desktopApps)
		{
			dataToBeReturned.add(new Object[] {hosts.get(0), apps } );
		}
		//return the iterator - testng will initialize the test class and calls the 
		//test method with each of the content of this iterator.
		return dataToBeReturned.iterator();

	}
	
	@DataProvider(name="AllLauncherHost")
	public static Iterator<Object[]> allLauncherHosts() {
		List<Host> hosts = SsoDataProvider.getHosts();
		
		List<Object[]> dataToBeReturned = new ArrayList<Object[]>();
		
		for(Host host: hosts) {
			dataToBeReturned.add(new Object[] {host});
		}
		return dataToBeReturned.iterator();
	}
	
	@DataProvider(name="stressTester")
	public static Iterator<Object[]> stressTester() {
		int COUNTMAX=1;
		
		if(SsoDataProvider.getProperty("StressTestIterations") != null) {
			COUNTMAX= Integer.parseInt(SsoDataProvider.getProperty("StressTestIterations"));
		} 
		
		List<Object[]> dataToBeReturned = new ArrayList<Object[]>();
		
	
		for(int count=1; count<=COUNTMAX; count++) {
			dataToBeReturned.add(new Object[] {ApacheServiceHandler.getActiveApache(SsoDataProvider.getSC1(),SsoDataProvider.getSC2()), count});
		}
		
		return dataToBeReturned.iterator();
	}
	
	public static String getOpenAMAuthURL(){
		return SsoDataProvider.getProperty("OpenAMAuthURL");
	}
	
	public static String getOpenAMLogoutURL(){
		return SsoDataProvider.getProperty("OpenAMLogoutURL");
	}
	
	public static String getLauncherURI(){
		return SsoDataProvider.getProperty("LauncherURI");
	}
	
	public static String getBrowserType(){
		return SsoDataProvider.getProperty("BrowserType");
	}
	
	public static String getLauncherLoginPageTitle(){
		return SsoDataProvider.getProperty("LauncherLoginPageTitle");
	}
	
	public static String getLauncherPageTitle(){
		return SsoDataProvider.getProperty("LauncherPageTitle");
	}
	
	public static String getLauncherCategoriesURL(){
		return SsoDataProvider.getProperty("LauncherCategoriesURI");
	}
	
	public static String getWebAppUrl(){
		return SsoDataProvider.getProperty("WebAppURL");
	}
	
	public static String getCitrixAppURL(){
		return SsoDataProvider.getProperty("CitrixAppURL");
	}
	
	public static int getTORWebAppTimeout(){
		return  Integer.parseInt(SsoDataProvider.getProperty("TORWebAppTimeoutInSeconds"));
	}

	public static String getSsoSiShortName() {
		return SsoDataProvider.getProperty("SsoSiShortName");
	}

	public static String getApacheSuShortName() {
		return SsoDataProvider.getProperty("ApacheSuShortName");
	}

	public static String getLogViewerTitle() {
		return SsoDataProvider.getProperty("LogViewerTitle");
	}
	
	public static String getHealthcheckScript() {
		return SsoDataProvider.getProperty("HealthcheckScript");
	}
	
	public static String getFailoverHealtheheckScript() {
		return SsoDataProvider.getProperty("FailoverHealthcheckScript");
	}

	public static String getBackupScript() {
		return SsoDataProvider.getProperty("BackupScript");
	}
	

	public static String getLogViewerURL() {
		return SsoDataProvider.getProperty("LogViewerURL");
	}

	public static String getSwitchSso() {
		return SsoDataProvider.getProperty("SwitchSso");
	}
}