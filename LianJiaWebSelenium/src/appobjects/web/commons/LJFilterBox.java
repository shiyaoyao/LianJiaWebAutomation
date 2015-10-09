package appobjects.web.commons;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class LJFilterBox extends SeleniumTestObject {
	public LJFilterBox(String sLocator){
		super(sLocator);
	}
	
	public static LJContainer getLJFilterOptions(){
		return new LJContainer("//*[@id='filter-options']");
	}
	
	public static LJContainer getLJSortPanel(){
		return new LJContainer("//*[@id='sort-panel']");
	}
	
	public static LJContainer getLJSortBar(){
		return new LJContainer("//*[@id='sort-bar']");
	}
	
	public static List<WebElement> getAllFilterOptions(){
		LJContainer ljFilterOptions = getLJFilterOptions();
    	    	
    	List<WebElement> options = ljFilterOptions.getChildrenWithMatch(By.xpath(".//dt"));
    	return options;
	}	
}
