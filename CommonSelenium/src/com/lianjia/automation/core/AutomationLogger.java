package com.lianjia.automation.core;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.internal.Base64Encoder;

import tasks.web.util.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * A pretty logger
 */
public class AutomationLogger {	
	int totalTestcases = 0;
	int passedTestcases = 0;
	int failedTestcases = 0;
	int skippedTestcases = 0;
	
	JsonObject logObject = null;
	JsonObject currentTestObject = null;
	
	HashMap<String,String> _attributes = new HashMap<String,String>();	
	
	//Overall time
	private long overallStartTime = 0;
	private long overallEndTime = 0;
	
	//Current test time
	private long currentTestStartTime = 0;
	private long currentTestEndTime = 0;
	private boolean currentTestPassed = true;
	private boolean currentTestSkipped = false;
	private boolean isRetest = false;
	private boolean rerunMode = true;
	
	//If the same test runs multiple times, this variable will be appended to the end of the log
	private int currentTestVariant =0; 
	
	//File writing
	private File testFile = null;
	private File logFile = null;
	private File logsDir = null;
	
	private Vector<String> testNames = null;

	private static JUnitXMLReporter vpReport = new JUnitXMLReporter(true);
	private static JUnitXMLReporter junitReport = new JUnitXMLReporter(false);
	private boolean junitReporting = false;
	private boolean junitRetestReplace = false;
	
	/**
	 * Instantiates the logger 
	 * @param logDirectory
	 * @param attributes Attributes passed in are transferred directly the main log file's JSON output
	 * @param testNames The names of the tests queued for execution
	 */
	protected AutomationLogger(File logDirectory, HashMap<String,String> attributes) {		
		logsDir = logDirectory;
		logFile = new File(logsDir, "results.json");
		logObject = new JsonObject();		
		
		setAttributes(attributes);
		
		vpReport.setLogDir(logsDir);
		junitReport.setLogDir(logsDir);
	}
		
	public void startLogging(Vector<String> testNames) {
	    setTests(testNames);
        this.totalTestcases = testNames.size();
        
        Map<String, String> attr = new HashMap<String, String>();
        attr.put("totalTestcases", this.totalTestcases+"");
        attr.put("status", "In Progress");
        
        overallStartTime = System.currentTimeMillis();
        logObject.add("startTime", new JsonPrimitive(new Date(overallStartTime).toString()));
        
        setAttributes(attr);

        if(junitReporting){
        	vpReport.startReport();
        	vpReport.appendTestSuite(_attributes.get("title"));
        	for(String testName: testNames){
        		vpReport.appendTest(testName);
        	}
        	vpReport.replaceWithRetest(junitRetestReplace);
        	junitReport.startReport();
        	junitReport.appendTestSuite(_attributes.get("title"));
        	for(String testName: testNames){
        		junitReport.appendTest(testName);
        	}
        	junitReport.replaceWithRetest(junitRetestReplace);

        }
	}
	
	/**
	 * Sets the names of tests which will be run. Calling startNewTest(name) will remove the test name from this list.
	 * @param names
	 */
	public void setTests(Vector<String> testNames) {
		this.testNames = testNames;
	
		//How many test cases are remaining?
		getAttributes().put("totalTestcasesRemaining", testNames.size()+"");

		JsonArray testsToBeRun = new JsonArray();
		for(int i=0;i<testNames.size();i++)
			testsToBeRun.add(new JsonPrimitive(testNames.get(i)));

		logObject.add("testsToBeRun", testsToBeRun);
	}
		
	/**
	 * Returns the names of tests yet to be run
	 * @return
	 */
	public Vector<String> getTests(){
		return this.testNames;
	}

	/**
	 * Sets attributes which are output as JSON
	 * @param attributes
	 */
	public void setAttributes(Map<String,String> attributes){
		if (_attributes == null)
		    _attributes = new HashMap<String, String>();
		_attributes.putAll(attributes);
		writeJSON();
	}
	
	public HashMap<String,String> getAttributes(){
		return _attributes;
	}
	
	/**
	 * Creates an entry in the log for a new test. 
	 * @param name - The name of the test
	 * @param desc - The description of the test
	 * @param isRetest- Is this a retry of a previously failed test?
	 */
	public void startNewTest(String title, String desc, String data, boolean isRetest){
		//The last test did not terminate correctly
		if (currentTestStartTime!=0){
		    logIt(LogLevel.ERROR, "Testcase failed to finish properly");
		    failCurrentTest();
		}
		
		//Is this test a retest of an existing test?
		this.isRetest = isRetest;				
			
		try {
			//Create the file in its own directory
			testFile = new File(logsDir, title);
			
			//Does the directory already exist? Could be a data driven test
			if (testFile.exists()){
				while(testFile.exists()){
					currentTestVariant++;
					testFile = new File(logsDir, title+"."+currentTestVariant);
				}
			}	
			//Add to an array in the overall log
			String type =!isRetest ?  "tests" : "retest";
			JsonArray tests = logObject.has(type) ? (JsonArray)logObject.get(type) : new JsonArray();
			JsonObject test = new JsonObject();
			test.addProperty("title",testFile.getName());
			test.addProperty("desc",desc);
			test.addProperty("data",data);
			test.addProperty("status","In Progress");
			test.addProperty("time","Running");
			tests.add(test);
			logObject.add(type,tests);
						
			//Create a new test object
			currentTestObject = new JsonObject();
			currentTestObject.addProperty("name",testFile.getName());
			currentTestObject.addProperty("desc",desc);
			currentTestObject.addProperty("data",data);
			currentTestObject.addProperty("status","In Progress");
			currentTestObject.addProperty("title",title);
			
			//Is test data driven? It should have a driving properties file
			if (_attributes.containsKey("properties"))
				currentTestObject.addProperty("properties",_attributes.get("properties"));
						
			//Remove the name of this tests from the test to be run list
			Vector<String> testNames = this.getTests();
			testNames.remove(title);
			this.setTests(testNames);
			
			getAttributes().put("totalTestcasesInProgress", "1");
			
			writeJSON();
			
			if(isRetest && junitReporting){
				vpReport.appendTest(currentTestObject.get("title").getAsString()+"RETEST");
				junitReport.appendTest(currentTestObject.get("title").getAsString()+"RETEST");
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		currentTestPassed = true;
		currentTestSkipped = false;
		currentTestStartTime = System.currentTimeMillis();
	}
	
	public void logClientInfo(Properties clientInfo) {
			currentTestObject.addProperty("client-name", (String)clientInfo.get("client-name"));
			currentTestObject.addProperty("client-os", (String)clientInfo.get("client-os"));
			currentTestObject.addProperty("browser-name", (String)clientInfo.get("browser-name"));
			currentTestObject.addProperty("automation", (String)clientInfo.get("automation"));
			currentTestObject.addProperty("engine", (String)clientInfo.get("engine"));
			currentTestObject.addProperty("selenium", (String)clientInfo.get("selenium"));
			
			writeJSON();
	}
	
	/**
	 * Writes the log object and current test 
	 * @throws JSONException 
	 */
	private void writeJSON() {
		
		//Transfer in memory properties directly into JSON object
		if (_attributes!=null){
			Iterator<String> itr = _attributes.keySet().iterator();
			while(itr.hasNext()){
				String key = itr.next();
				String value = _attributes.get(key);
				logObject.addProperty(key, value);
			}
		}
				
		try {
			//Delete the file and write a new JSON object
			if (logFile.exists())
				logFile.delete();
			logFile.createNewFile();
			BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile));
			logWriter.write(logObject.toString());
			logWriter.flush();
			logWriter.close();

			if (testFile!=null && currentTestObject!=null){
				if (testFile.exists())
					testFile.delete();
				
				testFile.createNewFile();
				BufferedWriter testWriter = new BufferedWriter(new FileWriter(testFile));
				testWriter.write(currentTestObject.toString());
				testWriter.flush();
				testWriter.close();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}	
	
	/**
	 * Ends the test cases, writes result
	 */
	public void endTest(){		
		if (currentTestSkipped){
			skippedTestcases++;
		}
		else if(!isRetest){
			passedTestcases += currentTestPassed ? 1 :0;
			failedTestcases += !currentTestPassed ? 1 :0;
		}else{
			if(currentTestPassed){
				passedTestcases++;
				failedTestcases--;
			}
		}
		
		currentTestEndTime = System.currentTimeMillis();		
		long elapsed = currentTestEndTime-currentTestStartTime;
	
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH'h'mm'm'ss's'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		
			JsonArray tests = (JsonArray)logObject.get(this.isRetest ? "retest" : "tests");
			//Get the last entry
			JsonObject entry = tests.get(tests.size()-1).getAsJsonObject();
			entry.addProperty("time", dateFormat.format(new Date(elapsed)));
			
			String status = currentTestSkipped ? "Skipped" : currentTestPassed ? "Passed" : "Failed";
			entry.addProperty("status", status);

			getAttributes().put("totalTestcasesSkipped", skippedTestcases+"");
			getAttributes().put("totalTestcasesPassed", passedTestcases+"");
			getAttributes().put("totalTestcasesFailed", failedTestcases+"");
			getAttributes().put("totalTestcasesPercentage", Math.round(((float)passedTestcases/(float)(totalTestcases-skippedTestcases))*100)+"%");
			
			if(junitReporting){
				String testName = currentTestObject.get("title").getAsString();
				if(isRetest){
					testName+="RETEST";
				}
				if(currentTestSkipped){
					vpReport.skipTest(testName);
					junitReport.skipTest(testName);
				}
				vpReport.resolveTest(testName, String.valueOf(elapsed/1000));
				junitReport.resolveTest(testName, String.valueOf(elapsed/1000));
			}
			currentTestObject.addProperty("status", currentTestSkipped ? "Skipped" : currentTestPassed ? "Passed" : "Failed");
		
		getAttributes().put("totalTestcasesInProgress", "0");
		
		writeJSON();
		
		//Indicates the test finished successfully, checked against in endLogging and startnewtestcase
		currentTestStartTime = 0;
		currentTestVariant = 0;
		currentTestObject = null;
		
		if(testNames.size() == 0) {
		    if ((isRetest  || !rerunMode) || currentTestPassed || currentTestSkipped)
		        endLogging();  // All tests should be done running, this should stop the test counter.
		}
	}	
	
	/**
	 * Marks current test as failed
	 */
	public void failCurrentTest(){				
		currentTestPassed = false;	
		endTest();
	}
		
	public void skipCurrentTest(){
		currentTestSkipped = true;
		endTest();
	}
	
	public void passCurrentTest() {	
	    endTest();
	}

	public void endLogging(){
		//The last test did not terminate correctly
		if (currentTestStartTime!=0){
		    logIt(LogLevel.ERROR, "Testcase failed to finish properly");
		    failCurrentTest();
		}
		
		overallEndTime = System.currentTimeMillis();
		int elapsed = (int)(overallEndTime-overallStartTime);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH'h'mm'm'ss's'");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		_attributes.put("endTime",new Date(overallEndTime).toString());
		_attributes.put("elapsedTime",dateFormat.format(new Date(elapsed)));
		_attributes.put("status","Completed");
		
		writeJSON();
		if(junitReporting){
			vpReport.endReport(String.valueOf(elapsed/1000));
			junitReport.endReport(String.valueOf(elapsed/1000));
		}
	}
	
	protected void logIt(Level type, String message){
		if (message==null  || currentTestObject==null){
			System.out.println("can not output logs");
			return;
		}

		String messageHTML = message;
		String vpointDelim = JUnitXMLReporter.VP_DELIMITER;
		String nonvpDelim = ";|";
		boolean bPassFailType = type == LogLevel.PASSED || 
				type == LogLevel.FAILED;
		
		////////////////////////////////////////////
		// FORMAT SCREEN CAPTURE FOR HTML LOGGING
		////////////////////////////////////////////
		if(type == LogLevel.IMAGE) {
			try {
				String filename = "SCREEN_CAPTURE_" +System.currentTimeMillis() + ".png";
				filename = filename.replaceAll("[^0-9a-zA-Z\\.]", "_");
				File file = new File(logsDir.getAbsolutePath()
						+ File.separator + 
						AutomationLoggerAppender.IMAGES_DIR + 
						File.separator + filename);

				Toolkit toolkit = Toolkit.getDefaultToolkit();  
				Dimension screenSize = toolkit.getScreenSize();  
				Rectangle screenRect = new Rectangle(screenSize);  
				// create screen shot  
				Robot robot = new Robot();  
				BufferedImage image = robot.createScreenCapture(screenRect);  
				// save captured image to PNG file  
				ImageIO.write(image, "png", file);
				byte[] data = new byte[(int)file.length()];
				InputStream in = new FileInputStream(file);
				in.read(data, 0, (int)file.length());
				in.close();
				//makes the embedded screen shot a link to the full size image
				messageHTML =messageHTML+ String.format("</br></br><a href=\"%s/%s\" target=_blank><img width='100%%' src='data:image/png;base64,%s'></a>", AutomationLoggerAppender.IMAGES_DIR, filename, new Base64Encoder().encode(data).replaceAll("\\s+", ""));
				
			} catch (Exception e) {
				logIt(Level.ERROR, e.getMessage());
			}
		}
	
			////////////////////////////////////////////
			// FORMAT JSON MESSAGE FOR HTML LOGGING
			// PRESERVE HTML TAGS IN MESSAGE
			////////////////////////////////////////////
			JsonArray messages = currentTestObject.has("messages") ? (JsonArray)currentTestObject.get("messages") : new JsonArray();
			JsonObject entry = new JsonObject();
			// Check for Verification point
			if (bPassFailType) {
				String patternVP = "(.*?"+StringUtils.escapeRegex(vpointDelim)+")";
				String nonVP = "(.*?"+StringUtils.escapeRegex(nonvpDelim)+")";
				if(messageHTML.matches(patternVP+".*") && messageHTML.contains("TST-")){
					messageHTML = messageHTML.replaceFirst(patternVP, "<span style=\"color: #3C81EF;\">$1</span>");
				} else if(messageHTML.matches(nonVP+".*"))
					messageHTML = messageHTML.replaceFirst(nonVP, "<span style=\"color: #809090;\">$1</span>");
			}
			
			// This line is removing all explicit HTML formatting from the HTML logs build from JSON 
			// data objects. This escaping HTML tags at this point renders LianJiaWeb automation logging 
			// virtually unreadable.
			
			if(LogLevel.IMAGE!= type){
				messageHTML = messageHTML.replaceAll("\\n", "</br>");
			}
			
			////////////////////////////////////////////
			// ADD LOGGING INSTANCE TO JSON ARRAY
			////////////////////////////////////////////
			entry.addProperty("type", type.toString());
			entry.addProperty("message",messageHTML);
			entry.addProperty("timestamp",new SimpleDateFormat(" HH:mm:ss,SSS").format(new Date()));
			entry.addProperty("callstamp",getCallStamp());
			
			messages.add(entry);
			currentTestObject.add("messages", messages);
			
			////////////////////////////////////////////
			// FORMAT MESSAGE FOR JUNIT XML LOGGING
			////////////////////////////////////////////
			if(junitReporting){
				message = StringEscapeUtils.escapeHtml4(message);
				// Only log messages that do NOT contain non-verification point delimiter ;|
				// This allows us to filter out specific PASSED/FAILED types not intended as verification points 
				boolean bLogVP = !message.contains(nonvpDelim);
				String testName = currentTestObject.get("title").getAsString();
				if(isRetest){
					testName+="RETEST";
				}
				if(bLogVP){
					if(type==LogLevel.FAILED){
						if(message.contains("\n\n")){
							vpReport.appendStack(testName, message);
							junitReport.appendStack(testName, message);
						}else{
							vpReport.appendVerificationPoint(testName, message, false);
							junitReport.appendFailure(testName, message);
						}
					}
					if(type==LogLevel.PASSED){
						vpReport.appendVerificationPoint(testName, message, true);
					}
				}
				if(type==LogLevel.ERROR){
					if(message.contains("\n\n")){
						String[] trace = message.split("\n\n");
						vpReport.appendUnhandledException(testName);
						vpReport.appendStack(testName, trace[1]);
						junitReport.appendError(testName, trace[0]);
						junitReport.appendStack(testName, trace[1]);
					}else{
					}
				}
			}

		writeJSON();		
	}

	/**
	 * get class and method information when output log
	 * @return
	 */
	private static String getCallStamp(){
		String sClassInfo = "";
		String sTestCaseInfo = "";
		String sMethod = "";	
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		String sClass=null;
		for(int i= 0; i < stacks.length; i++){
			sClass=stacks[i].getClassName();
			if(!sClass.contains(AutomationLogger.class.getPackage().getName()) 
					&& !sClass.contains("Throwable")
					&& !sClass.contains(Logger.class.getPackage().getName()) 
					&& !sClass.contains("BaseAssert")
					&& !sClass.contains("WidgetInterpreter")){
				sClassInfo = "\t\t\t\t[from line: " + stacks[i].getLineNumber() + " in method: " +  stacks[i].getClassName() + "." + stacks[i].getMethodName() + "]";
				for(int x = i; x < stacks.length; x++){
					sMethod = stacks[x].getMethodName();
					if(sMethod.equalsIgnoreCase("testMain") && !sClassInfo.contains("testMain")){
						sTestCaseInfo = "  <br>[from line: "+stacks[x].getLineNumber() + " in test case: " + stacks[x].getClassName() +"]";
					}
				}
				break;
			}
		}
		return sClassInfo + sTestCaseInfo;
	}
	
	public void setRerunMode(boolean b){
		rerunMode = b;
	}

	public void setJUnitReport(boolean b) {
		junitReporting = b;
	}
	
	public void setJUnitRetestReplace(boolean b) {
		junitRetestReplace = b;
	}
}
