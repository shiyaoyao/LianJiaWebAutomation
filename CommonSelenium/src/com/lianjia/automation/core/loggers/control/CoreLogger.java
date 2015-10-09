package com.lianjia.automation.core.loggers.control;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.lianjia.automation.CoreAutomation;
import com.lianjia.automation.CoreAutomation.Platform;
import com.lianjia.automation.core.util.BitmapHelper;
import com.lianjia.automation.core.util.DateHelper;
import com.lianjia.automation.core.util.FileHelper;
import com.lianjia.automation.core.util.StringHelper;

/**
 * Core Automation wrapper around log4j...
 */
public class CoreLogger {
	private static Logger logger = Logger.getLogger(CoreLogger.class.getName());
	private static Level currentLevel = null;
	private static String currentLog4JPropertiesFile = "";
	private static String log4JLogFileDirectory = Platform.getCurrentProjectPath().replace("\\", "/") + "/logs/";
	private static String logHeader = "";
	private static String lastCallingClass = "";
	private static String lastCallingMethod = "";
	private static boolean ALLOW_NEWLINES_IN_SUMMARY = false;
	private static boolean DEBUG_ENABLED = false;
	private static boolean LOG4J_CONFIGURED = false;
	private static boolean JUnitExecution = true;
	private static boolean captureScreenshotsOnWarnings = false;
	
	private static long startTime = DateHelper.setStartTime();
	private static String elapsedTime = DateHelper.getElapsedTime(startTime);
	
	public static void main(String[] args){
		handleLogConfig();		
		System.out.println("Text Log is:");
		System.out.println(getTextLogName());
		cleanupHtmlLog(getTextLogName().replace(".txt", "Summary.txt"), getHtmlLogName().replace(".html", "Summary.html"), getPassFailSummary(getTextLogName().replace(".txt", "Summary.txt")));
	}
	
	public static Logger getLogger()
	{
		return logger;
	}
	
	public static void enableDebugLogging()
	{
		DEBUG_ENABLED = true;
	}
	public static void disableDebugLogging()
	{
		DEBUG_ENABLED = false;
		logger.setLevel(currentLevel);
	}
	
	public static boolean isJUnitExcution(){
		return JUnitExecution;
	}
	
	public static void jUnitExecution_On(){
		JUnitExecution = true;
	}
	
	public static void jUnitExecution_Off(){
		JUnitExecution = false;
	}
		
	public static void debug(String message)
	{
		handleLogConfig();
		boolean shouldDisableDebugOnExit = false;
		Level currentLevel = logger.getLevel();
		if(!Logger.getRootLogger().getEffectiveLevel().toString().equals("OFF"))
		{
			if(DEBUG_ENABLED && !logger.isDebugEnabled())
			{
				
				logger.setLevel(Level.DEBUG);
				shouldDisableDebugOnExit = true;
			}
			logger.debug(message);
			if(shouldDisableDebugOnExit)
			{
				logger.setLevel(currentLevel);
			}			
		}		
	}	

	private static void suiteStart(String message)
	{
		handleLogConfig();
		if(!message.contains("automation.util.JarRunner") && !message.contains("automation.util.AntRunner") && !message.contains("automation.util.JarHelper"))
		{
			logger.info(" [SUITE START] " + message);						
		}
	}
	
	public static void suiteStart()
	{
		suiteStart(Platform.getCallingClassName());
	}
	
	
	public static void testStart()
	{
		handleLogConfig();
		logger.info(" [TEST START] " + Platform.getCallingMethodName());
	}
	
	public static void testStart(String message)
	{
		handleLogConfig();
		logger.info(" [TEST START] " + message);
	}
	
	public static void info(String message)
	{
		handleLogConfig();
		logger.info(" " + message);
	}
	
	public static void enableScreenshotsOnWarnings()
	{
		captureScreenshotsOnWarnings = true;
	}
	
	public static void warn(String message) {
		handleLogConfig();
		if(captureScreenshotsOnWarnings)
		{
			String screenshotName = BitmapHelper.captureScreenshotDoNotLog();
			message = message + String.format("<td align=center><a href=%s target=_blank><img src=%s border=1 width=24, height=16></a></td>", screenshotName, screenshotName);
		}
		logger.warn(" " + message);
	}

	
	public static void error(String message) {
		
		handleLogConfig();
		logger.error(message);
	}
	
	public static void error(String message, Throwable e) {
		handleLogConfig();

		logger.error(message, e);
	}
	
	public static String getLog4jPropertyFile()
	{
		return currentLog4JPropertiesFile;
	}
	
	private static List<String> getLog4jSearchLocations()
	{
		List<String> searchLocations = new ArrayList<String>();
		
		String propertyVal = System.getProperty("log4j.properties");
		if (propertyVal != null) {
			searchLocations.add(propertyVal);
		}
		
		if (Platform.isCallingClassRunningFromJar()) {
			searchLocations.add(FileHelper.join(Platform.getCallingClassLocation().getParent(), "log4j.properties"));
			searchLocations.add(FileHelper.join(Platform.getUserHome(), "Automation", "log4j.properties"));
		} else {
			searchLocations.add(FileHelper.join(Platform.getUserHome(), "Automation", "log4j.properties"));
			searchLocations.add(FileHelper.join(Platform.getCurrentProjectPath(), "log4j.properties"));
		}
		
		return searchLocations;
	}
	
	private static String getDefaultLog4jLocation()
	{
		if (Platform.isCallingClassRunningFromJar()) {
			return FileHelper.join(Platform.getCallingClassLocation().getParent(), "log4j.properties");
		} else {
			return FileHelper.join(Platform.getUserHome(), "Automation", "log4j.properties");
		}
	}
	
	private static void handleLogConfig()
	{
		if(!LOG4J_CONFIGURED)
		{
			for (String log4jSearchLocation : getLog4jSearchLocations())
			{
				if (FileHelper.fileExists(log4jSearchLocation))
				{
					LOG4J_CONFIGURED = true;
					currentLog4JPropertiesFile = log4jSearchLocation;
					PropertyConfigurator.configure(currentLog4JPropertiesFile);
					break;
				}
			}
			
			if(!LOG4J_CONFIGURED)
			{
				LOG4J_CONFIGURED = true;
				currentLog4JPropertiesFile = getDefaultLog4jLocation();
				writeDefaultLog4jPropertyFile();
				PropertyConfigurator.configure(currentLog4JPropertiesFile);
			}
						
			log4JLogFileDirectory = CoreAutomation.Log.getLogDirectory();
			FileHelper.makeDirs(log4JLogFileDirectory);
			
			addAppendersToRootLogger();
			
			currentLevel = logger.getLevel();
			// clear the log file and write a banner...
			writeHeader();
		}
		
		String myLastCallingClass;
		if(JUnitExecution)
			myLastCallingClass = Platform.getCallingClassName();
		else
			myLastCallingClass = getLogCallingClass();
		
		if(!lastCallingClass.equals(myLastCallingClass) && !myLastCallingClass.contains("com.lianjia.automation.core.") && !myLastCallingClass.contains("com.lianjia.automation.CoreAutomation") && !myLastCallingClass.contains("Tasks") && !myLastCallingClass.contains("Widgets") && !myLastCallingClass.contains("Objects"))
			{
			lastCallingClass = myLastCallingClass;
			suiteStart(lastCallingClass);
		}
		
		String myCallingMethodName = Platform.getCallingMethodName();
		if(!lastCallingMethod.equals(myCallingMethodName))
		{
			if(!myCallingMethodName.contains("com.lianjia.automation.core.loggers.control.CoreLogger.")  && !myCallingMethodName.toLowerCase().contains("teardown") && !myCallingMethodName.toLowerCase().contains("setup"))
			{
				lastCallingMethod = myCallingMethodName;
				testStart(lastCallingMethod);
				
			}
		}				
	}
	
	private static String getLogCallingClass() {
		String className = null;
		String previousFrameClass = null;
		
		// Find the class above 'CoreLogger' in the stack
		for (StackTraceElement frame : new Throwable().fillInStackTrace().getStackTrace()) {
			if (className != null && ! className.contains("J9VMInternals") ) {
				previousFrameClass = className;
			}
			className = frame.getClassName();
			
			if (previousFrameClass != null && previousFrameClass.contains("CoreLogger") && !className.contains("CoreLogger"))
				return className;
			
		}
		
		return className;
	}

	private static void addAppendersToRootLogger()
	{
		Logger rootLogger = Logger.getRootLogger();
		
		if (! rootLogger.getAllAppenders().hasMoreElements()) {
			if (logger.getLevel().isGreaterOrEqual(rootLogger.getLevel())) {
				rootLogger.setLevel(logger.getLevel());
			}
		}
		
		Enumeration<Appender> appenders = (Enumeration<Appender>) logger.getAllAppenders();
		while (appenders.hasMoreElements())
		{
			rootLogger.addAppender(appenders.nextElement());
		}
	}

	public static void generateCustomSummaryReports(String textLogName, String htmlLogName)
	{
		cleanupTextLog(textLogName);
		
		cleanupHtmlLog(textLogName.replace(".txt", "Summary.txt"), htmlLogName.replace(".html", "Summary.html"), getPassFailSummary(textLogName.replace(".txt", "Summary.txt")));
	}
	
	public static void generateSummaryReports() 
	{
		generateSummaryReports(false);
	}
	
	/**
	 * Takes the default log4j report and generates a nice high-level html report
	 * Also strips Selenium 1.x API noise out of the log 
	 */
	public static void generateSummaryReports(boolean allowNewLinesInSummary) {
		try {
			
			String textLogName = getTextLogName();
			String htmlLogName = getHtmlLogName();
			
			Platform.sleep(2);
			
			cleanupTextLog(textLogName);

			cleanupHtmlLog(textLogName.replace(".txt", "Summary.txt"), htmlLogName.replace(".html", "Summary.html"), getPassFailSummary(textLogName.replace(".txt", "Summary.txt")));

		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static String getLogDirectory()
	{
		try {
			String logFileName = getHtmlLogName();
			if(logFileName.contains("Results.html"))
			{
				return logFileName.replace("Results.html", "");
			} else {
				// deal with a custom log file name and potential differences in file separtors, sigh...
				String fileSeparator = Platform.getFileSeparator();
				if(logFileName.contains("/")){fileSeparator = "/";}
				if(logFileName.contains("\\")){fileSeparator = "\\";}
				if(logFileName.contains("\\\\")){fileSeparator = "\\\\";}
				return logFileName.substring(0,StringHelper.lastIndexOfIgnoreCase(logFileName,fileSeparator)) + fileSeparator;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return log4JLogFileDirectory;
	}

	public static String getTextLogName()
	{
		try {
			FileAppender appender = (FileAppender)Logger.getRootLogger().getAppender("TEXT");
			return appender.getFile();	
			
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String getHtmlLogName()
	{
		try {
			FileAppender appender = (FileAppender)Logger.getRootLogger().getAppender("HTML");
			return appender.getFile();	
			
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String getHtmlSummaryLogName()
	{
		try {
			FileAppender appender = (FileAppender)Logger.getRootLogger().getAppender("HTML");
			return appender.getFile().replace(".html", "Summary.html");	
			
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static void cleanupHtmlLog(String textLogFileName, String htmlLogFileName, String summaryResults)
	{
		if(FileHelper.fileExists(textLogFileName))
		{
			FileHelper.deleteFile(htmlLogFileName);
			if(FileHelper.fileExists(htmlLogFileName))
			{
				FileHelper.deleteFile(htmlLogFileName);				
			}
			
			// write the header
			writeHtmlHeader(htmlLogFileName);
			
			boolean writeLine = true;
			boolean currentTestResult = true;
			boolean testSummaryToLog = false;
			Date testStartTime = new Date();
			Date testEndTime = new Date();
			String detailedSummaryTable = "\n<b>Detailed Summary:</b><br>\n<table cellspacing=\"0\" cellpadding=\"3\" border=\"1px\" bordercolor=\"gray\" width=1024>\n<tr><th width=560>Scripts Executed</th>\n<th align=center width=100>Elapsed Time</th>\n<th align=center width=20>Result</th>\n</tr>\n";
			String[] orgFileContents = FileHelper.getFileContentsAsList(textLogFileName);
			for (int currentLine = 0; currentLine < orgFileContents.length; currentLine++) {
				
				writeLine = true;
				if(currentLine == 0){writeLine = false;}
				if(currentLine > 0 && currentLine < 10)
				{
					FileHelper.appendStringToFile(htmlLogFileName, orgFileContents[currentLine] + "\n");
					writeLine = false;
				}
				
				if(currentLine == 10)
				{
					writeLine = false; 
					writeResultsTableHeader(htmlLogFileName, summaryResults);
				}
				
				if(writeLine)
				{
					// if a test/suite start, write a test/suite start row and update the detailed summary table
					if(orgFileContents[currentLine].contains(" START]"))
					{
						boolean suiteStart = orgFileContents[currentLine].contains("SUITE START");
						String bgcolor = "E5E5E5";
						if(suiteStart)
							bgcolor = "BDBDBD";
						String startMessage = safeSplit(orgFileContents[currentLine],"]  \\[",1).replace("]", ":");
						FileHelper.appendStringToFile(htmlLogFileName, "<tr><td colspan=4 bgcolor=#" + bgcolor + "><b><a name=\"* " + startMessage + "\"></a>" + startMessage + "</td></tr>\n");
						
						//Build Detailed Summary table
						if(testSummaryToLog){
							testEndTime = extractTimeStamp(orgFileContents[currentLine]);
							detailedSummaryTable = logResultToSummaryTable(detailedSummaryTable, currentTestResult, testStartTime, testEndTime);
							testSummaryToLog = false;
							currentTestResult = true;
						}
						String summaryMessage = safeSplit(startMessage, "START: ", 1);
						if(suiteStart){
							detailedSummaryTable += "<tr><td colspan=3><b>" + summaryMessage + "</b></td></tr>\n";
						} else{
							detailedSummaryTable += "<tr><td><a href=\"#* " + startMessage + "\">" + summaryMessage + "</a></td>";
							testStartTime = extractTimeStamp(orgFileContents[currentLine]);
							testSummaryToLog = true;
						}
						
					// Manage Stack Trace Printing
					} else if(orgFileContents[currentLine].contains("com.") && (!orgFileContents[currentLine].contains("System property override")) && (!orgFileContents[currentLine].contains("* Script Name: ")) && (!orgFileContents[currentLine].contains("[WARN]"))){
						FileHelper.appendStringToFile(htmlLogFileName, "<tr><td></td><td></td><td><font color=#ff0000><b>" + orgFileContents[currentLine] + "\n");
						currentLine++;
						while((currentLine < orgFileContents.length) && (orgFileContents[currentLine].contains("com."))){
							FileHelper.appendStringToFile(htmlLogFileName, "<br>" + orgFileContents[currentLine] + "\n");
							currentLine++;
						}
						FileHelper.appendStringToFile(htmlLogFileName, "</b></font></td><td></td></tr>\n");
						currentLine--;
						
					// Manage other printing
					} else {
						if(orgFileContents[currentLine].contains("FAIL:")){
							currentTestResult = false;
						}
						
						if(orgFileContents[currentLine].startsWith("====") || orgFileContents[currentLine].startsWith("* "))
						{
							FileHelper.appendStringToFile(htmlLogFileName, "<tr><td></td><td></td><td>" + orgFileContents[currentLine] + "</td><td></td></tr>\n");

						} else {
							FileHelper.appendStringToFile(htmlLogFileName, getTableRowFromString(orgFileContents[currentLine]));							
						}
						
					}
				}
			}
			
			//Add the last test's results to the detailed summary table
			testEndTime = extractTimeStamp(orgFileContents[orgFileContents.length - 1]);
			detailedSummaryTable = logResultToSummaryTable(detailedSummaryTable, currentTestResult, testStartTime, testEndTime);
			
			writeHtmlFooter(htmlLogFileName);
			
			//Add Detailed Summary Table
			String fullHTMLContents = FileHelper.getFileContents(htmlLogFileName).replace("<b>Detailed Results:</b>", detailedSummaryTable + "\n</table><br><br>\n<b>Detailed Results:</b>");
			fullHTMLContents = fullHTMLContents.replace(elapsedTime , DateHelper.getElapsedTime(startTime));

			FileHelper.deleteFile(htmlLogFileName);
			FileHelper.appendStringToFile(htmlLogFileName, fullHTMLContents);
		}	
	}
	
	private static Date extractTimeStamp(String string) {
		String Date = safeSplit(string, " ", 0);
		String Time = safeSplit(string, " ", 1);
		return DateHelper.stringToDate(Date + " " + Time, "yyyy-MM-dd hh:mm:ss");
	}

	private static String safeSplit(String stringToSplit, String splitPattern, int indexToReturn)
	{
		try {
			return stringToSplit.split(splitPattern)[indexToReturn];
		} catch (Exception e) {
			return stringToSplit;
		}
	}
	
	public static String getTableRowFromString(String currentRow)
	{
		currentRow = "<tr><td>" + currentRow + "</td><td></td></tr>\n";
		currentRow = currentRow.replace(" [INFO]  PASS: ", "</td><td align=center bgcolor=#33ff66 title=\"Level\"><b>PASS</b></td><td title=\"Message\">");		
		currentRow = currentRow.replace(" [INFO] FAIL: ", "<td align=center bgcolor=red title=\"Level\"><b>FAIL</b></td><td title=\"Message\">");
		currentRow = currentRow.replace(" [INFO]  FAIL: ", "<td align=center bgcolor=red title=\"Level\"><b>FAIL</b></td><td title=\"Message\">");
		currentRow = currentRow.replace(" [ERROR] FAIL: ", "<td align=center bgcolor=red title=\"Level\"><b>FAIL</b></td><td title=\"Message\">");		
		currentRow = currentRow.replace(" [ERROR] ", "<td align=center bgcolor=red title=\"Level\"><b>ERROR</b></td><td title=\"Message\">");				
		currentRow = currentRow.replace(" [WARN] ", "<td align=center bgcolor=#FFA500 title=\"Level\"><b>WARN</b></td><td title=\"Message\">");
		currentRow = currentRow.replace(" [DEBUG] ", "<td align=center bgcolor=#659EC7 title=\"Level\"><b>DEBUG</b></td><td title=\"Message\">");
		currentRow = currentRow.replace(" [INFO] ", "</td><td align=center title=\"Level\"><b>INFO</b></td><td title=\"Message\">");

		currentRow = currentRow.replace("<td align=center><a href=","</td><td align=center><a href=");
		currentRow = currentRow.replace("height=16></a></td></td><td>","height=16></a></td>");
		return currentRow ;
	}
	
	public static void writeHtmlHeader(String htmlLogFileName)
	{
		FileHelper.appendStringToFile(htmlLogFileName, "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n");

		FileHelper.appendStringToFile(htmlLogFileName, "<html><head><title>Automation Results</title>\n");
		FileHelper.appendStringToFile(htmlLogFileName, "<style type=\"text/css\">\n");
		FileHelper.appendStringToFile(htmlLogFileName, "<!--\n");
		FileHelper.appendStringToFile(htmlLogFileName, "body, table {font-family: helvetica,arial,sans-serif; font-size: small;}\n");
		FileHelper.appendStringToFile(htmlLogFileName, "th {background: #336699; color: #FFFFFF; text-align: left;}\n");
		FileHelper.appendStringToFile(htmlLogFileName, "-->\n");
		FileHelper.appendStringToFile(htmlLogFileName, "</style>\n");
		FileHelper.appendStringToFile(htmlLogFileName, "</head>\n");
		FileHelper.appendStringToFile(htmlLogFileName, "<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">\n");
		FileHelper.appendStringToFile(htmlLogFileName, "\n");
		FileHelper.appendStringToFile(htmlLogFileName, "<pre>\n");	
	}	
	
	public static String logResultToSummaryTable(String currentTable, boolean testResult, Date start, Date end)
	{
		String bgColor = "red";
		String tResult = "FAIL";
		if(testResult){
			bgColor = "#33ff66";
			tResult = "PASS";
		}
		
		String testElapsedTime = DateHelper.getFormattedDateTime(end.getTime() - start.getTime(), "HH:mm:ss");
		return currentTable + "<td>" + testElapsedTime + "</td><td bgcolor=" + bgColor + "><b><center>" + tResult + "</center></b></td></tr>\n";
	}
	
	public static void writeHtmlFooter(String htmlLogFileName)
	{
		FileHelper.appendStringToFile(htmlLogFileName, "</table>\n<br>\n<br>\n</body>\n</html>");
	}
	
	public static void writeResultsTableHeader(String htmlLogFileName, String summaryResults)
	{
		FileHelper.appendStringToFile(htmlLogFileName, "</pre>\n" + summaryResults + "\n");
		FileHelper.appendStringToFile(htmlLogFileName, "<br>\n");
		FileHelper.appendStringToFile(htmlLogFileName, "<table cellspacing=\"0\" cellpadding=\"4\" border=\"1px\" bordercolor=\"gray\" width=1024>\n");
		FileHelper.appendStringToFile(htmlLogFileName, "<tr><th width=150>Time</th>\n");
		FileHelper.appendStringToFile(htmlLogFileName, "<th width=80>Level</th>\n");
		FileHelper.appendStringToFile(htmlLogFileName, "<th>Message</th><th align=center width=36>Screenshot</th>\n");
		FileHelper.appendStringToFile(htmlLogFileName, "</tr>\n");

	}
	
	/**
	 * private methods to deal with noisy log4j logs...
	 * @param logFileName
	 */
	public static String getPassFailSummary(String logFileName)
	{
		try {			
			String logFileContents = FileHelper.getFileContents(logFileName);
			
			int numberOfTestCases = StringHelper.count(logFileContents, "[TEST START]");
			int numberOfFailures = 0;
			int lastTestCaseIndex = logFileContents.indexOf("[TEST START]");
			
			for (int count = 0 ; count < numberOfTestCases ; count++)
			{
				int followingTestCaseIndex = logFileContents.indexOf("[TEST START]", lastTestCaseIndex+1);
				if(followingTestCaseIndex < 0)
					followingTestCaseIndex = logFileContents.length();
				String subLogFileContents = logFileContents.substring(lastTestCaseIndex, followingTestCaseIndex);
				numberOfFailures += ( StringHelper.contains(subLogFileContents, "FAIL:") ? 1 : 0 );
				lastTestCaseIndex = followingTestCaseIndex;
			}
			int numberOfPasses = numberOfTestCases - numberOfFailures;
			
			double percentPassed = (int)(((double) numberOfPasses / (double) numberOfTestCases) * 10000)/100.0;
			double percentFailed = (int)(((double) numberOfFailures / (double) numberOfTestCases) * 10000)/100.0;

			return "<br><b>Overall Results:</b><br><table cellspacing=\"0\" cellpadding=\"4\" border=\"1px\" bordercolor=\"#C0C0C0\" width=1024><tr><th>Total Testcases</th><th>Passed</th><th>Failed</th><th>Percent Passed</th><th>Percent Failed</th></tr><tr><td>" + numberOfTestCases + "</td><td>" + numberOfPasses + "</td><td>" + numberOfFailures + "</td><td>" + percentPassed + "%</td><td>" + percentFailed + "%</td></tr></table><br><br><b>Detailed Results:</b>";		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void removeLineFromSummaryReport(String lineToRemove)
	{
		CoreAutomation.Files.updateFile(CoreAutomation.Log.getTextLogName(), lineToRemove, lineToRemove + REMOVE_LINE_FROM_SUMMARY);
	}
	private static String REMOVE_LINE_FROM_SUMMARY = " MESSAGE_NOT_IN_SUMMARY_REPORT ";
	
	private static boolean shouldKeepLineInTextLog(String lineToCheck)
	{		
		boolean shouldKeepLine = true;
		
		if(lineToCheck.contains(REMOVE_LINE_FROM_SUMMARY)){return false;}		
		
		if(lineToCheck.contains(" SeleniumServer ")){return false;}
		if(lineToCheck.contains(" HttpServer ")){return false;}
		if(lineToCheck.contains(" Container ")){return false;}
		if(lineToCheck.contains(" SocketListener ")){return false;}
		if(lineToCheck.contains(" Credential ")){return false;}
		if(lineToCheck.contains(" ThreadedServer ")){return false;}
		
		if(lineToCheck.contains("Writing debug logs to")){return false;}
		if(lineToCheck.contains("[INFO] Java: ")){return false;}
		if(lineToCheck.contains("[INFO] OS: ")){return false;}
		if(lineToCheck.contains("Built from revision ")){return false;}
		if(lineToCheck.contains("RemoteWebDriver instances should")){return false;}
		if(lineToCheck.contains("[INFO] Version Jetty")){return false;}
		if(lineToCheck.contains("[INFO] Started HttpContext[/")){return false;}
		if(lineToCheck.contains("[INFO] Started org.openqa.jetty.")){return false;}
		if(lineToCheck.contains("[WARN] Failed to start: SocketListener")){return false;}
		
		if(lineToCheck.contains("Checking Resource aliases")){return false;}
		if(lineToCheck.contains("ServerSocket[")){return false;}
		if(lineToCheck.contains("HttpContext[")){return false;}
		if(lineToCheck.contains(".servlet.ServletHandler")){return false;}
		if(lineToCheck.contains("org.openqa.jetty.jetty.Server")){return false;}
				
		if(lineToCheck.startsWith("====")){return true;}
		if(lineToCheck.startsWith("* ")){return true;}
		if(lineToCheck.startsWith("	at")){return true;}
		
		if(lineToCheck.contains("core.automation.util.JarRunner")){return false;}
		if(lineToCheck.contains("core.automation.util.AntRunner")){return false;}
		if(lineToCheck.contains("core.automation.util.JarHelper")){return false;}
		
		if(!ALLOW_NEWLINES_IN_SUMMARY)
		{
			if(!lineToCheck.contains(" [") && !lineToCheck.contains("] ")){return false;}			
		}
		
		
		return shouldKeepLine;
	}	
	
	/**
	 * private methods to deal with noisy log4j logs...
	 * @param logFileName
	 */
	private static void cleanupTextLog(String logFileName)
	{
		try {	
			FileHelper.deleteFile(logFileName.replace(".txt", "Summary.txt"));
			if(FileHelper.fileExists(logFileName))
			{
				String[] orgFileContents = FileHelper.getFileContentsAsList(logFileName);
				for (int i = 1; i < orgFileContents.length; i++) {

					if(shouldKeepLineInTextLog(orgFileContents[i]))
					{
						FileHelper.appendStringToFile(logFileName.replace(".txt", "Summary.txt"), orgFileContents[i].replace("<pre>", "").replace("</pre>", "") + "\n");
					}
				}				
			} else {
				CoreAutomation.Log.error("Could not find '" + logFileName + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO: Log the log file directory
	private static void writeHeader() {
		String scriptName = "\n* Script Name:              " + Platform.getTopScriptName();
		if(scriptName.contains("JarRunner"))
		{
			scriptName = "";
		}
		String ljIPAddress = Platform.getLJIPAddress();
		if(!ljIPAddress.equals("unknown"))
		{
			ljIPAddress = " (" + ljIPAddress + ")";
		} else {
			ljIPAddress = "";
		}
		
		logHeader = "" +
				"\n==============================================================================" +
				scriptName + 
				"\n* Start Time:               " + DateHelper.getCurrentDateAndTime()  + 
				"\n* Elapsed Time:             " + elapsedTime  +
				"\n* Test Client:              " + Platform.getLocalClientName() + ljIPAddress + 
				"\n* Test Client OS:           " + Platform.getOSNameAndVersion() + 
				"\n* Test Client Java:         " + Platform.getJavaVendorAndVersion() +
				"\n* Test Client Java Home:    " + System.getProperty("java.home") +
				"\n* Log Directory:            " + CoreLogger.getLogDirectory() + 
				"\n* Core Automation:          " + Platform.getVersion() + 
				"\n==============================================================================\n";
		logger.info(logHeader);
	}
		
	private static void writeDefaultLog4jPropertyFile()	{
		FileHelper.makeDirs(new File(currentLog4JPropertiesFile).getParent());
		FileHelper.deleteFile(currentLog4JPropertiesFile);
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "# PLEASE DO NOT REMOVE ANY OF THE APPENDERS INCLUDED IN THIS FILE\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.logger.com.lianjia.automation.core.loggers.control.CoreLogger = INFO, CONSOLE, TEXT, HTML\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.additivity.com.lianjia.automation.core.loggers.control.CoreLogger = false\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.logger.com.dappit.Dapper.parser=ERROR\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.logger.org.w3c.tidy=FATAL\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.appender.CONSOLE = org.apache.log4j.ConsoleAppender\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.appender.CONSOLE.layout = org.apache.log4j.PatternLayout\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.appender.CONSOLE.layout.ConversionPattern = %d{yyyy-MM-dd HH:mm:ss} %c{1} [%p] %m%n\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.appender.TEXT = org.apache.log4j.FileAppender\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.appender.TEXT.File = " + log4JLogFileDirectory + "Results.txt\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.appender.TEXT.Append = false\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.appender.TEXT.layout = org.apache.log4j.PatternLayout\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.appender.TEXT.layout.ConversionPattern = %d{yyyy-MM-dd HH:mm:ss} [%p] %m%n\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.appender.HTML = org.apache.log4j.FileAppender\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.appender.HTML.File =  " + log4JLogFileDirectory + "Results.html\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.appender.HTML.Append = false\n");
		FileHelper.appendStringToFile(currentLog4JPropertiesFile, "log4j.appender.HTML.layout = org.apache.log4j.HTMLLayout\n");
	}
	
	public static void reloadLoggingConfig() {
		PropertyConfigurator.configure(currentLog4JPropertiesFile);
		addAppendersToRootLogger();
	}	
}
