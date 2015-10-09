package tasks.web.authentication;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import tasks.base.BaseTask;
import tasks.web.commons.LJNavTasks;
import appobjects.user.WebUser;
import appobjects.web.Browser;
import appobjects.web.authentication.LJLoginObjects;

import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.WebBrowser;

public class LJLoginTasks extends BaseTask{
	private static final Logger log = Logger.getLogger(LJLoginTasks.class);
	
	protected static WebElement oUsernameField = LJLoginObjects.getTextField_loginUsername();
	protected static WebElement oPasswordField = LJLoginObjects.getTextField_loginPassword();
	protected static WebElement oLoginBtn = LJLoginObjects.getButton_LoginButton();
	
	public static boolean login(WebUser user){
		boolean success = false;
		
		log.info("USERNAME: " + user.getPhone()+
				"        PASSWORD:" + user.getPassword());
		
		log.info("点击 登录 按钮");
		LJNavTasks.logIn();	
		
		log.info("弹出登录浮层");
		Platform.sleep(3);
		log.info("oUsernameField.isEnabled() = " + oUsernameField.isEnabled());
		if(oUsernameField.isEnabled() && oPasswordField.isEnabled()) {
			log.info("输入手机号");
			oUsernameField.sendKeys(user.getPhone());
			
			log.info("输入密码");
			oPasswordField.sendKeys(user.getPassword());
			
			log.info("点击 立即登录 按钮");
			oLoginBtn.click();
			
			Platform.sleep(3);
			success = LJNavTasks.isLogOutExist();
		} else if(!oUsernameField.isEnabled()){
			log.error("NO LOGIN SCREEN");
			logScreenCapture("No login screen");
			return false;
		}
		
		return success;
	} 	
		
	/**
	 * Clicks the logout button
	 * @return
	 */
	public static boolean logout(){
		boolean success = false;
		log.info("点击  退出 按钮");
		LJNavTasks.logOut();		
		
		Platform.sleep(3);
		success = LJNavTasks.isLogInExist();
		
		return success;
	}
		
	/**
	 * Check for an Internet Explorer certificate exception. If the IE certificate override link is 
	 * found before the User Name field, click the override link.
	 * Handle up to three (3) sequential certificate exceptions.
	 */
	protected static void _handleIECertWarnings() {
		String sInvalidCert = "invalidcert";
		// Bail out if the browser isn't Internet Explorer
		if(!WebBrowser.isInternetExplorer())
			return;
		
		String url = Browser.getCurrentUrl().toLowerCase();
		if(!url.contains(sInvalidCert)){
			return;
		}
		int count = 4;
		while (url.contains(sInvalidCert) && count-- > 0) {
			try {
				log.debug("Page title: <code>" + Browser.getTitle() + "'</code>");
				Browser.waitForPageLoaded();
			} catch (Exception e) {
				// Fall through for null object
			}
			url = Browser.getCurrentUrl().toLowerCase();
			if (url.contains("invalidcert")) {
				Browser.waitForPageLoaded();
			}
		}
		String pageTitle = Browser.getTitle();
		logCompare(true, !(pageTitle.toLowerCase().contains("error") || pageTitle.toLowerCase().contains("certificate")), "Verify page title <span style='color:#505050;'>("+pageTitle+")</span> is NOT 'Certificate Error'");
	}
}
