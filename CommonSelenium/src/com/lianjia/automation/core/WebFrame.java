package com.lianjia.automation.core;

import org.openqa.selenium.By;

import com.lianjia.automation.core.web.SeleniumCore;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class WebFrame extends SeleniumTestObject {

	public WebFrame(String myLocator) {
		super(myLocator);
	}
	
	public boolean selectFrame(){
		SeleniumCore.getWebDriverBrowser().getWebDriverAPI().switchTo().frame(SeleniumCore.getWebDriverBrowser().getWebDriverAPI().findElement(By.xpath(this.getObjectLocator())));
		return true;
	}
	
	public static void goToRootFrame() {
		SeleniumCore.getWebDriverBrowser().getWebDriverAPI().switchTo().defaultContent();
	}	

}
