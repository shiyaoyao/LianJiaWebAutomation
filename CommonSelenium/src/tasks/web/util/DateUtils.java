package tasks.web.util;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateUtils {
	private static final Logger log = Logger.getLogger(DateUtils.class);
	
	public static final String regexStartEnd = ".*?([0-9]+:[0-9APM]+\\s*-\\s*[0-9]+:[0-9APM]+).*";
	public static final String regexTime = ".*?([0-9]+:[0-9APM]+).*";
	
	public static final String DATEFORMAT_RAW = "yyyyMMdd";
	public static final String DATEFORMAT_SLASH_MMDDYYY = "MM/dd/yyyy";
	public static final String DATEFORMAT_DASH_MMDDYYY = "MM-dd-yyyy";
	public static final String DATEFORMAT_DOT_DDMMYYY = "dd.MM.yy";
	public static final String DATEFORMAT_DOT_YYYYMMDD = "yyyy.MM.dd";
	public static final String DATEFORMAT_COMA_MMMDDYYYY = "MMM dd, yyyy";
	public static final String DATEFORMAT_COMA_MMMMDDYYYY = "MMMM dd, yyyy";	
	public static final String DATEFORMAT_SPACE_DDMMMMYYY = "dd MMMMM yyyy";
	public static final String DATEFORMAT_SLASH_DOW_MMDDYYYY = "EEE MM/dd/yyyy";
	public static final String DATEFORMAT_DOW_MMDDYYYY_TIME = "EEE MM/dd/yyyy hh:mmaaa";
	public static final String DATEFORMAT_FULL = "EEEE, MMMM dd, yyyy";
	public static final String DATEFORMAT_FULL_TIME = "EEEE, MMMM dd, yyyy hh:mmaaa";
	public static final String DATEFORMAT_MONTHYEAR = "MMMM yyyy";
	public static final String DATEFORMAT_12HR = "hh:mmaaa";
	public static final String DATEFORMAT_24HR = "HH:mm";
	public static final String DATEFORMAT_CALENDAR_PROPERTY = "yyyyMMdd HHmmss";
	public static final String DATEFORMAT_ICAL = "yyyyMMdd'T'HHmmss";
	public static final String DATEFORMAT_MMM_DD_12HR = "MMM dd hh:mmaaa";
	
	public static final String simpleDateFormat = DATEFORMAT_SLASH_DOW_MMDDYYYY;

	public static String[] allDateFormats = { DATEFORMAT_SLASH_MMDDYYY,
		DATEFORMAT_DASH_MMDDYYY, DATEFORMAT_DOT_DDMMYYY, DATEFORMAT_DOT_YYYYMMDD, 
		DATEFORMAT_COMA_MMMDDYYYY, DATEFORMAT_COMA_MMMMDDYYYY, DATEFORMAT_SPACE_DDMMMMYYY, 
		DATEFORMAT_DOW_MMDDYYYY_TIME, DATEFORMAT_SLASH_DOW_MMDDYYYY, DATEFORMAT_FULL_TIME,
		DATEFORMAT_FULL, DATEFORMAT_MONTHYEAR, DATEFORMAT_MMM_DD_12HR, DATEFORMAT_CALENDAR_PROPERTY, 
		DATEFORMAT_ICAL, DATEFORMAT_RAW, DATEFORMAT_12HR, DATEFORMAT_24HR };


	//TODO GET NUMBER OF WEEKS IN CURRENT YEAR
	public static int getNumberOfWeeksInYear(){
		Calendar today = Calendar.getInstance();
		return getNumberOfWeeksInYear(today.get(Calendar.YEAR));
	}

	public static int getNumberOfWeeksInYear(int iYear){
		Calendar last = Calendar.getInstance();

		last.setFirstDayOfWeek(Calendar.MONDAY);
		last.setMinimalDaysInFirstWeek(1);

		last.set(iYear,Calendar.DECEMBER, 31);
		last.getTime();
		if(last.get(Calendar.WEEK_OF_YEAR)==1){
			last.add(Calendar.DATE, -7);
		}

		return last.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getNumberOfWeeksInYearISO(){
		Calendar last = Calendar.getInstance();
		last.setFirstDayOfWeek(Calendar.MONDAY);
		last.setMinimalDaysInFirstWeek(4);
		int iYear=last.get(Calendar.YEAR);
		last.set(iYear,Calendar.DECEMBER, 31);
		last.getTime();
		if(last.get(Calendar.WEEK_OF_YEAR)==1){
			last.add(Calendar.DATE, -7);
		}

		return last.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getNumberOfWeeksInYear(String sDate, String sFormat){
		return getNumberOfWeeksInYear(sDate, sFormat, Calendar.MONDAY, 1);
	}

	public static int getNumberOfWeeksInYearISO(String sDate, String sFormat){
		return getNumberOfWeeksInYear(sDate, sFormat, Calendar.MONDAY, 4);
	}

	/**
	 * <p>Returns the number of weeks in the specified year.</p>
	 * <p>The number of weeks in a given year can depend on whether the calendar format
	 * uses Sunday or Monday as the first day of the week. </p>
	 * @param sDate
	 * @param sFormat
	 * @param iFirstDayOfWeek
	 * @param iMinDaysInFirstWeek
	 * @return
	 */
	public static int getNumberOfWeeksInYear(String sDate, String sFormat, int iFirstDayOfWeek, int iMinDaysInFirstWeek){
		Date myDate = stringToDate(sDate, sFormat);
		Calendar last = Calendar.getInstance();
		int iYear;
		last.setTime(myDate);
		iYear=last.get(Calendar.YEAR);
		last.setFirstDayOfWeek(iFirstDayOfWeek);
		last.setMinimalDaysInFirstWeek(iMinDaysInFirstWeek);

		last.set(iYear,Calendar.DECEMBER, 31);
		last.getTime();
		if(last.get(Calendar.WEEK_OF_YEAR)==1){
			last.add(Calendar.DATE, -7);
		}

		return last.get(Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * Returns the current week of the current year. Assumes first day of
	 * the week is Monday.
	 * 
	 */
	public static int getWeekOfYear(){
		Calendar calToday = Calendar.getInstance();
		calToday.setFirstDayOfWeek(Calendar.MONDAY);
		calToday.setMinimalDaysInFirstWeek(1);
		return calToday.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getWeekOfYear(String sDate, String sFormat){
		return getWeekOfYear(sDate, sFormat, Calendar.MONDAY, 1);
	}

	/**
	 * Returns the week of the year is the specified date in ISO format.
	 * 
	 * @param sDate string value of date
	 * @param sFormat simple date format of date
	 * @return
	 */
	public static int getWeekOfYearISO(String sDate, String sFormat){
		return getWeekOfYear(sDate, sFormat, Calendar.MONDAY, 4);
	}

	/**
	 * Returns the week of the year in the specified date based on rules for minimum 
	 * week length
	 * 
	 * @param sDate string value of date
	 * @param sFormat simple format of date
	 * @param iFirstDayOfWeek first day of week rule (Sunday, Monday, etc)
	 * @param iMinDaysInFirstWeek minimum number of days required to qualify as the fist week of the year
	 * @return
	 */
	public static int getWeekOfYear(String sDate, String sFormat, int iFirstDayOfWeek, int iMinDaysInFirstWeek){
		Calendar calDate = Calendar.getInstance();
		Date myDate = stringToDate(sDate, sFormat);
		SimpleDateFormat format = new SimpleDateFormat(sFormat);
		calDate.setFirstDayOfWeek(iFirstDayOfWeek);
		calDate.setMinimalDaysInFirstWeek(iMinDaysInFirstWeek);
		try{
			myDate = format.parse(sDate);
			calDate.setTime(myDate);
		} catch (ParseException e){
			calDate.set(1970,0,1);
			return calDate.get(Calendar.YEAR);
		}
		return calDate.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * Method finds the week number of specific date: sDate. And then finds the date of the
	 * first day of the week 
	 * @param sDate string value of date
	 * @param iFirstDayOfWeek first day of week rule (Sunday, Monday, etc)
	 * @return
	 */
	public static String getFirstDateOfWeekOfGivenDate(String sDate, int iFirstDayOfWeek){
		Calendar cal = Calendar.getInstance();
		String sFormat = DateUtils.getFormatOfDate(sDate);
		Date date = DateUtils.stringToDate(sDate, sFormat);
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, iFirstDayOfWeek);
		Date mondayDate = cal.getTime();
		String sFirstOfWeek = dateToString(mondayDate, sFormat);
		return sFirstOfWeek;		
	}

	/**
	 * Convert the specified string date in the given format to a java.util.Date
	 * 
	 * @return Date conversion of String
	 */
	public static Date stringToDate(String sDate, String sFormat){
		if(sFormat == null || sFormat.isEmpty()){
			sFormat = getFormatOfDate(sDate);
		}
		SimpleDateFormat df = new SimpleDateFormat(sFormat);
		Date dDate;
		try{
			dDate = df.parse(sDate);
		} catch (ParseException e){
			Calendar calDate = Calendar.getInstance();
			calDate.set(1970,0,1);
			return calDate.getTime();
		}
		return dDate;
	}

	/**
	 * Converts the specified Date value into a string date of the specified format.
	 * 
	 * @param dt java.util.Date value
	 * @param sFormat simple date format of returned string (e.g. MM/dd/yyyy)
	 * @return
	 */
	public static String dateToString(Date dt, String sFormat){
		SimpleDateFormat df = new SimpleDateFormat(sFormat);
		String sDate ="";
		try{
			sDate = df.format(dt);
			return sDate;
		}catch (Exception e){
			return "";
		}
	}
	
	/**
	 * Returns the day of the week (Sunday, Monday) of the specified date
	 * 
	 * @param sDate string value of date
	 * @param sFormat simple date format of passed string
	 * @return
	 */
	public static int getDayOfWeek(String sDate, String sFormat){
		Date myDate = stringToDate(sDate, sFormat);
		Calendar myCal =Calendar.getInstance();
		myCal.setTime(myDate);
		return myCal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Returns the Calendar.DAY_OF_WEEK int value of <code>sDate</code> (Sunday==1) 
	 * 
	 * @param sDate
	 * Date string
	 * @return
	 * Calendar.DAY_OF_WEEK int
	 */
	public static int getDayOfWeek(String sDate){
		String sFormat = getFormatOfDate(sDate);
		Date myDate = stringToDate(sDate, sFormat);
		Calendar myCal =Calendar.getInstance();
		myCal.setTime(myDate);
		return myCal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Returns the Calendar.DAY_OF_WEEK int value of the current day of the week (Sunday==1)
	 * 
	 * @return
	 */
	public static int getDayOfWeek(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static String getDayOfWeekString(int iDay){
		switch (iDay){
		case 1:	return "Sunday";
		case 2: return "Monday";
		case 3: return "Tuesday";
		case 4: return "Wednesday";
		case 5: return "Thursday";
		case 6: return "Friday";
		case 7: return "Saturday";
		default: return "";
		}
	}

	/**
	 * Returns the day of the week of the specified date as a string (Monday, Tuesday...)
	 * 
	 * @param sDate string value of date
	 * @param sFormat simple date format
	 * @return
	 */
	public static String getDayOfWeekString(String sDate, String sFormat){
		int i = getDayOfWeek(sDate, sFormat);
		return getDayOfWeekString(i);
	}

	/**
	 * <p>Finds the the next specified day of week from the current date.</p>
	 * <p>The returned string is in format MM/dd/yyyy</p>
	 * @param iDayOfWeek Calendar.DAY_OF_WEEK integer where Sunday == 1
	 * @return 
	 */
	public static String getNextDayOfWeekFromToday(int iDayOfWeek){
		SimpleDateFormat dfRaw = new SimpleDateFormat(DATEFORMAT_SLASH_DOW_MMDDYYYY);
		Calendar cal = Calendar.getInstance();
		int days = iDayOfWeek-cal.get(Calendar.DAY_OF_WEEK);
		if (days<=0){
			days = 7+days;
		}
		cal.add(Calendar.DAY_OF_YEAR, days);
		return (dfRaw.format(cal.getTime()));
	}

	/**
	 * <p>Finds the next specified day of week from the passed date</p>
	 * <p>The returned string value is in raw date format: yyyyMMdd</p>
	 * @param sDate string value of date
	 * @param sFormat simple date format
	 * @param iDayOfWeek Calendar.DAY_OF_WEEK, Sunday==1
	 * @return the date of the next day-of-week as a string
	 */
	public static String getNextDayOfWeekFromDate(String sDate, int iDayOfWeek){
		String sFormat = getFormatOfDate(sDate);
		SimpleDateFormat dfRaw = new SimpleDateFormat(sFormat);
		Calendar cal = Calendar.getInstance();
		cal.setTime(stringToDate(sDate,sFormat));
		int days = iDayOfWeek-cal.get(Calendar.DAY_OF_WEEK);
		if (days<0){
			days = 7+days;
		}
		cal.add(Calendar.DAY_OF_YEAR, days);
		return (dfRaw.format(cal.getTime()));
	}
	
	/**
	 * Finds the next weekday from the specified date.<br>
	 * The returned date is in the same format as the passed date.
	 * @param sDate string value of date
	 * @return the date of the next weekday as a string.
	 */
	public static String getNextWeekdayFromDate(String sDate) {
		String sFormat = getFormatOfDate(sDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(stringToDate(sDate,sFormat));
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		int addDays = 1;
		if(dow == Calendar.FRIDAY)
			addDays = 3;
		if (dow == Calendar.SATURDAY)
			addDays = 2;
			
		return plusDays(sDate, addDays);
	}

	/**
	 * Finds the nearest weekday from the specified date.<br>
	 *  If the passed date is already a weekday, the same date is returned. The returned
	 *  date is in the same format as the passed date.
	 * @param sDate string value of date
	 * @return the date of the nearest weekday as a string.
	 */
	public static String getNearestWeekdayFromDate(String sDate) {
		String sFormat = getFormatOfDate(sDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(stringToDate(sDate,sFormat));
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		int addDays = 0;
		if(dow == Calendar.SUNDAY)
			addDays = 1;
		if (dow == Calendar.SATURDAY)
			addDays = -1;
			
		return plusDays(sDate, addDays);
	}
	/**
	 * 
	 * <p>Finds the previous specified day of week from the passed date</p>
	 * <p>The returned string value is in raw date format: yyyyMMdd</p>
	 * @param sDate string value of date
	 * @param sFormat simple date format
	 * @param iDayOfWeek Calendar.DAY_OF_WEEK, Sunday==1
	 * @return
	 */
	public static String getPreviousDayOfWeekFrom(String sDate, String sFormat, int iDayOfWeek){
		SimpleDateFormat dfRaw = new SimpleDateFormat(sFormat);
		Calendar cal = Calendar.getInstance();
		cal.setTime(stringToDate(sDate,sFormat));
		int offset =0;
		offset = iDayOfWeek<cal.get(Calendar.DAY_OF_WEEK)?0:7;
		int days = Math.abs(iDayOfWeek-cal.get(Calendar.DAY_OF_WEEK));
		cal.add(Calendar.DAY_OF_YEAR, -(Math.abs(offset-days)));
		return (dfRaw.format(cal.getTime()));
	}

	/**
	 * Returns true if the passed int is 1 or 7 (Calendar.DAY_OF_WEEK values for Saturday
	 * and Sunday)
	 * 
	 * @param iDay
	 * @return
	 */
	public static boolean isWeekend(int iDay){
		return (iDay==1||iDay==7)?true:false;
	}

	/**
	 * Returns true if the current day of the week is Saturday or Sunday (Calendar.DAY_OF_WEEK values 1 or 7)
	 *
	 * @return
	 */
	public static boolean isWeekend(){
		return isWeekend(getDayOfWeek());
	}

	/**
	 * Returns true if the passed date is on Saturday or Sunday
	 * 
	 * @param sDate
	 * @param sFormat
	 * @return
	 */
	public static boolean isWeekend(String sDate, String sFormat){
		return isWeekend(getDayOfWeek(sDate, sFormat));
	}

	/**
	 * Returns the day of the year of the specified date. Value is based on Calendar.DAY_OF_YEAR.
	 * @param sDate
	 * @param sFormat
	 * @return
	 */
	public static int getDayOfYear(String sDate, String sFormat){
		Date myDate = stringToDate(sDate, sFormat);
		Calendar myCal =Calendar.getInstance();
		myCal.setTime(myDate);
		return myCal.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * Returns the last day of the year of the specified date. Value is based on Calendar.DAY_OF_YEAR.
	 * @param sDate
	 * @param sFormat
	 * @return
	 */
	public static int getLastDayOfYear(String sDate, String sFormat){
		Calendar myCal = Calendar.getInstance();
		myCal.setTime(stringToDate(sDate, sFormat));
		int iYear = myCal.get(Calendar.YEAR);
		myCal.set(iYear, Calendar.DECEMBER, 31);
		return myCal.get(Calendar.DAY_OF_YEAR);

	}

	/**
	 * Returns the int value of the month of the specified date. 1=January..12=December
	 * @param sDate Date as string
	 * @param sFormat date pattern of the string
	 * @return int value of month 1 through 12
	 */
	public static int getMonth(String sDate, String sFormat){
		Calendar myCal = Calendar.getInstance();
		myCal.setTime(stringToDate(sDate, sFormat));
		return myCal.get(Calendar.MONTH)+1;
	}

	public static String getMonth(String sDate){
		String sFormat = getFormatOfDate(sDate);
		Calendar myCal = Calendar.getInstance();
		myCal.setTime(stringToDate(sDate, sFormat));
		int iMonth = myCal.get(Calendar.MONTH)+1;
		return new DateFormatSymbols().getMonths()[iMonth-1];
	}
	
	/**
	 * Returns the current date in the string format MM/dd/yyyy
	 * 
	 * @return
	 */
	public static String getToday(){
		SimpleDateFormat dfFormat = new SimpleDateFormat(DATEFORMAT_SLASH_DOW_MMDDYYYY);
		Calendar myCal = Calendar.getInstance();
		return dfFormat.format(myCal.getTime());
	}

	public static String getNow(){
		return dateToString(new Date(System.currentTimeMillis()), DATEFORMAT_ICAL);
	}
	/**
	 * Returns tomorrow's date in the string format MM/dd/yyyy
	 * 
	 * @return
	 */
	public static String getTomorrow(){
		SimpleDateFormat dfFormat = new SimpleDateFormat(DATEFORMAT_SLASH_DOW_MMDDYYYY);
		Calendar myCal = Calendar.getInstance();
		myCal.add(Calendar.DAY_OF_YEAR, 1);
		return dfFormat.format(myCal.getTime());
	}

	/**
	 * Returns yesterday's date in the string format MM/dd/yyyy
	 * 
	 * @return
	 */
	public static String getYesterday(){
		SimpleDateFormat dfFormat = new SimpleDateFormat(DATEFORMAT_SLASH_MMDDYYY);
		Calendar myCal = Calendar.getInstance();
		myCal.add(Calendar.DAY_OF_YEAR, -1);
		return dfFormat.format(myCal.getTime());
	}

	/**
	 * <p>Returns a safe date and time to create a calendar event. Avoids weekends and midnight rollover.</p>
	 * <ul>
	 * <li> String[0] = Date in format MM/dd/yyyy
	 * <li> String[1] = Time in 24hr format
	 * <li> String[2] = Day of week in short format, Mon, Tues,..
	 * </ul>
	 * 
	 * @return
	 */
	public static String[] getEventDateTime(){
		String sFormat12Hr = DATEFORMAT_SLASH_MMDDYYY+";hh:mmaaa;EEE";
		String sFinal="";
		Calendar mycal = Calendar.getInstance();
		mycal.add(Calendar.DAY_OF_YEAR, 1);
		mycal.add(Calendar.HOUR_OF_DAY, 1);

		int iDow = mycal.get(Calendar.DAY_OF_WEEK);
		int iHour = mycal.get(Calendar.HOUR_OF_DAY);
		int iMinute = mycal.get(Calendar.MINUTE);
		if (iDow==1||iDow==7){
			mycal.add(Calendar.DAY_OF_YEAR, 2);
		}

		if (iHour > 21){
			iHour = iHour - 3;
			mycal.set(Calendar.HOUR_OF_DAY, iHour);
		} else if (iHour < 4) {
			iHour = iHour + 4;
			mycal.set(Calendar.HOUR_OF_DAY, iHour);
		}

		if(iMinute>45){
			iMinute=0;
		}else if(iMinute>30){
			iMinute=45;
		}else if(iMinute>15){
			iMinute=30;
		}else{
			iMinute=15;
		}

		mycal.set(Calendar.MINUTE, iMinute);

		sFinal = dateToString(mycal.getTime(), sFormat12Hr);
		return sFinal.split(";");
	}
	
	public static String getEventTime(boolean bAMPM) {
		String sTime = getEventDateTime()[1];
		sTime = changeDateFormat(sTime, DATEFORMAT_24HR);
		String sHour = sTime.split(":")[0];
		int iHour = Integer.valueOf(sHour);
		if(iHour > 19){
			iHour -= 3;
			sTime = sTime.replace(sHour+":", String.valueOf(iHour)+":");
		} else if (iHour < 4) {
			iHour += 4;
			sTime = sTime.replace(sHour+":", String.valueOf(iHour)+":");
		}
		if(bAMPM)
			return convertTo12HourFormat(sTime);
		return sTime;
	}

	public static String getEventDate() {
		return changeDateFormat(getEventDateTime()[0], DATEFORMAT_SLASH_DOW_MMDDYYYY);
	}
	
	public static String getEventTime() {
		return getEventTime(true);
	}
	
	public static String roundCurrentTimeToHalfHour() {
		Calendar mycal = Calendar.getInstance();
		int hr = mycal.get(Calendar.HOUR);
		int min = mycal.get(Calendar.MINUTE);
		if(min > 30) {
			mycal.set(Calendar.HOUR, hr+1);
			mycal.set(Calendar.MINUTE, 0);
		} else if (min > 0) {
			mycal.set(Calendar.MINUTE, 30);
		}
		return dateToString(mycal.getTime(), DATEFORMAT_12HR);
	}
	
	/**
	 * Method checks if a string is a date based on the format passed in or not.
	 * @param toBeTested
	 * @param sFormat
	 * @return
	 */
	public static boolean isStringADate(String toBeTested, String sFormat) {
		SimpleDateFormat df = new SimpleDateFormat(sFormat);
		try {
			df.parse(toBeTested);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * Method checks if a string is a date based on the list of famously used formats or not.
	 * @param toBeTested
	 * @return
	 */
	public static boolean isStringADate(String sDate) {
		String sFormat = getFormatOfDate(sDate);
		if(sFormat.isEmpty()){
			log.warn("("+sDate+") is not a recognized date format. See tasks.web.util.DateUtils for recognized DATEFORMT patterns");
			StringBuilder sb = new StringBuilder();
			for(String df : allDateFormats){
				sb.append(df+"<br>\n");
			}
			log.debug("Valid date formats:<br>"+sb.toString());
			return false;
		}
		return true;

	}

	/**
	 * Returns the format of a date if it matches any of the predefined formats
	 * @param Date
	 * @return format
	 */
	public static String getFormatOfDate(String Date) {
		for (int i = 0; i < allDateFormats.length; i++) {
			if (isStringADate(Date, allDateFormats[i]))
				return allDateFormats[i];
		}
		return "";

	}

	/**
	 * Compares two dates and returns true if only they are equal
	 * @param FirstDate
	 * @param secDate
	 * @return true if two dates are equal, false if not.
	 */
	public static boolean compare(String firstDate, String secDate) {
		Date Date1 = null;
		Date Date2 = null;
		String sFormat1 = getFormatOfDate(firstDate);
		String sFormat2 = getFormatOfDate(secDate);
		
		if (!sFormat1.isEmpty() && !sFormat2.isEmpty()) {
			Date1 = stringToDate(firstDate, sFormat1);
			Date2 = stringToDate(secDate, sFormat2);
			if (Date1.equals(Date2))
				return true;
			else
				return false;
		} else
			return false;
	}

	/**
	 * Compares two string dates
	 * @param firstDate
	 * @param secDate
	 * @return 
	 * <code>-1: </code> firstDate is BEFORE secondDate
	 * <br><code>&nbsp0: </code> firstDate is SAME as secondDate
	 * <br><code>&nbsp1: </code> firstDate is AFTER secondDate
	 */
	public static int compareBeforeAfter(String firstDate, String secDate) {
		Date Date1 = null;
		Date Date2 = null;
		String sFormat1 = getFormatOfDate(firstDate);
		String sFormat2 = getFormatOfDate(secDate);
		
		if (!sFormat1.isEmpty() && !sFormat2.isEmpty()) {
			Date1 = stringToDate(firstDate, sFormat1);
			Date2 = stringToDate(secDate, sFormat2);
			if (Date1.before(Date2))
				return -1;
			else if (Date1.after(Date2))
				return 1;
			else
				return 0;
		} else
			return 999;
	}
	
	/**
	 * <p>Adds plusDays to the string date sDate.</br>
	 * plusDays can be negative to calculate a date in the past.</p>
	 * <p>The method attempts to detect the format pattern of the string date. If it
	 * can't determine the format the passed sDate is returned unchanged.</br>
	 * sDate should only contain month, day and year information. sDate values that include
	 * time or day of week information are not supported</p>
	 * @param sDate
	 * @param plusDays
	 * @return
	 */
	public static String plusDays(String sDate, int plusDays){
		String sFormat = getFormatOfDate(sDate);
		if(!sFormat.equals("")){
			Date dt = stringToDate(sDate, sFormat);
			Calendar mycal = Calendar.getInstance();
			mycal.setTime(dt);
			mycal.add(Calendar.DAY_OF_YEAR, plusDays);
			sDate = dateToString(mycal.getTime(), sFormat);
		} else {
			log.error("Unknown date format for "+sDate);
		}
		return sDate;
	}

	/**
	 * <p>Adds plusDays to the string date sDate.</br>
	 * plusDays can be negative to calculate a date in the past.</p>
	 * <p>The method attempts to detect the format pattern of the string date. If it
	 * can't determine the format the passed sDate is returned unchanged.</p>
	 * <p>The calculated date is returned in the format specified by sFormatOut</p>
	 * @param sDate
	 * @param plusDays
	 * @param sFormatOut
	 * @return
	 */
	public static String plusDays(String sDate, int plusDays, String sFormatOut){
		String sFormat = getFormatOfDate(sDate);
		if(!sFormat.equals("")){
			Date dt = stringToDate(sDate, sFormat);
			Calendar mycal = Calendar.getInstance();
			mycal.setTime(dt);
			mycal.add(Calendar.DAY_OF_YEAR, plusDays);
			sDate = dateToString(mycal.getTime(), sFormatOut);
		} else {
			log.error("Unknown date format for "+sDate);
		}
		return sDate;
	}

	/**
	 * <p>Adds plusDays to the string date sDate.</br>
	 * plusDays can be negative to calculate a date in the past.</p>
	 * <p>The method attempts to detect the format pattern of the string date. If it
	 * can't determine the format the passed sDate is returned unchanged.</br>
	 * sDate should only contain month, day and year information. sDate values that include
	 * time or day of week information are not supported</p>
	 * @param sDate
	 * @param plusDays
	 * @return
	 */
	public static String plusHours(String sTime, int plusTime){
		String sFormat = getTimeFormat(sTime);
		Date dt = stringToDate(sTime, sFormat);
		Calendar mycal = Calendar.getInstance();
		mycal.setTime(dt);
		mycal.add(Calendar.HOUR, plusTime);
		sTime = dateToString(mycal.getTime(), sFormat);
		return sTime;
	}
	
	public static Date plusHours(Date dt, int plusTime) {
		Calendar mycal = Calendar.getInstance();
		mycal.setTime(dt);
		mycal.add(Calendar.HOUR, plusTime);
		return mycal.getTime();
	}
	
	public static String plusHoursAndMinutes(String sTime, int plusHours, int plusMins){
		Date dt = stringToDate(sTime, "hh:mmaaa");
		Calendar mycal = Calendar.getInstance();
		mycal.setTime(dt);
		mycal.add(Calendar.HOUR, plusHours);
		mycal.add(Calendar.MINUTE, plusMins);
		sTime = dateToString(mycal.getTime(), "hh:mmaaa");
		
		return sTime;
	}
	
	public static String plusMinutes(String sDateTime, int plusMins){
		String sFormat = getFormatOfDate(sDateTime);
		if(sFormat.isEmpty())
			return sDateTime;
		Calendar mycal = Calendar.getInstance();
		mycal.setTime(stringToDate(sDateTime, sFormat));
		mycal.add(Calendar.MINUTE, plusMins);
		
		return dateToString(mycal.getTime(), sFormat);
	}
	
	public static Date plusHoursAndMinutes(Date dt, int plusHours, int plusMins){
		Calendar mycal = Calendar.getInstance();
		mycal.setTime(dt);
		mycal.add(Calendar.HOUR, plusHours);
		mycal.add(Calendar.MINUTE, plusMins);
		
		return mycal.getTime();
	}
	
	public static String plusDaysHoursMin(String dt, int days, int hours, int min) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(stringToDate(dt,DATEFORMAT_DOW_MMDDYYYY_TIME));
		cal.add(Calendar.DAY_OF_YEAR, days);
		cal.add(Calendar.HOUR, hours);
		cal.add(Calendar.MINUTE, min);
		return DateUtils.dateToString(cal.getTime(),DATEFORMAT_DOW_MMDDYYYY_TIME);
	}
	
	public static String plusMonths(String date, int months){
		Calendar mycal = Calendar.getInstance();
		date = DateUtils.simpleDate(date, true);
		mycal.setTime(DateUtils.stringToDate(date, simpleDateFormat));
		mycal.add(Calendar.MONTH, months);
		String sdate = DateUtils.dateToString(mycal.getTime(), simpleDateFormat);
		return sdate;
	}
	
	public static String plusYears(String date, int years){
		Calendar mycal = Calendar.getInstance();
		date = DateUtils.simpleDate(date, true);
		mycal.setTime(DateUtils.stringToDate(date, simpleDateFormat));
		mycal.add(Calendar.YEAR, years);
		String sdate = DateUtils.dateToString(mycal.getTime(), simpleDateFormat);
		return sdate;
	}
	
	public static String plusCalendar(String date, int iCalendarField, int iPlus){
		Calendar mycal = Calendar.getInstance();
		date = DateUtils.simpleDate(date, true);
		mycal.setTime(DateUtils.stringToDate(date, simpleDateFormat));
		mycal.add(iCalendarField, iPlus);
		String sdate = DateUtils.dateToString(mycal.getTime(), simpleDateFormat);
		return sdate;
	}
	/**
	 * Takes the passed date in sFormatIn format and returns it in sFormatOut format
	 * 
	 * @param sDate
	 * @param sFormatIn
	 * @param sFormatOut
	 * @return
	 */
	public static String changeDateFormat(String sDate, String sFormatIn, String sFormatOut){
		Date dt = stringToDate(sDate, sFormatIn);
		String sd = dateToString(dt, sFormatOut);
		return sd;
	}

	/**
	 * Takes the passed date, determines its format and returns it in sFormatOut format
	 * 
	 * @param sDate
	 * @param sFormatIn
	 * @param sFormatOut
	 * @return
	 */
	public static String changeDateFormat(String sDate, String sFormatOut){
		String sFormatIn = getFormatOfDate(sDate);
		Date dt = stringToDate(sDate, sFormatIn);
		String sd = dateToString(dt, sFormatOut);
		return sd;
	}

	/**
	 * Converts the given string date to the simple format</br>
	 * 
	 * @param sDate Sting date to convert
	 * @param bIncludeDayOfWeek <br>if true, returns date in format: <code>EEE MM/dd/yyyy</code></br>
	 * if false, returns date in format: <code>MM/dd/yyyy</code>
	 * @return
	 */
	public static String simpleDate(String sDate, boolean bIncludeDayOfWeek) {
		if(bIncludeDayOfWeek)
			return changeDateFormat(sDate, DATEFORMAT_SLASH_DOW_MMDDYYYY);
		return changeDateFormat(sDate, DATEFORMAT_SLASH_MMDDYYY);
	}
	
	public static String simpleDateTime(String sDate) {
		String outFormat = sDate.matches(".* \\d{1,2}:\\d\\d[APM]*$") || sDate.matches(".*T\\d{4}.*")?  " hh:mmaaa" : "";
		if(sDate.matches(".* \\d{1,2}:\\d\\d[APM]*$"))
			sDate = sDate.substring(0,sDate.lastIndexOf(' '))+" "+time12H(sDate.substring(sDate.lastIndexOf(' ')).trim());
		return changeDateFormat(sDate, DATEFORMAT_SLASH_DOW_MMDDYYYY+outFormat);
		
	}
	
	public static String deltaDaysHoursMins(long d1, long d2) {
		boolean isNeg = (d2-d1) < 0;
		long dif = Math.abs(d1 - d2);
		long ldays = 24 * 60 * 60 * 1000;
		long lhrs = 60 * 60 * 1000;
		long lmin = 60 * 1000;
		String sdif = "";

		int temp = (int) (dif / ldays);
		if (temp >= 0) {
			sdif = String.format("%02dd ", temp);
			dif = dif - (temp * ldays);
		}
		temp = (int) (dif / lhrs);
		if (temp >= 0) {
			sdif = sdif + String.format("%02dh ", temp);
			dif = dif - (temp * lhrs);
		}
		temp = (int) (dif / lmin);
		if (temp >= 0) {
			sdif = sdif + String.format("%02dm", temp);
			dif = dif - (temp * lmin);
		}
		return isNeg ? "-" + sdif : sdif;
	}
	
	public static String deltaDaysHoursMins(String sDate1, String sDate2) {
		long longDate1 = DateUtils.stringToDate(sDate1, null).getTime();
		long longDate2 = DateUtils.stringToDate(sDate2, null).getTime();
		return deltaDaysHoursMins(longDate1, longDate2);
	}

	/**
	 * Converts the given string date to a verbose format
	 * @param sDate The string date to convert
	 * @param bIncludeTime<br>if true, returns date in format: <code>EEEE, MMMM dd, yyyy</code><br>
	 * if false, returns date in format: <code>EEEE, MMMM dd, yyyy hh:mmaaa</code>
	 * @return
	 */
	public static String fullDate(String sDate, boolean bIncludeTime) {
		if(bIncludeTime)
			return changeDateFormat(sDate, DATEFORMAT_FULL_TIME);
		return changeDateFormat(sDate, DATEFORMAT_FULL);
	}
	
	/**
	 * Returns the current time of the day as a string.
	 * @param bAM_PM <code>&nbsp;TRUE:</code> return 12-hour format (02:00PM).
	 * <code>&nbsp;FALSE:</code> return 24-hour format (14:00)
	 * @return Current time as a sting
	 */
	public static String getCurrrentTimeOfDay(boolean bAM_PM) {
		Calendar mycal = Calendar.getInstance();
		int iHour = mycal.get(Calendar.HOUR_OF_DAY);
		int iMinute = mycal.get(Calendar.MINUTE);
		String tm24 = String.format("%02d",iHour) + ":" + String.format("%02d",iMinute);
		return bAM_PM ? time12H(tm24) : tm24;
	}

	/**
	 * Returns the expected default start time for a new calendar
	 * event based on the current time of day at runtime.<br>
	 * Default event start times are the next quarter hour from the current time.
	 * @param bAM_PM <code>&nbsp;TRUE:</code> return 12-hour format (02:00PM).
	 * <code>&nbsp;FALSE:</code> return 24-hour format (14:00)
	 * @return Expected start time as a string
	 */
	public static String getExpectedStartTime(boolean bAM_PM) {
		Calendar mycal = Calendar.getInstance();
		int iHour = mycal.get(Calendar.HOUR_OF_DAY);
		int iMinute = mycal.get(Calendar.MINUTE);
		
		if(iMinute>45){
			iMinute=0;
			iHour = iHour+1 == 24 ? 0 : iHour + 1;
		}else if(iMinute>30){
			iMinute=45;
		}else if(iMinute>15){
			iMinute=30;
		}else{
			iMinute=15;
		}
		String tm24 = Integer.toString(iHour) + ":" + Integer.toString(iMinute);
		return bAM_PM ? time12H(tm24) : tm24;
	}
	/**
	 * Method changes the format of time to 12 hour format* 
	 * @param sTime
	 * @return
	 */

	public static String convertTo12HourFormat(String sTime){
		if(isStringADate(sTime, "hh:mmaaa")){
			return changeDateFormat(sTime, "hh:mmaaa", "hh:mmaaa"); // already in 12 hr format, but make sure it has a leading zero
		}
		return changeDateFormat(sTime, "HH:mm", "hh:mmaaa");
	}
	
	public static String time24H(String sTime) {
		if(!sTime.trim().toLowerCase().endsWith("m")){
			return changeDateFormat(sTime, "HH:mm", "HH:mm"); // already in 24 hr format, but make sure it has a leading zero
		}
		return changeDateFormat(sTime, "hh:mmaaa", "HH:mm");
	}
	
	public static String time12H(String sTime) {
		return convertTo12HourFormat(sTime);
	}
	
	public static String getTimeFormat(String sTime) {
		SimpleDateFormat df = new SimpleDateFormat("hh:mmaaa");
		try {
			df.parse(sTime);
			return "hh:mmaaa";
		} catch (ParseException e) {
			//
		}
		return "HH:mm";
	}

	/**
	 *  Searches the string for a <code>&nbsp;time&nbsp;</code> pattern(s)
	 *  and returns the first matching substring. <br>Both 12HR and 24HR formats are supported.
	 *  If <code>AM</code> or <code>PM</code> is not specified, 24HR format is assumed.
	 * <br><br>
	 * Example:<br>
	 * Passing the string: <br><code>&nbsp;&nbsp;&nbsp;&nbsp;Meeting 10:00AM - 11:00AM Room 1235</code><br>
	 * will return:<br><code>&nbsp;&nbsp;&nbsp;&nbsp;10:00AM - 11:00AM&nbsp;</code> if <code>bIncludeEndTime</code> is <b>true</b><br>
	 * or:<br><code>&nbsp;&nbsp;&nbsp;&nbsp;10:00AM&nbsp;</code> if <code>bIncludeEndTime</code> is <b>false</b><br>
	 * @param sStringWithTime
	 * @param bIncudeEndTime TODO
	 * @return
	 */
	public static String getTimeInString(String sStringWithTime, boolean bIncludeEndTime){
		String time = bIncludeEndTime ? sStringWithTime.replaceAll(regexStartEnd, "$1") : sStringWithTime.replaceAll(regexTime, "$1");
		time = time.trim().replaceAll("\\s*-\\s*", " - ");
		return time;
	}
	
	public static String elapsedTime(long startTime){
		long elapsed = System.currentTimeMillis()-startTime;
		elapsed += 18000000; // delta from GMT
		return dateToString(new Date(elapsed), "HH'h'mm'm'ss's'SSS'ms'");
	}

	/**
	 * Calculates elapsed time in seconds using System.nanoTime() values.
	 * @param begin The beginning time as set by System.nanoTime()
	 * @return The elapsed time in seconds from <code>begin</code>.<br>
	 * Example: <code>23.5554678</code>
	 */
	public static double elapsedSeconds(double begin){
		return (System.nanoTime() - begin)/1e9;
	}
	
	/**
	 * Calculates elapsed time in seconds as a formatted String, based on System.nanoTime() values.<br>
	 * The return value is in the form:  <code>"64.43 seconds"</code>
	 * 
	 * @param begin The beginning time as set by System.nanoTime()
	 * @return The elapsed time in seconds from <code>begin</code> as a formatted String.<br>
	 * Seconds are calculated to two decimal places and the units string  <code>" seconds" </code> is appended to the returned value.<br>
	 * Example: <code>23.55 seconds</code>
	 */
	public static String elapsedSecondsString(double begin){
		return String.format("%.2f",(System.nanoTime() - begin)/1e9) + " seconds";
	}
	
	/**
	 * Calculates elapsed time in hours, minutes and seconds as a formatted String based on System.nanoTime() values.<br>
	 * The return value is in the form:&nbsp;&nbsp;<code>5h 13m 2s</code></br></br>
	 * If the elapsed time is less than one hour, the return string only includes minutes and seconds: &nbsp;&nbsp;<b>13m 2s</b></br>
	 * If the elapsed time is less than one minute, the return string only includes seconds:&nbsp;&nbsp; <b>2s</b>
	 * @param begin - The beginning time as set by <code>System.nanoTime()</code>
	 * @return Elapsed time string in the form:&nbsp;&nbsp;<code>5h 13m 2s</code>
	 */
	public static String elapsedTimeString(double begin) {
		return secondsToHrMinSec(elapsedSeconds(begin));
	}
	
	/**
	 * Converts <code>Seconds</code> to a string in the format:&nbsp;&nbsp;<code>5h 13m 2s</code></br></br>
	 * If the elapsed time is less than one hour, the return string only includes minutes and seconds: &nbsp;&nbsp;<b>13m 2s</b></br>
	 * If the elapsed time is less than one minute, the return string only includes seconds:&nbsp;&nbsp; <b>2s</b>
	 * @param Seconds - seconds as a double
	 * @return
	 */
	public static String secondsToHrMinSec(double Seconds){
		double dsec = Seconds;
		int hr = 0;
		int min = 0;
		int sec = 0;

		int ih = 3600;
		int im = 60;
		String sTime = "";
		String hformat = "%dh %dm %ds";
		String mformat = " %dm %ds";
		String sformat = "%ds";

		hr = (int) dsec / ih;
		dsec = dsec - (hr * ih);
		min = (int) dsec / im;
		sec = (int) dsec - (min * im);

		if (hr != 0)
			sTime = String.format(hformat, hr, min, sec);
		else if (min != 0)
			sTime = String.format(mformat, min, sec);
		else
			sTime = String.format(sformat, sec);

		return sTime;
	}
	
	public static String uniqueDateStamp(){
		return dateToString(Calendar.getInstance().getTime(),"MMdd:HH:mm:ss");
	}
	
	public static String uniqueTimeStamp(){
		return dateToString(Calendar.getInstance().getTime(),"HH:mm:ss.SSS");
	}
}
