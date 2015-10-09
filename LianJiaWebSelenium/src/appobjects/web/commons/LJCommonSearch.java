package appobjects.web.commons;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LJCommonSearch extends LJContainer{
	protected static final Logger log = Logger.getLogger(LJCommonSearch.class);
	
	public LJCommonSearch(String sLocator) {
		super(sLocator);
	}
	
	public WebElement getCheckField(){
		return this.getWebElement().findElement(By.className("search-tab")).findElement(By.className("check"));
	}
	
	public WebElement getSearchBoxField(){
		return this.getWebElement().findElement(By.id("keyword-box"));
	}
	
	public WebElement getSearchBtn(){
		return this.getWebElement().findElement(By.cssSelector("button[type='submit']"));
	}		
}
