package appobjects.web.authentication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

public class LJLoginObjects extends SeleniumTestObject {	
	private static final LJLoginObjects ljLoginObjects = new LJLoginObjects("id=dialog");
	
	public LJLoginObjects(String sLocator){
		super(sLocator);
	}		

	public static WebElement getTextField_loginUsername(){
		WebElement loginUsername = ljLoginObjects.getWebElement().findElement(By.className("users"));
    	return loginUsername; 
	}
	
	public static WebElement getTextField_loginPassword(){
		WebElement loginPassword = ljLoginObjects.getWebElement().findElement(By.className("password"));
    	return loginPassword; 
	}	

	public static WebElement getButton_LoginButton(){
		WebElement loginButton = ljLoginObjects.getWebElement().findElement(By.className("login-user-btn"));
    	return loginButton; 
	}
	
	public static WebElement getCheckBox_LoginRememberMe(){
		WebElement loginRememberMe = ljLoginObjects.getWebElement().findElement(By.name("remember"));
    	return loginRememberMe; 
	}

}
