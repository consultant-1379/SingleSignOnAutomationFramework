package com.ericsson.nms.sso.taf.operators.ui;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import com.ericsson.cifwk.taf.UiOperator;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.Ports;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.nms.sso.taf.getters.ui.SsoUIGetter;
import com.ericsson.nms.sso.taf.handlers.selenium.Browser;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher.LoginPage;
import com.ericsson.nms.sso.taf.operators.SsoOperator;
import com.ericsson.nms.sso.taf.operators.ui.SsoUiOperator.Protocol;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;
import com.ericsson.nms.sso.taf.utils.ssoUtils;

public class SsoUiOperator implements UiOperator {

	static Logger log = Logger.getLogger(SsoOperator.class);
	SsoUIGetter getter = new SsoUIGetter();
	Browser browser = new Browser();
	
	public enum Protocol {
	    HTTP, HTTPS
	}
	
	public void startBrowser() {
		log.info("Starting Browser.");
		String browsertype=SsoTestDataProvider.getBrowserType();
		if(browsertype.equalsIgnoreCase("FIREFOX")) {
			browser.startBrowser(Browser.BrowserType.FIREFOX);
		} else if(browsertype.equalsIgnoreCase("GRID")) {
			browser.startBrowser(Browser.BrowserType.GRID);
		} else {
			log.error("Unknown Browser: "+browsertype);
		}
	}
	
	public LoginPage gotoLoginPage(Host myHost) {
		gotoPage(myHost, SsoTestDataProvider.getLauncherURI());
		return new LoginPage(browser.getDriver());
	}

	public String getHTMLTitle() {
		return browser.getHTMLTitle();
	}
	
	public String getCurrentUrl() {
		String currentURL = browser.getDriver().getCurrentUrl();
		log.debug( "Current URL is : " + currentURL );
		return currentURL;
	}
	
	public void navigateBack() {
		browser.navigateBack();
	}
	
	public void refreshPage() {
		browser.refreshPage();
		ssoUtils.sleep(2);
	}
	
	public void interact(int minute) {
		log.info("Interacting with UI for "+minute+" minutes");
		for(int i=minute; i>0; i--) {
			log.info("Interact iteration:"+i);
			ssoUtils.sleep(60);
			browser.refreshPage();
		}
	}
	/**
	 * 
	 * @param myHost
	 * @param uri
	 * @return
	 */
	public void gotoPage(String url) {
		//Default for now is http.
		log.debug("Navigating to URL:"+url);
		browser.getDriver().get(url);
		}
	
	/**
	 * 
	 * @param myHost
	 * @param uri
	 * @return
	 */
	public void gotoPage(Host myHost, String url) {
		//Default for now is http.
		Map<Ports, String> ports=myHost.getPort();
			if(ports.containsKey(Ports.HTTP)){
				log.info("Using HTTP.");
				gotoPage(myHost, url, Protocol.HTTP);
			} else if(ports.containsKey(Ports.HTTPS)) {
				log.info("Using HTTPS.");
				gotoPage(myHost, url, Protocol.HTTPS);
			}
		}
	
	/**
	 * 
	 * @param myHost
	 * @param uri
	 * @return
	 */
	public void gotoPage(Host myHost, String url, Protocol protocol) {
		String uri=protocol+"://"+myHost.getIp()+"/"+ url;
		gotoPage(uri);
		}


	public User getOPERUser(Host host) {
		for(User user: host.getUsers()) {
			if(user.getType().equals(UserType.OPER)) {
				return user;
			}
		}
		log.debug("No OPER user found");
		return null;	
	}
	
	public void cleanUp() {
		browser.closeBrowser();
	}

	public void switchToDefaultWindow(){
		log.info("Switching to default window");
		browser.switchToDefaultWindow();
		ssoUtils.sleep(2);
	}
	
	public void switchToLogviewerWindow() {
		String currentWindow = browser.getDefaultWindowHandle();
		Set<String> listAllBrowsers = browser.listAllBrowsers();
		if(listAllBrowsers.size()!=2) {
			log.warn("Did not find 2 browsers so don't know which one to switch to. Found: "+listAllBrowsers.size() +" browser");
			return;
		}
		Iterator<String> itr = listAllBrowsers.iterator();
		while(itr.hasNext()) {
			String logViewerHandle = itr.next();
			if(!logViewerHandle.equals(currentWindow)) {
				log.info("Switching to Logviewer");
				browser.switchTotWindow(logViewerHandle);
			}
		}
		ssoUtils.sleep(2);
	}

	public LoginPage gotoLoginPage(Host myHost, String launcherURI, Protocol http) {
		String url=http+"://"+myHost.getIp()+"/"+ launcherURI;
		log.debug("Navigating to URL:"+url);
		browser.getDriver().get(url);
		return new LoginPage(browser.getDriver());
	}

}
