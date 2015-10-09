package tasks.web.commons;

import java.io.IOException;

import org.apache.log4j.Logger;

import tasks.base.BaseTask;
import tasks.web.util.WindowManagement;
import appobjects.web.Browser;
import appobjects.web.bases.LJLocators;
import appobjects.web.commons.LJNav;

import com.lianjia.automation.core.Platform;

public class LJNavTasks extends BaseTask{
	private static final Logger log = Logger.getLogger(LJNavTasks.class);
	private static LJNav LJNav = new LJNav(LJLocators.LJ_HOME_HEADER);
	
	public static boolean isLJLogoExist(){
		if(LJNav.getLJLogo().exists())		
			return true;
		else
			return false;
	}
	
	public static String gotoHome() {
		log.info("城市首页");
		LJNav.getLJLogo().click();
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		try {
			WindowManagement.setBrowserFocus();
		} catch (IOException e) {
			//
		}
		return Browser.getCurrentUrl();
	}
	
	public static boolean isLJOldLogoExist(){
		if(LJNav.getLJOldLogo().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoHomeByOldLogo() {
		log.info("城市首页");
		LJNav.getLJOldLogo().click();
		
		WindowManagement.switchToNewWindowUpdate();	
		Platform.sleep(1);	
		
		Platform.sleep(20);

		return Browser.getCurrentUrl();
	}
	
	public static boolean isErShouFangExist(){
		if(LJNav.getNavErShouFang().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoErShouFang() {
		log.info("全部二手房");
		LJNav.getNavErShouFang().click();	
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(3);

		return Browser.getCurrentUrl();
	}
	
	public static boolean isErShouFang_AllExist(){
		LJNav.getNavErShouFang().hover(3);
		if(LJNav.getNavAll().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoErShouFang_All() {
		log.info("二手房_全部");

		LJNav.getNavAll().click();	
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);
		
		return Browser.getCurrentUrl();
	}
	
	public static boolean isDiTieFangExist(){
		LJNav.getNavErShouFang().hover(3);
		if(LJNav.getNavDiTieFang().exists())
			return true;
		else
			return false;
	
	}
	
	public static String gotoDiTieFang() {
		log.info("二手房_地铁房");
		LJNav.getNavDiTieFang().click();	
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		return Browser.getCurrentUrl();
	}
	
	public static boolean isYouZhiXueQuExist(){
		LJNav.getNavErShouFang().hover(3);	
		if(LJNav.getNavYouZhiXueQu().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoYouZhiXueQu() {
		log.info("二手房_优质学区");
		LJNav.getNavYouZhiXueQu().click();		
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		return Browser.getCurrentUrl();
	}
	
	public static boolean isChengJiaoExist(){
		LJNav.getNavErShouFang().hover(3);
		if(LJNav.getNavChengJiao().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoChengJiao() {
		log.info("二手房_成交房源");
		LJNav.getNavChengJiao().click();		
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		return Browser.getCurrentUrl();
	}
	
	public static boolean isXinFangExist(){
		if(LJNav.getNavXinFang().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoXinFang(){
		log.info("新房");
		LJNav.getNavXinFang().click();	
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		return Browser.getCurrentUrl();
	}
	
	public static boolean isZuFangExist(){
		if(LJNav.getNavZuFang().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoZuFang(){
		log.info("租房");
		LJNav.getNavZuFang().click();	
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(3);

		return Browser.getCurrentUrl();	
	}	
	
	public static boolean isShangPuExist(){
		if(LJNav.getNavShangPu().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoShangPu(){
		log.info("商铺");
		LJNav.getNavShangPu().click();	
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		return Browser.getCurrentUrl();
	}
	
	public static boolean isOverSeaExist(){
		if(LJNav.getNavOversea().exists())
			return true;
		else
			return false;
	}

	public static String gotoOverSea(){
		log.info("海外");
		LJNav.getNavOversea().click();		
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		return Browser.getCurrentUrl();				
	}
	
	public static boolean isXiaoQuExist(){
		if(LJNav.getNavXiaoQu().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoXiaoQu(){
		log.info("小区");
		LJNav.getNavXiaoQu().click();	
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		return Browser.getCurrentUrl();			
	}
	
	public static boolean isAgentExist(){
		if(LJNav.getNavAgent().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoAgent(){
		log.info("经纪人");
		LJNav.getNavAgent().click();	
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		return Browser.getCurrentUrl();	
	}
	
	public static boolean isJinNangExist(){
		if(LJNav.getNavJinNang().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoJinNang(){
		log.info("锦囊");
		LJNav.getNavJinNang().click();		
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		return Browser.getCurrentUrl();		
	}
	
	public static boolean isFangJiaExist(){
		if(isJinNangExist()){
			LJNav.getNavJinNang().hover(3);
			if(LJNav.getNavFangJia().exists())
				return true;
			else
				return false;
		} else
			return false;
		
	}
	
	public static String gotoFangJia(){
		log.info("房价");
		LJNav.getNavFangJia().click();	
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		return Browser.getCurrentUrl();
	}
	
	public static boolean isAskExist(){
		if(isJinNangExist()){
			LJNav.getNavJinNang().hover(3);
			if(LJNav.getNavAsk().exists())
				return true;
			else
				return false;
		} else
			return false; 		
	}
	
	public static String gotoAsk(){
		log.info("锦囊_问答");
		LJNav.getNavAsk().click();	
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		return Browser.getCurrentUrl();		
	}
	
	public static boolean isBaiKeExist(){
		if(isJinNangExist()){
			LJNav.getNavJinNang().hover(3);
			if(LJNav.getNavBaiKe().exists())
				return true;
			else
				return false;
		} else
			return false;		
	}
	
	public static String gotoBaiKe(){
		log.info("锦囊_百科");
		LJNav.getNavBaiKe().click();
		
		WindowManagement.switchToNewWindow();	
		Platform.sleep(1);

		return Browser.getCurrentUrl();						
	}
	
	public static boolean isLiCaiExist(){
		if(LJNav.getNavLiCai().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoLiCai(){
		log.info("理财");
		LJNav.getNavLiCai().click();
		
		WindowManagement.switchToNewWindow();		
		Platform.sleep(3);

		return Browser.getCurrentUrl();		
	}
	
	public static boolean isOnHandExist(){
		if(LJNav.getNavOnHand().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoOnHand(){
		log.info("掌上链家");
		LJNav.getNavOnHand().click();
		WindowManagement.switchToNewWindow();
		Platform.sleep(5);

		return Browser.getCurrentUrl();		
	}
	
	public static boolean isMaiFangExist(){
		if(LJNav.getNavMaiFang().exists())
			return true;
		else
			return false;
	}
	
	public static String gotoMaiFang(){
		log.info("卖房");
		LJNav.getNavMaiFang().click();
		
		WindowManagement.switchToNewWindow();
		
		Platform.sleep(1);

		return Browser.getCurrentUrl();		
	}
	
	
	public void register(){
		log.info("注册");
		LJNav.getRegister().click();	
	}
	
	public static boolean isautoRegisterExist(){
		if(LJNav.getRegister().exists())
			return true;
		else
			return false;
	}
	
	public void autoRegister(){
		log.info("立即注册");
		LJNav.getRegister().click();	
	}	
	
	public static boolean isLogInExist(){
		if(LJNav.getLogIn().exists())
			return true;
		else
			return false;
	}
	
	public static void logIn(){
		log.info("登录");
		LJNav.getLogIn().click();	
	}
	
	public static boolean isLogOutExist(){
		if(LJNav.getLogOut().exists())
			return true;
		else
			return false;
	}
	
	public static void logOut(){
		log.info("退出");
		LJNav.getLogOut().click();	
	}
	
}
