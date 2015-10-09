/**
 * 
 */
package com.lianjia.automation.core;

import org.apache.log4j.Level;

public class LogLevel extends Level {

	public static final Level RESUL	= new LogLevel(Level.INFO_INT, "RESULT", Level.INFO_INT);
	public static final Level PASSED = new LogLevel(Level.INFO_INT, "PASSED", Level.INFO_INT);
	public static final Level FAILED = new LogLevel(Level.INFO_INT, "FAILED", Level.INFO_INT);
	public static final Level IMAGE = new LogLevel(Level.INFO_INT, "IMAGE", Level.INFO_INT);
	public static final Level TESTPLAN = new LogLevel(Level.INFO_INT, "TESTPLAN", Level.INFO_INT);
	public static final Level EXCEPTION = new LogLevel(Level.INFO_INT, "EXCEPTION", Level.INFO_INT);
	public static final Level SETTING = new LogLevel(Level.INFO_INT, "SETTING", Level.INFO_INT);
	
	/**
	 * @param level
	 * @param levelStr
	 * @param syslogEquivalent
	 */
	public LogLevel(int level, String levelStr, int syslogEquivalent) {
		super(level, levelStr, syslogEquivalent);
	}	
}
