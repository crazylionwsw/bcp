package com.fuze.bcp.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 日期工具类
 */
public class DateTimeUtils {
	
	/**
	 * 比较两个日前之间的天数 
	 * @param smdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetweenToday(String smdate) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        long todaytime = cal.getTimeInMillis();         

        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        long between_days=(todaytime-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    }

	/**
	 * 比较两个日前之间的天数 
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(String smdate,String bdate) throws ParseException{  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();    
        cal.setTime(sdf.parse(smdate));    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(sdf.parse(bdate));    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));     
    }  



	/**
	 * 比较第二个时间戳是否大于第一个时间戳
	 * @param firstTs
	 * @param secTs
	 * @return
	 */
	public static boolean compareTwoTs(String firstTs,String secTs){
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat formatter_short = new SimpleDateFormat("yyyy-MM-dd");
		try{
			
			Date ts1 = firstTs.length()<=10?formatter_short.parse(firstTs):formatter.parse(firstTs);
			Date ts2 = secTs.length()<=10?formatter_short.parse(secTs):formatter.parse(secTs);
			return ts2.compareTo(ts1)>0;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 取得所属时区时间戳
	 * @param signHour	exam:"-8" 为北京的时间
	 * @return
	 */
	public static Timestamp getLocalTs(String signHour){
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT" + signHour);
		TimeZone.setDefault(tz);
		Date today = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp ts = Timestamp.valueOf(formatter.format(today));
		
		return ts;
	}
	/**
	 * 按照传入的日期格式，取得当前日期所对应的字符串
	 * @param format	exam:"yyyy-MM-dd"
	 * @return
	 */
	public static String getTodayStr(String format){
		Format formatter = new SimpleDateFormat(format);
		Date date = new Date();
		String dateStr = formatter.format(date);
		
		return dateStr;
	}

	/**
	 * 获取几个月之后或者几个月之前的今天
	 * @param format
	 * @param offsetNum
	 * @return
	 */
	public static String getOffsetMonthTodayStrOf(String format,int offsetNum){
		Format formatter = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH,offsetNum);
		String dateStr = formatter.format(cal.getTime());
		return dateStr;
	}

	/**
	 * 将字符串转化为日期
	 * @param format	exam:"yyyy-MM-dd"	
	 * @param str	exam:"2010-12-15" (与format对应)
	 * @return
	 */
	public static Date str2Date(String format, String str){
		Format formatter = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = ((DateFormat) formatter).parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(formatter.format(date));
		return date;
	}

	
	/**
	 * 取得所属时区时间戳
	 * @return
	 */
	public static Timestamp getLocalTs(){
		Date today = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp ts = Timestamp.valueOf(formatter.format(today));
		return ts;
	}
	/**
	 * 按照传入的日期格式，取得当前日期所对应的字符串
	 * @return
	 */
	public static String getCreateTime(){
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String dateStr = formatter.format(date);
		return dateStr;
	}

	/*
	* 设置每月第一天的日期
	* 如: 2017-12-01
	 */
	public static String actGetFirstDate(){
		//规定返回日期格式
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		Date theDate=calendar.getTime();
		GregorianCalendar gcLast=(GregorianCalendar)Calendar.getInstance();
		gcLast.setTime(theDate);
		//设置为第一天
		gcLast.set(Calendar.DAY_OF_MONTH, 1);
		String day_first=sf.format(gcLast.getTime());
		String firstDay = day_first+" 00:00:00.000";
		return firstDay;
	}

	public static String getCreateTimeMillis(){
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = new Date();
		String dateStr = formatter.format(date);
		return dateStr;
	}

	public static SimpleDateFormat getSimpleDateFormat(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日").
	 * @param date String 想要格式化的日期
	 * @param oldPattern String 想要格式化的日期的现有格式
	 * @param newPattern String 想要格式化成什么格式
	 * @return String
	 */
	public static String StringPattern(String date, String oldPattern, String newPattern) {
		if (date == null || oldPattern == null || newPattern == null)
			return "";
		SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern) ;        // 实例化模板对象
		SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern) ;        // 实例化模板对象
		Date d = null ;
		try{
			d = sdf1.parse(date) ;   // 将给定的字符串中的日期提取出来
		}catch(Exception e){            // 如果提供的字符串格式有错误，则进行异常处理
			e.printStackTrace() ;       // 打印异常信息
		}
		return sdf2.format(d);
	}


}
