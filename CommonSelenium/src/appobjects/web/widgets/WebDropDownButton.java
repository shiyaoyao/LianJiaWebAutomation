package appobjects.web.widgets;

import com.lianjia.automation.core.loggers.control.CoreLogger;
import com.lianjia.automation.core.web.SeleniumCore;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;
import com.thoughtworks.selenium.SeleniumException;

public class WebDropDownButton extends SeleniumTestObject{	
	public WebDropDownButton(String myLocator) {
		super(myLocator);		
	}

	public static WebDropDownButton getNewWebDropDownButton(String locator)	{
		
		return new WebDropDownButton(locator);
	}
	
	public void select(String optionToSelect) {
		SeleniumCore.debug("TestObject.select(\"" + getObjectLocator() + "\",\"" +optionToSelect+ "\")");
		try{
			super.click();
			WebLink myLink = new WebLink(optionToSelect);
			myLink.waitForExistence();
			myLink.click();
		}  catch (SeleniumException se){
			CoreLogger.error(se.getStackTrace().toString()); 	
			se.printStackTrace();
		}		
	}
}
