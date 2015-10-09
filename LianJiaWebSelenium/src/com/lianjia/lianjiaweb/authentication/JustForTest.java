package com.lianjia.lianjiaweb.authentication;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import tasks.web.commons.LJNavTasks;
import appobjects.web.authentication.LJLoginObjects;

import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.SeleniumCore;

public class JustForTest extends AbstractAuthenticationCategory {
    @Override
	public void testMain() throws Throwable {
    	WebDriver driver = SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
    	//WebElement hello = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div/span/a[2]/span")) ;
    	
    	//WebElement hello = driver.findElement(By.linkText("���ַ�"));    ---Success
    	
    	//WebElement hello = driver.findElement(By.className("welcome"));  ---Success
    	
    	//---Success
    	//WebElement hello = driver.findElement(By.linkText("���뱱��t��վ")); 
    	//WebElement hello = driver.findElement(By.className("chooseBtn")); 
    	
    	//WebElement hello = driver.findElement(By.xpath("//a[contains(@href,'lianjia')]"));
    	
    	//log.warn("******************************** hello = " + hello.getText());
    	//hello.click();
    	
    	
    	//Actions actions = new Actions(driver);
    	//WebElement menuHoverLink = driver.findElement(By.xpath("//span[contains(text(),'�Ҳ��������')]")); 
    	//log.warn("******************************** menuHoverLink = " + menuHoverLink.getText());
    	//actions.moveToElement(menuHoverLink).perform();
    	//menuHoverLink.click();
    	
    	//SeleniumTestObject webObject = new SeleniumTestObject("//span[text()='����']");
    	//SeleniumTestObject webObject = new SeleniumTestObject(("css=div[mod-id='lj-home-header']"));
    	
    	//WebElement menuHoverLink = driver.findElement(By.cssSelector("div[mod-id='lj-home-header']"));
		//String oText = webObject.getWebElement().getText();
    	
    	//SeleniumTestObject menuHoverLink = new SeleniumTestObject("css=div[mod-id='lj-home-header']");
    	//System.out.println("menuHoverLink  =============" + menuHoverLink.isVisible());
    	
    	//SeleniumTestObject child = menuHoverLink.getNewWebObject("css=span[class='city']");
    	
		//System.out.println("child =============" + child.getText());
		
    	//logBanner("Switch to the TianJin site now!");   	
    	//LJCommonHeadTasks.citySwitch(StringFetch.getString("L_TIANJIN"));
 
    	
    	/*
    	 * Current city site is TianJin
    	 */
    	//String curCity = new LJCommonHeadTasks().getCityName();
    	//System.out.println("22222222222===" + curCity);
    	//assertTrue("The current city site is TianJin!", curCity.equals(StringFetch.getString("L_TIANJIN")));
    	//SeleniumTestObject webObject = new SeleniumTestObject("css=div[mod-id='lj-home-search']");
    	
    	//SeleniumTestObject web01 = webObject.getChildObject("//h3/strong[1]/a/span");
    	//String curCity = LJHomeHeadTasks.getCityName();
    	//logCompare(curCity, StringFetch.getString("L_BEIJING"), "The Default city site is BeiJing!");
    	
    	//logCompare(true, LJNavigagorTasks.gotoYouZhiXueQu(), "������ѧ���б�ҳ");    	
    	//LJNavigagorTasks.gotoErShouFang();
    	/*
    	boolean success = false;
    	WebUser user = getUserDirectory().getUsers().get(0);
    	
    	LJNavTasks.logIn();
    	
    	WebTextField oUsernameField = new LJLoginObjects("id=dialog").getTextField_loginUsername();
    	WebTextField oPasswordField = new LJLoginObjects("id=dialog").getTextField_loginPassword();
    	System.out.println("22222222222===" + oUsernameField.getProperty("placeholder"));
    	
    	
    	if(oUsernameField.exists() && oPasswordField.exists()) {
			oUsernameField.setText(user.getPhone());
			oPasswordField.setText(user.getPassword());
			oPasswordField.pressEnter();
			success = LJNavTasks.isLogOutExist();
		} else if(!oUsernameField.exists()){
			log.error("NO LOGIN SCREEN");
			logScreenCapture("No login screen");
			
		}
		*/
    	
    	LJNavTasks.logIn();	
    	Platform.sleep(3);
    	
    	
    	LJLoginObjects ljLoginObjects = new LJLoginObjects("id=dialog");
    	System.out.println("22222222222===" + ljLoginObjects.getId());
    	
    	WebElement phone = ljLoginObjects.getWebElement().findElement(By.className("users"));
    	phone.sendKeys("aaaaaaaaaaaaaaaaaaaaaaaaaa");
    	Platform.sleep(5);
    	System.out.println("22222222222===" + phone.getTagName());
		
	}

    @Override
    public String getAuthor() {
        return "SYJ";
    }

    @Override
    public String getDescription() {
        return "LianJiaWeb authentication methods";
    }

    @Override
    public String getDataPath() {
        return "data";
    }

    @Override
    public boolean isSupported() {
        return true;
    }
}
