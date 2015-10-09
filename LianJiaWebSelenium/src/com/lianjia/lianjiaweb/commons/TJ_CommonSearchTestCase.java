package com.lianjia.lianjiaweb.commons;

import java.net.URLDecoder;

import tasks.web.commons.LJCommonSearchTasks;
import tasks.web.commons.LJNavTasks;
import tasks.web.homepage.LJHomeHeaderTasks;
import tasks.web.util.WindowManagement;
import appobjects.web.Browser;
import appobjects.web.bases.LJLocators;

import com.lianjia.StringFetch;
import com.lianjia.automation.core.Platform;

public class TJ_CommonSearchTestCase extends AbstractCommonsCategory{
	@Override
	public void testMain() throws Throwable {
		logTestPlan("城市切换 - 天津站");
		String cityName = StringFetch.getString("L_TIANJIN");
		
		LJHomeHeaderTasks.switchToCity(cityName);
		assertEquals("切换至 " + cityName, cityName, LJHomeHeaderTasks.getCurCity());
		String curUrl = Browser.getCurrentUrl();
		//切换至 二手房频道
		assertEquals("切换至二手房频道", curUrl + "ershoufang/", LJNavTasks.gotoErShouFang());
		
		String selItem = LJCommonSearchTasks.getCurSelectItem();
		logCompare(true, selItem.equals("二手房"), "当前所在的搜索分类是：" + selItem);
		
		String searchCategory = StringFetch.getString("ERSHOUFANG");
		String channel = LJLocators.SEARCH_ERSHOUFANG_CHANNEL;
		String sug = StringFetch.getString("TJ_ERSHOUFANG_SUG_KEYWORD");
		String keyWord = StringFetch.getString("TJ_ERSHOUFANG_SEARCH_KEYWORD");
		
		//1.1  二手房 - tips		
		logCompare(true, LJCommonSearchTasks.getSearchBoxTips().length()>0, LJCommonSearchTasks.getSearchBoxTips());
		
		//1.2  二手房 - 热门搜索
		LJCommonSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJCommonSearchTasks.isHotSearchExist(channel), "热门搜索列表");
		
		//1.3  二手房 - sug
		LJCommonSearchTasks.setKeyWord(sug);
		Platform.sleep(5);
		logCompare(true, LJCommonSearchTasks.isSearchSugExist(), "搜索sug");
		
		//1.4  二手房 - 搜索结果跳转		
		LJCommonSearchTasks.setKeyWord(keyWord);
		LJCommonSearchTasks.clickSearchIcon();
		
		Platform.sleep(5);
		
		logCompare(curUrl + channel + "/rs" + keyWord, URLDecoder.decode(Browser.getCurrentUrl(), "UTF-8"), "二手房搜索跳转");

		//1.5  二手房 - 搜索历史		
		LJCommonSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJCommonSearchTasks.isSearchHistoryExist(channel), "搜索历史列表");
		
		//2. 新房
		searchCategory = StringFetch.getString("XINFANG");
		channel = LJLocators.SEARCH_XINFANG_CHANNEL;
		sug = StringFetch.getString("TJ_XINFANG_SUG_KEYWORD");
		keyWord = StringFetch.getString("TJ_XINFANG_SEARCH_KEYWORD"); 
		
		assertEquals("选择 " + searchCategory, true, LJCommonSearchTasks.selectItemByChannel(searchCategory));
		Platform.sleep(5);
		
		//2.1 新房 - tips
		logCompare(true, LJCommonSearchTasks.getSearchBoxTips().length()>0, LJCommonSearchTasks.getSearchBoxTips());
		
		//2.2  新房  - 热门搜索
		LJCommonSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJCommonSearchTasks.isHotSearchExist(channel), "热门搜索列表");
		
		//2.3  新房 - sug
		LJCommonSearchTasks.setKeyWord(sug);
		Platform.sleep(5);
		logCompare(true, LJCommonSearchTasks.isSearchSugExist(), "搜索sug");
		
		//2.4  新房 - 搜索结果跳转				
		LJCommonSearchTasks.setKeyWord(keyWord);
		LJCommonSearchTasks.clickSearchIcon();
		
		Platform.sleep(5);
		
		WindowManagement.switchToNewWindow();
		
		logCompare(curUrl+ channel + "/rs" + keyWord, URLDecoder.decode(Browser.getCurrentUrl(), "UTF-8"), "新房搜索跳转");
		
		WindowManagement.closeWin();

		Browser.refresh();
		
		Platform.sleep(3);
		
		//2.5  新房 - 搜索历史
		assertEquals("选择 " + searchCategory, true, LJCommonSearchTasks.selectItemByChannel(searchCategory));
		LJCommonSearchTasks.setFocusOnSearchBoxField();
		Platform.sleep(3);
		logCompare(true, LJCommonSearchTasks.isSearchHistoryExist(channel), "搜索历史列表");	 
		
		//3. 小区
		searchCategory = StringFetch.getString("XIAOQU");
		channel = LJLocators.SEARCH_XIAOQU_CHANNEL;
		sug = StringFetch.getString("TJ_XIAOQU_SUG_KEYWORD");
		keyWord = StringFetch.getString("TJ_XIAOQU_SEARCH_KEYWORD"); 
		
		assertEquals("选择 " + searchCategory, true, LJCommonSearchTasks.selectItemByChannel(searchCategory));
		Platform.sleep(5);
		
		//3.1 小区 - tips
		logCompare(true, LJCommonSearchTasks.getSearchBoxTips().length()>0, LJCommonSearchTasks.getSearchBoxTips());
		
		//3.2  小区   - 热门搜索
		LJCommonSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJCommonSearchTasks.isHotSearchExist(channel), "热门搜索列表");
		
		//3.3  小区  - sug
		LJCommonSearchTasks.setKeyWord(sug);
		Platform.sleep(5);
		logCompare(true, LJCommonSearchTasks.isSearchSugExist(), "搜索sug");
		
		//3.4  小区  - 搜索结果跳转				
		LJCommonSearchTasks.setKeyWord(keyWord);
		LJCommonSearchTasks.clickSearchIcon();
		
		Platform.sleep(5);
		
		WindowManagement.switchToNewWindow();
		
		logCompare(curUrl+ channel + "/rs" + keyWord, URLDecoder.decode(Browser.getCurrentUrl(), "UTF-8"), "小区搜索跳转");
		
		Platform.sleep(3);
		
		//3.5 小区  - 搜索历史
		LJCommonSearchTasks.setFocusOnSearchBoxField();
		Platform.sleep(5);
		logCompare(true, LJCommonSearchTasks.isSearchHistoryExist(channel), "搜索历史列表");	
		
		//4.房价
		searchCategory = StringFetch.getString("CHAFANGJIA");
		channel = LJLocators.SEARCH_FANGJIA_CHANNEL;
		sug = StringFetch.getString("TJ_FANGJIA_SUG_KEYWORD");
		keyWord = StringFetch.getString("TJ_FANGJIA_SEARCH_KEYWORD"); 
		
		assertEquals("选择 " + searchCategory, true, LJCommonSearchTasks.selectItemByChannel(searchCategory));
		Platform.sleep(5);
		
		//4.1 房价 - tips
		logCompare(true, LJCommonSearchTasks.getSearchBoxTips().length()>0, LJCommonSearchTasks.getSearchBoxTips());
		
		//4.2  房价   - 热门搜索
		LJCommonSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJCommonSearchTasks.isHotSearchExist(channel), "热门搜索列表");
		
		//4.3 房价  - sug
		LJCommonSearchTasks.setKeyWord(sug);
		Platform.sleep(5);
		logCompare(true, LJCommonSearchTasks.isSearchSugExist(), "搜索sug");
		
		//4.4 房价  - 搜索结果跳转				
		LJCommonSearchTasks.setKeyWord(keyWord);
		LJCommonSearchTasks.clickSearchIcon();
		
		Platform.sleep(5);
		
		WindowManagement.switchToNewWindow();
		
		logCompare(curUrl+ channel + "/rs" + keyWord, URLDecoder.decode(Browser.getCurrentUrl(), "UTF-8"), "房价搜索跳转");
		
		Platform.sleep(3);
		
		//4.5  房价  - 搜索历史
		LJCommonSearchTasks.setFocusOnSearchBoxField();
		Platform.sleep(5);
		logCompare(true, LJCommonSearchTasks.isSearchHistoryExist(channel), "搜索历史列表");	
		
		//5. 经纪人
		searchCategory = StringFetch.getString("JINGJIREN");
		channel = LJLocators.SEARCH_JINGJIREN_CHANNEL;
		keyWord = StringFetch.getString("TJ_JINGJIREN_SEARCH_KEYWORD"); 
		
		assertEquals("选择 " + searchCategory, true, LJCommonSearchTasks.selectItemByChannel(searchCategory));
		Platform.sleep(5);
		
		//5.1 经纪人 - tips
		logCompare(true, LJCommonSearchTasks.getSearchBoxTips().length()>0, LJCommonSearchTasks.getSearchBoxTips());
		
		//5.2 经纪人  - 搜索结果跳转				
		LJCommonSearchTasks.setKeyWord(keyWord);
		LJCommonSearchTasks.clickSearchIcon();
		
		Platform.sleep(5);
		
		WindowManagement.switchToNewWindow();
		
		logCompare(curUrl+ channel + "/rs" + keyWord, URLDecoder.decode(Browser.getCurrentUrl(), "UTF-8"), "经纪人搜索跳转");
		
		//6.租房
		searchCategory = StringFetch.getString("ZUFANG");
		channel = LJLocators.SEARCH_CHUZUFANG_CHANNEL;
		sug = StringFetch.getString("TJ_FANGJIA_SUG_KEYWORD");
		keyWord = StringFetch.getString("TJ_FANGJIA_SEARCH_KEYWORD"); 
		
		assertEquals("选择 " + searchCategory, true, LJCommonSearchTasks.selectItemByChannel(searchCategory));
		Platform.sleep(5);
		
		//6.1 租房 - tips
		logCompare(true, LJCommonSearchTasks.getSearchBoxTips().length()>0, LJCommonSearchTasks.getSearchBoxTips());
		
		//6.2  租房   - 热门搜索
		LJCommonSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJCommonSearchTasks.isHotSearchExist(channel), "热门搜索列表");
		
		//6.3 租房  - sug
		LJCommonSearchTasks.setKeyWord(sug);
		Platform.sleep(5);
		logCompare(true, LJCommonSearchTasks.isSearchSugExist(), "搜索sug");
		
		//6.4 租房  - 搜索结果跳转				
		LJCommonSearchTasks.setKeyWord(keyWord);
		LJCommonSearchTasks.clickSearchIcon();
		
		Platform.sleep(5);
		
		WindowManagement.switchToNewWindow();
		
		logCompare(curUrl+ channel + "/rs" + keyWord, URLDecoder.decode(Browser.getCurrentUrl(), "UTF-8"), "租房搜索跳转");
		
		Platform.sleep(3);
		
		//6.5  租房  - 搜索历史
		LJCommonSearchTasks.setFocusOnSearchBoxField();
		Platform.sleep(5);
		logCompare(true, LJCommonSearchTasks.isSearchHistoryExist(channel), "搜索历史列表");	
		
		
		
	}
}
