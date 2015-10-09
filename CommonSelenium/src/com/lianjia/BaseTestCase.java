
package com.lianjia;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class BaseTestCase extends BaseAssert implements BaseTestCaseInterface {
    /**
     * Info, warn, error, and fatal logging levels can be used tests.  Debug is reserved for the testing framework. 
     */
	public enum STATUS {PASS, FAIL, SKIP};
    protected Logger log;
    private boolean _rerun = false; 
    protected String dataFile = "";
    protected String uniqueInstance;
    
    public BaseTestCase() {
        log = Logger.getLogger(getClass());
        uniqueInstance = "";
    }
    
    @Before
    public void before() throws Throwable {
        testSetUp();
    }
    
    @Test
    public void runTest() throws Throwable {
        testMain();
    }
    
    @After
    public void after() throws Throwable {
        testTearDown();
    }

    @Override
    public boolean isRerun() {
        return _rerun;
    }

    @Override
    public void setRerun(boolean isRerun) {
        _rerun = isRerun;
    }
    
    public String getTestCaseId() {
    	return "TST-00000";
    }
    
    public void setDataFile(String sFileName){
    	dataFile = sFileName;
    }
    
    public String getDataFile() {
    	return dataFile;
    }
    
    public void setInstance(String sInstance){
    	uniqueInstance = sInstance;
    }
    
    public String getInstance() {
    	return uniqueInstance;
    }
}
