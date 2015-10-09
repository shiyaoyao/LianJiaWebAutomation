package com.lianjia.automation.core.web.selenium;

import com.lianjia.automation.core.loggers.control.CoreLogger;
import com.lianjia.automation.core.web.SeleniumCore;

public class SeleniumTextTestObject extends SeleniumTestObject{
	public SeleniumTextTestObject(String myLocator) {
		super(myLocator);
	}
	
	/**
	 * sets text in a text field - inherited by all widgets
	 * logs the action
	 * @param myLocator - locator used to find the widget
	 * @param myValue - value to set in the text field
	 */
	protected void setText(String myLocator, String myValue) {
		CoreLogger.debug("SeleniumTextTestObject.setText(String myLocator) myLocator = '" + myLocator+ "'");

		String myFoundLocator = SeleniumCore.getLocatorHelpers.getFoundLocator(myLocator);
		String mySearchString = SeleniumCore.getLocatorHelpers.getSearchStringFromXPath(myFoundLocator);

		try{
			SeleniumCore.getWebDriverBrowser().setText(myFoundLocator, myValue);
			SeleniumCore.logAction("Enter text \"" + myValue + "\" in text field \"" + mySearchString +"\"");
		} catch (Exception e){
			CoreLogger.debug("EXCEPTION SETTING THE TEXT '" + myValue + "' IN THE FIELD '" + mySearchString + "'");
			CoreLogger.error("Enter text \"" + myValue + "\" in text field \"" + mySearchString +"\"",e);		
			throw new RuntimeException(e);
		}		
		
	}

	public void setText(String myValue)	{
		CoreLogger.debug("SeleniumTextTestObject.setText() myLocator=" + getObjectLocator() + "\"");
		
		setText(getObjectLocator(), myValue);
	}
	
	protected void setTextWithHTML(String myLocator, String myValue) {
		CoreLogger.debug("SeleniumTextTestObject.setText(String myLocator) myLocator = '" + myLocator+ "'");

		String myFoundLocator = SeleniumCore.getLocatorHelpers.getFoundLocator(myLocator);
		String mySearchString = SeleniumCore.getLocatorHelpers.getSearchStringFromXPath(myLocator);
		try{
			SeleniumCore.getWebDriverBrowser().setText(myFoundLocator, myValue);
			SeleniumCore.logAction("Enter HTML content in text field \"" + mySearchString +"\"");
		} catch (Exception e){
			CoreLogger.debug("EXCEPTION SETTING THE HTML CONTENT IN THE FIELD '" + mySearchString + "'");
			CoreLogger.error("Enter HTML content in text field \"" + mySearchString +"\"",e);		
			throw new RuntimeException(e);
		}		
	}
	
	public void setTextWithHTML(String myValue)	{
		CoreLogger.debug("SeleniumTextTestObject.setText() myLocator=" + getObjectLocator() + "\"");
		
		setTextWithHTML(getObjectLocator(), myValue);
	}	
}
