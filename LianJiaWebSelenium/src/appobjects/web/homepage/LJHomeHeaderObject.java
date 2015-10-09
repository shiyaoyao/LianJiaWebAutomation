package appobjects.web.homepage;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import appobjects.web.commons.LJContainer;

public class LJHomeHeaderObject extends LJContainer{
	protected static final Logger log = Logger.getLogger(LJHomeHeaderObject.class);
	
	public LJHomeHeaderObject(String sLocator){
		super(sLocator);
	}
	
	public enum City {
		北京("110000"), 
		天津("120000"), 
		上海("310000"), 
		成都("510100"), 
		南京("320100"), 
		杭州("330100"), 
		青岛("370200"),
		大连("210200"),
		苏州("320500"),
		厦门("350200"),
		武汉("420100"),
		深圳("440300"),
		长沙("430100"),
		重庆("500000"),
		西安("610100");
		
		private String cityNum;

		private City(String cityNum) {
		     this.cityNum = cityNum;
		}
		
		public String getCityNum(){
			return cityNum;
		}
	}
	
	public WebElement getLJLogo(){
    	return this.getWebElement().findElement(By.className("logo"));
	}
	
	public WebElement getCitySwitch(){
		return this.getWebElement().findElement(By.className("exchange"));
	}
	

	//北京真实在售二手房数量
	public WebElement getCityAllSell(){
		return this.getWebElement().findElement(By.className("msg")).findElement(By.tagName("a"));
	}
	
	//购房安心-亿元保障
	public WebElement getGuarantee(){
		return this.getWebElement().findElement(By.className("map-search"));
	}
	
	//地图找房
	public WebElement getDiTu(){
		return this.getWebElement().findElement(By.className("ditu"));
	}
}
