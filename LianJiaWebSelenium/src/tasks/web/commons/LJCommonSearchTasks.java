package tasks.web.commons;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.lianjia.automation.core.Platform;

import tasks.base.BaseTask;
import appobjects.web.bases.LJLocators;
import appobjects.web.commons.LJCommonSearch;

public class LJCommonSearchTasks extends BaseTask{
	private static LJCommonSearch ljCommonSearch = new LJCommonSearch(LJLocators.LJ_COMMONSEARCH);	
		
	public static boolean selectItemByChannel(String channel){	
		WebElement check = ljCommonSearch.getCheckField();
		check.click();
		
		Platform.sleep(3);
				
    	List<WebElement> allSearchItems = ljCommonSearch.getWebElement().findElement(By.className("search-tab")).findElement(By.className("tabs")).findElements(By.tagName("li"));
		
    	int i = 0;    	
    	WebElement item = allSearchItems.get(i);
    	
    	while(i<allSearchItems.size() && !item.getText().equals(channel)){
    		i = i+1;
    		item = allSearchItems.get(i);
    	}
		
    	if(item.getText().equals(channel)){
    		item.click();
    		
        	return true;
    	} else
    		return false;    	
	}
	
	public static String getCurSelectItem(){
		return ljCommonSearch.getCheckField().getText();
	}
	
	//搜索框tips
	public static String getSearchBoxTips(){
		return ljCommonSearch.getSearchBoxField().getAttribute("placeholder");
	}
	
	//聚焦搜索框
	public static void setFocusOnSearchBoxField(){
		ljCommonSearch.getSearchBoxField().click();
	}
	
	//热门搜索
	public static boolean isHotSearchExist(String channel){
		boolean isHotName = false;
		boolean isHotList = false;
		
		WebElement web = ljCommonSearch.getWebElement().findElement(By.id(channel));
		
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
		WebElement web = ljCommonSearch.getWebElement().findElement(By.id("suggest-cont"));
		
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
		
		WebElement web = ljCommonSearch.getWebElement().findElement(By.id(channel));
			
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
		ljCommonSearch.getSearchBoxField().clear();
		ljCommonSearch.getSearchBoxField().sendKeys(keyWord);
	}
		
	//搜索icon
	public static void clickSearchIcon(){
		ljCommonSearch.getSearchBtn().click();
	}
	
}
