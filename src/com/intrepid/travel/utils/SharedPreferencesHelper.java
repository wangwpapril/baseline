package com.intrepid.travel.utils;


import com.intrepid.travel.Enums.PreferenceType;
import com.intrepid.travel.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesHelper{

	private SharedPreferences settings;
	private SharedPreferences userSettings;
	private SharedPreferences.Editor editor ;
	private Context context;
	private String userName;
	
	public SharedPreferencesHelper(Context context){
		this.context = context;
		settings=PreferenceManager.getDefaultSharedPreferences(context);
		userName = MyApplication.getInstance().getCurrentUser();
		userSettings = this.context.getSharedPreferences(userName, Activity.MODE_PRIVATE);
	}
	
	/**
	 * 根据传入的Preference Key返回系统偏好设置中对应的Value
	 * @param key    	Preferences Key 唯一标示一个配置项
	 * @return
	 */
	public String getPreferenceValue(String key){
		String returnValue = "";
		try {
			returnValue = settings.getString(key, "");
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			Logger.e(ex);
			ex.printStackTrace();
		}		
		return returnValue;
	}
		
	/**
	 * 保存全局的偏好配置(与用户无关)
	 * @param key		Preferences Key 唯一标示一个配置项
	 * @param pType	Preference支持多种配置数据类型：Boolean/Int/Float/Long/String
	 * @param value	Preference配置项对应值
	 */
	public boolean SaveCommonPreferenceSettings(String key,PreferenceType pType,String value){
		try{
			//settings=this.context.getSharedPreferences("systemuser", Activity.MODE_PRIVATE);
			editor = settings.edit();		
			switch(pType){
				case Boolean :
					editor.putBoolean(key, Boolean.parseBoolean(value));
					break;
				case Int:
					editor.putInt(key, Integer.parseInt(value));
					break;
				case Float:
					editor.putFloat(key, Float.parseFloat(value));
					break;
				case Long:
					editor.putLong(key, Long.parseLong(value));
					break;
				case String:
					editor.putString(key, value);
					break;
				default:
					break;	
			}			
			editor.commit();
			return true;
		}catch(Exception ex){
			Logger.e(ex);
			return false;
		}
	}
	
	
	/**
	 * 根据传入的Preference Key返回用户偏好设置中对应的Value
	 * @param key    	Preferences Key 唯一标示一个配置项
	 * @return
	 */
	public String GetUserPreferenceValue(String key){
		String returnValue = "";
		try {
			returnValue = userSettings.getString(key, "");
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			Logger.e(ex);
			ex.printStackTrace();
		}
		
		return returnValue;
	}
	/**
	 * 负责保存用户自己的偏好设置(每个用户都以一个专用的偏好设置文件)
	 * @param key		     	Preferences Key 唯一标示一个配置项
	 * @param pType	     	Preference支持多种配置数据类型：Boolean/Int/Float/Long/String
	 * @param value			Preference配置项对应值
	 * @return
	 */
	public boolean SaveUserPreferenceSettings(String key,PreferenceType pType,String value){
		try{
			editor = userSettings.edit();		
			switch(pType){
				case Boolean :
					editor.putBoolean(key, Boolean.parseBoolean(value));
					break;
				case Int:
					editor.putInt(key, Integer.parseInt(value));
					break;
				case Float:
					editor.putFloat(key, Float.parseFloat(value));
					break;
				case Long:
					editor.putLong(key, Long.parseLong(value));
					break;
				case String:
					editor.putString(key, value);
					break;
				default:
					break;	
			}	
			editor.commit();
			return true;
		}catch(Exception ex){
			Logger.e(ex);
			return false;
		}
	}
}
