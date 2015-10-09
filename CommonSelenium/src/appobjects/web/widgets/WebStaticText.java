package appobjects.web.widgets;

import com.lianjia.automation.core.web.WebBrowser;
import com.lianjia.automation.core.web.selenium.SeleniumTextTestObject;

public class WebStaticText extends SeleniumTextTestObject{
	private String thisText = "";
	
	public WebStaticText(String locator) {
		super(locator);
	}
		
	public boolean exists()	{
		return WebBrowser.isTextPresent(thisText);
	}

}
