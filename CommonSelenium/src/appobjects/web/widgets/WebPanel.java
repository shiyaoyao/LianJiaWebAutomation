package appobjects.web.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class WebPanel  extends SeleniumTestObject{	
	public WebPanel(String myLocator) {
		super(myLocator);		
	}
	
	public WebElement getChildrenWithMatch(By arg0){
		WebElement children = getWebElement().findElement(arg0);
		
		return children;
	}	
}
