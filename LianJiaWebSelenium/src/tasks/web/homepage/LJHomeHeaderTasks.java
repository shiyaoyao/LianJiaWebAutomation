package tasks.web.homepage;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import tasks.base.BaseTask;
import tasks.web.commons.LJNavTasks;
import tasks.web.util.WindowManagement;
import appobjects.web.Browser;
import appobjects.web.bases.LJLocators;
import appobjects.web.dialogs.CityExchangeDialog;
import appobjects.web.homepage.LJHomeHeaderObject;
import appobjects.web.homepage.LJHomeHeaderObject.City;

import com.lianjia.StringFetch;
import com.lianjia.automation.core.Platform;

public class LJHomeHeaderTasks extends BaseTask {
	private static final Logger log = Logger.getLogger(LJHomeHeaderTasks.class);
	private static LJHomeHeaderObject ljHomeObject = new LJHomeHeaderObject(LJLocators.LJ_HOME_HEADER);
	
	public static String getCityNum(String cityName){
		City city =City.valueOf(cityName);
		
		return city.getCityNum();
	}
	
	public static boolean isLJLogoExist(){
		return ljHomeObject.getLJLogo().isDisplayed();
	}	
	
	public boolean isLJCitySwitchExist(){
		return ljHomeObject.getCitySwitch().isDisplayed();
	}	

	public static boolean switchToCity(String cityName){
		WebElement btn = ljHomeObject.getCitySwitch();
		
		log.info("btn.isDisplayed() = " + btn.isDisplayed());
		
		if(btn.isDisplayed()){
			btn.click();
			
			Platform.sleep(1);
			
			CityExchangeDialog dialog = new CityExchangeDialog();			
			if(dialog.exists()){
				dialog.switchtoCity(cityName);
				Platform.sleep(3);
				if(getCurCity().equals(cityName)){
					log.info("城市成功切换至：" + cityName);
					return true;
				}
				else{
					log.info("城市未成功切换至：" + cityName);
					return false;				
				}					
			} else {
				log.info("城市切换对话框未弹出。");
				return false;
			}			
		} else{
			log.info("未定位到城市切换button。");
			return false;
		}
	}
	
	public static String getCurCity(){
		return ljHomeObject.getCitySwitch().getText();
	}
	
	public static void navTabsVerification(String cityName){
    	//二手房
    	logCompare(true, LJNavTasks.isErShouFangExist(), "开启二手房导航入口！");
    	    	
    	//二手房-全部
    	if(cityName.equals(StringFetch.getString("L_XIAMEN")))
    		logCompare(false, LJNavTasks.isErShouFang_AllExist(), "不开启二手房-全部导航入口！");
    	else
    		logCompare(true, LJNavTasks.isErShouFang_AllExist(), "开启二手房-全部导航入口！");
    	
    	//二手房-地铁房
    	if(cityName.equals(StringFetch.getString("L_XIAMEN"))
    			|| cityName.equals(StringFetch.getString("L_QINGDAO")))
    		logCompare(false, LJNavTasks.isDiTieFangExist(), "不开启二手房-地铁房导航入口！");
    	else
    		logCompare(true, LJNavTasks.isDiTieFangExist(), "开启二手房-地铁房导航入口！");
    	
    	//二手房-优质学区
    	if(cityName.equals(StringFetch.getString("L_CHENGDU"))
    			|| cityName.equals(StringFetch.getString("L_SUZHOU"))
    			|| cityName.equals(StringFetch.getString("L_XIAMEN"))
    			|| cityName.equals(StringFetch.getString("L_WUHAN"))
    			|| cityName.equals(StringFetch.getString("L_CHANGSHA")))
    		logCompare(false, LJNavTasks.isYouZhiXueQuExist(), "不开启二手房-优质学区导航入口！");
    	else
    		logCompare(true, LJNavTasks.isYouZhiXueQuExist(), "开启二手房-优质学区导航入口！");
    	
    	//二手房-成交房源
    	if(cityName.equals(StringFetch.getString("L_XIAMEN"))
    			|| cityName.equals(StringFetch.getString("L_WUHAN"))
    			|| cityName.equals(StringFetch.getString("L_CHANGSHA")))
    		logCompare(false, LJNavTasks.isChengJiaoExist(), "不开启二手房-成交房源导航入口！");
    	else
    		logCompare(true, LJNavTasks.isChengJiaoExist(), "开启二手房-成交房源导航入口！");
    	    	
    	//新房
    	if(cityName.equals(StringFetch.getString("L_HANGZHOU")) 
    			|| cityName.equals(StringFetch.getString("L_SUZHOU"))
    			|| cityName.equals(StringFetch.getString("L_XIAMEN"))){
    		
    		logCompare(false, LJNavTasks.isXinFangExist(), "不开启新房导航入口!");
    	} else{
    		logCompare(true, LJNavTasks.isXinFangExist(), "开启新房导航入口！");
    	}
    	
    	//租房
    	if(cityName.equals(StringFetch.getString("L_SHANGHAI"))){
    		
    		logCompare(false, LJNavTasks.isZuFangExist(), "不开启租房导航入口！");
    	} else{
    		logCompare(true, LJNavTasks.isZuFangExist(), "开启租房导航入口！");
    	}
    	
    	//商铺, 海外, 百科
    	if(cityName.equals(StringFetch.getString("L_BEIJING"))
    			|| cityName.equals(StringFetch.getString("L_DALIAN"))
    			|| cityName.equals(StringFetch.getString("L_NANJING"))){
    		logCompare(true, LJNavTasks.isShangPuExist(), "开启商铺导航入口！");
    		logCompare(true, LJNavTasks.isOverSeaExist(), "开启海外导航入口！");
    		logCompare(true, LJNavTasks.isBaiKeExist(), "开启百科导航入口！");
    	} else{
    		logCompare(false, LJNavTasks.isShangPuExist(), "不开启商铺导航入口！");
    		logCompare(false, LJNavTasks.isOverSeaExist(), "不开启海外导航入口！");
    		logCompare(false, LJNavTasks.isBaiKeExist(), "不开启百科导航入口！");
    	}
    	
    	//小区
    	if(cityName.equals(StringFetch.getString("L_SUZHOU"))
    			|| cityName.equals(StringFetch.getString("L_XIAMEN"))
    			|| cityName.equals(StringFetch.getString("L_WUHAN"))
    			|| cityName.equals(StringFetch.getString("L_CHANGSHA"))){
    		logCompare(false, LJNavTasks.isXiaoQuExist(), "不开启小区导航入口！");
    	} else{
    		logCompare(true, LJNavTasks.isXiaoQuExist(), "开启小区导航入口！");
        }    	
    	
    	//经纪人
    	/**if(cityName.equals(StringFetch.getString("L_XIAMEN"))
    			|| cityName.equals(StringFetch.getString("L_WUHAN"))
    			|| cityName.equals(StringFetch.getString("L_SHANGHAI"))){
    		logCompare(false, LJNavTasks.isAgentExist(), "不开启经纪人导航入口！");
    	} else{
    		logCompare(true, LJNavTasks.isAgentExist(), "开启经纪人导航入口！");
        } */
    	logCompare(true, LJNavTasks.isAgentExist(), "开启经纪人导航入口！");
    	
    	//锦囊，房价，问答
    	if(cityName.equals(StringFetch.getString("L_SUZHOU"))
    			|| cityName.equals(StringFetch.getString("L_XIAMEN"))
    			|| cityName.equals(StringFetch.getString("L_WUHAN"))
    			|| cityName.equals(StringFetch.getString("L_CHANGSHA"))){
    		logCompare(false, LJNavTasks.isJinNangExist(), "不开启锦囊导航入口！");
    		logCompare(false, LJNavTasks.isFangJiaExist(), "不开启房价导航入口！");
    		logCompare(false, LJNavTasks.isAskExist(), "不开启问答导航入口！");
    	} else{
    		logCompare(true, LJNavTasks.isJinNangExist(), "开启锦囊导航入口！");
    		logCompare(true, LJNavTasks.isFangJiaExist(), "开启房价导航入口！");
    		logCompare(true, LJNavTasks.isAskExist(), "开启问答导航入口！");
    	} 
    	
    	//理财
    	logCompare(true, LJNavTasks.isLiCaiExist(), "开启理财导航入口!");
    	
    	//掌上链家
    	logCompare(true, LJNavTasks.isOnHandExist(), "开启掌上链家导航入口!");    	
    	
    	//卖房
    	logCompare(true, LJNavTasks.isMaiFangExist(), "开启卖房导航入口!");    	
    }
	
	public static void navTabsLinkVerification(String cityName, String curUrl){
    	//二手房
    	logCompare(curUrl + "ershoufang/", LJNavTasks.gotoErShouFang(), "点击二手房，进入全部二手房列表页！");
    	
    	//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    	
    	//二手房-全部
    	if(!(cityName.equals(StringFetch.getString("L_XIAMEN")))){
    		logCompare(curUrl + "ershoufang/", LJNavTasks.gotoErShouFang_All(), "点击二手房-全部，进入全部二手房列表页！");
        	
        	//返回首页
        	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    	}
    	
    	//二手房-地铁房
    	if(!(cityName.equals(StringFetch.getString("L_XIAMEN")) 
    			|| cityName.equals(StringFetch.getString("L_QINGDAO"))))  {
    		logCompare(curUrl + "ditiefang/", LJNavTasks.gotoDiTieFang(), "点击二手房-地铁房，进入地铁房列表页！");
        	
        	//返回首页
        	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    	}  
    	
    	//二手房-优质学区
    	if(!(cityName.equals(StringFetch.getString("L_CHENGDU"))
    			|| cityName.equals(StringFetch.getString("L_SUZHOU"))
    			|| cityName.equals(StringFetch.getString("L_XIAMEN"))
    			|| cityName.equals(StringFetch.getString("L_WUHAN"))
    			|| cityName.equals(StringFetch.getString("L_CHANGSHA")))){
    		logCompare(curUrl + "xuequfang/", LJNavTasks.gotoYouZhiXueQu(), "点击二手房-优质学区，进入学区房列表页！");
        	
        	//返回首页
        	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    	}    		
    	
    	//二手房-成交房源
    	if(!(cityName.equals(StringFetch.getString("L_XIAMEN")) 
    			|| cityName.equals(StringFetch.getString("L_WUHAN"))
    			|| cityName.equals(StringFetch.getString("L_CHANGSHA"))))
    		logCompare(curUrl + "chengjiao/", LJNavTasks.gotoChengJiao(), "点击二手房-成交房源，进入成交房源列表页！");
    	
    	//返回首页
    	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    	
    	//新房
    	if(!(cityName.equals(StringFetch.getString("L_HANGZHOU")) 
    			|| cityName.equals(StringFetch.getString("L_SUZHOU"))
    			|| cityName.equals(StringFetch.getString("L_XIAMEN"))
    			|| cityName.equals(StringFetch.getString("L_DALIAN")))) {
    		if(cityName.equals(StringFetch.getString("L_CHANGSHA"))
    				|| cityName.equals(StringFetch.getString("L_CHONGQING"))
    				|| cityName.equals(StringFetch.getString("L_XIAN"))){
    			logCompare(curUrl + "xinfang/", LJNavTasks.gotoXinFang(), "点击新房，进入新房列表页！");
            	
            	//返回首页
    			Browser.loadUrl(curUrl);
            	logCompare(curUrl, Browser.getCurrentUrl(), "返回城市站首页！");
    		} else {
    			logCompare(curUrl + "xinfang/", LJNavTasks.gotoXinFang(), "点击新房，进入新房列表页！");
            	
    			//返回首页
    			Browser.loadUrl(curUrl);
            	logCompare(curUrl, Browser.getCurrentUrl(), "返回城市站首页！");
    		}
    	}
    		
    	
    	//租房
    	if(!cityName.equals(StringFetch.getString("L_SHANGHAI"))){
    		if(cityName.equals(StringFetch.getString("L_BEIJING"))){
    			logCompare("http://www.zufangzi.com/", LJNavTasks.gotoZuFang(), "点击租房，进入丁丁租房首页！");
    			 WindowManagement.closeWin();
    			logCompare(curUrl, Browser.getCurrentUrl(), "关闭丁丁租房推广页面，返回城市站首页！");
    		} else {
    			logCompare(curUrl + "zufang/", LJNavTasks.gotoZuFang(), "点击租房，进入租房列表页！");
    			logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    		}
    	}
    	    	
    	//商铺, 海外, 百科
    	if(cityName.equals(StringFetch.getString("L_BEIJING"))){
    		logCompare(curUrl + "shangpuchuzu/", LJNavTasks.gotoShangPu(), "点击商铺，进入商铺列表页！");
    		
    		//返回首页
        	logCompare(curUrl, LJNavTasks.gotoHomeByOldLogo(), "点击logo，返回城市站首页！");
        	
    		logCompare("http://www.lianjia.com/overseas/", LJNavTasks.gotoOverSea(), "点击海外，进入海外列表页！");
    		
    		//返回首页
        	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
        	
    		logCompare(curUrl + "baike/", LJNavTasks.gotoBaiKe(), "点击百科，进入百科列表页！");
    		
    		//返回首页
        	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    	}
    	
    	//小区
    	if(!(cityName.equals(StringFetch.getString("L_SUZHOU"))
    			|| cityName.equals(StringFetch.getString("L_XIAMEN"))
    			|| cityName.equals(StringFetch.getString("L_WUHAN"))
    			|| cityName.equals(StringFetch.getString("L_CHANGSHA")))){
    		logCompare(curUrl + "xiaoqu/", LJNavTasks.gotoXiaoQu(), "点击小区，进入小区列表页！");
    		
    		//返回首页
        	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
        } 
    	
    	//经纪人
    	if(!(cityName.equals(StringFetch.getString("L_XIAMEN"))
    			|| cityName.equals(StringFetch.getString("L_WUHAN"))
    			|| cityName.equals(StringFetch.getString("L_SHANGHAI")))){
    		logCompare(curUrl + "jingjiren/", LJNavTasks.gotoAgent(), "点击经纪人，进入经纪人列表页！");
    		
    		//返回首页
        	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
        } 
    	
    	//锦囊，房价，问答
    	if(!(cityName.equals(StringFetch.getString("L_SUZHOU"))
    			|| cityName.equals(StringFetch.getString("L_XIAMEN"))
    			|| cityName.equals(StringFetch.getString("L_WUHAN"))
    			|| cityName.equals(StringFetch.getString("L_CHANGSHA")))){
    		logCompare(curUrl + "fangjia/", LJNavTasks.gotoJinNang(), "点击锦囊，进入房价频道！");
    		
    		//返回首页
        	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
        	
        	logCompare(curUrl + "fangjia/", LJNavTasks.gotoFangJia(), "点击锦囊-房价，进入房价频道！");
    		
        	//返回首页
        	logCompare(curUrl, LJNavTasks.gotoHome(), "点击logo，返回城市站首页！");
    		
    		logCompare(curUrl + "ask/", LJNavTasks.gotoAsk(), "点击锦囊-问答，进入问答频道！");
    		
    		//返回首页
        	logCompare(curUrl, LJNavTasks.gotoHomeByOldLogo(), "点击logo，返回城市站首页！");
    	} 
    	
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
	
	//城市真实在售二手房数量
	public static int getCityAllSell(){
		String allSell = ljHomeObject.getCityAllSell().getText();
		return (Integer.parseInt(allSell)); 
	}
	
	//购房安心-亿元保障
	public static boolean isGuaranteeExist(){
		if(ljHomeObject.getWebElement().findElements(By.className("map-search")).size()>0)
			return true;
		else
			return false;
	}		
	
	//购房安心-亿元保障	
	public static String gotoBaodan(){
		log.info("保单专题");
		ljHomeObject.getGuarantee().click();
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(3);

		try {
			WindowManagement.setBrowserFocus();
		} catch (IOException e) {
			//
		}
		return Browser.getCurrentUrl();
	}
	
	//地图找房
	public static boolean isDiTuExist(){
		if(ljHomeObject.getWebElement().findElements(By.className("ditu")).size()>0)
			return true;
		else
			return false;
	}
	
	//地图找房
	public static String gotoDiTu(){
		log.info("地图找房");
		ljHomeObject.getDiTu().click();
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(3);

		try {
			WindowManagement.setBrowserFocus();
		} catch (IOException e) {
			//
		}
		return Browser.getCurrentUrl();
	}
	
	public static void guaranteeVer(String cityName, String curUrl){
		if(cityName.equals(StringFetch.getString("L_HANGZHOU"))
				|| cityName.equals(StringFetch.getString("L_SUZHOU"))
				|| cityName.equals(StringFetch.getString("L_XIAMEN"))
				|| cityName.equals(StringFetch.getString("L_CHANGSHA"))
				|| cityName.equals(StringFetch.getString("L_SHENZHEN"))){
			logCompare(false, LJHomeHeaderTasks.isGuaranteeExist(), "不开启 购房安心-亿元保障 入口");
		} else {
			logCompare(true, LJHomeHeaderTasks.isGuaranteeExist(), "开启 购房安心-亿元保障 入口");
			logCompare("http://www.lianjia.com/zhuanti/baodan/#" + LJHomeHeaderTasks.getCityNum(cityName), LJHomeHeaderTasks.gotoBaodan(), "点击购房安心 - 亿元保障，进入保单专题！");
			
			//返回首页
	    	WindowManagement.closeWin();
	    	logCompare(curUrl, Browser.getCurrentUrl(), "关闭保单专题，返回城市站首页！");
		}		
	}
	
	public static void dituVer(String cityName, String curUrl){
		if(cityName.equals(StringFetch.getString("L_CHENGDU"))
				|| cityName.equals(StringFetch.getString("L_QINGDAO"))
				|| cityName.equals(StringFetch.getString("L_XIAMEN"))
				|| cityName.equals(StringFetch.getString("L_SHENZHEN"))
				|| cityName.equals(StringFetch.getString("L_CHANGSHA"))
				|| cityName.equals(StringFetch.getString("L_WUHAN"))){
			logCompare(false, LJHomeHeaderTasks.isDiTuExist(), "不开启 地图找房 入口");
		} else {
			logCompare(true, LJHomeHeaderTasks.isDiTuExist(), "开启 地图找房  入口");			
			logCompare(curUrl + "ditu/", LJHomeHeaderTasks.gotoDiTu(), "点击 地图找房，进入地图找房频道！");
			
			//返回首页
	    	WindowManagement.closeWin();
	    	logCompare(curUrl, Browser.getCurrentUrl(), "关闭 地图找房，返回城市站首页！");
		}	
	}
}
