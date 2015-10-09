package appobjects.web.widgets;

import com.lianjia.automation.core.web.selenium.SeleniumTestObject;


public class WebImage extends SeleniumTestObject{
	private static String myWidgetType = "image";
	
	public WebImage(String myLocator) {
		super(myLocator);
		
	}
	
	/**
	 * getter used for dynamically found widgets
	 * @param locator
	 * @return new instance of the widget
	 */
	public static WebImage getNewWebImage(String locator)
	{
		return new WebImage(locator);
	}
}
