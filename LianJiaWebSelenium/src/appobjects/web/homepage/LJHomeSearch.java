package appobjects.web.homepage;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import appobjects.web.commons.LJContainer;

public class LJHomeSearch extends LJContainer{
	protected static final Logger log = Logger.getLogger(LJHomeSearch.class);
	
	public LJHomeSearch(String sLocator) {
		super(sLocator);
	}
	
	//搜索输入框
	public WebElement getSearchBoxField(){
		return this.getWebElement().findElement(By.id("keyword-box"));
	}
	
	//搜索icon
	public WebElement getSearchBtn(){
		return this.getWebElement().findElement(By.cssSelector("input[type='submit']"));
	}		
}
