package tasks.web.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lianjia.automation.core.Platform;

public class ResultsParser {
	private static final Logger log = Logger.getLogger(ResultsParser.class);
	
	public static String _eol = System.getProperty("line.separator");
	public static String _slash = System.getProperty("file.separator");

	public static String resultpath = System.getProperty("user.dir") + "/logs/current/";
	public static String resultfile = System.getProperty("user.dir") + "/logs/current/results.json";
	public static String suitePassFail = resultpath+"SuitePassFailResults.txt";
	
	/**
	 * Returns a list of all test case names that failed in the most recent
	 * test suite execution
	 * @param bIncludeDataPropFile TODO
	 * @param bIncPropFileName TODO
	 * @return
	 * @throws Throwable
	 */
	public static ArrayList<String> getFailedTestCasesWithPropFile(boolean bIncludeDataPropFile) throws Throwable {
		resultfile = resultfile.replace("/", _slash);
		HashMap<String, String> mapTestAndData = new HashMap<String, String>();
		
		ArrayList<String> failures = new ArrayList<String>();
		mapTestAndData = getTestCasesAndData(true);
		Set<String> keys = mapTestAndData.keySet();
		
		for(String key : keys) {
			if(bIncludeDataPropFile)
				failures.add(key + " : " + mapTestAndData.get(key));
			else
				failures.add(key);
		}
		return failures;

	}
	
	public static ArrayList<String> getTestCases() throws Throwable {
		ArrayList<String> testCases = new ArrayList<String>();
		String filecont = SimpleFileIO.getFileContents(resultfile);
		JsonObject jsonObject = new JsonObject();
		jsonObject = new JsonParser().parse(filecont).getAsJsonObject();

		JsonArray JSONtests = new JsonArray();

		try {
			JSONtests = jsonObject.get("tests").getAsJsonArray();
			for(int f = 0; f < JSONtests.size(); f++) {
				jsonObject = JSONtests.get(f).getAsJsonObject();
				testCases.add(jsonObject.get("title").getAsString());
			}
		} catch (Exception e) {
			//
		}
		return testCases;
	}
	
	public static ArrayList<String> getFailedTestCases() throws Throwable {
		ArrayList<String> testCases = new ArrayList<String>();
		String filecont = SimpleFileIO.getFileContents(resultfile);
		String testCaseName = "";
		Pattern pattern = Pattern.compile("\\.\\d+$"); // Pattern for rerun suffix: .01
		Matcher dotNum;
		
		JsonObject jsonObject = new JsonObject();
		jsonObject = new JsonParser().parse(filecont).getAsJsonObject();
		JsonArray JSONtests = new JsonArray();
		JsonArray JSONretest = new JsonArray();
		
		try {
			JSONtests = jsonObject.get("tests").getAsJsonArray();
			JSONretest = jsonObject.get("retest").getAsJsonArray();
		} catch (Exception e) {
			//
		}
		try {
			for(int f = 0; f < JSONtests.size(); f++) {
				jsonObject = JSONtests.get(f).getAsJsonObject();
				if(jsonObject.get("status").getAsString().toUpperCase().matches("FAILED|SPR")) 
					testCases.add(jsonObject.get("title").getAsString());
			}
			
			for(int x = 0; x < JSONretest.size(); x++) {
				jsonObject = JSONretest.get(x).getAsJsonObject();
				if(jsonObject.get("status").getAsString().equals("Passed")) {
					testCaseName = jsonObject.get("title").getAsString();
					dotNum = pattern.matcher(testCaseName);
					if(dotNum.find())
						testCaseName = testCaseName.replace(dotNum.group(), "");
					if (testCases.contains(testCaseName)) {
						testCases.remove(testCaseName);
					} 
				}
			}
		} catch (Exception e) {
			//
		}
		return testCases;
	}
	
	public static HashMap<String, String> getTestCasesAndData(boolean bFailedOnly) throws Throwable{
		HashMap<String, String> mapTestAndData = new HashMap<String, String>();
		ArrayList<String> testCases = new ArrayList<String>();
		String filecont = SimpleFileIO.getFileContents(resultfile);
		JsonObject jsonObject = new JsonObject();
		jsonObject = new JsonParser().parse(filecont).getAsJsonObject();

		JsonArray JSONtests = new JsonArray();
		
		try {
			JSONtests = jsonObject.get("tests").getAsJsonArray();
		} catch (Exception e) {
			//
		}
		if(bFailedOnly)
			testCases = getFailedTestCases();
		else
			testCases = getTestCases();

		// Get the name of all test cases that initially failed
		for(int f = 0; f < JSONtests.size(); f++) {
			jsonObject = JSONtests.get(f).getAsJsonObject();
			if(testCases.contains(jsonObject.get("title").getAsString())){
				mapTestAndData.put(jsonObject.get("title").getAsString(),jsonObject.get("data").getAsString());
			}
		}
		
		return mapTestAndData;
	}
	
	public static ArrayList<String> getPassFail() {
		
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> passfail = new ArrayList<String>();

		JsonObject jObject;
		JsonArray jArray;
		jObject = new JsonParser().parse(SimpleFileIO.getFileContents(resultfile)).getAsJsonObject();
		jArray = jObject.get("tests").getAsJsonArray();
		names = parseJSONArray(jArray, "title");
		passfail = parseJSONArray(jArray, "status");
		
		if(names.size() != passfail.size()){
			log.warn("NAMES list and PASSFAIL list are different sizes");
			return results;
		}
		
		for(int x=0; x<names.size(); x++) {
			results.add(names.get(x)+", "+passfail.get(x).toLowerCase());
		}
		return results;

	}
	
	public static ArrayList<String> getTestCaseList(boolean bWithDescription) {
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> desc = new ArrayList<String>();
		
		JsonObject jObject;
		JsonArray jArray;
		jObject = new JsonParser().parse(SimpleFileIO.getFileContents(resultfile)).getAsJsonObject();
		jArray = jObject.get("tests").getAsJsonArray();
		
		String suite = parseJSONFile(resultfile, "title");
		results.add("# Test Suite: "+suite);
		names = parseJSONArray(jArray, "title");
		desc = parseJSONArray(jArray, "desc");
		
		if(names.size() != desc.size()){
			log.warn("NAMES list and PASSFAIL list are different sizes");
			return results;
		}
		
		for(int x=0; x<names.size(); x++) {
			if(bWithDescription)
				results.add(names.get(x)+", \""+desc.get(x)+"\"");
			else
				results.add(names.get(x));
		}
		return results;
	}
	
	public static ArrayList<String> getTestCaseList(String resultsPath, boolean bWithDescription) {
		String _resultpath = resultpath;
		String _resultfile = resultfile;
		
		if(resultsPath != null){
			_resultpath = SimpleFileIO.getAbsolutePath(resultsPath);
			_resultpath = _resultpath.endsWith("/") ? _resultpath : _resultpath + "/";
			_resultfile = _resultpath + "results.json";
		}
		
		_resultfile = SimpleFileIO.getAbsolutePath(_resultfile);
		
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> desc = new ArrayList<String>();
		
		JsonObject jObject;
		JsonArray jArray;
		jObject = new JsonParser().parse(SimpleFileIO.getFileContents(_resultfile)).getAsJsonObject();
		jArray = jObject.get("tests").getAsJsonArray();
		
		String suite = parseJSONFile(_resultfile, "title");
		results.add("# Test Suite: "+suite);
		names = parseJSONArray(jArray, "title");
		desc = parseJSONArray(jArray, "desc");
		
		if(names.size() != desc.size()){
			log.warn("NAMES list and PASSFAIL list are different sizes");
			return results;
		}
		
		for(int x=0; x<names.size(); x++) {
			if(bWithDescription)
				results.add(names.get(x)+", \""+desc.get(x)+"\"");
			else
				results.add(names.get(x));
		}
		return results;
	}
	
	public static String parseJSONFile(String sFile, String sJSONKey) {
		String sContents = SimpleFileIO.getFileContents(sFile).replace(_eol, "");
		JsonObject jo;
		try {
			jo = new JsonParser().parse(sContents).getAsJsonObject();
			return jo.get(sJSONKey).getAsString();
		} catch (Exception e) {
			return "";
		}
	}

	public static ArrayList<String> parseJSONArray(JsonArray jArray, String key) {
		ArrayList<String> results = new ArrayList<String>();
		JsonObject jObject;
		
		try {			
			for(int x = 0; x < jArray.size(); x++ ){
				jObject = jArray.get(x).getAsJsonObject();
				results.add(jObject.get(key).getAsString());
			}
		} catch (Exception e) {
			
		}
		
		return results;
	}

	public static ArrayList<String> getTestPlanInfo(String resultsPath) throws Throwable {
		String _suitePassFail = suitePassFail;
		String _resultpath = resultpath;
		String _resultfile = resultfile;
		
		if(resultsPath != null){
			_resultpath = SimpleFileIO.getAbsolutePath(resultsPath);
			_resultpath = _resultpath.endsWith("/") ? _resultpath : _resultpath + "/";
			_suitePassFail = _resultpath + "SuitePassFailResults.txt";
			_resultfile = _resultpath + "results.json";
		}
		
		_resultfile = SimpleFileIO.getAbsolutePath(_resultfile);
		_suitePassFail = SimpleFileIO.getAbsolutePath(_suitePassFail);
		
		ArrayList<String> loggedTestPlan = new ArrayList<String>();
		ArrayList<String> allTestCases = new ArrayList<String>();
		String message;
		
		File suitePF = new File(_suitePassFail);
		
		int retry = 60;
		while(!suitePF.exists() && retry-- > 0) Platform.sleep(1);
		
		if(suitePF.exists()){
			ArrayList<String> raw = SimpleFileIO.getLinesInFile(suitePF.getAbsolutePath(),true);
			for(String r : raw){
				if(!r.startsWith("com"))
					continue;
				allTestCases.add(r.substring(0,r.indexOf(',')));
			}
		} else {
			allTestCases.addAll(getTestCases());
		}
		
		JsonObject jObject = new JsonObject();
		JsonArray JSONmessages = new JsonArray();
		
		String suite = parseJSONFile(_resultfile, "title");
		loggedTestPlan.add("Test Suite : "+suite);
		loggedTestPlan.add("\n----------------------------------------------------------------------");
		
		int count = 1;
		String _status = "";
		for(String fTC : allTestCases){
			if(fTC.isEmpty())
				continue;
			try{
				jObject = new JsonParser().parse(SimpleFileIO.getFileContents(_resultpath+fTC)).getAsJsonObject();
			} catch (Exception e){
				continue;
			}
			// Log test case class name, pass/fail, and description
			_status = jObject.get("status").getAsString().toUpperCase();
			_status = _status.equals("SPR") ? "FAILED" : _status;
			loggedTestPlan.add("\n["+(String.format("%02d", count++))+"] "+fTC+": "+_status);
			loggedTestPlan.add("     ("+jObject.get("desc").getAsString()+")\n");
			
			// Get all the log posts from the test case's JSON results file
			JSONmessages = jObject.get("messages").getAsJsonArray();
			for(int f = 0; f < JSONmessages.size(); f++) {
				jObject = JSONmessages.get(f).getAsJsonObject();
				
				// If the post is logging a failure, see if it has an SPR notation
				// if so, add it to the summary string
				
				if(jObject.get("type").getAsString().equalsIgnoreCase("TESTPLAN")) {
					message = StringUtils.removeHtmlTags(jObject.get("message").getAsString()).toUpperCase();
					message = "        "+ message.replaceAll("\\*\\*\\*\\*", "");
					loggedTestPlan.add(message);
				}
			}
			loggedTestPlan.add("\n----------------------------------------------------------------------");
		}
		return loggedTestPlan;
	}
	
	public static ArrayList<String> getTestPlanInfo() throws Throwable {
		return getTestPlanInfo(null);
	}
	
	public static void logSuitePassFailResults(String timingFilePath) throws IOException {
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
			logFilteredPassFail(timingFilePath);
			FileUtils.writeLines(vp, ResultsParser.getTestPlanInfo());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static void logFilteredPassFail(String timingFilePath) throws IOException {
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
