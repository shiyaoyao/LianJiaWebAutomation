
package com.lianjia;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.lianjia.automation.core.LogLevel;


public abstract class BaseRunner extends ParentRunner<BaseTestCase> implements TestFailListener {
	private static boolean isConfigLogged = false;

	protected Logger log;
	protected static boolean rerunMode = true;
	protected List<BaseTestCase> failed;

	public void setRerunMode(boolean rerunMode) {
		if(BaseRunner.rerunMode != rerunMode){
			BaseRunner.rerunMode = rerunMode;
			log.info("RERUNMODE;"+rerunMode);
		}
	}

	protected String name;

	public BaseRunner(Class<? extends BaseTestCase> testClass) throws InitializationError {
		super(testClass);        
		log = Logger.getLogger(getClass());

		if (!isConfigLogged) {
			String config = getConfigLogString();
			if (config != null)
				log.info(config);
			isConfigLogged = true;
		}
		name = testClass.getName();
		for (BaseTestCase test : getChildren())
			log.info(new StringBuffer("FOUNDTEST; ").append(test.getClass().getName()).append(test.getInstance()));

		failed = new ArrayList<BaseTestCase>();
	}

	@Override
	protected Description describeChild(BaseTestCase child) {
		return Description.createTestDescription(child.getClass(), child.getTestDescription());
	}

	@SuppressWarnings("static-access")
	@Override
	protected void runChild(BaseTestCase child, RunNotifier notifier) {
		Description description = describeChild(child);
		boolean bStarted = false;
		EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
		eachNotifier.fireTestStarted();
		String instance = child.getInstance();
		log.info(new StringBuffer("TESTSTART; name=").append(child.getClass().getName()).append(instance).append(";")
				.append(child.isRerun() ? "rerun;" : "")
				.append("datafile=").append(child.getDataFile()).append(";")
				.append("description=").append(child instanceof BaseTestCase ? ((BaseTestCase)child).getTestDescription() : child.getTestDescription())
		);
		try {
			if(child.isSupported()) {
				bStarted = true;
				child.before();
				if(!child.bTestCaseSkipped) {
					child.runTest();
					if(child.bTestCaseSkipped) {
						log.info("TESTIGNORE; " + child.getClass().getName());
					} else if(!child.bTestCasePass) {
						throw new AssertionError("A non-fatal error occured during the test case");
					} else
						log.info("TESTPASS; " + child.getClass().getName());
				} else {
					eachNotifier.fireTestIgnored();
					log.info("TESTIGNORE; " + child.getClass().getName());
				}
			} else {
				eachNotifier.fireTestIgnored();
				log.info("TESTIGNORE; " + child.getClass().getName());
			}
		} catch (Throwable t) { //Altered for better logging (Failures are AssertionErrors. Errors are Unexpected Exceptions)
			onTestFail(child, t);  // do this before we log testfail
			if (t instanceof AssumptionViolatedException)
				eachNotifier.addFailedAssumption((AssumptionViolatedException) t);
			else
				eachNotifier.addFailure(t);

			if (t instanceof AssertionError){
				log.log(LogLevel.FAILED,"TESTFAIL; " + child.getClass().getName() + "; ", t);
			}else{
				log.error("TESTFAIL; " + child.getClass().getName() + "; ", t);
			}

			if(!child.isRerun())
				failed.add(child);
		} finally {
			try {
				if (child.isSupported() || bStarted){
					log.info("TESTCOMPLETE;" + child.getClass().getName());
					child.after();
				}
			} catch (Throwable t) {
				log.error(t);
			}
			eachNotifier.fireTestFinished();
		}        
	}

	protected boolean rerunMode() {
		return rerunMode;
	}

	/**
	 * Returns a {@link Statement}: Call {@link #runChild(Object, RunNotifier)}
	 * on each object returned by {@link #getChildren()} (subject to any imposed
	 * filter and sort)
	 */
	@Override
	protected Statement childrenInvoker(final RunNotifier notifier) {
		return new Statement() {
			@Override
			public void evaluate() {
				for (BaseTestCase test : getChildren()) {
					runChild(test, notifier);
				}
				if (rerunMode()) {
					for(BaseTestCase failedTest : failed){
						failedTest.setRerun(true);
						runChild(failedTest, notifier);
					}
				}
			}
		};
	}

	/**
	 * @return The config string to log at the beginning of the test run. 
	 *   Logged only once at the run start, if at all.
	 */
	protected abstract String getConfigLogString();
}
