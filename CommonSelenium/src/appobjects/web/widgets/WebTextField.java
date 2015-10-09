package appobjects.web.widgets;

import com.lianjia.automation.core.loggers.control.CoreLogger;
import com.lianjia.automation.core.web.SeleniumCore;
import com.lianjia.automation.core.web.selenium.SeleniumTextTestObject;

public class WebTextField  extends SeleniumTextTestObject{
	public int instanceNumber = 0;	
	
	public WebTextField(String myLocator) {
		super(myLocator);	
	}

	public static WebTextField getNewWebTextField(String locator) {
		return new WebTextField(locator);
	}

	public void setPassword(String value) {
		setPassword(getObjectLocator(),value);
	}

	private void setPassword(String myLocator,String value)	{
		CoreLogger.debug("SeleniumTestObject.setPassword(String myLocator) myLocator = '" + myLocator+ "'");		
		
		CoreLogger.debug("SeleniumTestObject.setText(String myLocator) myLocator = '" + myLocator+ "'");
		
		String myFoundLocator = SeleniumCore.getLocatorHelpers.getFoundLocator(myLocator);
		String mySearchString = SeleniumCore.getLocatorHelpers.getSearchStringFromXPath(myLocator);

		try{
			SeleniumCore.getWebDriverBrowser().setText(myFoundLocator, value);
			SeleniumCore.logAction("Enter text  \"********\" in text field \"" + mySearchString +"\"");
		} catch (Exception e){
			CoreLogger.debug("EXCEPTION SETTING THE TEXT  \"********\" IN THE FIELD '" + mySearchString + "'");
			CoreLogger.error("Enter text  \"********\" in text field \"" + mySearchString +"\"",e);		
			throw new RuntimeException(e);
		}	
	}

	public void clearText()	{

		CoreLogger.debug("TestObject.clearText() myLocator=" + getObjectLocator() + "\"");
		clearText(getObjectLocator());
	}

	private void clearText(String myLocator) {
		setText("");
	}
}
