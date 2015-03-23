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
	private String userId;
	
	public SharedPreferencesHelper(Context context){
		this.context = context;
		settings=PreferenceManager.getDefaultSharedPreferences(context);
		userId = MyApplication.getInstance().getCurrentUser();
		userSettings = this.context.getSharedPreferences(userId, Activity.MODE_PRIVATE);
	}
	
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
		

	public boolean SaveCommonPreferenceSettings(String key,PreferenceType pType,String value){
		try{
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
