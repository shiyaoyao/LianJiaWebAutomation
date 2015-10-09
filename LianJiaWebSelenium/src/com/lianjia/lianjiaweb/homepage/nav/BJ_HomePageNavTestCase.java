package com.lianjia.lianjiaweb.homepage.nav;

import tasks.web.commons.LJNavTasks;
import tasks.web.homepage.LJHomeHeaderTasks;
import tasks.web.util.WindowManagement;
import appobjects.web.Browser;

import com.lianjia.StringFetch;
import com.lianjia.lianjiaweb.homepage.AbstractHomePageCategory;


public class BJ_HomePageNavTestCase extends AbstractHomePageCategory{
	@Override
	public void testMain() throws Throwable {
		//Logo
		logTestPlan("首页logo.");
		logCompare(true, LJHomeHeaderTasks.isLJLogoExist(), "链家网首页展示Logo");
		
		logTestPlan("城市切换 - 北京站");
		String cityName = StringFetch.getString("L_BEIJING");
		
		LJHomeHeaderTasks.switchToCity(cityName);
		assertEquals("切换至    北京站", cityName, LJHomeHeaderTasks.getCurCity());
		
		String curUrl = Browser.getCurrentUrl();
		
    	//二手房
    	logCompare(curUrl + "ershoufang/", LJNavTasks.gotoErShouFang(), "点击二手房，进入全部二手房列表页！");
    	
    	//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    	
    	//二手房-全部
    	logCompare(curUrl + "ershoufang/", LJNavTasks.gotoErShouFang_All(), "点击二手房-全部，进入全部二手房列表页！");
    	
    	//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    	
    	//二手房-地铁房
    	logCompare(curUrl + "ditiefang/", LJNavTasks.gotoDiTieFang(), "点击二手房-地铁房，进入地铁房列表页！");
    	
    	//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    	
    	//二手房-优质学区
    	logCompare(curUrl + "xuequfang/", LJNavTasks.gotoYouZhiXueQu(), "点击二手房-优质学区，进入学区房列表页！");
    	
    	//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");    		
    	
    	//二手房-成交房源
    	logCompare(curUrl + "chengjiao/", LJNavTasks.gotoChengJiao(), "点击二手房-成交房源，进入成交房源列表页！");
    	
    	//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    		    	
    	//新房
    	logCompare(curUrl + "xinfang/", LJNavTasks.gotoXinFang(), "点击新房，进入新房列表页！");
    	
    	//返回首页
		Browser.loadUrl(curUrl);
    	logCompare(curUrl, Browser.getCurrentUrl(), "返回城市站首页！");
    		    	
    	//租房
    	logCompare("http://www.zufangzi.com/", LJNavTasks.gotoZuFang(), "点击租房，进入丁丁租房首页！");
		WindowManagement.closeWin();
		logCompare(curUrl, Browser.getCurrentUrl(), "关闭丁丁租房推广页面，返回城市站首页！");
    	
    	//海外
		logCompare("http://us.lianjia.com/", LJNavTasks.gotoOverSea(), "点击海外，进入海外列表页！");
		
		//返回首页
		Browser.loadUrl(curUrl);
    	logCompare(curUrl, Browser.getCurrentUrl(), "返回城市站首页！");
    	
    	//小区
    	logCompare(curUrl + "xiaoqu/", LJNavTasks.gotoXiaoQu(), "点击小区，进入小区列表页！");
		
		//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！"); 
    	
    	//经纪人
    	logCompare(curUrl + "jingjiren/", LJNavTasks.gotoAgent(), "点击经纪人，进入经纪人列表页！");
		
		//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    	
    	//锦囊
    	logCompare(curUrl + "baike/", LJNavTasks.gotoJinNang(), "点击锦囊，进入百科频道！");
		
		//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    	
    	//问答
    	logCompare(curUrl + "ask/", LJNavTasks.gotoAsk(), "点击锦囊-问答，进入问答频道！");
		
		//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHomeByOldLogo(), "点击logo，返回城市站首页！");

    	//百科
    	logCompare(curUrl + "baike/", LJNavTasks.gotoBaiKe(), "点击锦囊-百科，进入百科列表页！");
    			
    	//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    	
    	//房价
    	logCompare(curUrl + "fangjia/", LJNavTasks.gotoFangJia(), "点击房价，进入房价频道！");
		
    	//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");	
    	
    	//理财
    	logCompare("https://licai.lianjia.com/", LJNavTasks.gotoLiCai(), "点击理财，进入理财频道！");
    	
    	//返回首页
    	WindowManagement.closeWin();
    	logCompare(curUrl, Browser.getCurrentUrl(), "关闭理财页面，返回城市站首页！");
    	
    	//掌上链家
    	logCompare("http://www.lianjia.com/client/", LJNavTasks.gotoOnHand(), "点击掌上链家，进入掌上链家推广页！");    	
    	
    	//返回首页
    	WindowManagement.closeWin();
    	logCompare(curUrl, Browser.getCurrentUrl(), "关闭掌上链家，返回城市站首页！");
    	
    	//卖房
    	logCompare(curUrl + "yezhu/", LJNavTasks.gotoMaiFang(), "点击卖房，进入卖房频道！");  
    	
    	//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");		
	}		
}
