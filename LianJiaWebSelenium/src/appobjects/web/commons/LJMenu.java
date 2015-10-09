package appobjects.web.commons;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import appobjects.web.LJAppObject;

import com.lianjia.StringFetch;

public class LJMenu extends LJContainer {
	private static final Logger log = Logger.getLogger(LJMenu.class);
	
	/**
	 * Constructor
	 * @param myLocator String of either an XPath or id property
	 */
	public LJMenu(String myLocator) {
		super(LJAppObject.convertIdToCssLocator(myLocator));
	}
	
	public LJMenu() {
		super(LJAppObject.parseLocator(null,null));
	}
	
	/**
	 * Returns the currently visible LJMenu object
	 * @return
	 */
	public static LJMenu getCurrentMenu(){	
		LJMenu current = new LJMenu(LJAppObject.parseLocator(null,null));
		if(current.waitForVisibleElement(2000)){
			return new LJMenu(current.getId());
		}
		return null;
	}
	
	
	/**
	 * If the menu is visible, returns the number of items in the menu. Disabled (grayed out)
	 * items are included in the count.
	 * @return int count of menu items
	 */
	public int itemCount(){
		int i = 0;
		if(this.isVisible()){
			while(getItem(i).exists()){				
				i++;
			}
		}
		return i;
	}
	
	/**
	 * Returns true if there is currently a visible drop down menu, else false
	 * @return
	 */
	public static boolean isMenuVisible(){
		return (new LJMenu()).isVisible();
	}
	
	/**
	 * Returns the LJMenuItem at index i
	 * @param i 0-offset menu item index
	 * @return LJMenuItem 
	 */
	public LJMenuItem getItem(int i){
		String sLoc = LJAppObject.parseIdAndText(null, getId(), i);
		LJMenuItem item = new LJMenuItem(sLoc);
		sLoc = item.getId();
		if(!sLoc.isEmpty())
			item = new LJMenuItem(sLoc);
		return item;
	}
	
	public static LJMenuItem getCurrentMenuItem(int i){
		String sLoc = LJAppObject.parseIdAndText(null,null, i);
		LJMenuItem menit = new LJMenuItem(sLoc);
		sLoc = menit.getId();
		if(!sLoc.isEmpty()) 
			menit = new LJMenuItem(sLoc);
		return menit;
	}

	public static LJMenuItem getCurrentMenuItem(String itemText){
		String sLoc = LJAppObject.parseIdAndText(null,null, itemText);
		LJMenuItem menit = new LJMenuItem(sLoc);
		sLoc = menit.getId();
		if(!sLoc.isEmpty()) 
			menit = new LJMenuItem(sLoc);
		return menit;
		
	}

	/**
	 * Returns the LJMenuItem whose text matches itemText
	 * @param itemText case-sensitive String for the desired menu item
	 * @return LJMenuItem
	 */
	public LJMenuItem getItem(String itemText){
		if( itemText.startsWith("L_")) itemText = StringFetch.getString(itemText);
		String sLoc = LJAppObject.parseIdAndText(null, getId(), itemText);
		LJMenuItem item = new LJMenuItem(sLoc);
		sLoc = item.getId();
		if(!sLoc.isEmpty())
			item = new LJMenuItem(sLoc);
		return item;
		
	}
	
	public boolean selectItem(String itemText){
		LJMenuItem menuitem = getItem(itemText);
		if (!menuitem.exists()){
			log.info("Menu item ("+itemText+") not found");
			return false;
		}
		menuitem.click();
		return true;
	}

	public ArrayList<LJMenuItem> getAllItems(){
		ArrayList<LJMenuItem> items = new ArrayList<LJMenuItem>();
		int i = itemCount();
		for(int x = 0; x < i; x++) {
			items.add(getItem(x));
		}
		return items;
	}
	
	public boolean selectItem(int i){
		LJMenuItem menuitem = getItem(i);
		menuitem.click();
		return true;
	}

}
