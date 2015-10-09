package appobjects.web.ershoufang;

import appobjects.web.bases.LJLocators;

import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class ErShouFangDetailsTabObject extends SeleniumTestObject {	
	public ErShouFangDetailsTabObject(String sLocator){
		super(sLocator);
	}	
	
	/*
	 * 二手房详情页
	 */
	//房源描述
	public ErShouFangDetailsTabObject getTab_FangYuanMiaoShu(){
		return new ErShouFangDetailsTabObject(LJLocators.FANGYUANMIAOSHU);
	}
	
	//房源图片
	public ErShouFangDetailsTabObject getTab_FangYuanTUPIAN(){
		return new ErShouFangDetailsTabObject(LJLocators.FANGYUANTUPIAN);
	}
	
	//客户看房记录
	public ErShouFangDetailsTabObject getTab_KeHuKanFangJiLu(){
		return new ErShouFangDetailsTabObject(LJLocators.KANFANGJILU);
	}
	
	//小区成交房源
	public ErShouFangDetailsTabObject getTab_XiaoQuChengJiaoFangYuan(){
		return new ErShouFangDetailsTabObject(LJLocators.XIAOQUCHENGJIAOFANGYUAN);
	}
		
	//周边配套
	public ErShouFangDetailsTabObject getTab_ZhouBianPeiTao(){
		return new ErShouFangDetailsTabObject(LJLocators.ZHOUBIANPEITAO);
	}

}
