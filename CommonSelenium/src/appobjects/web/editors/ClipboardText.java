package appobjects.web.editors;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

import tasks.web.util.SeleniumUtils;
import tasks.web.util.WindowManagement;

import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class ClipboardText implements ClipboardOwner {
	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		
	}

	public void copy(String sText) {
		StringSelection sCopy = new StringSelection( sText );
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents( sCopy, this );
	}
	
	public String pasteToString() throws UnsupportedFlavorException, IOException {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    String textContent = clipboard.getData(DataFlavor.stringFlavor).toString();
	    return textContent == null ? "" : textContent;
	}
	
	public void pasteToObject(SeleniumTestObject oObject) {
		try{
			System.out.println("************ Pasting text **********");
			WindowManagement.setBrowserFocus();
			if(oObject != null){
				oObject.clickElement();
				System.out.println("**** loc: "+oObject.getObjectLocator());
				System.out.println("**** id: "+oObject.getId());
			}
			SeleniumUtils.pressCtrlEnd();
			Robot ro = new Robot();
			if(Platform.isMac()){			
				ro.keyPress(KeyEvent.VK_META);
				ro.keyPress(KeyEvent.VK_V);
				ro.keyRelease(KeyEvent.VK_V);
				ro.keyRelease(KeyEvent.VK_META);
			} else {
				ro.keyPress(KeyEvent.VK_CONTROL);
				ro.keyPress(KeyEvent.VK_V);
				ro.keyRelease(KeyEvent.VK_V);
				ro.keyRelease(KeyEvent.VK_CONTROL);
				
			}
		} catch (Exception e) {
			//
		}
	}
	
	public void paste() {
		try{
			System.out.println("************ Pasting text **********");
			Robot ro = new Robot();
			if(Platform.isMac()){			
				ro.keyPress(KeyEvent.VK_META);
				ro.keyPress(KeyEvent.VK_V);
				ro.keyRelease(KeyEvent.VK_V);
				ro.keyRelease(KeyEvent.VK_META);
			} else {
				ro.keyPress(KeyEvent.VK_CONTROL);
				ro.keyPress(KeyEvent.VK_V);
				ro.keyRelease(KeyEvent.VK_V);
				ro.keyRelease(KeyEvent.VK_CONTROL);
				
			}
		} catch (Exception e) {
			//
		}
	}
}
