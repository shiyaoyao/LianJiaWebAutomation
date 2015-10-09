package appobjects.web.widgets;

import com.lianjia.automation.core.loggers.control.CoreLogger;
import com.lianjia.automation.core.web.SeleniumCore;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;
import com.thoughtworks.selenium.SeleniumException;

public class WebRadioButton extends SeleniumTestObject{
	public WebRadioButton(String myLocator) {
		super(myLocator);
		
	}

	public static WebRadioButton getNewWebRadioButton(String locator) {
		return new WebRadioButton(locator);
	}

	public void select(String value) {
		select(getObjectLocator(),value);
	}

	private void select(String myLocator,String value) {
		SeleniumCore.debug("TestObject.select(\"" + myLocator + "\",\"" +value+ "\")");

		try{
			SeleniumCore.getWebDriverBrowser().select(myLocator, value);
		}catch (SeleniumException se){
			CoreLogger.error(se.getStackTrace().toString()); 
			se.printStackTrace();
		}

	}

	public boolean isSelected()	{
		return true;
	}
}
