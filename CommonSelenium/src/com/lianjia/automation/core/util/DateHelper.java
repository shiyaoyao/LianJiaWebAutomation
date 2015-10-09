package com.lianjia.automation.core.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Script Name   : <b>DateHelper</b>
 */
public class DateHelper {	
	/**Global long for ms diff between GMT and EST*/
	public static long glCalOffset = 18000000;
	
	/**Global long for timer start time*/
	public static long glStartTime = 0;	

	/**	
	* Sets clock start time  <p>
	*/
	public static long setStartTime()
	{
		long lTime = System.currentTimeMillis();
		return lTime;
	}
	
	/**	
	* returns long data type of elapsed time   <p>
	* @return Long containing the elapsed time between the start time specified and the end time. 
	*/
	public static long getElapsedTimeLong(long startTime)
	{
		//end time long format
		long endTime = System.currentTimeMillis();

		//elapsed time long format
		long time = endTime - startTime;
		return time;
	}	

	/**	
	* Gets elapsed time from the specified start time. Returns string in "HH:mm:ss:SSS" format. <p>
	* @param startTime		the start time
	* @return A string containing the time difference between the script start time and the script elapsed time. 
	*/
	public static String getElapsedTime(long startTime)
	{
		//end time long format
		long endTime = System.currentTimeMillis();

		//elapsed time long format
		long time = endTime - startTime;
		
		//Format the Date time information
		String s = getFormattedDateTime(time, "HH:mm:ss:SSS");

		return s;
	}

	/**	
	* Gets elapsed time from the specified start time. Returns string in "HH:mm:ss:SSS" format. <p>
	* @param startTime		the start time
	* @return A string containing the time difference between the script start time and the script elapsed time. 
	*/
	public static String getElapsedTime(long startTime, String sFormat)
	{
		//end time long format
		long endTime = System.currentTimeMillis();

		//elapsed time long format
		long time = endTime - startTime;

		//Format the Date time information
		String s = getFormattedDateTime(time, sFormat);

		return s;
	}

	/**	
	* returns formatted String version of date supplied as long  <p>
	* @param dDateTime			date and time in long data type
	* @param sDateTimeFormat	The string format you would like the date and time to be displayed as. (i.e. "HH:mm:ss:SSS")
	* @return String containing the formatted date time of the given long datetime. 
	*/
	public static String getFormattedDateTime(long dDateTime, String sDateTimeFormat)
	{
		SimpleDateFormat tmformat = new SimpleDateFormat(sDateTimeFormat);

		Date tm = new Date(glCalOffset + dDateTime);

		String s = tmformat.format(tm);

		return s;
	}	

	/**	
	* returns a long from a specified Date String  <p>
	* Helpful in making date-based calculations
	* @param String sDateTime	date and time in String data type (i.e. "12/25/2004")
	* @param sDateTimeFormat	The string format you would like the date and time to be displayed as. (i.e. "HH:mm:ss:SSS")
	* @return long containing the date time value of the specified date 
	* (i.e. getLongFromFormattedDateTime( "09/21/1964", "MM/dd/yy" ) would return the long value 1135486800000 which
	* can then be used in date-based calculations.  
	*/
	public static long getLongFromFormattedDateTime(String sDate, String sFormat)
	{
		Date dDate = stringToDate( sDate, sFormat );
		
		return dDate.getTime();
		
	}	

	/**
	* returns a string of the current date in MMMM dd, yyyy format
	* @return 		
	*/
	public static String getCurrentDate()
	{
		//get current date
		long currentDate = System.currentTimeMillis() - glCalOffset;
		String sCurrentDate;
		
		//Format the current date information to string. Check if day is single or double digit
		String sDay = getFormattedDateTime(currentDate, "dd");
		if (Integer.parseInt(sDay) <= 9) 
			sCurrentDate = getFormattedDateTime(currentDate, "MMMM d, yyyy");
		else	
			sCurrentDate = getFormattedDateTime(currentDate, "MMMM dd, yyyy");
		return sCurrentDate;		
	}	

	/**
	* returns a string of the current date in a specified format
	* @param sFormat - The date format to use. (i.e. "MMMM dd, yyyy"=September 21,2006, "MMM yyyy" = "Sep 2006")
	* @return String of the current date in the specified format 		
	*/
	public static String getCurrentDate(String sFormat)
	{
		//get current date
		long currentDate = System.currentTimeMillis() - glCalOffset;
		String sCurrentDate;
		
		//Format the current date information to string. Check if day is single or double digit
		String sDay = getFormattedDateTime(currentDate, "dd");
		if (Integer.parseInt(sDay) <= 9) 
			sCurrentDate = getFormattedDateTime(currentDate, sFormat);
		else	
			sCurrentDate = getFormattedDateTime(currentDate, sFormat);
		
		return sCurrentDate;
	}
	
	/**
	* returns a string of the current date in a specified format
	* @param sFormat - The date format to use. (i.e. "MMMM dd, yyyy"=September 21,2006, "MMM yyyy" = "Sep 2006")
	* @return String of the current date in the specified format 		
	*/
	public static String getCurrentDatePlusOne(String sFormat)
	{
		//get current date
		long glOneDay = 86400000;
		long currentDate = System.currentTimeMillis() - glCalOffset + glOneDay;
		String sCurrentDate;
		
		//Format the current date information to string. Check if day is single or double digit
		String sDay = getFormattedDateTime(currentDate, "dd");
		if (Integer.parseInt(sDay) <= 9) 
			sCurrentDate = getFormattedDateTime(currentDate, sFormat);
		else	
			sCurrentDate = getFormattedDateTime(currentDate, sFormat);
		
		return sCurrentDate;		
	}	
	
	/**
	 * returns the current date and time
	 * @return
	 */
	public static String getCurrentDateAndTime()
	{
		DateFormat dtformat = DateFormat.getDateInstance();
		DateFormat tmformat = DateFormat.getTimeInstance();
		return dtformat.format(new Date()) + " " + tmformat.format(new Date());
	}

	/**
	 * Extracts part of a date value from a given Date in string format. ie. getDatePartValue("11/5/04","/",0) returns 11) 
	 * @param sDate - Date in a String format (i.e. 11/5/04)
	 * @param sDelim - Date Delimeter. In above example would be "/"
	 * @param iPlacement - integer containing placement of the date part. In above example month iPlacement equal to 0 
	 * will return the month (11). If iPlacement is set to 1 then it would return the day (5) in the above example. If the 
	 * iPlacement is set to 2 then it return the year (04) in the above example.
	 */
	public static int getDatePartValue(String sDate, String sDelim, int iPlacement)
	{
		String[] lsDate = StringHelper.stringToStringArray(sDate, sDelim);
		
		return Integer.parseInt(lsDate[iPlacement]);
	}
	
	/** 
	* Generates String containing a Date Time Stamp	 <p>
	* @return a String containing a date-based unique value
	*/
	public static String getDateTimeStamp()
	{
		//generates String containing unique random date-based value	
		String s;
		Date d = new Date();
		//long l;

		//format
		SimpleDateFormat tmformat = new SimpleDateFormat("yyyyMMdd.HHmmss");
		s = tmformat.format(d);

		return s;
	}


	/** 
	* Generates String containing unique random date-based value	 <p>
	* @return a String containing a date-based unique value
	*/
	public static String genDateBasedRandVal()
	{
		//generates String containing unique random date-based value	
		String s;
		Date d = new Date();
		//long l;

		//format
		SimpleDateFormat tmformat = new SimpleDateFormat("MMddHHmmssSSSS");
		s = tmformat.format(d);

		return s;
	}

	/** 
	* Generates String containing unique random date-based value of a specified number of characters	 <p>
	* @param int i - number of characters to limit output to
	* @return a String containing a date-based unique value
	*/
	public static String genDateBasedRandVal(int i)
	{
		//generates String containing unique random date-based value	
		String s;
		Date d = new Date();
		//long l;

		//format
		SimpleDateFormat tmformat = new SimpleDateFormat("MMddHHmmss");
		s = tmformat.format(d);
		
		int ilen = s.length();
		
		return s.substring(ilen-i);		
	}

	/** 	
	* Sorts list of dates  <p>
	* @param String[] s - list of dates
 	* @param boolean bAscOrDeAsc - sort order
	*/
	public static String[] sortDate(String[] s,boolean bAscOrDeAsc)
	{
		int cnt;
		String temp;
		String sFormat = "M/d/yy H:mm";
		for(int i=0;i<s.length;i++)
		{
			cnt=0;
			for(int j=0;j<s.length-i-1;j++)
			{
				if(bAscOrDeAsc)
				{
					if(stringToDate(parseDate(s[j]),sFormat).after(stringToDate(parseDate(s[j+1]),sFormat)))
					{
						temp=s[j];
						s[j]=s[j+1];
						s[j+1]=temp;
						cnt++;
					}
				}
				else
				{
					if(stringToDate(parseDate(s[j]),sFormat).before(stringToDate(parseDate(s[j+1]),sFormat)))
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
	* Returns a date parse format  <p>
	* @param String sText - parse format
	* @return String parseformat
	*/
	public static String parseFormat(String sText) {
			String patten = "";
			String sDate="" ,sTime="";
			String sMonth,sDay,sYear,sHour,sZone;
			for(int x = 0;x<sText.length();)
			{					
				sDate = sText.substring(0,sText.indexOf(" ")).trim();
				break;
			}
			sText = sText.substring(sText.indexOf(" ")).trim();
			for(int x = 0;x<sText.length();)
			{
					sTime = sText.substring(0,sText.indexOf(" ")).trim();
					break;
			}
			for(int x = 0;x<sDate.length();)
			{
				sMonth = sDate.substring(0,sDate.indexOf("/")).trim();
				if(!sMonth.startsWith("0"))
					patten += "M/";
				break;
			}
			sDate = sDate.substring(sDate.indexOf("/")+1).trim();
			for(int x = 0;x<sDate.length();)
			{
				sDay = sDate.substring(0,sDate.indexOf("/")).trim();
				if(!sDay.startsWith("0"))
					patten += "d/";
				break;
			}
			sYear = sDate.substring(sDate.indexOf("/")+1).trim();
			if(sYear.length()==2)
				patten += "yy ";
			for(int x = 0;x<sTime.length();)
			{				
					sHour = sTime.substring(0,sTime.indexOf(":")).trim();
					if(!sHour.startsWith("0"))
						patten += "H:mm ";
					break;
			}
			
			sZone = sText.substring(sText.indexOf(" ")).trim();
			if(sZone.equalsIgnoreCase("AM")||sZone.equalsIgnoreCase("PM"))
				patten +="a";
			return patten;
	}		

	/** 	
	* Parses date  <p>
	* @param String sText - Date Text
	* @return String sText in date format
	*/
	public static String parseDate(String sText) {
			String s= sText;
			String sDate,sTime,sHour,sZone,sMinute;
			sZone = s.substring(s.length()-3,s.length()).trim();
			sDate = s.substring(0,s.indexOf(" ")).trim();
			s = s.substring(s.indexOf(" ")).trim();
			sTime = s.substring(0,s.indexOf(" ")).trim();
			sHour = sTime.substring(0,sTime.indexOf(":")).trim();
			sMinute = sTime.substring(sTime.indexOf(":")+1).trim();
			if(s.endsWith("PM"))
				sHour = String.valueOf(Integer.parseInt(sHour)+12);
			s = sDate+" "+sHour+":"+sMinute+" "+sZone;
			return s;
	}			

	/** 	
	* Returns string as Date type  <p>
	* @param String sDate - Date Text
	* @param String sFormat - format to display date in
	* @return Date
	*/	
	public static java.util.Date stringToDate( String sDate, String sFormat )
  	{
    	java.util.Date dDate = null;
   	 	try
    	{
     		 ParsePosition pos = new ParsePosition(0);
     		 SimpleDateFormat sdf = new SimpleDateFormat( sFormat );
      		if(sDate.endsWith("PM"))
    		{
      			dDate = sdf.parse( sDate, pos );
      		}
   			else
      		{
      			dDate = sdf.parse( sDate, pos );
      		}
    	}
   		catch ( Exception e )
   		{
      		return dDate;
    	}
    	return dDate;
  	}
  	
  	
	/** 	
	* Formats a date string as specified in sFormat  <p>
	* @param String sDate - Date Text i.e. "12/05/2006"
	* @param String sFormatIn - format to display date in i.e "MM/dd/yyyy"
	* @param String sFormatOut - format to display date in i.e "MMMM dd, yyyy"
	* @return String i.e. "December 5, 2006"
	*/	
	public static String formatDateString( String sDate, String sFormatIn, String sFormatOut )
  	{
		long lDate = getLongFromFormattedDateTime(sDate, sFormatIn);
		
		return getFormattedDateTime(lDate, sFormatOut);
  	}
	

	/** 	
	* Returns todays date in 2/23/05 format <p>
	* @return String - todays date
	*/
	public static String getTodaysDate(){
		java.util.Date today = new java.util.Date();

		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
			
		String dateOut = dateFormatter.format(today);
		return dateOut;		
	}

	/** 	
	* Returns date string value of todays date plus the specified days, months and years   
	* (i.e. if todays date is 2/23/05 then todayPlus(7,2,10) would return 4/30/2015)<p>
	* @return String - calculated date
	*/
	public static String todayPlus(int iDays,int iMonths,int iYears){
		String sDate = getTodaysDate();
		java.util.Calendar cal = java.util.Calendar.getInstance();
		String sNewDate = "";	
		
		try{
			boolean twoDigitYr = false;	
			int chA = sDate.trim().indexOf("/");	 
			int iMth = Integer.parseInt(sDate.substring(0,chA));
			int chB = sDate.trim().indexOf("/",chA+1);
			int intDy = Integer.parseInt(sDate.substring(chA+1,chB));
			int iYr = Integer.parseInt(sDate.substring(chB+1,sDate.length()));

			if (iYr < 100){
				twoDigitYr = true;
				iYr = iYr+2000;
			}
			//Set the calendar object with the Year/Month/Day info passed in
			cal.set(Calendar.MONTH,iMth-1);
			cal.set(Calendar.YEAR, iYr);
			cal.set(Calendar.DAY_OF_MONTH, intDy);
						
			//Increment that field accordingly.
			cal.add(java.util.Calendar.YEAR,iYears);
			cal.add(java.util.Calendar.MONTH,iMonths);
			cal.add(java.util.Calendar.DAY_OF_MONTH,iDays);
		
			
			//To return the new "Date", need to construct format
			//Have to increment the Month by 1 because it is zero based.
			if (twoDigitYr == true){
				String str = Integer.toString(cal.get(1));
				sNewDate = (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH)+"/"+ str;
			}
			else
			sNewDate = (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH)+"/"+ cal.get(Calendar.YEAR);

		} catch (Exception e){
			
		}
				
		return sNewDate;		
	}
	
	public static String sGetCurrentTimePlusX (int iMinsToAdd)
	{
		String sHour;
		String sMins;
		String sTime;
		String sNewHour;
		String sNewMins;
		int iMins;
		int iHours;
		Date dToday = new Date();
		String sToday = dToday.toString();
		sTime = sToday.substring(sToday.indexOf(":")-2);
		sTime = sTime.substring(0,sTime.indexOf(" "));
		sHour = sTime.substring(0,sTime.indexOf(":"));
		sMins = sTime.substring(sTime.indexOf(":")+1,sTime.lastIndexOf(":"));
		iMins = Integer.parseInt(sMins);
		iHours = Integer.parseInt(sHour);
		if ((iMins+iMinsToAdd) > 59){
			sNewMins=String.valueOf((iMins+iMinsToAdd)-60);
			sNewHour=String.valueOf(iHours+1);
		}
		else {sNewMins=String.valueOf(iMins+iMinsToAdd);
		sNewHour=sHour;}
		if (sNewMins.length()==1)
			sNewMins = "0" + sNewMins;
		
		return sNewHour+":"+sNewMins;
	}
	public static String sGetCurrentTimePlusX12 (int iMinsToAdd)
	{
		String sHour;
		String sMins;
		String sTime;
		String sNewHour;
		String sNewMins;
		int iMins;
		int iHours;
		Date dToday = new Date();
		String sToday = dToday.toString();
		sTime = sToday.substring(sToday.indexOf(":")-2);
		sTime = sTime.substring(0,sTime.indexOf(" "));	
		sHour = sTime.substring(0,sTime.indexOf(":"));
		sMins = sTime.substring(sTime.indexOf(":")+1,sTime.lastIndexOf(":"));
		iMins = Integer.parseInt(sMins);
		iHours = Integer.parseInt(sHour);
		if ((iMins+iMinsToAdd) > 59){
			sNewMins=String.valueOf((iMins+iMinsToAdd)-60);
			iHours=iHours+1;
			}
		else {
			sNewMins=String.valueOf(iMins+iMinsToAdd);
			}
		if (sNewMins.length()==1)
			sNewMins = "0" + sNewMins;
		if (iHours>12)
			iHours=iHours-12;
		sNewHour=String.valueOf(iHours);
	return sNewHour+":"+sNewMins;
	}
}
