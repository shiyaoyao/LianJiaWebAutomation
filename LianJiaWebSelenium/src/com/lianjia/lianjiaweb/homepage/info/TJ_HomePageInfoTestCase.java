package com.lianjia.lianjiaweb.homepage.info;

import tasks.web.homepage.LJHomeHeaderTasks;
import appobjects.web.Browser;

import com.lianjia.StringFetch;
import com.lianjia.lianjiaweb.homepage.AbstractHomePageCategory;


public class TJ_HomePageInfoTestCase  extends AbstractHomePageCategory {
	@Override
	public void testMain() throws Throwable {
		logTestPlan("城市切换 - 天津站");
		String cityName = StringFetch.getString("L_TIANJIN");
		
		LJHomeHeaderTasks.switchToCity(cityName);
		assertEquals("切换至 " + cityName, cityName, LJHomeHeaderTasks.getCurCity());
		
		String curUrl = Browser.getCurrentUrl();
		
		//真实在售二手房套数
		int allSell = LJHomeHeaderTasks.getCityAllSell();
		logCompare(true, allSell > 30000, cityName + "真实在售二手房：" + allSell + " 套");
		
		//购房安心-亿元保单入口
		LJHomeHeaderTasks.guaranteeVer(cityName, curUrl);
		
		//地图找房
		LJHomeHeaderTasks.dituVer(cityName, curUrl);		
	}	
}
