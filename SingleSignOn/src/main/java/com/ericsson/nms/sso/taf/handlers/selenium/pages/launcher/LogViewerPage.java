package com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.PageComponent;

public class LogViewerPage extends PageComponent{

	static Logger log = Logger.getLogger(LogViewerPage.class);
	private final WebDriver driver; 

	public LogViewerPage(WebDriver driver) {
		super(driver);
		this.driver = driver; 
		log.info("Created Logviewer Page.");
	}

	@FindBy(className ="eaContainer-LogoutButton-link")
	private WebElement signOutButton;
	
	@FindBy(xpath="/html/body/div/div[2]/div/div[@class='torLogViewer-Content-mainHeaderTitle dmsLogViewer-Container-title']")
	private WebElement containerTitle;
	
    public void signOut() {
    	signOutButton.click();
    	sleep(1);
    	driver.switchTo().alert().accept();
    	log.info("SignOut of Logviewer button clicked.");
    } 
    
    public String getLogViewerTitle() {
    	//String title=containerTitle.getText();
    	String title=getTitle();
    	log.info("Logviewer Title:"+title);
    	return title;
    } 
}
