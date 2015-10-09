package appobjects.web.dialogs;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import appobjects.web.commons.LJDialog;

public class CityExchangeDialog extends LJDialog{
	public CityExchangeDialog() {
		super("className=city-change");
	}
	
	public CityExchangeDialog(String dialogLocator) {
		super(dialogLocator);
	}

	public List<WebElement> getAllCities() {
		WebElement cityPanel = (this.getWebElement()).findElement(By.tagName("ul"));
		List<WebElement> allCities = cityPanel.findElements(By.tagName("li"));
		return allCities;
	}
	
	public void switchtoCity(int index){
		WebElement city = this.getAllCities().get(index);
		city.click();
	}
	
	public void switchtoCity(String cityName){
		WebElement cityPanel = (this.getWebElement()).findElement(By.tagName("ul"));
		WebElement city = cityPanel.findElement(By.linkText(cityName));
		city.click();
	}
	
}
