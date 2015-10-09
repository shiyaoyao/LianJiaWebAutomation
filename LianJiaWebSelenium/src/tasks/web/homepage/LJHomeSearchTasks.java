package tasks.web.homepage;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import tasks.base.BaseTask;
import appobjects.web.bases.LJLocators;
import appobjects.web.homepage.LJHomeSearch;

public class LJHomeSearchTasks extends BaseTask{
	private static LJHomeSearch ljHomeSearch = new LJHomeSearch(LJLocators.LJ_HOMESEARCH);	
	
	public static boolean selectTabByChannel(String channel){	 	
    	List<WebElement> allSearchTabs = ljHomeSearch.getWebElement().findElement(By.className("menu")).findElements(By.tagName("li"));
		
    	int i = 0;    	
    	WebElement tab = allSearchTabs.get(i);
    	
    	while(i<allSearchTabs.size() && !tab.getText().equals(channel)){
    		i = i+1;
    		tab = allSearchTabs.get(i);
    	}
		
    	if(tab.getText().equals(channel)){
    		WebElement selTab = tab.findElement(By.tagName("span"));    	
        	selTab.click();	
        	
        	return true;
    	} else
    		return false;    	
	}
	
	//搜索框tips
	public static String getSearchBoxTips(){
		return ljHomeSearch.getSearchBoxField().getAttribute("placeholder");
	}
	
	//聚焦搜索框
	public static void setFocusOnSearchBoxField(){
		ljHomeSearch.getSearchBoxField().click();
	}
	
	//热门搜索
	public static boolean isHotSearchExist(String channel){
		boolean isHotName = false;
		boolean isHotList = false;
		
		WebElement web = ljHomeSearch.getWebElement().findElement(By.id(channel));
		
		WebElement hotName = web.findElement(By.className("hot-name"));
		
		if(hotName.getText().equals("热门搜索"))
			isHotName = true;
		
		List<WebElement> hotList = web.findElement(By.className("list")).findElements(By.tagName("li"));
		if(hotList.size() > 0)
			isHotList = true;
		
		if(isHotName && isHotList)
			return true;
		else
			return false;		
	}
	
	//搜索sug
	public static boolean isSearchSugExist(){		
		WebElement web = ljHomeSearch.getWebElement().findElement(By.id("suggest-cont"));
		
		List<WebElement> sugList = web.findElements(By.tagName("li"));
		if(sugList.size() > 0)
			return true;
		else
			return false;		
	}
	
	//热门搜索
	public static boolean isSearchHistoryExist(String channel){
		boolean isHistoryName = false;
		boolean isHistoryList = false;
		
		WebElement web = ljHomeSearch.getWebElement().findElement(By.id(channel));
			
		WebElement hotName = web.findElement(By.className("hot-name"));
			
		if(hotName.getText().equals("搜索历史"))
			isHistoryName = true;
			
		List<WebElement> hotList = web.findElement(By.className("list")).findElements(By.tagName("li"));
		if(hotList.size() > 0)
			isHistoryList = true;
		
		if(isHistoryName && isHistoryList)
			return true;
		else
			return false;		
	}
	
	//搜索输入框输入关键词
	public static void setKeyWord(String keyWord){
		ljHomeSearch.getSearchBoxField().clear();
		ljHomeSearch.getSearchBoxField().sendKeys(keyWord);
	}
		
	//搜索icon
	public static void clickSearchIcon(){
		ljHomeSearch.getSearchBtn().click();
	}
	
}
