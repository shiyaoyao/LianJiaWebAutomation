package com.lianjia.lianjiaweb.unittest;

import tasks.web.util.DateUtils;
import tasks.web.util.StringUtils;

import com.lianjia.automation.core.web.WebBrowser;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;
import com.lianjia.lianjiaweb.LJTestCase;

public class UnitTest extends LJTestCase {

	@Override
	public void testMain() throws Throwable {
		SeleniumTestObject	versionSpan		 = new SeleniumTestObject("css=span.version");
		String 		failing1		 = "css=span.failingAlert.bar";
		String		passing1		 = "css=span.passingAlert.bar";
		String 		failing2		 = "css=span.bar.failed";
		String		passing2		 = "css=span.bar.passed";
		
		SeleniumTestObject   failingAlertBar  = new SeleniumTestObject(failing2);
		SeleniumTestObject   passingAlertBar  = new SeleniumTestObject(passing2);
		String 		failedText		 = "NO INFO";
		String 		passedText		 = "NO INFO";
		String      url = System.getenv("UNITTEST_URL");	
		
		// If UNITTEST_URL isn't defined, try using the URL specified in target.properties
		// Verify that UNITTEST_URL is available
		if(url == null){
			log.warn("DID NOT FIND ENVIRONMENT VARIABLE: UNITTEST_URL. WILL USE TARGET PROPERTY URL.");
			// Get target.properties server URL
			url = getURL();
			// Add unit test path to base server URL
			url = (url.endsWith("/") ? url.substring(0,url.length()-1) : url);	
			assertTrue("Environment variable UNITTEST_URL not specified. Constructed URL: "+url, !url.isEmpty());
		} else {
			assertTrue("Environment variable UNITTEST_URL was specified ("+url+")", url != null);			
		}

		SeleniumTestObject[] waitObjects = new SeleniumTestObject[] {failingAlertBar, passingAlertBar};

		logBanner("Load Unit Tests...");
       
		// Load UNITTEST_URL and wait for results to display with a 5 minute timeout
		WebBrowser.loadURL(url);
		
		double begin = System.nanoTime();
		
		versionSpan.waitForMatchingElement();	
		double version = (Double)StringUtils.getNumberInStringAsNumber(versionSpan.getText(), false);
		log.debug("Jasmine version: "+version);
		if(version >= 1 && version < 2.0){
			failingAlertBar  = new SeleniumTestObject(failing1);
			passingAlertBar  = new SeleniumTestObject(passing1);
		}
		
		// Wait up to 5 minutes for EITHER the passingAlertBar or failingAlertBar to exist on the page
		SeleniumTestObject.waitForAnyToExist(waitObjects, 5 * 60 *1000);

		logBanner("Unit Tests completed in "+DateUtils.elapsedSecondsString(begin));
 		
		// Write text to log
		if (passingAlertBar.exists()){
			passedText = "Passed: "+passingAlertBar.getText();
			log.info(passedText);
		}
		if (failingAlertBar.exists()){
			failedText = "Failed: "+failingAlertBar.getText();
			log.info(failedText);
		}

		// Assert passingAlertBar displayed
		// Assert failingAlertBar not displayed		
		if(!logCompare(true, passingAlertBar.exists(),"PassingAlertBar exists ("+passedText+")")){
			assertTrue("FailingAlertBar does not exist ("+failedText+")", !failingAlertBar.exists());
		}
	}

	@Override
	public String getCategory() {
		return "Unittest";
	}

	@Override
	public String getAuthor() {
		return "SYJ";
	}

	@Override
	public String getDataPath() {
		return "data";
	}

	@Override
	public boolean isSupported() {
		return true;
	}

	@Override
	public String getDescription() {
		return "Runs LianJiaWeb unittests";
	}
}
