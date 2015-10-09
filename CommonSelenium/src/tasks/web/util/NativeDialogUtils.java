package tasks.web.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.apache.log4j.Logger;

import appobjects.web.editors.ClipboardText;

import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.WebBrowser;

public class NativeDialogUtils {	
	 private static final Logger log = Logger.getLogger(NativeDialogUtils.class);

	 public static boolean selectFileFromNativeBrowseDialog(String sFullPath) throws IOException, AWTException {
		sFullPath = sFullPath.trim();
		ClipboardText clip = new ClipboardText();
		clip.copy(sFullPath);
		String browserName = WebBrowser.getWebBrowserName();
		Robot robot = new Robot();
		robot.setAutoDelay(100);
		boolean success = true;
		log.debug("Adding (" + sFullPath + ") with " + browserName);
		if (Platform.isMac()) {
			browserName = browserName.replace("Mozilla ","");
			try {
				log.debug("Adding (" + sFullPath + ") with " + browserName);
				ProcessBuilder process = new ProcessBuilder("lib/AttachFile.mac.sh", browserName);
				Process p = process.start();
				p.waitFor();
				String result = SimpleFileIO.getFileContents(System.getProperty("user.dir")+"/lib/result.txt");
				if(!Boolean.valueOf(result.split("\n")[0])){
					success = false;
					log.warn("AppleScript failure while adding attachment: "+result);
				}
				log.debug("Applescript exit value: " + result);
			} catch (InterruptedException e) {
				return false;
			}
			Platform.sleep(.5);
			return success;
		}
		
		if (Platform.isWindows()) {
			try {
				success = addFile_Windows();
				Platform.sleep(1);
				return success;
			} catch (Throwable e) {
				return false;
			}
		}
		
		if (Platform.isLinux()) {
			try {
				Platform.sleep(0.5);
				WindowManagement.setBrowserFocus();
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_L);
				robot.keyRelease(KeyEvent.VK_L);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_SLASH);
				robot.keyRelease(KeyEvent.VK_SLASH);
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_A);
				robot.keyRelease(KeyEvent.VK_A);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_BACK_SPACE);
				robot.keyRelease(KeyEvent.VK_BACK_SPACE);
				clip.paste();
			} catch (Exception e) {
				success = false;
			}

			Platform.sleep(0.5);
			log.debug("Press Enter");
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Platform.sleep(0.2);
		}		
		return success;
	}
	 
	public static boolean addFile_Windows() throws Throwable {
		if (!Platform.isWindows())
			return false;
		
		String browserName = WebBrowser.getWebBrowserName();
		log.debug("Set focus to " + browserName);
		log.debug("OS: " + Platform.getOSNameAndVersion());
		String workingdir = System.getProperty("user.dir")
				+ Platform.getFileSeparator() + "lib"
				+ Platform.getFileSeparator();
		ProcessBuilder process = null;

		process = new ProcessBuilder(workingdir + "AttachFile.exe", browserName.toString());
		int exitCode=0;
		try {
			Process p = process.start();
			exitCode = p.waitFor();
			System.out.println("process : " + exitCode);
		} catch (InterruptedException e) {
		}

		Platform.sleep(1);
		return exitCode == 0;
	}	 
}
