package com.intrepid.travel.utils;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

public class GPSManager {

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		

		if (gps) {
			return true;
		}
		return false;
	}

	/**
	 * 强制帮用户打开GPS
	 * @param context
	 */
	public static final void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (Exception e) {
//			Common.log("openGPS Exception = " + e.toString());
		}
	}
	
	public static void turnGPSOn(Context context) 
	{ 
	     Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE"); 
	     intent.putExtra("enabled", true); 
	     context.sendBroadcast(intent); 
	 
	    String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED); 
	    if(!provider.contains("gps")){ //if gps is disabled  
	        final Intent poke = new Intent(); 
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");  
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE); 
	        poke.setData(Uri.parse("3"));  
	        context.sendBroadcast(poke); 
	    } 
	} 
	
	//打开GPS设置界面
	public static final void openSettingGPS(Context context){
		Intent intent = new Intent();  
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        try   
        {  
        	context.startActivity(intent);  
        } catch(ActivityNotFoundException ex)   
        {  
            // The Android SDK doc says that the location settings activity  
            // may not be found. In that case show the general settings.  
            // General settings activity  
            intent.setAction(Settings.ACTION_SETTINGS);  
            try {  
            	context.startActivity(intent);  
            } catch (Exception e) {  
            }  
        }  
	}
	
    /** 
  * 根据两点间经纬度坐标（double值），计算两点间距离， 
  *  
  * @param lat1 
  * @param lng1 
  * @param lat2 
  * @param lng2 
  * @return 距离：单位为米 
  */  
 public static double DistanceOfTwoPoints(double lat1,double lng1,   
          double lat2,double lng2) {
	 double EARTH_RADIUS = 6378137; 
     double radLat1 = rad(lat1);  
     double radLat2 = rad(lat2);  
     double a = radLat1 - radLat2;  
     double b = rad(lng1) - rad(lng2);  
     double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  
             + Math.cos(radLat1) * Math.cos(radLat2)  
             * Math.pow(Math.sin(b / 2), 2)));  
     s = s * EARTH_RADIUS;  
     s = Math.round(s * 10000) / 10000;  
     return s;  
 } 
 
 private static double rad(double d) {  
     return d * Math.PI / 180.0;  
 } 

}
