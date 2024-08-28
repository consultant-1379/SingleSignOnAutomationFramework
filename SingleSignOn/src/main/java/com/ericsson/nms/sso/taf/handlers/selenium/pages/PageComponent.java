package com.ericsson.nms.sso.taf.handlers.selenium.pages;

import org.openqa.selenium.WebDriver;

public abstract class PageComponent extends Container {

	public PageComponent(WebDriver driver) {
		super(driver);
		instanciatePageElements(getClass());
	}

}
