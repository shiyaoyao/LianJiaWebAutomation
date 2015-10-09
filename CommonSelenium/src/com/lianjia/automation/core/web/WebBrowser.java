package com.lianjia.automation.core.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Set;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.lianjia.automation.CoreAutomation;
import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.loggers.control.CoreLogger;
import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

public class WebBrowser {	
	private static boolean isPortrait = true;
	private static boolean isHighRes = true;
	private static boolean isSafariAsiPad = false;
	private static boolean isSafariAsiPhone = false;
	private static boolean isSafariAsAndroid = false;
	private static String WEB_BROWSER_NAME = "";
	
	/**
	 * sets focus to the browser window 
	 */
	public static void activate() {
		new WebDriverBackedSelenium(SeleniumCore.getWebDriverBrowser().getWebDriverAPI(), WebBrowser.getCurrentBrowserURL()).windowFocus();
	}

	/**
	 * closes the web browser window
	 */
	public static void close() {
		SeleniumCore.getWebDriverBrowser().getWebDriverAPI().close();
	}
	
	public static void shutdown(boolean shutDownSeleniumServer)
	{
		SeleniumCore.getWebDriverBrowser().getWebDriverAPI().quit();
	}
	
	/**
	 * maximizes the browser window
	 */
	public static void maximize() {
		//In ChromeDriver we pass the start-maximized flag
		if(!isChrome()){
			//This line causes the linux ChromeDriver to crash
			SeleniumCore.getWebDriverBrowser().getWebDriverAPI().manage().window().maximize();
		}	
	}

	/**
	 * makes the browser go back one page
	 */
	public static void back() {
		SeleniumCore.getWebDriverBrowser().getWebDriverAPI().navigate().back();
	}
	
	/**
	 * loads a url - starts the browser if needed
	 * @param url - url to load
	 */
	public static void loadUrl(String url) {
		SeleniumCore.getWebDriverBrowser().loadURL(url);
		maximize();
	}

	/**
	 * determines if the text supplied is found on the browser page
	 * @param textToVerify
	 * @return true/false if text is found
	 */
	public static boolean isTextPresent(String textToVerify)
	{
		boolean foundIt = false;
		
		foundIt = WebBrowser.getBodyText().contains(textToVerify);
		if(!foundIt)
		{
			new WebDriverBackedSelenium(SeleniumCore.getWebDriverBrowser().getWebDriverAPI(), WebBrowser.getCurrentBrowserURL()).isTextPresent(textToVerify);
		}
		
		return foundIt;			
	}
	
	public static String getEval(String javaScriptSnippit)
	{
		return ((JavascriptExecutor) SeleniumCore.getWebDriverBrowser().getWebDriverAPI()).executeScript("return " + javaScriptSnippit + ";").toString();
		
	}
	
	/**
	 * waits for the number of seconds supplied for the text to exist
	 * @param textToVerify
	 * @param secondsToWait
	 * @return true/false if found
	 */
	public static boolean waitForText(String textToVerify, int secondsToWait)
	{
		boolean foundText = false;
		for (int i = 0; i < secondsToWait; i++) {
			try {
				if(isTextPresent(textToVerify))
				{
					foundText = true;
					break;
				} else {
					Platform.sleep(1);
				}
			} catch (Exception e) {
				Platform.sleep(1);
			}
		}
		return foundText;
	}
	
	/**
	 * waits for the number of seconds supplied for the text to go away
	 * @param textToVerify
	 * @param secondsToWait
	 * @return true/false if found
	 */
	public static boolean waitForTextToNotExist(String textToVerify, int secondsToWait)
	{
		boolean foundText = true;
		for (int i = 0; i < secondsToWait; i++) {
			try {
				if(isTextPresent(textToVerify))
				{
					foundText = true;
					Platform.sleep(1);
				} else {
					foundText = false;
					break;
				}
			} catch (Exception e) {
			}
		}
		return foundText;
	}
	
	/**
	 * loads a url - starts the browser if needed - overloaded for RFT source compatibility
	 * @param url - url to load
	 */
	public static void loadURL(String url) {
		loadUrl(url);		
	}
	
	/**
	 * selects the window - used to select a secondary (child) window
	 * to go back to the main window, use null for the windowTitle
	 * @param windowTitle
	 */
	public static void selectWindow(String windowTitle)
	{
		SeleniumCore.getWebDriverBrowser().getWebDriverAPI().switchTo().window(windowTitle);
	}
	
	/**
	 * refreshes the browser page - please note that you may have to handle a popup window
	 */
	public static void refresh()
	{
		SeleniumCore.getWebDriverBrowser().getWebDriverAPI().navigate().refresh();
	}
	
	/**
	 * used to switch to a different browser frame
	 * @param frameName
	 */
	public static void selectFrame(String frameName)
	{
		SeleniumCore.getWebDriverBrowser().getWebDriverAPI().switchTo().frame(frameName);
	}
	
	/**
	 * used to switch to a different browser - allows you to test in multiple browsers at the same time on the same system
	 * @param mySeleniumInstance
	 */
	public static void switchBrowser(WebDriver myWebDriverInstance)
	{
		SeleniumCore.switchWebDriverBrowser(myWebDriverInstance);
	}
	
	/**
	 * starts a browser and returns an instance of that browser
	 * experimental support for creating and using multiple WebDriver Browsers
	 * WILL NOT WORK WITH IE - ONLY FIREFOX!!!
	 * @param browserName
	 * @return
	 */
	public static WebDriver startWebDriver(String browserName)
	{
		SeleniumCore.getWebDriverBrowser().start(browserName);
		return SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
	}
	
	/**
	 * starts a browser and returns an instance of that browser using a specific FF Profile
	 * experimental support for creating and using multiple WebDriver Browsers
	 * WILL NOT WORK WITH IE - ONLY FIREFOX!!!
	 * @param browserName
	 * @return
	 */
	public static WebDriver startWebDriver(String browserName, File ffProfile)
	{
		SeleniumCore.getWebDriverBrowser().start(browserName, ffProfile);
		return SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
	}
	
	/**
	 * support for using the iPhoneDriver and AndroidDriver to test mobile web apps using Selenium
	 */
	public static class MobileDevices {
		private static String DEVICE_IPAD = "iPad";
		private static String DEVICE_IPAD_RETINA = "iPad (Retina)";
		private static String DEVICE_IPAD_MINI = "iPad";
		private static String DEVICE_IPHONE = "iPhone";
		private static String DEVICE_IPHONE4 = "iPhone (Retina 3.5-inch)";
		private static String DEVICE_IPHONE5 = "iPhone (Retina 4-inch)";
			
		public static class iPad {
			
			public static void start()
			{
				launchiOSSimulator(DEVICE_IPAD);
				connectToRemoteMobileDevice("localhost", WebBrowser.gsiPad);
			}
			
			public static void shutdown()
			{
				killiOSSimulator();
			}
			
		}
		public static class iPadMini {
	
			public static void start()
			{
				launchiOSSimulator(DEVICE_IPAD_MINI);				
				connectToRemoteMobileDevice("localhost", WebBrowser.gsiPad);
			}
			
			public static void shutdown()
			{
				killiOSSimulator();
			}
			
		}
		
		public static class iPadRetina {
			
			public static void start()
			{
				launchiOSSimulator(DEVICE_IPAD_RETINA);	
				connectToRemoteMobileDevice("localhost", WebBrowser.gsiPad);
			}
			
			public static void shutdown()
			{
				killiOSSimulator();
			}
			
		}
		
		public static class iPhone {
			public static void start()
			{
				launchiOSSimulator(DEVICE_IPHONE);	
				connectToRemoteMobileDevice("localhost", WebBrowser.gsiPhone);
			}
			
			public static void shutdown()
			{
				killiOSSimulator();
			}
		}
		public static class iPhone4 {
			public static void start()
			{
				launchiOSSimulator(DEVICE_IPHONE4);	
				connectToRemoteMobileDevice("localhost", WebBrowser.gsiPhone);
			}
			
			public static void shutdown()
			{
				killiOSSimulator();
			}
		}

		public static class iPhone5 {
			public static void start()
			{
				launchiOSSimulator(DEVICE_IPHONE5);	
				connectToRemoteMobileDevice("localhost", WebBrowser.gsiPhone);
			}
			
			public static void shutdown()
			{
				killiOSSimulator();
			}
		}
		
		/**
		 * kills the iOS simulator
		 */
		private static void killiOSSimulator()
		{
			if(Platform.isMac() && CoreAutomation.Files.fileExists("/Applications/Xcode.app"))
			{
				try {Runtime.getRuntime().exec(new String[]{"/usr/bin/killall","iPhone Simulator"}, null, null);} catch (Exception e) {}				
			}
		}
		
		/**
		 * launches the iOS simulator setting the appropriate device
		 * expects iWebDriver.app to be installed in /Applications
		 */
		private static void launchiOSSimulator(String deviceToUse)
		{
			// set the device / runtime type as an Apple device...
			setAppleDevice();
			
			if(!CoreAutomation.Files.fileExists("/Applications/Xcode.app"))
			{
				CoreAutomation.Log.error("COULD NOT FIND XCODE!  You must have Xcode installed in /Applications/Xcode.app to use the iOS Simulator Support");
			}
			
			File tmpFile;
			String scaleToUse = "1";
			String windowOrientation = "3";

			if(deviceToUse.toLowerCase().contains("retina"))
			{
				deviceToUse = "'" + deviceToUse + "'";
				scaleToUse = "\"0.75\"";
			}
			
			CoreAutomation.Log.info("Launching iOS Simulator as " + deviceToUse + ", please wait...");
			
			try {
				tmpFile = File.createTempFile("file",".sh");
				tmpFile.deleteOnExit();
				
		        BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile));
		        
		    	out.write("#!/bin/sh\n");
		    	out.write("/usr/bin/defaults write com.apple.iphonesimulator SimulateDevice \"" +deviceToUse+"\"\n");
		    	
		    	out.write("/usr/bin/defaults write com.apple.iphonesimulator windowOrientation " +windowOrientation+"\n");
		    	out.write("/usr/bin/defaults write com.apple.iphonesimulator SimulatorWindowLastScale " +scaleToUse+"\n");

		    	out.write("/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/Applications/iPhone\\ Simulator.app/Contents/MacOS/iPhone\\ Simulator -SimulateApplication /Applications/iWebDriver.app/iWebDriver\n");
		    	out.close();
		    				
				//Change permissions to execute newly created script
		    	Runtime.getRuntime().exec("chmod 755 "+tmpFile.getAbsolutePath());

		    	//Execute Script
		    	Runtime.getRuntime().exec(tmpFile.getAbsolutePath());			
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
	    	Platform.sleep(15);
		}
				
		/**
		 * connects to an iOS or Android simulator running the WebDriver driver and returns an instance of that browser
		 * remote support is untested, primarily used with localhost - tests and simulator are running from the same system
		 * @param browserName
		 * @return
		 */
		private static WebDriver connectToRemoteMobileDevice(String deviceHostname, String browserName)
		{
			WebBrowser.setEngineToSeleniumWebDriver();
			SeleniumCore.getWebDriverBrowser().connectToRemoteMobileDevice(deviceHostname, browserName);
			return SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
		}
	}
	
	/**
	 * returns the html source of the current page
	 * @return string containing the html source
	 */
	public static String getBodyText()
	{			
		return new WebDriverBackedSelenium(SeleniumCore.getWebDriverBrowser().getWebDriverAPI(), WebBrowser.getCurrentBrowserURL()).getBodyText();
	}
	
	/**
	 * returns the html source of the current page
	 * @return string containing the html source
	 */
	public static String getHtmlSource()
	{
		return SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getPageSource();			
	}
	
	/**
	 * returns the html source of a provided url.
	 * navigates to the new url to retrieve the source and
	 * returns back to the previous page
	 * @param url URL to retrieve HTML source
	 * @return string containing the html source
	 */
	public static String getHtmlSource(String url)
	{
		loadUrl(url);
		String source =  getHtmlSource();
		back();
		return source;
	}
	
	/**
	 * returns the html source of the current page
	 * @return string containing the html source
	 */
	public static String getCurrentBrowserURL()
	{
		return  SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getCurrentUrl();			
	}
	
	public static void openNewWindow(String url, String windowTitle)
	{
		new WebDriverBackedSelenium(SeleniumCore.getWebDriverBrowser().getWebDriverAPI(), WebBrowser.getCurrentBrowserURL()).openWindow(url, windowTitle);
	}
	
	public static String waitForPopupWindow(String myWindowName)
	{
		String myPopupWindow = "";
		for (int i = 0; i < 30; i++) {
			myPopupWindow = getPopupWindow(myWindowName);
			if(myPopupWindow.equals(""))
			{
				Platform.sleep(1);
			} else {
				break;
			}
		}
		return myPopupWindow;
	}

	private static String getPopupWindow(String myWindowName)
	{
		try {
			Set<String> myWindowHandles = SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getWindowHandles();
			if (SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getWindowHandles().size() >= 1)
			{
				for(String currentWindowHandle : myWindowHandles){
					SeleniumCore.getWebDriverBrowser().getWebDriverAPI().switchTo().window(currentWindowHandle);
					if (SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getTitle().equals(myWindowName))
					{
						return myWindowName;
					}
				}
			}
			
		} catch (Exception e) {
		}
		return null;
	}

	public static void setEngineToSeleniumWebDriver()
	{
		Platform.setEngineToSeleniumWebDriver();
	}

	public static void setWebBrowser(String browserName, boolean updateVisualReporterPropertyFile)
	{
		CoreLogger.info("Setting Browser to: " + browserName);
		SeleniumCore.setCurrentBrowser(browserName);
		WEB_BROWSER_NAME = browserName;

	}
	
	public static void setWebBrowser(String browserName)
	{
		setWebBrowser(browserName, false);
	}

	public static void setBrowserToChrome()
	{
		setWebBrowser(gsGoogleChrome);
	}

	public static void setBrowserToSafari()
	{
		setWebBrowser(gsSafari);
	}

	public static void setBrowserToSafariAsiPhone()
	{
		setWebBrowser(gsSafariAsiPhone);
	}

	public static void setBrowserToSafariAsiPad()
	{
		setWebBrowser(gsSafariAsiPad);
	}

	public static void setBrowserToSafariAsAndroid()
	{
		setWebBrowser(gsSafariAsAndroid);
	}
	
	public static void setBrowserToIE()
	{
		setWebBrowser(gsInternetExplorer);
	}

	public static void setBrowserToIE8()
	{
		setWebBrowser(gsInternetExplorer8);
	}
	public static void setBrowserToIE9()
	{
		setWebBrowser(gsInternetExplorer9);
	}

	public static void setBrowserToFirefox()
	{
		setWebBrowser(gsMozillaFirefox);
	}
	
	public static void setBrowserToWebDriverUnitTestBrowser()
	{
		setWebBrowser(gsWebDriverUnitTest);
	}
	
	
	public static void setBrowserToiPhone()
	{
		setWebBrowser(gsiPhone);
	}
	
	public static void setBrowserToiPad()
	{
		setWebBrowser(gsiPad);
	}
	
	public static void setBrowserToAndroid()
	{
		setWebBrowser(gsAndroid);
	}

	/**Global string for Mozilla Firefox Browser*/
	public static String gsMozillaFirefox = "Mozilla Firefox";

	/**Global string for Microsoft Internet Explorer Browser*/
	static String gsInternetExplorer = "Internet Explorer";

	/**Global string for Microsoft Internet 8.0 Explorer Browser*/
	private static String gsInternetExplorer8 = "Internet Explorer 8.0";

	/**Global string for Microsoft Internet 9.0 Explorer Browser*/
	private static String gsInternetExplorer9 = "Internet Explorer 9.0";

	/**Global string for Safari Browser*/
	public static String gsSafari = "Safari";

	/**Global string for Chrome Browser*/
	public static String gsGoogleChrome = "Google Chrome";

	/**Global string for Opera Browser*/
	public static String gsOpera = "Opera";

	/**Global string for the WebDriver UnitTest Browser*/	
	public static String gsWebDriverUnitTest = "wdunit";	
	
	/**Global string for Android Browser*/
	public static String gsAndroid = "Android";

	/**Global string for iPhone Browser*/
	public static String gsiPhone = "iPhone";

	/**Global string for iPad Browser*/
	public static String gsiPad = "iPad";

	
	/**Global string for Safari as iPad Browser*/
	public static String gsSafariAsiPad = "SafariAsiPad";
	/**Global string for Safari as iPhone Browser*/
	public static String gsSafariAsiPhone = "SafariAsiPhone";

	/**Global string for Safari as Android Browser*/
	public static String gsSafariAsAndroid = "SafariAsAndroid";

	/**Global string for Safari in proxy mode for Windows & Safari 5 defect*/
	@SuppressWarnings("unused")
	private static String gsSafariProxy = "SafariProxy";

	private static boolean MOBILE_DEVICE_IOS = false;
	private static boolean MOBILE_DEVICE_ANDROID = false;
	
	public static void setAppleDevice()
	{
		MOBILE_DEVICE_IOS = true;
	}

	public static void setAndroidDevice()
	{
		MOBILE_DEVICE_ANDROID = true;
	}

	public static boolean isAndroidDevice()
	{
		return MOBILE_DEVICE_ANDROID;
	}
	
	public static boolean isAppleDevice()
	{
		return MOBILE_DEVICE_IOS;
	}
	
	/**
	 * returns the web browser name
	 */
	public static String getWebBrowserName()
	{
		return WEB_BROWSER_NAME;
	}

	/**
	 * returns true/false if browser is Safari
	 */
	public static boolean isSafari()
	{
		return getWebBrowserName().toLowerCase().contains(gsSafari.toLowerCase());
	}
	
	/**
	 * returns true/false if Safari is running on Mac OS X
	 */
	public static boolean isSafariOnMac()
	{
		if(getWebBrowserName().toLowerCase().contains(gsSafari.toLowerCase()) && Platform.isMac())
		{
			return true;
		}
		return false;
	}

	/**
	 * returns true/false if Safari is running on Windows
	 */
	public static boolean isSafariOnWindows()
	{
		if(getWebBrowserName().toLowerCase().contains(gsSafari.toLowerCase()) && Platform.isWindows())
		{
			return true;
		}
		return false;
	}

	/**
	 * returns true/false if browser is Chrome
	 */
	public static boolean isChrome()
	{
		return getWebBrowserName().toLowerCase().contains(gsGoogleChrome.toLowerCase());    	
	}

	/**
	 * returns true/false if browser is Opera - OPERA SUPPORT IS EXPERIMENTAL
	 */
	public static boolean isOpera()
	{
		return getWebBrowserName().equalsIgnoreCase(gsOpera);
	}

	/**
	 * returns true/false if browser is Firefox
	 */
	public static boolean isFirefox()
	{
		return gsMozillaFirefox.toLowerCase().contains(getWebBrowserName().toLowerCase());
	}

	/**
	 * returns true/false if browser is IE
	 */
	public static boolean isInternetExplorer()
	{
		return getWebBrowserName().equalsIgnoreCase(gsInternetExplorer);
	}
	
	/**
	 * returns true/false if browser is IE 8
	 */
	public static boolean isInternetExplorer8()
	{
		return getWebBrowserName().equalsIgnoreCase(gsInternetExplorer8);
	}

	/**
	 * returns true/false if browser is IE 9
	 */
	public static boolean isInternetExplorer9()
	{
		return getWebBrowserName().equalsIgnoreCase(gsInternetExplorer9);
	}

	/**
	 * returns true/false if running on Android simulator - EXPERIMENTAL
	 */
	public static boolean isAndroid()
	{
		return getWebBrowserName().equalsIgnoreCase(gsAndroid);
	}

	/**
	 * returns true/false if running on iPhone simulator - EXPERIMENTAL
	 */
	public static boolean isiPhone()
	{
		return getWebBrowserName().equalsIgnoreCase(gsiPhone);
	}

	/**
	 * returns true/false if running on iPad simulator - EXPERIMENTAL
	 */
	public static boolean isiPad()
	{
		return getWebBrowserName().equalsIgnoreCase(gsiPad);
	}

	/**
	 * returns true/false if Safari is running in iPad emulation
	 */
	public static boolean isSafariAsiPad()
	{
		return isSafariAsiPad;   	
	}

	/**
	 * returns true/false if Safari is running in iPhone emulation
	 */
	public static boolean isSafariAsiPhone()
	{
		return isSafariAsiPhone; 	
	}
	
	/**
	 * returns true/false if Safari is running in iPhone emulation
	 */
	public static boolean isSafariAsAndroid()
	{
		return isSafariAsAndroid; 	
	}

	/**
	 * returns true/false if iOS / Safari is in portrait mode
	 */
	public static boolean isiOSPortrait()
	{
		return isPortrait;
	}
	
	/**
	 * returns true/false if iOS/Android / Safari is in portrait mode
	 */
	public static boolean isMobileHighResolution()
	{
		return isHighRes;
	}
		
	/**
	 * returns true/false if iOS / Safari is in landscape mode
	 */
	public static boolean isMobileLandscape()
	{
		return isPortrait;
	}

	/**
	 * sets iOS / Safari to landscape mode
	 */
	public static void setMobileToLandscape()
	{
		isPortrait = false;
	}
	/**
	 * sets iOS / Safari to portrait mode
	 */
	public static void setMobileToPortrait()
	{
		isPortrait = true;
	}
	
	/**
	 * returns true/false if iOS / Safari is in landscape mode
	 */
	public static boolean isiOSLandscape()
	{
		return isPortrait;
	}

	/**
	 * sets iOS / Safari to landscape mode
	 */
	public static void setiOSToLandscape()
	{
		isPortrait = false;
	}
	/**
	 * sets iOS / Safari to portrait mode
	 */
	public static void setiOSToPortrait()
	{
		isPortrait = true;
	}

	/**
	 * sets iOS / Safari to landscape mode
	 */
	public static void setMobileToHighResolution()
	{
		isHighRes = true;
	}
	
	/**
	 * sets iOS / Safari to portrait mode
	 */
	public static void setMobileToLowResolution()
	{
		isHighRes = false;
	}

	/**
	 * sets Safari to iPad emulation mode
	 */
	public static void setSafariToiPadMode()
	{
		isSafariAsiPad = true;
		isSafariAsiPhone = false;
		isSafariAsAndroid = false;
	}

	/**
	 * sets Safari to iPhone emulation mode
	 */
	public static void setSafariToiPhoneMode()
	{
		isSafariAsiPad = false;
		isSafariAsiPhone = true;
		isSafariAsAndroid = false;
	}
	
	/**
	 * sets Safari to Android emulation mode
	 */
	public static void setSafariToAndroidMode()
	{
		isSafariAsiPad = false;
		isSafariAsiPhone = false;
		isSafariAsAndroid = true;
	}

	/**
	 * sets safari to default mode - switches off iPhone / iPad emulation
	 */
	public static void setSafariToDefaultMode()
	{
		isSafariAsiPad = false;
		isSafariAsiPhone = false;
		isSafariAsAndroid = false;
	}	
}
