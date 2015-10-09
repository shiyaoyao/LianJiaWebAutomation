package appobjects.web.widgets;

import org.openqa.selenium.WebElement;

import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;


public class WebCheckBox extends SeleniumTestObject{	
	public WebCheckBox(String myLocator) {
		super(myLocator);		
	}
	
	public static WebCheckBox getNewWebCheckBox(String locator)	{
		return new WebCheckBox(locator);
	}

	public boolean check() {
		this.click();
		Platform.sleep(3);
		
		WebElement web = this.getWebElement();	
		String className = web.getAttribute("class");
		if(className.equals("check-a"))
			return true;
		else
			return false;
	}
	
	public boolean unCheck(){
		this.click();
		Platform.sleep(3);
		
		WebElement web = this.getWebElement();	
		String className = web.getAttribute("class");
		if(className.equals("check-a"))
			return false;
		else
			return true;
	}
}
