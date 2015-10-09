package com.lianjia.lianjiaweb.homepage.search;

import java.net.URLDecoder;

import tasks.web.commons.LJNavTasks;
import tasks.web.homepage.LJHomeHeaderTasks;
import tasks.web.homepage.LJHomeSearchTasks;
import appobjects.web.Browser;
import appobjects.web.bases.LJLocators;

import com.lianjia.StringFetch;
import com.lianjia.automation.core.Platform;
import com.lianjia.lianjiaweb.homepage.AbstractHomePageCategory;


public class XM_HomePageSearchTestCase extends AbstractHomePageCategory{
	@Override
	public void testMain() throws Throwable {
		logTestPlan("厦门站城市首页搜索功能.");
		
		String cityName = StringFetch.getString("L_XIAMEN");		
		LJHomeHeaderTasks.switchToCity(cityName);		
		assertEquals("切换至    " + cityName, cityName, LJHomeHeaderTasks.getCurCity());
		
		String curUrl = Browser.getCurrentUrl();
					
		//1. 找二手房
		String tabSel = StringFetch.getString("SEARCH_ERSHOUFANG");
		String channel = LJLocators.SEARCH_ERSHOUFANG_CHANNEL;
		String sug = StringFetch.getString("XM_ERSHOUFANG_SUG_KEYWORD");
		String keyWord = StringFetch.getString("XM_ERSHOUFANG_SEARCH_KEYWORD");		
		
		assertEquals("选择 " + channel, true, LJHomeSearchTasks.selectTabByChannel(tabSel));
		Platform.sleep(5);		
		
		//1.1  找二手房 - tips		
		logCompare(true, LJHomeSearchTasks.getSearchBoxTips().length()>0, LJHomeSearchTasks.getSearchBoxTips());
		
		//1.2  找二手房 - 热门搜索
		LJHomeSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJHomeSearchTasks.isHotSearchExist(channel), "热门搜索列表");
		
		//1.3  找二手房 - sug
		LJHomeSearchTasks.setKeyWord(sug);
		Platform.sleep(5);
		logCompare(true, LJHomeSearchTasks.isSearchSugExist(), "搜索sug");
		
		//1.4  找二手房 - 搜索结果跳转
		LJHomeSearchTasks.setKeyWord(keyWord);
		LJHomeSearchTasks.clickSearchIcon();
		
		Platform.sleep(5);
		
		logCompare(curUrl+ channel + "/rs" + keyWord, URLDecoder.decode(Browser.getCurrentUrl(), "UTF-8"), "二手房搜索跳转");
		
		assertEquals("返回城市首页", curUrl, LJNavTasks.gotoHome());
		
		//1.5  找二手房 - 搜索历史		
		LJHomeSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJHomeSearchTasks.isSearchHistoryExist(channel), "搜索历史列表");
		
		/**
		//2. 找新房
		tabSel = StringFetch.getString("SEARCH_XINFANG");
		channel = LJLocators.SEARCH_XINFANG_CHANNEL;
		sug = StringFetch.getString("XM_XINFANG_SUG_KEYWORD");
		keyWord = StringFetch.getString("XM_XINFANG_SEARCH_KEYWORD"); 
		
		assertEquals("选择 " + tabSel, true, LJHomeSearchTasks.selectTabByChannel(tabSel));
		Platform. sleep(5);
		
		//2.1 找新房 - tips
		logCompare(true, LJHomeSearchTasks.getSearchBoxTips().length()>0, LJHomeSearchTasks.getSearchBoxTips());
		
		//2.2  找新房  - 热门搜索
		LJHomeSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJHomeSearchTasks.isHotSearchExist(channel), "热门搜索列表");
		
		//2.3  找新房 - sug
		LJHomeSearchTasks.setKeyWord(sug);
		Platform.sleep(5);
		logCompare(true, LJHomeSearchTasks.isSearchSugExist(), "搜索sug");
		
		//2.4  找新房 - 搜索结果跳转	
		LJHomeSearchTasks.setKeyWord(keyWord);
		LJHomeSearchTasks.clickSearchIcon();
		
		Platform.sleep(5);
		
		logCompare(curUrl+ channel + "/rs" + keyWord, URLDecoder.decode(Browser.getCurrentUrl(), "UTF-8"), "新房搜索跳转");
		
		Browser.loadUrl(curUrl);
		
		//2.5  找新房 - 搜索历史
		assertEquals("选择 " + tabSel, true, LJHomeSearchTasks.selectTabByChannel(tabSel));
		LJHomeSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJHomeSearchTasks.isSearchHistoryExist(channel), "搜索历史列表");	
		*/
		//3. 出租房
		tabSel = StringFetch.getString("SEARCH_CHUZUFANG");
		channel = LJLocators.SEARCH_CHUZUFANG_CHANNEL;
		sug = StringFetch.getString("XM_ZUFANG_SUG_KEYWORD");
		keyWord = StringFetch.getString("XM_ZUFANG_SEARCH_KEYWORD");
		
		assertEquals("选择 " + tabSel, true, LJHomeSearchTasks.selectTabByChannel(tabSel));
		Platform.sleep(5);
		
		//3.1 出租房  - tips
		logCompare(true, LJHomeSearchTasks.getSearchBoxTips().length()>0, LJHomeSearchTasks.getSearchBoxTips());
		
		//3.2  出租房  - 热门搜索
		LJHomeSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJHomeSearchTasks.isHotSearchExist(channel), "热门搜索列表");
		
		//3.3  出租房 - sug
		LJHomeSearchTasks.setKeyWord(sug);
		Platform.sleep(5);
		logCompare(true, LJHomeSearchTasks.isSearchSugExist(), "搜索sug");
		
		//3.4  出租房 - 搜索结果跳转	
		LJHomeSearchTasks.setKeyWord(keyWord);
		LJHomeSearchTasks.clickSearchIcon();
		
		Platform.sleep(5);
		
		logCompare(curUrl+ channel + "/rs" + keyWord, URLDecoder.decode(Browser.getCurrentUrl(), "UTF-8"), "租房搜索跳转");
		
		Browser.loadUrl(curUrl);
		
		//3.5  出租房 - 搜索历史
		assertEquals("选择 " + tabSel, true, LJHomeSearchTasks.selectTabByChannel(tabSel));
		LJHomeSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJHomeSearchTasks.isSearchHistoryExist(channel), "搜索历史列表");	
		/**	
		//4. 找小区
		tabSel = StringFetch.getString("SEARCH_XIAOQU");
		channel = LJLocators.SEARCH_XIAOQU_CHANNEL;
		sug = StringFetch.getString("XM_XIAOQU_SUG_KEYWORD");
		keyWord = StringFetch.getString("XM_XIAOQU_SEARCH_KEYWORD");		
		
		assertEquals("选择 " + tabSel, true, LJHomeSearchTasks.selectTabByChannel(tabSel));
		Platform.sleep(5);
		
		//4.1 找小区  - tips
		logCompare(true, LJHomeSearchTasks.getSearchBoxTips().length()>0, LJHomeSearchTasks.getSearchBoxTips());
		
		//4.2  找小区  - 热门搜索
		LJHomeSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJHomeSearchTasks.isHotSearchExist(channel), "热门搜索列表");
		
		//4.3  找小区 - sug
		LJHomeSearchTasks.setKeyWord(sug);
		Platform.sleep(5);
		logCompare(true, LJHomeSearchTasks.isSearchSugExist(), "搜索sug");
		
		//4.4  找小区 - 搜索结果跳转	
		LJHomeSearchTasks.setKeyWord(keyWord);
		LJHomeSearchTasks.clickSearchIcon();
		
		Platform.sleep(5);
		
		logCompare(curUrl+ channel + "/rs" + keyWord, URLDecoder.decode(Browser.getCurrentUrl(), "UTF-8"), "小区搜索跳转");
		
		Browser.loadUrl(curUrl);
		
		//4.5  找小区 - 搜索历史
		assertEquals("选择 " + tabSel, true, LJHomeSearchTasks.selectTabByChannel(tabSel));
		LJHomeSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJHomeSearchTasks.isSearchHistoryExist(channel), "搜索历史列表");	
						
		
		//5. 查房价
		tabSel = StringFetch.getString("SEARCH_FANGJIA");
		channel = LJLocators.SEARCH_FANGJIA_CHANNEL;
		sug = StringFetch.getString("XM_FANGJIA_SUG_KEYWORD");
		keyWord = StringFetch.getString("XM_FANGJIA_SEARCH_KEYWORD");
		
		assertEquals("选择 " + tabSel, true, LJHomeSearchTasks.selectTabByChannel(tabSel));
		Platform.sleep(5);
		
		//5.1 查房价   - tips
		logCompare(true, LJHomeSearchTasks.getSearchBoxTips().length()>0, LJHomeSearchTasks.getSearchBoxTips());
		
		//5.2  查房价  - 热门搜索
		LJHomeSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJHomeSearchTasks.isHotSearchExist(channel), "热门搜索列表");
		
		//5.3  查房价 - sug
		LJHomeSearchTasks.setKeyWord(sug);
		Platform.sleep(5);
		logCompare(true, LJHomeSearchTasks.isSearchSugExist(), "搜索sug");
		
		//5.4  查房价 - 搜索结果跳转	
		LJHomeSearchTasks.setKeyWord(keyWord);
		LJHomeSearchTasks.clickSearchIcon();
		
		Platform.sleep(5);
		
		logCompare(curUrl+ channel + "/rs" + keyWord, URLDecoder.decode(Browser.getCurrentUrl(), "UTF-8"), "房价搜索跳转");
		
		Browser.loadUrl(curUrl);
		
		//5.5  查房价 - 搜索历史
		assertEquals("选择 " + tabSel, true, LJHomeSearchTasks.selectTabByChannel(tabSel));
		LJHomeSearchTasks.setFocusOnSearchBoxField();
		logCompare(true, LJHomeSearchTasks.isSearchHistoryExist(channel), "搜索历史列表");			
	*/
	}		
}
