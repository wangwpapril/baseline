package com.intrepid.travel.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.R.integer;
import android.text.format.Time;
import android.util.Log;

public class TimeFormatHelper {

	public static Date addDate(Date d, int day) throws ParseException {

		long time = d.getTime();
		day = day * 24 * 60 * 60 * 1000;
		time += day;
		return new Date(time);

	}
	
	public static int DateInterval(Date st, Date ed){
		long sl = st.getTime();
		long el = ed.getTime();
		long ei = el - sl;
		
		int interval = (int)ei / (1000 * 60 * 60 * 24);
		
		return interval;
	}
	
	public static String getFormatTime(Date date){
		if(date == null)
			return "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
		return formatter.format(date);
	}
	
	/**
	 * 转换为字符串，时间格式mm:ss
	 * @param date
	 * @return
	 */
	public static String getFormatTimeV2(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat formatter = new SimpleDateFormat(
				"mm:ss");
		return formatter.format(date);
	}
	
	public static String getShortFormatTime(Date date, String format){
		if(date == null)
			return "";
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
	/**
	 * modify by SongQing ,增加从GMT 到本地时间的对应转换，解决显示GMT时间的问题
	 * @param date
	 * @return
	 */
	public static Date parse(String date){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
		Date temp = null;
		try {
			temp = format.parse(date);
			//double zone = TimeZone.getDefault().getRawOffset();

			//temp =  new Date((long) (temp.getTime() - zone));
			temp =  new Date((long) (temp.getTime()));

		} catch (Exception e) {
			Logger.e(e);
		}
		return temp;
	}
	
	/**
	 * 获取GMT时间，时间格式：20061218.094246.930+0800
	 * @param date
	 * @return
	 */
	public static String getGMTTime(Date date){
		
		if(date == null)
			return "";
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd.HHmmss.SSS");
		
		SimpleDateFormat timeZoneFormatter = new SimpleDateFormat("Z");
		
		String timeZone = timeZoneFormatter.format(date);
		
		int position = timeZone.indexOf("-");
		
		if(position == -1)
			position = timeZone.indexOf("+");
		
		timeZone = timeZone.substring(position); 
		
		String dateGMT = formatter.format(date);
		
		dateGMT = dateGMT.concat(timeZone);
		
		dateGMT = dateGMT.replace(":", "");
		
		return dateGMT;
	}
	
	/**
	 * 获取GMT时间，时间格式：20061218.094246.930+0800
	 * @param date 传入时间：20061218.094246.930+0800
	 * @return
	 */
	public static String getGMTTime(String date){
		
		Date temp = parse(date);
		
		if(temp == null)
			return "";
		
		return getGMTTime(temp);
	}
	
	/**
	 * 将毫秒转换为标准时间格式
	 * @param ms  eg：1340590080000
	 * @return
	 */
	public static String convertLongToDate(String ms){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		try{	
			calendar.setTimeInMillis(Long.parseLong(ms));
			
			return formatter.format(calendar.getTime());
		}catch(Exception e){
			Logger.e(e);
			Log.e("XINHUA'S PROJECT", e.toString());
			return formatter.format(new Date());
		}
	}
	
	/**
	 * 将标准时间格式转换为毫秒
	 * @param ms  
	 * @return
	 */
	public static long convertDateToLong(String ms){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try{
			Date myDate=formatter.parse(ms);
			

			return myDate.getTime();
		}catch(Exception e){
			Logger.e(e);
			Log.e("XINHUA'S PROJECT", e.toString());
			return new Date().getTime();
		}
	}
	
}
