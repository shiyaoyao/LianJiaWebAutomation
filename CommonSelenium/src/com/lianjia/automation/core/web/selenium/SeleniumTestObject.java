package com.lianjia.automation.core.web.selenium;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import tasks.web.util.DateUtils;
import tasks.web.util.SeleniumUtils;
import tasks.web.util.StringUtils;
import tasks.web.util.WindowManagement;

import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.SeleniumCore;
import com.lianjia.automation.core.web.WebBrowser;

public class SeleniumTestObject {
	private String objectLocator = "";
	private By findby = null;
	
	public char nonbreakingspace = '\u00a0';
	
	public SeleniumTestObject() {
		
	}	
	
	public SeleniumTestObject(String myLocator) {
		objectLocator = myLocator;
		if(myLocator.startsWith("css=")){
			findby = By.cssSelector(myLocator.replace("css=", ""));
		} else if(myLocator.startsWith("//")) {
			findby = By.xpath(myLocator);
		} else if(myLocator.startsWith("linktext=")){
			findby = By.linkText(myLocator.replace("linktext=", ""));
		} else if(myLocator.startsWith("className=")){
			findby = By.className(myLocator.replace("className=", ""));
		} else if(myLocator.startsWith("id=")){
			findby = By.id(myLocator.replace("id=", ""));
		}
	}
	
	public static SeleniumTestObject getNewWebObject(String sLocator) {
		return new SeleniumTestObject(sLocator);
	}
	
	public WebElement getChildObjectByXpath(String xpathLocator) {
		WebElement oWe = this.getWebElement();
		
		return oWe.findElement(By.xpath("." + xpathLocator));
	}
	
	public WebElement getChildObjectByCss(String cssLocator) {
		WebElement oWe = this.getWebElement();
		
		return oWe.findElement(By.cssSelector(cssLocator));
	}
	
	public WebElement getWebElement() {
		WebElement oWe = null;
		
		try {
			oWe = SeleniumCore.getWebDriverBrowser().getWebDriverAPI().findElement(findby);
		} catch (Exception e) {
			//
		}
		return oWe;
	}

	public String getObjectLocator() {
		return objectLocator;
	}
	
	public By getFindBy() {
		return findby;
	}
	
	public By getFindBy(String myLocator){
		if(myLocator.startsWith("css=")){
			findby = By.cssSelector(myLocator.replace("css=", ""));
		} else if(myLocator.startsWith("//")) {
			findby = By.xpath(myLocator);
		} else if(myLocator.startsWith("linktext=")){
			findby = By.linkText(myLocator.replace("linktext=", ""));
		} else if(myLocator.startsWith("className=")){
			findby = By.className(myLocator.replace("className=", ""));
		} else if(myLocator.startsWith("id=")){
			findby = By.id(myLocator.replace("id=", ""));
		}
		
		return findby;
	}
	
	public boolean exists() {
		boolean found = false;
		if(findby!=null){
			try {
				found =  SeleniumCore.getWebDriverBrowser().getWebDriverAPI().findElements(findby).size() > 0;
				return found;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return found;
		}
		return found;
	}

	public boolean isVisible(){
		if(!this.exists()){
			return false;
		}
		String sVisAttrib = getClassProperty();
		if(sVisAttrib.contains("s-hidden") || sVisAttrib.contains("s-nodisplay")) 
			return false;
		sVisAttrib = getStyleProperty().toLowerCase();
		if(sVisAttrib.contains("display: none") || sVisAttrib.contains("display:none") 
				|| sVisAttrib.contains("visibility: hidden") || sVisAttrib.contains("visibility:hidden")) 
			return false;		

		try {
			WebElement temp = getWebElement();
			if(temp == null) {
				System.out.println("**** Obect passed .exists() but getWebElement() is returning null! ****");
				return false;
			}
			return temp.isDisplayed();
		} catch (Exception e) {
			System.out.println("**** Exception trying to find .isDisplayed() of WebElement ****");
			return false;
		}
	}
	
	public static boolean isVisible(String sLocator){
		return (getNewWebObject(sLocator).isVisible());
	}
	
	public static boolean isVisible(SeleniumTestObject testObject){
		if(!testObject.exists()) return false;
		String style = testObject.getProperty("className");
		if(style.isEmpty())style = testObject.getProperty("class");
		if(style.contains("s-hidden") || style.contains("s-nodisplay")) return false;
		style = testObject.getProperty("style");
		if(style.contains("display: none") || style.contains("display:none")) return false;
		return true;
	}
	
	public boolean isEnabled() {
		String sEnabled = this.getProperty("disabled");
		boolean bEnabled = sEnabled.equalsIgnoreCase("false") || sEnabled.isEmpty();
		return bEnabled;
	}
	
	public void click() {
		if(WebBrowser.isInternetExplorer()) {
			this.jsClick();
			return;
		}
		clickElement();
	}

	public void jsClick() {
		executeScript("arguments[0].click();");
		return;
	}
	
	public void jsMouseOut() {
		String js = "var evt = window.createEvent('MouseEvent'); " +
		        "evt.initMouseEvent('mouseout', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null); " +
		        "document.getElementById('" + this.getId() + "').dispatchEvent(evt);";
				executeScript(js+"arguments[0].onmouseout(evt);");
				return;
	}
	
	public void jsMouseOver() {
		String js = "var evt = window.createEvent('MouseEvent'); " +
		        "evt.initMouseEvent('mouseover', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null); " +
		        "document.getElementById('" + this.getId() + "').dispatchEvent(evt);";
				executeScript(js+"arguments[0].onmouseover(evt);");
				return;
	}
	
	public String executeScript(String js){
		String sEval = "";
		JavascriptExecutor jsEval = (JavascriptExecutor)SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
		try {
			sEval = jsEval.executeScript(js,getWebElement()).toString();
		} catch (Exception e) {
			//
		}
		return sEval;
	}

	public boolean clickElement() {
		try{
			getWebElement().click();
		} catch (Exception e) {
			//
		}
		return false;
	}
	
	public void ctlClick(){
		WebElement elem = SeleniumCore.getWebDriverBrowser().getWebDriverAPI().findElement(this.findby);
		
		if (!WebBrowser.isChrome() && !Platform.isMac()){
			
			try {
				int _key = Platform.isMac() ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
				Robot robot = new Robot();
				try {
					WindowManagement.setBrowserFocus();
				} catch (IOException e) {
					e.printStackTrace();
				}
				robot.keyPress(_key);
				elem.click();
				robot.keyRelease(_key);
			} catch (AWTException e) {
				throw new AssertionError(e.getMessage());
			}

		}else {
			Keys key = Platform.isMac() ? Keys.META : Keys.CONTROL;
			Actions actions = new Actions(SeleniumCore.getWebDriverBrowser().getWebDriverAPI());
			actions.sendKeys(key).click(elem).perform();
		}
	}
	
	public void hover() {
		this.waitForMatchingElement(2000);
		Actions action = new Actions(SeleniumCore.getWebDriverBrowser().getWebDriverAPI());
		action.moveToElement(getWebElement()).perform();
	}
	
	public void hover(double iSeconds) {
		this.hover();
		Platform.sleep(iSeconds);
	}
	
	public void hover(int x, int y) {
		this.waitForMatchingElement();
		Actions action = new Actions(SeleniumCore.getWebDriverBrowser().getWebDriverAPI());
		action.moveToElement(getWebElement(), x, y).perform();
	}
	
	public void setText(String sText) {
		WebElement we = getWebElement();
		try{
			we.clear();
			we.sendKeys(sText);
		} catch (Exception e) {
			if(this.exists())
				this.setValue(sText);
		}
	}
	
	public void clearText() {
		getWebElement().clear();
	}
	
	public void typeText(String sText) {
		getWebElement().sendKeys(sText);
	}

	public void setValue(String sValue) {
		JavascriptExecutor jsEval = (JavascriptExecutor)SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
		jsEval.executeScript("arguments[0].value = arguments[1];", this.getWebElement(), sValue);

	}
	
	public void setDOMAttribute(String sAttributeName, String sValue) {
		JavascriptExecutor jsEval = (JavascriptExecutor)SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
		jsEval.executeScript("arguments[0]."+sAttributeName+" = arguments[1];", this.getWebElement(), sValue);
	}
	
	public void pressEnter() {
		getWebElement().sendKeys(Keys.ENTER);
	}
	
	public void pressTab() {
		getWebElement().sendKeys(Keys.TAB);
	}

	public String getTextContent(){
		String text = "";
		if (this.exists()){
			if(text.isEmpty())
				text = this.getProperty("textContent");
			if(text.isEmpty())
				text = this.getProperty("outerText");
			if(text.isEmpty())
				text = this.getProperty("value");
						
			text = text == null ? "" : text.replace(nonbreakingspace, ' ');
			if(text.equals("null")) text = "";
		}
		return text;
	}
	
	public void scrollIntoView(String sContainerID) {
		String sOffsetTop = this.getProperty("offsetTop");
		String js = "window.getElementById('"+sContainerID+"').scrollTop="+sOffsetTop;
		if(!sOffsetTop.equals(""))
			SeleniumUtils.jsEval(js);
	}
	
	public void scrollIntoView(SeleniumTestObject oContainer) {
		String sOffsetTop = this.getProperty("offsetTop");
		String js = "window.getElementById('"+oContainer.getId()+"').scrollTop="+sOffsetTop;
		if(!sOffsetTop.equals(""))
			SeleniumUtils.jsEval(js);
	}
	
	public void horizScrollIntoView(SeleniumTestObject oContainer) {
		String sOffsetLeft = this.getProperty("offsetLeft");
		String js = "window.getElementById('"+oContainer.getId()+"').scrollLeft="+sOffsetLeft;
		if(!sOffsetLeft.equals(""))
			SeleniumUtils.jsEval(js);
	}
	
	public void horizScrollToPosition(int position) {
		String js = "window.getElementById('"+this.getId()+"').scrollLeft="+position;
		SeleniumUtils.jsEval(js);
	}
	
	public String getText() {
		return getTextContent();
	}

	public String getWidthAndHeight(){
		String wandh = "0,0";
		String w = "0";
		String h = "0";
		
		if(this.exists()){
			w = this.getProperty("offsetWidth");
			w = w.equals("") ? "0" : w;
			h = this.getProperty("offsetHeight");
			h = h.equals("") ? "0" : h;
			wandh = w+","+h;
		}
		return wandh;
	}

	public int getWidth() {
		int width = 0;
		if(this.exists()) {
			width = Integer.valueOf(this.getProperty("offsetWidth"));
		}
		return width;
	}
	
	public Dimension getSize(){
		Dimension dim;
		String[] wh = this.getWidthAndHeight().split(",");
		dim = new Dimension(Integer.valueOf(wh[0].trim()), Integer.valueOf(wh[1]));
		return dim;
	}

	public int getLeftPosition() {
		int left = 0;
		if(this.exists()){
			left = this.getWebElement().getLocation().getX();
		}
		return left;
	}

	public int getRightPosition() {
		int right = 0;
		if(this.exists()){
			right = getLeftPosition() + getWidth();
		}
		return right;
	}

	public boolean zeroDimension(){
		String[] wh = getWidthAndHeight().split(",");
		return wh[0].equals("0") || wh[1].equals("0");
	}
	
	public int getZIndex(){
		String[] style = getProperty("style").split(";");
		String sZ = "";
		int iZ = -1;
		for(String s : style){
			if(s.toLowerCase().trim().startsWith("z")){
				sZ = s.split(":")[1].trim();
				iZ = Integer.valueOf(sZ);
				System.out.println("Z-index: "+iZ);
				break;
			}
		}
		return iZ;
	}

	public String getBackgroundColor() {
		String bg;
		Pattern patern = Pattern.compile("\\(\\d+, \\d+, \\d+");
		Matcher matcher;
		String bgcolor = "";
		String css = this.getWebElement().getCssValue("background-color");
		
		System.out.println("css==========" + css);
		
		String temp = "";
		matcher = patern.matcher(css);
		if(matcher.find()){
			temp = matcher.group();
			temp = temp.substring(1);
			String[] color = StringUtils.csvStringToArray(temp);
			bg = Integer.toHexString(Integer.valueOf(color[0]));
			bg = bg.length() == 1 ? "0"+bg : bg;
			temp = bg;
			bg = Integer.toHexString(Integer.valueOf(color[1]));
			bg = bg.length() == 1 ? "0"+bg : bg;
			temp += bg;
			bg = Integer.toHexString(Integer.valueOf(color[2]));
			bg = bg.length() == 1 ? "0"+bg : bg;
			temp += bg;
			bgcolor = temp.toUpperCase();
		} else if(css.contains("#")){
			bgcolor = StringUtils.getNumberInString(css);
		}
		return bgcolor;
	}

	public String screenPointCenter(){
		String[] wandh = getWidthAndHeight().split(",");
		int w = Integer.valueOf(wandh[0])/2;
		int h = Integer.valueOf(wandh[1])/2;
		System.out.println("Center pt: "+String.valueOf(w)+","+String.valueOf(h));
		return String.valueOf(w)+","+String.valueOf(h);
	}

	public String getXpathLocator(){
		String sId = getProperty("id");
		if(sId.equals("")) return "";
		return "//*[@id='"+sId+"']";
	}

	public String getCssLocator(){
		String sId = getProperty("id");
		if(sId.equals("")) return "";
		return  "css=#" + sId +"']";
	}

	public int getObjectCount() {
		int count = 0;

		try {
			count =  SeleniumCore.getWebDriverBrowser().getWebDriverAPI().findElements(findby).size();
			return count;
		} catch (Exception e) {
			//
		}
		return count;
	}

	public static int getObjectCount(String locator) {
		SeleniumTestObject wo = new SeleniumTestObject(locator);
		return wo.getObjectCount();
	}

	public String getClassProperty() {
		return this.getProperty("class");
	}
	
	public String getStyleProperty() {
		return this.getProperty("style");
	}
	
	public String getValue() {
		return this.getProperty("value");
	}
	
	public String getId(){
		return this.getProperty("id");
	}
	
	public SeleniumTestObject getParentByTag(String tag) {
		tag = tag.toLowerCase();
		SeleniumTestObject wo = new SeleniumTestObject("null");
		String thisId = getId();
		String parent = "";
		
		String ancestor = "/ancestor::"+tag+"[1]";
		String currentBy = getFindBy().toString();
		if(currentBy.startsWith("By.id") || currentBy.startsWith("By.xpath")) {
			if(currentBy.startsWith("By.id"))
				parent = "//*[@id='" + thisId + "']";
			else
				parent = getObjectLocator();
			
			wo = new SeleniumTestObject(parent+"/ancestor::"+tag+"[1]");
		} else if(!thisId.isEmpty()) {
			wo = new SeleniumTestObject("//*[@id='" + thisId + "']" + ancestor);
		}
		if(!wo.getId().isEmpty())
			wo = new SeleniumTestObject(wo.getId());
		
		return wo;
	}

	public SeleniumTestObject getParent() {
		return getParentByTag("*");
	}

	public SeleniumTestObject getChild() {
		String loc = this.getObjectLocator();
		if(loc.startsWith("//"))
			loc = loc + "/*[1]";
		else if (loc.startsWith("css"))
			loc = loc+">*";
		else if (!this.getId().isEmpty())
			loc = "//*[@id='"+this.getId()+"']/*[1]";		
	
		return new SeleniumTestObject(loc);
	}
	
	public SeleniumTestObject getChildByTag(String tagname) {
		String loc = this.getObjectLocator();
		if(loc.startsWith("//"))
			loc = loc + "//"+tagname+"[1]";
		else if (loc.startsWith("css"))
			loc = loc+" "+tagname;
		else if (!this.getId().isEmpty())
			loc = "//*[@id='"+this.getId()+"']//"+tagname+"[1]";
		
		return new SeleniumTestObject(loc);
	}

	public String getOnClick() {
		String sMainDoc = "";
		String js = this.getProperty("onclick");
		if(js.contains(sMainDoc+".") && !js.contains("window."+sMainDoc+".")){
			js = js.replace(sMainDoc+".", "window."+sMainDoc+".");
		}
		return js;
	}
	
	public String getProperty(String sProp) {;
		String attrib = "";
		if(findby==null){
			if(objectLocator.startsWith("css=")){
				findby = By.cssSelector(objectLocator.replace("css=", ""));
			} else if(objectLocator.startsWith("//")) {
				findby = By.xpath(objectLocator);
			} else if(objectLocator.startsWith("linktext=")){
				findby = By.linkText(objectLocator.replace("linktext=", ""));
			} else if(objectLocator.startsWith("className=")){
				findby = By.className(objectLocator.replace("className=", ""));
			} else if(objectLocator.startsWith("id=")){
				findby = By.id(objectLocator.replace("id=", ""));
			}
		}
		try {
			WebElement temp = getWebElement();
			attrib = temp.getAttribute(sProp);
			
			attrib = attrib == null ? "" : attrib;
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return attrib;
	}

	public boolean waitForMatchingElement(){
		return waitForMatchingElement(60*1000); // 1min = 60,000ms
	}

	public boolean waitForMatchingElement(long milliseconds){
		double timeout = (double)milliseconds; // milliseconds
		double start = System.nanoTime()/1e6;
		int tick =0;

		boolean success = this.exists();
		double now = (System.nanoTime()/1e6) - start;
		while(!success && now < timeout){
			tick++;
			Platform.sleep(.01);
			success = this.exists();
			now = (System.nanoTime()/1e6) - start;
		}
		
		if(!success)
			System.out.println("\nFailed to Exist: timeout = "+milliseconds+" ms, actual: "+String.format("%.1f",now)+" ms ("+tick+")\n  "+this.getObjectLocator()+"\n");
		return success;
	}
	
	public boolean waitForNotExists(long milliseconds) {
		double timeout = (double)milliseconds; // milliseconds
		double start = System.nanoTime()/1e6;
		int tick = 0;

		boolean success = !this.exists();
		double now = (System.nanoTime()/1e6) - start;
		while(!success && now < timeout){
			tick++;
			Platform.sleep(.01);
			success = !this.exists();
			now = (System.nanoTime()/1e6) - start;
		}
		if(!success)
			System.out.println("\nStill Exists: timeout = "+milliseconds+" ms, actual: "+String.format("%.1f",now)+" ms ("+tick+")\n  "+this.getObjectLocator()+"\n");
		return success;
	}

	public boolean waitForVisibleElement(long milliseconds){
		double timeout = (double)milliseconds; // milliseconds
		double start = System.nanoTime()/1e6;
		int tick = 0;
		
		boolean success = this.isVisible();
		double now = (System.nanoTime()/1e6) - start;
		while(!success && now < timeout){
			tick++;
			Platform.sleep(.01);
			success = this.isVisible();
			now = (System.nanoTime()/1e6) - start;
		}
		if(!success)
			System.out.println("\nStill not Visible: timeout = "+milliseconds+" ms, actual: "+String.format("%.1f",now)+" ms ("+tick+")\n  "+this.getObjectLocator()+"\n");
		return success;
	}
	
	public boolean waitForExistence(){
		return waitForMatchingElement();
	}

	public static SeleniumTestObject waitForAnyToExist(SeleniumTestObject[] aObjects){
		long milliseconds = 60*1000; // 1 minute
		return waitForAnyToExist(aObjects, milliseconds);
	}

	public static SeleniumTestObject waitForAnyToExist(SeleniumTestObject[] aObjects, long wait_millisec){
		double timeout = (double)wait_millisec; // milliseconds
		double start = System.nanoTime()/1e6;
		double now = (System.nanoTime()/1e6) - start;

		while(now < timeout){
			for(SeleniumTestObject wo : aObjects){
				if(wo.exists()){
					System.out.println("\nFind any: "+wo.getObjectLocator()+" timeout: "+timeout+" ms, actual: "+String.format("%.1f", now)+" ms\n");
					return new SeleniumTestObject(wo.getObjectLocator());
				}
			}
			now = (System.nanoTime()/1e6) - start;
		}
		return null;
	}
	
	public boolean waitForNotVisible(){
		return waitForNotVisible(Platform.giWaitTO);
	}

	public boolean waitForNotVisible(double seconds){
		double timeout = seconds > 120 ? (double)seconds / 1000 : (double)seconds;
		double start = System.nanoTime();
		int tick = 0;
		
		boolean success = !this.isVisible();
		double now = DateUtils.elapsedSeconds(start);
		while(!success && now < timeout){
			tick++;
			success = !this.isVisible();
			now = DateUtils.elapsedSeconds(start);
		}
		
		if(!success)
			System.out.println("\nStill Not Visible: timeout = "+seconds+" sec, actual: "+String.format("%.1f",now)+" sec ("+tick+")\n  "+this.getObjectLocator()+"\n");
		return success;
	}

	public boolean waitForEnabled(long milliseconds) {
		double timeOut = milliseconds/1000;
		double begin = System.nanoTime();
		while(DateUtils.elapsedSeconds(begin) < timeOut) {
			if(isEnabled())
				return true;
		}
		return false;
	}
	
	public boolean waitForDisabled(long milliseconds) {
		double timeOut = milliseconds/1000;
		double begin = System.nanoTime();
		while(DateUtils.elapsedSeconds(begin) < timeOut) {
			if(!isEnabled())
				return true;
		}
		return false;
	}

	public static boolean waitForAllToExist(SeleniumTestObject[] aObjects) {
		boolean bFound = false;
		boolean bAllFound = true;
		int count = 240;
		for (int i = 0; i < aObjects.length; i++) {
			bFound = aObjects[i].exists();
			count = 240;
			while (!bFound && count-- > 0) {
				Platform.sleep(0.25);
			}
			bAllFound &= bFound;

			if (!bAllFound)
				break;
		}
		return bAllFound;
	}
	
	public static boolean waitForAllToExist(SeleniumTestObject[] aObjects, long milliseconds) {
		double timeout = (double)milliseconds; // milliseconds
		double start = System.nanoTime()/1e6;
		double now = (System.nanoTime()/1e6) - start;

		ArrayList<SeleniumTestObject> notFound = new ArrayList<SeleniumTestObject>();
		for(SeleniumTestObject wo : aObjects){
			notFound.add(wo);
		}
		while(notFound.size() > 0 &&  now < timeout){
			for(int i=0; i < notFound.size(); i++){
				if(notFound.get(i).exists()){
					notFound.remove(i);
				}
			}
			now = (System.nanoTime()/1e6) - start;
		}
		return notFound.size() == 0;
	}
}
