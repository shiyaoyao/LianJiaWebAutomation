package com.lianjia;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
//import junit.framework.AssertionFailedError;

import com.lianjia.automation.core.LogLevel;

public abstract class BaseAssert {
	public static final String sFAIL = "FAILED";
	public static final String sPASS = "PASSED";
	public static String sTestCaseName = "";
	public static boolean bTestCasePass = true;
	public static boolean bTestCaseSkipped = false;
	public static String sTestCaseId = "";
	public static int iTestCaseVP = 1;
	
	public static boolean bOverrideLogLevelDuringSetUp = false;
	protected static HashMap<String, Integer> vpMap = new HashMap<String, Integer>();
	public static final String sNotVP = "non_vp;|";
	
    /**
     * This is a horrible way to get the logger for the class calling us...  however to get asserts to log 
     * as the class calling assert, I don't see much choice here.  We could wrap calls as non static calls 
     * which would be <i>far</i> better, but we want tasks and app opjects to be able to assert and those 
     * are currently static classes.
     * @return The logger for the Assert calling class.
     */
    private static Logger getLogger() {
        Logger logger = null;
        String sThisTestCaseName = "";
        String sTrace = "";
        try {
            throw new Exception();
        } catch (Exception e) {
            StackTraceElement[] trace = e.getStackTrace();
            
            for(StackTraceElement te : trace)
            	sTrace = sTrace + te.toString() + ",";
            
            for (int x = 2; x < trace.length ; x++){
            	if(trace[x].toString().matches(".*testMain.*")){
            		sThisTestCaseName = trace[x].getClassName().substring(trace[x].getClassName().lastIndexOf('.')+1);
            		if(!sThisTestCaseName.equals(sTestCaseName)){
            			sTestCaseName = sThisTestCaseName;
            			bTestCasePass = true;
            		}
            		break;
            	}
            }
            
            for (int i = 2; logger == null && i < trace.length; i++) {	
                if (!trace[i].getClassName().equals(BaseAssert.class.getName())){
                	logger = Logger.getLogger(trace[i].getClassName());
                }
            }
        }
        return logger;
    }
    
    /**
     * Initializes test case values. Call this when iterating through data-driven test cases.<br><br>
     * Test case specific vaules are automatically reset when a new
     * test case class name is detected. 
     * However, when running data-driven test cases, the same test case class is 
     * used repeatedly, thus the values are not reset between iterations.
     */
    public static void resetTestCaseValues(String testCaseId) {
    	sTestCaseName = "";
    	bTestCaseSkipped = false;
    	bTestCasePass = true;
    	bOverrideLogLevelDuringSetUp = false;
    	sTestCaseId = testCaseId;
    	iTestCaseVP = 1;
    	vpMap = new HashMap<String, Integer>();
    }
    
    private static String formatClassAndValue(Object value, String valueString) {
        String className= value == null ? "null" : value.getClass().getName();
        return className + "<" + valueString + ">";
    }
    
    /**
     * Asserts that the needle can be found in the haystack.
     * @param message The identifying message for the {@link AssertionError} (<code>null</code> okay)
     * @param haystack The string to search <b>in</b>
     * @param needle The string to search <b>for</b>
     */
    static public void assertContains(String message, String haystack, String needle) {
        try {

            if (!haystack.contains(needle)) {
                Assert.fail(new StringBuffer(message == null ? "" : message)
                    .append("\nexpected:\n   ").append(formatClassAndValue(haystack, String.valueOf(haystack)))
                    .append("\nto contain:\n   ").append(formatClassAndValue(needle, String.valueOf(needle))).toString()
                );
            }
            logMessage(LogLevel.PASSED,message);
        } catch (AssertionError afe) {
            logMessage(LogLevel.FAILED, afe.getMessage());
            throw afe;
        }
    }
    
    /**
     * Asserts that the needle <b>cannot</b> be found in the haystack.
     * @param message The identifying message for the {@link AssertionError} (<code>null</code> okay)
     * @param haystack The string to search <b>in</b>
     * @param needle The string to search <b>for</b>
     */
    static public void assertNotContains(String message, String haystack, String needle) {
        try {

            if (haystack.contains(needle)) {
                Assert.fail(new StringBuffer(message == null ? "" : message)
                    .append("\nexpected:\n   ").append(formatClassAndValue(haystack, String.valueOf(haystack)))
                    .append("\nto NOT contain:\n   ").append(formatClassAndValue(needle, String.valueOf(needle))).toString()
                );
            }
            logMessage(LogLevel.PASSED,message);
        } catch (AssertionError afe) {
            logMessage(LogLevel.FAILED, afe.getMessage());
            throw afe;
        }
    }
    
    /**
     * Asserts that a condition is true. If it isn't it throws
     * an AssertionError with the given message.
     */
    static public void assertTrue(String message, boolean condition) {
        try {

            Assert.assertTrue(message, condition);
            logMessage(LogLevel.PASSED,message);
        } catch (AssertionError afe) {
        	logMessage(LogLevel.FAILED, afe.getMessage());
            throw afe;
        }
    }
    
    /**
     * Asserts that a condition is true. If it isn't it throws
     * an AssertionError.
     */
    static public void assertTrue(boolean condition) {
        assertTrue(null, condition);
    }
    
    /**
     * Asserts that a condition is false. If it isn't it throws
     * an AssertionError with the given message.
     */
    static public void assertFalse(String message, boolean condition) {
        assertTrue(message, !condition);
    }
    
    /**
     * Asserts that a condition is false. If it isn't it throws
     * an AssertionError.
     */
    static public void assertFalse(boolean condition) {
        assertFalse(null, condition);
    }
    
    /**
     * Fails a test with the given message.
     */
    static public void fail(String message) {
        try {
            Assert.fail(message);
            logMessage(LogLevel.PASSED,message);
        } catch (AssertionError afe) {
        	logMessage(LogLevel.FAILED, afe.getMessage());
            throw afe;
        }
    }
    
    /**
     * Fails a test with no message.
     */
    static public void fail() {
        fail(null);
    }
    
    /**
     * Asserts that two objects are equal. If they are not
     * an AssertionError is thrown with the given message.
     */
    static public void assertEquals(String message, Object expected, Object actual) {
        try {

             Assert.assertEquals(message, expected, actual);
             logMessage(LogLevel.PASSED,message);
        } catch (AssertionError afe) {
        	logMessage(LogLevel.FAILED, afe.getMessage());
            throw afe;
        }
    }
    
    /**
     * Asserts that two objects are equal. If they are not
     * an AssertionError is thrown.
     */
    static public void assertEquals(Object expected, Object actual) {
        assertEquals(null, expected, actual);
    }
    
    /**
     * Asserts that two Strings are equal. 
     */
    static public void assertEquals(String message, String expected, String actual) {
        try {

            Assert.assertEquals(message, expected, actual);
            logMessage(LogLevel.PASSED,message);
        } catch (AssertionError afe) {
        	logMessage(LogLevel.FAILED, afe.getMessage());
            throw afe;
        }
    }
    
    /**
     * Asserts that two Strings are equal. 
     */
    static public void assertEquals(String expected, String actual) {
        assertEquals(null, expected, actual);
    }
    
    /**
     * Asserts that two doubles are equal concerning a delta.  If they are not
     * an AssertionError is thrown with the given message.  If the expected
     * value is infinity then the delta value is ignored.
     */
    static public void assertEquals(String message, double expected, double actual, double delta) {
        try {

            Assert.assertEquals(message, expected, actual, delta);
            logMessage(LogLevel.PASSED,message);
        } catch (AssertionError afe) {
        	logMessage(LogLevel.FAILED, afe.getMessage());
            throw afe;
        }
    }
    
    /**
     * Asserts that two doubles are equal concerning a delta. If the expected
     * value is infinity then the delta value is ignored.
     */
    static public void assertEquals(double expected, double actual, double delta) {
        assertEquals(null, expected, actual, delta);
    }
    
    /**
     * Asserts that two floats are equal concerning a positive delta. If they
     * are not an AssertionError is thrown with the given message. If the
     * expected value is infinity then the delta value is ignored.
     */
    static public void assertEquals(String message, float expected, float actual, float delta) {
        try {

             Assert.assertEquals(message, expected, actual, delta);
             logMessage(LogLevel.PASSED,message);
        } catch (AssertionError afe) {
        	logMessage(LogLevel.FAILED, afe.getMessage());
            throw afe;
        }
    }
    
    /**
     * Asserts that two floats are equal concerning a delta. If the expected
     * value is infinity then the delta value is ignored.
     */
    static public void assertEquals(float expected, float actual, float delta) {
        assertEquals(null, expected, actual, delta);
    }
    
    /**
     * Asserts that two longs are equal. If they are not
     * an AssertionError is thrown with the given message.
     */
    static public void assertEquals(String message, long expected, long actual) {
        assertEquals(message, new Long(expected), new Long(actual));
    }
    
    /**
     * Asserts that two longs are equal.
     */
    static public void assertEquals(long expected, long actual) {
        assertEquals(null, expected, actual);
    }
    
    /**
     * Asserts that two booleans are equal. If they are not
     * an AssertionError is thrown with the given message.
     */
    static public void assertEquals(String message, boolean expected, boolean actual) {
        assertEquals(message, Boolean.valueOf(expected), Boolean.valueOf(actual));
    }
    
    /**
     * Asserts that two booleans are equal.
     */
    static public void assertEquals(boolean expected, boolean actual) {
        assertEquals(null, expected, actual);
    }
    
    /**
     * Asserts that two bytes are equal. If they are not
     * an AssertionError is thrown with the given message.
     */
    static public void assertEquals(String message, byte expected, byte actual) {
        assertEquals(message, new Byte(expected), new Byte(actual));
    }
    
    /**
     * Asserts that two bytes are equal.
     */
    static public void assertEquals(byte expected, byte actual) {
        assertEquals(null, expected, actual);
    }
    
    /**
     * Asserts that two chars are equal. If they are not
     * an AssertionError is thrown with the given message.
     */
    static public void assertEquals(String message, char expected, char actual) {
        assertEquals(message, new Character(expected), new Character(actual));
    }
    
    /**
     * Asserts that two chars are equal.
     */
    static public void assertEquals(char expected, char actual) {
        assertEquals(null, expected, actual);
    }
    
    /**
     * Asserts that two shorts are equal. If they are not
     * an AssertionError is thrown with the given message.
     */
    static public void assertEquals(String message, short expected, short actual) {
        assertEquals(message, new Short(expected), new Short(actual));
    }
    
    /**
     * Asserts that two shorts are equal.
     */
    static public void assertEquals(short expected, short actual) {
        assertEquals(null, expected, actual);
    }
    
    /**
     * Asserts that two ints are equal. If they are not
     * an AssertionError is thrown with the given message.
     */
    static public void assertEquals(String message, int expected, int actual) {
        assertEquals(message, new Integer(expected), new Integer(actual));
    }
    
    /**
     * Asserts that two ints are equal.
     */
    static public void assertEquals(int expected, int actual) {
        assertEquals(null, expected, actual);
    }
    
    /**
     * Asserts that an object isn't null.
     */
    static public void assertNotNull(Object object) {
        assertNotNull(null, object);
    }
    
    /**
     * Asserts that an object isn't null. If it is
     * an AssertionError is thrown with the given message.
     */
    static public void assertNotNull(String message, Object object) {
        try {

            Assert.assertNotNull(message, object);
            logMessage(LogLevel.PASSED,message);
        } catch (AssertionError afe) {
        	logMessage(LogLevel.FAILED, afe.getMessage());
            throw afe;
        }
    }
    
    /**
     * Asserts that an object is null. If it isn't an {@link AssertionError} is
     * thrown.
     * Message contains: Expected: <null> but was: object
     * 
     * @param object
     *            Object to check or <code>null</code>
     */
    static public void assertNull(Object object) {
        try {
            Assert.assertNull(object);
            logMessage(LogLevel.PASSED, "NULL");
        } catch (AssertionError afe) {
        	logMessage(LogLevel.FAILED, afe.getMessage() );
            throw afe;
        }
    }
    
    /**
     * Asserts that an object is null.  If it is not
     * an AssertionError is thrown with the given message.
     */
    static public void assertNull(String message, Object object) {
        try {

            Assert.assertNull(message, object);
            logMessage(LogLevel.PASSED,message);
        } catch (AssertionError afe) {
            logMessage(LogLevel.FAILED, afe.getMessage());
            throw afe;
        }
    }
    
    /**
     * Asserts that two objects refer to the same object. If they are not
     * an AssertionError is thrown with the given message.
     */
    static public void assertSame(String message, Object expected, Object actual) {
        try {

            Assert.assertSame(message, expected, actual);
            logMessage(LogLevel.PASSED,message);
        } catch (AssertionError afe) {
            logMessage(LogLevel.FAILED, afe.getMessage());
            throw afe;
        }
    }
    
    /**
     * Asserts that two objects refer to the same object. If they are not
     * the same an AssertionError is thrown.
     */
    static public void assertSame(Object expected, Object actual) {
        assertSame(null, expected, actual);
    }
    
    /**
     * Asserts that two objects do not refer to the same object. If they do
     * refer to the same object an AssertionError is thrown with the
     * given message.
     */
    static public void assertNotSame(String message, Object expected, Object actual) {
        try {

            Assert.assertNotSame(message, expected, actual);
            logMessage(LogLevel.PASSED,message);
        } catch (AssertionError afe) {
            logMessage(LogLevel.FAILED, afe.getMessage());
            throw afe;
        }
    }
    /**
     * Asserts that two objects do not refer to the same object. If they do
     * refer to the same object an AssertionError is thrown.
     */
    static public void assertNotSame(Object expected, Object actual) {
        assertNotSame(null, expected, actual);
    }
    
    protected static void logMessage(Level level, String sMessage){
    	Logger _log = getLogger();
    	boolean bFail = level==LogLevel.FAILED;
    	boolean bImage = level == LogLevel.IMAGE;
    	
    	String message = sMessage == null ? "NULL message" : sMessage;
    	message = addVP(message);
    	if(bOverrideLogLevelDuringSetUp && !bImage)
        	level = LogLevel.SETTING;
    	
    	if(bFail){
    		if(!bOverrideLogLevelDuringSetUp) bTestCasePass = false;
    	}
    	
    	_log.log(level, message);
    	
    	if(bFail)
    		_log.log(LogLevel.IMAGE, "SCREEN CAPTURE");
    }
	/**
	 * Compares two booleans together. Logs the output
	 * @param bExpected
	 * @param bActual
	 * @param sLogMessage Message you want logged
	 * @return boolean result
	 */
	public static boolean logCompare(boolean bExpected, boolean bActual, String sLogMessage){
		boolean bReturn = false;
		Level level = null;
		
        if(bExpected == bActual){ 
			bReturn = true;
			level = LogLevel.PASSED;
		}else{
			bReturn = false;
			bTestCasePass = bReturn;
			level = LogLevel.FAILED;
		}

       // report compare results to log & console
		logMessage(level, 
				sLogMessage 
				+ " :: Expected: (" + bExpected + ") Actual: (" + bActual + ")");
		
		return bReturn;
	}
	
	/**
	 * Compares the two ints. Logs the output
	 * @param iExpected
	 * @param iActual
	 * @param sLogMessage Message you want logged	
	 * @return boolean result
	 */
	public static boolean logCompare(int iExpected, int iActual, String sLogMessage){
		boolean bReturn = false;
		Level level = null;
		
		if(iExpected == iActual){
			bReturn = true;
			level = LogLevel.PASSED;
		}else{
			bReturn = false;
			bTestCasePass = bReturn;
			level = LogLevel.FAILED;
		}

		// report compare results to log & console
		logMessage(level, 
				sLogMessage 
				+ " :: Expected: (" + iExpected + ") Actual: (" + iActual + ")");
		
		return bReturn;
	}

	/**
	 * Compares a string with a regular expression. Logs the output
	 * @param rExpected
	 * @param sActual
	 * @param sLogMessage message you want logged
	 * @return boolean result
	 */
	public static boolean logCompare(Pattern rExpected, String sActual, String sLogMessage){
		boolean bReturn = false;
		Level level = null;
		// Create a pattern to match a literal substring
		Pattern pContains = Pattern.compile(".*?"+Pattern.quote(rExpected.pattern())+".*");
		Matcher matcher = pContains.matcher(sActual);
		 if(matcher.matches()){
			bReturn = true;
			level = LogLevel.PASSED;
		}else{
			bReturn = false;
			bTestCasePass = bReturn;
			level = LogLevel.FAILED;
		}

		// report compare results to log & console
		logMessage(level, 
				sLogMessage 
				+ " :: <br><i>Expected:</i> (" + rExpected.pattern() + ") <br><i>Actual:</i> (" + sActual + ")");

		return bReturn;
	}
	
	/**
	 * Compares the Two strings. Logs the output
	 * @param sExpected
	 * @param sActual
	 * @param sLogMessage message you want logged
	 * @return boolean result
	 */
	public static boolean logCompare(String sExpected, String sActual, String sLogMessage){
		boolean bReturn = false;
		Level level = null;
		
		if(null != sExpected && sExpected.equals(sActual)){
			bReturn = true;
			level = LogLevel.PASSED;
		}else{
			bReturn = false;
			level = LogLevel.FAILED;
			bTestCasePass = false;
		}

		// report compare results to log & console
		logMessage(level, 
				sLogMessage 
				+ " :: <br><code>Expected: (" + sExpected + ") "+(sExpected.length() > 100? "<br><br>" : "<br>")+"Actual:&nbsp;&nbsp; (" + sActual + ")</code>");
		
		return bReturn;
	}
	
	public static void logScreenCapture(String sMessage) {
		logMessage(LogLevel.IMAGE, sMessage);
	}
	
	public static void logScreenCapture() {
		logMessage(LogLevel.IMAGE,"SCREEN CAPTURE");
	}
	
	/**
	 * THIS METHOD INTENDED FOR LIANJIA AUTOMATION.
	 * </br></br>
	 * If the test case sets <code>sTestCaseId</code> to a TCDB number with a 
	 * prefix of <b>TST-</b> the method will return a JUnitXMLReporter verification
	 * point. </br></br>
	 * If <code>sTestCaseId</code> is not a TCDB number, an empty string is returned
	 * @return JUnit XML verification point string ending with the delimiter <code>:|</code>
	 */
	private static String _getVP() {
		if(!sTestCaseId.startsWith("TST-"))
			return "";
		
		String sVP = "";
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		String sTrace = "";
		for(StackTraceElement e : stack)
			sTrace = sTrace +" : "+ e.toString();
		
		if(!sTrace.contains("com.lianjia.lianjiaweb"))
			return "";
		
		int istack = stack.length;
		for(int x = 0; x < istack; x++) {
			if (stack[x].getMethodName().equals("logMessage")){
				istack = x+2;
				break;
			}
		}
		StackTraceElement stel = stack[istack];
		String sClass = stel.getClassName();
		String sMethod = stel.getMethodName();
		boolean bTestCaseVP = sClass.startsWith("com.lianjia.lianjiaweb");
		
		if (sTestCaseId.startsWith("TST-") && ( bTestCaseVP || sMethod.toLowerCase().contains("verify"))){
			if(sTrace.matches(".*testSetUp.*"))
				sVP = sTestCaseId+".SETUP;| ";
			else
				sVP = sTestCaseId+"."+String.format("%03d",iTestCaseVP++)+":| ";
		}
		else {
			sVP = sNotVP+" ";
		}
		
		return sVP;
	}
	
	private static String addVP(String sMessage){
		if (sMessage == null || sMessage.contains(":|"))
			return sMessage;
		
		return _getVP()+sMessage;
	}
	
}
