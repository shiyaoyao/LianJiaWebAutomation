package com.lianjia.automation;


import java.awt.Dimension;
import java.awt.Toolkit;

import com.lianjia.automation.core.loggers.control.CoreLogger;
import com.lianjia.automation.core.util.BitmapHelper;
import com.lianjia.automation.core.util.DateHelper;
import com.lianjia.automation.core.util.FileHelper;
import com.lianjia.automation.core.util.StringHelper;

public class CoreAutomation {

	public static class DateAndTime extends DateHelper{

	}
	
	public static class Strings extends StringHelper {

	}

	public static class Files extends FileHelper{
		
	}

	public static class Screenshots extends BitmapHelper {

	}
	
	public static class Platform extends com.lianjia.automation.core.Platform{
		
	}
	
	public static class Log extends CoreLogger{	
		
	}
	
	public static class Assert {
		private static boolean captureScreenshot = true;
		private static boolean showScreenshotInLogs = true;
		private static boolean failureOnlyLogging = false;
		public static void enableScreenshots()
		{
			captureScreenshot = true;
		}
		public static void disableScreenshots()
		{
			captureScreenshot = false;
		}
		
		public static void showScreenshotsAsLinksInLogs()
		{
			showScreenshotInLogs = false;
		}
		
		public static void enableFailureOnlyLogging()
		{
			failureOnlyLogging = true;
		}
		public static void disableFailureOnlyLogging()
		{
			failureOnlyLogging = false;
		}
		// Asserts that a condition is false.
		public static boolean	assertFalse(boolean condition, java.lang.String message) 
		{
			try {
				return assertFalse(message, condition);
			}
			catch(AssertionError e)
			{
				return false;
			}
		}
		
		// Asserts that a condition is false.
		public static boolean	assertFalse(java.lang.String message, boolean condition) 
		{
			String screenshotLink = captureScreenshotIfEnabled();
			
			if(condition)
			{
				Log.error("FAIL: " + message + screenshotLink);
			} else {
				if(!failureOnlyLogging)
				{
					Log.info("PASS: " + message + screenshotLink);									
				}
			}
			org.junit.Assert.assertFalse(message, condition);
			return !condition;
		}
		
		// Asserts that a condition is true.
		public static boolean	assertTrue(boolean condition, java.lang.String message) 
		{
			try {
				return assertTrue(message, condition);
			}
			catch(AssertionError e)
			{
				return false;
			}
		}
		
		// Asserts that a condition is true.
		public static boolean	assertTrue(java.lang.String message, boolean condition) 
		{
			String screenshotLink = captureScreenshotIfEnabled();
			
			if(!condition)
			{
				Log.error("FAIL: " + message + screenshotLink);
			} else {
				if(!failureOnlyLogging)
				{
					Log.info("PASS: " + message + screenshotLink);									
				}				
			}
			org.junit.Assert.assertTrue(message, condition);			
			return condition;			
		}
				
		// Asserts that a condition is equal.
		public static boolean	assertEquals(boolean expected, boolean actual, java.lang.String message)
		{
			try {
				return assertEquals(message, expected, actual);
			}
			catch(AssertionError e)
			{
				return false;
			}
		}
		
		// Asserts that a condition is equal.
		public static boolean	assertEquals(java.lang.String message, Object expected, Object actual) 
		{
			String screenshotLink = captureScreenshotIfEnabled();
			
			message = message + " expected = " + expected + " actual = " + actual;

			if(!expected.equals(actual))
			{
				Log.error("FAIL: " + message + screenshotLink);
			} else {
				if(!failureOnlyLogging)
				{
					Log.info("PASS: " + message + screenshotLink);									
				}				
			}			
			org.junit.Assert.assertEquals(message,expected,actual);
			return expected.equals(actual);
			
		}
		
		// Asserts that two doubles or floats are equal to within a positive delta.
		public static void assertEquals(java.lang.String message, double expected, double actual, double delta) 
		{
			String screenshotLink = captureScreenshotIfEnabled();			
			message = message + " expected = " + expected + " actual = " + actual + " delta = " + delta;
			try {
				org.junit.Assert.assertEquals(message, expected, actual, delta);
			} catch(AssertionError e) {
				Log.error("FAIL: " + message + screenshotLink);
				throw e;
			}
			if(!failureOnlyLogging)
			{
				Log.info("PASS: " + message + screenshotLink);									
			}	
		}
		
		// Asserts that a condition is equal.
		public static boolean assertGreaterThan(java.lang.String message, double expected, double actual) 
		{
			String screenshotLink = captureScreenshotIfEnabled();
			message = message + ": assert that " + actual + " is greater than " + expected;
			if(actual < expected)
			{
				Log.error("FAIL: " + message + screenshotLink);
			} else {
				if(!failureOnlyLogging)
				{
					Log.info("PASS: " +  message + screenshotLink);									
				}				
			}
			org.junit.Assert.assertTrue(message, actual > expected);
			return actual > expected;			
		}
				
		
		// Asserts that a condition is equal.  Also handle null values.
		public static boolean assertEqualsWithNull(java.lang.String message, Object expected, Object actual) 
		{
			String screenshotLink = captureScreenshotIfEnabled();			

			message = message + " expected = " + expected + " actual = " + actual;

			try {
				org.junit.Assert.assertEquals(message,expected,actual);
			} catch(AssertionError e) {
				Log.error("FAIL: " + message + screenshotLink);
				throw e;
			}
			if(!failureOnlyLogging)
			{
				Log.info("PASS: " + message + screenshotLink);									
			}
			return true;
		} 		
		
		// Fails a test with the given message.
		public static void	assertFail(java.lang.String message) 
		{
			assertFail(message,null);
		}
		
		// Fails a test with the given message.
		public static void	assertFail(java.lang.String message, java.lang.Throwable e) 
		{
			try {
				fail(message,e);
			}
			catch(AssertionError ae)	{	}
		}
		
		// Fails a test with the given message.
		public static void	fail(java.lang.String message) 
		{
			fail(message,null);
		}
		
		// Fails a test with the given message.
		public static void	fail(java.lang.String message, java.lang.Throwable e) 
		{
			message = message + captureScreenshotIfEnabled();
			try {
				Log.error(e.getMessage() ,e);
				e.printStackTrace();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			Log.error("FAIL: " + message);
			if(message.contains("<td"))
			{
				message = message.split("<td")[0];
			}
			org.junit.Assert.fail(message);
		}	
		
		private static String captureScreenshotIfEnabled()
		{
			if(captureScreenshot)
			{
				// when running tests on a GUI-less environment, this code always causes an exception.
				// test to see if we're in a GUI, and set captureScreenshot to FALSE if we aren't
				try {
					@SuppressWarnings("unused")
					Dimension myScreen = Toolkit.getDefaultToolkit().getScreenSize();
				} catch (Exception e) {
					CoreAutomation.Assert.disableScreenshots();
					CoreAutomation.Log.warn("Could not capture screenshot - error getting screen contents...");
					return "";
				}
								
				String screenshotName = BitmapHelper.captureScreenshotDoNotLog();
				if(showScreenshotInLogs)
				{
					return String.format("<td align=center><a href=%s target=_blank><img src=%s border=1 width=24, height=16></a></td>", screenshotName, screenshotName);
					
				} else {
					return String.format("<td align=center><a href=%s target=_blank>%s</a></td>", screenshotName, screenshotName);
					
				}
			}
			return "";
		}			
	}	
}
