package appobjects.web;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import tasks.web.util.BrowserReadyThread;
import tasks.web.util.DateUtils;
import tasks.web.util.IECertificateThread;
import tasks.web.util.SeleniumUtils;
import tasks.web.util.StringUtils;
import tasks.web.util.WindowManagement;

import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.SeleniumCore;
import com.lianjia.automation.core.web.WebBrowser;

public class Browser extends WebBrowser {
	private static final Logger log = Logger.getLogger(Browser.class);
	
	private static String BROWSER_NAME;
	private static String BROWSER_VERSION;
	public static String CERT_ERROR = "invalidcert";
	private static JavascriptExecutor JAVASCRIPT = null;
	private static HashMap<String, WebDriver> DRIVERS = new HashMap<String, WebDriver>();
	private static String _driverBrowser;
	
	public static final String _chrome = "chrome";
	public static final String _firefox = "firefox";
	public static final String _explorer = "explorer";
	public static final String _safari = "safari";
	
	public static String getTitle() {
		String sTitle = "";
		sTitle = getWebDriver(null).getTitle();
		return sTitle;
	}
	
	public static String getCurrentUrl() {
		String sUrl = getWebDriver(null).getCurrentUrl();
		log.debug("Current URL: "+sUrl);
		return sUrl;		
	}
	
	public static String getName(){
		if(BROWSER_NAME == null)
			BROWSER_NAME = SeleniumUtils.getBrowserName().trim();
		return BROWSER_NAME;
	}
	
	public static String getSimpleName() {
		String name = getName().toLowerCase();
		if(name.contains(" "))
			name = name.substring(name.lastIndexOf(' ')+1);
		if(_driverBrowser == null)
			_driverBrowser = name;
		return name;
	}
	
	public static String getDriverBrowser() {
		if(_driverBrowser == null)
			getSimpleName();
		return _driverBrowser;
	}
	
	public static String getVersion(){
		if(BROWSER_VERSION == null || BROWSER_VERSION.equalsIgnoreCase("unknown")){
			BROWSER_VERSION = SeleniumUtils.getBrowserVersion();
		}
		return BROWSER_VERSION;
	}
	
	public static double getVersionNumber() {
		return StringUtils.getNumberInStringAsNumber(Browser.getVersion(),false).doubleValue();
	}
	
	public static boolean isChrome37() {
		return isChrome() && getVersionNumber() > 36;
	}
	
	public static void back() {
		getWebDriver(null).navigate().back();
	}
	
	public static void refresh() {
		getWebDriver(null).navigate().refresh();
	}
	
	public static void setSize(int iWidth, int iHeight) {
		log.setLevel(Level.toLevel("DEBUG"));
		log.debug("Set Browser size: "+ iWidth + " : "+iHeight);
		getWebDriver(null).manage().window().setSize(new org.openqa.selenium.Dimension(iWidth, iHeight));
	}
	
	public static Dimension getSize() {
		return BROWSERSIZE.getSize();
	}
	
	public static int getWidth() {
		return BROWSERSIZE.getSize().width;
	}
	
	public static int getHeight() {
		return BROWSERSIZE.getSize().height;
	}
	
	public static void setPosition(int iX, int iY) {
		log.setLevel(Level.toLevel("DEBUG"));
		log.debug("Position Browser at: "+iX+" : "+iY);
		getWebDriver(null).manage().window().setPosition(new org.openqa.selenium.Point(iX, iY));
	}
	
	public static Point getPosition() {
		return BROWSERSIZE.getPosition();
	}
	
	public static void positionLeft(boolean bWide) {
		java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		double x = screen.getWidth();
		maximize();
		
		double divisor = bWide ? 0.75 : 0.5;
		int width = (int)(x * divisor);
		width = Math.max(800, width);
		int height = BROWSERSIZE.getSize().height;
		height = Math.max(height, 768);
		
		setPosition(0,0);
		setSize(width, height);
		setPosition(0,0);
	}
	
	public static void positionRight(boolean bWide) {
		java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		double x = screen.getWidth();
		maximize();
		
		if(screen.getWidth() <=1024 || screen.getHeight() <= 768){
			return;
		}
		double divisor = bWide ? 0.75 : 0.5;
		int width = (int)(x * divisor);
		width = Math.max(800, width);
		int height = BROWSERSIZE.getSize().height;
		height = Math.max(height, 768);

		setSize(width, height);
		setPosition(((int)screen.getWidth() - width),0);
	}
	
	public static void maximize(){
		log.setLevel(Level.toLevel("DEBUG"));
		java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		int iTaskbarOffset = Platform.isWindows() ? 32 : 0;
		int x = (int)screen.getWidth();
		int y = (int)screen.getHeight()-iTaskbarOffset;
		
		// If this isn't a Mac running Chrome use the WebDriver maximize()
		if(!(Platform.isMac() && isChrome())){
			getWebDriver(null).manage().window().maximize();
		}
		
		Dimension browserSize = BROWSERSIZE.getSize();

		if(browserSize.width >= x - 25 && browserSize.height >= y - 25){
			log.debug("Maximized with WebDriver.manage().window().maximize()");
			return;
		} else {
			log.debug("WebDriver maximize() didn't work. Will force browser to "+x+" by "+y);
			setPosition(0,0);
			setSize(x, y);
			return;
		}
	}
	
	public static void deleteCookies() {
		log.setLevel(Level.toLevel("DEBUG"));
		StringBuilder sb = new StringBuilder();
		Set<Cookie> crumbs = SeleniumCore.getWebDriverBrowser().getWebDriverAPI().manage().getCookies();
		ArrayList<Cookie> sessionCookies = new ArrayList<Cookie>(crumbs);
		
		if(sessionCookies.size()==0)
			log.debug("No cookies found for this session");
		
		int count = 1;
		for(Cookie cookie : sessionCookies){
			sb.append("("+(count++)+") <code>"+cookie.toString()+"</code><br>");
		}
		log.debug("Deleting Cookies:<br>"+sb.toString());
		
		Iterator<Cookie> i = crumbs.iterator();
		while(i.hasNext()){
			SeleniumCore.getWebDriverBrowser().getWebDriverAPI().manage().deleteCookie(i.next());
		}
	}
	
	protected static boolean waitForIELoaded() {
		return waitForIELoaded(90);
	}
	
	protected static boolean waitForIELoaded(double seconds){
		boolean success = false;
		double timeout = seconds > 120 ? 120 : seconds;
		double start = System.nanoTime()/1e9;
		double now = (System.nanoTime()/1e9) - start;
		String source = "";
		String temp = "";
		try {
			source = Browser.getWebDriver(null).getPageSource();
		} catch (Exception e) {
			log.debug("Exception getting IE page source");
			temp = "";
		}
		while(!success && now < timeout){
			try{
				temp =Browser.getWebDriver(null).getPageSource();
				success = source.equals(temp);
				Platform.sleep(.1);
				if(!success)
					source = Browser.getWebDriver(null).getPageSource();
				else
					break;
			} catch (Exception e) {
				log.debug("Exception checking IE page source with WebDriver.getPageSource()");
				temp = "";
			}
			now = (System.nanoTime()/1e9) - start;
		}
		log.debug("IE ready ("+success+") in "+String.format("%.1f",now)+" sec, max timeout: "+String.format("%.1f",timeout)+" sec");
		
		return success;
	}
	
	public static boolean waitForPageLoaded() {
		return waitForPageLoaded(60);
	}
	
	public static boolean waitForPageLoaded(double seconds) {
		boolean isIE = Browser.isInternetExplorer();
		boolean success = false;
		double timeout = seconds > 120 ? 120 : seconds; 
		double start = System.nanoTime()/1e9;
		String browserReady = "";
		log.debug("Wait "+seconds+" seconds for browser to reach ready state");
		
		double now = (System.nanoTime()/1e9) - start;
		while(!success && now < timeout){
			browserReady = getBrowserReadyState();
			success = browserReady.equalsIgnoreCase("complete");
			if(success)
				break;
			now = (System.nanoTime()/1e9) - start;
			
			if(isIE && (browserReady.equalsIgnoreCase("exception") || browserReady.equalsIgnoreCase("interactive")) && now > 2){
				log.debug("Switching to waitForIELoaded() after "+String.format("%.1f",now)+" sec");
				return waitForIELoaded(timeout - now);
			}
			Platform.sleep(.01);			
		}
		log.debug(Browser.getWebBrowserName()+" ready in "+String.format("%.1f",now)+" sec, max timeout: "+String.format("%.1f",timeout)+" sec");
		return success;
	}
	
	public static String getBrowserReadyState() {
		String browserReady;
		if(JAVASCRIPT == null)
			JAVASCRIPT = (JavascriptExecutor)getWebDriver(null);
		try {
			browserReady = JAVASCRIPT.executeScript("return document.readyState").toString();
		} catch (Exception e) {
			browserReady = "EXCEPTION";
		}
		BrowserReadyThread browserThread = new BrowserReadyThread();
		browserThread.start();
		double to = 2.00;
		double begin = System.nanoTime();
		double elapsed = DateUtils.elapsedSeconds(begin);
		while ((!browserThread.pass() || browserThread.isAlive()) && elapsed < to) {
			elapsed = DateUtils.elapsedSeconds(begin);
		}
		// Check for problems with the thread.
		if (!browserThread.pass() || browserThread.isAlive()) {
			browserThread.interrupt();
			System.out.println("Click thread is still alive");
			return "EXCEPTION";
		}
		browserReady = browserThread.browserStatus();
		return browserReady;
	}
	
	public static boolean handleIECertError() {
		if(!isInternetExplorer())
			return true;
		
		int count = 4;
		String url = Browser.getCurrentUrl().toLowerCase();
		
		while (url.contains(CERT_ERROR) && count-- > 0) {
			log.debug("("+(4-count)+") Handle Internet Explorer Certificate Error");
			try {
				_jsCertHandler();
				waitForPageLoaded();
			} catch (Exception e) {
				// Fall through for null object
			}
			url = Browser.getCurrentUrl().toLowerCase();
			if (url.contains(CERT_ERROR)) {
				waitForPageLoaded();
			}
		}
		
		return !url.contains(CERT_ERROR);
	}
	
	protected static boolean _jsCertHandler() {
		boolean certPage = false;
		IECertificateThread cd = new IECertificateThread();
		cd.start();
		Platform.sleep(0.5);
		double timeout = 20*1000;// 20 sec
		while((!cd.pass() || cd.isAlive()) && timeout >= 0){
			Platform.sleep(0.250);
			timeout = timeout - 250;
		}
		// Check for problems with the thread.
		boolean threadPass = cd.pass();
		boolean threadAlive = cd.isAlive();
		if(!threadPass || threadAlive){
			log.debug("Error handling cert in thread. Exist status: <code>"+cd.exitStatus()+"</code> Action status: <code>"+cd.actionStatus()+"</code>");
			Platform.sleep(10);
			cd.interrupt();
		}
		
		certPage = Browser.getCurrentUrl().toLowerCase().contains(CERT_ERROR);
		if(certPage)
			log.warn("INTERNET EXPLORER IVALID CERTIFICATE WARNING");
		
		return !certPage;
	}
	
	public static void closeAndQuit() {
		String bn = getSimpleName();
		if(DRIVERS.containsKey(bn)){
			WebDriver d = DRIVERS.get(bn);
			DRIVERS.remove(bn);
			d.quit();
			if(DRIVERS.size() >0){
				Set<String> ks = DRIVERS.keySet();
				for(String k : ks){
					switchWebDriver(k.toString());
				}
			}
		}
	}
	
	public static WebDriver getWebDriver(String Browser) {
		WebDriver _driver;
		if(Browser == null){
			if(!DRIVERS.containsKey(getSimpleName()))
				DRIVERS.put(getSimpleName(), SeleniumCore.getWebDriverBrowser().getWebDriverAPI());
			_driverBrowser = getSimpleName();
			return SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
		}
		log.setLevel(Level.toLevel("DEBUG"));
		String browserName = Browser.toLowerCase();
		String instance = StringUtils.getEndDigits(browserName);
		
		log.debug("path: "+System.getProperty("user.dir"));
		String driverName = "chromedriver.exe";
		if(Platform.isMac())
			driverName = "chromedriver_mac";
		if(Platform.isLinux())
			driverName = "chromedriver_linux32";
		String fs = System.getProperty("file.separator");
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+fs+"lib"+fs+driverName);
		System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"/lib/IEDriverServer.exe");
		
		if (browserName.contains(_chrome)) {
			WebBrowser.setBrowserToChrome();		
			BROWSER_NAME = BROWSER_VERSION =null;
			JAVASCRIPT = null;
			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable(LogType.PERFORMANCE, java.util.logging.Level.ALL);
			
			ChromeOptions myChromeOptions = new ChromeOptions();
			myChromeOptions.addArguments("start-maximized");
			myChromeOptions.addArguments("allow-outdated-plugins");
			myChromeOptions.addArguments("test-type");
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();	
			capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
			capabilities.setCapability(ChromeOptions.CAPABILITY, myChromeOptions);
			_driver = new ChromeDriver(capabilities);
			DRIVERS.put(_chrome+instance, _driver);
			_driverBrowser = _chrome+instance;
			switchWebDriver(_chrome);
			return _driver;
		} else if (browserName.contains(_firefox)) {
			WebBrowser.setBrowserToFirefox();
			BROWSER_NAME = BROWSER_VERSION =null;
			JAVASCRIPT = null;
			
			FirefoxProfile fp = new FirefoxProfile();
			fp.setEnableNativeEvents(true);
			fp.setPreference("browser.tabs.warnOnClose", false);								
			fp.setPreference("extensions.update.enabled", false);
			fp.setPreference("extensions.shownSelectionUI", false);
			fp.setPreference("extensions.showMismatchUI", false);
			fp.setPreference("browser.link.open_newwindow.restriction", 0);
			_driver = new FirefoxDriver(fp);
			DRIVERS.put(_firefox+instance, _driver);
			_driverBrowser = _firefox+instance;
			switchWebDriver(_firefox);
			return _driver;
		} else if (browserName.contains(_explorer)) {
			WebBrowser.setBrowserToIE();
			BROWSER_NAME = BROWSER_VERSION =null;
			JAVASCRIPT = null;
			
			DesiredCapabilities options = DesiredCapabilities.internetExplorer();
			options.setCapability("nativeEvents", false);
			_driver = new InternetExplorerDriver(options);
			DRIVERS.put(_explorer+instance,_driver);
			_driverBrowser = _explorer+instance;
			switchWebDriver(_explorer);
			return _driver;
		}
		
		return SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
	}
	
	public static WebDriver switchWebDriver(String sBrowserName){
		BROWSER_NAME = BROWSER_VERSION =null;
		JAVASCRIPT = null;
		String sName = sBrowserName.toLowerCase();
		if(sName.contains(" "))
			sName = sName.substring(sName.lastIndexOf(' ')+1);

		if(!DRIVERS.containsKey(sName))
			getWebDriver(sName);
		
		SeleniumCore.switchWebDriverBrowser(DRIVERS.get(sName));
		
		if(sName.contains(_explorer)){
			setBrowserToIE();
		} else if (sName.contains(_firefox)){
			if(Platform.isMac()){
				setWebBrowser(_firefox);
			} else 
				setBrowserToFirefox();
		} else if (sName.contains(_chrome)){
			setBrowserToChrome();
		} else if (sName.contains(_safari)){
			setBrowserToSafari();
		} else {
			log.warn("'"+sBrowserName+"' is not supported. Setting to Google Chrome");
			setBrowserToChrome();
		}
		try {
			WindowManagement.setBrowserFocus(sBrowserName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		_driverBrowser=sName;
		return DRIVERS.get(sName);
	}
	
	private static class BROWSERSIZE {
		private static org.openqa.selenium.Dimension dimSize;
		private static org.openqa.selenium.Point pntPos;
		
		public static java.awt.Dimension getSize(){
			dimSize = getWebDriver(null).manage().window().getSize();
			return new java.awt.Dimension(dimSize.width, dimSize.height);
		}
		
		public static java.awt.Point getPosition(){
			pntPos = getWebDriver(null).manage().window().getPosition();
			return new java.awt.Point(pntPos.x, pntPos.y);
		}		
	}
}
