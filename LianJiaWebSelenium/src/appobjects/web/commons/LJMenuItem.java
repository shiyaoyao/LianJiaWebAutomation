package appobjects.web.commons;

//import org.apache.log4j.Logger;

import tasks.web.util.StringUtils;

import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.WebBrowser;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;


public class LJMenuItem extends SeleniumTestObject {
//	private static final Logger log = Logger.getLogger(LJMenuItem.class);
	 
	private static String IS_DISABLED = "s-menu-text-disabled";
	
	/**
	 * Constructor
	 * @param myLocator
	 */
	public LJMenuItem(String myLocator){
		super(myLocator);
	}
	
	/**
	 * Return true if the menu item has a image displayed next to it
	 * @return
	 */
	public boolean isSelected(){
		SeleniumTestObject selector = new SeleniumTestObject("//*[@id='"+getId()+"']/td[1]/img");
		return selector.isVisible();
	}
	
	/**
	 * True if the item is grayed out
	 * @return
	 */
	public boolean isDisabled(){
		return this.getClassProperty().contains(IS_DISABLED);
	}
	
	/**
	 * True if the item has a submenu indicator to it's right
	 * @return
	 */
	public boolean hasSubMenu(){
		LJContainer arrow = new LJContainer("//*[@id='"+getId()+"']/td[4]/img");
		return arrow.isVisible();
	}

	public int getItemIndex() {
		String sInt = this.getId();
		int index = StringUtils.getEndDigitsAsInt(sInt);
		return index;
	}
	
	public void click() {
		if(Platform.isSeleniumWD() && WebBrowser.isFirefox()){
			this.jsClick();
			return;
		}
		super.click();
	}
}
