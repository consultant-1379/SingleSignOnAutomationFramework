package com.ericsson.nms.sso.taf.handlers.selenium;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.ericsson.nms.sso.taf.utils.ssoUtils;
import com.ericsson.cifwk.taf.utils.FileFinder;
import com.ericsson.nms.sso.taf.data.SsoDataProvider;


public class Browser {
	
	
	static Logger log = Logger.getLogger(Browser.class);
	private WebDriver driver;
	private File tmpProfileDir;
	private String defaultWindowHandle;
	BrowserType browserType;
	
	public String getDefaultWindowHandle() {
		return defaultWindowHandle;
	}

	public enum BrowserType {
	    FIREFOX, IE, CHROME, HTMLUNIT, GRID
	}
	
	public enum Protocol {
	    HTTP, HTTPS
	}
	
	public void startBrowser(BrowserType browserType) {
		this.browserType=browserType;
		this.driver = getWebDriver(browserType);
		defaultWindowHandle=driver.getWindowHandle();
		log.debug("Window Handle :"+ defaultWindowHandle);
	}

	public void closeBrowser() {
		closeAllBrowsers();
		if(browserType.equals(BrowserType.FIREFOX)) {
			cleanUpProfile();
		}
	}
	
	
	public void closeAllBrowsers() {
		Iterator<String> itr = driver.getWindowHandles().iterator();
		while(itr.hasNext()) {
			String tmpwindowHandle = itr.next();

			log.debug("Closing window:"+tmpwindowHandle);
			driver.switchTo().window(tmpwindowHandle);
			ssoUtils.sleep(3);
			driver.close();

		}
	}
	
	public Set<String> listAllBrowsers() {
		Set<String> set= driver.getWindowHandles();
		Iterator<String> itr = set.iterator();
		while(itr.hasNext()) {
			log.debug("Browser window:"+itr.next());
		}
		return set;
	}
	
	private void cleanUpProfile() {
		log.debug("Deleting Profile Directory:"+tmpProfileDir.getPath());
		try {
			
			FileUtils.deleteDirectory(tmpProfileDir);
		} catch (IOException e) {
			log.error("Could not delete tmp Profile Dir");
			//throw new RuntimeException(e);
		}
	}

	public WebDriver getDriver() {
	    return this.driver;
	}
	
	public String getHTMLTitle() {
		String title = driver.getTitle();
		log.info("Page Title :"+title);
		return title;
	}
	
	public void navigateBack() {
		log.info("Navigating Back");
		driver.navigate().back();
	}
	
	public void refreshPage() {
		log.info("Refreshing Page");
		driver.navigate().refresh();
	}
	
	public void switchToDefaultWindow() {
		log.debug("Switching back to default window:"+defaultWindowHandle);
		driver.switchTo().window(defaultWindowHandle);
	}
	
	public void switchTotWindow(String WindowHandle) {
		log.debug("Switching back to window:"+WindowHandle);
		driver.switchTo().window(WindowHandle);
	}

	private WebDriver getWebDriver(BrowserType browserType) {
	    switch (browserType) {
	        case FIREFOX:
	            return new FirefoxDriver(getFirefoxProfile());
	        case IE:
	            // Need to implement
	            return null;
	        case CHROME:
	            // Need to implement
	            return null;
	        case HTMLUNIT:
	            // Need to implement
	            return null;
	        case GRID:
	        	DesiredCapabilities capability = DesiredCapabilities.firefox();
	        	capability.setPlatform(Platform.WINDOWS);
	        	//capability.setBrowserName(“firefox” ); 
	        	//capability.setPlatform(“LINUX”);  
	        	//capability.setVersion(“3.6”);
			try {
				return new RemoteWebDriver(new URL(SsoDataProvider.getProperty("SeleniumGridUrl")), capability);
			} catch (MalformedURLException e) {
                System.out.println(e.getMessage());
			}
	        default:
	            throw new RuntimeException("Browser type unsupported");
	    }
	}


	
	private FirefoxProfile getFirefoxProfile() {
		File profileDir;
		tmpProfileDir = new File("tmp\\profile_"+ssoUtils.generateRandomString(8));
		
		String firefoxProfilePath = SsoDataProvider.getProperty("FirefoxProfile");
		if(firefoxProfilePath == null) {
			profileDir = new File(getDefaultProfileLocation());
		} else {
			profileDir = new File(firefoxProfilePath);
		}
		
		try {
			FileUtils.copyDirectoryToDirectory(profileDir, tmpProfileDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		log.debug("Using profile:"+tmpProfileDir.getPath());
		
		return new FirefoxProfile(profileDir);
	}
	
	private String getDefaultProfileLocation() {
		String defaultProfile = FileFinder.findFile("defaultfirefox-profile").get(0); 
		log.debug("Using found profile" + defaultProfile);
		return  defaultProfile;
	}
}
