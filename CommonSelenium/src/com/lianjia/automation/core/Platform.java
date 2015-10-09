package com.lianjia.automation.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import com.lianjia.automation.CoreAutomation;
import com.lianjia.automation.core.loggers.control.CoreLogger;
import com.lianjia.automation.core.util.FileHelper;

public class Platform {
	// version number
	private static final String RELEASE_VERSION = Platform.class.getPackage().getImplementationVersion(); 
	
	public static void main(String[] args) {
		System.out.println("com.lianjia.automation.core release " + RELEASE_VERSION);
	}

	/**
	 * returns the version of the Core Automation Foundation Library release
	 */
	public static String getVersion()
	{
		return RELEASE_VERSION;
	}

	/**
	 * used to specify the if we should handle errors upon exception
	 */
	public static boolean handleErrorOnException = false;

	/**
	 * Operating System Name
	 */
	public static final String OS_NAME;

	/**
	 * Operating System Name
	 */
	static {
		OS_NAME = System.getProperty("os.name");
	}

	/**
	 * Operating System version
	 */
	public static final String OS_VERSION = System.getProperty("os.version");
	
	/**
	 * Java Vendor
	 */
	public static final String JAVA_VENDOR= System.getProperty("java.vendor");
	/**
	 * Java version
	 */
	public static final String JAVA_VERSION = System.getProperty("java.version");
	
	public static String IPAD_USER_AGENT = "\'\"Mozilla/5.0 (iPad; U; CPU OS 4_2_1 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5\"\'";
	public static String IPHONE_USER_AGENT = 	"\'\"Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_2_1 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148a Safari/6533.18.5\"\'";		
	public static String ANDROID_USER_AGENT = "\'\"Mozilla/5.0 (Linux; U; Android 0.5; en-us) AppleWebKit/522+ (KHTML, like Gecko) Safari/419.3\"\'";
	
	private static boolean isWindows = OS_NAME.startsWith("Windows");

	private static boolean isMac = OS_NAME.indexOf("OS X") != -1;

	private static boolean isLinux = OS_NAME.equals("Linux");

	private static String sLinuxVersionInfo = File.separator + "etc" + File.separator + "issue";

	private static String sWin7 = "6.1";

	private static boolean isDojoWorkaroundEnabled = true;
	
	public static boolean isDojoWorkaroundEnabled() {
		return isDojoWorkaroundEnabled;
	}

	public static void disableDojoWorkaround()
	{
		isDojoWorkaroundEnabled = false;
	}
	
	private static String SELENIUM_SERVER_HOSTNAME = "localhost";

	private static int SELENIUM_SERVER_PORT = 4444;
	
	/**
	 * returns true/false if the OS is Mac OS X
	 */
	public static boolean isMac() {
		return isMac;
	}

	/**
	 * returns true/false if the OS is Windows
	 */
	public static boolean isWindows() {
		return isWindows;
	}

	/**
	 * returns true/false if the OS is Linux
	 */
	public static boolean isLinux() {
		return isLinux;
	}

	/**
	 * returns the appropriate file separator for the system // \
	 */
	public static String getFileSeparator()
	{
		return System.getProperty("file.separator");
	}

	/**
	 * returns the current user home directory
	 */
	public static String getUserHome()
	{
		return System.getProperty("user.home");
	}

	public static String getProjectName()
	{
		int myCount = getCurrentProjectPath().split("/").length;
		return getCurrentProjectPath().split("/")[myCount -1];
	}
	
	/**
	 * returns string with the current project path
	 */
	public static String getCurrentProjectPath()
	{
		String myProjectPath = new File(".").getAbsolutePath();

		myProjectPath = myProjectPath.replace("\\", "/");

		if(myProjectPath.endsWith("."))
		{
			myProjectPath = myProjectPath.substring(0,myProjectPath.length()-1);    		
		}

		if(myProjectPath.endsWith("/"))
		{
			myProjectPath = myProjectPath.substring(0,myProjectPath.length()-1);    		
		}
		return myProjectPath;
	}

	/**
	 * Sleep for a specified number of seconds
	 * @param numberOfSecondsToSleep - number of seconds to sleep
	 */
	public static void sleep(double numberOfSecondsToSleep)
	{
		try {
			Thread.sleep((long) (numberOfSecondsToSleep * 1000));
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * Returns a string containing the local host name of the test client system
	 * @return
	 */
	public static String getLocalClientName()
	{
		try{
		return java.net.InetAddress.getLocalHost().toString();}
		catch(Exception e){return "Could not get local host client name";}
		
	}

	/**
	 * returns a human readable OS version
	 */

	public static String getOSDisplayVersion()
	{
		String osVersion = "Unknown";

		if(isMac)
		{
			osVersion = OS_VERSION;
		}

		if(isWin7())
		{
			osVersion = "7 (" + OS_VERSION + ")";
		}

		if(isLinux())
		{
			osVersion = OS_VERSION;
		}
		return osVersion;
	}

	/**
	 * returns the OS Name and Version
	 */
	public static String getOSNameAndVersion()
	{
		return OS_NAME + " " + getOSDisplayVersion();
	}

	/**
	 * returns the Java Vendor and Version
	 */
	public static String getJavaVendorAndVersion()
	{
		return JAVA_VENDOR + " " + JAVA_VERSION;
	}
	
	/**
	 * constant for setting SELENIUM WEB DRIVER as the engine
	 */
	public static final String SELENIUMWD = "SELENIUMWD";
	
	
	private static boolean isSeleniumWD = false;

	private static boolean isSeleniumSingleWindowMode = false;
	
	/**
	 * used to check if Selenium is running in Single Window Mode - this is not the default as it has compatibility problems with some applications
	 */
	public static boolean isSeleniumSingleWindowMode()
	{
		return isSeleniumSingleWindowMode;
	}
	
	/**
	 * used to set Selenium to run in Single Window Mode - this is not the default as it has compatibility problems with some applications
	 */
	public static void setSeleniumSingleWindowMode(boolean isSingleWindowMode)
	{
		isSeleniumSingleWindowMode = isSingleWindowMode;
	}
	
	/**
	 * returns true/false if the engine is set to Selenium WD (aka WebDriver)
	 */
	public static boolean isSeleniumWD()
	{
		return isSeleniumWD;
	}
	
	/**
	 * sets the engine name to Selenium WebDriver API (aka Selenium 2 API)
	 */
	public static void setEngineToSeleniumWebDriver()
	{
		setEngine(SELENIUMWD);
	}
	
	/**
	 * sets the engine name - Selenium or Rational Functional Tester
	 * Do not use Rational Functional Tester if you are running a pure java / selenium environment
	 * @param engineName - use the pre-defined constants for RFT or SELENIUM
	 */
	public static void setEngine(String engineName)
	{
		isSeleniumWD = true;
	}
	
	/**
	 * sets the selenium server port
	 * @param hostname - use '4444' for the default port 
	 */
	public static void setSeleniumPort(int port)
	{
		SELENIUM_SERVER_PORT = port;
	}
	
	/**
	 * gets the selenium server hostname
	 * @param hostname - use 'localhost' 
	 */
	public static int getSeleniumPort()
	{
		return SELENIUM_SERVER_PORT;
	}
	

	/**
	 * sets the selenium server hostname
	 * @param hostname - use 'localhost' to run locally (which is the default)
	 */
	public static void setSeleniumHostname(String hostname)
	{
		SELENIUM_SERVER_HOSTNAME = hostname;
	}
	
	/**
	 * gets the selenium server hostname
	 * @param hostname - use 'localhost' 
	 */
	public static String getSeleniumHostname()
	{
		return SELENIUM_SERVER_HOSTNAME;
	}

	/**
	 * returns the engine name - Selenium or Rational Functional Tester
	 * Do not use Rational Functional Tester if you are running a pure java / selenium environment
	 */
	public static String getEngineName() {
		String myEngine ="Selenium WebDriver";
		CoreLogger.debug("Platform.getEngineName() = '" + myEngine + "'");
		return myEngine;
	}


	/**Global string for the Embedded Browser*/
	public static String gsEmbedded = "embedded";
	
	/**Global Time-out variable - 1 Second*/
	public static int giPause1TO = 1;	

	/**Global Time-out variable - 2 Seconds*/
	public static int giPause2TO = 2;	

	/**Global Time-out variable - 5 Seconds*/
	public static int giShortTO = 5;	

	/**Global Time-out variable - 10 Seconds*/
	public static int giNormalTO = 10;	

	/**Global Time-out variable - 15 Seconds*/
	public static int giWaitTO = 15;	

	/**Global Time-out variable - 30 Seconds*/
	public static int giMedTO = 30;	

	/**Global Time-out variable - 60 Seconds*/
	public static int giLongTO = 60;	

	/**Global Time-out variable - 60 Seconds*/
	public static String gsNewline = System.getProperty("line.separator");

	// locators to use...	
	private static String defaultLocator = "XPATH";
	
	public static void setDefaultLocator(String locatorToUseByDefault)
	{
		if(locatorToUseByDefault.toLowerCase().equals("css"))
		{
			defaultLocator = "CSS";
		} else {
			defaultLocator = "XPATH";
		}
	}
	
	// future use to enable unit testing automation without performing actions...
	private static boolean actionsDisabled = false;
	public static boolean isPlatformInUnitTestMode()
	{
		return actionsDisabled;
	}
	public static void disableActions()
	{
		actionsDisabled = true;	
	}
	
	public static boolean isXPathDefaultLocatorType()
	{
		return defaultLocator.equals("XPATH");
	}
	public static boolean isCSSDefaultLocatorType()
	{
		return defaultLocator.equals("CSS");
	}


	private static boolean alwaysTestExists = true;
	public static void enableExistsCheck()
	{
		alwaysTestExists = true;
	}

	public static void disableExistsCheck()
	{
		alwaysTestExists = false;
	}

	public static boolean isExistsCheckEnabled()
	{
		return alwaysTestExists;
	}
	
	private static boolean useFuzzyFind = true;
	public static void enableFuzzyFind()
	{
		useFuzzyFind = true;
	}

	public static void disableFuzzyFind()
	{
		useFuzzyFind = false;
	}

	public static boolean isFuzzyFindEnabled()
	{
		return useFuzzyFind;
	}
		
	private static boolean useCache = false;
	public static void enableWidgetCache()
	{
		useCache = true;
	}

	public static void disableWidgetCache()
	{
		useCache = false;
	}

	public static boolean isWidgetCacheEnabled()
	{
		return useCache;
	}

	public static String getTempDirectory()
	{
		return System.getProperty("java.io.tmpdir");
	}
	
	public static String getLJIPAddress()
	{
		try {
			InetAddress myIPAddresses[] = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
			for (int i = 0; i < myIPAddresses.length; i++) {				
				if(myIPAddresses[i].getHostAddress().startsWith("9."))
					return myIPAddresses[i].getHostAddress();
			}
			// return a 10. address if we get this far
			for (int i = 0; i < myIPAddresses.length; i++) {
				if(myIPAddresses[i].getHostAddress().startsWith("10."))
					return myIPAddresses[i].getHostAddress();
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return "unknown";
	}
		
	/**
	 * Determines if the OS Vendor is Red Hat by examining 
	 * the contents of /proc/version
	 * 
	 * @return true id RED HAT, false otherwise
	 */
	public static boolean isRHEL() {
		boolean bRHEL = false;

		if (isLinux) {
			String [] versionInfo = FileHelper.getFileContentsAsListByUTF8(sLinuxVersionInfo);


			for (int i=0; i<versionInfo.length; i++) {
				if (versionInfo[i].toUpperCase().contains("RED HAT")) {
					bRHEL = true;
					break;
				}
			}
		}
		return bRHEL;
	}

	/**
	 * Determines if the OS Vendor is SLED by examining 
	 * the contents of /proc/version
	 * 
	 * @return true id SLED, false otherwise
	 */
	public static boolean isSLED() {
		boolean bSLED = false;

		if (isLinux) {
			String [] versionInfo = FileHelper.getFileContentsAsListByUTF8(sLinuxVersionInfo);


			for (int i=0; i<versionInfo.length; i++) {
				if (versionInfo[i].toUpperCase().contains("SUSE LINUX")) {
					bSLED = true;
					break;
				}
			}
		}
		return bSLED;
	}

	/**
	 * Determines if the OS Vendor is Ubuntu by examining 
	 * the contents of /proc/version
	 * 
	 * @return true id Ubuntu, false otherwise
	 */
	public static boolean isUBUNTU() {
		boolean bUBUNTU = false;

		if (isLinux) {
			String [] versionInfo = FileHelper.getFileContentsAsListByUTF8(sLinuxVersionInfo);


			for (int i=0; i<versionInfo.length; i++) {
				if (versionInfo[i].toUpperCase().contains("UBUNTU")) {
					bUBUNTU = true;
					break;
				}
			}
		}
		return bUBUNTU;
	}

	/**
	 * determine if the os is Win7 by examining os version
	 * @return
	 */
	public static boolean isWin7(){
		if(isWindows() && OS_VERSION.contains(sWin7)){
			return true;
		}else{
			return false;
		}
	}

	public static void sendMacKeyPress(String windowTitle, String keypress)
	{
		File tmpFile;
		try {
			
			tmpFile = File.createTempFile("file", Platform.isMac() ? ".sh" : ".au3");
			tmpFile.deleteOnExit();
			
	        BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile));
	        
	    	out.write("#!/bin/sh\n");
	    	out.write("/usr/bin/osascript <<EOF\n");
	    	out.write("tell application \"" + windowTitle+"\"\n");
	    	out.write("activate\n");
	    	out.write("delay 2\n");
	    	out.write("tell application \"System Events\" to keystroke " + keypress + "\n");
	    	out.write("end tell\n");
	    	out.close();	    	
			
			//Change permissions to execute newly created script
	    	Runtime.getRuntime().exec("chmod 755 "+tmpFile.getAbsolutePath());

	    	//Execute Script
	    	Runtime.getRuntime().exec(tmpFile.getAbsolutePath());
	    	
	    	Platform.sleep(3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public static Process runCommand(String command,boolean waitForCompletion)
	{
		return runCommand(command, waitForCompletion, false);
	}
	
	/**
	 * Execute command, optionally wait until command completed
	 * @param command - a specified system command
	 * @param waitForCompletion - true/false to wait for the command to complete
	 */
	public static Process runCommand(String command, boolean waitForCompletion,boolean logOutput)
	{
		try {
			CoreLogger.debug(command);
			Process myCommand;
			myCommand = Runtime.getRuntime().exec(command);
			if(waitForCompletion)
			{
				myCommand.waitFor();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(myCommand.getInputStream()));
				String line;
				while((line= in.readLine())!=null){
					CoreAutomation.Log.info(line);
				}
				return null;			
			} else {
				return myCommand;				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Execute command, waits until command completed
	 * @param command - a specified system command
	 */
	public static void runCommandAndWait(String command)
	{
		runCommand(command, true);
	}

	/**
	 * Execute command, waits until command completed
	 * @param command - a specified system command
	 */
	public static void runCommandLine(String command){
		runCommandLineAndReturn(command);
	}

	/**
	 * Execute command, wait until command completed and return all output
	 * @param command
	 * @return command output of each line, in String Array
	 */
	public static String[] runCommandLineAndReturn(String command) {
		return runCommandLineAndReturn(command, null, null);
	}

	/**
	 * Execute command, wait until command completed and return all output
	 * @param command - a specified system command
	 * @param envp - array of strings, 
	 * 			each element of which has environment variable settings in the format name=value, 
	 *		 	or null if the subprocess should inherit the environment of the current process.
	 * @param dir - the working directory of the subprocess, 
	 * 			or null if the subprocess should inherit the working directory of the current process.
	 * @return command output of each line, in array of string object
	 * @throws 
	 * SecurityException - 
	 * 	If a security manager exists and its checkExec method doesn't allow creation of the subprocess <br>
	 *  NullPointerException - If command is null, or one of the elements of envp is null <br>
	 *  IllegalArgumentException - If command is empty
	 */
	public static String[] runCommandLineAndReturn(String command, String[] envp, File dir) {
		ArrayList<String> al = new ArrayList<String>();
		try {
			Process proc = Runtime.getRuntime().exec(command, envp, dir);
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while((line= in.readLine())!=null){
				al.add(line);
			}
		} catch(IOException e){
		} 

		return al.toArray(new String[0]);
	}

	/**
	 * Execute command, wait until command completed and return all output
	 * @param command
	 * @return command output of each line, in String Array
	 */
	public static String[] runCommandLineAndWait(String command) {
		return runCommandLineAndWait(command, null, null);
	}
	
	/**
	 * Execute command, wait until command completed and return all output
	 * @param command - a specified system command
	 * @param envp - array of strings, 
	 * 			each element of which has environment variable settings in the format name=value, 
	 *		 	or null if the subprocess should inherit the environment of the current process.
	 * @param dir - the working directory of the subprocess, 
	 * 			or null if the subprocess should inherit the working directory of the current process.
	 * @return command output of each line, in array of string object
	 * @throws 
	 * SecurityException - 
	 * 	If a security manager exists and its checkExec method doesn't allow creation of the subprocess <br>
	 *  NullPointerException - If command is null, or one of the elements of envp is null <br>
	 *  IllegalArgumentException - If command is empty
	 */
	public static String[] runCommandLineAndWait(String command, String[] envp, File dir) {
		return runCommandLineAndWait(command, envp, dir, false);
	}
	
	public static String[] runCommandLineAndWait(String command, String[] envp, File dir, boolean destroyProcess) {		
		ArrayList<String> al = new ArrayList<String>();
		try {
			Process proc = Runtime.getRuntime().exec(command, envp, dir);
			ArrayList<String> commandOutput = new ArrayList<String>();
			String outputLine;
			BufferedReader myInputStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader myErrorStream = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			while ((outputLine = myInputStream.readLine()) != null) {
				commandOutput.add(outputLine);
				System.out.println(outputLine);
			}
			myInputStream.close();
			while ((outputLine = myErrorStream.readLine()) != null) {
				commandOutput.add("STDERR: " + outputLine);
				System.out.println("STDERR: " + outputLine);
			}
			myErrorStream.close();
			proc.waitFor();
			CoreAutomation.Log.debug("NUMBER OF LINES IN OUTPUT = " +commandOutput.toArray().length);
			// destroy the process now that we're done
			if(destroyProcess)
			{
				proc.destroy();				
			}
			return commandOutput.toArray(new String[commandOutput.size()]);
		} catch(Exception e){
			e.printStackTrace();
		} 
		return al.toArray(new String[0]);
	}
	
	public static String[] runCommandLineAndWait(String[] command, String[] envp, File dir) 
	{
		return runCommandLineAndWait(command, envp, dir, false);
	}
	
	public static String[] runCommandLineAndWait(String[] command, String[] envp, File dir, boolean destroyProcess) 
	{
		String commandLineToRun = "";
		for (int i = 0; i < command.length; i++) {
			if(command[i].contains(" "))
			{
				command[i] = "\"" + command[i] + "\"";
			}
			commandLineToRun = commandLineToRun + " " + command[i];
		}
		CoreAutomation.Log.info("Running Command: '" + commandLineToRun + "'");

		ArrayList<String> al = new ArrayList<String>();
		try {
			Process proc = Runtime.getRuntime().exec(command, envp, dir);
			ArrayList<String> commandOutput = new ArrayList<String>();
			String outputLine;
			BufferedReader myInputStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader myErrorStream = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			while ((outputLine = myInputStream.readLine()) != null) {
				commandOutput.add(outputLine);
				System.out.println(outputLine);
			}
			myInputStream.close();
			while ((outputLine = myErrorStream.readLine()) != null) {
				commandOutput.add("STDERR: " + outputLine);
				System.out.println("STDERR: " + outputLine);
			}
			myErrorStream.close();
			proc.waitFor();
			CoreAutomation.Log.debug("NUMBER OF LINES IN OUTPUT = " +commandOutput.toArray().length);
			// destroy the process now that we're done
			if(destroyProcess)
			{
				proc.destroy();				
			}
			return commandOutput.toArray(new String[commandOutput.size()]);
		} catch(Exception e){
			e.printStackTrace();
		} 
		return al.toArray(new String[0]);
	}
	
	/**
	 * Returns the calling class. Specifically, traverses up the stack until
	 * finding a method invoked via reflection, or if no reflection is found,
	 * returns the topmost class on the stack.
	 * 
	 * @return The fully qualified class name
	 */
	public static String getCallingClassName()
	{	
		String className = null;
		String previousFrameClass = null;
		
		// Find a class with a method invoked via reflection
		for (StackTraceElement frame : new Throwable().fillInStackTrace().getStackTrace()) {
			if (className != null && ! className.contains("J9VMInternals") ) {
				previousFrameClass = className;
			}
			className = frame.getClassName();
			if (className.contains("sun.reflect.NativeMethodAccessorImpl")) {
				return previousFrameClass;
			}
		}
		return className;
	}
	
	/**
	 * Returns the location of the calling class (
	 * {@link Platform#getCallingClassName()})- either a jar file or a directory.
	 * Note this is not the path to the .class file itself.
	 * 
	 * @return File representing the class location
	 */
	public static File getCallingClassLocation() {
		try {
			Class<?> cls = Class.forName(getCallingClassName());
			URI uri = cls.getProtectionDomain().getCodeSource().getLocation().toURI();
			return new File(uri).getAbsoluteFile();
		} catch (ClassNotFoundException e) {
			return null;
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * @return True if the calling class ({@link Platform#getCallingClassName()}
	 *         ) was loaded from a jar file
	 */
	public static boolean isCallingClassRunningFromJar()
	{
		File tempFile = getCallingClassLocation();
		return tempFile == null ? false : !tempFile.isDirectory();
	}
	
	public static String getTopScriptName()
	{
		String className = "Unable to determine top script...";
		
		try {
			int i = new Throwable().fillInStackTrace().getStackTrace().length;
			className = new Throwable().fillInStackTrace().getStackTrace()[i-1].getClassName();				
		
		} catch (Exception e) {
		}		
		return className;
	}	
	
	/**
	 * returns a string with the current method name - perhaps this should move to the Platform class???
	 * @return - String - method name
	 */
	public static String getCallingMethodName()
	{
		int numberOfClasses = new Throwable().fillInStackTrace().getStackTrace().length;
		String myMethodName = "";
		int offset = 2;
		try {			
			for (int currentClass = 0; currentClass < numberOfClasses; currentClass++)
			{
				String tempMethodName = new Throwable().fillInStackTrace().getStackTrace()[currentClass].getMethodName();
				if(tempMethodName.contains("getCallingMethodName"))
				{
					offset = currentClass + 2;					
				}
				if(tempMethodName.contains("invoke0") )
				{
					offset = -1;					
					String myClass = new Throwable().fillInStackTrace().getStackTrace()[currentClass + offset].getClassName();
					return myClass + "." + new Throwable().fillInStackTrace().getStackTrace()[currentClass + offset].getMethodName() + "()";
				}
			}

			String myClass = new Throwable().fillInStackTrace().getStackTrace()[offset].getClassName();
			return myClass + "." + new Throwable().fillInStackTrace().getStackTrace()[offset].getMethodName() + "()";			
		} catch (Exception e) {
		}
		
		return myMethodName;
	}

	/**
	 * returns a string with the current method name - perhaps this should move to the Platform class???
	 * @return - String - method name
	 */
	public static String getCurrentMethodName()
	{		
		int numberOfClasses = new Throwable().fillInStackTrace().getStackTrace().length;
		String myMethodName = "";
				
		for (int currentClass = 0; currentClass < numberOfClasses; currentClass++)
		{
			if(new Throwable().fillInStackTrace().getStackTrace()[currentClass].getMethodName().contains("getCurrentMethodName"))
			{
				String myClass = new Throwable().fillInStackTrace().getStackTrace()[currentClass + 1].getClassName();
				return myClass + "." + new Throwable().fillInStackTrace().getStackTrace()[currentClass + 1].getMethodName() + "()";
			}
		}		
		return myMethodName;
	}
	
	public static String[] runShellCommand(String command)
	{
		return runShellCommand(command, null);
	}
	
	public static String[] runShellCommand(String command,String[] env)
	{
		try {
			ArrayList<String> commandOutput = new ArrayList<String>();
			String[] commandToRun = {"/bin/sh","-c",command};
			Process myProcess = Runtime.getRuntime().exec(commandToRun,env);
			String outputLine;
			BufferedReader myInputStream = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));
			BufferedReader myErrorStream = new BufferedReader(new InputStreamReader(myProcess.getErrorStream()));
			while ((outputLine = myInputStream.readLine()) != null) {
				commandOutput.add(outputLine);
				CoreAutomation.Log.debug(outputLine);
			}
			myInputStream.close();
			while ((outputLine = myErrorStream.readLine()) != null) {
				commandOutput.add(outputLine);
				CoreAutomation.Log.debug(outputLine);
			}
			myErrorStream.close();
			myProcess.waitFor();
			CoreAutomation.Log.debug("NUMBER OF LINES IN OUTPUT = " +commandOutput.toArray().length);
			return commandOutput.toArray(new String[commandOutput.size()]);
		}
		catch (Exception err) {
			err.printStackTrace();
		}
		return new String[] {""};				
	}
	
	/**
	 * Determines if the current OS is 64 bit.
	 * @return - Boolean - True if recognized as 64 else false
	 */
	public static boolean is64Bit(){
		String arch = "";
		if(isWindows){
			arch = System.getenv("PROCESSOR_ARCHITECTURE");
			String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
			
			if(arch.contains("64") || (wow64Arch != null && wow64Arch.contains("64"))){
				return true;
			}
		}else if(isMac){
			arch = Arrays.toString(runCommandLineAndWait("uname -v"));
			arch = arch.toUpperCase();
			
			if(arch.contains("X86_64") || arch.contains("AMD64") || arch.contains("I686")){
				return true;
			}else if(arch.contains("UNKNOWN")){
				arch = Arrays.toString(runCommandLineAndWait("file /usr/bin/file")).toUpperCase();
				if(arch.contains("64-BIT")){
					return true;
				}
			}
		}else if(isLinux){
			arch = Arrays.toString(runCommandLineAndWait("uname -i"));
			arch = arch.toUpperCase();
			
			if(arch.contains("X86_64") || arch.contains("AMD64") || arch.contains("I686")){
				return true;
			}else if(arch.contains("UNKNOWN")){
				arch = Arrays.toString(runCommandLineAndWait("file /usr/bin/file")).toUpperCase();
				if(arch.contains("64-BIT")){
					return true;
				}
			}
		}else{
			CoreAutomation.Log.debug("is32Bit:  Unable to recognize Operating System");
		}
		return false;
	}
	
	public static boolean isFileOnPath(String filename){
		String delimiter = isWindows() ? ";" : ":";
		StringTokenizer pathTokens = new StringTokenizer(System.getenv("PATH"),delimiter);
		File temp;
		String[] fileNames;
		while(pathTokens.hasMoreTokens()){
			temp = new File(pathTokens.nextToken());
			if(temp.isDirectory()){
				fileNames = temp.list();
				Arrays.sort(fileNames);
				if(Arrays.binarySearch(fileNames,filename) >= 0){
					return true;
				}
			}else if(temp.isFile()){
				if(filename.equals(temp.getName())){
					return true;
				}
			}
		}
		return false;
	}
}
