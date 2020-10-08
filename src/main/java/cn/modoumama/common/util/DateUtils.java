package cn.modoumama.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 项目名称：Remoting_Common  
 *  
 * 类名称：cn.modoumama.utils.DateUtil
 * 类描述：    
 * 创建人：邓强
 * 创建时间：2016-10-27 上午10:01:05   
 * 修改人：  
 * 修改时间：  
 * 修改备注：     
 * @version   V1.0      
 */
public class DateUtils {
	static Logger logger = LoggerFactory.getLogger(DateUtils.class);

	public static String getCNDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		StringBuffer datebuf = new StringBuffer();
		datebuf.append(calendar.get(Calendar.YEAR));
		datebuf.append("年");
		datebuf.append(calendar.get(Calendar.MONTH)+1);
		datebuf.append("月");
		datebuf.append(calendar.get(Calendar.DAY_OF_MONTH));
		datebuf.append("日");
		datebuf.append(" ");
		FastDateFormat dfmt = FastDateFormat.getInstance("HH:mm:ss");
		datebuf.append(dfmt.format(calendar));
		return datebuf.toString();
	}
	
	public static String getDateTimeString() {
		FastDateFormat dfmt = FastDateFormat.getInstance("yyyyMMddHHmmss");
		return dfmt.format(new java.util.Date());
	}

	public static String getDateString() {
		FastDateFormat dfmt = FastDateFormat.getInstance("yyyyMMdd");
		return dfmt.format(new java.util.Date());
	}
	
	public static String getHHmmString(Date date) {
		FastDateFormat dfmt = FastDateFormat.getInstance("HH:mm");
		return dfmt.format(date);
	}
	
	public static String getyyyyMMddString(Date date) {
		FastDateFormat dfmt = FastDateFormat.getInstance("yyyyMMdd");
		return dfmt.format(date);
	}

	public static String getYYYYMMDDHH24MISS(String strYYYY_MM_DD) {
		String tmp = "";
		try {
			java.util.Date d = getDate(strYYYY_MM_DD, "yyyy-mm-dd");
			FastDateFormat dfmt = FastDateFormat.getInstance("yyyyMMddHHmmss");
			return dfmt.format(d);
		} catch (ParseException e) {
			logger.warn("", e);
		}

		return tmp;
	}

	/**
	 * 取某年某月的最后一天
	 * 
	 * @param month
	 * @return
	 */
	public static String getYearMonth(Date date) {

		try {
			Calendar calendar = getCalendarFromDate(date);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			String _month = String.valueOf(month);
			if (_month.length() < 2) {
				_month = "0" + _month;
			}
			String value = String.valueOf(year).substring(2);
			String result = value + _month;
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 根据Date 得到对应的Calendar
	 * 
	 * @param date
	 * @return @
	 */
	public static Calendar getCalendarFromDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}
	
	
	public static Date getBeforeNDaysDate(int day){
		return getAfterNDaysDate(new Date(),-1*day);
	}

	public static String getDateString(String pattern) {
		FastDateFormat dfmt = FastDateFormat.getInstance(pattern);
		return dfmt.format(new java.util.Date());
	}

	public static String getDateString(int days, String pattern) {
		FastDateFormat dfmt = FastDateFormat.getInstance(pattern);
		long days2 = days;
		return dfmt.format(new java.util.Date(new java.util.Date().getTime()
				+ 86400000L * days2));
	}

	public static String getDateString(java.util.Date date) {
		if (date == null) {
			return "";
		}
		FastDateFormat dfmt = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
		return dfmt.format(date);
	}

	public static String getShortDateString(java.util.Date date) {
		if (date == null) {
			return "";
		}
		FastDateFormat dfmt = FastDateFormat.getInstance("yyyy-MM-dd");
		return dfmt.format(date);
	}

	public static String getDateString(java.util.Date date, String pattern) {
		FastDateFormat dfmt = FastDateFormat.getInstance(pattern);
		return date != null ? dfmt.format(date) : "";
	}

	public static String getDateString(String date, String pattern) {
		if ((date == null) || ("".equals(date)) || ("null".equals(date)))
			return "";
		try {
			@SuppressWarnings("deprecation")
			java.util.Date d = new java.util.Date(date);
			return getDateString(d, pattern);
		} catch (Exception e) {
			logger.warn("", e);
		}
		return "";
	}

	public static String getDateString(java.sql.Date date, String pattern) {
		FastDateFormat dfmt = FastDateFormat.getInstance(pattern);
		return date != null ? dfmt.format(date) : "";
	}

	public static java.sql.Date getSQLDate(String date) {
		try {
			if (VerifyObject.verifyString(date)) {
				java.util.Date d = org.apache.commons.lang3.time.DateUtils
						.parseDate(date, "yyyy-MM-dd");
				return new java.sql.Date(d.getTime());
			}
			return null;
		} catch (ParseException e) {
			logger.warn("", e);
		}
		return null;
	}

	public static java.util.Date getDate(String strDate) throws ParseException {
		return org.apache.commons.lang3.time.DateUtils.parseDate(strDate,
				"yyyyMMddHHmmss");
	}

	public static java.util.Date getDate_(String strDate) throws ParseException {
		return org.apache.commons.lang3.time.DateUtils.parseDate(strDate,
				"yyyy-MM-dd HH:mm:ss");
	}

	public static java.util.Date getDate(String strDate, String pattern)
			throws ParseException {
		return org.apache.commons.lang3.time.DateUtils.parseDate(strDate,
				pattern);
	}

	public static String getStringDate(String stringdate) {
		if (stringdate == null) {
			return null;
		}
		FastDateFormat formatter2 = FastDateFormat
				.getInstance("yyyy-MM-dd HH:mm:ss");
		String dateString = "";
		try {
			java.util.Date date = org.apache.commons.lang3.time.DateUtils
					.parseDate(stringdate, "yyyyMMddHHmmss");
			dateString = formatter2.format(date);
		} catch (ParseException e) {
			logger.warn("", e);
		}
		return dateString;
	}

	public static String getStringDate(String stringdate, String fpattern,
			String tpattern) {
		if (stringdate == null) {
			return null;
		}
		FastDateFormat formatter2 = FastDateFormat.getInstance(tpattern);
		String dateString = "";
		try {
			java.util.Date date = org.apache.commons.lang3.time.DateUtils
					.parseDate(stringdate.trim(), fpattern);
			dateString = formatter2.format(date);
		} catch (ParseException e) {
			logger.warn("", e);
		}
		return dateString;
	}

	public static String getChdate(int month_num) {
		Calendar c1 = Calendar.getInstance();
		String result = "";
		c1.add(2, month_num);
		result = String.valueOf(c1.get(1));
		if (c1.get(2) + 1 >= 10)
			result = result + String.valueOf(c1.get(2) + 1);
		else {
			result = result + "0" + String.valueOf(c1.get(2) + 1);
		}
		return result;
	}

	/**
	 * 得到当前系统的年份
	 */
	public static int getSysYear() {
		Calendar calendar = new GregorianCalendar();
		int iyear = calendar.get(1);
		return iyear;
	}

	/**
	 * 得到当前系统的月份
	 */
	public static int getSysMonth() {
		Calendar calendar = new GregorianCalendar();
		int imonth = calendar.get(2) + 1;
		return imonth;
	}

	public static String getDateOfSp(String sp) {
		String reday = "";
		int y = getSysYear();
		int m = getSysMonth();
		int d = getSysDay();
		reday = y + sp;
		if (m < 10)
			reday = reday + "0" + m + sp;
		else {
			reday = reday + m + sp;
		}
		if (d < 10)
			reday = reday + "0" + d;
		else {
			reday = reday + d;
		}
		return reday;
	}

	public static String getDateOfFirstDay(String sp) {
		String reday = "";
		int y = getSysYear();
		int m = getSysMonth();
		reday = y + sp;
		if (m < 10)
			reday = reday + "0" + m + sp + "01";
		else {
			reday = reday + m + sp + "01";
		}
		return reday;
	}

	/**
	 * 得到当前系统的天
	 */
	public static int getSysDay() {
		Calendar calendar = new GregorianCalendar();
		int idate = calendar.get(5);
		return idate;
	}

	public static String getDateString2() {
		String tmp = "";
		tmp = getSysYear() + "    " + getSysMonth() + "    " + getSysDay()
				+ "    ";
		return tmp;
	}

	public static int getTwoMonthNum(String startDate, String endDate) {
		int year1 = Integer.parseInt(startDate.substring(0, 4));
		int year2 = Integer.parseInt(endDate.substring(0, 4));
		int month1 = Integer.parseInt(startDate.substring(4));
		int month2 = Integer.parseInt(endDate.substring(4));
		return Math.abs(year1 - year2) * 12 - (month1 - month2) + 1;
	}

	public static String getNextMonth(String startDate, int i) {
		int start = Integer.parseInt(startDate);
		int next = start + i;
		int year = Integer.parseInt(String.valueOf(next).substring(0, 4));
		int month = Integer.parseInt(String.valueOf(next).substring(4));
		if (month > 12) {
			year++;
			month -= 12;
		}
		if (month < 10) {
			return year + "0" + month;
		}
		return year + "" + month;
	}

	public static int getDays(String yearMonth) {
		int[] days = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int year = Integer.parseInt(yearMonth.substring(0, 4));
		int month = Integer.parseInt(yearMonth.substring(4)) - 1;
		if (month == 1) {
			if (year % 4 == 0) {
				if (year % 100 == 0) {
					return 28;
				}
				return 29;
			}

			return 28;
		}

		return days[month];
	}

	public static int isBetweenDays(String startDay, String endDay) {
		FastDateFormat formatter = FastDateFormat.getInstance("yyyyMMdd");
		java.util.Date date = new java.util.Date();
		String today = formatter.format(date);
		startDay = today.substring(0, 6) + startDay;
		endDay = today.substring(0, 6) + endDay;
		if ((today.compareTo(startDay) >= 0) && (today.compareTo(endDay) <= 0)) {
			return 0;
		}
		return 1;
	}

	public static boolean isDate(String dateStr, String dateFomrat) {
		boolean tmp = false;
		try {
			java.util.Date d = getDate(dateStr, dateFomrat);
			FastDateFormat formatter = FastDateFormat.getInstance(dateFomrat);
			formatter.format(d);
			tmp = true;
		} catch (ParseException e) {
			tmp = false;
		}
		return tmp;
	}

	public static boolean isBetweenDays(String startDay, String endDay,
			String dateFomrat) {
		boolean tmp = false;

		if ((isDate(startDay, dateFomrat)) && (isDate(endDay, dateFomrat))) {
			try {
				if (getDate(startDay, dateFomrat).getTime() > getDate(endDay,
						dateFomrat).getTime())
					tmp = false;
				else
					tmp = true;
			} catch (ParseException localParseException) {
			}
		}
		return tmp;
	}

	public static String getYyyyMm(String theDayYyyy_mm_dd) {
		String dayYYYYMMDD = "";
		dayYYYYMMDD = StringUtils.replace(theDayYyyy_mm_dd,"-", "");
		return dayYYYYMMDD.substring(0, 6);
	}

	public static boolean isDateStr(String strDate, String pattern) {
		boolean tmp = true;
		try {
			getDate(strDate, pattern);
		} catch (ParseException e) {
			tmp = false;
		}

		return tmp;
	}

	public static java.util.Date getAfterNDaysDate(java.util.Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(6, days);

		return cal.getTime();
	}

	public static long DaysBetweenTwoDate(String firstString,
			String secondString) throws ParseException {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		java.util.Date firstDate = org.apache.commons.lang3.time.DateUtils
				.parseDate(firstString, pattern);
		java.util.Date secondDate = org.apache.commons.lang3.time.DateUtils
				.parseDate(secondString, pattern);

		return (secondDate.getTime() - firstDate.getTime()) / 60000L;
	}

	public static long daysBetweenTwoDate(java.util.Date date1,
			java.util.Date date2) {
		long nDay = (date1.getTime() - date2.getTime()) / 86400000L > 0L ? (date1
				.getTime() - date2.getTime()) / 86400000L
				: (date2.getTime() - date1.getTime()) / 86400000L;

		return nDay;
	}

	public static java.util.Date addYMD(java.util.Date date, int yearNum,
			int monthNum, int dayNum) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(1, yearNum);
		c.add(2, monthNum);
		c.add(5, dayNum);
		return c.getTime();
	}

	public static String getDayIncrease(String endtime, int n) {
		FastDateFormat formatter = FastDateFormat.getInstance("yyyy-MM-dd");
		try {
			java.util.Date todate = org.apache.commons.lang3.time.DateUtils
					.parseDate(endtime, "yyyy-MM-dd");
			Calendar gc = Calendar.getInstance();
			gc.setTime(todate);
			gc.add(5, n);
			endtime = formatter.format(gc.getTime());
		} catch (ParseException e) {
			logger.warn("getDayIncrease exception: ", e);
		}
		return endtime;
	}

	public static String getCurrentTime() {
		java.util.Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);

	}

	/**
	 * 取某年某月的第一天
	 * 
	 * @param month
	 * @return
	 */
	public static Date getFirstDayOfMonth(String year, String month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	/**
	 * 取某年某月的最后一天
	 * 
	 * @param month
	 * @return
	 */
	public static Date getLastDayOfMonth(String year, String month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	
	/**
	 * 获取当天的开始时间
	 * 
	 * @param month
	 * @return
	 */
	public static Date getDayStartTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取两个日期是否同一天
	 * 
	 * @param month
	 * @return
	 */
	public static boolean isOneDay(Date date1,Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar2.setTime(date2);
		if (calendar1.get(Calendar.YEAR)!=calendar2.get(Calendar.YEAR)) {
			return false;
		}else {
			return calendar1.get(Calendar.DAY_OF_YEAR)==calendar2.get(Calendar.DAY_OF_YEAR)?true:false;
		}
	}
	
	/**
	 * 比较两个时间,newDate大于oldDate 返回true
	 * @param newDate
	 * @param oldDate
	 * @return
	 */
	public static boolean isOut(Date newDate,Date oldDate) {
		return newDate.getTime() > oldDate.getTime();
	}
	
	/**
	 * 获取week_day
	 * 
	 * @param month
	 * @return
	 */
	public static String getWeekDay(Date date) {
		Calendar calendar1=Calendar.getInstance();
		calendar1.set(Calendar.HOUR_OF_DAY, 23);
		calendar1.set(Calendar.MINUTE, 59);
		calendar1.set(Calendar.SECOND, 59);
		calendar1.set(Calendar.MILLISECOND, 999);
		long td = calendar1.getTimeInMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		if (date.getTime()>td) {
			return "明天";
		}else if (date.getTime()<td&&date.getTime()>td-24*3600*1000) {
			return "今天";
		}
		switch (weekDay) {
		case 1:
			return "周日";
		case 2:
			return "周一";
		case 3:
			return "周二";
		case 4:
			return "周三";
		case 5:
			return "周四";
		case 6:
			return "周五";
		case 7:
			return "周六";
		default:
			return "";
		}
	}
	
	/**
	 * 获取一天的开始时间
	 * 
	 * @param month
	 * @return
	 */
	public static Date getDayStartTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * @Title: getRankingTime  
	 * @Description: 获取当前的期数 
	 * @return
	 */
	public static Integer getRankingTime(){
		Calendar calendar = Calendar.getInstance();
		return Integer.parseInt(DateUtils.dateFormat(calendar,"yyyyMM"));
	}

	/**
	 * util.Date转sql.Date
	 * @param date java.util.Date
	 * @return
	 */
	public static java.sql.Date toSqlDate(Date date){
		return new java.sql.Date(date.getTime());
	}

	/**
	 * sql.Date转util.Date
	 * @param date java.sql.Date
	 * @return
	 */
	public static Date toUtilDate(java.sql.Date date){
		return new Date(date.getTime());
	}

	/**
	 * 格式化日期
	 * @param calendar  java.util.Calendar类型
	 * @param pattern
	 * @return
	 */
	public static String dateFormat(Calendar calendar){
		String pattern = "yyyy-MM-dd HH:mm:ss";
		return dateFormat(calendar.getTime(),pattern);
	}

	/**
	 * 格式化日期
	 * @param date   java.sql.Date
	 * @param pattern
	 * @return
	 */
	public static String dateFormat(java.sql.Date date) {  
		String pattern = "yyyy-MM-dd";
        return dateFormat(date, pattern);  
    }

	/**
	 * 格式化日期
	 * @param date   java.util.Date
	 * @param pattern
	 * @return
	 */
	public static String dateFormat(Date date) {  
		String pattern = "yyyy-MM-dd HH:mm:ss";
        return dateFormat(date, pattern);   
    } 

	/**
	 * 格式化日期
	 * @param calendar  java.util.Calendar类型
	 * @param pattern
	 * @return
	 */
	public static String dateFormat(Calendar calendar, String pattern){
		return dateFormat(calendar.getTime(),pattern);
	}

	/**
	 * 格式化日期
	 * @param date   java.sql.Date
	 * @param pattern
	 * @return
	 */
	public static String dateFormat(java.sql.Date date, String pattern) {  
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);  
        String demo = sdf.format(date);  
        return demo;  
    }

	/**
	 * 格式化日期
	 * @param date   java.util.Date
	 * @param pattern
	 * @return
	 */
	public static String dateFormat(Date date, String pattern) {  
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);  
        String demo = sdf.format(date);  
        return demo;  
    }
	
	/**
	 * @Description: 返回两个时间的时间差，按天返回
	 * @param currDate
	 * @param oldDate
	 * @return
	 */
	public static Integer  timeDifference(Date currDate,Date oldDate){
		return millisecondDifference(currDate, oldDate, TimeMillisecond.DAY);
	}
	
	/**
	 * @Description: 返回两个时间的时间差，毫秒
	 * @param currDate
	 * @param oldDate
	 * @param millisecond   按时间差值 ，1秒1000毫米
	 * @return
	 */
	public static Integer  millisecondDifference(Date currDate,Date oldDate,Integer millisecond){
		Long curr =  currDate.getTime();
		Long old = oldDate.getTime();
		Long tmp = (curr-old)/millisecond;
		return 	Math.abs(tmp.intValue());
	}
	
	public static Integer getCurrMinuteByDay(){
		Calendar calendar = Calendar.getInstance();
		Long curr =  calendar.getTimeInMillis();
		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Long old =calendar.getTimeInMillis();
		Long tmp = (curr-old)/60000;
		
		return tmp.intValue();
	}
	
	
	
	/**
	 * @Title: addDay  
	 * @Description:一个时间增加多少天
	 * @param date	需要增加的时间
	 * @param day	增加的天数
	 * @return 返回增加之后的时间
	 */
	public static Date addDay(Date date, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		
		return calendar.getTime();
	}
	/**
	 * 字符串转时间
	 * 支持格式:
	 * yyyyMMdd<br>
	 * yyyyMMddHH<br>
	 * yyyyMMddHHmm<br>
	 * yyyyMMddHHmmss<br>
	 * yyyy-MM-dd<br>
	 * yyyy.MM.dd<br>
	 * yyyy/MM/dd<br>
	 * yyyy MM dd<br>
	 * 年月日格式可以和下面的日以交换
	 * yyyyMMdd HH<br>
	 * yyyyMMdd HHmm<br>
	 * yyyyMMdd HH mm<br>
	 * yyyyMMdd HH:mm<br>
	 * yyyyMMdd HH：mm<br>
	 * yyyyMMdd HH mm ss<br>
	 * yyyyMMdd HH:mm:ss<br>
	 * yyyyMMdd HH：mm：ss<br>
	 * MM/dd<br>
	 * MM-dd<br>
	 * MM.dd<br>
	 * HH<br>
	 * HHmm<br>
	 * HH mm<br>
	 * HH:mm<br>
	 * HH：mm<br>
	 * HH mm ss<br>
	 * HH:mm:ss<br>
	 * HH：mm：ss<br>
	 * @param dateStr
	 * @return
	 */
	public static Date getDateFromString(String dateStr){
		dateStr = dateStr.trim();
		Pattern pat = Pattern.compile("^(([0-9]{4})([0-9]{2})([0-9]{2})\\s?([0-9]{2})([0-9]{2})([0-9]{2})|((([0-9]{4})([^0-9:]*)([0-9]{2})([^0-9:]*)([0-9]{2}))|((([0-9]{4})([^0-9:\\s]))?([0-9]{1,2})([^0-9:\\s])([0-9]{1,2})))?((T|\\s*)?([0-9]{1,2})((([:：\\s])([0-9]{1,2})(([:：\\s])?([0-9]{1,2}))?)|([0-9]{2}))?)?)$");
        Matcher mat = pat.matcher(dateStr);
        if(!mat.find()){
        	return null;
        }

        if(StringUtils.isBlank(mat.group(1))){
        	return null;
        }else{
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
	        int y,M,d,H,m,s;
	        if(mat.group(2) != null || mat.group(10) != null || mat.group(16) != null || mat.group(22) != null){
	       	 y = Integer.parseInt(mat.group(2) != null?mat.group(2):mat.group(10)!=null?mat.group(10):mat.group(16)!=null?mat.group(16):mat.group(22));
	       	 calendar.set(Calendar.YEAR, y);
	        }
	        if(mat.group(3) != null || mat.group(12) != null || mat.group(19) != null){
	       	 M = Integer.parseInt(mat.group(3) != null?mat.group(3):mat.group(12)!=null?mat.group(12):mat.group(19));
	       	 M = M>0?M-1:0;
	       	 calendar.set(Calendar.MONTH, M);
	        }
	        if(mat.group(4) != null || mat.group(14) != null || mat.group(21) != null){
	       	 d = Integer.parseInt(mat.group(4) != null?mat.group(4):mat.group(14)!=null?mat.group(14):mat.group(21));
	       	 calendar.set(Calendar.DAY_OF_MONTH, d);
	        }
	        if(mat.group(5) != null || mat.group(24) != null){
	       	 H = Integer.parseInt(mat.group(5) != null?mat.group(5):mat.group(24));
	       	 calendar.set(Calendar.HOUR_OF_DAY, H);
	        }
	        if(mat.group(6) != null || mat.group(28) != null || mat.group(32) != null){
	       	 m = Integer.parseInt(mat.group(6) != null?mat.group(6):mat.group(28)!=null?mat.group(28):mat.group(32));
	       	 calendar.set(Calendar.MINUTE, m);
	        }
	        if(mat.group(7) != null || mat.group(31) != null){
	       	 s = Integer.parseInt(mat.group(7) != null?mat.group(7):mat.group(31));
	       	 calendar.set(Calendar.SECOND, s);
	        }
			return calendar.getTime();
        }
	}
	
	public static class TimeMillisecond{
		public static final Integer SECOND = 1000;
		public static final Integer MINUTE = 60*SECOND;
		public static final Integer HOUR = 60*MINUTE;
		public static final Integer DAY = 24*HOUR;
		public static final Integer THREE_DAYS = 3*DAY;
		public static final Integer WEEK = 7*DAY;
	}
}
