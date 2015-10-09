package appobjects.web.xiaoqu;

import appobjects.web.bases.LJLocators;

import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class XiaoQuDetailsTabObject extends SeleniumTestObject {	
	public XiaoQuDetailsTabObject(String sLocator){
		super(sLocator);
	}	
	
	/*
	 * 小区详情页
	 */
	//小区概况
	public XiaoQuDetailsTabObject getTab_XiaoQuGaiKuang(){
		return new XiaoQuDetailsTabObject(LJLocators.XIAOQUGAIKUANG);
	}
	
	//二手房
	public XiaoQuDetailsTabObject getTab_ErShouFang(){
		return new XiaoQuDetailsTabObject(LJLocators.XIAOQUERSHOUFANG);
	}
	
	//成交价格
	public XiaoQuDetailsTabObject getTab_ChengJiaoJiaGe(){
		return new XiaoQuDetailsTabObject(LJLocators.CHENGJIAOJIAGE);
	}
	
	//小区成交记录
	public XiaoQuDetailsTabObject getTab_XiaoQuChengJiaoJiLu(){
		return new XiaoQuDetailsTabObject(LJLocators.CHENGJIAOJILU);
	}
	
	//优秀经纪人
	public XiaoQuDetailsTabObject getTab_YouXiuJingJiRen(){
		return new XiaoQuDetailsTabObject(LJLocators.YOUXIUJINGJIREN);
	}
	
	//地图配套
	public XiaoQuDetailsTabObject getTab_DiTuPeiTao(){
		return new XiaoQuDetailsTabObject(LJLocators.DITUPEITAO);
	}
	
	//附近小区
	public XiaoQuDetailsTabObject getTab_FuJinXiaoQu(){
		return new XiaoQuDetailsTabObject(LJLocators.FUJINXIAOQU);
	}
	
	
	public XiaoQuDetailsTabObject getCurrentTab(){
		return new XiaoQuDetailsTabObject(LJLocators.TAB_CURSELECTED);
	}	
	
	public String getTabTitle(){
		return getCurrentTab().getText();
	}
}
