package appobjects.web.widgets;

import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class WebButton extends SeleniumTestObject{	
	public WebButton(String myLocator) {
		super(myLocator);		
	}
	
	public static WebButton getNewWebButton(String locator) {		
		return new WebButton(locator);
	}
	
	
}
