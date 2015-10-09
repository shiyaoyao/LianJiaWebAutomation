
package com.lianjia.lianjiaweb;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.InitializationError;

import com.lianjia.BaseRunner;
import com.lianjia.BaseTestCase;
import com.lianjia.automation.core.LogLevel;

public class LJTestRunner extends BaseRunner {
    private static final LJRunConfig RUN_CONFIG = new LJRunConfig();
    protected long startTime;
	protected long lapTime;
	protected String timingFile = System.getProperty("user.dir")+"/logs/current/";

    public LJTestRunner(Class<? extends BaseTestCase> testClass) throws InitializationError {
        super(testClass);
        setRerunMode(Boolean.parseBoolean(RUN_CONFIG.getProperty("rerunMode")));
    }

    @Override
    protected List<BaseTestCase> getChildren() {
        List<BaseTestCase> ret = new ArrayList<BaseTestCase>(20);
        
        try {
            LJTestCase instance = (LJTestCase)getTestClass().getJavaClass().newInstance();
            List<String> iterations = RUN_CONFIG.getPropertiesFiles(instance.getDataPath());
            int i = 1;
			for(String file : iterations) {
				instance = instance == null ? (LJTestCase)getTestClass().getJavaClass().newInstance() : instance;
				instance.setDataFile(file);
				if(iterations.size() > 1)
					instance.setInstance("_" + String.format("%02d", i++));
				setTestProperties(instance, file);
				ret.add(instance);

				instance = null; // reset for next loop
			}
        } catch (Throwable t) {
            log.error("Error spawning test case instance of: " + getTestClass().getJavaClass().getName(), t);
        }
        
        return ret;
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
    
    	if (!(t instanceof AssumptionViolatedException))
    		log.log(LogLevel.WARN, "TESTFAILED: "+test.getClass().getName());
    	else
    		log.log(LogLevel.INFO, "Assumption failure. No screenshot");
    }

    @Override
    protected String getConfigLogString() {
        return new StringBuffer("CONFIG; browser=").append(RUN_CONFIG.getProperty("browser")).append(";")
        .append("url=").append(RUN_CONFIG.getProperty("url")).append(";").toString();
    }
}
