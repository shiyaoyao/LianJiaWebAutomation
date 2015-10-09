package tasks.web.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;


import com.lianjia.automation.core.web.SeleniumCore;

public class BrowserReadyThread extends Thread {
	private boolean bPass = false;
	private String sBrowserStatus = "EXCEPTION";
	
	public BrowserReadyThread() {
	
	}
	
	public void run() {
		WebDriver driver = SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
		JavascriptExecutor jsExe = (JavascriptExecutor)driver;
		try {
			sBrowserStatus = jsExe.executeScript("return document.readyState").toString();
			bPass = true;
		} catch (Exception e) {
			sBrowserStatus = "EXCEPTION";
			System.out.println(sBrowserStatus);
			return;
		}
	}
	
	public String browserStatus(){
		return sBrowserStatus;
	}
	
	public boolean pass() {
		return bPass;
	}

}
