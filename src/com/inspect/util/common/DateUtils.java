
package com.inspect.util.common;

import static org.apache.commons.lang.time.DateUtils.truncate;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**                      
 *  日期处理工具函数包,包括日期对象、日期字符串相关转换函数      
 */
public class DateUtils {
	static Log logger = LogFactory.getLog(DateUtils.class);
	/**
	 * 定义常见的时间格式
	 */
	private DateUtils(){}
	private static String[] dateFormat = { "yyyy-MM-dd HH:mm:ss",
			"yyyy/MM/dd HH:mm:ss", "yyyy年MM月dd日HH时mm分ss秒", "yyyy-MM-dd",
			"yyyy/MM/dd", "yy-MM-dd", "yy/MM/dd", "yyyy年MM月dd日", "HH:mm:ss",
			"yyyyMMddHHmmss", "yyyyMMdd", "yyyy.MM.dd", "yy.MM.dd","yyyy-MM-dd HH:mm","HH:mm","yyyy-MM-dd'T'HH:mm:ss" };

	/**
	 * 将日期格式从 java.util.Calendar 转到 java.sql.Timestamp 格式
	 * @param date java.util.Calendar 格式表示的日期
	 * @return     java.sql.Timestamp 格式表示的日期
	 */
	public static Timestamp convUtilCalendarToSqlTimestamp(Calendar date) {
		if (date == null)
			return null;
		else
			return new Timestamp(date.getTimeInMillis());
	}

	/**
	 * 将日期格式从 java.util.Timestamp 转到 java.util.Calendar 格式
	 * @param date java.sql.Timestamp 格式表示的日期
	 * @return     java.util.Calendar 格式表示的日期
	 */
	public static Calendar convSqlTimestampToUtilCalendar(Timestamp date) {
		if (date == null)
			return null;
		else {
			java.util.GregorianCalendar gc = new java.util.GregorianCalendar();
			gc.setTimeInMillis(date.getTime());
			return gc;
		}
	}

	/**
	 * 解析一个字符串，形成一个Calendar对象，适应各种不同的日期表示法
	 * @param dateStr 期望解析的字符串，注意，不能传null进去，否则出错
	 * @return 返回解析后的Calendar对象
	 * <br>
	 * <br>可输入的日期字串格式如下：
	 * <br>"yyyy-MM-dd HH:mm:ss",
	 * <br>"yyyy/MM/dd HH:mm:ss",
	 * <br>"yyyy年MM月dd日HH时mm分ss秒",
	 * <br>"yyyy-MM-dd",
	 * <br>"yyyy/MM/dd",
	 * <br>"yy-MM-dd",
	 * <br>"yy/MM/dd",
	 * <br>"yyyy年MM月dd日",
	 * <br>"HH:mm:ss",
	 * <br>"yyyyMMddHHmmss",
	 * <br>"yyyyMMdd",
	 * <br>"yyyy.MM.dd",
	 * <br>"yy.MM.dd"
	 */
	public static Calendar parseCalender(String dateStr) {
		if (dateStr == null || dateStr.trim().length() == 0)
			return null;
		Date result = parseDate(dateStr, 0);
		//	System.out.println("result="+result);
		Calendar cal = Calendar.getInstance();
		cal.setTime(result);
		return cal;
	}
	
	/**
	 * 解析一个字符串，形成一个Date对象
	 * 可输入的日期字串格式如下：
	 * yyyy-MM-dd HH:mm
	 * @param dateStr
	 * @return
	 */
	public static Date parseDate(String dateStr){
		if (dateStr == null || dateStr.trim().length() == 0)
			return null;
		return parseDate(dateStr, 13);
	}

	/**
	 * 将一个日期转成日期时间格式，格式这样  2002-08-05 21:25:21
	 * @param date  期望格式化的日期对象
	 * @return 返回格式化后的字符串
	 * <br>
	 * <br>例：
	 * <br>调用：

	 * <br>Calendar date = new GregorianCalendar();
	 * <br>String ret = DateUtils.toDateTimeStr(date);
	 * <br>返回：

	 * <br> ret = "2002-12-04 09:13:16";
	 */
	public static String toDateTimeStr(Calendar date) {
		if (date == null)
			return null;
		return new SimpleDateFormat(dateFormat[0]).format(date.getTime());
	}
	
	/**
	 * 将一个日期转成日期时间格式，格式这样  2002-08-05 21:25:21
	 * @param date  期望格式化的日期对象
	 * @return 返回格式化后的字符串
	 * <br>
	 * <br>例：
	 * <br>调用：

	 * <br>Calendar date = new GregorianCalendar();
	 * <br>String ret = DateUtils.toDateTimeStr(date);
	 * <br>返回：

	 * <br> ret = "2002-12-04 09:13:16";
	 */
	public static String toDateTimeStr(int format,Calendar date) {
		if (date == null)
			return null;
		return new SimpleDateFormat(dateFormat[format]).format(date.getTime());
	}
	/**
	 * 将一个日期转成日期格式，格式这样  2002-08-05
	 * @param date  期望格式化的日期对象
	 * @return 返回格式化后的字符串
	 * <br>
	 * <br>例：
	 * <br>调用：

	 * <br>Calendar date = new GregorianCalendar();
	 * <br>String ret = DateUtils.toDateStr(calendar);
	 * <br>返回：

	 * <br>ret = "2002-12-04";
	 */
	public static String toDateStr(Calendar date) {
		if (date == null)
			return null;
		return new SimpleDateFormat(dateFormat[3]).format(date.getTime());
	}
	
	public static String toDateStrByFormatIndex(Calendar date,int formatIndex) {
		if (date == null)
			return null;
		return new SimpleDateFormat(dateFormat[formatIndex]).format(date.getTime());
	}

	public static int calendarMinus(Calendar d1, Calendar d2) {
		if (d1 == null || d2 == null) {
			return 0;
		}

		d1.set(Calendar.HOUR_OF_DAY, 0);
		d1.set(Calendar.MINUTE, 0);
		d1.set(Calendar.SECOND, 0);

		d2.set(Calendar.HOUR_OF_DAY, 0);
		d2.set(Calendar.MINUTE, 0);
		d2.set(Calendar.SECOND, 0);

		long t1 = d1.getTimeInMillis();
		long t2 = d2.getTimeInMillis();
		logger.debug("DateUtils: d1 = " + DateUtils.toDateTimeStr(d1)
				+ "(" + t1 + ")");
		logger.debug("DateUtils: d2 = " + DateUtils.toDateTimeStr(d2)
				+ "(" + t2 + ")");
		long daylong = 3600 * 24 * 1000;
		t1 = t1 - t1 % (daylong);
		t2 = t2 - t2 % (daylong);

		long t = t1 - t2;
		int value = (int) (t / (daylong));

		logger.debug("DateUtils: d2 -d1 = " + value + " （天）");

		return value;
	}
	
	/**
	 * 查询当天的前n天的具体时间
	 * @param n
	 * @return
	 */
	public static Calendar getCurrentCalBefore(int n) {
		  Calendar day=Calendar.getInstance();  
          day.add(Calendar.DATE,-n);  
          return day;
	}
	
	/**
	 * 查询当天的前n年的具体时间
	 * @param n
	 * @return
	 */
	public static Calendar getCurrentCalBeforeYear(int n) {
		  Calendar day=Calendar.getInstance();  
          day.add(Calendar.YEAR,-n);  
          return day;
	}
	
	
	public static int dateMinus(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return 0;
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);
		return calendarMinus(cal1,cal2);
		}

	/**
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long calendarminus(Calendar d1, Calendar d2) {
		if (d1 == null || d2 == null) {
			return 0;
		}
		return (d1.getTimeInMillis() - d2.getTimeInMillis()) / (3600 * 24000);
	}

	/**
	 * 内部方法，根据某个索引中的日期格式解析日期

	 * @param dateStr 期望解析的字符串
	 * @param index 日期格式的索引

	 * @return 返回解析结果
	 */
	public static Date parseDate(String dateStr, int index) {
		DateFormat df = null;
		try {
			df = new SimpleDateFormat(dateFormat[index]);

			return df.parse(dateStr);
		} catch (ParseException pe) {
			return parseDate(dateStr, index + 1);
		} catch (ArrayIndexOutOfBoundsException aioe) {
			return null;
		}
	}
	
	/**
	 * 字符转日期,字符串格式："yyyy-MM-dd"，例如2006-01-01
	 * @param dateStr
	 * @return
	 */
	public static Date stringToDate(String dateStr){
		if (dateStr == null || dateStr.trim().length() == 0){
			return null;
		}
		return parseDate(dateStr, 3);
	}	
	
	/**
	 * DATE to String，支持多种格式
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date,int index ){
		if (date == null){
			return null;
		}
		return new SimpleDateFormat(dateFormat[index]).format(date);
	}
	
	/**
	 * DATE to String，转换结果格式为："yyyy-MM-dd"，例如2006-01-01
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date ){
		if (date == null){
			return null;
		}
		return new SimpleDateFormat(dateFormat[3]).format(date);
	}

	/**
	 * 将日期格式从 java.util.Date 转到 java.sql.Timestamp 格式
	 * convUtilDateToSqlTimestamp <br>
	 * @param date java.util.Date 格式表示的日期
	 * @return Timestamp java.sql.Timestamp 格式表示的日期
	 */
	public static Timestamp convUtilDateToSqlTimestamp(Date date) {
		if (date == null)
			return null;
		else
			return new Timestamp(date.getTime());
	}
	public static Calendar convUtilDateToUtilCalendar(Date date) {
	        if (date == null)
	                return null;
	        else {
	                java.util.GregorianCalendar gc = new java.util.GregorianCalendar();
	                gc.setTimeInMillis(date.getTime());
	                return gc;
	        }
	}
	/**
	 * 内部方法，根据某个索引中的日期格式解析日期
	 * @param dateStr 期望解析的字符串
	 * @param index 日期格式的索引
	 * @return 返回解析结果
	 */
	public static Timestamp parseTimestamp(String dateStr, int index) {
		DateFormat df = null;
		try {
			df = new SimpleDateFormat(dateFormat[index]);

			return new Timestamp(df.parse(dateStr).getTime());
		} catch (ParseException pe) {
			return new Timestamp(parseDate(dateStr, index + 1).getTime());
		} catch (ArrayIndexOutOfBoundsException aioe) {
			return null;
		}
	}

	/**
	 * 内部方法，根据默认的日期格式“yyyy-MM-dd”解析日期
	 * @param dateStr 期望解析的字符串
	 * @return 返回解析结果
	 */
	public static Timestamp parseTimestamp(String dateStr) {
		DateFormat df = null;
		try {
			df = new SimpleDateFormat(dateFormat[3]);
			return new Timestamp(df.parse(dateStr).getTime());
		} catch (ParseException pe) {
			return null;
		} catch (ArrayIndexOutOfBoundsException aioe) {
			return null;
		}
	}
	
	public static Timestamp getCurrentTimestamp(){
		return new Timestamp(System.currentTimeMillis());
	}
	
	public static Date getCurrentDate(){
		return new Date(System.currentTimeMillis());
	}
	
	/**
	 * 
	 */
	public static final String MATRIX_NULL_FLAG = "/";

	public static final long MILSEC_PER_DAY = 24 * 3600000;

	public static final FastDateFormat FAST_DAY_FORMATTER = FastDateFormat
			.getInstance("yyyy-MM-dd");

	public static final FastDateFormat FAST_MINUTE_FORMATTER = FastDateFormat
			.getInstance("yyyy-MM-dd HH:mm");

	public static final FastDateFormat FAST_SECOND_FORMATETR = FastDateFormat
			.getInstance("yyyy-MM-dd HH:mm:ss");
	public static final FastDateFormat FAST_SHORT_FORMATETR = FastDateFormat
			.getInstance("yyyyMMdd");
	public static final FastDateFormat FAST_ONLY_MINUTE_FORMATETR = FastDateFormat
			.getInstance("HH:mm");

	public static String getLastDate(int year, int month) {
		int[] monthDay = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
			monthDay[1] = 29;
		}
		int monthDayNum = monthDay[month - 1];
		String end = year + "-" + month + "-" + monthDayNum;
		return end;
	}
	
	public static int getmonthDayNum(int year, int month) {
		int[] monthDay = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
			monthDay[1] = 29;
		}
		int monthDayNum = monthDay[month - 1];	
		return monthDayNum;
	}

	/* 格式化日期为短形式 */
	public static String getShortDate(Date myDate) {
		return FAST_SHORT_FORMATETR.format(myDate);
	}

	/* 格式化日期为标准形式 */
	public static String formatDateTime(Date myDate, String pattern) {
		FastDateFormat format = FastDateFormat.getInstance(pattern);
		return format.format(myDate);
	}

	/* 判断myDate是否为null */
	public static Date isDate(Date myDate) {
		if (myDate == null)
			return new Date();
		return myDate;
	}

	public static Date currentDate() {
		return currentDate(null);
	}
	
	public static Date currentDateIgnoreTime() {
		return truncate(new Date(), Calendar.DATE);
	}

	/**
	 * 
	 * @return
	 */
	public static Date currentDate(DateFormat format) {
		Date today = new Date();
		if (format != null) {
			String string = format.format(today);
			today = parse(string, format);
		}
		return today;
	}

	/**
	 * 查询当天的前n天的具体时间
	 * @param n
	 * @return
	 */
	public static String getCurrentDateBefore(int n) {
		  Calendar day=Calendar.getInstance();  
          day.add(Calendar.DATE,-n);  
          SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
          String result=sdf.format(day.getTime());
		  return result;
	}
	
	/**
	 * 日期差(time1 - time2，返回负数，若time1在time2之前)
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static long getQuot(Date time1, Date time2) {
		long quot = 0;

		try {
			Date date1 = time1;
			Date date2 = time2;
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quot;
	}
	
	/**
	 * 日期差(忽略时间部分，time1 - time2，返回负数，若time1在time2之前)
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static long getQuoteIgnoreTime(Date time1, Date time2) {
		return getQuot(truncate(time1, Calendar.DATE), truncate(time2, Calendar.DATE));
	}
	
	public static Date max(Date time1, Date time2) {
		return time1.before(time2) ? time2 : time1;
	}
	
	public static Date min(Date time1, Date time2) {
		return time1.before(time2) ? time1 : time2;
	}
	
	public static boolean between(Date date, Date start, Date end) {
		return !date.before(start) && !date.after(end);
	}

	// 判断日期为星期几,1为星期日com.vnvtrip.util,依此类推
	public static int dayOfWeek(Object date1) {
		Date date = (Date) date1;
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(date);
		// 计算此日期是一周中的哪一天
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		return x;
	}
	
	// 判断日期为星期几,1为星期一
	public static int dayOfWeek3(Object date1) {
		Date date = (Date) date1;
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(date);
		// 计算此日期是一周中的哪一天
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		return x != 1 ? x - 1: 7;
	}

	public static String dayOfWeek2(Object date1) {
		Date date = (Date) date1;
		// 首先定义一个calendar，必须使用getInstance()进行实例化
		Calendar aCalendar = Calendar.getInstance();
		// 里面野可以直接插入date类型
		aCalendar.setTime(date);
		// 计算此日期是一周中的哪一天
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		return dayOfWeekByDayNum(x);
	}

	public static String dayOfWeekByDayNum(int x) {
		String str = "周日";
		if (x == 7) {
			str = "周六";
		} else if (x == 6) {
			str = "周五";
		} else if (x == 5) {
			str = "周四";
		} else if (x == 4) {
			str = "周三";
		} else if (x == 3) {
			str = "周二";
		} else if (x == 2) {
			str = "周一";
		}
		return str;
	}

	// 根据当前一个星期中的第几天得到它的日期
	public static Date getDateOfCurrentWeek(int day) {
		Calendar aCalendar = Calendar.getInstance();
		int x = aCalendar.get(Calendar.DAY_OF_WEEK);
		aCalendar.add(Calendar.DAY_OF_WEEK, day - (x - 1));
		return aCalendar.getTime();
	}

	// 某一天在一个月中的第几周
	public static int getWeekOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	public static Date addSomeDay(Date oldDate, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(oldDate);
		calendar.add(Calendar.DATE, num);
		return calendar.getTime();
	}

	// 把日期“2006-09-01”转化成20060901
	public static String Dateformat(String date) {
		int i = date.length();
		StringBuffer newdate = new StringBuffer(date.substring(0, 4));
		if (i == 8) {

			newdate.append(0);
			newdate.append(date.substring(5, 6));
			newdate.append(0);
			newdate.append(date.substring(7, 8));
		} else if (i == 9) {
			if (date.substring(7, 8).equalsIgnoreCase("-")) {

				newdate.append(date.substring(5, 7));
				newdate.append(0);
				newdate.append(date.substring(8, 9));
			} else {
				newdate.append(0);
				newdate.append(date.substring(5, 6));
				newdate.append(date.substring(7, 9));
			}

		} else {

			newdate.append(date.substring(5, 7));
			newdate.append(date.substring(8, 10));
		}

		return newdate.toString();

	}

	/* 新增static方法 */
	/* 格式话日期为yyyy-MM-dd形式 */
	public static String formatDate(Date myDate) {
		return FAST_DAY_FORMATTER.format(myDate);
	}

	/* 格式话日期为yyyy-MM-dd HH:mm形式 */
	public static String formatDateMinutes(Date myDate) {
		return FAST_MINUTE_FORMATTER.format(myDate);
	}

	/* 格式话日期为yyyy-MM-dd HH:mm:ss形式 */
	public static String formatDateTime(Date myDate) {
		return FAST_SECOND_FORMATETR.format(myDate);
	}

	/* 格式话日期为yyyy-MM-dd HH:mm:ss形式 */
	public static String formatDateMinutesTime(Date myDate) {
		return FAST_ONLY_MINUTE_FORMATETR.format(myDate);
	}

	/* 将字符串转换成日期 */
	public static Date getDateByString(String rq) {

		DateFormat df = new SimpleDateFormat();
		Date d = null;
		try {
			d = df.parse(rq);
		} catch (Exception e) {
		}
		return d;
	}

	public static Date getDateByString(String str, String pattern) {
		SimpleDateFormat sdf = null;
		try {
			sdf = new SimpleDateFormat(pattern);
			return sdf.parse(str);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 比较时间是否相同
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean equals(Date start, Date end) {
		if (start != null && end != null && start.getTime() == end.getTime()) {
			return true;
		}
		return false;
	}

	public static final Date convertStringToDate(String aMask, String strDate) {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);

		try {
			date = df.parse(strDate);
		} catch (Exception pe) {
			pe.printStackTrace();
		}
		return (date);
	}

	// add by csg
	// 当前月份第一天
	public static Date getCurrentMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	// 得到当前系统日期．add by lnb 12.12

	public static String getCurrentTime() {
		Date myDate = new Date(System.currentTimeMillis());
		return formatDateTime(myDate);
	}

	public static boolean isSameDay(Date c1, Date c2) {
		return formatDate(c1).equals(formatDate(c2));
	}

	public static Calendar string2Cal(String arg) {
		SimpleDateFormat sdf = null;
		String trimString = arg.trim();
		if (trimString.length() > 14)
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		else
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = sdf.parse(trimString);
		} catch (ParseException e) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal;
	}

	/**
	 * 匹配是否在某个时间段中
	 * 
	 * @param timePeriod
	 *            "00:00-06:00"格式
	 * @param time
	 *            "07:30"
	 * @return
	 */
	public static boolean isInPeriod(String timePeriod, String time) {
		String startTime = timePeriod.substring(0, 5);

		String endTime = timePeriod.substring(6);

		String timeTime = time;

		// 和时间段的开始或者结束刚好相等的时候
		if (startTime.equalsIgnoreCase(timeTime)
				|| endTime.equalsIgnoreCase(timeTime)) {
			return true;
		}

		SimpleDateFormat d = new SimpleDateFormat("HH:mm");

		try {
			Date startDate = d.parse(startTime);

			Date endDate = d.parse(endTime);

			Date timeDate = d.parse(timeTime);

			if (timeDate.after(startDate) && timeDate.before(endDate)) {
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static boolean isBetween(Date date, Date from, Date to) {
		//Assert.notNull(date, "date cannot be null.");
		//Assert.notNull(from, "from cannot be null.");
	//	Assert.notNull(to, "to cannot be null.");
		//Assert.isTrue(!from.after(to), "from cannot be after to.");
		return !date.before(from) && !date.after(to);
	}

	public static Date ifNull(Date date, Date defaultDate) {
		return date != null ? date : defaultDate;
	}

	public static String format(Date date, DateFormat df) {
		if (date == null) {
			return "";
		} else if (df != null) {
			return df.format(date).toLowerCase();
		} else {
			return FAST_DAY_FORMATTER.format(date);
		}
	}

	public static String format(Date date) {
		return format(date, null);
	}

	public static Date parseUseDefaultFormat(String date) {
		return parse(date, getDayFormatter());
	}

	public static Date parse(String date, DateFormat df) {
		try {
			return df.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException("parse date [" + date
					+ "] failed in use [" + getDayFormatter() + "]", e);
		}
	}

	// 增加或减少几个月
	public static Date addMonth(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.MONTH, num);
		return startDT.getTime();
	}

	// 增加或减少天数
	public static Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}

	public static List<Date> splitDays(Date start, Date end) {
		return splitDays(start, end, null);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @param dayOfWeeks
	 *            周日(1), 周一(2), ..., 周六(7)
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<Date> splitDays(Date start, Date end, int[] dayOfWeeks) {
		List<Date> dates = new ArrayList<Date>();
		for (Date date = start; !date.after(end); date = addDay(date, 1)) {
			if (ArrayUtils.isEmpty(dayOfWeeks)
					|| ArrayUtils.contains(dayOfWeeks, date.getDay() + 1)) {
				dates.add(date);
			}
		}
		return dates;
	}
	
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param dayOfWeeks
	 *            周日(1), 周一(2), ..., 周六(7)
	 * @return
	 */
	public static List<Date> splitDays2(Date start, Date end, int[] dayOfWeeks) {
		List<Date> dates = new ArrayList<Date>();
		for (Date date = start; !date.after(end); date = addDay(date, 1)) {
			if (ArrayUtils.isEmpty(dayOfWeeks)
					|| ArrayUtils.contains(dayOfWeeks, dayOfWeek3(date))) {
				dates.add(date);
			}
		}
		return dates;
	}

	/**
	 * 取得时间距阵
	 * 
	 * @param dayStart
	 * @param dayEnd
	 * @return
	 */
	public static List<String> getDayPeriodFullMatrix(String dayStart,
			String dayEnd) {

		List<String> retList = new ArrayList<String>();
		Calendar calStart = new GregorianCalendar();
		Calendar calEnd = new GregorianCalendar();
		String str = null;
		try {
			calStart.setTime(getDayFormatter().parse(dayStart));
			calEnd.setTime(getDayFormatter().parse(dayEnd));
			calEnd.add(Calendar.DATE, 1); // 包含最后一天

			// 前端补足
			int dayOfWeek = calStart.get(Calendar.DAY_OF_WEEK);
			for (; dayOfWeek > 1; dayOfWeek--) {
				retList.add(MATRIX_NULL_FLAG);
			}

			// 中间部分
			for (; calStart.before(calEnd); calStart.add(Calendar.DATE, 1)) {
				str = FAST_DAY_FORMATTER.format(calStart.getTime());
				retList.add(str);
			}

			// 后端补足
			dayOfWeek = calEnd.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek != 1) {
				for (; dayOfWeek != 1 && dayOfWeek <= 7; dayOfWeek++) {
					retList.add(MATRIX_NULL_FLAG);
				}
			}
			if(retList.size()<42) {
				int length=42-retList.size();
				for(int i=0;i<length;i++) {
					retList.add(MATRIX_NULL_FLAG);
					//		System.out.println(i);
				}
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		return retList;
	}

	/**
	 * 取得时间距阵
	 * 
	 * @param dayStart
	 * @param dayEnd
	 * @return
	 */
	public static List<String> getDayPeriodFullMatrix(Date dayStart, Date dayEnd) {
		return getDayPeriodFullMatrix(format(dayStart), format(dayEnd));
	}

	/**
	 * <li>SimpleDateFormat is not thread saft, so when you need, you should
	 * create it</li>
	 */
	public static SimpleDateFormat getDayFormatter() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}

	public static SimpleDateFormat getMinuteFormatter() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}

	public static SimpleDateFormat getMonthFormatter() {
		return new SimpleDateFormat("yyyy-MM");
	}
	
	public static SimpleDateFormat getSecondFormatter() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	public static String getFormatSecondDate() {
		// TODO Auto-generated method stub
		Date date = new Date(); 
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String date1=( simpleDateFormat).format(date);
		return date1;
	}
	public static String getFormatMinSecondDate() {
		// TODO Auto-generated method stub
		Date date = new Date(); 
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
		String date1=( simpleDateFormat).format(date);
		return date1;
	}
	public static String getFormatMinDate() {
		// TODO Auto-generated method stub
		Date date = new Date(); 
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmm"); 
		String date1=( simpleDateFormat).format(date);
		return date1;
	}
	
	public static String getFormatMonthDate() {
		// TODO Auto-generated method stub
		Date date = new Date(); 
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM"); 
		String date1=( simpleDateFormat).format(date);
		return date1;
	}
	
	public static String getLastMonth(String pgid,boolean lastmonthflag){
		StringBuffer buf=new StringBuffer(" from TPlanTask pt where  1=1  and pt.pgid='"+pgid+"'");
		java.util.Calendar c=Calendar.getInstance();//今天的时间
		int month = c.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
		int yu=month%3;
		System.out.println("取余："+yu);
		System.out.println("当月="+month);
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
		java.util.Date date0=c.getTime();
		String date1=( simpleDateFormat).format(date0);
		c.add(Calendar.MONTH, -1);//今天的时间月份-1支持1月的上月
		java.util.Date date=c.getTime();
		String date2=( simpleDateFormat).format(date);
		System.out.println("上月="+date2);
		c.add(Calendar.MONTH, -1);//今天的时间月份-1支持1月的上月
		date=c.getTime();
		String date3=( simpleDateFormat).format(date);
		System.out.println("上上月="+date3);
		
		//buf.append(" and  pt.pstartdate='"+date1+"'");
		if(yu == 0){
			buf.append(" and ( pt.pstartdate='"+date1+"' or pt.pstartdate='"+date2+"' or pt.pstartdate='"+date3+"')");
		}else if(yu == 2){
			buf.append(" and ( pt.pstartdate='"+date1+ "' or pt.pstartdate='"+date2+"')");
		}else{ //yu == 1: 不做处理，按季度进行查询，上季度的月份不列入清单
			buf.append(" and  pt.pstartdate='"+date1+"'");
		}
		
		
		/*
		if(lastmonthflag==false){
			//月份为6
			if(yu==0){
				buf.append(" and (pt.pstartdate='"+date2+"' or pt.pstartdate='"+date3+"')" );
				//from TPlanTask pt where pt.pstartdate 1=1  and pt.pgid=201  and (pt.pstartdate='2014-05' or pt.pstartdate='2014-04')
			}
			//月份为4
			if(yu==1){
				buf.append(" and 1=0 ");
				//
			}
			//月份为5
			if(yu==2){
				buf.append(" and pt.pstartdate='"+date2+"'");
				// from TPlanTask pt where pt.pstartdate 1=1  and pt.pgid=201  and pt.pstartdate='2014-04'
			}
		}
		else{
			buf.append(" and  pt.pstartdate='"+date1+"'");
		}
		*/
		System.out.println(buf.toString());
		return buf.toString();

	}
	public static String getFormatMiniteDate() {
		// TODO Auto-generated method stub
		Date date = new Date(); 
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH-mm"); 
		String date1=( simpleDateFormat).format(date);
		return date1;
	}
	
	public static String gettimeOnlyOwnByGPS() {
		// TODO Auto-generated method stub
		Date date = new Date(); 
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH-mm"); 
		String date1=( simpleDateFormat).format(date);
		return date1;
	}
	public static String getFormatDayDate() {
		// TODO Auto-generated method stub
		Date date = new Date(); 
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd"); 
		String date1=( simpleDateFormat).format(date);
		return date1;
	}
	public static void main(String[] args) {
//		List<String> matrix = getDayPeriodFullMatrix("2010-02-01", "2010-02-28");
//		System.out.println(matrix);
//		System.out.println(matrix.size());
//		Date time1 = parse("2009-09-26 09:00:23", getSecondFormatter());
//		Date time2 = parse("2009-09-27 09:00:24", getSecondFormatter());
//		System.out.println(getQuoteIgnoreTime(time1, time2));
		
		String aa=MaapDateFormatter.getStoreFormatter().
		format(new Date(System.currentTimeMillis() - 10 * 3600 * 24 * 1000));
		//System.out.println(aa);
		//System.out.println(10 * 3600 * 24 * 1000);
	}

	/**
	 * 字符串格式时间转换到对象时间
	 * 
	 * @param str
	 * @return
	 */
	public static Date string2DateTime(String str) {
		SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = fo.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 返回date时间 只到天 2008-05-20 00:00
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNoHHMMDate(Date date) {
		return org.apache.commons.lang.time.DateUtils.truncate(date,
				Calendar.DATE);
	}

	/**
	 * 增加天
	 * 
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date addDays(Date date, int i) {
		return org.apache.commons.lang.time.DateUtils.addDays(date, i);
	}

	public static long diff(Date d1, Date d2, String field) {
		long d1t = d1.getTime();
		long d2t = d2.getTime();
		if ("middleNight".equalsIgnoreCase(field)) { // 计算间夜，先除后减
			return d1t / MILSEC_PER_DAY - d2t / MILSEC_PER_DAY;
		} else {
			return d2t - d1t;
		}
	}

	public static String getLikeTimePointCode(Date myDate) {
		String myTime = formatDateMinutesTime(myDate);
		String[] myTimeList = myTime.split(":");
		return myTimeList[0] + myTimeList[1] + "00";
	}

    /**
     * 筛选日期(400系统)
     *
     * @param start
     * @param end
     * @param dayOfWeeks
     * @return
     */
    public static List<Date> getDates(Date start, Date end, Integer[] dayOfWeeks) {
        List<Date> list = new ArrayList<Date>();
        Date date = start;
        for(int i = 1;i <= 7;i++) {
            if(ArrayUtils.contains(dayOfWeeks,dayOfWeek(date))) {
                while(date.compareTo(end) <= 0) {
                    list.add(date);
                    date = addDays(date,7);
                }
                date = addDays(start,i);
            } else {
                date = addDays(date,1);
            }
        }
        return list;
    }
    
    /**
     * 比较日期
     */
    public static boolean constractDate(Date d1,Date d2){
    	if(d1==null||d2==null)return false;
    	Date date1=string2DateTime(formatDate(d1));
    	Date date2=string2DateTime(formatDate(d2));	
    	if(date1.equals(date2)) return true;
    	return date1.before(date2);
    }
    
    /**
     * 星期几
     * @param date
     * @return
     */
    public static int getWeekByDate(Date date) {
    	Calendar cal=Calendar.getInstance();                  
		cal.setTime(date);  
		return cal.get(Calendar.DAY_OF_WEEK);
    }
    
    /**
     * 比较两日期是否相等
     */
    public static boolean equalDate(Date d1,Date d2){
    	if(d1==null||d2==null)return false;
    	Date date1=string2DateTime(formatDate(d1));
    	Date date2=string2DateTime(formatDate(d2));	
    	return date1.equals(date2);
    }
    
    /**
	 * 将某个日期转换成业务逻辑上面的星期几
	 * calendar:	周一：2；周二：3；周三：4；周四：5；周五：6；周六：7；周日：1
	 * 业务逻辑  ：   周一：1；周二：2；周三：3；周四：4；周五：5；周六：6；周日：7
	 * @param day
	 * @return
	 */
	public static String explainDayOfWeek(Date date){
		int departDay = dayOfWeek(date);
		if(departDay == 1){
			return "7";
		}else if(departDay == 2){
			return "1";
		}else{
			return String.valueOf(departDay-1);
		}
	}
	
	/**
	 * 比较 是否当前日子以前的日子(不包含当前天) 
	 */
	public static boolean gtAfter(Date date){
		return date.before(addDay(new Date(),-1));
	}
	
	/**
	 * 比较两个日期相差的天数,忽略时分秒
	 * 例2009-12-28 10:00:00 到 2009-12-29 09:00:00 返回1
	 */
	public static int getIntervalDays(Date fDate, Date oDate) {

	       if (null == fDate || null == oDate) {

	           return -1;

	       }
	       
	       Calendar d1 = Calendar.getInstance();
	       Calendar d2 = Calendar.getInstance();
	       d1.setTime(fDate);
	       d2.setTime(oDate);
	       
	       d1.set(Calendar.HOUR, 0);
	       d1.set(Calendar.MINUTE, 0);
	       d1.set(Calendar.SECOND, 0);
	       fDate = d1.getTime();
	       
	       d2.set(Calendar.HOUR, 0);
	       d2.set(Calendar.MINUTE, 0);
	       d2.set(Calendar.SECOND, 0);
	       oDate = d2.getTime();
	       
	       long intervalMilli = oDate.getTime() - fDate.getTime();

	       return (int) (intervalMilli / (24 * 60 * 60 * 1000));

    }
	
	/**
	 * 比较两个日期相差的天数
	 * 例2009-12-28 10:00:00 到 2009-12-29 09:00:00 返回0
	 */
	public static int getIntervalDays2(Date fDate, Date oDate) {

	       if (null == fDate || null == oDate) {

	           return -1;

	       }
	       
	       long intervalMilli = oDate.getTime() - fDate.getTime();

	       return (int) (intervalMilli / (24 * 60 * 60 * 1000));

  }
	

	/**
	 * 获取本季度时间
	 * 
	 * @param pgid
	 * @return
	 */
	public static String thisQuarter() {
		StringBuffer buf = new StringBuffer(" ");
		java.util.Calendar c = Calendar.getInstance();// 今天的时间
		int month = c.get(Calendar.MONTH) + 1; // 获取月份，0表示1月份
		int yu = month % 3;
		//System.out.println("取余：" + yu);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
		String date1 = null;
		String date2= null;
		String date3= null;

		//季度的第三月
		if (yu == 0) {
			Date date0 = c.getTime();
			 date1 = (simpleDateFormat).format(date0);//第三月
			c.add(Calendar.MONTH, -1);// 第二月
			Date date = c.getTime();
			 date2 = (simpleDateFormat).format(date); //第一月月
			c.add(Calendar.MONTH, -1);// 今天的时间月份-1支持1月的上月
			date = c.getTime();
			 date3 = (simpleDateFormat).format(date);
			
		}
		//季度的第一月
		else if(yu == 1){
			Date date0 = c.getTime();
			date1 = (simpleDateFormat).format(date0);//第一月
			c.add(Calendar.MONTH, +1);//第二月
			Date date = c.getTime();
			date2 = (simpleDateFormat).format(date); 
			c.add(Calendar.MONTH, +1);// 第三月
			date = c.getTime();
			date3 = (simpleDateFormat).format(date);
		}
		//季度的第二月
		else if (yu == 2) {
			Date date0 = c.getTime();
			date1 = (simpleDateFormat).format(date0);//第二月
			c.add(Calendar.MONTH, -1);// 第一月
			Date date = c.getTime();
			date2 = (simpleDateFormat).format(date); 
			c.add(Calendar.MONTH, 2);// 第三月
			date = c.getTime();
			date3= (simpleDateFormat).format(date);
			
		} 
		buf.append(" and ( pstartdate='" + date1) 
			.append("' or pstartdate='" + date2 )
			.append( "' or pstartdate='"+ date3 + "') ");
		//System.out.println(buf.toString());
		return buf.toString();
	}
	
}
