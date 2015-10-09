package appobjects.web.editors;

import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.Keys;

import tasks.web.util.WindowManagement;
import appobjects.web.LJAppObject;
import appobjects.web.WebFrame;

import com.lianjia.automation.core.web.WebBrowser;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public abstract class Editor implements IEditor {
	protected String CONTAINER_ID = "e-${unid}-bodyrich";
	protected String READ_ID = "e-${unid}-body";
	
	protected String editorContainerId;
	public AbstractToolbar oToolbar;
	
	protected SeleniumTestObject oEditorContainer = new SeleniumTestObject("e-null-container");
	protected SeleniumTestObject oReadBody = new SeleniumTestObject("e-null-container"); 
	protected SeleniumTestObject oBody;
	
	public static String TAG = "TAG";
	public static String VALUE = "VALUE";
	public static String STATE = "STATE";
	public static String ENABLED = "ENABLED";
	public static String DISABLED = "DISABLED";
	
	public static String SRC = "SRC";
	public static String SIZE = "SIZE";
	
	public Editor(String id) {
		editorContainerId = id;
	}
	
	public Editor() {
		
	}
	
	protected void init() {
		if(!oEditorContainer.isVisible() && !oReadBody.isVisible()) {
			String id = LJAppObject.getDynamicId();
			editorContainerId = LJAppObject.parseLocator(CONTAINER_ID, id);
			if(editorContainerId.startsWith("//"))
				oEditorContainer = new SeleniumTestObject(editorContainerId);
			else
				oEditorContainer = new SeleniumTestObject("//*[@id='"+editorContainerId+"' and ./*]");
			if(WebBrowser.isInternetExplorer())
				oBody = new SeleniumTestObject("//*[@id='"+editorContainerId+"-editor']");
			else
				oBody = new SeleniumTestObject("//body[@role='main']");
			oReadBody = new SeleniumTestObject("//div[@id='"+LJAppObject.parseLocator(READ_ID, id)+"']");
		}
	}
	
	public boolean exists() {
		init();
		return oEditorContainer.exists() || oReadBody.exists();
	}
	
	public boolean readBodyExists() {
		init();
		return oReadBody.isVisible();
	}
	
	public void setFocus() {
		try {
			init();
			if (!WebBrowser.isInternetExplorer())
				// this blurs window focus on IE
				WindowManagement.setBrowserFocus();
			oEditorContainer.click();
			typeEditorContent(" "+Keys.BACK_SPACE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setEditorContent(String content) {
		init();
		switchToFrame();
		oBody.setDOMAttribute("innerHTML", content);
		resetFrame();
	}
	
	public void typeEditorContent(CharSequence... content){//String content) {
		init();
		SeleniumTestObject wo = new SeleniumTestObject(editorContainerId+"-editor");
		if(WebBrowser.isInternetExplorer()){
			wo.getWebElement().sendKeys(content);
		} else {
			switchToFrame();
			wo.getWebElement().sendKeys(content);
			WebFrame.selectMainFrame();
		}
	}
	public String getEditorContent() {
		init();
		String content = "";
		if(oReadBody.isVisible())
			content = oReadBody.getProperty("innerHTML");
		else if(oEditorContainer.isVisible()) {
			switchToFrame();
			content = oBody.getProperty("innerHTML");
			resetFrame();
		}
		
		return content;
	}
	
	public SeleniumTestObject getButtonOnEditor(){
		init();
		SeleniumTestObject bodyContainer = oReadBody.isVisible()?oReadBody:oEditorContainer;
		SeleniumTestObject button = new WebFrame(bodyContainer.getObjectLocator()+"//input[@type='button']");
		return button;
		
	}
	
	public HashMap<String, String> getButtonInfo () {
		init();
		switchToFrame();
		String sLocator = oReadBody.isVisible() ? oReadBody.getObjectLocator() : oBody.getObjectLocator();
		SeleniumTestObject btn = new WebFrame(sLocator+"//input[@type='button']");
		HashMap<String, String> buttonInfo = new HashMap<String, String>();
		
		if(btn.exists()){
			buttonInfo.put(TAG, btn.getProperty("outerHTML"));
			buttonInfo.put(VALUE, btn.getProperty("value"));
			buttonInfo.put(STATE, btn.isEnabled() ? Editor.ENABLED : Editor.DISABLED);
		} else {
			return null;
		}
		resetFrame();
		return buttonInfo;
	}
	
	/**
	 * Looks for an object with the specified Xpath
	 * @param sXpath An Xpath locator relative to the editor body.<br>
	 * The string should begin with a tagName and NOT with a '/' or '//'<br><br>
	 * Example: <code>isObjectInEditor("a[contains(@href,'google.com')]")</code>
	 *@return a SeleniumTestObject if found, else null
	 */
	public SeleniumTestObject getObjectInEditor(String sXpath) {
		String loc = sXpath.startsWith("/") ? sXpath.substring(1) : sXpath;
		loc = sXpath.startsWith("/") ? sXpath.substring(1) : sXpath;
		init();
		switchToFrame();
		loc = (oReadBody.isVisible() ? oReadBody.getObjectLocator() : oBody.getObjectLocator()) + "//" + loc;
		SeleniumTestObject wo = new SeleniumTestObject(loc);
		return wo.exists() ? wo : null;
	}
	
	/**
	 * Click an object with the specified Xpath
	 * @param sXpath An Xpath locator relative to the editor body.<br>
	 * The string should begin with a tagName and NOT with a '/' or '//'<br><br>
	 * Example: <code>isObjectInEditor("a[contains(@href,'google.com')]")</code>
	 *
	 */
	public void clickObjectInEditor(String sXpath) {	
		SeleniumTestObject wo = getObjectInEditor(sXpath);
		if(wo != null){
			wo.click();
		}
		resetFrame();
	}
	
	/**
	 * Checks for existence of an object with the specified Xpath
	 * @param sXpath An Xpath locator relative to the editor body.<br>
	 * The string should begin with a tagName and NOT with a '/' or '//'<br><br>
	 * Example: <code>isObjectInEditor("a[contains(@href,'google.com')]")</code>
	 *@return true if object found
	 */
	public boolean isObjectInEditor(String sXpath) {
		boolean found = getObjectInEditor(sXpath) != null;
		resetFrame();
		return found;
	}
	
	public HashMap<String, String> getImageInfo (int iIndex) {
		init();
		switchToFrame();
		String sLocator = oReadBody.isVisible() ? oReadBody.getObjectLocator() : oBody.getObjectLocator();
		SeleniumTestObject img = new WebFrame(sLocator+"//img["+iIndex+"]");
		HashMap<String, String> imageInfo = new HashMap<String, String>();
		
		if(img.exists()){
			imageInfo.put(TAG, img.getProperty("outerHTML"));
			imageInfo.put(SRC, img.getProperty("src"));
			imageInfo.put(SIZE, img.getWidthAndHeight());
		} else {
			return null;
		}
		resetFrame();
		return imageInfo;
	}
	
	public String getEditorPlainText() {
		init();
		String content = "";
		if(oReadBody.isVisible())
			content = oReadBody.getText();
		else if(oEditorContainer.isVisible()) {
			switchToFrame();
			content = oBody.getText();
			resetFrame();
		}

		return content;
	}
	
	public void switchToFrame(){
		init();
		WebFrame wo = new WebFrame(oEditorContainer.getObjectLocator()+"/iframe");
		if(wo.exists())
			wo.selectFrame();
	}
	
	public void resetFrame() {
		if(!oEditorContainer.exists() && !oReadBody.exists())
			WebFrame.selectMainFrame();
	}
}
