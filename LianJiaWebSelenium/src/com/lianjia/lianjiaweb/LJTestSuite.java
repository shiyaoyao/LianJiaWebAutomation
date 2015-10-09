package com.lianjia.lianjiaweb;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
/**
 * The defining class for test suites in LianJiaWeb Selenium Automation. This class 
 * extends the LianJiaWeb test case so that the suite is allowed to contain another 
 * suite (Done mostly for the Custom Suite implementation).   
 *
 */
@RunWith(LJTestSuiteRunner.class)
public abstract class LJTestSuite extends LJTestCase {
	
	protected List<Class<? extends LJTestCase>> tests;
	
	public LJTestSuite() {
		super();
		tests = new ArrayList<Class<? extends LJTestCase>>();
	}
	
	public void addToTestList(String[] testNames){
		for(String testName: testNames)
		{
			try {
				Class<? extends LJTestCase> test = (Class<? extends LJTestCase>) Class.forName(testName).asSubclass(LJTestCase.class);
				tests.add(test);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				log.warn("Class Not Found for "+testName+", Skipping that test case");
				log.debug(e.getStackTrace());
			}catch (ClassCastException e){
				log.warn("Class "+testName+" was not a valid LJTestCase, Skipping that test case");
				log.debug(e.getStackTrace());
			}
		}
	}
	
	public List<Class<? extends LJTestCase>> getChildren(){
		return tests;
	}

}
