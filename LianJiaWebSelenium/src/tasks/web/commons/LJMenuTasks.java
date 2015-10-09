package tasks.web.commons;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import tasks.base.BaseTask;
import appobjects.web.LJAppObject;
import appobjects.web.commons.LJMenu;
import appobjects.web.commons.LJMenuItem;

import com.lianjia.StringFetch;
import com.lianjia.automation.core.Platform;

public class LJMenuTasks extends BaseTask{
	private static final Logger log = Logger.getLogger(LJMenuTasks.class);
	
	public static boolean selectItemInCurrentMenu(String itemText){
		if( itemText.startsWith("L_")) itemText = StringFetch.getString(itemText);
		log.info("Selecting '"+itemText+"' from the top-most menu");
		LJMenu oMenu = LJMenu.getCurrentMenu();
		if(!oMenu.exists()){
			log.error("Menu ("+oMenu.getObjectLocator()+") not found");
			logScreenCapture();
			return false;
		}
		return oMenu.selectItem(itemText);
	}
	
	public static boolean isItemInCurrentMenu(String itemText){
		if( itemText.startsWith("L_")) itemText = StringFetch.getString(itemText);
		LJMenuItem menitem = LJMenu.getCurrentMenuItem(itemText);
		if(menitem.exists())
			return true;
		return false;
	}

	public static ArrayList<String> getAllItemText() {
		ArrayList<String> items = new ArrayList<String>();
		LJMenu m = getTopMostMenu();
		if(m!=null){
			for(LJMenuItem i : m.getAllItems()){
				items.add(i.getText());
			}
		}
		return items;
	}
	
	public static ArrayList<String> getAllItemText(LJMenu menu) {
		ArrayList<String> items = new ArrayList<String>();
		if(menu!=null){
			for(LJMenuItem i : menu.getAllItems()){
				items.add(i.getText());
			}
		}
		return items;
	}
	
	
	public static boolean selectItemInCurrentMenu(int itemIndex){
		log.info("Selecting item ("+itemIndex+") from the top-most menu");
		LJMenu oMenu = LJMenu.getCurrentMenu();
		return oMenu.selectItem(itemIndex);
	}
	
	public static boolean selectFromSubMenu(String sTopMenu, String sSubMenu) {
		if(sTopMenu.startsWith("L_")) sTopMenu = StringFetch.getString(sTopMenu);
		if(sSubMenu.startsWith("L_")) sSubMenu = StringFetch.getString(sSubMenu);	
		log.info("Select '"+sSubMenu+"' from the '"+sTopMenu+"' menu");
		ArrayList<LJMenu> allMenus = getAllVisibleMenus();
		int iMenus = allMenus.size();
		if(iMenus < 1) assertTrue("Top menu '"+sTopMenu+"' found", false);
		if(selectItemInTopMostMenu(allMenus, sTopMenu)){
			allMenus = getAllVisibleMenus();
			if(allMenus.size()==iMenus){
				Platform.sleep(1);
				allMenus = getAllVisibleMenus();
			}
			
			return selectItemInTopMostMenu(allMenus, sSubMenu);
		}
		return false;
	}
	
	public static ArrayList<LJMenu> getAllVisibleMenus(){
		ArrayList<LJMenu> menus =  new ArrayList<LJMenu>();
		menus.clear();
		int i = 1;
		// I was getting some failures finding context menus with the other locator
		// pattern. I've replaced it with the locator patter we use to find visible
		// pop-up menus - epm (22 jan 2014)
		LJMenu current = new LJMenu(LJAppObject.parseLocator(null,null));
		while(current.exists() && i<14){
			if(current.isVisible()) 
				menus.add(new LJMenu(LJMenu.convertIdToCssDivPath(current.getProperty("id"))));
			i++;
			current = new LJMenu(LJAppObject.parseLocator(null,null));
		}
		return menus;
	}
	
	public static int countOpenMenus(){
		int iCount =  getAllVisibleMenus().size();
		return iCount;
	}
	
	public static boolean selectItemInTopMostMenu(String sItem) {
		if( sItem.startsWith("L_")) sItem = StringFetch.getString(sItem);
		log.info("Selecting '"+sItem+"' from the top-most menu");
		LJMenu oMenu = getTopMostMenu();
		return oMenu.selectItem(sItem);
	}
	
	public static boolean selectItemInTopMostMenu(ArrayList<LJMenu> allMenus, String sItem) {
		boolean success = false;
		LJMenu oMenu = getTopMostMenu(allMenus);
		if( sItem.startsWith("L_")) sItem = StringFetch.getString(sItem);
		log.info("Selecting '"+sItem+"' from the top-most menu");
		try {
			success = oMenu.selectItem(sItem);
		} catch (Exception e) {
			assertNotNull("Selecting '"+sItem+"' in topmost menu ("+oMenu.getObjectLocator()+")", null);
		}
		return success;
	}

	public static LJMenuItem getItemInTopMostMenu(String sItem) {
		LJMenuItem oItem = new LJMenuItem("foo");
		LJMenu oMenu = getTopMostMenu();
		oItem = oMenu.getItem(sItem);
		return oItem;
	}
	
	public static LJMenu getTopMostMenu(){
		ArrayList<LJMenu> alMenus = new ArrayList<LJMenu>();
		alMenus = getAllVisibleMenus();
		if (alMenus.size()==0) return null;
		LJMenu topMenu = alMenus.remove(0);
		for (LJMenu menu : alMenus){
			if(menu.getZIndex() > topMenu.getZIndex()){
				topMenu = menu;
			}
		}
		return topMenu;
	}
	
	private static LJMenu getTopMostMenu(ArrayList<LJMenu> allMenus){
		if (allMenus.size()==0) return null;
		LJMenu topMenu = allMenus.remove(0);
		for (LJMenu menu : allMenus){
			if(menu.getZIndex() > topMenu.getZIndex()){
				topMenu = menu;
			}
		}
		return topMenu;
	}

}
