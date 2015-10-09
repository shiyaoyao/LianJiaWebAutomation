
package com.lianjia;

public interface BaseTestCaseInterface extends BaseTestCaseConfig {
    /**
     * Test setup goes here, this method is run before each test.
     * @throws Throwable
     */
    public abstract void testSetUp() throws Throwable;
   
    /**
     * Main test code goes in here.
     * @throws Throwable
     */
    public abstract void testMain() throws Throwable;
    
    /**
     * Test teardown goes here, this method is run after each test.
     * @throws Throwable
     */
    public abstract void testTearDown() throws Throwable;
    
    /**
     * @return Category name for this test case instance.
     */
    public abstract String getCategory();
    
    /**
     * @return Name of the test author.
     */
    public abstract String getAuthor();
    
    /**
     * @return The description for this test case.
     */
    public abstract String getTestDescription();
    
    /**
     * @return The path to the data directory to use for this test.
     */
    public abstract String getDataPath();
    
    /**
     * @return If the test is supported to run.
     */
    public abstract boolean isSupported();
    
    /**
     * @return If the test run is currently being rerun.
     */
    public abstract boolean isRerun();
    
    /**
     * Sets if the test run is currently being rerun.
     */
    public abstract void setRerun(boolean isRerun);    
}
