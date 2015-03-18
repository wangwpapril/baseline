package com.intrepid.travel.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intrepid.travel.Enums.NetStatus;
import com.intrepid.travel.Enums.PreferenceKeys;
import com.intrepid.travel.Enums.PreferenceType;
import com.intrepid.travel.MyApplication;
import com.intrepid.travel.R;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.preference.Preference;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


public class DeviceInfoHelper {

	private TelephonyManager mTelephonyManager;
	private Context mContext;
	private static String ANDROID_DEFAULT_DEVICE_ID ="Android.IMEI.2012";
	private static final int ERROR = -1;

	public DeviceInfoHelper() {
		this.mTelephonyManager = (TelephonyManager) MyApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
		this.mContext = MyApplication.getInstance();
	}
	
	/**
	 * 鑾峰彇褰撳墠Android缁堢绾胯矾涓�殑鐢佃瘽鍙风爜
	 * @return
	 */
	public String getPhoneNumberString(){
		return mTelephonyManager.getLine1Number()==null?"":mTelephonyManager.getLine1Number();
	}
	
	/**
	 *  鍞竴鐨勮澶嘔D锛�  
	 *  GSM鎵嬫満鐨�IMEI 鍜�CDMA鎵嬫満鐨�MEID,娌℃湁3G妯″潡鐨凱ad杩斿洖android璁惧鍙�
	 * @return Return null if device ID is not available.   
	 */
	public String getDeviceId(){
	 
		String phoneDeviceID = mTelephonyManager.getDeviceId();
		String padDeviceID = Secure.getString(this.mContext.getContentResolver(), Secure.ANDROID_ID);
		if( phoneDeviceID != null){
			return phoneDeviceID;
		}else if(padDeviceID!= null ){
			return padDeviceID;
		}else{
			return ANDROID_DEFAULT_DEVICE_ID;
		}		
	}
	
	public String translateDeviceId(){
		String deviceId=getDeviceId();
		if(deviceId.length()>16){
			deviceId=Encrypt.toMD5(deviceId).substring(5, 20);
		}
		return deviceId;
		
	}
	/**
	 *  璁惧鐨勮蒋浠剁増鏈彿锛�  
	 *  渚嬪锛歵he IMEI/SV(software version) for GSM phones.  
	 * @return Return null if the software version is not available.   
	 */
	public String getDeviceSoftwareVersion(){
		return mTelephonyManager.getDeviceSoftwareVersion();
	}
	
	/**
	 * 璁惧鐨勫睆骞曞垎杈ㄧ巼
	 * @return  濡�00x480
	 */
	public String getDisplayMetrics(){
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager mWindowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.getDefaultDisplay().getMetrics(dm);  
		
		return dm.heightPixels + "x" + dm.widthPixels;   
	}
	
	/**
	 * 杩斿洖褰撳墠鎵嬫満鐨勫瀷鍙�
	 * @return 濡�Mailstone
	 */
	public String getDeviceModel(){
		return  android.os.Build.MODEL;
	}
	
	/**
	 * 鑾峰彇璁惧绯荤粺SDK鐗堟湰鍙�
	 * @return eg:8
	 */
	public static String getDeviceVersionSDK(){
		return  android.os.Build.VERSION.SDK;
	} 
	
	
	/**
	 * 鑾峰彇璁惧绯荤粺鍙戝竷鐗堟湰鍙�
	 * @return   eg:2.3.3
	 */
	public static String getDeviceVersionRelease(){
		return  android.os.Build.VERSION.RELEASE;
	} 	
	
	/**
	 * 杩斿洖褰撳墠绋嬪簭鐗堟湰鍚�
	 * @param context
	 * @return  android:versionCode="1" 	android:versionName="1.0" {1,1.0}
	 */
	public static String[] getAppVersionName(Context context) {     
	   String version[] = new String[2];
	   try {     
	       // ---get the package info---     
	       PackageManager pm = context.getPackageManager();     
	       PackageInfo pi = pm.getPackageInfo(context.getPackageName(),0);     
	       version[0] = String.valueOf(pi.versionCode);
	       version[1] = pi.versionName;   
	              
	    } catch (Exception e) {     
	        Log.e("VersionInfo", "Exception", e); 
	        Logger.e(e);
	    }  
	   return version;
	} 
	
	/**
	 * 鑾峰彇褰撳墠缃戠粶鐘舵�
	 */
	public NetStatus getNetStatus(){
		ConnectivityManager connMgr = null;
		NetworkInfo activeInfo = null;
		
		try {
			connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			activeInfo = connMgr.getActiveNetworkInfo(); 			//褰撳墠缃戠粶杩炴帴绫诲瀷鍒ゆ柇
		} catch (Exception e) {
			Logger.e(e);
		}
		
		if (activeInfo != null && activeInfo.isConnected()) {	//濡傛灉缃戠粶鍙敤(鏃犺浠�箞缃戠粶绫诲瀷)
			switch (activeInfo.getType()) {
			case ConnectivityManager.TYPE_MOBILE:			//绉诲姩缃戠粶
				return NetStatus.MOBILE;
			case ConnectivityManager.TYPE_WIFI:				//Wi-Fi缃戠粶
				return NetStatus.WIFI;
			default:	
				return NetStatus.Disable;
			}
		}
		else{		//缃戠粶涓嶅彲鐢�
			return NetStatus.Disable;
		}
	}
	

	
	 /**
     * 鑾峰彇鎵嬫満鍐呴儴鍓╀綑瀛樺偍绌洪棿
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 鑾峰彇鎵嬫満鍐呴儴鎬荤殑瀛樺偍绌洪棿
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 鑾峰彇SDCARD鍓╀綑瀛樺偍绌洪棿
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * 鑾峰彇SDCARD鎬荤殑瀛樺偍绌洪棿
     * @return
     */
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }
    
    /**
     * SDCARD鏄惁鍙敤
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
    
    public static String GetBuildProproperties(String propertiesName){
    	try {
    		InputStream is = new BufferedInputStream(new FileInputStream(new File("/system/build.prop")));
    		BufferedReader br = new BufferedReader(new InputStreamReader(is));
    		String strTemp = "";
    		while ((strTemp = br.readLine()) != null){// 濡傛灉鏂囦欢娌℃湁璇诲畬鍒欑户缁�
    			if (strTemp.indexOf(propertiesName) != -1){
    				return strTemp.substring(strTemp.indexOf("=") + 1);
    			}
    		}
    		br.close();
    		is.close();
    		return null;
		} catch (Exception e) {
				 e.printStackTrace();
				 return null;
		}
	}



}
