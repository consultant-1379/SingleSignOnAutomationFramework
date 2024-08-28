package com.ericsson.nms.sso.taf.handlers.selenium.pages;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;


import com.google.common.base.Predicate;

public abstract class Container {
	public static int defaultExplicitWaitTimeout = 10;
		
	private final WebDriver driver;
	protected final Logger logger;
	
	public Container(WebDriver driver) {
		this.logger = Logger.getLogger(getClass());
		this.driver = driver;
	}
	
	protected void instanciatePageElements(Class<? extends Container> clazz) {
		PageFactory.initElements(driver, this);
		instanciatePageComponents(clazz);
	}

	@SuppressWarnings("unchecked")
	private final void instanciatePageComponents(Class<? extends Container> clazz) {
		do {
			for(Field field : clazz.getDeclaredFields()) {
				if(PageComponent.class.isAssignableFrom(field.getType())) {
					field.setAccessible(true);
					try {
						Constructor<?> constructor = field.getType().getDeclaredConstructor(WebDriver.class);
						field.set(this, constructor.newInstance(getDriver()));
					} catch (Exception e) {
						logger.trace("Not possible to instanciate object: "+clazz.getName(), e);
					}
				}
			}
			clazz = (Class<? extends Container>) clazz.getSuperclass();
		} while(!clazz.isAssignableFrom(PageComponent.class));
	}
	
	
	public FluentWait<WebDriver> getWait() {
		return getWait(defaultExplicitWaitTimeout);
	}
	
	public FluentWait<WebDriver> getWait(int timeOutInSeconds) {
		return new FluentWait<WebDriver>(driver)
			.withTimeout(timeOutInSeconds, TimeUnit.SECONDS)
			.pollingEvery(1, TimeUnit.SECONDS);
	}
	
	public FluentWait<WebDriver> getWait(String messageDisplayedWhenTimeExpires) {
		return getWait(defaultExplicitWaitTimeout, messageDisplayedWhenTimeExpires);
	}
	
	public FluentWait<WebDriver> getWait(int timeOutInSeconds, String messageDisplayedWhenTimeExpires) {
		return getWait(timeOutInSeconds)
				.ignoring(NoSuchElementException.class)
				.withMessage(messageDisplayedWhenTimeExpires);
	}
	
	public void waitUntil(Predicate<WebDriver> predicate, FluentWait<WebDriver> wait) {
		wait.until(predicate);
	}
	
	public void waitUntil(Predicate<WebDriver> predicate) {
		waitUntil(predicate, getWait());
	}
	
	public <T> T waitUntil(ExpectedCondition<T> expectedCondition, Wait<WebDriver> wait) {
		return wait.until(expectedCondition);
	}
	
	public <T> T waitUntil(ExpectedCondition<T> expectedCondition) {
		return waitUntil(expectedCondition, getWait());
	}
	
	
	public WebElement waitUntilElementIsDisplayed(By locator, Wait<WebDriver> wait) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public WebElement waitUntilElementIsDisplayed(By locator) {
		return waitUntilElementIsDisplayed(locator, getWait());
	}
	
	
	public WebElement waitUntilElementIsDisplayed(WebElement element, Wait<WebDriver> wait) {
		return wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	public WebElement waitUntilElementIsDisplayed(WebElement element) {
		return waitUntilElementIsDisplayed(element, getWait());
	}
	
	public WebElement waitUntilElementIsPresent(By locator, Wait<WebDriver> wait) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}
	
	public WebElement waitUntilElementIsPresent(By locator) {
		return waitUntilElementIsPresent(locator, getWait());
	}
	
	public WebElement waitUntilElementIsClickable(WebElement element, Wait<WebDriver> wait) {
		return wait.until(elementToBeClickable(element));
	}
	
	public WebElement waitUntilElementIsClickable(WebElement element) {
		return waitUntilElementIsClickable(element, getWait());
	}
	
	public WebElement waitUntilElementIsClickable(By locator, Wait<WebDriver> wait) {
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}
	
	public WebElement waitUntilElementIsClickable(By locator) {
		return waitUntilElementIsClickable(locator, getWait());
	}

	
	private static ExpectedCondition<WebElement> elementToBeClickable(final WebElement element) {
		return new ExpectedCondition<WebElement>() {
			private ExpectedCondition<WebElement> visibilityOfElement =
				ExpectedConditions.visibilityOf(element);

			public WebElement apply(WebDriver driver) {
				WebElement element = visibilityOfElement.apply(driver);
				try {
					if (element != null && element.isEnabled()) {
						return element;
					} else {
						return null;
					}
				} catch (StaleElementReferenceException e) {
					return null;
				}
			}
		};
	}
	
	protected static ExpectedCondition<WebElement> visibilityOfAnyElement(final By... locators) {
		return new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				for(By locator : locators) {
					WebElement webElement = driver.findElement(locator);
					if(webElement.isDisplayed()) {
						return webElement;
					}
				}
				return null;
			}
		};
	}
	
	protected static ExpectedCondition<WebElement> attributeOfElementToBe(final By locator, 
			final String attribute, final String attributeValue) {
		return new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				WebElement webElement = driver.findElement(locator);
				String attributeCurrentValue = webElement.getAttribute(attribute);
				if(attributeCurrentValue != null && attributeCurrentValue.equals(attributeValue)) {
					return webElement;
				}
				else {
					return null;
				}
			}
		};
	}
	
	public boolean isElementPresent(By by) {
		return getDriver().findElements(by).size() > 0;
	}
	
	public WebElement findElement(SearchContext searchContext, By by, String errorMessageIfNotFound) {
		try {
			return searchContext.findElement(by);
		}
		catch(NoSuchElementException e) {
			throw new NoSuchElementException(errorMessageIfNotFound, e);
		}
	}
	public WebElement findElement(By by, String errorMessageIfNotFound) {
		return findElement(driver, by, errorMessageIfNotFound);
	}
	
	
	protected static void sleep(int sec) {
		try {
			Thread.sleep(sec*1000);
		} 
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	
	public String getTitle() {
	    waitUntil(titleIsVisible(),getWait());
	    String title=driver.getTitle();
	    Log.debug("Page Title:"+title);
	    return title;
	}

	private Predicate<WebDriver> titleIsVisible() {
	    return new Predicate<WebDriver>() {
	        @Override public boolean apply(WebDriver driver) {
	            return isTitleVisible();
	        }
	    };
	}
	
	private boolean isTitleVisible() {
	    return (driver.getTitle().length() > 0);
	}
	
}
