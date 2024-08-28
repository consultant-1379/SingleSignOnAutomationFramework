package com.ericsson.nms.sso.taf.handlers.selenium.pages.launcher;


import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.ericsson.nms.sso.taf.handlers.selenium.pages.PageComponent;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;

public class LauncherPage extends PageComponent {
	static Logger log = Logger.getLogger(LauncherPage.class);
	
	private final WebDriver driver;

/*	@FindBy(className ="TorMegamenu-topBar-signOutButton")
	private WebElement signOutButton;
*/	
	@FindBy(className ="eaContainer-LogoutButton-link")
	private WebElement signOutButton;
	
	@FindBy(xpath="//a[@title='Launch Command Log Search']")
	private WebElement HCAppLink;
	
//	@FindBy(xpath="//a[@title='Launch CIF Activity Manager']")
//	@FindBy(xpath="//a[@title='Launch OSS Common Explorer']")
	@FindBy(xpath="//a[@title='Launch Alarm List Viewer']")
	private WebElement CIFAMLink;
	
	@FindBy(xpath="/html/body/div/div[2]/div/div/div/ul/li[3]/a")
	private WebElement AtoZLinkLink;
	
	@FindBy(xpath="/html/body/div/div[2]/div/div/div/ul/li[2]/a")
	private WebElement CatagoriesLink;
	
	@FindBy(xpath="/html/body/div/div[2]/div/div/div/ul/li[1]/a")
	private WebElement FavouritesLink;
	
	@FindBy(xpath="/html/body/div/div[2]/div/div[3]/div/div[2]/div/div/input")
	private WebElement SearchBoxEntryForm;
	
	public LauncherPage(WebDriver driver) {
		super(driver);  
        this.driver = driver;
        if (!SsoTestDataProvider.getLauncherPageTitle().equals(getTitle())) {
            throw new IllegalStateException("This is not the launcher page!");
        }
        log.info("Created Launcher Page.");
	}
	
	public void enterSearchBoxText(String text) {
		log.info("Entering:"+text);
		SearchBoxEntryForm.sendKeys(text);
	}
	
	
    public void clickCIFAMLinkLink() {
    	log.info("Clicking CIFAMLink");
    	CIFAMLink.click();
    }  

    public void clickHCAppLink() {  
    	log.info("Clicking HCAppLink");
    	HCAppLink.click();
    }  
    
    public void clickSignOutButton() {  
    	signOutButton.click();
    } 
    
    public void signOut() {
    	signOutButton.click();
    	sleep(1);
    	driver.switchTo().alert().accept();
    	log.info("SignOut button clicked.");
    	sleep(1);
    } 
    
    public void clickAppLink(String title) {
    	WebElement link = findAppLink(title);
    	if(link==null) {
    		log.error("Could not find link for :"+ title);
    		return;
    	}
    	link.click();
    	log.info("Clicking :"+title);
    }
    
    private WebElement findAppLink(String title){
    	log.debug("Finding link for:"+title);
    	return driver.findElement(By.xpath("//a[@title='"+title+"']"));
    }
    
    public void clickAtoZLink() {
    	log.info("Clicking AtoZLinkLink");
    	AtoZLinkLink.click();
    } 
    
    public void clickCatagoriesLink() {
    	log.info("Clicking CatagoriesLink");
    	CatagoriesLink.click();
    } 
    
    public void clickFavouritesLink() {
    	log.info("Clicking FavouritesLink");
    	FavouritesLink.click();
    } 
    
	public LogViewerPage clickLogViewerLink() {
		log.info("Launching Log Viewer");
		By logViewerLocator = By.linkText("Log Viewer");
		waitUntilElementIsPresent(logViewerLocator).click();
		sleep(1);
		return new LogViewerPage(driver);
	}

}
