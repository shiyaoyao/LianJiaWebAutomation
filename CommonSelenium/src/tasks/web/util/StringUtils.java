package tasks.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class StringUtils {
	private static final Logger log = Logger.getLogger(StringUtils.class);
	
	static String[] latin = {"41-5a", "61-7a"};
	static String[] supplimental = {"c0-cf", "d1-d6", "d8-dc", "df-ef", "f1-f6", "f8-fc", "ff"};
	static String[] extended = {"100-17e", "180-24f"};
	static String[] latinUpper = {"41-5a"};
	
	static ArrayList<Character> charlistUpper = new ArrayList<Character>();
	static ArrayList<Character> charlistLatin = new ArrayList<Character>();
	static ArrayList<Character> charlistSupplimental = new ArrayList<Character>();
	static ArrayList<Character> charlistExtended = new ArrayList<Character>();
	
	public static final String _eol = System.getProperty("line.separator");
	
		/**
		 * Replaces all non-breaking space characters with a regular space
		 * 
		 * @return Reformatted string;
		 */
		public static String replaceNonBreakingSpace(String sInString){
			String sOutString="";
			sOutString = sInString.replace('\u00a0','\u0020');
			sOutString = sOutString.replace(String.valueOf('\u200b'),"");
			return sOutString;
		}
		
		/**
		 * Trims leading and trailing whitespace, and replaces all whitespace characters
		 * with a regular space. \n \t and \r chars will be replaced with a space character
		 * 
		 * @param sInString
		 * @return Reformatted string
		 */
		public static String stringCleanUp(String sInString){
			String sOutString = replaceNonBreakingSpace(sInString);
			sOutString = sOutString.trim();
			sOutString = sOutString.replaceAll("\\s", " ");
			return sOutString;
		}
		
		/**
		 * Removes line breaks from string read from the data files for comparison purposes.
		 * @param sInString
		 * @return Reformatted string
		 */
		public static String removeLineBreak(String sInString){
			String sOutString = replaceNonBreakingSpace(sInString);
			sOutString = sOutString.trim().replace(_eol,"").replace("\n",""); // Remove EOL characters
			sOutString = sOutString.replaceAll("</{0,1}[brBR]+>", " "); // Replace HTML <br> with space
			return sOutString;
		}
		
		public static String removeHtmlTags(String sText) {
			sText = removeLineBreak(sText).replace("&nbsp;", " ");
			sText = sText.replaceAll("&\\w{1,6};", "");
			sText = sText.replaceAll("<.*?>", "");
			return sText;
		}
		
		public static String escapeHtmlTags(String sText) {
			sText = sText.replace("<", "&lt;").replace(">", "&gt;");
			sText = sText.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
			return sText;
		}
		
		public static String escapeRegex(String sRegex) {
			String reg = "\\[\\^\\$\\.\\|\\?\\*\\+\\(\\)\\\\";
			return sRegex.replaceAll("([" + reg + "])", "\\\\$1");
		}
		
		public static String cleanUpHtml(String html) {
			String clean = HtmlTagsToLowerCase(html);
			clean = clean.replace(_eol, "").replace("\n",""); // On windows the _eol may not pick up the \n char added by some browsers.
			
			String s;
			Pattern p = Pattern.compile("(<.*?>)");
			Matcher m = p.matcher(clean);
			while(m.find()) {
				s = m.group();
				clean = clean.replace(s, s.replace(": ",":"));
			}
			
			return clean;
		}
		
		public static String plainText(String sInText) {
			String plain;
			plain = removeLineBreak(sInText.replace(_eol," ").replace("\n"," "));
			plain = removeHtmlTags(plain);
			plain = replaceNonBreakingSpace(plain).trim();
			
			return plain;
		}
		
		/**
		 * Tests a string for sequential whitespace characters.
		 * Counts all whitespace as a space character. E.g. " \n" is considered a double-space
		 * 
		 * Useful for checking UI strings (menu items, labels, etc.) for erroneous space characters
		 * 
		 * @param sInString
		 * @return true if two or more sequential whitespace characters 
		 */
		public static boolean containsDoubleSpaces(String sInString){
			String s = stringCleanUp(sInString);
			return s.contains("  ")?true:false;
		}
		
		/**
		 * Removes douple spaces from string.
		 * Counts all whitespace as a space character. E.g. " \n" is considered a double-space
		 * 
		 * Useful for checking UI strings (menu items, labels, etc.) for erroneous space characters
		 * 
		 * @param sInString
		 * @return true if two or more sequential whitespace characters 
		 */
		public static String removeDoubleSpaces(String sInString){
			String s = stringCleanUp(sInString);
			if(s.contains("  "))
				s=s.replace("  ", " ");
			return s;
		}
		
		/**
		 * Test for the string pattern ": ". Return true ONLY if the pattern matches the FIRST occurrence of ":"
		 * 
		 * Useful for checking prefix formatting of meeting notices.
		 * 
		 * @param sInString
		 * @return true if the first ':' is followed by a space
		 */
		public static boolean spaceAfterPrefix(String sInString){
			String s =stringCleanUp(sInString);
			int i = s.indexOf(':');
			if (i<0){
				return false;
			}
			return (i==s.indexOf(": "))?true:false;
		}
		
		public static boolean equalsIgnoreSpaces(String s1, String s2){
			s1=stringCleanUp(s1).replace(" ", "");
			s2=stringCleanUp(s2).replace(" ", "");
			return s1.equals(s2);
		}
		
		/**
		 * Returns a randomized version the given string based on the seed integer.</br>
		 * Randomization is restricted to the Basic Latin and Latin-1 Supplemental character ranges. <br><br>
		 * 
		 * The method preserves the case of the original characters as well as the character range <br>
		 * (Basic for Basic, Supplemental for Supplemental). Upper case and lower case versions of<br>
		 * a given character will be replaced with case sensitive versions of the randomized character.<br><br>
		 * &nbsp;&nbsp;For Example: <br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;original: Aa&#xc8;&#xe8;<br>
		 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;randomized: Qq&#xc7;&#xe7;<br><br>
		 * Non-alpha characters are not affected. E.g. numbers, punctuation (including apostrophe and hyphen), <br> 
		 * white space, symbols, and any characters beyond Unicode U+00FF. </br></br>
		 * Use this method to avoid duplicating string patterns between test runs that may yield ambiguous results.<br> 
		 * Particularly useful for tests that perform searches, or generating unique contact names.
		 * 
		 * @param sStringToRandonize String to randomize
		 * @param seed Randomizing seed
		 * @return Randomized string.
		 */
		public static String randomize(String sStringToRandonize, int seed){
			if(seed < 0){
				log.info("Seed integer is < 0 therefore will not randomize");
				return sStringToRandonize;
			}
			StringBuffer sb;
			char[] characters  = sStringToRandonize.toCharArray();
			char newChar;
			String s;
			boolean bMakeUpper = false;
			
			if(charlistLatin.size() < 2)
				initLatinChars();
			
			if(charlistSupplimental.size() < 2)
				initSuppChars();
			
			sb = new StringBuffer();
			for(char c : characters){
				s = String.valueOf(c);
				bMakeUpper = Character.toLowerCase(c) != c;
				if(!charlistSupplimental.contains(c) && !charlistLatin.contains(c)){
					sb.append(s);
				} else {
					if(charlistSupplimental.contains(c))
						newChar = charlistSupplimental.get((charlistSupplimental.indexOf(c)+seed)%charlistSupplimental.size());
					else{
						newChar = charlistLatin.get((charlistLatin.indexOf(c)+seed)%charlistLatin.size());
					}
					
					newChar = bMakeUpper ? Character.toUpperCase(newChar) : Character.toLowerCase(newChar);
					sb.append(newChar);
				}
			}
			return sb.toString();
		}
		
		public static String randomizeString(String sStringToRandomize){
			int iRand = (int) Math.round((Math.random()) * 100);
			return randomize(sStringToRandomize, iRand);
		}
		
		public static String replaceVariables(String stingWithPlaceHolder) {
			if (stingWithPlaceHolder == null)
				return null;
		
			String maskVar = ".*(\\$\\{.*?\\}).*";
			String maskVal = "\\$\\{.*?\\}";
			String holder = stingWithPlaceHolder.replaceAll(maskVar, "$1").toLowerCase();
			
			String val = StringUtils.randomizeString(holder.substring(2, holder.length() - 1));
			
			if (holder.equals("${testcase}") || holder.equals("${testname}")){
				val = CallPoint.testcaseName();
				val = val.contains(".") ? val.substring(val.lastIndexOf('.')+1) : val;
			}
			else if (holder.contains("${time}"))
				val = DateUtils.dateToString(Calendar.getInstance().getTime(), "HHmmss.SSS");
			else if (holder.equals("${date}"))
				val = DateUtils.dateToString(Calendar.getInstance().getTime(), "yyyyddMM'T'HHmm");
			else if (holder.equals("${random}"))
				val = StringUtils.makeName(false).replace(" ", "");
			else
				val = StringUtils.randomizeString(holder.substring(2, holder.length() - 1));
			
			return stingWithPlaceHolder.replaceAll(maskVal, "["+val+"]");
		}
		
		/** 
		 * First name is five characters; an upper case character followed by four lower case characters<br>
		 * Last name is eight characters; an upper case character followed by seven lower case characters<br>
		 * @param bIncludeSupplemental If <b>true</b>, the name will include characters from the Latin-1 Supplemental character set.<br>
		 * If <b>false</b>, characters will be restricted to the Basic Latin character set.
		 * @return formatted name
		 */
		public static String makeName(boolean bIncludeSupplemental){
			String sFirstName = getRandomString(5, bIncludeSupplemental).toLowerCase();
			String sLastName = getRandomString(8, bIncludeSupplemental).toLowerCase();
			sFirstName = String.valueOf(sFirstName.charAt(0)).toUpperCase()+sFirstName.substring(1);
			sLastName = String.valueOf(sLastName.charAt(0)).toUpperCase()+sLastName.substring(1);
			
			return sFirstName+" "+sLastName;
		}
		
		/**
		 * @param charCodes
		 * @return
		 */
		private static ArrayList<Character> charList (String[] charCodes) {
			ArrayList<Character> characters = new ArrayList<Character>();
			int start;
			int end;
			for(String s : charCodes){
				if(!s.contains("-")){
					start = (int)Integer.valueOf(s,16);
					characters.add((char)start);
				} else{
					start = Integer.valueOf(s.split("-")[0],16);
					end = Integer.valueOf(s.split("-")[1],16);
					for(int x = start; x<=end; x++){
						characters.add((char)x);
					}
				}
			}
			return characters;
		}

		private static ArrayList<Character> initLatinChars() {
			charlistLatin.clear();
			charlistLatin.addAll(charList(latin));
			return charlistLatin;
		}
		private static ArrayList<Character> initSuppChars() {
			charlistSupplimental.clear();
			charlistSupplimental.addAll(charList(supplimental));
			return charlistSupplimental;
		}
		
		public static String getRandomString(int iLength, boolean bIncludeSupplemental) {
			ArrayList<Character> charlist = new ArrayList<Character>();
			if(charlistLatin.size()<2){
				initLatinChars();
			}
			
			charlist.addAll(charlistLatin);
			if(bIncludeSupplemental){
				if(charlistSupplimental.size() < 2)
					initSuppChars();
				charlist.addAll(charlistSupplimental);
			}
			
			Random rndGen = new Random();
			int limit = charlist.size()-1;
			int start = rndGen.nextInt(limit);
			StringBuilder sb = new StringBuilder();
			for (int x = 0 ; x<= iLength; x++){
				start = start+50;
				sb.append(charlist.get((start)%limit));
			}
			String temp = sb.toString();
			if(temp.startsWith("'") || temp.startsWith("-"))
				temp = temp.substring(1);
			else
				temp = temp.substring(0,iLength);
			return temp;
		}
		
		public static String[] parseCommaDelimitedStringToArray(String line){
			java.util.regex.Pattern pat = java.util.regex.Pattern.compile("(\".*?\")"); // find quoted text
		    Matcher matcher = pat.matcher(line);
		    StringBuffer sb = new StringBuffer();
		    while (matcher.find()){
		    	matcher.appendReplacement(sb,matcher.group(1).replaceAll(",","&comm")); 
		    }
		    matcher.appendTail(sb);
		    String[] fields = sb.toString().split("\\s*,\\s*");
		    for (int i=0;i<fields.length;i++){
		       String out = fields[i]!=null ? fields[i].replaceAll("&comm",",").trim() : ""; // replace the comma marker (&comm) with the comma char.
		       if(out.startsWith("\"") && out.endsWith("\""))
		    	   fields[i]=out.replaceAll("\"", "");
		    }
		    return fields;
		}
		
		/**
		 * Method checks if a string contains any numbers.Returns true if it does, false if it does not.
		 * @param str - String to be checked.
		 * @return
		 */
		public static boolean containsNumber(String str){
		//It can't contain numbers if it's null or empty...
	       if (str == null || str.length() == 0)
	           return false;       
	       for (int i = 0; i < str.length(); i++) {
	           //If we find a digit character we return false.
	           if (Character.isDigit(str.charAt(i)))
	        	   return true;           
	       }
	       return false;
		}
		
		/**
		 * Method checks if a string ends with any numbers.Returns true if it does, false if it does not.
		 * @param str - String to be checked.
		 * @return
		 */
		public static boolean endsWithNumber(String str){
		//It can't contain numbers if it's null or empty...
	       if (str == null || str.length() == 0)
	           return false;       
	       for (int i = 0; i < str.length(); i++) {
	           //If we find a digit character we return false.
	           if (Character.isDigit(str.charAt(i)))
	        	   return true;           
	       }
	       return false;
		}
		
		/**
		 * @param str
		 * @return
		 */
		public static String getEndDigits(String str){
			String digits = "";
			Pattern p= Pattern.compile(".*?([0-9]+$)");
			Matcher m = p.matcher(str.trim());
			if(m.matches()){
				try{
					digits = m.group(1);
				} catch (Exception e){
					
				}
			}
			return digits;
		}
		
		public static String getNumberInString(String str) {
			str = stringCleanUp(str);
			String digits = "-1";
			Pattern p= Pattern.compile(".*?([0-9\\.]+).*");
			Matcher m = p.matcher(str.trim());
			if(m.matches()){
				try{
					digits = m.group(1);
				} catch (Exception e){
					
				}
			}
			digits = digits.endsWith(".") ? digits.substring(0,digits.length()-1) : digits;
			return digits;
		}
		
		public static Number getNumberInStringAsNumber(String str, boolean bAllowNeg) {
			String sNum = "-999";
			if(str == null || str.isEmpty())
				return -999;
			str = stringCleanUp(str);
			Pattern p= Pattern.compile(".*?([-]{0,1}[0-9]+\\.{0,1}[0-9]+).*");
			Matcher m = p.matcher(str.trim());
			if(m.matches()){
				try{
					sNum = m.group(1);
				} catch (Exception e){
					
				}
			}
			sNum = sNum.endsWith(".") ? sNum.substring(0,sNum.length()-1) : sNum;
			if(sNum.startsWith("-") && !bAllowNeg)
				sNum = sNum.substring(1);
			
			if(sNum.contains("."))
				return (double)Double.valueOf(sNum);
			
			return (int)Integer.valueOf(sNum);
		}
		
		public static String getUniqueSuffix(String str) {
			String digits = "";
			Pattern p= Pattern.compile(".*?(\\d\\d:\\d\\d:\\d\\d\\.\\d\\d\\d$)");
			Matcher m = p.matcher(str.trim());
			Pattern p2 = Pattern.compile(".*?(\\d\\d\\d\\d:\\d\\d:\\d\\d\\:\\d\\d$)");
			Matcher m2 = p2.matcher(str.trim());
			
			if(m.matches()){
				try{
					digits = m.group(1);
				} catch (Exception e){
					//	
				}
			} else if(m2.matches()) {
				try{
					digits = m2.group(1);
				} catch (Exception e){
					//	
				}
			}
			return digits;
		}
		
		/**
		 *  <p>Returns the int value of the terminating digits of a string, if any exist.</p>
		 * <p>String "e-listview-header-col3" will return int 3 <br/>
		 * String "e-listview-header-3a" will return int -1</p>
		 * @param str
		 * @return
		 */
		public static int getEndDigitsAsInt(String str){
			return getEndDigits(str).equals("") ? -1 : Integer.valueOf(getEndDigits(str)).intValue();
		}
		
		public static boolean isNumber(String str) {
			try {
				return str.matches("-?\\d+(\\.\\d+)?");
			} catch (Exception e) {
				return false;
			}
		}
		
		/**
		 * Method replace special Regular expression characters with backslash + character so it can be escaped when executing reg expression.
		 * @param 
		 * 		str - String to be checked.
		 * 		charToEscape - Special Regular Expression to be escaped.
		 * @return
		 */
		public static String replaceCharToEscapeInRegExpression(String Str, String charToEscape){
			if(Str.contains(charToEscape))
				Str = Str.replace(charToEscape, "\\"+charToEscape);
			return Str;
		}
		
		public static String stringArrayToDelimitedString(String[] stringArray, String delimiter){
			String sOut="";
			if(stringArray==null||delimiter==null) return "";
			for(int x=0;x<stringArray.length;x++){
				if(x==0) sOut += stringArray[x];
				else sOut += delimiter+stringArray[x];
			}
			return sOut;
		}
		
		public static String[] delimitedStringToArray(String delimitedString, char delimiterChar){
			java.util.regex.Pattern pat = java.util.regex.Pattern.compile("(\".*?\")"); // find quoted text
			String delimiter = String.valueOf(delimiterChar);
			
			delimiter = delimiter.equals("|") ? "\\|" : delimiter;
			delimitedString = delimitedString.trim();
		    
			Matcher matcher = pat.matcher(delimitedString);
		    StringBuffer sb = new StringBuffer();
		    while (matcher.find()){
		    	matcher.appendReplacement(sb,matcher.group(1).replaceAll(delimiter,"&comm")); 
		    }
		    matcher.appendTail(sb);
		    String[] stringArray = sb.toString().split("\\s*"+delimiter+"\\s*"); // split the string with comma dilimitation
		    for (int i=0;i<stringArray.length;i++){
		       String out = stringArray[i]!=null ? stringArray[i].replaceAll("&comm",delimiter).trim() : ""; // replace the comma marker (&comm) with the comma char.
		       stringArray[i]=out.replaceAll("\"", "");
		    }
		    return stringArray;
			
		}
		
		public static String[] csvStringToArray(String csvString) {
			return parseCommaDelimitedStringToArray(csvString);
		}
		
		public static ArrayList<String> csvStringToList(String csvString) {
			ArrayList<String> csvList = new ArrayList<String>();
			for(String s : csvStringToArray(csvString))
				csvList.add(s.trim());
			return csvList;
		}
		
		public static ArrayList<String> arrayToList(String[] sArray){
			ArrayList<String> sList = new ArrayList<String>();
			sList.addAll(Arrays.asList(sArray));
			return sList;
		}
		
		public static String[] listToArray(ArrayList<String> sList) {
			String[] sArray = sList.toArray(new String[sList.size()]);
			return sArray;
		}
		
		public static String listToDelimitedString(ArrayList<String> stringList, String delimiter){	
			if(stringList == null || stringList.size() < 1)
				return "";
			StringBuffer sb = new StringBuffer();
			for(String s : stringList){
				sb.append(s).append(delimiter);
			}
			return sb.substring(0,sb.length()-delimiter.length());
		}
		
		public static String arrayToDelimitedString(String[] inArray, String delimiter) {
			ArrayList<String> inList = new ArrayList<String>();
			inList.addAll(Arrays.asList(inArray));
			return listToDelimitedString(inList, delimiter);
		}
		
		public static int countMatches(String sHaystack, String sNeedle) {
			int iFound = 0 ;
			Pattern pattern = Pattern.compile(Pattern.quote(sNeedle));
			Matcher matcher = pattern.matcher(sHaystack);
			while (matcher.find()){
				iFound++;
			}
			return iFound;
		}
		
		public static String HtmlTagsToLowerCase(String sText) {
			String sLower = sText;
			String s;
			Pattern p = Pattern.compile("(<.*?>)");
			Matcher m = p.matcher(sText);
			while(m.find()) {
				s = m.group();
				sLower = sLower.replace(s, s.toLowerCase());
			}
			return sLower;
		}
		
		public static String titleCase(String sText) {
			if(sText.length()<2)
				return sText.toUpperCase();
			
			String tc = sText.substring(0,1).toUpperCase()+sText.substring(1);
			String match;
			Pattern p = Pattern.compile("([\\s\\(][a-zA-Z])");
			Matcher m = p.matcher(sText);
			while(m.find()){
				match = m.group();
				tc = tc.replace(match, match.toUpperCase());
			}
			return tc;
		}
		
		/**
		 * Convert comma-separated decimal RGB value to a single Hex value
		 * @param rgb
		 * @return
		 */
		public static String rgbAsHex(String rgb) {
			String temp = rgb.substring(rgb.indexOf('(')+1);
			String hex = "";
			temp = temp.replace(")","").replaceAll("\\s","");
			String[] bits = temp.split(",");
			for(int x =0; x<3; x++){
				temp = Integer.toHexString(Integer.valueOf(bits[x]));
				temp = temp.length() == 1 ? "0"+temp : temp;
				hex += temp;
			}
			return hex.toUpperCase();
		}		
	}

