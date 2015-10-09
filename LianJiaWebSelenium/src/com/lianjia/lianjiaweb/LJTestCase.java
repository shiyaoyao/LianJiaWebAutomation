package com.lianjia.lianjiaweb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.runner.RunWith;

import tasks.client.Client;
import tasks.web.authentication.LJLoginTasks;
import tasks.web.util.DateUtils;
import tasks.web.util.ResultsParser;
import tasks.web.util.SimpleFileIO;
import tasks.web.util.StringUtils;
import tasks.web.util.WindowManagement;
import appobjects.user.WebUser;
import appobjects.web.Browser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lianjia.BaseTestCase;
import com.lianjia.automation.core.LogLevel;
import com.lianjia.automation.core.Platform;
import com.lianjia.automation.core.web.WebBrowser;
import com.lianjia.lianjiaweb.users.WebUserDirectory;


@RunWith(LJTestRunner.class)
public abstract class LJTestCase extends BaseTestCase {
	private LJRunConfig config;
	private Properties properties;
	
	private String propfile;
	public Properties labelstrings;
	
	// USER LOG IN AND SWITCHING 	
	/**
	 * Log in the initial user at the beginning of a test case by username/password
	 * 
	 */
	public void logIn(WebUser user) {
		boolean success = false;
		
		success = LJLoginTasks.login(user);
		if(!success){
			log.warn("Login Failure. Retry.");
			WebBrowser.close();
			WebBrowser.loadUrl(getURL());
			success = LJLoginTasks.login(user);
		}
		
		assertTrue("Successfully logged in", success);
		
		try {
			WindowManagement.setBrowserFocus();
		} catch (IOException e) {
			//
		}
		WindowManagement.refreshAllWindows();
	}
	
	public void logOut() {
		boolean success = false;
		
		success = LJLoginTasks.logout();
		if(!success){
			log.warn("Logout Failure. Retry.");
			success = LJLoginTasks.logout();
		}
		
		assertTrue("Successfully logout", success);
		
		try {
			WindowManagement.setBrowserFocus();
		} catch (IOException e) {
			//
		}
		WindowManagement.refreshAllWindows();
	}
	
	// SET UP AND TEAR DOWN METHODS	
	protected void setProperties(LJRunConfig runConfig, Properties props, String file, Properties langprops) {
		config = runConfig;
		properties = props;
		propfile = file;
		labelstrings = langprops;
	}

	public void testSetUp() throws Throwable {
		// Set up BaseAssert for this test case
		resetTestCaseValues(this.getTestCaseId());
		
		getUserDirectory().getUsers();
		
		if(config.getProperty("disableConsoleLog").equalsIgnoreCase("true") || 
				(!config.getProperty("disableConsoleLog").equalsIgnoreCase("false") && config.getProperty("browser").toLowerCase().contains("explorer")))
					
		log.debug("Preparing Selenium...");
		Platform.setEngine(Platform.SELENIUMWD);

		if(Platform.isMac()){
			WindowManagement.hideMacOsDock();
		}
		
		String client_name = InetAddress.getLocalHost().getHostName();
		setWebBrowser(getEnvProperty("browser"));
		Client.open(this);
		WindowManagement.setBrowserFocus();
		
		if(Platform.isMac() && Browser.isChrome()){
			Browser.maximize();
		}
		
		Browser.handleIECertError();
		
		properties.setProperty("browsername", Browser.getName());
		properties.setProperty("browserversion", Browser.getVersion());
		
		log.info(new StringBuffer("CLIENTINFO; client-name=").append(client_name).append(";")
	            .append("client-os=").append(Platform.getOSNameAndVersion()).append(";")
	            .append("browser-name=").append(properties.getProperty("browsername", "Unknown")+" "+properties.getProperty("browserversion", "Unknown")).append(";")
	            .append("automation=").append(config.getProperty("buildlabel")).append(";")
	            .append("engine=").append(Platform.getEngineName()).append(";")
	            .append("selenium=").append(getEnvProperty("coreversion")) );
		
		WindowManagement.refreshAllWindows();
	}

	public void testTearDown() throws Throwable {
		try {
			try{
				//LJCommonHeadTasks.logOut();
			} catch (Exception e) {
				//
			}
			WindowManagement.showMacOsDock();
			try {
				WebBrowser.shutdown(true);
			} catch (Exception e) {
				log.error(e);
			}
			if (Platform.isWindows()) {
				String browserImage = "iexplore.exe";
				if(WebBrowser.isChrome())
					browserImage = "chrome.exe";
				else if(WebBrowser.isFirefox())
					browserImage = "firefox.exe";
				
				// THE TARGET BROWSER PROCESS SHOULD NO LONGER EXIST. IF FOUND; KILL IMAGE TASK
				if (Platform.runCommandLineAndReturn("tasklist /fi \"IMAGENAME eq "+browserImage+"\"").length > 1) {
					System.out.println("Browser ("+browserImage+"). Kill it");
					log.info( "("+browserImage+") still running. Kill it");
					Platform.runCommandLineAndReturn("taskkill /f /im "+browserImage);
				}
			} 
		} catch (Exception e) {
			log.error(e);
		} finally {
			// ALL WEBDRIVER IMAGES SHOULD BE CLOSED. IF STILL FOUND; KILL IMAGE TASK
			killWebDriver();
		}
		
		logResultStats();
		logPassFail();		 
	}

	public void logResultStats() throws Throwable {
		String _eol = System.getProperty("line.separator");
		int count = 1;
		String sJSONres = "logs/current/results.json";
		String sResults = "logs/current/results.txt";
		String sBuild = "logs/current/build.txt";
		String sHeading = "# TEST SUITE STATS";
		String sTitle =  "title";
		String sTotal = "totalTestcases";
		String sPassed = "totalTestcasesPassed";
		String sFailed = "totalTestcasesFailed";
		String sSkipped = "totalTestcasesSkipped";
		String sPercentage = "totalTestcasesPercentage";
		String sElapsedTime = "elapsedTime";
		String sBuildInfo = "";
		ArrayList<String> failedTestCasesAndPropFile = new ArrayList<String>();
		
		sBuildInfo = SimpleFileIO.getFileContents(sBuild);
		sBuildInfo = sBuildInfo.isEmpty() ? "NO BUILD INFO" : sBuildInfo;
		
		try {
			failedTestCasesAndPropFile = ResultsParser.getFailedTestCasesWithPropFile(false);
		} catch(Exception e) {
			//
		}
		JsonObject job = new JsonParser().parse(SimpleFileIO.getFileContents(sJSONres)).getAsJsonObject();
		
		String sSuiteName = getJSONprop(job,sTitle);
		sSuiteName = sSuiteName.startsWith("Non-Suite") ? getClass().getName() : sSuiteName;
		
		StringBuilder sPost = new StringBuilder();
		sPost.append(sHeading + _eol);
		sPost.append("testSuite=" + sSuiteName + _eol);
		sPost.append("totalTestCases=" + getJSONprop(job,sTotal) + _eol);
		sPost.append("testsPassed=" + getJSONprop(job,sPassed) + _eol);
		sPost.append("testsFailed=" + getJSONprop(job,sFailed) + _eol);
		sPost.append("testsSkipped=" + getJSONprop(job,sSkipped) + _eol);
		sPost.append("passPercentage=" + getJSONprop(job,sPercentage) + _eol);
		try {
			sPost.append("elapsedTime=" + getJSONprop(job,sElapsedTime) + _eol);
		} catch (Exception e) {
			//
		}
		if(failedTestCasesAndPropFile.size() > 0) {
			sPost.append(_eol);
			sPost.append("# FAILED TEST CASES" + _eol);
			for(String ftc : failedTestCasesAndPropFile) {
				sPost.append("failure-" + (count++) + "=" + ftc + _eol);
			}
			sPost.append(_eol);
			
		}

		Writer output = new BufferedWriter(new FileWriter(sResults));
		output.write(sPost.toString() + _eol + sBuildInfo +_eol);
		output.close();		
	}
	
	private String getJSONprop(JsonObject json, String sProp) {
		String value = "";
		try {
			value = json.get(sProp).getAsString();
		} catch (Exception e) {}
		return value;
	}
	
	public void logPassFail() throws Throwable {
		String JSONfile = System.getProperty("user.dir") + "/logs/current/results.json";
		JSONfile = JSONfile.replace("/", System.getProperty("file.separator"));
		String outFile = System.getProperty("user.dir") + "/logs/current/PassFailResults.txt";
		outFile = outFile.replace("/", System.getProperty("file.separator"));
		HashMap<String, String> hmTests = new HashMap<String,String>();
		String sTitle = "";
		Pattern pattern = Pattern.compile("\\.\\d+$");
		Matcher dotNum;
		String _status = "";
		
		JsonObject job = new JsonParser().parse(SimpleFileIO.getFileContents(JSONfile)).getAsJsonObject();
		JsonArray JSONtests = new JsonArray();
		JsonArray JSONretest = new JsonArray();
		
		try {
			JSONtests = job.get("tests").getAsJsonArray();
			JSONretest = job.get("retest").getAsJsonArray();
		} catch (Exception e) {
			//
		}

		// get all test cases that were run
		for(int f = 0; f < JSONtests.size(); f++) {
			job = JSONtests.get(f).getAsJsonObject();
			//Temporarily added time data here until full time data is available
			String timestamp = job.get("time").getAsString().toLowerCase();
			long timelength = (Integer.parseInt(timestamp.substring(0,2)) * 60 * 60) + (Integer.parseInt(timestamp.substring(3,5)) * 60) + Integer.parseInt(timestamp.substring(6,8));
			_status = job.get("status").getAsString().toLowerCase();
			hmTests.put(job.get("title").getAsString(),_status + ", "+ timelength);
		}
		
		// replace status of rerun test cases with rerun status
		for(int x = 0; x < JSONretest.size(); x++) {
			job = JSONretest.get(x).getAsJsonObject();
			sTitle = job.get("title").getAsString();
			dotNum = pattern.matcher(sTitle);
			if(dotNum.find())
				sTitle = sTitle.replace(dotNum.group(), "");
			String timestamp = job.get("time").getAsString().toLowerCase();
			long timelength = (Integer.parseInt(timestamp.substring(0,2)) * 60 * 60) + (Integer.parseInt(timestamp.substring(3,5)) * 60) + Integer.parseInt(timestamp.substring(6,8));
			if(hmTests.containsKey(sTitle)){
				_status = job.get("status").getAsString().toLowerCase();
				hmTests.put(sTitle,_status + ", "+ timelength);
			}
		}
		StringBuilder sb = new StringBuilder();
		
		for(String key : hmTests.keySet()){
			sb.append(key + ", "+ hmTests.get(key)).append(System.getProperty("line.separator"));
		}
		
		Writer allout = new BufferedWriter(new FileWriter(outFile));
		allout.write(sb.toString());
		allout.close();
	}
	
	// PROPERTY GETTERS AND TEST CASE INFORMATION GETTERS
	public final String getTestDescription() {
		return new StringBuffer(getTestCaseId()).append(isRerun() ? " ::RERUN:: " : " - ").append(getDescription()).toString();
	}
	
	/**
	 * Retrieves the property from the data file and replaces supported
	 * variables.
	 * 
	 * @param property
	 *            - The name of the property in the data file.
	 * @return - Returns a string if the property exists or null if it does not.
	 */
	public final String getProperty(String property) {
		String prop = properties.getProperty(property);
		return replaceVariables(prop == null ? "" : prop);
	}

	/**
	 * Retrieves to property from the test case data property file. Assigns <code>sDefault</code>
	 * if the property isn't defined in the property file.
	 * @param property - The name of the property in the test case data file.
	 * @param sDefault - The value to return if <code>property</code> is undefined
	 * @return - String value of the specified test case data property, or <code>sDefault</code>
	 */
	public final String getProperty(String property, String sDefault) {
		String prop = properties.getProperty(property);
		return replaceVariables(prop == null ? sDefault : prop);
	}

	public List<String> getPropertyFiles(String path) {
		return config.getPropertiesFiles(path);
	}
	
	/**
	 * Gets the test case class name
	 * 
	 * @return
	 */
	public final String tcName() {
		return getClass().getSimpleName();
	}

	/**
	 * Replaces variables in the format <b>${varName}</b> and returns the new
	 * string. <br>
	 * Supported variable names:
	 * <ul>
	 * <li>Date - The current date</li>
	 * </ul>
	 * 
	 * @param str
	 *            The string with variables.
	 * @return The new string with variables replaced.
	 */
	private String replaceVariables(String str) {
		if (str == null)
			return null;
	
		String maskVar = ".*(\\$\\{.*?\\}).*";
		String maskVal = "\\$\\{.*?\\}";
		String holder = str.replaceAll(maskVar, "$1").toLowerCase();
		if(holder.equalsIgnoreCase(str))
			return str;
		
		String val = StringUtils.randomizeString(holder.substring(2, holder.length() - 1));
		
		if (holder.equals("${testcase}") || holder.equals("${testname}"))
			val = tcName();
		else if (holder.contains("${time}"))
			val = DateUtils.dateToString(Calendar.getInstance().getTime(), "HHmmss.SSS");
		else if (holder.equals("${date}"))
			val = DateUtils.dateToString(Calendar.getInstance().getTime(), "yyyyddMM'T'HHmm");
		else if (holder.equals("${random}"))
			val = StringUtils.makeName(false).replace(" ", "");
		else
			val = StringUtils.randomizeString(holder.substring(2, holder.length() - 1));
		
		return str.replaceAll(maskVal, "["+val+"]");
	}

	/**
	 * @return The target URL for this test case.
	 */
	public final String getURL() {
		return getEnvProperty("url");
	}

	
	/**
	 * @return The user directory for this test
	 */
	public final WebUserDirectory getUserDirectory() {
		return config.getUserDirectory();
	}

	@Override
	public String getEnvProperty(String name) {
		return config.getProperty(name);
	}
	
	public void setEnvProperty(String name, String value) {
		config.setProperty(name, value);
	}

	/**
	 * @return The description for this test case.
	 */
	public abstract String getDescription();

	public Properties getConfigProperties() {
		return config.getConfigProps();
	}

	public Properties getTestCaseProperties() {
		return properties;
	}

	public final String getLocalString(String property) {
		String prop = labelstrings.getProperty(property);
		return replaceVariables(prop == null ? "" : prop);
	}

	// LOGGING METHODS
	
	public void listConfigProperties() {
		Properties configprops = config.getConfigProps();
		for (Object k : configprops.keySet()) {
			log.info("Config prop: " + k.toString() + " :: " + configprops.get(k).toString());
		}
	}

	public void listProperties() {
		for (Object k : properties.keySet()) {
			log.info("Test case prop: " + k.toString() + " :: " + properties.get(k).toString());
		}
	}
	
	/**
	 * Logs a formatted information message to the HTML log. The message will be forced to
	 * upper case and centered within the table cell.
	 * <p>Surrounding characters in the message with angle brackets (<...>) will preserve their 
	 * case as passed to this method and will NOT force them to upper case.</p>
	 * @param sMessage
	 */
	public void logTestPlan(String sMessage) {
		String[] messages = sMessage.split("<");
		String sParsed = "";
		int iEnd = -1;
		for (String s : messages) {
			iEnd = s.indexOf('>');
			sParsed += iEnd < 0 ? s.toUpperCase() : s.substring(0, iEnd)+s.substring(iEnd+1).toUpperCase();
		}
		log.log(LogLevel.TESTPLAN, "<div style=\"width: 100%; text-align: center;\">**** "+sParsed + " ****</div>");
	}
	
	public void logBanner(String sMessage) {
		logTestPlan(sMessage);
	}
	
	
	// PRIVATE METHODS	
	protected void setWebBrowser(String sBrowserName) {
		config.getConfigProps().setProperty("browser", sBrowserName);
		String sName = sBrowserName.toLowerCase();
		if(sName.contains("explorer")){
			WebBrowser.setBrowserToIE();
		} else if (sName.contains("firefox")){
			if(Platform.isMac()){
				WebBrowser.setWebBrowser("Firefox");
			} else 
				WebBrowser.setBrowserToFirefox();
		} else if (sName.contains("chrome")){
			WebBrowser.setBrowserToChrome();
		} else if (sName.contains("safari")){
			WebBrowser.setBrowserToSafari();
		} else {
			log.warn("'"+sBrowserName+"' is not supported. Setting to Google Chrome");
			WebBrowser.setBrowserToChrome();
		}
	}
	
	private void killWebDriver() {
		if(Platform.isWindows()){
			int count = 50;
			String kill = "\""+System.getProperty("user.dir")+"\\lib\\killdriver.bat\"";
			String[] res = Platform.runCommandLineAndReturn(kill);
			while(res.length > 0 && count-- >0)
				res = Platform.runCommandLineAndReturn(kill);
		}
		else if(Platform.isMac() || Platform.isLinux()){
			String macBrowser = WebBrowser.getWebBrowserName();
			macBrowser = macBrowser.substring(macBrowser.indexOf(' ')+1);
			System.out.println("Kill Browser: "+macBrowser);
			Platform.runCommandLineAndReturn(System.getProperty("user.dir")+"/lib/killdriver.sh "+macBrowser);
		}
	}
	
}
