package com.lianjia.lianjiaweb.homepage.nav;

import tasks.web.homepage.LJHomeHeaderTasks;
import appobjects.web.Browser;

import com.lianjia.StringFetch;
import com.lianjia.lianjiaweb.homepage.AbstractHomePageCategory;


public class WH_HomePageNavTestCase extends AbstractHomePageCategory{
	@Override
	public void testMain() throws Throwable {
		//Logo
		logTestPlan("首页logo.");
		logCompare(true, LJHomeHeaderTasks.isLJLogoExist(), "链家网首页展示Logo");
		
		logTestPlan("城市切换 - 武汉站");
		String cityName = StringFetch.getString("L_WUHAN");
		
		LJHomeHeaderTasks.switchToCity(cityName);
		assertEquals("切换至    武汉站", cityName, LJHomeHeaderTasks.getCurCity());

		LJHomeHeaderTasks.navTabsVerification(cityName);
		LJHomeHeaderTasks.navTabsLinkVerification(cityName,Browser.getCurrentUrl());
		
	}		
}
