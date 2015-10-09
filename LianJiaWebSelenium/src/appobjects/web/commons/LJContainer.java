package appobjects.web.commons;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import appobjects.web.LJAppObject;

import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class LJContainer extends SeleniumTestObject {
	public LJContainer() {	
		
	}
	
	public LJContainer(String myLocator) {
		super(myLocator);		
	}

	public static String convertIdToCssDivPath(String sId){
		return LJAppObject.convertIdToCssLocator(sId);
	}
		
	public List<WebElement> getChildrenWithMatch(By arg0){
		List<WebElement> children = getWebElement().findElements(arg0);
		
		return children;
	}
	
}
