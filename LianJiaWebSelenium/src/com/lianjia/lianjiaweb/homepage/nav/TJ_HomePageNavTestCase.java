package com.lianjia.lianjiaweb.homepage.nav;

import tasks.web.homepage.LJHomeHeaderTasks;
import appobjects.web.Browser;

import com.lianjia.StringFetch;
import com.lianjia.lianjiaweb.homepage.AbstractHomePageCategory;


public class TJ_HomePageNavTestCase extends AbstractHomePageCategory{
	@Override
	public void testMain() throws Throwable {
		//Logo
		logTestPlan("首页logo.");
		logCompare(true, LJHomeHeaderTasks.isLJLogoExist(), "链家网首页展示Logo");
		
		logTestPlan("城市切换 - 天津站");
		String cityName = StringFetch.getString("L_TIANJIN");
		
		LJHomeHeaderTasks.switchToCity(cityName);
		assertEquals("切换至    天津站", cityName, LJHomeHeaderTasks.getCurCity());

		LJHomeHeaderTasks.navTabsVerification(cityName);
		LJHomeHeaderTasks.navTabsLinkVerification(cityName,Browser.getCurrentUrl());
	}		
}
