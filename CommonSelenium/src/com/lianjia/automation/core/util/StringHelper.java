package com.lianjia.automation.core.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

import com.lianjia.automation.core.Platform;

/**
 * Script Name   : <b>StringHelper</b>
 */
public class StringHelper
{
	/**				
	* Removes whitespace from a specified string <p>
	* @param 			String s - string to remove whitespace from
	* @return			String - string based on orinal specified string except without whitespace (i.e. string "I went out" is returned as "Iwentout"
	*/
	public static String removeWhiteSpace(String s)
	{
		StringTokenizer st = new StringTokenizer(s);
		int index = st.countTokens();

		StringBuffer str = new StringBuffer();

		for (int i = 0; i < index; i++)
		{
			str.append(st.nextToken().trim());
		}

		return str.toString();
	}

	/**				
	* Converts a specified string to a string array using default space delimeter<p>
	* @param 			String s - string to convert to string array
	* @return			String[] - the string array (i.e. string "one two three four" is returned as String [] = {"one","two","three","four"} 
	*/
	public static String[] stringToStringArray(String s)
	{
		return stringToStringArray(s, " ");
	}
	
	/**				
	* Converts a specified string to a string array using specified delimiter<p>
	* @param 			String s - string to convert to string array
	* @return			String[] - the string array (i.e. string "one, two, three, four," is returned as String [] = {"one","two","three","four"} 
	*/
	public static String[] stringToStringArray(String s, String sDelim)
	{
		String[] lines = s.split(sDelim);
		return lines;
	}

	/**	
	* Strips whitespace character (160) from output
	* @param String - String to strip ascii 160 characters from
	* @return String - Returns the original string stripped of any ascii 160 characters
	*/
	public static String stripWSChar(String s)
	{
		StringBuffer sb = new StringBuffer();

		for (int x = 0; x < s.length(); x++)
		{
			char c = s.charAt(x);
			int i = c;

			if (i != 160)
			{
				sb.append(c);
			}
		}
		return sb.toString();
	}
       
	/**
	 * Searches for lines that match search strings and returns the matching lines <p>
	 * @param String[] lsSearchContent = String array to search. Tip: use GetFileContentsAsList(String filename) method to search entire file 
	 * @param String sSearchStr = String to search for;
	 * @return String of extracted text lines from the searched content that match the specified search string criteria
	 * 
	 */    
	public static String getLines(String[] lsSearchContent, String sSearchStr)
	{
		String sMatch = "";
		int iNumOfMatches = 0;

		//find matches
		for (int x = 0; x < lsSearchContent.length; x++)
		{
			if (lsSearchContent[x] != null)
			{
				if (lsSearchContent[x].indexOf(sSearchStr) != -1)
				{
					iNumOfMatches++;
					sMatch = sMatch + lsSearchContent[x] + Platform.gsNewline;
				}
			}
		}
		return sMatch;
	}	
	
	/**
	 * Sorts data in string array. Ignores case.
	 * @param String s[] - String Array of unsorted data
	 * @param boolean bAscOrDesc - true for Ascending or false for descending order
	 *
	 * @return String[] - String array data in sorted order
	 */
	public static String[] sortIgnoreCase(String[] s,boolean bAscOrDesc)
	{
		int cnt;
		String temp;
		for(int i=0;i<s.length;i++)
		{
			cnt=0;
			for(int j=0;j<s.length-i-1;j++)
			{
				if(bAscOrDesc)
				{
					if(s[j].compareToIgnoreCase(s[j+1])>0)
					{
						temp=s[j];
						s[j]=s[j+1];
						s[j+1]=temp;
						cnt++;
					}
				}
				else
				{
					if(s[j].compareToIgnoreCase(s[j+1])<=0)
					{
						temp=s[j];
						s[j]=s[j+1];
						s[j+1]=temp;
						cnt++;
					}
				}
			}			
			if(cnt==0)
				continue;			
		}
			return s;
	}	
	
	/**
	 * Sorts data in string array. Case sensitive.
	 * @param String s[] - String Array of unsorted data
	 * @param boolean bAscOrDesc - true for Ascending or false for descending order
	 *
	 * @return String[] - String array data in sorted order
	 */
	public static String[] sort(String[] s,boolean bAscOrDesc)
	{
		int cnt;
		String temp;
		for(int i=0;i<s.length;i++)
		{
			cnt=0;
			for(int j=0;j<s.length-i-1;j++)
			{
				if(bAscOrDesc)
				{
					if(s[j].compareTo(s[j+1])>0)
					{
						temp=s[j];
						s[j]=s[j+1];
						s[j+1]=temp;
						cnt++;
					}
				}
				else
				{
					if(s[j].compareTo(s[j+1])<=0)
					{
						temp=s[j];
						s[j]=s[j+1];
						s[j+1]=temp;
						cnt++;
					}
				}
			}			
			if(cnt==0)
				continue;			
		}
			return s;
	}
	
	/**
	 * Replaces the occurences of a certain pattern in a string with a
	 * replacement String. This is the fastest replace function known to author.
	 *
	 * @param s      the string to be inspected
	 * @param sub    the string pattern to be replaced
	 * @param with   the string that should go where the pattern was
	 *
	 * @return the string with the replacements done
	 */
	public static String replace(String s, String sub, String with) {
		if ((s == null) || (sub == null) || (with == null)) {
			return s;
		}
		int c = 0;
		int i = s.indexOf(sub, c);
		if (i == -1) {
			return s;
		}
		StringBuffer buf = new StringBuffer(s.length() + with.length());
		do {
			 buf.append(s.substring(c,i));
			 buf.append(with);
			 c = i + sub.length();
		 } while ((i = s.indexOf(sub, c)) != -1);
		 if (c < s.length()) {
			 buf.append(s.substring(c,s.length()));
		 }
		 return buf.toString();
	}

	/**
	 * Character replacement in a string. All occurrencies of a character will be
	 * replaces.
	 *
	 * @param s      input string
	 * @param sub    character to replace
	 * @param with   character to replace with
	 *
	 * @return string with replaced characters
	 */
	public static String replace(String s, char sub, char with) {
		if (s == null) {
			return s;
		}
		char[] str = s.toCharArray();
		for (int i = 0; i < str.length; i++) {
			if (str[i] == sub) {
				str[i] = with;
			}
		}
		return new String(str);
	}

	/**
	 * Replaces the very first occurance of a substring with suplied string.
	 *
	 * @param s      source string
	 * @param sub    substring to replace
	 * @param with   substring to replace with
	 *
	 * @return modified source string
	 */
	public static String replaceFirst(String s, String sub, String with) {
		if ((s == null) || (sub == null) || (with == null)) {
			return s;
		}
		int i = s.indexOf(sub);
		if (i == -1) {
			return s;
		}
		return s.substring(0, i) + with + s.substring(i + sub.length());
	}

	/**
	 * Replaces the very first occurence of a character in a String.
	 *
	 * @param s      string
	 * @param sub    char to replace
	 * @param with   char to replace with
	 *
	 * @return modified string
	 */
	public static String replaceFirst(String s, char sub, char with) {
		if (s == null) {
			return s;
		}
		char[] str = s.toCharArray();
		for (int i = 0; i < str.length; i++) {
			if (str[i] == sub) {
				str[i] = with;
				break;
			}
		}
		return new String(str);
	}

	/**
	 * Replaces the very last  occurance of a substring with suplied string.
	 *
	 * @param s      source string
	 * @param sub    substring to replace
	 * @param with   substring to replace with
	 *
	 * @return modified source string
	 */
	public static String replaceLast(String s, String sub, String with) {
		if ((s == null) || (sub == null) || (with == null)) {
			return s;
		}
		int i = s.lastIndexOf(sub);
		if (i == -1) {
			return s;
		}
		return s.substring(0, i) + with + s.substring(i + sub.length());
	}

	/**
	 * Replaces the very last occurence of a character in a String.
	 *
	 * @param s      string
	 * @param sub    char to replace
	 * @param with   char to replace with
	 *
	 * @return modified string
	 */
	public static String replaceLast(String s, char sub, char with) {
		if (s == null) {
			return s;
		}
		char[] str = s.toCharArray();
		for (int i = str.length - 1; i >= 0; i--) {
			if (str[i] == sub) {
				str[i] = with;
				break;
			}
		}
		return new String(str);
	}

	/**
	 * Determines if a string is empty. If string is NULL, it returns true.
	 *
	 * @param s      string
	 *
	 * @return true if string is empty or null.
	 */
	public static boolean isEmpty(String s) {
		if (s != null) {
			return (s.length() == 0);
		}
		return true;
	}

	/**
	 * Set the maximum length of the string. If string is longer, it will be
	 * shorten.
	 *
	 * @param s      string
	 * @param len    max number of characters in string
	 *
	 * @return string with length no more then specified
	 */
	public static String setMaxLength(String s, int len) {
		if (s == null) {
			return s;
		}
		if (s.length() > len) {
			s = s.substring(0, len);
		}
		return s;
	}

	/**
	 * Converts an object to a String. If object is <code>null</code> it will be
	 * not converted.
	 *
	 * @param obj    object to convert to string
	 *
	 * @return string created from the object or <code>null</code>
	 */
	public static String toString(Object obj) {
		if (obj == null) {
			return (String)null;
		}
		return obj.toString();
	}

	/**
	 * Converts an object to a String. If object is <code>null</code> a empty
	 * string is returned.
	 *
	 * @param obj    object to convert to string
	 *
	 * @return string created from the object
	 */
	public static String toNotNullString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	/**
	 * Splits a string in several parts (tokens) that are separated by delimeter.
	 * Delimeter is <b>always</b> surrounded by two strings! If there is no
	 * content between two delimeters, empty string will be returned for that
	 * token. Therefore, the length of the returned array will always be:
	 * #delimeters + 1.<br><br>
	 *
	 * Method is much, much faster then regexp <code>String.split()</code>,
	 * and a bit faster then <code>StringTokenizer</code>.
	 *
	 * @param src       string to split
	 * @param delimeter split delimeter
	 *
	 * @return array of splitted strings
	 */
	public static String[] split(String src, String delimeter) {
		if (src == null) {
			return null;
		}
		if (delimeter == null) {
			return new String[] {src};
		}
		int maxparts = (src.length() / delimeter.length()) + 2;		// one more for the last
		int[] positions = new int[maxparts];
		int dellen = delimeter.length();
		
		int i = 0, j = 0;
		int count = 0;
		positions[0] = - dellen;
		while ((i = src.indexOf(delimeter, j)) != -1) {
			count++;
			positions[count] = i;			
			j = i + dellen;						
		}
		count++;
		positions[count] = src.length();
		
		String[] result = new String[count];
		
		for (i = 0; i < count; i++) {
			result[i] = src.substring(positions[i] + dellen, positions[i + 1]);
		}		
		return result;
	}

	/**
	 * Finds first index of a substring in the given source string with ignored case.
	 *
	 * @param src    source string for examination
	 * @param subS   substring to find
	 *
	 * @return index of founded substring or -1 if substring is not found
	 * @see #indexOfIgnoreCase(String, String, int)
	 */
	public static int indexOfIgnoreCase(String src, String subS) {
		return indexOfIgnoreCase(src, subS, 0);
	}

	/**
	 * Finds first index of a substring in the given source string with ignored
	 * case. This seems to be the fastest way doing this, with common string
	 * length and content (of course, with no use of Boyer-Mayer type of
	 * algorithms). Other implementations are slower: getting char array frist,
	 * lowercasing the source string, using String.regionMatch etc.
	 *
	 * @param src        source string for examination
	 * @param subS       substring to find
	 * @param startIndex starting index from where search begins
	 *
	 * @return index of founded substring or -1 if substring is not found
	 */
	public static int indexOfIgnoreCase(String src, String subS, int startIndex) {
		String sub = subS.toLowerCase();
		int sublen = sub.length();
		int total = src.length() - sublen + 1;
		for (int i = startIndex; i < total; i++) {
			int j = 0;
			while (j < sublen) {
				char source = Character.toLowerCase(src.charAt(i + j));
				if (sub.charAt(j) != source) {
					break;
				}
				j++;
			}
			if (j == sublen) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Finds last index of a substring in the given source string with ignored
	 * case.
	 *
	 * @param s
	 * @param subS   substring to find
	 *
	 * @return last index of founded substring or -1 if substring is not found
	 * @see #indexOfIgnoreCase(String, String, int)
	 * @see #lastIndexOfIgnoreCase(String, String, int)
	 */
	public static int lastIndexOfIgnoreCase(String s, String subS) {
		return lastIndexOfIgnoreCase(s, subS, 0);
	}

	/**
	 * Finds last index of a substring in the given source string with ignored
	 * case.
	 *
	 * @param src        source string for examination
	 * @param subS       substring to find
	 * @param startIndex starting index from where search begins
	 *
	 * @return last index of founded substring or -1 if substring is not found
	 * @see #indexOfIgnoreCase(String, String, int)
	 */
	public static int lastIndexOfIgnoreCase(String src, String subS, int startIndex) {
		String sub = subS.toLowerCase();
		int sublen = sub.length();
		int total = src.length() - sublen;
		for (int i = total; i >= startIndex; i--) {
			int j = 0;
			while (j < sublen) {
				char source = Character.toLowerCase(src.charAt(i + j));
				if (sub.charAt(j) != source) {
					break;
				}
				j++;
			}
			if (j == sublen) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Tests if this string starts with the specified prefix with ignored case.
	 *
	 * @param src    source string to test
	 * @param subS   starting substring
	 *
	 * @return <code>true</code> if the character sequence represented by the argument is
	 *         a prefix of the character sequence represented by this string;
	 *         <code>false</code> otherwise.
	 */
	public static boolean startsWithIgnoreCase(String src, String subS) {
		return startsWithIgnoreCase(src, subS, 0);
	}
	
	/**
	 * Tests if this string starts with the specified prefix with ignored case
	 * and with the specified prefix beginning a specified index.
	 *
	 * @param src        source string to test
	 * @param subS       starting substring
	 * @param startIndex index from where to test
	 *
	 * @return <code>true</code> if the character sequence represented by the argument is
	 *         a prefix of the character sequence represented by this string;
	 *         <code>false</code> otherwise.
	 */
	public static boolean startsWithIgnoreCase(String src, String subS, int startIndex) {
		String sub = subS.toLowerCase();
		int sublen = sub.length();
		if (startIndex + sublen > src.length()) {
			return false;
		}
		int j = 0;
		int i = startIndex;
		while (j < sublen) {
			char source = Character.toLowerCase(src.charAt(i));
			if (sub.charAt(j) != source) {
				return false;
			}
			j++; i++;
		}
		return true;
	}

	/**
	 * Tests if this string ends with the specified suffix.
	 *
	 * @param src    String to test
	 * @param subS   suffix
	 *
	 * @return <code>true</code> if the character sequence represented by the argument is
	 *         a suffix of the character sequence represented by this object;
	 *         <code>false</code> otherwise.
	 */
	public static boolean endsWithIgnoreCase(String src, String subS) {
		String sub = subS.toLowerCase();
		int sublen = sub.length();
		int j = 0;
		int i = src.length() - sublen;
		if (i < 0) {
			return false;
		}
		while (j < sublen) {
			char source = Character.toLowerCase(src.charAt(i));
			if (sub.charAt(j) != source) {
				return false;
			}
			j++; i++;
		}
		return true;
	}
	
	/**
	 * Checks whether a string matches a given wildcard pattern.
	 * Possible patterns allow to match single characters ('?') or any count of
	 * characters ('*'). Wildcard characters can be escaped (by an '\').
	 *
	 * <p>This method uses recursive matching, as in linux or windows. regexp works the same.
	 * This method is very fast, comparing to similar implementations.
	 *
	 * @param string	input string
	 * @param pattern	pattern to match
	 * @return 			<code>true</code> if string matches the pattern, otherwise <code>fasle</code>
	 */
	public static boolean isMatch(String string, String pattern) {
		return isMatch(string, pattern, 0, 0);
	}

	/**
	 * Internal matching recursive function.
	 */
	public static boolean isMatch(String string, String pattern, int stringStartNdx, int patternStartNdx) {
		int pNdx = patternStartNdx;
		int sNdx = stringStartNdx;
		int pLen = pattern.length();
		int sLen = string.length();
		boolean nextIsNotWildcard = false;
		while (true) {

			// check if end of string and/or pattern occured
			if ((sNdx >= sLen) == true) {		// end of string still may have pending '*' in pattern
				while ((pNdx < pLen) && (pattern.charAt(pNdx) == '*')) {
					pNdx++;
				}
                if (pNdx >= pLen) {				// end of both string and pattern
					return true;
				}
				return false;
			}
			if (pNdx >= pLen) {					// end pf pattern, but not end of the string
				return false;
			}
			char p = pattern.charAt(pNdx);		// pattern char

			// perform logic
			if (nextIsNotWildcard == false) {

				if (p == '\\') {
					pNdx++;
					nextIsNotWildcard =  true;
					continue;
				}
				if (p == '?') {
					sNdx++; pNdx++;
					continue;
				}
				if (p == '*') {
					char pnext = 0;						// next pattern char
					if (pNdx + 1 < pLen) {
						pnext = pattern.charAt(pNdx + 1);
					}
					if (pnext == '*') {					// double '*' have the same effect as one '*'
						pNdx++;
						continue;
					}
					int i;
					pNdx++;

					// find recursively if there is any substring from the end of the
					// line that matches the rest of the pattern !!!
					for (i = string.length(); i >= sNdx; i--) {
						if (isMatch(string, pattern, i, pNdx) == true) {
							return true;
						}
					}
					return false;
				}
			} else {
				nextIsNotWildcard = false;
			}
			
			// check if pattern char and string char are equals
			if (p != string.charAt(sNdx)) {
				return false;
			}

			// everything matches for now, continue
			sNdx++; pNdx++;
		}
	}
	
	/**
	 * Count substring occurences in a source string.
	 *
	 * @param source	source string
	 * @param sub		substring to count
	 * @return			number of substring occurences
	 */
	public static int count(String source, String sub) {
		int count = 0;
		int i = 0, j = 0;		
		while (true) {
			i = source.indexOf(sub, j);
			if (i == -1) {
				break;
			}
			count++;
			j = i + sub.length();
		}
		return count;
	}
	
	/**
	 * Repeats a specified string a specified number of times
	 * @param s - string to repeat
	 * @param iRepeat - number of times to repeat string.
	 * repeat("*",7); would return "*******"  
	 * @return
	 */
	public static String repeat(String s, int iRepeat)
	{
		StringBuffer buf = new StringBuffer();
		for (int i=0;i<iRepeat;i++) 
		{
			buf.append(s);
		}
		return buf.toString();
	}	
	
	/**
	 * Returns specified string in proper case
	 * @param s - string to convert to proper case (first character is initialized)  
	 */
	public static String toProperCase(String s)
	{
		String sInitChar = s.substring(0,1); //get first char
		String sCapitalizedChar = sInitChar.toUpperCase();  //capitalize the first char
		String sOutStr = StringHelper.replaceFirst(s,sInitChar,sCapitalizedChar); //replace the existing char with the capitalized one
		return sOutStr; //return the string in proper case
	}	

    /**
    * Returns the result of a text check on a string-- checks for strings being equal;  no VP performed
    * @param firstString string being checked in
    * @param secondString pattern being checked for
    * @return true if strings equal, false if not
    * */
    public static boolean doStringsMatch(String firstString, String secondString) {
        return firstString.equals(secondString);
    }

    /**
    * Returns the result of a text check on a string- checks for pattern occurring in a string; no VP perf.
    * @param searchString string being checked in
    * @param subString pattern being checked for
    * @return true if present, false if not
    * */
    public static boolean isSubstring(String searchString, String substring) {
    	 Pattern p = Pattern.compile(substring);
    	 return p.matcher(searchString).matches();
    }

    /**
    * Returns the string with the space characters "fixed"-- this was coded for difficulties with weird spaces
    *     screwing up string matching
    * @param s string being fixed
    * @return string with fixed space characters
    * */
    public static String fixString(String s) {
        String t = "";
        for (int i = 0; i < s.length(); i++) {
            if (((int) s.charAt(i)) == 160) {
                t += (char) 32;
            }
            else {
                t += s.charAt(i);
            }
        }
        return t;
    }

    /**
    * Prints the character values for 2 strings
    * @param s1 1st string
    * @param s2 2nd string
    * */
    public static void printCharVals(String s1, String s2) {
        for (int i = 0; i < s1.length(); i++) {
            System.out.print("'" + s1.charAt(i) + "' == '" + (int) s1.charAt(i) + "'");
            System.out.print(":");
            System.out.println("'" + (int) s2.charAt(i) + "' == '" + s2.charAt(i) + "'");
        }
    }

	/**
	 * Searches the input array for an occurrence of the search pattern
	 * @param pattern  the string pattern to search for
	 * @param screenContents  the String array to search in
	 * @return boolean, true if found, false if not found
	 */
	public static boolean findPattern (String pattern, String[] screenContents)
	{
		boolean found = false;
		
		Pattern p = Pattern.compile(pattern);
		
		//search for a pattern in the screen contents
		for (int i = 0; i < screenContents.length; ++i)
		{
			if (screenContents[i] != null)  { 
				if (p.matcher(screenContents[i]).matches())
					return true;
			}
		}
		return found;
	}

	/**
	 * Searches the input array for an occurrence of the search pattern
	 * @param pattern  the string pattern to search for
	 * @param screenContents  the String to search in
	 * @return boolean, true if found, false if not found
	 */
	public static boolean findPattern (String pattern, String screenContents)
	{
		if (pattern == null || screenContents == null)
			return false;
		Pattern p = Pattern.compile(pattern);
		return p.matcher(screenContents).matches();
	}

	/**
	 * Searches the input array for an occurrence of the search pattern
	 * @param pattern  the string pattern to search for
	 * @param screenContents  the String array to search in
	 * @return int  index of the first occurrence of the search pattern, or -1
	 */
	public static int findPatternRow (String pattern, String[] screenContents)
	{
		int row = -1;
		Pattern p = Pattern.compile(pattern);
		for (int i = 0; i < screenContents.length; ++i)
		{
			if (screenContents[i] != null)  { 			
				if (p.matcher(screenContents[i]).matches())
					return i;
			}
		}
		return row;
	}

	/**
	 * Searches the input array for an occurrence of the search pattern
	 * @param pattern  the string pattern to search for
	 * @param fromIndex  the array index from which to start the search
	 * @param screenContents  the String array to search in
	 * @return int  index of the first occurrence of the search pattern after the index
	 */
	public static int findPatternRow (String pattern, int fromIndex, String[] screenContents)
	{
		int row = -1;
		Pattern p = Pattern.compile(pattern);
		
		//search for a pattern in the screen contents
		for (int i = fromIndex; i < screenContents.length; ++i)
		{
			if (screenContents[i] != null)  { 
				if (p.matcher(screenContents[i]).matches())
					return i;
			}
		}
		return row;
	}

	/**
	 * Searches the input array for occurrences of the search pattern
	 * @param pattern  the string pattern to search for
	 * @param screenContents  the String array to search in
	 * @return int [] containing the indexes where the search pattern was found
	 */
	public static int [] findPatternRows (String pattern, String[] screenContents)
	{
		Vector v = new Vector();
		Pattern p = Pattern.compile(pattern);
		
		//search for a string in the screen contents
		for (int i = 0; i < screenContents.length; ++i)
		{
			if (screenContents[i] != null)  { 			
				if (p.matcher(screenContents[i]).matches())
					v.add(new java.lang.Integer(i));
			}
		}
		//creates the array of indexes found with the pattern
		Object [] objs = v.toArray();
		int [] rows = new int [objs.length];
		for (int i = 0; i < objs.length; ++i)
			rows[i] = ((java.lang.Integer) objs[i]).intValue();
		return rows;
	}

	/**
	 * Searches the input array for an occurrence of the search string
	 * @param string  the substring to search for
	 * @param screenContents  the String array to search in
	 * @return boolean, true if found, false if not found
	 */
	public static boolean findString (String string, String[] screenContents)
	{
		boolean found = false;
		//search for a string in the screen contents
		for (int i = 0; i < screenContents.length; ++i)
		{
			if (screenContents[i] != null)  { 			
				if (screenContents[i].indexOf(string) != -1)
					return true;
			}
		}
		return found;
	}

	/**
	 * Searches the input array for an occurrence of the search string
	 * @param string  the substring to search for
	 * @param screenContents  the String to search in
	 * @return boolean, true if found, false if not found
	 */
	public static boolean findString (String string, String screenContents)
	{
		if (string == null || screenContents == null)
			return false;

		if (screenContents.indexOf(string) != -1)
				return true;

		else return false;
	}

	/**
	 * Searches the input array for an occurrence of the search string
	 * @param string  the substring to search for
	 * @param screenContents  the String array to search in
	 * @return int  index of the first occurrence of the search string
	 */
	public static int findStringRow (String string, String[] screenContents)
	{
		int row = -1;
		//search for a string in the screen contents
		for (int i = 0; i < screenContents.length; ++i)
		{
			if (screenContents[i] != null)  { 
				if (screenContents[i].indexOf(string) != -1)
					return i;
			}
		}
		return row;
	}

	/**
	 * Searches the input array for an occurrence of the search string after the index
	 * @param string  the substring to search for
	 * @param fromIndex  the array index from which to start the search
	 * @param screenContents  the String array to search in
	 * @return int  index of the first occurrence of the search string after the index
	 */
	public static int findStringRow (String string, int fromIndex, String[] screenContents)
	{
		int row = -1;
		//search for a string in the screen contents
		for (int i = fromIndex; i < screenContents.length; ++i)
		{
			if (screenContents[i] != null)  { 
				if (screenContents[i].indexOf(string) != -1)
					return i;
			}
		}
		return row;
	}

	/**
	 * Searches the input array for occurrences of the search string
	 * @param string  the substring to search for
	 * @param screenContents  the String array to search in
	 * @return int [] containing the indexes where the search string was found
	 * 
	 */
	public static int [] findStringRows (String string, String[] screenContents)
	{
		Vector v = new Vector();
		//search for a string in the screen contents
		for (int i = 0; i < screenContents.length; ++i)
		{
			if (screenContents[i] != null)  { 			
				if (screenContents[i].indexOf(string) != -1)
					v.add(new java.lang.Integer(i));
			}
		}
		//creates the array of indexes found with the string
		Object [] objs = v.toArray();
		int [] rows = new int [objs.length];
		for (int i = 0; i < objs.length; ++i)   {
			rows[i] = ((java.lang.Integer) objs[i]).intValue();
		}
		return rows;
	}

	  /**
     * a method that returns the postion of a string sText in a array of string asText.
     * -1 is returned if not found.
     * @param asText: List of string to search.
	 * @param sText: The string to search for.
	 * 
	 * Returns the index of the row in asText that starts with sText.
	 * Returns -1 if not found.
     */
	public static int  findStringRowBeginning(String[] asText, String sText)
    {
    	int iTmp;
    	int iLength;
    	String sFromList = ""; 
   
    	iLength = sText.length();
    	
     	for (iTmp = 0; iTmp < asText.length ; iTmp++)
    	{
   		if (asText[iTmp].length() >= iLength)
    			{sFromList = asText[iTmp].substring(0, iLength);
        			if ( sFromList.equalsIgnoreCase(sText)) {
    				return iTmp;}}
    	};
    	
    	return -1;
    }
	
	  /**
     * a method that returns the postion of a string sText in a array of string asText.
     * -1 is returned if not found.
     * @param asText: List of string to search.
	 * @param sText: The string to search for.
	 * @param iStartAt: int of the row to start at
	 * 
	 * Returns the index of the row in asText that starts with sText.
	 * Returns -1 if not found.
     */
	public static int  findStringRowBeginningStartAt(String[] asText, String sText, int iStartAt)
    {
    	int iTmp;
    	int iLength;
    	String sFromList = ""; 
   
    	iLength = sText.length();
    	
     	for (iTmp = iStartAt; iTmp < asText.length ; iTmp++)
    	{
   		if (asText[iTmp].length() >= iLength)
    			{sFromList = asText[iTmp].substring(0, iLength);
        			if ( sFromList.equalsIgnoreCase(sText)) {
    				return iTmp;}}
    	};
    	
    	return -1;
    }
	
    /**
     * This method performs multiple replacements in the same string.
     *
     * @param ht A hashtable whose keys are the strings to be searched for and
     *        replaced, and whose  values are the replacements strings.
     * @param s The source string to search.
     *
     * @return The original source string with all replacements completed.
     */    public static String replace(Hashtable ht, String s)
    {
        Enumeration enumr = ht.keys();
        String newstring = s;

        while (enumr.hasMoreElements())
        {
            String from = (String) enumr.nextElement();
            String to = (String) ht.get(from);
            newstring = replace(newstring, from, to);
        }        return newstring;
    }

    /**
     * Performs multiple replacements in multiple strings.
     *
     * @param ht A hashtable whose keys are the strings to be searched for and
     *        replaced, and whose  values are the replacements strings.
     * @param s The source strings (in an array) to search.
     *
     * @return The original source strings with all replacements completed.
     */
    public static String[] replaceAll(Hashtable ht, String[] s)
    {
        String[] newstring = s;

        for (int i = 0; i < s.length; i++)
        {
            newstring[i] = replace(ht, s[i]);
        }

        return newstring;
    }	
    
    /**
     * Converts a string array to a String
     * @param a
     * @param separator
     * @return
     */
    public static String arrayToString(String[] s) {
        StringBuffer sOut = new StringBuffer();
        if (s.length > 0) {
            sOut.append(s[0]);
            for (int i=1; i<s.length; i++) {
                sOut.append(System.getProperty("line.separator"));
                sOut.append(s[i]);
            }
        }
        return sOut.toString();
    }
    
    /**
     * Judge if specified string comprises of number characters.<br>
     * <ul>
     * <li>integer: 1000
     * <li>long: 922337203685477580L
     * <li>double: 1000d or 1000.35D
     * <li>float: 1000f or 1000.2F
     * <li>science number: 34E5 or 23.5684e10 
     * TODO: range control, such as long number range: 9223372036854775807,-9223372036854775808
     * </ul>
     * 
     * @param s
     * @return true, if given string is number string, else false
     * 
     */
    public static boolean isNumber(String s) {
    	if(s==null||s.trim().length()==0){
    		return false;
    	}
    	int pointCount = 0;
    	int eCount = 0;
    	for (int index = 0, length = s.length(); index < length; index++) {
    		char c = s.charAt(index);
    		// negative
    		if (c == '-') {
    			if (index != 0) {
    				return false;
    			}
    		}
    		// can not put 0 at first position, if it is not 0
    		if (c == '0') {
    			if (index == 0 && s.length() > 1) {
    				return false;
    			}
    		} 
    		// decimal fraction - can not contain more than one radix point
    		if (c == '.') {
    			if (pointCount == 0) {
    				pointCount++;
    				continue;
    			} else {
    				return false;
    			}
    		}
    		// E count
    		if (c == 'E' || c == 'e') {
    			// exponent can not be decimal fraction
    			int idx1 = s.indexOf('.');
    			int idx2 = s.indexOf(c);
    			if (idx2 < idx1) {
    				return false;
    			}
    			// E/e can not appear at start or end or more than one time
    			if (index == 0 || index == length - 1) {
    				return false;
    			} else if (eCount == 0) {
    				eCount++;
    				continue;
    			} else {
    				return false;
    			}
    		}
    		// postfix of float, long, and double
    		if (c == 'F' || c == 'f' || c == 'D' || c == 'd') {
    			return index == length - 1;
    		}
    		if (c == 'L' || c == 'l') {
    			return index == length - 1 && pointCount == 0;
    		}
    		if (!isNumberChar(c)) {
    			return false;
    		}
    	}
    	return true;
    }

    /**
     * judge if given character is number character.
     * @param c
     * @return true, if given character is number character; else, false
     *  
     */
    private static boolean isNumberChar(char c) {
    	return c >= '0' && c <= '9';
    }

    /**
     * After replacing white space in strings, see if the target string contains the substring.
     * This is helpful when some web applications format test with non breaking spaces.
     * @param sTarget
     * @param sSub
     * @return
     */
    public static boolean contains(String sTarget, String sSub){
    	sTarget=replaceWhiteSpace(sTarget);
    	sSub=replaceWhiteSpace(sSub);
    	
    	return sTarget.contains(sSub);
    }
    
    
    /**
	 * Replaces the white space character (\u00a0, 160) with space character
	 * @param s string to replace white space
	 * @return Reformatted string
	 */
	public static String replaceWhiteSpace(String s) {
		StringBuffer sb = new StringBuffer();

		for (int x = 0; x < s.length(); x++)
		{
			char c = s.charAt(x);
			int i = c;

			if (i != 160)
			{
				sb.append(c);
			} else {
				sb.append(' ');
			}
		}

		return sb.toString();
	}       

	public static String firstCharacterToUpperCase(String stringToModify)
	{
		if(stringToModify.equals(""))
		{
			return stringToModify;
		}
		try {
			String myFirstLetter = stringToModify.substring(0, 1);
			String myRemainingLetters = stringToModify.replaceFirst(myFirstLetter, "");
			return myFirstLetter.toUpperCase() + myRemainingLetters;
			
		} catch (Exception e) {
			return stringToModify;
		}		
	}
	
	public static String toCamelCase(String delimiter, String stringToModify)
	{
		String myNewString = stringToModify;
		if(!stringToModify.contains(delimiter))
		{
			return stringToModify;
		}
		
		try {
			String[] myLocatorWords = myNewString.split(delimiter);
			myNewString = "";
			for (int i = 0; i < myLocatorWords.length; i++) {
				
				myNewString = myNewString + firstCharacterToUpperCase(myLocatorWords[i]);
			}			
		} catch (Exception e) {
		}		
		return myNewString;
	}
}
