package appobjects.web;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import tasks.web.util.SeleniumUtils;

import com.lianjia.automation.CoreAutomation.Log;
import com.lianjia.automation.core.web.SeleniumCore;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class WebFrame extends SeleniumTestObject {
	private static final Logger log = Logger.getLogger(WebFrame.class);
	
	public static WebFrame oMainFrame = new WebFrame("//html");
	public static SeleniumTestObject oHeadDiv = new SeleniumTestObject("//html/head");
	public static SeleniumTestObject oBodyDiv = new SeleniumTestObject("//html/body");
	
	public WebFrame(String myLocator) {
		super(myLocator);
	} 
	
	public boolean selectFrame() {
		try {			
			if (this.exists() || this.getProperty("tagName").toLowerCase().contains("html"))
				return true;
			else
				return false; 
		} catch (Exception e) {
			log.error("Failed to select the frame");
		}
		return false;
	}

	// SELECTING FRAMES
	public static  WebFrame MainFrame() {
		return oMainFrame;
	}
	
	public static void gotoTopWindow() {
		try {
			SeleniumCore.getWebDriverBrowser().getWebDriverAPI().switchTo().defaultContent().switchTo().defaultContent();
		} catch (Exception e) {
			log.error("Failed to select the Top Window frame");
		}
	}
	
	public static boolean selectMainFrame() {
		log.setLevel(Level.toLevel("DEBUG"));
		if(!oMainFrame.exists()  && oBodyDiv.exists()){
			return true;
		}
		WebFrame.gotoTopWindow();
		try {
			boolean success = false;
			if(oMainFrame.exists() && MainFrame().selectFrame()){
				success = oBodyDiv.waitForMatchingElement(3000);
			}
			// retry if failed
			if(!success){
				log.debug("Failed to select s_MainFrame on first attempt");
				WebFrame.gotoTopWindow();
				if(oMainFrame.exists()){
					MainFrame().selectFrame();
					success =  oBodyDiv.waitForMatchingElement(2000);
					if(!success){
						log.debug("Failed to selecte s_MainFrame or to find body div");
						return false;
					}
				}
			}
			log.debug("Selected s_MainFrame");
			
			return success;
		} catch (Exception e) {
			log.warn("Failed to select s_MainFrame");
		}
		return false;
	}
	
	public static boolean selectFrame(String sId) {
		try {
			if(!SeleniumTestObject.isVisible(sId)){
				log.debug("Frame id <code>"+sId+"</code> not found");
				return false;
			}
			SeleniumCore.getWebDriverBrowser().getWebDriverAPI().switchTo()
					.frame(SeleniumCore.getWebDriverBrowser().getWebDriverAPI().findElement(By.id(sId)));
			return true;
		} catch (Exception e) {

		}
		Log.error("Failed to select frame222222222222222 '" + sId + "'");
		return false;
	}
	
	public static boolean isMainFrameSelected() {
		return oMainFrame.exists();
	}
	
	public static boolean isMainFrameVisible() {
		return oMainFrame.exists();
	}
	
	public static boolean isMainBodyVisible() {
		return oBodyDiv.exists();
	}
}
