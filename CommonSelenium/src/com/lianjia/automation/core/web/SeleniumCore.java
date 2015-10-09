package com.lianjia.automation.core.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.server.SeleniumServer;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.lianjia.automation.CoreAutomation;
import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.loggers.control.CoreLogger;
import com.lianjia.automation.core.util.FileHelper;
import com.thoughtworks.selenium.SeleniumException;

/**
 * Core Selenium Class
 * 
 */
public class SeleniumCore {
	
	/**
	 * debug boolean is used to enable/disable extra debug logging
	 */
	public static boolean debug = false;	
	public static boolean logActionsInSeleniumCoreMethods = false;
	public static boolean trustAllCerts = false;
	
	private static boolean logWidgetObjectNames = false;
	private static boolean logWidgetObjectNamesAsClasses = false;
	private static boolean captureScreenshotsOnWidgetErrors = false;
	private static Process seleniumServerProcess;
	private static WebDriver wdBrowser = null;
	private static String defaultBrowser = "*firefox";
	private static String browser_safari = "*safari";
	private static String browser_safari_proxy = "*safariproxy";
	private static String browser_safari_ipad = "*safari_ipad";
	private static String browser_safari_iphone = "*safari_iphone";
	private static String browser_safari_android = "*safari_android";
	private static String browser_firefox = "*firefox";
	private static String browser_ie = "*iexplore";
	private static String browser_chrome = "*googlechrome";
	private static String browser_opera = "*opera";
	private static String browser_android = "*android";
	private static String browser_iphone = "*iphone";
	private static String browser_ipad = "*ipad";
	private static String browser_embedded = "*" + Platform.gsEmbedded;

	private static String currentBrowser = "";
	private static String userAgent = "";

	private static HashMap object_cache = new HashMap(); 
	
	private static boolean logActions = true;
	public static void logAction(String message)
	{
		if(logActions)
		{
			CoreLogger.info(message);
		}
	}

	public static void disableActionLogging()
	{
		logActions = false;
	}
	
	public static void enableActionLogging()
	{
		logActions = true;
	}
	
	public static boolean shouldLogWidgetObjectNames()
	{
		return logWidgetObjectNames;
	}
	public static boolean shouldLogWidgetObjectNamesAsClasses()
	{
		return logWidgetObjectNamesAsClasses;
	}
	
	public static void enableWidgetObjectNameAsClassesLogging()
	{
		enableWidgetObjectNameLogging();
		logWidgetObjectNamesAsClasses = true;
	}
	public static void enableWidgetObjectNameLogging()
	{
		logWidgetObjectNames = true;
	}
	
	public static void enableScreenshotsOnWidgetErrors()
	{
		captureScreenshotsOnWidgetErrors = true;
	}

	public static HashMap getObjectCache()
	{
		return object_cache;
	}	
	
	/**
	 * returns the current browser name
	 * @return String Current Browser Name
	 * 
	 */
	public static String getCurrentBrowserName()
	{
		return currentBrowser;
	}

	public static void setCurrentBrowser(String browserName)
	{
		CoreLogger.debug("DEBUG: setCurrentBrowser to '" +  browserName + "'");
		if(browserName.toLowerCase().contains("safari")){currentBrowser = browser_safari;}
		if(browserName.toLowerCase().contains("safari") && browserName.toLowerCase().contains("ipad")){currentBrowser = browser_safari;WebBrowser.setSafariToiPadMode();}
		if(browserName.toLowerCase().contains("safari") && browserName.toLowerCase().contains("iphone")){currentBrowser = browser_safari;WebBrowser.setSafariToiPhoneMode();}
		if(browserName.toLowerCase().contains("safari") && browserName.toLowerCase().contains("android")){currentBrowser = browser_safari;WebBrowser.setSafariToAndroidMode();}
		if(browserName.toLowerCase().contains("safari") && browserName.toLowerCase().contains("proxy")){currentBrowser = browser_safari_proxy;}
		if(browserName.toLowerCase().contains("explorer")){currentBrowser = browser_ie;}
		if(browserName.toLowerCase().contains("firefox")){currentBrowser = browser_firefox;}
		if(browserName.toLowerCase().contains("chrome")){currentBrowser = browser_chrome;}
		if(browserName.toLowerCase().contains("opera")){currentBrowser = browser_opera;}
		if(browserName.toLowerCase().contains("android") && !browserName.toLowerCase().contains("safari")){currentBrowser = browser_android;}
		if(browserName.toLowerCase().equals("iphone") && !browserName.toLowerCase().contains("safari")){currentBrowser = browser_iphone;}
		if(browserName.toLowerCase().equals("ipad") && !browserName.toLowerCase().contains("safari")){currentBrowser = browser_ipad;}
		if (browserName.toLowerCase().equals(Platform.gsEmbedded) || browserName.toLowerCase().equals(browser_embedded)) {
			currentBrowser = browser_embedded;
		}
		
		if (browserName.toLowerCase().equals(WebBrowser.gsWebDriverUnitTest.toLowerCase())) {currentBrowser = WebBrowser.gsWebDriverUnitTest;}
		
		CoreLogger.debug("setCurrentBrowser to '" +  browserName + "'");
		CoreLogger.debug("WebBrowser.gsWebDriverUnitTest.toLowerCase() to '" +  WebBrowser.gsWebDriverUnitTest.toLowerCase() + "'");
		
		
		defaultBrowser = currentBrowser;
		}
	
	/**
	 * static method used to return an instance of the getHtmlUnitBrowser class
	 * @return getHtmlUnitBrowser instance
	 * 
	 */
	public static webDriverHelper getHtmlUnitBrowser()
	{
		return new webDriverHelper();
	}

	public static webDriverHelper getWebDriverBrowser()
	{
		return new webDriverHelper();
	}

	public static class webDriverHelper{
		public static final String CHROME_WEBDRIVER_PROPERTY = "webdriver.chrome.driver";
		public static final String IE_WEBDRIVER_PROPERTY = "webdriver.ie.driver";
		
		public WebDriver getWebDriverAPI() {
			return wdBrowser;
		}
		
		public void connectToRemoteMobileDevice(String deviceHostaname, String browserName)
		{
			DesiredCapabilities deviceType = null;
			if(browserName.equals(WebBrowser.gsSafariAsiPhone) || browserName.equals(WebBrowser.gsiPhone))
			{
				deviceType = DesiredCapabilities.iphone();
			}
			
			if(browserName.equals(WebBrowser.gsiPad) || browserName.equals(WebBrowser.gsiPad))
			{
				deviceType = DesiredCapabilities.ipad();
			}
			
			if(browserName.equals(WebBrowser.gsSafariAsAndroid) || browserName.equals(WebBrowser.gsAndroid))
			{
				deviceType = DesiredCapabilities.android();
			}
			
			try {
				wdBrowser = new RemoteWebDriver(new URL("http://" + deviceHostaname + ":3001/wd/hub"),deviceType);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		
		public void start()	{
			CoreLogger.debug("IN START launching " + getWebDriverBrowser().getDisplayName(defaultBrowser));
			start(getWebDriverBrowser().getDisplayName(defaultBrowser));
		}
		
		public void start(String browserName, File ffProfile)
		{
			CoreLogger.debug("BROWSER NAME = " + browserName);
			if(browserName.equals(WebBrowser.gsInternetExplorer))
			{
				wdBrowser = new InternetExplorerDriver();
			}
			// start firefox and add some custom profile preferences...
			if(browserName.equals(WebBrowser.gsMozillaFirefox) || WebBrowser.gsMozillaFirefox.contains(browserName))
			{
				FirefoxProfile profile;
				
				CoreAutomation.Log.debug("ffProfile is null ='" + (ffProfile == null) + "'");
				
				if ((ffProfile == null || !ffProfile.exists()) && (FileHelper.pathExists(Platform.getCurrentProjectPath().replace("\\", "/") + "/lib/ffCertProfile") || FileHelper.pathExists(Platform.getUserHome().replace("\\", "/") + "/lib/ffCertProfile")))
				{
					CoreLogger.debug("SeleniumCore.startSeleniumServer() ffProfile = '" + Platform.getCurrentProjectPath().replace("\\", "/") + "/lib/ffCertProfile'");					
					//File ffProfile = new File(Platform.getCurrentProjectPath().replace("\\", "/") + "/lib/ffCertProfile");
					ffProfile = new File(Platform.getCurrentProjectPath().replace("\\", "/") + "/lib/ffCertProfile");
					if(!ffProfile.exists())
					{
						ffProfile = new File(Platform.getUserHome() + "/lib/ffCertProfile");
					}
					CoreLogger.debug("USING FF PROFILE " + ffProfile.getPath());	
				} 			
				profile = (ffProfile != null && ffProfile.exists()) ? new FirefoxProfile(ffProfile) : new FirefoxProfile();			
				profile.setPreference("extensions.shownSelectionUI", false);
				profile.setPreference("extensions.checkCompatibility", false);
				profile.setPreference("extensions.checkCompatibility.25", false);
				profile.setPreference("extensions.checkCompatibility.24", false);
				profile.setPreference("extensions.checkCompatibility.23", false);
				profile.setPreference("extensions.checkCompatibility.22", false);
				profile.setPreference("extensions.checkCompatibility.21", false);
				profile.setPreference("extensions.checkCompatibility.20", false);
				profile.setPreference("extensions.checkCompatibility.19", false);
				profile.setPreference("extensions.checkCompatibility.18", false);
				profile.setPreference("extensions.checkCompatibility.17", false);
				profile.setPreference("extensions.checkCompatibility.16", false);
				profile.setPreference("extensions.checkCompatibility.15", false);
				profile.setPreference("extensions.checkCompatibility.14", false);
				profile.setPreference("extensions.checkCompatibility.13", false);
				profile.setPreference("extensions.checkCompatibility.12", false);
				profile.setPreference("extensions.checkCompatibility.11", false);
				profile.setPreference("extensions.checkCompatibility.10", false);
				profile.setPreference("extensions.checkCompatibility.4", false);
				profile.setPreference("extensions.checkCompatibility.4.0", false);
				profile.setPreference("extensions.checkCompatibility.4.0b", false);
				profile.setPreference("extensions.checkCompatibility.5", false);
				profile.setPreference("extensions.checkCompatibility.6", false);
				profile.setPreference("extensions.checkCompatibility.7", false);
				profile.setPreference("extensions.checkCompatibility.8", false);
				profile.setPreference("extensions.checkCompatibility.9", false);								
				profile.setPreference("browser.tabs.warnOnClose", false);								
				profile.setPreference("extensions.update.enabled", false);
				profile.setPreference("extensions.shownSelectionUI", false);
				profile.setPreference("extensions.showMismatchUI", false);
				
				// test for LianJiaWeb
				profile.setPreference("capability.principal.certificate.p0.granted", "UniversalXPConnect");
				profile.setPreference("capability.principal.certificate.p0.id", "E3:CC:89:01:B1:96:1F:64:5A:04:7A:47:FD:77:4A:99:B7:00:02:9F");
				profile.setPreference("capability.principal.certificate.p0.subjectName", "CN=International Business Machines Corporation,OU=Lotus Software Group,OU=Digital ID Class 3 - Java Object Signing,O=International Business Machines Corporation,L=Littleton,ST=Massachusetts,C=US");
		
				wdBrowser = new FirefoxDriver(profile);			
			}
			if(browserName.contains(WebBrowser.gsSafari))
			{
				wdBrowser = new SafariDriver();
			}
			if(browserName.equals(WebBrowser.gsGoogleChrome))
			{
				ChromeOptions myChromeOptions = new ChromeOptions();
				myChromeOptions.addArguments("start-maximized");
				myChromeOptions.addArguments("allow-outdated-plugins");
				
				wdBrowser = new ChromeDriver(myChromeOptions);
			}
			if(browserName.equals(WebBrowser.gsWebDriverUnitTest))
			{
				wdBrowser = new HtmlUnitDriver(true);
			}
		}
		
		public void start(String browserName)
		{
			start(browserName, null);
		}
		public void loadURL(String sURL)
		{
			try {
				wdBrowser.get(sURL); 
				SeleniumCore.logAction("Loaded \"" + sURL + "\" using Selenium version " + getSeleniumVersion());
			} catch (Exception e) {
				start();
				//Experienced race condition when launching FF on Rhel need to allow enough time for start
				Platform.sleep(2.0); 
				wdBrowser.get(sURL); 							
				SeleniumCore.logAction("Loaded \"" + sURL + "\" using Selenium version "  + getSeleniumVersion());
			}
		}
		
		private WebElement getElement(String myLocator) throws SeleniumException
		{
			CoreLogger.debug("SeleniumCore.getElement('" + myLocator + "')");

			ArrayList<String> myLocators = new ArrayList<String>();
			if(myLocator.contains("css=") && myLocator.contains("a:contains"))
			{
				myLocator = myLocator.replaceAll("\\(", "\\(partiallinktext=");
				String tempLocators[] = myLocator.split("\\(|\\)");
				for ( int i=0; i < tempLocators.length ; i++)
				{
					tempLocators[i] = tempLocators[i].trim();
					
					//if contains is not the first locator
					if(tempLocators[i].contains("contains") && tempLocators[i].lastIndexOf(" ") != -1)
						myLocators.add(tempLocators[i].substring(0, tempLocators[i].lastIndexOf(" ")).trim());

					//replace all types of quotes for partiallinktext if any
					else if(tempLocators[i].contains("partiallinktext="))
						myLocators.add(tempLocators[i].replaceAll("'|\"", "").trim());

					else if(!tempLocators[i].contains("contains"))
						myLocators.add(tempLocators[i].trim());
				}
			}
			else
				myLocators.add(myLocator);

			myLocator = myLocators.get(0).toString();
			CoreLogger.debug("SeleniumCore.getElement parent ('" + myLocator + "')");
			boolean foundIt = false;
			WebElement myElement = null;
			if(myLocator.startsWith("//"))
			{
				if(!foundIt){try {myElement = wdBrowser.findElement(By.xpath(myLocator)); foundIt = true;} catch (Exception e) {}}				
			}
			
			if(myLocator.startsWith("xpath="))
			{
				if(!foundIt){try {myElement = wdBrowser.findElement(By.xpath(myLocator.replace("xpath=", "")));  foundIt = true;} catch (Exception e) {}}								
			}
			if(myLocator.startsWith("css="))
			{
				CoreLogger.debug("DEBUG: SeleniumCore.getElement('" + myLocator + "') - Should be a CSS Selector");
				
				if(!foundIt){try {myElement = wdBrowser.findElement(By.cssSelector(myLocator.replace("css=", "")));  foundIt = true;} catch (Exception e) {}}								
			}
			if(myLocator.startsWith("id="))
			{
				CoreLogger.debug("DEBUG: SeleniumCore.getElement('" + myLocator + "') - Should be an ID property");
				
				if(!foundIt){try {myElement = wdBrowser.findElement(By.id(myLocator.replace("id=", ""))); foundIt = true;} catch (Exception e) {}}								
			}

			if(myLocator.startsWith("name="))
			{
				CoreLogger.debug("DEBUG: SeleniumCore.getElement('" + myLocator + "') - Should be an Name property");
				
				if(!foundIt){try {myElement = wdBrowser.findElement(By.name(myLocator.replace("name=", ""))); foundIt = true;} catch (Exception e) {}}								
			}

			if(myLocator.startsWith("linktext="))
			{
				if(!foundIt){try {myElement = wdBrowser.findElement(By.linkText(myLocator.replace("linktext=", "")));  foundIt = true;} catch (Exception e) {}}								
			}

			// try these in case the automation engineer has not specified the property name up front
			if(Platform.isFuzzyFindEnabled())
			{
				CoreLogger.debug("DEBUG: SeleniumCore.getElement('" + myLocator + "') - with Fuzzy enabled");
				
				if(!foundIt){try {myElement = wdBrowser.findElement(By.id(myLocator.replace("id=", "")));  foundIt = true;} catch (Exception e) {}}
				if(!foundIt){try {myElement = wdBrowser.findElement(By.name(myLocator.replace("name=", "")));  foundIt = true;} catch (Exception e) {}}
				if(!foundIt){try {myElement = wdBrowser.findElement(By.partialLinkText(myLocator.replace("partiallinktext=", ""))); foundIt = true;} catch (Exception e) {}}
				if(!foundIt){try {myElement = wdBrowser.findElement(By.linkText(myLocator.replace("linktext=", "")));  foundIt = true;} catch (Exception e) {}}				
			}
			CoreLogger.debug("FOUND ELEMENT = " + foundIt);
			//loop the child locators
			for ( int i = 1; i < myLocators.size() ; i++)
			{
				foundIt = false;
				myLocator = myLocators.get(i).toString();
				CoreLogger.debug("SeleniumCore.getElement child "+ i +" ('" + myLocator + "')");
				if(!foundIt){try {myElement = myElement.findElement(By.partialLinkText(myLocator.replace("partiallinktext=", ""))); foundIt = true;} catch (Exception e) {}}
				if(!foundIt){try {myElement = myElement.findElement(By.cssSelector(myLocator.replace("css=", "")));  foundIt = true;} catch (Exception e) {}}
				CoreLogger.debug("FOUND ELEMENT "+i+" = " + foundIt);
			}
			// throw an exception if not found
			if(!foundIt){
				CoreLogger.debug("SeleniumCore.getElement('" + myLocator + "') - NOT FOUND!!!!");
				throw new SeleniumException("Could not find element with locator: "+myLocator);
			}
			return myElement;
		}
		
		public String getDisplayName()
		{
			String myBrowserName = "";
			if(currentBrowser.equals(browser_ie)){myBrowserName = "Internet Explorer";}
			if(currentBrowser.equals(browser_safari)){myBrowserName = "Apple Safari";}
			if(currentBrowser.equals(browser_firefox)){myBrowserName = "Firefox";}
			if(currentBrowser.equals(browser_chrome)){myBrowserName = "Google Chrome";}
			if(currentBrowser.equals(browser_opera)){myBrowserName = "Opera";}
			if(currentBrowser.equals(browser_android)){myBrowserName = "Android";}
			if(currentBrowser.equals(browser_iphone)){myBrowserName = "iPhone";}
			if(currentBrowser.equals(browser_ipad)){myBrowserName = "iPad";}
			if(currentBrowser.equals(browser_safari_ipad)){myBrowserName = "Safari as iPad";}
			if(currentBrowser.equals(browser_safari_iphone)){myBrowserName = "Safari as iPhone";}
			if(currentBrowser.equals(browser_safari_android)){myBrowserName = "Safari as Android";}
			if (currentBrowser.equals(browser_embedded)) {myBrowserName = "Embedded Browser";}
			if (currentBrowser.equals(WebBrowser.gsWebDriverUnitTest)) {myBrowserName = WebBrowser.gsWebDriverUnitTest;}

			return myBrowserName;
		}

		public String getDisplayName(String browserName)
		{
			String myBrowserName = "";
			if(browserName.equals(browser_ie)){myBrowserName = "Internet Explorer";}
			if(browserName.equals(browser_safari)){myBrowserName = "Apple Safari";}
			if(browserName.equals(browser_firefox)){myBrowserName = "Firefox";}
			if(browserName.equals(browser_chrome)){myBrowserName = "Google Chrome";}
			if(browserName.equals(browser_opera)){myBrowserName = "Opera";}
			if(browserName.equals(browser_android)){myBrowserName = "Android";}
			if(browserName.equals(browser_iphone)){myBrowserName = "iPhone";}
			if(browserName.equals(browser_ipad)){myBrowserName = "iPad";}
			if(browserName.equals(browser_safari_ipad)){myBrowserName = "Safari as iPad";}
			if(browserName.equals(browser_safari_iphone)){myBrowserName = "Safari as iPhone";}
			if(browserName.equals(browser_safari_android)){myBrowserName = "Safari as Android";}
			if (browserName.equals(browser_embedded)) {myBrowserName = "Embedded Browser";}
			if (browserName.equals(WebBrowser.gsWebDriverUnitTest)) {myBrowserName = WebBrowser.gsWebDriverUnitTest;}

			return myBrowserName;
		}

		public String getDisplayVersion()
		{		
			CoreLogger.debug("userAgent=" + userAgent);
			String browserVersion = "Unknown";
			try{
				if(currentBrowser.equals(browser_ie)){browserVersion = userAgent.split("MSIE ")[1].split("; Windows")[0];}
				if(currentBrowser.equals(browser_safari) || currentBrowser.equals(browser_safari_ipad) || currentBrowser.equals(browser_safari_iphone)){browserVersion = userAgent.split("Version/")[1].split(" Safari")[0];}
				if(currentBrowser.equals(browser_firefox)){browserVersion = userAgent.split("Firefox/")[1];}
				if(currentBrowser.equals(browser_chrome)){browserVersion = userAgent.split("Chrome/")[1].split(" Safari")[0];}
				if(currentBrowser.equals(browser_opera)){browserVersion = userAgent.split("Opera/")[1].split(" ")[0];}			
			} catch (Exception e){}
			return browserVersion;
		}

		
		public boolean isEnabled(String myLocator)
		{
			CoreLogger.debug("WEBDRIVER isEnabled('" + myLocator + "'");			
			try {
				
				
				return getElement(myLocator).isEnabled();
			} catch (Exception e) {
				
			}
			return false;
		}
		
		public boolean isDisplayed(String myLocator)
		{
			CoreLogger.debug("WEBDRIVER isDisplayed('" + myLocator + "'");			
			try {
				return getElement(myLocator).isDisplayed();
			} catch (Exception e) {
				
			}
			return false;
		}
		
		public boolean exists(String myLocator)
		{
			CoreLogger.debug("WEBDRIVER exists('" + myLocator + "'");			
			boolean foundIt = false;
			
			if(!foundIt){try {getElement(myLocator); foundIt = true;} catch (Exception e) {}}

			return foundIt;
		}
		
		public void click(String myLocator) throws Exception
		{
			CoreLogger.debug("WEBDRIVER click('" + myLocator + "'");

			try {
				getElement(myLocator).click();
			} catch (Exception e) {
				CoreLogger.warn("ERROR CLICKING '" + myLocator + "'");
				CoreLogger.debug(e.getStackTrace().toString());
				throw e;
			}
			
		}
		
		public void rightClick(String myLocator) throws Exception {
			CoreLogger.debug("WEBDRIVER rightClick('" + myLocator + "'");
			try {
				WebElement element = getElement(myLocator);
				Actions actions = new Actions(wdBrowser);
				actions.contextClick(element).perform();
			} catch (Exception e) {
				CoreLogger.warn("ERROR RIGHT CLICKING '" + myLocator + "'");
				CoreLogger.debug(e.getStackTrace().toString());
				throw e;
			}
		}
		
		public void doubleClick(String myLocator) throws Exception {
			CoreLogger.debug("WEBDRIVER doubleClick('" + myLocator + "'");
			try {
				WebElement element = getElement(myLocator);
				Actions actions = new Actions(wdBrowser);
				actions.doubleClick(element).perform();
			} catch (Exception e) {
				CoreLogger.warn("ERROR DOUBLE CLICKING '" + myLocator + "'");
				CoreLogger.debug(e.getStackTrace().toString());
				throw e;
			}
		}
				
		public void setText(String myLocator, String myString) throws Exception
		{
			CoreLogger.debug("WEBDRIVER setText('" + myLocator + "', '" + myString + "'");
			
			try {
				WebElement myTextField = getElement(myLocator);
				myTextField.clear();
				myTextField.sendKeys(myString);
			} catch (Exception e) {
				CoreLogger.warn("ERROR SETTING TEXT IN '" + myLocator + "'");
				CoreLogger.debug(e.getStackTrace().toString());
				throw e;
			}
			
		}


		public String getText(String myLocator) throws Exception
		{
			CoreLogger.debug("WEBDRIVER getText('" + myLocator + "'");
			
			try {
				WebElement myTextField = getElement(myLocator);
				
				
				return myTextField.getAttribute("value");
				
			} catch (Exception e) {
				CoreLogger.warn("ERROR SETTING TEXT IN '" + myLocator + "'");
				CoreLogger.debug(e.getStackTrace().toString());
				//throw e;
			}
			
			return "";
		}

		
		public void select(String myLocator, String myString) throws SeleniumException
		{
			CoreLogger.debug("WEBDRIVER select('" + myLocator + "', '" + myString + "'");
			
			boolean foundIt = false;
			Select mySelect = null;
			try {
				mySelect = new Select(getElement(myLocator));
				foundIt = true;
			} catch (SeleniumException e) {
				CoreLogger.warn("ERROR SELECTING '" + myLocator + "'");
				CoreLogger.debug(e.getStackTrace().toString());
				throw e;
			}
			
			if(foundIt)
			{
				try {
					mySelect.selectByVisibleText(myString);
					return;
				} catch (Exception e) {	}
				
				
				try {
					mySelect.selectByValue(myString);
					return;
				} catch (Exception e) {	}
				
				//fall back options
				try {
					List<WebElement> myListOptions = mySelect.getOptions();
					for (WebElement option : myListOptions) {
						CoreLogger.debug("WEBDRIVER select option('" + option.getText() + "', '" + option.getAttribute("value") + "'");
						if(myString.contains("glob:") && option.getText().contains(myString.replace("glob:", "").replace('*', ' ').trim()))
						{
							option.click();	
							break;
						}
						else if(myString.contains("glob:") && option.getAttribute("value").contains(myString.replace("glob:", "").replace('*', ' ').trim()))
						{
							option.click();	
							break;
						}
						else if(option.getText().equals(myString))
						{
							option.click();	
							break;
						}
						else if(option.getAttribute("value").equals(myString))
						{
							option.click();	
							break;
						}


					}	


				} catch (SeleniumException e) {
					CoreLogger.warn("ERROR SELECTING '" + myString + "' IN CONTROL '" + myLocator + "'");
					CoreLogger.debug(e.getStackTrace().toString());
					throw e;
				}
			}			
		}
		
		public String getSelectedValue(String myLocator)
		{
			CoreLogger.debug("WEBDRIVER getSelectedValue('" + myLocator + "'");
			
			try {
				return new Select(getElement(myLocator)).getFirstSelectedOption().getAttribute("value");

			} catch (SeleniumException e) {
				CoreLogger.warn("ERROR GETTING SELECTED VALUE '" + myLocator + "'");
				CoreLogger.debug(e.getStackTrace().toString());
			}
			return null;
		}
		
		public String getSelectedText(String myLocator)
		{
			CoreLogger.debug("WEBDRIVER getSelectedText('" + myLocator + "'");
			
			try {
				return new Select(getElement(myLocator)).getFirstSelectedOption().getText();

			} catch (SeleniumException e) {
				CoreLogger.warn("ERROR GETTING SELECTED TEXT '" + myLocator + "'");
				CoreLogger.debug(e.getStackTrace().toString());
			}
			return null;
		}
		
		public boolean waitForElement(String myLocator)
		{
			for (int i = 0; i < Platform.giLongTO; i++) {
				Platform.sleep(1);
				if(exists(myLocator))
				{
					break;
				}
			}
			return exists(myLocator);
		}
		
		public  void waitForWindow(String windowName)
		{
			boolean foundWindow = false;
			for (int i = 0; i < Platform.giLongTO; i++) {
				Platform.sleep(1);
				if(foundWindow){break;}
				try {
					
					Set<String> myString = wdBrowser.getWindowHandles();
					Iterator<String> myWindows = myString.iterator();
					while(myWindows.hasNext())
					{
						wdBrowser.switchTo().window(myWindows.next().toString());
						CoreLogger.debug("WINDOW TITLE: " + SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getTitle());
						CoreLogger.debug("GETPAGE TITLE: " + SeleniumCore.getWebDriverBrowser().getPageTitle());
						if(wdBrowser.getTitle().equals(windowName) || getPageTitle().equals(windowName))
						{
							foundWindow = true;
							break;
						}					
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	
		public String getPageTitle()
		{
			// the default api appears to be broken, here's a workaround
			String myPageTitle = "";
			try {
				String myPageSource = wdBrowser.getPageSource().toString();
				myPageTitle = myPageSource.split("</title>")[0].split("<title>")[1].trim();
				
			} catch (Exception e) {
			}
			return myPageTitle;
		}
		/**
		 * Returns the Text inside of an element
		 * @param myLocator The locator of the HTML element
		 * @return The text in between the tags of the HTML element
		 */
		public String getInnerText(String myLocator) {
			String ret = null;
			try {
				WebElement we = getElement(myLocator);
				ret = we.getText();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ret;
		}

		
		public void waitForMatchingElement(String myLocator, int timeout) {
			Wait<WebDriver> wait = new WebDriverWait(getWebDriverAPI(), timeout);
			wait.until(visibilityOfElementLocated(myLocator));	
		}

		private ExpectedCondition<WebElement> visibilityOfElementLocated(final String myLocator) {
			return new ExpectedCondition<WebElement>() {
			    public WebElement apply(WebDriver driver) {
			      WebElement toReturn;
				try {
					toReturn = getElement(myLocator);
					// If not found getElement throws an exception
				    return toReturn;
				      
				} catch (Exception e) {
					Platform.sleep(0.5);
				}
			      return null;
			    }
			  };
		}
		
		public String getPropertyValue(String myLocator, String propertyName) {
			String ret = "";
			try {
				WebElement we = getElement(myLocator);
				ret = we.getAttribute(propertyName);
			} catch (Exception e) {
			}
			return ret;
		}
	}
	
	public static void switchWebDriverBrowser(WebDriver myWebDriverInstance)
	{
		wdBrowser = myWebDriverInstance;
	}

	public static void logMethodStub(String methodName)
	{
		System.out.println("ATTENTION!  The method '" + methodName + "' is an auto-generated method stub and not yet supported for WebCore tests.  Please join the Community Source project and help add support for this method...");
	}
	
	public static void debug(String message)
	{
		CoreLogger.debug(message);
	}
	
	public static String getCurrentMethodName()
	{
		int numberOfClasses = new Throwable().fillInStackTrace().getStackTrace().length;
		String myMethodName = "";
		for (int currentClass = 0; currentClass < numberOfClasses; currentClass++)
		{
			if(new Throwable().fillInStackTrace().getStackTrace()[currentClass].getMethodName().contains("getCurrentMethodName"))
			{
				String myClass = new Throwable().fillInStackTrace().getStackTrace()[currentClass + 1].getClassName();
				return myClass + "." + new Throwable().fillInStackTrace().getStackTrace()[currentClass + 1].getMethodName() + "()";
			}
		}
		return myMethodName;
	}
	
	public static class getLocatorHelpers{		
		private static String getFoundCSSLocator(String myLocator, boolean returnExists) {
			CoreLogger.debug("SeleniumCore.getFoundCSSLocator() - CSS LOCATORS ARE DEFAULT");
			
			boolean exists = false;
			String fullLocator = myLocator;

			if (myLocator.startsWith("//"))	{
				return "NOT_FOUND_TRY_XPATH";
			}
						
			if(myLocator.startsWith("css="))
			{
				if (!exists){
					try{
						exists = SeleniumCore.getWebDriverBrowser().isDisplayed(myLocator);
				
						if (exists){
							fullLocator= myLocator;
						}
					} catch (Exception e){
						
					}
				}				
			}			
			
			String myCSSString = "css=a:contains("+ myLocator + ")";
			if (!exists){
				try{
					exists = SeleniumCore.getWebDriverBrowser().isDisplayed(myCSSString);
					if (exists){
						fullLocator= myCSSString;
					}
				} catch (Exception e){
					
				}
			}

			myCSSString = "css=span:contains("+ myLocator + ")";
			if (!exists){
				try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(myCSSString);
					if (exists){
						fullLocator= myCSSString;
					}
				} catch (Exception e){
					
				}
			}
			myCSSString = "css=input:contains("+ myLocator + ")";
			if (!exists){
				try{
					exists = SeleniumCore.getWebDriverBrowser().isDisplayed(myCSSString);
					if (exists){
						fullLocator= myCSSString;
					}
				} catch (Exception e){
					
				}
			}

			myCSSString = "css=radio:contains("+ myLocator + ")";
			if (!exists){
				try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(myCSSString);
					if (exists){
						fullLocator= myCSSString;
					}
				} catch (Exception e){
					
				}
			}

			myCSSString = "css=img:contains("+ myLocator + ")";
			if (!exists){
				try{
					exists = SeleniumCore.getWebDriverBrowser().isDisplayed(myCSSString);
					if (exists){
						fullLocator= myCSSString;
					}
				} catch (Exception e){
					
				}
			}
			
			CoreLogger.debug("SeleniumCore.getFoundCSSLocator(\"" +myLocator.replace("-=-=-","|")+ "\") returning fullLocator '" +fullLocator+ "'");
			
			if(returnExists && exists) {
				fullLocator = "LOCATOR_EXISTS: " + fullLocator;
			}			
			
			return fullLocator;		
		}

		public static String getFoundLocator(String myLocator) {
			return getFoundLocator(myLocator,false);
		}

		public static String getFoundLocator(String myLocator, boolean returnExists) {
			CoreLogger.debug("SeleniumCore.getFoundLocator(\"" +myLocator + "\")");
			boolean exists = false;			
			
			if(!Platform.isExistsCheckEnabled()) {
				return myLocator;
			}

			if(myLocator.startsWith("css=")) {
				String myCSSLocator =  getFoundCSSLocator(myLocator, returnExists);
				if(!myCSSLocator.contains("NOT_FOUND_TRY_XPATH")) {
					return myCSSLocator;
				} else {
					return myLocator;
				}
			}
			
			if(Platform.isCSSDefaultLocatorType()) {
				String myCSSLocator =  getFoundCSSLocator(myLocator, returnExists);
				CoreLogger.debug("SeleniumCore.getFoundLocator - CSS RETURNS: " + myCSSLocator + "'");
				if(!myCSSLocator.contains("NOT_FOUND_TRY_XPATH")) {
					return myCSSLocator;
				}
			}
			
			if(myLocator.startsWith("css=")) {
				String myCSSLocator =  getFoundCSSLocator(myLocator, returnExists);
				if(!myCSSLocator.contains("NOT_FOUND_TRY_XPATH")) {
					return myCSSLocator;
				} else {
					return myLocator;
				}
			}			
			
			// if IE, swap out the input
			if(WebBrowser.isInternetExplorer()) {
				myLocator = myLocator.replace("Html.INPUT.text", "*");
			}
			
			String fullLocator = myLocator;
			
			if(Platform.isWidgetCacheEnabled()) {
				if(getObjectCache().containsKey(myLocator))	{
					CoreLogger.debug("FOUND WIDGET IN CACHE!!! '" + myLocator + "'");

					String cachedLocator = getObjectCache().get(myLocator).toString();				
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(cachedLocator);if (exists){fullLocator= cachedLocator;}} catch (Exception e){}}
				}				
			}			
			
			// try to find it as is - this will speed up location when we have XPath locators defined already...
			if(myLocator.startsWith("//")) {
				if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(myLocator);if (exists){fullLocator= myLocator;}} catch (Exception e){}}				
			}
			
			// Check to see if we have a regular expression
			if (myLocator.contains("|") && !exists)	{
				myLocator = myLocator.replace("|", "-=-=-");
				String[] myLocators = myLocator.split("-=-=-");
				CoreLogger.debug("SeleniumCore.getFoundLocator was passed an 'or' Regular Expression that contains " + myLocators.length + " locators...");
				for(int myIteration = 0; myIteration < myLocators.length; myIteration++) {
					
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocators[myIteration], "*", "@id"));if (exists){fullLocator= getXPathString(myLocators[myIteration], "*", "@id");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocators[myIteration], "*", "@name"));if (exists){fullLocator= getXPathString(myLocators[myIteration], "*", "@name");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocators[myIteration], "*", "@href"));if (exists){fullLocator= getXPathString(myLocators[myIteration], "*", "@href");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocators[myIteration], "radio", "@value"));if (exists){fullLocator= getXPathString(myLocators[myIteration], "radio", "@value");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocators[myIteration], "*", "@value"));if (exists){fullLocator= getXPathString(myLocators[myIteration], "*", "@value");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocators[myIteration], "a", "text()"));if (exists){fullLocator= getXPathString(myLocators[myIteration], "a", "text()");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocators[myIteration], "*", "text()"));if (exists){fullLocator= getXPathString(myLocators[myIteration], "*", "text()");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(myLocators[myIteration]);if (exists){fullLocator= myLocators[myIteration];}} catch (Exception e){}}
				}
			} else {
				if(Platform.isFuzzyFindEnabled()) {
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocator, "*", "@id"));if (exists){fullLocator= getXPathString(myLocator, "*", "@id");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocator, "*", "@name"));if (exists){fullLocator= getXPathString(myLocator, "*", "@name");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocator, "*", "@href"));if (exists){fullLocator= getXPathString(myLocator, "*", "@href");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocator, "radio", "@value"));if (exists){fullLocator= getXPathString(myLocator, "radio", "@value");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocator, "*", "@value"));if (exists){fullLocator= getXPathString(myLocator, "*", "@value");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocator, "*", "@data"));if (exists){fullLocator= getXPathString(myLocator, "*", "@data");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocator, "*", "@textContent"));if (exists){fullLocator= getXPathString(myLocator, "*", "@textContent");}} catch (Exception e){}}
					
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocator, "a", "text()"));if (exists){fullLocator= getXPathString(myLocator, "a", "text()");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(getXPathString(myLocator, "*", "text()"));if (exists){fullLocator= getXPathString(myLocator, "*", "text()");}} catch (Exception e){}}
					if (!exists){try{exists = SeleniumCore.getWebDriverBrowser().isDisplayed(myLocator);if (exists){fullLocator= myLocator;}} catch (Exception e){}}			
					
				}
			}
			CoreLogger.debug("SeleniumCore.getFoundLocator(\"" +myLocator.replace("-=-=-","|")+ "\") returning fullLocator '" +fullLocator+ "'");

			if(returnExists && exists) {
				fullLocator = "LOCATOR_EXISTS: " + fullLocator;
			}
			
			return fullLocator;				
		}
		
		private static String getXPathString(String mySearchString, String myClassName, String myPropertyName) {
		
			CoreLogger.debug("SeleniumCore.getXPathString() mySearchString ='" +mySearchString+ "'");
			CoreLogger.debug("SeleniumCore.getXPathString() myClassName ='" +myClassName+ "'");
			CoreLogger.debug("SeleniumCore.getXPathString() myPropertyName ='" +myPropertyName+ "'");
			
			// if the .* is just on the end, chop it off as Selenium already handles that for us
			if (mySearchString.endsWith(".*")) {
				mySearchString = mySearchString.substring(0, mySearchString.length() - 2);
			}
			
			// if the .* is just on the front, chop it off as Selenium already handles that for us
			if (mySearchString.startsWith(".*")) {
				mySearchString = mySearchString.substring(2, mySearchString.length());
			}
			
			CoreLogger.debug("SeleniumCore.getXPathString() mySearchString ='" +mySearchString+ "'");			
			
			if (mySearchString.contains(".*")) {
				CoreLogger.debug("SeleniumCore.getXPathString() mySearchString.contains(.*) = true'");
				
				String myReturnString = "//" + myClassName + "[contains(" + myPropertyName + ", '";
				// have to replace the .* as split won't split using it!
				mySearchString = mySearchString.replace(".*", "#=#=#=#");
				
				for (int i = 0; i < mySearchString.split("#=#=#=#").length; i++) {
					if (i == 0) {
						myReturnString = myReturnString + mySearchString.split("#=#=#=#")[i]  + "')";					
					} else {
						myReturnString = myReturnString + " and contains(" + myPropertyName + ", '"+ mySearchString.split("#=#=#=#")[i]  + "')";
					}
				}
				myReturnString = myReturnString + "]";
				CoreLogger.debug("SeleniumCore.getXPathString() returning XPath '" + myReturnString + "'");
				
				return myReturnString;
				
			} else {
				CoreLogger.debug("SeleniumCore.getXPathString() mySearchString.contains(.*) = false'");
				if(myPropertyName.contains("id")) {
					return "//" + myClassName + "[@id='" + mySearchString + "']";					
				}				
				return "//" + myClassName + "[contains(" + myPropertyName + ", '" + mySearchString + "')]";
			}
		}

		public static String getSearchStringFromXPath(String myXPath) {			
			if(!myXPath.contains("'")) {
				return myXPath;
			}
			String mySearchString = myXPath.split("'")[1];
			
			if(mySearchString.length() == 0) {
				mySearchString = myXPath;
			}
			return mySearchString;
		}
	
		public static String getCSSSelectorFromXPathLocator(String myXPath) {		
			return myXPath;
		}
	}
	
	public static String getSeleniumVersion()
	{
		String VERSION = "unknown";
	    try {
			final Properties p = new Properties();
		    InputStream stream = SeleniumServer.class.getClass().getResourceAsStream("/VERSION.txt");
			p.load(stream);
			VERSION =  p.getProperty("selenium.core.version") + p.getProperty("selenium.core.revision");
		} catch (IOException e) {
		}
		return VERSION;	  
	}	
}
