
package com.lianjia.lianjiaweb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import tasks.web.util.ResultsParser;
import tasks.web.util.SimpleFileIO;
import appobjects.web.Browser;

import com.lianjia.BaseSuiteRunner;
import com.lianjia.BaseTestCase;
import com.lianjia.automation.core.AutomationLoggerAppender;
import com.lianjia.automation.core.LogLevel;
import com.lianjia.automation.core.Platform;

public class LJTestSuiteRunner extends BaseSuiteRunner {
    private static final LJRunConfig RUN_CONFIG = new LJRunConfig();
    protected long startTime;
	protected long lapTime;
	protected String timingFilePath = System.getProperty("user.dir")+"/logs/current/";
	
    public LJTestSuiteRunner(Class<? extends LJTestCase> testClass) throws InitializationError {
        super(testClass);
        rerunMode = Boolean.parseBoolean(RUN_CONFIG.getProperty("rerunMode"));
    }

    @Override
    protected List<BaseTestCase> getChildren() {
    	List<BaseTestCase> ret = new ArrayList<BaseTestCase>(20);

    	try {
    		for(Class<? extends LJTestCase> test:((LJTestSuite) getTestClass().getJavaClass().newInstance()).getChildren()){
    			if(LJTestSuite.class.isAssignableFrom(test)){
    				ret.addAll(this.getChildren((LJTestSuite)test.newInstance()));
    			}
    			else{
    				LJTestCase instance = test.newInstance();
    				List<String> iterations = RUN_CONFIG.getPropertiesFiles(instance.getDataPath());
    				int i = 1;
					for(String file : iterations) {
						instance = instance == null ? (LJTestCase)test.newInstance() : instance;
						instance.setDataFile(file);
						if(iterations.size() > 1)
							instance.setInstance("_" + String.format("%02d", i++));
						setTestProperties(instance, file);
						ret.add(instance);

						instance = null; // reset for next loop

					}
    			}
    		}
    	} catch (Throwable t) {
    		log.error("Error spawning test case instance of: " + getTestClass().getJavaClass().getName(), t);
    	}

    	return ret;
    }
    
    private Collection<? extends BaseTestCase> getChildren(LJTestSuite newInstance) {
    	List<BaseTestCase> ret = new ArrayList<BaseTestCase>(20);
    	try {
    		for(Class<? extends LJTestCase> test: newInstance.getChildren()){
    			if(LJTestSuite.class.isAssignableFrom(test)){
    				ret.addAll(this.getChildren((LJTestSuite)test.newInstance()));
    			}
    			else{
    				LJTestCase instance = test.newInstance();
    				List<String> iterations = RUN_CONFIG.getPropertiesFiles(instance.getDataPath());
    				for (String file : iterations) {
    					instance = instance == null ? (LJTestCase)test.newInstance() : instance;
    					setTestProperties(instance, file);
    					ret.add(instance);

    					instance = null; //reset for next loop
    				}
    			}
    		}
    	} catch (Throwable t) {
    		log.error("Error spawning test case instance of: " + getTestClass().getJavaClass().getName(), t);
    	}

    	return ret;
    }
    @SuppressWarnings("static-access")
	@Override
    protected void runChild(BaseTestCase child, RunNotifier notifier) {
    	boolean bStarted = false;
    	boolean bSupported = false;
    	lapTime = System.nanoTime();
    	startTime = System.nanoTime();
		
        Description description = describeChild(child);
        child.resetTestCaseValues(child.getTestCaseId());
        
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        eachNotifier.fireTestStarted();
        String instance = child.getInstance();
        log.info(new StringBuffer("TESTSTART; name=").append(child.getClass().getName()).append(instance).append(";")
            .append(child.isRerun() ? "rerun;" : "")
            .append("datafile=").append(child.getDataFile()).append(";")
            .append("description=").append(child instanceof BaseTestCase ? ((BaseTestCase)child).getTestDescription() : child.getTestDescription()));
       String sName = timingFilePath+child.getClass().getName()+instance+(child.isRerun() ? ".1" : "")+"_time";
        File timingFileName = new File(sName);
        try {
			FileUtils.writeStringToFile(timingFileName, child.getClass().getName()+instance+"\n", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bSupported = child.isSupported();
            if (bSupported) {
				bStarted = true;
				child.before();
				logElapsedTime(timingFileName, "setup", startTime);
				if(!child.bTestCaseSkipped) {
					child.runTest();
					if(child.bTestCaseSkipped) {
						log.info("TESTIGNORE; " + child.getClass().getName());
					} else if(!child.bTestCasePass) {
						log.error("TESTCASE (" + child.getClass().getName()
								+ ") encountered non-fatal error during execution");
						throw new AssertionError("A non-fatal error occured during the test case");
					} else {
						log.log(LogLevel.PASSED, "end_of_test:|**** END TEST CASE ["+child.getClass().getName()+"] : PASSED ****");
            			log.info("TESTPASS; " + child.getClass().getName());
					}
				} else {
					eachNotifier.fireTestIgnored();
					log.info("TESTIGNORE; " + child.getClass().getName());
				}
            }
            else {
                eachNotifier.fireTestIgnored();
                log.info("TESTIGNORE; " + child.getClass().getName());
            }
        } catch (Throwable t) {
        	child.bTestCasePass = false;
            onTestFail(child, t);  // do this before we log testfail
            if (t instanceof AssumptionViolatedException)
                eachNotifier.addFailedAssumption((AssumptionViolatedException) t);
            else
                eachNotifier.addFailure(t);
            log.log(LogLevel.FAILED, "end_of_test:| **** END TEST CASE ["+child.getClass().getName()+"] : FAILED ****");
            log.error("TESTFAIL; " + child.getClass().getName() + "; ", t);
            
            if(!child.isRerun())
            	failed.add(child);
        } finally {
            try {
                if (bSupported || bStarted){
                	logElapsedTime(timingFileName, "test", lapTime);
                	if(child.bTestCaseSkipped){
                		Browser.shutdown(true);
                	} else {
                		log.info("TESTCOMPLETE;" + child.getClass().getName());
                        child.after();
                	}
                }
            } catch (Throwable t) {
                log.error(t);
            }
            eachNotifier.fireTestFinished();
        }
        
        try {
			logElapsedTime(timingFileName, "teardown", lapTime);
			String status = "";
			if(child.bTestCaseSkipped || !bSupported){
				status = "Skipped";
			} else {
				status = child.bTestCasePass ? "Passed" : "Failed";
			}
			logTestCaseStatus(timingFileName, status);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
    }
    
    @Override
    protected Statement childrenInvoker(final RunNotifier notifier) {
        return new Statement() {
			@Override
            public void evaluate() {
				junitReporting = RUN_CONFIG.getProperty("junitReporting").toLowerCase().equals("false") ? false : true;
            	log.info("SUITESTART;" + sTestSuiteName);
            	log.info("JUNITRETEST;"+junitReporting);
                for (BaseTestCase test : getChildren()) {
                    runChild(test, notifier);
                }
                // If rerun mode is set in Selenium.properties, run all failed tests again.
                if (rerunMode()) {
                	for(BaseTestCase failedTest : failed){
                		failedTest.setRerun(true);
                		runChild(failedTest, notifier);
                	}
                }
                log.info(AutomationLoggerAppender.SUITEEND);
                try {
					logSuitePassFailResults();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
            }
        };
    }

	/**
     * Sets the runconfig and test properties for the test instance
     * @param test The test instance to set a properties on
     * @param file The file containing the test properties to set
     */
    private void setTestProperties(LJTestCase test, String file) {
        file = test.getDataPath().replaceAll("[/]$", "") + "/" + file;
        test.setProperties(RUN_CONFIG, RUN_CONFIG.getProperties(file), file, RUN_CONFIG.getStrings());
    } 
    
    public void onTestFail(BaseTestCase test, Throwable t) {
    	
    }

    @Override
    protected String getConfigLogString() {
        return new StringBuffer("CONFIG; browser=").append(RUN_CONFIG.getProperty("browser")).append(";")
        .append("url=").append(RUN_CONFIG.getProperty("url")).append(";").toString();
    }
    
    protected void logElapsedTime(File file, String stage, long startNano) throws IOException{
    	long now = System.nanoTime();
    	lapTime = now;
    	String data = stage+"="+((now-startNano)/1e9);
    	if(stage.equalsIgnoreCase("teardown")){
    		data = data+"\ntotal="+((now-startTime)/1e9);
    	}
    	FileUtils.writeStringToFile(file, data+"\n", true);
    }
    
    protected void logTestCaseStatus(File file, String status) throws IOException {
    	FileUtils.writeStringToFile(file, "status="+status, true);
    }
    
	protected void logSuitePassFailResults() throws IOException {
		List<String> timeFiles = SimpleFileIO.getListOfFiles(timingFilePath, "_time");
		File passfail = new File(timingFilePath + "/SuitePassFailResults.txt");
		File vp = new File(timingFilePath + "/VerificationPoints.txt");
		FileUtils.writeStringToFile(passfail, "#testcase, status, setup, test, teardown, total\n");
		for(String s : timeFiles) {
			Properties p = new Properties();
			p = SimpleFileIO.getProperties(timingFilePath + "/" + s);
			String tcStat = s.replace("_time", "") + ", " +
						p.getProperty("status") + ", " +
						p.getProperty("setup", "0.0") + ", " +
						p.getProperty("test", "0.0") + ", " +
						p.getProperty("teardown", "0.0") + ", " +
						p.getProperty("total", "0.0") + "\n";
			FileUtils.writeStringToFile(passfail, tcStat, true);
		}
		try {
			logFilteredPassFail();
			FileUtils.writeLines(vp, ResultsParser.getTestPlanInfo());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void logFilteredPassFail() throws IOException {
		File suitepassfail = new File(timingFilePath + "/SuitePassFailResults.txt");
		File passfail = new  File(timingFilePath + "/PassFailResults.txt");
		File filtered = new File(timingFilePath + "/SuitePassFailResults_Filtered.txt");
		String rerunSuffix = "\\.1\\s*,";
		if(!suitepassfail.exists()) {
			int retry = 60;
			while(!suitepassfail.exists() && retry-- > 0) Platform.sleep(1);
			if(!suitepassfail.exists()){
				if(passfail.exists())
					FileUtils.copyFile(passfail, suitepassfail);
				return;
			}
		}
		
		ArrayList<String> lines = SimpleFileIO.getLinesInFile(suitepassfail.getAbsolutePath(), true);
		ArrayList<String> rerunTestCases = new ArrayList<String>();
		ArrayList<String> filteredResults = new ArrayList<String>();
		String temp;
		int index;
		
		for(String line : lines) {
			index = line.indexOf(',');
			if(index == -1)
				continue;
			temp = line.substring(0,index).trim();
			index = temp.indexOf(".1");
			if(index > 0){
				rerunTestCases.add(temp.substring(0,index));
			}
		}
		for(String line : lines) {
			index = line.indexOf(',');
			if(index == -1)
				continue;
			temp = line.substring(0,index).trim();
			if(!rerunTestCases.contains(temp))
				filteredResults.add(line.replaceAll(rerunSuffix,","));
		}
		
		SimpleFileIO.writeFile(filtered.getAbsolutePath(), filteredResults);
	}
}
