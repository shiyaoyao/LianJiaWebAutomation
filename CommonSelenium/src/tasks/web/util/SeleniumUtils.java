package tasks.web.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import appobjects.web.Browser;
import appobjects.web.editors.ClipboardText;

import com.lianjia.BaseAssert;
import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.SeleniumCore;
import com.lianjia.automation.core.web.WebBrowser;

public class SeleniumUtils {
	private static final Logger log = Logger.getLogger(SeleniumUtils.class);
	private static Robot robot;

	private static void initRobot() throws IOException, AWTException {
		robot = new Robot();
		WindowManagement.setBrowserFocus();
		robot.setAutoDelay(100);
	}

	public static String jsEval(String js) {
		String sEval = "";
		JavascriptExecutor jsEval = (JavascriptExecutor) SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
		try {
			sEval = jsEval.executeScript("return " + js).toString();
		} catch (Exception e) {
			try {
				Object obj = jsEval.executeScript(js);
				if (obj != null) {
					sEval = obj.toString();
				}
			} catch (Exception e1) {
				log.debug("jsEval exception for:<br>" + js);
			}
		}
		return sEval;
	}

	public static String formatIdForSelenium(String sId) {
		String sFinal = sId;
		sFinal = sId.replace("\\$", "$");
		return sFinal;
	}
	
	public static String getBrowserName() {
		String name = SeleniumCore.getWebDriverBrowser().getDisplayName();
		return name;
	}

	public static String getBrowserVersion() {
		String sBrowser = SeleniumUtils.getBrowserName();
		String sAgent = SeleniumUtils.jsEval("navigator.userAgent");
		if(sAgent.isEmpty())
			return "unknown";
		String[] s1 = sBrowser.split("\\s");

		String shortBrsrNm = s1[s1.length > 0 ? s1.length - 1 : 0] + "/";
		shortBrsrNm = shortBrsrNm.replace("Explorer/", "MSIE ");

		if(shortBrsrNm.contains("Safari")) {
			if(sAgent.indexOf("Version/") != -1) {
				shortBrsrNm = "Version/";
			}
		}

		int istart = sAgent.indexOf(shortBrsrNm);
		String sVersion = sAgent.substring(istart + shortBrsrNm.length()).split("\\s")[0];
		sVersion = sVersion.replace(";", "");
		return sVersion;
	}
	
	public static String getInternetExplorerEmulation() {
		String sEmulation="";
		String js = "window.getElementsByTagName('meta')";
		int i = -1;
		try {
			i = Integer.valueOf(SeleniumUtils.jsEval(js+".length"));
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(i < 1) 
			return "";
		String attribContent="";
		for(int x = 0; x<i; x++){
			attribContent = SeleniumUtils.jsEval(js+"["+x+"].getAttribute('content')");
			if(attribContent.contains("IE=")){
				if(attribContent.split("=").length>1)
					sEmulation=attribContent.split("=")[1].trim();
				break;
			}
		}
		log.debug("IE Emulation: ("+(sEmulation.isEmpty() ? "none" : sEmulation)+")");
		return sEmulation;
		
	}

	public static boolean isInternetExplorer() {
		return getBrowserName().startsWith("Internet");
	}

	public static boolean isFirefox() {
		return getBrowserName().equals("Firefox");
	}

	public static void shiftTabEnter(int tabs) {
		try {
			initRobot();
			robot.keyPress(KeyEvent.VK_SHIFT);
			for(int x = tabs; x>0; x--){
				robot.keyPress(KeyEvent.VK_TAB);
				robot.keyRelease(KeyEvent.VK_TAB);
				Platform.sleep(.25);
			}
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Platform.sleep(3);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pressEnter() {
		try {
			if(Browser.isInternetExplorer()){
				robot = new Robot();
				robot.setAutoDelay(100);
			} else {
				initRobot();
			}
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pressESC() {
		try {
			initRobot();
			robot.keyPress(KeyEvent.VK_ESCAPE);
			robot.keyRelease(KeyEvent.VK_ESCAPE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pressDownArrow() {
		try {
			initRobot();
			robot.keyPress(KeyEvent.VK_DOWN);
			robot.keyRelease(KeyEvent.VK_DOWN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void pressUpArrow() {
		try {
			initRobot();
			robot.keyPress(KeyEvent.VK_UP);
			robot.keyRelease(KeyEvent.VK_UP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void pressDelete() {
		try {
			initRobot();
			int key = Platform.isMac() ? KeyEvent.VK_BACK_SPACE : KeyEvent.VK_DELETE;
			robot.keyPress(key);
			robot.keyRelease(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void pressTab() {
		try {
			initRobot();
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
		} catch (Exception e) {
		}
	}

	public static void pressCtrlTab() {
		try {
			initRobot();
			int key = KeyEvent.VK_TAB;
			int control = KeyEvent.VK_CONTROL;
			robot.keyPress(control);
			robot.keyPress(key);
			robot.keyRelease(key);
			robot.keyRelease(control);
		} catch (Exception e) {
			//
		}
	}
	
	public static void pressAltTab() {
		try {
			initRobot();
			int key = KeyEvent.VK_TAB;
			int alt = Platform.isMac() ? KeyEvent.VK_META : KeyEvent.VK_ALT;
			robot.keyPress(alt);
			robot.keyPress(key);
			robot.keyRelease(key);
			robot.keyRelease(alt);
		} catch (Exception e) {
			//
		}
	}
	
	/**
	 * Experimental method for switching to the next browser tab
	 * in Firefox. Will cycle through when at the last tab.
	 * @return
	 */
	public static String nextBrowserTab() {
		if(!WebBrowser.isFirefox()) {
			log.warn("METHOD ONLY SUPPORTED FOR FIREFOX");
			return "";
		}
		Platform.sleep(1);
		pressCtrlTab();
		Platform.sleep(1);
		WindowManagement.switchToAppWindow();

		return WindowManagement.getTitle();
	}
	
	/**
	 * Experimental method for switching to a specific browser tab
	 * in Firefox. Will cycle through until a matching title is found.
	 * @return
	 */
	public static boolean switchBrowserTab(String sTitle) {
		if(!WebBrowser.isFirefox()) {
			log.warn("METHOD ONLY SUPPORTED FOR FIREFOX");
			return false;
		}
		try {
			WindowManagement.setBrowserFocus();
		} catch (IOException e) {}
		int count = 5;
		boolean success = WindowManagement.getTitle().contains(sTitle);
		while(!success && count-- > 0){
			success = nextBrowserTab().contains(sTitle);
		}
		return success;
	}
	
	public static void pressCtrlT() {
		try {
			initRobot();
			WindowManagement.setBrowserFocus();
			int keyEvent = Platform.isMac() ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
			robot.keyPress(keyEvent);
			robot.keyPress(KeyEvent.VK_T);
			robot.keyRelease(KeyEvent.VK_T);
			robot.keyRelease(keyEvent);
		} catch (Exception e) {
			//
		}
	}
	
	public static ArrayList<String> getAllBrowserTabs() {
		ArrayList<String> tabs = new ArrayList<String>();
		String url = WebBrowser.getCurrentBrowserURL();
		tabs.add(url);
		nextBrowserTab();
		url = WebBrowser.getCurrentBrowserURL();
		while(!tabs.contains(url)){
			tabs.add(url);
			nextBrowserTab();
			url = WebBrowser.getCurrentBrowserURL();
		}
		return tabs;
	}
	
	public static boolean switchBrowserTabURL(String sURL) {
		if(!WebBrowser.isFirefox()) {
			log.warn("METHOD ONLY SUPPORTED FOR FIREFOX");
			return false;
		}
		try {
			WindowManagement.setBrowserFocus();
		} catch (IOException e) {}
		int count = 5;
		boolean success = WebBrowser.getCurrentBrowserURL().contains(sURL);
		while(!success && count-- > 0){
			nextBrowserTab();
			success = WebBrowser.getCurrentBrowserURL().contains(sURL);
		}
		return success;
	}
	
	public static void pressCtrlEnd() {
		try {
			initRobot();
			int key = Platform.isMac() ? KeyEvent.VK_DOWN : KeyEvent.VK_END;
			int control = Platform.isMac() ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
			robot.keyPress(control);
			robot.keyPress(key);
			robot.keyRelease(key);
			robot.keyRelease(control);
		} catch (Exception e) {
			//
		}
	}

	public static void pressCtrlRight() {
		try {
			initRobot();
			int key = KeyEvent.VK_RIGHT;
			int control = Platform.isMac() ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
			robot.keyPress(control);
			robot.keyPress(key);
			robot.keyRelease(key);
			robot.keyRelease(control);
		} catch (Exception e) {
			//
		}
	}
	
	public static void pressCtrlLeft() {
		try {
			initRobot();
			int key = KeyEvent.VK_LEFT;
			int control = Platform.isMac() ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
			robot.keyPress(control);
			robot.keyPress(key);
			robot.keyRelease(key);
			robot.keyRelease(control);
		} catch (Exception e) {
			//
		}
	}
	
	public static void pressCtrlA() {
		try {
				initRobot();
				int keyEvent = Platform.isMac() ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
				robot.keyPress(keyEvent);
				robot.keyPress(KeyEvent.VK_A);
				robot.keyRelease(KeyEvent.VK_A);
				robot.keyRelease(keyEvent);
			} catch (Exception e) {}
	}
	
	public static void pressCtrlR() {
		try {
			initRobot();
			int keyEvent = Platform.isMac() ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
			robot.keyPress(keyEvent);
			robot.keyPress(KeyEvent.VK_R);
			robot.keyRelease(KeyEvent.VK_R);
			robot.keyRelease(keyEvent);
		} catch (Exception e) {
			//
		}
	}
	
	public static void selectAll() {
		try {
			initRobot();
			int keyEvent = Platform.isMac() ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
			robot.keyPress(keyEvent);
			robot.keyPress(KeyEvent.VK_A);
			robot.keyRelease(KeyEvent.VK_A);
			robot.keyRelease(keyEvent);
		} catch (Exception e) {
			//
		}
	}
	
	public static void pressCtrlC() {
		try {
			initRobot();
			int keyEvent = Platform.isMac() ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
			robot.keyPress(keyEvent);
			robot.keyPress(KeyEvent.VK_C);
			robot.keyRelease(KeyEvent.VK_C);
			robot.keyRelease(keyEvent);
		} catch (Exception e) {
			//
		}
	}
	
	public static void pressCtrlV() {
		try {
			initRobot();
			int keyEvent = Platform.isMac() ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
			robot.keyPress(keyEvent);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(keyEvent);
		} catch (Exception e) {
			//
		}
	}
	
	public static void pasteTextToCurrentFocus(String sText) {
		ClipboardText clip = new ClipboardText();
		clip.copy(sText);
		
		clip.paste();
	}
	
	public static void pressAltN() {
		try {
			initRobot();
			int keyEvent = KeyEvent.VK_ALT;
			robot.keyPress(keyEvent);
			robot.keyPress(KeyEvent.VK_N);
			robot.keyRelease(KeyEvent.VK_N);
			robot.keyRelease(keyEvent);
		} catch (Exception e) {
			//
		}
	}
	
	public static void pressKeyEvent(int keyEvent) {
		try {
			initRobot();
			robot.keyPress(keyEvent);
			robot.keyRelease(keyEvent);
		}  catch (Exception e) {
			//
		}
	}

	// ////////////////////////////////////////////////
	// PULLING ATTACHMENT FILES OUT OF THE PROJECT JAR
	// ////////////////////////////////////////////////

	/**
	 * Extracts a resource from a jar to a reserved location in the file system.
	 * If the resource is already an OS file object (i.e. not in a jar file), or
	 * if the resource has already been extracted from a jar, that object is
	 * returned without any further file I/O.
	 * 
	 * <p>
	 * Use this method if the specific resource file name is required or if the
	 * resource will be reused through a given automation session.
	 * 
	 * <p>
	 * When necessary, the resource will be copied to:<br />
	 * &nbsp;&nbsp;&nbsp;&nbsp;
	 * <code>{user.home}/Selenium_Execution/{sResourcePath}</code>
	 * </p>
	 * <p>
	 * Example: sResourcePath = data/attachment/image/Foo.png<br />
	 * &nbsp;&nbsp;&nbsp;&nbsp;
	 * <code>C:/Users/epm/Selenium_Execution/data/attachment/image/Foo.png</code>
	 * </p>
	 * <p>
	 * If the resource object is found to already exist in the extracted
	 * location, this method assumes the resource has been extracted earlier and
	 * will NOT extract it again.
	 * </p>
	 * <p>
	 * If the resources is not going to be reused, and if the extracted file
	 * name is not part of testing, see {@link #copyResourceToTempFile(String)}
	 * </p>
	 * 
	 * @param sResourcePath
	 *            full path to the resource. E.g. data/attachment/image/Foo.png
	 * @return OS file object of the copied resource
	 * @throws AWTException
	 */
	public static File getResourceAsFile(String sResourcePath) throws IOException {
		SeleniumUtils utils = new SeleniumUtils();
		// Set up Selenium_Execution folder
		String seleniumExecution = System.getProperty("user.home") + System.getProperty("file.separator")
				+ "Selenium_Execution";
		File destDir = new File(seleniumExecution);
		int i = sResourcePath.lastIndexOf('/');
		String dataPath = sResourcePath.substring(0, i == -1 ? sResourcePath.length() : i);
		String filename = sResourcePath.replace(dataPath + "/", "");

		dataPath = dataPath.replaceAll("[/]$", "");
		URL dir = utils.getClass().getClassLoader().getResource(sResourcePath);
		if(dir != null && dir.getProtocol().equals("file")) {
			try {
				log.debug("Resource is found directly as an OS file object");
				return new File(dir.toURI());
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}

		// If the resource already exists in the file system, return that file
		destDir = new File(destDir, dataPath);
		destDir.mkdirs();
		File destFile = new File(destDir, filename);
		log.debug("File resource: " + destFile.getPath());
		if(destFile.exists()) {
			log.debug("File: " + destFile.getName() + " is already pulled from project resource path");
			return destFile;
		}

		InputStream in = utils.getClass().getClassLoader().getResourceAsStream(sResourcePath);
		// Copy the resource
		// ************
		byte[] bytebuf = new byte[1024];
		int length = 0;

		try {
			FileOutputStream os = new FileOutputStream(destFile);
			while((length = in.read(bytebuf)) > 0) {
				os.write(bytebuf, 0, length);
			}
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		// ************

		in.close();

		return destFile.exists() ? destFile : null;
	}

	/**
	 * Extracts a resource object from a jar to a temporary file in the OSs temp
	 * directory. This method always make a copy of the resource object, even if
	 * the resource is directly accessible to the OS file system.
	 * <p>
	 * In most cases {@link #getResourceAsFile(String)} is a better choice.
	 * However, this method avoids creating objects within the file structure of
	 * the user space and therefore leaves a lighter footprint once testing has
	 * finished.
	 * <p>
	 * If the extracted file name is expected to match the resource name, or if
	 * the resource will be reused throughout the automation session, use
	 * {@link #getResourceAsFile(String)}
	 * </p>
	 * <p>
	 * The copied file will be deleted as part of the operating system's garbage
	 * collection process
	 * </p>
	 * 
	 * @param sResourcePath
	 *            full path to the resource object. E.g.
	 *            data/attachment/image/Foo.png
	 * @return OS file object to the temp file
	 * @throws IOException
	 * @throws AWTException
	 */
	public static File copyResourceToTempFile(String sResourcePath) throws IOException {
		SeleniumUtils utils = new SeleniumUtils();
		// Set up Selenium_Execution folder
		String tempDir = System.getProperty("java.io.tmpdir");
		File destDir = new File(tempDir);
		int i = sResourcePath.lastIndexOf('/');
		String dataPath = sResourcePath.substring(0, i == -1 ? sResourcePath.length() : i);
		String filename = sResourcePath.replace(dataPath + "/", "");
		i = filename.lastIndexOf('.');
		String newfile = filename.substring(0, (i == -1 ? filename.length() : i));
		String ext = filename.replace(newfile, "");
		filename = newfile + "_" + String.valueOf(System.currentTimeMillis()) + ext;

		dataPath = dataPath.replaceAll("[/]$", "");

		// If the resource already exists in the file system, return that file
		File destFile = new File(destDir, filename);
		log.debug("Temp file: " + destFile.getPath());
		InputStream in = utils.getClass().getClassLoader().getResourceAsStream(sResourcePath);
		// Copy the resource
		// ************
		byte[] bytebuf = new byte[1024];
		int length = 0;

		try {
			FileOutputStream os = new FileOutputStream(destFile);
			while((length = in.read(bytebuf)) > 0) {
				os.write(bytebuf, 0, length);
			}
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		// ************

		in.close();

		return destFile.exists() ? destFile : null;
	}

	public static int getCssCount(String selector) {
		int ret = SeleniumCore.getWebDriverBrowser().getWebDriverAPI().findElements(By.cssSelector(selector)).size();
		return ret;
	}

	public static int getXpathCount(String selector) {
		int ret = SeleniumCore.getWebDriverBrowser().getWebDriverAPI().findElements(By.xpath(selector)).size();
		return ret;
	}

	public static void callInfo() {
		String cp = CallPoint.callPoint();
		log.info("Call Point: "+cp);
		BaseAssert.logScreenCapture(cp);
	}
}
