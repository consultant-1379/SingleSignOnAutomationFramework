package com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


import com.ericsson.nms.sso.taf.handlers.selenium.pages.PageComponent;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;


public class LoginPage extends PageComponent {

	private final WebDriver driver;  
	static Logger log = Logger.getLogger(LoginPage.class);

	@FindBy(id="loginUsername")
	private WebElement usernameTextEntryForm;

	@FindBy(id="loginPassword")
	private WebElement passwordTextEntryForm;

	@FindBy(id="submit")
	private WebElement submitButton;

	public LoginPage(WebDriver driver) {  
		super(driver);  
		this.driver = driver; 
		if (!SsoTestDataProvider.getLauncherLoginPageTitle().equals(getTitle())) {
			throw new IllegalStateException("This is not the login page");
		}
		log.info("Created Login Page.");
	}  

	public LauncherPage loginAs(String username, String password) {  

		executeLogin(username, password);  
		return new LauncherPage(driver);  
	}  

	public void loginFail(String username, String password) {  
		executeLogin(username, password);   
	}  


	private void executeLogin(String username, String password) {  
		log.debug("Entering Username:"+username);
		log.debug("Entering Password:"+password);
		usernameTextEntryForm.sendKeys(username);
		passwordTextEntryForm.sendKeys(password);
		submitButton.click();
	}  

	public String getErrorMessage() {  
		//TODO - not correct
		return driver.findElement(By.xpath("/html/body/p/font[@color='red']")).getText();  
	}  
}
