package appobjects.web.ershoufang;

import appobjects.web.bases.LJLocators;
import appobjects.web.widgets.WebLink;

import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class ErShouFangListTabObject extends SeleniumTestObject {	
	public ErShouFangListTabObject(String sLocator){
		super(sLocator);
	}	
	
	/**
	 * 二手房列表页
	 */
	//全部二手房
	public WebLink getTab_AllErShouFang(){
		return new WebLink(LJLocators.ALL_ERSHOUFANG);
	}
	
	//地铁房
	public WebLink getTab_DiTieFang(){
		return new WebLink(LJLocators.DITIEFANG);
	}
	
	//优质学区
	public WebLink getTab_YouZhiXueQu(){
		return new WebLink(LJLocators.YOUZHIXUEQU);
	}
	
	//成交行情
	public WebLink getTab_ChengJiaoFangYuan(){
		return new WebLink(LJLocators.CHENGJIAOFANGYUAN);
	}
		
	public WebLink getCurrentTab(){
		return new WebLink(LJLocators.TAB_CURSELECTED);
	}	
	
	public String getTabTitle(){
		return getCurrentTab().getText();
	}
}
