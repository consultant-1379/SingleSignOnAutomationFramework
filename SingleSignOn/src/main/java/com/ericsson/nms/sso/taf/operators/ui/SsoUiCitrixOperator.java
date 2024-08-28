package com.ericsson.nms.sso.taf.operators.ui;

import org.apache.log4j.Logger;
import org.sikuli.api.ScreenRegion;

import com.ericsson.cifwk.taf.UiOperator;
import com.ericsson.nms.sso.taf.getters.ui.SsoUIGetter;
import com.ericsson.nms.sso.taf.handlers.desktopui.DesktopUi;
import com.ericsson.nms.sso.taf.operators.SsoOperator;

public class SsoUiCitrixOperator implements UiOperator {

	static Logger log = Logger.getLogger(SsoOperator.class);
	SsoUIGetter getter = new SsoUIGetter();
	
	public boolean isCitrixLoginScreenPresent() {
		DesktopUi deskUi = new DesktopUi();
		//First wait
		sleep(20);
		
		//Manage existing session pop-up if present
		ScreenRegion existingSession = deskUi.findElement(deskUi.getDesktop(), getter.getImageFile(SsoUIGetter.citrixExistingSession));	
		if(existingSession != null) {
			log.info("Clicking on Existing Session window");
			existingSession = deskUi.findElement(existingSession, getter.getImageFile(SsoUIGetter.citrixNewSessionButton));
			deskUi.clickElementCenter(existingSession);
		} else {
			log.info("Existing Session window not present.");
		}
		
		//Click invalid login
		sleep(10);

		ScreenRegion invalidLogin = deskUi.findElement(deskUi.getDesktop(), getter.getImageFile(SsoUIGetter.citrixInvalidLoginWindow));	
		if(invalidLogin != null) {
			log.info("Clicking on Invalid Login window");
			invalidLogin = deskUi.findElement(invalidLogin, getter.getImageFile(SsoUIGetter.citrixInvalidLoginOKButton));
			deskUi.clickElementCenter(invalidLogin);
		} else {
			log.info("Invalid Login window not present.");
		}
		
		sleep(2);
		//Verify that login window is present
		ScreenRegion Login = deskUi.findElement(deskUi.getDesktop(), getter.getImageFile(SsoUIGetter.citrixLoginLogoImage));	
		if(Login == null) {
			log.info("No Login window found.");
			return false;
		} else {
			log.info("Login window found! - clearing it for next test.");
			//Now clear it for next test
			Login = deskUi.findElement(Login, getter.getImageFile(SsoUIGetter.citrixLoginCancelButton));
			deskUi.clickElementCenter(Login);
			return true;
		}

	}

	public boolean exitCitrixApp() {
		DesktopUi deskUi = new DesktopUi();
	
		ScreenRegion exitButton = deskUi.findElement(deskUi.getDesktop(), getter.getImageFile(SsoUIGetter.citrixAppExit));
		if(exitButton != null) {
			log.info("Clicking on exit button");
			deskUi.clickElementCenter(exitButton);
		} else {
			log.error("Exit button not present.");
			return false;
		}
		
		sleep(1);
		
		//Not going to fail here.
		ScreenRegion exitOKButton = deskUi.findElement(deskUi.getDesktop(), getter.getImageFile(SsoUIGetter.citrixAppExitOkButton));
		if(exitOKButton != null) {
			log.info("Clicking on exit OK button");
			deskUi.clickElementCenter(exitOKButton);
		} else {
			log.info("Exit OK button not present.");
		}
		
		return true;
	}
	
	public void interactWithCitrixApp() {
		//TODO
	}
	

	private void sleep(int time) {

		try {
			log.info("Sleeping for "+time +" seconds...");
		    Thread.sleep(time * 1000);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
	

}
