package com.fuze.bcp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SimpleUtils {

	private static final Logger logger = LoggerFactory.getLogger(SimpleUtils.class);


	/**
	 * 判断第一个日期是否在两个日期段之间
	 * @param date
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean betweenTwoDates(String date,String startDate,String endDate){
		boolean afterStart = false;
		boolean beforeEnd = false;
		//判断是否在startDate之后
		if(StringUtils.isEmpty(startDate)){
			afterStart = true;
		}else{
			afterStart = daysBetween(startDate,date)>=0;
		}

		if(StringUtils.isEmpty(endDate)){
			beforeEnd = true;
		}else{
			beforeEnd =  daysBetween(date,endDate)>=0;
		}

		return afterStart && beforeEnd;
	}
	/**
	 * 比较日前和今天的天数差距
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
	 * @param sdate  开始日期
	 * @param edate  结束日期
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(String sdate,String edate){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(sdate));
			long time1 = cal.getTimeInMillis();
			try {
				cal.setTime(sdf.parse(edate));
			} catch (Exception ex) {
				sdf.applyPattern("yyyy年MM月dd日");
				cal.setTime(sdf.parse(edate));
			}
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);

			return Integer.parseInt(String.valueOf(between_days));
		}catch(Exception ex){
			logger.error(ex.getMessage(),ex);
		}
		return 0;
    }

	/**
	 * 格式化小数位
	 * @param dl
	 * @param digit
	 * @return
	 */
	public static double formatDigit(double dl, int digit){
		String str = "#0";
		if(digit>1){
			str = str + ".";
		}
		for(int i=0;i<digit; i++){
			str = str + "0";
		}
		DecimalFormat df1 = new DecimalFormat(str);
		return Double.parseDouble(df1.format(dl));
	}
	/**
	 * 格式化整数位
	 * @param dl
	 * @return
	 */
	public static int formatInteger(double dl){
		return Double.valueOf(dl).intValue();
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
			logger.error(ex.getMessage(),ex);
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
		String dateStr = formatter.format(Calendar.getInstance().getTime());
		return dateStr;
	}

	/**
	 * 按照传入的日期格式，取得当前日期所对应的字符串
	 * @return
	 */
	public static String getTodayStr(){
		Format formatter = getOnlySimpleDateFormat();
		String dateStr = formatter.format(Calendar.getInstance().getTime());
		return dateStr;
	}


	/**
	 * 获取几个月之后或者几个月之前的同一个日子
	 * @param
	 * @return
	 */
	public static String getOffsetMonthToday(String date,int offsetNum)  {
		try {
			DateFormat formatter = getOnlySimpleDateFormat();
			Calendar cal = Calendar.getInstance();
			try {
				cal.setTime(formatter.parse(date));
			} catch (Exception ex) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
				cal.setTime(sdf.parse(date));
			}
			cal.add(Calendar.MONTH, offsetNum);
			String dateStr = formatter.format(cal.getTime());
			return dateStr;
		}catch(ParseException ex){
			String msg = String.format("传入的日期【%s】不合法！",date);
			logger.error(msg,ex);
			return msg;
		}
	}


	/**
	 * 获取几个月之后或者几个月之前的今天
	 * @param format
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
	 * 几天之前或者几天之后的日期
	 * @param format
	 * @return
	 */
	public static String getOffsetDaysTodayStrOf(String format,String date,int offsetNum){
		try {
			DateFormat formatter = new SimpleDateFormat(format);
			Calendar cal = Calendar.getInstance();
			cal.setTime(formatter.parse(date));
			cal.add(Calendar.DAY_OF_YEAR, offsetNum);
			String dateStr = formatter.format(cal.getTime());
			return dateStr;
		}catch(ParseException ex){
			return String.format("输入的日期[%s]不合法！",date);
		}
	}

	public static String getOffsetDaysTodayStrOf(String date,int offsetNum){
		try {
			DateFormat formatter = SimpleUtils.getOnlySimpleDateFormat();
			Calendar cal = Calendar.getInstance();
			cal.setTime(formatter.parse(date));
			cal.add(Calendar.DAY_OF_YEAR, offsetNum);
			String dateStr = formatter.format(cal.getTime());
			return dateStr;
		}catch(ParseException ex){
			return String.format("输入的日期[%s]不合法！",date);
		}
	}
	/**
	 * 几天之前或者几天之后的日期
	 * @param format
	 * @return
	 */
	public static String getOffsetDaysTodayStrOf(DateFormat format,String date,int offsetNum){
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(format.parse(date));
			cal.add(Calendar.DAY_OF_YEAR, offsetNum);
			String dateStr = format.format(cal.getTime());
			return dateStr;
		}catch(ParseException ex){
			return String.format("输入的日期[%s]不合法！",date);
		}
	}



	/**
	 * 确定ts是否已经超期
	 * @param ts
	 * @param days
	 * @return
	 */
	public static boolean isExpireDays(String ts,long days) throws ParseException {
		int existdays = daysBetweenToday(ts);
		return (existdays>days);
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
	 * 判断字符串是否是email格式
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str){
		String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	/**
	 * 将float类型的数据大小，转换为常见的大小，如：MB（即兆）。
	 * @param size  float类型
	 * @return 返回的字符串是以B、KB、MB、GB、TB为单位的大小表示
	 */
	public static String formatSize(float size){
		long kb = 1024L;
		long mb = kb*1024L;
		long gb = mb*1024L;
		long tb = gb*1024L;
		
		String result = "0 B";
		//不去转换负数，遇到时将提示出错
		if(size < 0){
			return "size error";
		}
		//对于超出float表示范围，则提示数据太大
		if(size > Float.MAX_VALUE){
			return "size too large";
		}
		//对数据大小从B到TB分区间处理，优先以最小的单位表示
		if(size < kb){
			result = String.format("%d B", (int)size);
		}else if(size < mb){
			result = String.format("%.2f KB", size/kb);
		}else if(size < gb){
			result = String.format("%.2f MB", size/mb);
		}else if(size < tb){
			result = String.format("%.2f GB", size/gb);
		}else{
			//对TB级的数据大小，精确到小数点后第三位（即GB）
			result = String.format("%.3f TB", size/tb);
		}
		return result;
	}
	
	/**
	 * 当使用ajax get方式提交后，再把文本回显为原字符串
	 * @param str	由数字编码组成的字符串，且以空格分隔
	 * @return	以unicode编码的字符串
	 */
	public static String unicodeToString(String str){
		if(str==null || "".equals(str)) return "";
		
		StringBuffer buf = new StringBuffer("");
		String[] ss = str.split(" ");
		for (int i = 0; i < ss.length; i++) {
			char c = (char)Integer.parseInt(ss[i].trim());
			buf.append(c);
		}
		String newStr = buf.toString();
		return newStr;
	}

	/**
	 * 根据生日获取年龄
	 * @param birthday
	 * @return
	 */
	public static Integer getAge(String birthday){
		int now = Calendar.getInstance().get(Calendar.YEAR);
		Calendar  cal = Calendar.getInstance();
		cal.setTime(str2Date("yyyy-MM-dd",birthday));
		return now -cal.get(Calendar.YEAR)+1;
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
	public static String getShortDate(){
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String dateStr = formatter.format(date);
		return dateStr;
	}


	public static SimpleDateFormat getSimpleDateFormat(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public static SimpleDateFormat getOnlySimpleDateFormat(){
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	public static SimpleDateFormat getMonthFormat(){
		return new SimpleDateFormat("yyyy-MM");
	}
	/**
	 * 根据文件名获取文件的后缀
	 * @param fileName
	 * @return fileName's suffix
	 */
	public static String getFileSuffix(String fileName){
		String suffix = "";
		if(fileName == null){
			return suffix;
		}
		//以"." 作为识别后缀的标识
		int suffixIndex = fileName.lastIndexOf(".");
		if(suffixIndex == -1){
			return suffix;
		}
		//取"." 后面的字符串作为后缀
		suffix = fileName.substring(suffixIndex + 1);
		return suffix;
	}


	/**
	 * 随机生成6位含有字母和数字的随机字符串
	 * @param length
	 * @return
     */
	public static String getStringRandom(int length) {

		String val = "";
		Random random = new Random();

		//参数length，表示生成几位随机数
		for(int i = 0; i < length; i++) {

			String charOrNum = random.nextInt(2) % 2 == 0 ? "num" : "num";
			//输出字母还是数字
			if( "char".equalsIgnoreCase(charOrNum) ) {
				//输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char)(random.nextInt(26) + temp);
			} else if( "num".equalsIgnoreCase(charOrNum) ) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	/**
	 * 随机生成手机验证码
	 * @return
     */
	public static char[] getIdentifyingCode(int length){
		String chars = "0123456789";
		char[] rands = new char[length];
		for (int i = 0; i < length; i++) {
			int rand = (int) (Math.random() * 10);
			rands[i] = chars.charAt(rand);
		}
		return rands;

	}

	public static double round(double value, int scale, int roundingMode) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(scale, roundingMode);
		double d = bd.doubleValue();
		bd = null;
		return d;
	}

	public static void main(String[] strs){
		try {
			System.out.println(daysBetween("2017-09-12","2017-09-05"));
//			System.out.println(getOffsetMonthTodayStrOf("YYYYMMdd", 3));
//			System.out.println(getOffsetMonthTodayStrOf("YYYY-MM-DD", -5));
			//System.out.println(isExpireDays("2017-06-30", 31));
//			System.out.println(getOffsetDaysTodayStrOf("yyyy-MM-dd","2017-08-01",-1));
//			System.out.println(getAge("1976-08-09"));
//			System.out.println(round(12.349,-1,BigDecimal.ROUND_DOWN));
//			System.out.println("舍掉小数取整:Math.floor(-2.5)=" + (int)Math.floor(-2.5));
			System.out.println(formatDigit(0.348432,2));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
