/**
 * 
 */
package org.reed.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author chenxiwen
 * @date 2017年7月26日下午4:57:54
 */
public final class TimeUtil {
	
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";

//	private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.CHINA);

	private static final ThreadLocal<SimpleDateFormat> dateTimeFormatContainer = new ThreadLocal<>();
	private static final Lock dateTimeFormatLock = new ReentrantLock();

	private static SimpleDateFormat dateTimeFormatter(){
		try{
			dateTimeFormatLock.lock();
			SimpleDateFormat sdf = dateTimeFormatContainer.get();
			if(sdf == null){
				sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.CHINA);
				dateTimeFormatContainer.set(sdf);
			}
			return sdf;
		}finally {
			dateTimeFormatLock.unlock();
		}

	}
	
//	private static SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
	private static final ThreadLocal<SimpleDateFormat> dateFormatContainer = new ThreadLocal<>();
	private static final Lock dateFormatLock = new ReentrantLock();

	private static SimpleDateFormat dateFormatter(){
		try{
			dateFormatLock.lock();
			SimpleDateFormat sdf = dateFormatContainer.get();
			if(sdf == null){
				sdf = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
				dateFormatContainer.set(sdf);
			}
			return sdf;
		}finally {
			dateFormatLock.unlock();
		}

	}
//	private static SimpleDateFormat formatter = new SimpleDateFormat();
	private static final ThreadLocal<SimpleDateFormat> formatterContainer = new ThreadLocal<>();
	private static final Lock formatterLock = new ReentrantLock();

	private static SimpleDateFormat formatter(String pattern){
		try{
			formatterLock.lock();
			SimpleDateFormat sdf = formatterContainer.get();
			if(sdf == null){
				sdf = new SimpleDateFormat();
				formatterContainer.set(sdf);
			}
			sdf.applyPattern(pattern);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
			return sdf;
		}finally {
			formatterLock.unlock();
		}

	}
	private static final Calendar calendar = Calendar.getInstance(Locale.CHINA);
	
	public static String format(String pattern, Date date) throws NullPointerException, IllegalArgumentException{
		return formatter(pattern).format(date);
	}
	
	public static String format(String pattern, long millis) throws NullPointerException, IllegalArgumentException{
		return formatter(pattern).format(new Date(millis));
	}
	
	public static Date parse(String pattern, String dateStr) throws NullPointerException, IllegalArgumentException, ParseException{
		return formatter(pattern).parse(dateStr);
	}
	
	public static String nowDateTime(){
		return dateTimeFormatter().format(new Date());
	}
	
	public static String nowDate(){
		return dateFormatter().format(new Date());
	}
	
	public static String getDateTime(Date date){
		return dateTimeFormatter().format(date);
	}
	
	public static String getDateTime(long millis){
		return dateTimeFormatter().format(new Date(millis));
	}
	
	public static Date fromDateTimeStr(String datetimeStr) throws ParseException{
		return dateTimeFormatter().parse(datetimeStr);
	}
	
	public static Date fromDateStr(String dateStr) throws ParseException{
		return dateFormatter().parse(dateStr);
	}
	
	public static Date fromMillis(long mills){
		return new Date(mills);
	}

}
