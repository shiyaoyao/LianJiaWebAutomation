package com.lianjia.lianjiaweb.homepage;

import tasks.web.homepage.LJHomeHeaderTasks;

import com.lianjia.StringFetch;
import com.lianjia.automation.core.Platform;

public class CityExchangeTestCase extends AbstractHomePageCategory {
	@Override
	public void testMain() throws Throwable {
		logTestPlan("城市切换 - 北京站");
		String cityName = StringFetch.getString("L_BEIJING");		
		Platform.sleep(1);
		
		LJHomeHeaderTasks.switchToCity(cityName);
		assertEquals("切换至 " + cityName, cityName, LJHomeHeaderTasks.getCurCity());
		
		Platform.sleep(1);
		
		logTestPlan("城市切换 - 天津站");
		cityName = StringFetch.getString("L_TIANJIN");		
		
		LJHomeHeaderTasks.switchToCity(cityName);
		assertEquals("切换至 " + cityName, cityName, LJHomeHeaderTasks.getCurCity());
		
		Platform.sleep(1);
		
		logTestPlan("城市切换 - 杭州站");
		cityName = StringFetch.getString("L_HANGZHOU");		
		
		LJHomeHeaderTasks.switchToCity(cityName);
		assertEquals("切换至 " + cityName, cityName, LJHomeHeaderTasks.getCurCity());
		
		Platform.sleep(1);
		
		logTestPlan("城市切换 - 武汉站");
		cityName = StringFetch.getString("L_WUHAN");		
		
		LJHomeHeaderTasks.switchToCity(cityName);
		assertEquals("切换至 " + cityName, cityName, LJHomeHeaderTasks.getCurCity());				
	}
}
