package com.intrepid.travel;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.intrepid.travel.Enums.LoginStatus;
import com.intrepid.travel.Enums.NetStatus;
import com.intrepid.travel.utils.DeviceInfoHelper;
import com.intrepid.travel.utils.Logger;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class MyApplication extends Application implements UncaughtExceptionHandler{
    
	public static String TAG="Travel Smart Application";
	public static boolean isStop=false;
//	public User currentUserInfo = null;
//	private UserService userService = null;
	private static DeviceInfoHelper deviceInfoHelper =null;
	
	private ArrayList<Activity> activityList;				
	
	private static LoginStatus loginStatus = null;		
	private static NetStatus netStatus = null;									
	private static String sessionId;								
	public static Object mLock;
	

	//搴旂敤瀹炰緥
	private static MyApplication instance;
	
	public static MyApplication getInstance(){
		return instance;
	}

	public void exit(){
		for(Activity activity : activityList){
			try {
				activity.finish();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		
		MyApplication.isStop=true;
		
		
//		Intent locationIntent = new Intent(this, LocationService.class);
	//	stopService(locationIntent);
				
		System.exit(0);		
	}
	
	public DeviceInfoHelper getDeviceInfoHelper(){
		deviceInfoHelper = new DeviceInfoHelper();
		return deviceInfoHelper;
	}
	
	public static String getSessionId() {
		return sessionId;
	}

	public static void setSessionId(String sessionId) {
		MyApplication.sessionId = sessionId;
	}
	
	public static NetStatus getNetStatus() {
		return netStatus;
	}

	public static void setNetStatus(NetStatus netStatus) {
		MyApplication.netStatus = netStatus;
	}
	
	public static LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public static void setLoginStatus(LoginStatus loginStatus) {
		MyApplication.loginStatus = loginStatus;
	}
	
	//娣诲姞Activity鍒板鍣ㄤ腑
	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	
	public ArrayList<Activity> getActivities(){
		refreshActivities();
		return activityList;
	}
	public void refreshActivities(){
		for(int i=0;i<activityList.size();i++){
			if(activityList.get(i).isFinishing())
				activityList.remove(i);
		}
	}

	HashMap<String, Object> intentHashMap;

	public HashMap<String, Object> getIntentHashMap() {
		if(intentHashMap==null){
			intentHashMap=new HashMap<String, Object>();
		}
		return intentHashMap;
	}

	public void setIntentHashMap(HashMap<String, Object> intentHashMap) {
		this.intentHashMap = intentHashMap;
	}
	

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Application onCreate()");
		instance=this;
		mLock=new Object();
		intentHashMap=new HashMap<String, Object>();
//		currentUserInfo = new User();
//		userService =new UserService(this);
//		loginStatus = LoginStatus.none;
		activityList = new ArrayList<Activity>();
				
		isStop=false;
		
		Thread.setDefaultUncaughtExceptionHandler(this);  
			
	}

	
	public String getCurrentUser(){
		return "test";
//		return currentUserInfo.getUsername();
	}

	
	public void uncaughtException(Thread thread, Throwable ex) {
		Logger.e(ex);
		exit();
        android.os.Process.killProcess(android.os.Process.myPid());  
        System.exit(1);	
		
	}
}