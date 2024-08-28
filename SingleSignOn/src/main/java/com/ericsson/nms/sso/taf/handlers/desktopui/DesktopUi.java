package com.ericsson.nms.sso.taf.handlers.desktopui;

import java.io.File;

import org.apache.log4j.Logger;
import org.sikuli.api.*;
import org.sikuli.api.robot.Mouse;
import org.sikuli.api.robot.Key;
import org.sikuli.api.robot.desktop.DesktopMouse;
import org.sikuli.api.robot.desktop.DesktopKeyboard;

public class DesktopUi {
	
	static Logger log = Logger.getLogger(DesktopUi.class);
	static final int DEFAULT_WAIT_TIME=50;
	
	public ScreenRegion getDesktop() {
		ScreenRegion desktop = new DesktopScreenRegion();
		return desktop;
	}

	public ScreenRegion findElement(ScreenRegion region, File image) {
		return findElement(region, image, DEFAULT_WAIT_TIME);
	}
	
	public ScreenRegion findElement(ScreenRegion region, File image, int waitime) {
		Target imageTarget = new ImageTarget(image);
		ScreenRegion r = region.wait(imageTarget,waitime);
		return r;
	}
	
	public void clickElementCenter(ScreenRegion region) {
	       Mouse mouse = new DesktopMouse();
	       mouse.click(region.getCenter());
	       log.debug("Mouse clicked");
	}
	
	public void typeKeys(String keys) {
        DesktopKeyboard keyboard = new DesktopKeyboard();
        keyboard.type(keys);
        log.debug("Typing:"+keys);
	}
	
	public void sendTAB() {
		DesktopKeyboard keyboard = new DesktopKeyboard();
		keyboard.type(Key.TAB);
		log.debug("Sending: TAB");
	}
	
	public void sendENTER() {
		DesktopKeyboard keyboard = new DesktopKeyboard();
		keyboard.type(Key.ENTER);
		log.debug("Sending: ENTER");
	}
	
	public boolean verifyElementPresent(ScreenRegion region, File image) {
		ScreenRegion r=findElement(region, image, DEFAULT_WAIT_TIME);
        if(r == null){
        	log.debug("Element not present: "+image.getName());
        	return false;
        }
        return true;
	}
}
