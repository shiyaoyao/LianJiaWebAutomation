package appobjects.web;

import com.lianjia.BaseAssert;
import com.lianjia.StringFetch;
import com.lianjia.automation.core.web.SeleniumCore;
import com.lianjia.automation.core.web.selenium.SeleniumTestObject;

/**
 * This class provides general helper methods for parsing locator pattern strings. These strings
 * use place holder constructs in the form ${<key_word>} to facilitate dynamic locator construction
 * at run time
 *
 */
public class LJAppObject {

	public static final String LOCALIZE = "${localize:";
	public static final String OBFUSCATION = "${obfuscation:";
	public static final String DEBUG = "${debug}";
	public static final String UNID = "${unid}";
	public static final String ELEMENT_ID = "${element_id}";
	public static final String ELEMENT_TEXT = "${element_text}";

	/**
	 * Extracts the unique id substring from the active tab
	 * 
	 * @return Unique identifier used for the current form or view
	 */
	public static String getDynamicId() {
		String sUnid = "";
		if(sUnid.matches(".*-\\d+")) {
			return sUnid;
		}
		
		if(sUnid.isEmpty()){
			return SeleniumTestObject.getNewWebObject(null).getId().replace("e-form-", "");
		}
		return "";
	}

	/**
	 * Parses the given locator string for the ${localize:} and ${unid} place holders. If ${unid} is 
	 * found in the locator pattern it is replaces with the dynamic unid.
	 * 
	 * @param sLoc Locator pattern
	 * @return Parsed locator string
	 */
	public static String parseLocator(String sLoc) {
		String sFinalLoc = sLoc;
		String sLabel = "";
		if (sLoc.contains(LOCALIZE)) {
			int iStart = sLoc.indexOf(LOCALIZE) + LOCALIZE.length();
			try {
				sLabel = sLoc.substring(iStart, sLoc.indexOf('}', iStart));
				sFinalLoc = sLoc.replace(LOCALIZE, "");
				sFinalLoc = sFinalLoc.replace(sLabel + "}", StringFetch.getString(sLabel.trim()));
			} catch (Exception e) {
				BaseAssert.assertTrue("Excpetion processing locator '" + sLoc + "'", false);
			}
		}

		if (sLoc.contains(OBFUSCATION))
			sFinalLoc = parseObfuscation(sFinalLoc);
		
		if (sLoc.contains(UNID)) {
			String sUnid = getDynamicId();
			sFinalLoc = parseIdPlaceHolder(sFinalLoc, sUnid);
		}

		return sFinalLoc;
	}

	/**
	 * Parses the given locator pattern for ${localize:} and ${unid} OR ${element_id}. If either
	 * of the id place holders is found in the locator patter, it is replaced by the given id string
	 * 
	 * @param sLoc Locator pattern
	 * @param sId Unid or element id
	 * @return Parsed locator string
	 */
	public static String parseLocator(String sLoc, String sId) {
		String sFinalLoc = sLoc;
		String sLabel = "";
		
		if (sLoc.contains(LOCALIZE)) {
			int iStart = sLoc.indexOf(LOCALIZE) + LOCALIZE.length();
			try {
				sLabel = sLoc.substring(iStart, sLoc.indexOf('}', iStart));
				sFinalLoc = sLoc.replace(LOCALIZE, "");
				sFinalLoc = sFinalLoc.replace(sLabel + "}", StringFetch.getString(sLabel.trim()));
			} catch (Exception e) {
				BaseAssert.assertTrue("Excpetion processing locator '" + sLoc + "'", false);
			}
		}

		if (sLoc.contains(OBFUSCATION))
			sFinalLoc = parseObfuscation(sFinalLoc);
		
		if (sId.length()>0) {
			sFinalLoc = parseIdPlaceHolder(sFinalLoc, sId);
		}

		return sFinalLoc;
	}

	/**
	 * Parses the given locator pattern for ${unid} or ${element_id}, substituting the
	 * given id string
	 * 
	 * @param sLoc Locator pattern
	 * @param sId id string
	 * @return Parsed locator string
	 */
	public static String parseIdPlaceHolder (String sLoc, String sId) {
		String sFinalLoc = sLoc;
		sId = sLoc.startsWith("css=") ? escapeDollarSign(sId) : sId;
		if(sLoc.contains(UNID)){
			sFinalLoc = sLoc.replace(UNID, sId);
		} else if (sLoc.contains(ELEMENT_ID)) {
			sFinalLoc = sLoc.replace(ELEMENT_ID, sId);
		}
		return sFinalLoc;
	}
	
	/**
	 * Parses the given locator pattern for ${element_text} substituting the given text string. This
	 * can be used when the locator pattern requires specific text, such as the text of a menu item. 
	 * This method will accept a string label (i.e. L_ACTION_NEW) and replace the ${element_text} place
	 * holder with the localize string value.
	 * 
	 * @param sLoc Locator pattern
	 * @param sText Text or string label
	 * @return Processed locator string
	 */
	public static String parseTextPlaceHolder(String sLoc, String sText){
		String sLocal = sText;
		if (sText.startsWith("L_"))
			sLocal = StringFetch.getString(sText);
		sLocal = sLocal.isEmpty() ? sText : sLocal;
		sLoc = parseApostrophe(sLoc, sText);
		return sLoc.replace(ELEMENT_TEXT, sLocal);
	}
	
	/**
	 * 
	 * Parses the given locator pattern for ${element_text} substituting the string value of the 
	 * given integer.
	 *  
	 * @param sLoc Locator pattern
	 * @param i Integer to substitute 
	 * @return Parsed locator string
	 */
	public static String parseTextPlaceHolder(String sLoc, int i) {
		String sText = String.valueOf(i);
		return parseTextPlaceHolder(sLoc, sText);
	}
	
	/**
	 * Parses the given locator pattern for ${unid} or ${element_id} and ${element_text}.
	 * 
	 * @param sLoc Locator pattern
	 * @param sId Unid or element id
	 * @param sText dynamic text
	 * @return Parsed locator string
	 */
	public static String parseIdAndText(String sLoc, String sId, String sText){
		String sFinalLoc = parseLocator(sLoc, sId);
		sFinalLoc = parseTextPlaceHolder(sFinalLoc, sText);
		return sFinalLoc;
	}
	
	/**
	 * Parses the given locator pattern for ${unid} or ${element_id} and ${element_text}. ${element_text} 
	 * will be replaced by the string value of the integer i.
	 * 
	 * @param sLoc Locator pattern
	 * @param sId Unid or element id
	 * @param i integer value
	 * @return
	 */
	public static String parseIdAndText(String sLoc, String sId, int i){
		String sFinalLoc = parseLocator(sLoc, sId);
		sFinalLoc = parseTextPlaceHolder(sFinalLoc, i);
		return sFinalLoc;
	}
	
	/**
	 * Wraps the given id string with css locator syntax. The method presumes sId to be a complete
	 * element id (e.g. e-actions-calendarview-regular). Calling this method with just a unid or a
	 * partial element id will have unpredictable results
	 * 
	 * @param sId element id
	 * @return
	 */
	public static String convertIdToCssLocator(String sId) {
		if (sId.startsWith("//") || sId.startsWith("dom="))
			return sId;
		if (!sId.startsWith("css="))
			sId = "css=#"+sId;
		return sId.replace("-$","-\\$");
	}
	
	public static String convertIdToXpathLocator(String sId) {
		if (sId.startsWith("//") || sId.startsWith("dom="))
			return sId;
		if (sId.startsWith("css="))
			sId = sId.replace("-$", "-\\");
		else 
			sId = "//*[@id='"+sId+"']";
		return sId;
	}
	/**
	 * Parses the given locator pattern for ${obfuscation:xxx}. If the current LianJiaWeb session is
	 * running a debug build, the obfuscation place holder is replaced with the un-obfuscated value
	 * of xxx. Otherwise the place holder is replaced by the obfuscated value, xxx.
	 * 
	 * @param sLoc locator pattern containing obfuscation place holder ${obfuscation:xxx} where xxx is an obfuscated string.
	 * @return A locator string with the propert context value of the obfuscated string.
	 */
	public static String parseObfuscation(String sLoc) {
		String sFinalLoc = sLoc;
		String sObfuscated = "";
		if (sLoc.contains(OBFUSCATION)) {
			int iStart = sLoc.indexOf(OBFUSCATION) + OBFUSCATION.length();
			try {
				sObfuscated = sLoc.substring(iStart, sLoc.indexOf('}', iStart));
				sFinalLoc = sLoc.replace(OBFUSCATION, "");
				sFinalLoc = sFinalLoc.replace(sObfuscated + "}", null);
			} catch (Exception e) {
				BaseAssert.assertTrue("Excpetion processing locator '" + sLoc + "'", false);
			}
		}
		return sFinalLoc;
	}
	
	/**
	 * Replaces the dollar sign character ($) with "\\$" in the given string. This is necessary when
	 * css locators are used in Selenium
	 * 
	 * @param sId string to parse
	 * @return sting with escaped dollar sign character
	 */
	private static String escapeDollarSign(String sId){
		if(sId.contains("\\$")) return sId;
		return  sId.replace("$","\\$");
	}

	private static String parseApostrophe(String sLoc, String sName){
		String escaped = sLoc;
		if(sName.contains("'"))
			escaped = escaped.replace("'","\"");
		return escaped;
	}
	
	public static boolean selectFrame(String sFrame) {
		try {
			SeleniumCore.getWebDriverBrowser().getWebDriverAPI().switchTo().frame(sFrame);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return false;
	}
	

	public static SeleniumTestObject mainFrame(){
		return new SeleniumTestObject("//html");
	}
}
