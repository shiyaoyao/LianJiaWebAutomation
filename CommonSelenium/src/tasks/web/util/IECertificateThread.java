package tasks.web.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.lianjia.automation.core.web.SeleniumCore;

public class IECertificateThread extends Thread {
	private boolean bMaintenance = false;
	private boolean bPass = false;
	private String sActionStatus = "No Action Taken";
	private String sExitStatus = "FAIL";
	
	public IECertificateThread() {
	
	}
	
	public void run() {
		WebDriver driver = SeleniumCore.getWebDriverBrowser().getWebDriverAPI();
		try {
			if(driver.getPageSource().contains("overridelink")){
				sActionStatus = "Found 'overridelink' object on page. Running javascript action to click it.";
				System.out.println(sActionStatus);
				SeleniumCore.getWebDriverBrowser().getWebDriverAPI().navigate().to("javascript:document.getElementById('overridelink').click()");
				bPass = true;
				sExitStatus="Success";
				return;
			} else {
				try {
					sActionStatus = "No Cert Override link. Looking for LotusLive information status box.";
					System.out.println(sActionStatus);
					bMaintenance = driver.findElements(By.xpath("//div[@id='statusBox']")).size() > 0;
					bPass = true;
					sExitStatus="Success";
					return;
				} catch (Exception e) {
					sExitStatus = "Exception looking for LotusLive status object";
					System.out.println(sExitStatus);
					return;
				}
			}
		} catch (Exception e) {
			sExitStatus = "Exception invoking javascript";
			System.out.println(sExitStatus);
			return;
		}
	}
	
	public boolean statusBox() {
		return bMaintenance;
	}
	
	public String actionStatus() {
		return sActionStatus;
	}
	
	public String exitStatus(){
		return sExitStatus;
	}
	
	public boolean pass() {
		return bPass;
	}

}
