package tasks.web.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import appobjects.web.WebFrame;

import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.SeleniumCore;

/**
 * <p>Use this class to manage all open windows that Selenium can switch to and control. These methods
 * should be the sole mechanism for switching Selenium focus to different windows for testing.</p>
 * </ul></p>
 */
public class WindowManagement {
	private static final Logger log = Logger.getLogger(WindowManagement.class);
	
	public static List<String> winHandles = new ArrayList<String>();
	public static String appWindow = "";
	private static String currentWindow = appWindow;
	
	/**
	 * Gets the window handle for the default main browser window
	 * 
	 * @return
	 */
	public static String getAppWindowHandle() {
		if(appWindow.isEmpty()){
			appWindow = SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getWindowHandles().iterator().next();
		}
		if(!winHandles.contains(appWindow)){
			winHandles.add(appWindow);
		}
		if(!winHandles.contains(currentWindow)){
			currentWindow = appWindow;
		}
		
		return appWindow;
	}
	
	public static String getCurrentWindowHandle() {
		return currentWindow;
	}
	
	public static void refreshAllWindows() {
		appWindow="";
		currentWindow="";
		getAllWindows();
		getAppWindowHandle();
	}
	/**
	 * Changes the Main Application window to a new window handle. Use this if
	 * the original LianJiaWeb application window is no longer the default main window.
	 * 
	 * @param sHandle
	 */
	public static void setAppWindowHandle(String sHandle) {
		appWindow = sHandle;
	}
	
	/**
	 * Sets Selenium focus to the main LianJiaWeb applicaton window
	 * 
	 */
	public static void switchToAppWindow() {
		if(appWindow.isEmpty())
			getAppWindowHandle();
		log.info("Switching to mainwin handle: "+appWindow);
		SeleniumCore.getWebDriverBrowser().getWebDriverAPI().switchTo().window(appWindow);
		log.debug("Window Title: "+SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getTitle());
		currentWindow = appWindow;
		WebFrame.selectMainFrame();
	}
	
	/**
	 * Switch Selenium focus to the window with the specified handle
	 * 
	 * @param sHandle Selenium window handle
	 * @return true if the window wis selected
	 */
	public static boolean switchToWindow(String sHandle) {
		getAllWindows();
		if(sHandle.trim().isEmpty() || !winHandles.contains(sHandle)){
			log.info("Window "+sHandle+" isn't found");
			return false;
		}
		log.info("Switching to Window: '"+sHandle+"'");
		try {
			SeleniumCore.getWebDriverBrowser().getWebDriverAPI().switchTo().window(sHandle);
			if(!winHandles.contains(sHandle)){
				winHandles.add(sHandle);
			}
			currentWindow = sHandle;
			log.debug("Window title: "+getTitle());
			
			// If frame s_MainFrame exists within 2 seconds, switch to it
			if(WebFrame.MainFrame().waitForMatchingElement(2000)){
				WebFrame.MainFrame().selectFrame();
			} else {
				log.debug("Window ("+sHandle+") doesn't contain s_MainFrame.");
			}
			return true;
		} catch (Exception e) {
			if(winHandles.contains(sHandle)){
				winHandles.remove(sHandle);
			}
			log.info("No window with handle '"+sHandle+"' is found.");
			return false;	
		}
	}
	
	/**
	 * Searches for a newly opened window and returns it's handle
	 * 
	 * @return the Selenium window handle for a newly opened window
	 */
	public static String getNewWindow() {
		String sNewWindowHandle = "";
		String sHandle = "";
		Iterator<String> i = SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getWindowHandles().iterator();
		
		while(i.hasNext()){
			sHandle = i.next();
			if(!winHandles.contains(sHandle)){
				winHandles.add(sHandle);
				sNewWindowHandle = sHandle;
			}
		}
		
		if(sNewWindowHandle.isEmpty()){
			log.debug("Didn't find a new window");
		} else {
			log.debug("New window handle: '"+sNewWindowHandle+"'");
		}
		return sNewWindowHandle;
	}
	
	/**
	 * Switches Selenium focus to a newly opened window
	 * 
	 * @return the Selenium window handle of the newly opened window. Empty string if no new window found.
	 */
	public static String switchToNewWindow() {
		log.debug("Current window: "+getCurrentWindowHandle());
		String sNewWindowHandle = getNewWindow();
		if(!sNewWindowHandle.isEmpty()){
			switchToWindow(sNewWindowHandle);
			currentWindow = sNewWindowHandle;
		}
		
		return sNewWindowHandle;
	}
	
	public static void switchToNewWindowUpdate(){
		WebDriver driver = SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
	    //Store the current window handle  
	    String winHandleBefore = driver.getWindowHandle();  
	              
	    //Switch to new window opened  
	    for(String winHandle : driver.getWindowHandles()){
	    	System.out.println("+++" + winHandle);
	    	driver.switchTo().window(winHandle);
	    }  
	    
	    // Close the original window  
	    driver.switchTo().window(winHandleBefore);  
	    driver.close();  
	              
	    //Switch to new window opened  
	    for(String winHandle : driver.getWindowHandles()){  
	        System.out.println("+++" + winHandle);  
	        driver.switchTo().window(winHandle);  
	    }  
	}
	
	/**
	 * Use this method to switch to a non-DIV browser dialog or to any
	 * Window that doesn't load the main LianJiaWeb application frame: s_MainFrame</br></br>
	 * Same as <code>switchToNewWindow(String sHandle)</code>, but WebDriver will not attempt <code>switchTo().frame('s_MainFrame')</code>
	 * @param sHandle
	 * @return
	 */
	public static boolean switchToDialog(String sHandle) {
		getAllWindows();
		if(sHandle.trim().isEmpty() || !winHandles.contains(sHandle)){
			log.info("Window ("+sHandle+") isn't found");
			return false;
		}
		log.info("Switching to Window: '"+sHandle+"'");
		try {
			SeleniumCore.getWebDriverBrowser().getWebDriverAPI().switchTo().window(sHandle);
			if(!winHandles.contains(sHandle)) {
				winHandles.add(sHandle);
			}
			currentWindow = sHandle;
			log.debug("Dialog title: " + getTitle());
			return true;
		} catch (Exception e) {
			if(winHandles.contains(sHandle)) {
				winHandles.remove(sHandle);
			}
			log.info("No window with handle '" + sHandle + "' is found.");
			return false;
		}
	}
	
	/**
	 * Use this method to switch to a non-DIV browser dialog or to any
	 * Window that doesn't load the main LianJiaWeb application frame: s_MainFrame</br></br>
	 * Same as <code>switchToNewWindow()</code>, but WebDriver will not attempt <code>switchTo().frame('s_MainFrame')</code>
	 * @return
	 */
	public static String switchToNewDialog() {
		log.debug("Current window: "+getCurrentWindowHandle());
		String sNewWindowHandle = getNewWindow();
		if(!sNewWindowHandle.isEmpty()){
			switchToDialog(sNewWindowHandle);
			currentWindow = sNewWindowHandle;
		}
		
		return sNewWindowHandle;
	}
	
	public static String getNewDialog() {
		return getNewWindow();
	}
	
	/**
	 * Switch to the first window that isn't this window.
	 * 
	 * @return
	 */
	public static String switchToOtherWindow() {
		String sHandle = "";
		String sCurrent = getCurrentWindowHandle();
		for(String s : winHandles){
			if(!s.equals(sCurrent)){
				switchToWindow(s);
				sHandle = getCurrentWindowHandle();
				break;
			}
		}
		return sHandle;
	}
	
	/**
	 * Close the first widow that isn't this window or the AppWindow
	 * 
	 * @return
	 */
	public static boolean closeOtherWindow() {
		boolean foundwindow = false;
		int i = winHandles.size();
		
		System.out.println("winHandles.size()=========" + winHandles.size());
		String sCurrent = getCurrentWindowHandle();
		
		System.out.println("sCurrent=========" + sCurrent);
		
		for(String s : winHandles){
			System.out.println("for s=========" + s);
			//if(!s.equals(sCurrent) && !s.equals(appWindow)){
			if(!s.equals(sCurrent)){
				System.out.println("for if");
				closeWin(s);
				foundwindow = true;
				break;
			}
		}
		return foundwindow ? winHandles.size() < i : true;
	}		
	
	/**
	 * Get the title of the currently selected window
	 * 
	 * @return window title
	 */
	public static String getTitle() {
		String sTitle = SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getTitle();
		return sTitle;
	}
	
	/**
	 * Get the window.location.href attribute of the currently selected window
	 * 
	 * @return href value
	 */
	public static String getLocation() {
		String sLoc = SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getCurrentUrl();
		log.debug("Window location: '"+sLoc+"'");
		return sLoc;
	}
	
	/**
	 * <p>Close the window the the specified handle</p>
	 * <p>Switches focus to the window with the specified handle and closes it. Then sets
	 * the Selenium focus to the main application window.</p>
	 * 
	 * @param sHandle window handle
	 * @return true if the window is closed or if it no longer exists
	 */
	public static boolean closeWin(String sHandle){
		log.info("Closing window: '"+sHandle+"'");
		if(sHandle.equals(currentWindow)){
			System.out.println("closeWin111111111");
			return closeWin();
		}
		if(!switchToWindow(sHandle)){
			System.out.println("closeWin2222222");
			log.info("Window '"+sHandle+"' appears to be closed already");
			if(winHandles.contains(sHandle))
				winHandles.remove(sHandle);
			switchToAppWindow();
			return true;
		}
		winHandles.remove(sHandle);
		
		SeleniumCore.getWebDriverBrowser().getWebDriverAPI().close();
		switchToAppWindow();
		return !doesWindowExist(sHandle);
	}
	
	/**
	 * Closes the currently active window. Then sets focus to the main application window.
	 * 
	 * @return true if the window no longer exists
	 */
	public static boolean closeWin() {
		log.info("Closing the current window");
		try {
			if(winHandles.contains(currentWindow))
				winHandles.remove(currentWindow);
			
			SeleniumCore.getWebDriverBrowser().getWebDriverAPI().close();
			log.debug("Current window was closed");
			switchToAppWindow();
			return true;
		} catch (Exception e) {
			log.debug("Window appears to be closed already");
			switchToAppWindow();
			return true;
		}
	}
	
	/**
	 * Checks to see if the window with the specifed handle exists.
	 * 
	 * @param sHandle Selenium window handle
	 * @return true if the window exists, else false
	 */
	public static boolean doesWindowExist(String sHandle) {
		getAllWindows();
		return winHandles.contains(sHandle);
	}
	
	/**
	 * Refreshes the list of currently open windows.
	 * 
	 */
	public static int getAllWindows() {
		winHandles.clear();
		appWindow = "";
		String sHandle = "";
		Iterator<String> i = SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getWindowHandles().iterator();
		while (i.hasNext()) {
			sHandle = i.next().toString();
			winHandles.add(sHandle);
		}
		appWindow = getAppWindowHandle();
		return winHandles.size();
	}
	
	/**
	 * Logs all the window handles for open windows.
	 * 
	 */
	public static void listAllWindowObjects() {
		int count = 1;
		Iterator<String> iter = winHandles.iterator();
		while(iter.hasNext()){
			log.info("Window ("+(count++)+") handle: '"+iter.next()+"'");
		}
	}
	
	/**
	 * Logs the titles of all currently open windows.
	 * 
	 */
	public static void allTitles(){
		int count = 1;
		String[] wh;
		String current = currentWindow;
		getAllWindows();
		Set<String> winset = SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getWindowHandles();
		wh = winset.toArray(new String[winset.size()]);
		for(String s : wh){
			switchToWindow(s);
			log.info("("+(count++)+") title: '"+ SeleniumCore.getWebDriverBrowser().getWebDriverAPI().getTitle()+"'");
		}
		switchToWindow(current);
	}
	
	public static void setBrowserFocus()  throws IOException {
		String browserName = SeleniumUtils.getBrowserName().replace("Apple ", "");
		log.debug("Set focus to "+browserName);
		String workingdir = System.getProperty("user.dir")+Platform.getFileSeparator()+"lib"+Platform.getFileSeparator();
		ProcessBuilder process=null;
		
		if(Platform.isMac()) {
			browserName = browserName.replace("Mozilla ", "");
			process = new ProcessBuilder(workingdir+"BrowserFocus.mac.sh", browserName.toString());
		}
		
		if (Platform.isWindows()) {
			process = new ProcessBuilder(workingdir+"BrowserFocus.exe", browserName.toString());
		}
		
		if (Platform.isLinux()) {
			process = new ProcessBuilder(workingdir+"BrowserFocus.linux.sh", browserName.toString());
		}
		
		try {
			Process p = process.start();
			int exitCode = p.waitFor();
			System.out.println("process : "+exitCode);
		} catch (InterruptedException e) {
			//
		}
		
		Platform.sleep(1);
		return;
		
	}
	
	public static void setBrowserFocus(String sBrowser)  throws IOException {
		String browserName = sBrowser;
		log.debug("Set focus to "+browserName);
		String workingdir = System.getProperty("user.dir")+Platform.getFileSeparator()+"lib"+Platform.getFileSeparator();
		ProcessBuilder process=null;
		
		if(Platform.isMac()) {
			browserName = browserName.replace("Mozilla ", "");
			process = new ProcessBuilder(workingdir+"BrowserFocus.mac.sh", browserName.toString());
		}
		
		if (Platform.isWindows()) {
			process = new ProcessBuilder(workingdir+"BrowserFocus.exe", browserName.toString());
		}
		
		if (Platform.isLinux()) {
			process = new ProcessBuilder(workingdir+"BrowserFocus.linux.sh", browserName.toString());
		}
		
		try {
			Process p = process.start();
			int exitCode = p.waitFor();
			System.out.println("process : "+exitCode);
		} catch (InterruptedException e) {
			//
		}
		
		Platform.sleep(1);
		return;		
	}
	
	/**
	 * Hides or displays the MacOS Dock to give more screen space for the
	 * browser
	 * 
	 * @param bHideDock
	 *            if true, hide the Dock; else show it
	 * @throws IOException
	 */
	protected static void hideMacOsDock(boolean bHideDock) throws IOException {

		String workingdir = System.getProperty("user.dir") + Platform.getFileSeparator() + "lib"
				+ Platform.getFileSeparator();
		ProcessBuilder process = null;

		if(!Platform.isMac()) {
			return;
		}
		process = new ProcessBuilder(workingdir + "HideMacOSDock.mac.sh", String.valueOf(bHideDock));

		try {
			Process p = process.start();
			int exitCode = p.waitFor();
			System.out.println("process : " + exitCode);
		} catch (InterruptedException e) {
			//
		}
	}

	/**
	 * Enables the Auto Hide preference for the Mac OS Dock. </br>
	 * This gives more screen space to maximize a browser
	 */
	public static void hideMacOsDock() {
		try {
			hideMacOsDock(true);
		} catch (IOException e) {
			//
		}
	}
	
	/**
	 * Disables the Auto Hide preference for the Mac OS Dock. </br>
	 * This is the usual default setting.
	 */
	public static void showMacOsDock() {
		try {
			hideMacOsDock(false);
		} catch (IOException e) {
			//
		}
	}
}
