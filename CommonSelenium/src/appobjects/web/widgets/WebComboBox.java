package appobjects.web.widgets;

import com.lianjia.automation.core.loggers.control.CoreLogger;
import com.lianjia.automation.core.web.SeleniumCore;
import com.lianjia.automation.core.web.selenium.SeleniumTextTestObject;
import com.thoughtworks.selenium.SeleniumException;

public class WebComboBox  extends SeleniumTextTestObject{
	public WebComboBox(String myLocator) {
		super(myLocator);		
	}

	/**
	 * getter used for dynamically found widgets
	 * @param locator
	 * @return new instance of the widget
	 */
	public static WebComboBox getNewWebComboBox(String locator)
	{
		return new WebComboBox(locator);
	}


	/**
	 * selects a value from a combo box - uses the locator stored in the instance of the selenium test object 
	 * logs the action
	 * @param value
	 */
	public void select(String value)
	{
		select(getObjectLocator(),value);
	}
	
	/**
	 * selects a value from a combo box using the locator supplied
	 * logs the action
	 * @param myLocator
	 * @param value
	 */
	private void select(String myLocator,String value)
	{
		try{
			SeleniumCore.getWebDriverBrowser().select(myLocator, value);
		}catch (SeleniumException se){
			CoreLogger.error(se.getStackTrace().toString());
			se.printStackTrace();
		}	
	}
}
