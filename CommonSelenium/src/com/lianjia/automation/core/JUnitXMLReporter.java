package com.lianjia.automation.core;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for generating JUnit like XML reports. XML format designed using
 * https://svn.jenkins-ci.org/trunk/hudson/dtkit/dtkit-format/dtkit-junit-model/src/main/resources/com/thalesgroup/dtkit/junit/model/xsd/junit-4.xsd
 */
public class JUnitXMLReporter {

	private static final String PASS_FAIL_RESULTS_XML = "PassFailResults.xml";
	private static final String JUNIT_RESULTS_XML = "PassFailResultsNoVP.xml";
	public static final String VP_DELIMITER = ":|";
	private static final String ATT_CLASSNAME = "classname";
	private static final String ATT_MESSAGE = "message";
	private static final String ATT_NAME = "name";
	private static final String ATT_STATUS = "status";
	private static final String ATT_TESTS = "tests";
	private static final String ELEMENT_ERROR = "error";
	private static final String ELEMENT_FAILURE = "failure";
	private static final String ELEMENT_SKIPPED = "skipped";
	private static final String ELEMENT_STACKTRACE = "system-err";
	private static final String ELEMENT_TESTCASE = "testcase";
	private static final String ELEMENT_TESTSUITE = "testsuite";
	private static final String ELEMENT_TESTSUITES = "testsuites";
	private static final String ELEMENT_OUT = "system-out";
	private Document docXML;
	private File logDir;
	private boolean bReplaceWithRetest = false;
	private boolean vpMode;

	/**
	 * Default Constructor. If this fails, check your configuration for the DocumentBuilderFactory.
	 */
	public JUnitXMLReporter(boolean vpMode){
		try {
			this.vpMode = vpMode;
			docXML = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Appends a error element to the described testcase. If one already exists it adds the message at the end of the existing message.
	 * @param testName The name of the test case. Class name can also be used.
	 * @param message The error message
	 */
	public void appendError(String testName, String message){
		Element test = getTest(testName);
		if(test!=null){
			if (test.hasChildNodes()){
				appendMessage((Element)test.getFirstChild(),message);
			}else{
				Element error = docXML.createElement(ELEMENT_ERROR);
				error.setAttribute(ATT_MESSAGE, message);
				test.appendChild(error);
			}
			test.setAttribute(ATT_STATUS, "error");
		}else{
			System.out.println("Could Not Append error to Testcase - "+testName);
		}
	}
	
	/**
	 * Appends a failure element to the described testcase. If one already exists it adds the message at the end of the existing message.
	 * @param testName The name of the test case. Class name can also be used.
	 * @param message The failure message
	 */
	public void appendFailure(String testName, String message){
		Element test = getTest(testName);
		if(message.contains(VP_DELIMITER)){
			message = message.substring(message.indexOf(VP_DELIMITER)+VP_DELIMITER.length());
		}
		if(test!=null){
			if (test.hasChildNodes()){
				appendMessage((Element)test.getFirstChild(),message);
			}else{
				Element failure = docXML.createElement(ELEMENT_FAILURE);
				failure.setAttribute(ATT_MESSAGE, message);
				test.appendChild(failure);
				test.setAttribute(ATT_STATUS, "failed");
			}
		}else{
			System.out.println("Could Not Append Failure to Testcase - "+testName);
		}
	}
	
	public void appendStack(String testName, String message) {
		Element stack = docXML.createElement(ELEMENT_STACKTRACE);
		stack.setTextContent(message);
		Element test = getTest(testName);
		if(test!=null){
			test.appendChild(stack);
		}else{
			System.out.println("Could Not Append Stack to Testcase - "+testName);
		}
	}
	
	public void replaceWithRetest(boolean bReplace){
		bReplaceWithRetest = bReplace;
	}

	/**
	 * Appends a testcase element to the first testsuite element in the document.
	 * Assumes all parent elements are already in the document
	 * @param className The class name of the test case (com.lianjia.project.category.TestName)
	 */
	public void appendTest(String className){
		if(vpMode){
			Element test = docXML.createElement(ELEMENT_TESTSUITE);
			test.setAttribute(ATT_NAME, className);
			test.setAttribute(ATT_TESTS, "0");
			Element suite = (Element)getTestSuite();
			suite.appendChild(test);
			//Increment tests attribute
			suite.setAttribute(ATT_TESTS, String.valueOf((Integer.parseInt(suite.getAttribute(ATT_TESTS))+1)));
		}else{
			String name = className.substring(className.lastIndexOf('.')+1);
			Element test = docXML.createElement(ELEMENT_TESTCASE);
			test.setAttribute(ATT_NAME, name);
			test.setAttribute(ATT_STATUS, "skipped");
			test.setAttribute(ATT_CLASSNAME, className);
			Element suite = (Element)getTestSuite();
			suite.appendChild(test);
			//Increment tests attribute
			suite.setAttribute(ATT_TESTS, String.valueOf((Integer.parseInt(suite.getAttribute(ATT_TESTS))+1)));
		}

		if(className.contains("RETEST") && bReplaceWithRetest){
			Element _test = getTest(className.replace("RETEST", ""));
			if(_test != null) {
				if(_test.getParentNode()!=null){
					System.out.println("Replacing first pass results for "+className+" with RETEST results");
					Element p = (Element) _test.getParentNode();
					p.removeChild(_test);
				}
				
			}
		}		
	}

	/**
	 * Appends a testsuite element to the document under the root (testsuites)
	 * @param name The name of the testsuite
	 */
	public void  appendTestSuite(String name){
		if(vpMode){
			Element suite = docXML.createElement(ELEMENT_TESTSUITES);
			suite.setAttribute(ATT_NAME, name);
			suite.setAttribute(ATT_TESTS, "0");
			docXML.appendChild(suite);
		}else{
			Element suite = docXML.createElement(ELEMENT_TESTSUITE);
			suite.setAttribute(ATT_NAME, name);
			suite.setAttribute(ATT_TESTS, "0");
			getRoot().appendChild(suite);
		}
	}
	
	/**
	 * Fills in the total time for the testsuite and writes the XML file.
	 * @param time
	 */
	public void endReport(String time) {
		Element suite = getTestSuite();
		suite.setAttribute("time", time);
		publishReport();
	}
	
	/**
	 * Resolves an unfinished test and adds the execution time. 
	 * Appended testcases start with the status of skipped. If the test produces a failure
	 * the status is changed to failed. If there are no failures, errors, or skipped elements,
	 * the test is marked as passed. This method also writes to the XML file.
	 * @param testName The name of the test case. Class name can also be used.
	 * @param time The execution time of the test
	 */
	public void resolveTest(String testName, String time){
		Element test = getTest(testName);
		if(!test.hasChildNodes()){
			test.setAttribute(ATT_STATUS, "passed");
		}
		test.setAttribute("time", time);
		publishReport();
	}

	/**
	 * Sets the output directory for the XML file
	 * @param logDir A File that points to the directory to out put the XML file
	 */
	public void setLogDir(File logDir){
		this.logDir = logDir;
	}

	/**
	 * Marks the described testcase as skipped
	 * @param testName The name of the test case. Class name can also be used.
	 */
	public void skipTest(String testName){
		if(!vpMode){
			Element test = getTest(testName);
			test.appendChild(docXML.createElement(ELEMENT_SKIPPED));
		}
	}
	
	/**
	 * Creates the root element and writes the XML file.
	 */
	public void startReport(){
		appendRootElement();
		publishReport();
	}
	private void appendMessage(Element element, String message) {
		String newMessage = element.getAttribute(ATT_MESSAGE)+'\n'+message;
		element.setAttribute(ATT_MESSAGE, newMessage);
	}
	
	private void appendRootElement(){ 
		if(!vpMode){
			Element rootElement = docXML.createElement(ELEMENT_TESTSUITES);
			docXML.appendChild(rootElement);
		}
	}
	
	/**
	 * Returns the root element (testsuites)
	 * @return The first element of the XML Doc which will be the root element
	 */
	private Node getRoot() {
		return docXML.getFirstChild();
	}

	private Element getTest(String testName) {
		NodeList tests = null;
		Element ret = null;
		if(vpMode){
			tests = docXML.getElementsByTagName(ELEMENT_TESTSUITE);
		}else{
			tests = docXML.getElementsByTagName(ELEMENT_TESTCASE);
			if(testName.contains(".")){
				testName=testName.substring(testName.lastIndexOf('.')+1);
			}
		}
		for(int i = 0; i < tests.getLength(); i++){
			Element el = (Element) tests.item(i);
			if(el.getAttribute(ATT_NAME).equals(testName)){
				ret = el;
				break;
			}
		}
		if(ret==null){
			System.out.println("Could Not Find Testcase - "+testName);
		}
		return ret;
	}
	
	/**
	 * Returns the first testsuite element in the document
	 * @return The first child of the root element
	 */
	private Element getTestSuite() {
		if(vpMode){
			return (Element) getRoot();
		}else{
			return (Element) getRoot().getFirstChild();
		}
	}
	
	private void publishReport(){
		// write the content into xml file
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(docXML);
			StreamResult result = null;
			if(vpMode){
				result = new StreamResult(new File(logDir,PASS_FAIL_RESULTS_XML));
			}else{
				result = new StreamResult(new File(logDir,JUNIT_RESULTS_XML));
			}
			
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}finally{}

	}
	
	/**
	 * Appends a system out element to the described testcase. 
	 * @param testName The name of the test case. Class name can also be used.
	 * @param message The message
	 */
	public void appendOut(String testName, String message){
		Element test = getTest(testName);
		if(test!=null){
			appendOut(message, test);
		}else{
			System.out.println("Could Not Append Output Message to Testcase - "+testName);
		}
	}
	
	/**
	 * Appends a system out element to the provided testcase element
	 * @param message The message
	 * @param elemet The element
	 */
	private void appendOut(String message, Element element) {
		Element stack = docXML.createElement(ELEMENT_OUT);
		stack.setTextContent(message);
		element.appendChild(stack);
	}

	public void appendVerificationPoint(String testName, String message, boolean didPass){
		Element test = getTest(testName);

		if(test!=null){
			Element vp = docXML.createElement(ELEMENT_TESTCASE);
			vp.setAttribute(ATT_CLASSNAME, testName);
			if(message.contains("[VP:")||message.contains("[TC:")){
				message=message.substring(message.indexOf("]")+1).trim();
			}
			String[] messageParts = {"NO VP GIVEN", message};
			if(message.contains(VP_DELIMITER)){
				messageParts[0] = message.substring(0, message.indexOf(VP_DELIMITER));
				messageParts[1] = message.substring(message.indexOf(VP_DELIMITER)+VP_DELIMITER.length());
			}
			vp.setAttribute(ATT_NAME, messageParts[0]);
			appendOut(messageParts[1], vp);
			test.appendChild(vp);
			if(!didPass){
				vp.setAttribute(ATT_STATUS, "failed");
				vp.appendChild(docXML.createElement(ELEMENT_FAILURE));
			}
			test.setAttribute(ATT_TESTS, String.valueOf((Integer.parseInt(test.getAttribute(ATT_TESTS))+1)));
			
		}else{
			System.out.println("Could Not Append Verification Point to Testcase - "+testName);
		}
	}
	
	public void appendUnhandledException(String testName) {
		Element test = getTest(testName);

		if(test!=null){
			Element vp = docXML.createElement(ELEMENT_TESTCASE);
			vp.setAttribute(ATT_CLASSNAME, testName);

			vp.setAttribute(ATT_NAME, "Unhandled Exception");
			appendOut("An unhandled exception was thrown. Please see Error entry", vp);
			test.appendChild(vp);
			vp.setAttribute(ATT_STATUS, "failed");
			vp.appendChild(docXML.createElement(ELEMENT_ERROR));
			
		}else{
			System.out.println("Could Not Append Unhandled Exception to Testcase - "+testName);
		}
	}
}
