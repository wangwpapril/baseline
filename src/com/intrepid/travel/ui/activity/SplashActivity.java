package com.intrepid.travel.ui.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.intrepid.travel.Enums.PreferenceKeys;
import com.intrepid.travel.MyApplication;
import com.intrepid.travel.R;
import com.intrepid.travel.utils.DeviceInfoHelper;
import com.intrepid.travel.utils.GPSManager;
import com.intrepid.travel.utils.SharedPreferenceUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * Splash screen activity is used to show splash . Here we use handler to show
 * splash for particular time and then disappear automatically.
 * 
 */
public class SplashActivity extends Activity {


	private static final String TAG = "SplashActivity";	
	double latitude,longitude;
	private String provider;
	private LocationManager locationManager;
	private Location location;
	
   		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.splash);
		MyApplication.getInstance().addActivity(this);
		
		Location location = getLocation(this);
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}

		test();
//		String ss = SharedPreferenceUtil.getString(PreferenceKeys.userId.toString(), "");
		this.initialize();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			@Override
			public void run(){
				Intent mIntent = null;
				if(MyApplication.getLoginStatus()) {
					mIntent = new Intent(SplashActivity.this,SettingsActivity.class);
				}else{
					mIntent = new Intent(SplashActivity.this,LoginActivity.class);
				}
//				Intent mIntent = new Intent(SplashActivity.this,MainActivity.class);
//				mIntent = new Intent(SplashActivity.this,LoginActivity.class);
//				mIntent = new Intent(SplashActivity.this,TripsListActivity.class);
//				Intent mIntent = new Intent(SplashScreenActivity.this,SlidingdrawerActivity.class);
//				Intent mIntent = new Intent(SplashScreenActivity.this,DraweringActivity.class);
				startActivity(mIntent);
				SplashActivity.this.finish();
			}
		},2000);	
  
	}

	private void initialize(){
//		dvDeviceInfoHelper = new DeviceInfoHelper();
	
	}
	
	public static Location getLocation(Context context) {
		LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		return location;
	}

	public final void test() {
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// }
		// }).start();

		boolean isGpsOpen = GPSManager.isOPen(this);
		if (!isGpsOpen) {
			// GPSManager.openGPS(currentActivity);
			GPSManager.openSettingGPS(this);
			// GPSManager.turnGPSOn(currentActivity);
		} else {
			// 获取LocationManager服务
			locationManager = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
			// 获取Location Provider
			getProvider();
			// 如果未设置位置源，打开GPS设置界面
			// 获取位置
			location = locationManager.getLastKnownLocation(provider);
			// 显示位置信息到文字标签
			updateWithNewLocation(location);
			// 注册监听器locationListener，第2、3个参数可以控制接收gps消息的频度以节省电力。第2个参数为毫秒，
			// 表示调用listener的周期，第3个参数为米,表示位置移动指定距离后就调用listener
			locationManager.requestLocationUpdates(provider, 2000, 10,
					locationListener);
		}
	}

	// 获取Location Provider
	private void getProvider() {
		// 构建位置查询条件
		Criteria criteria = new Criteria();
		// 查询精度：高
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 是否查询海拨：否
		criteria.setAltitudeRequired(false);
		// 是否查询方位角:否
		criteria.setBearingRequired(false);
		// 是否允许付费：是
		criteria.setCostAllowed(true);
		// 电量要求：低
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 返回最合适的符合条件的provider，第2个参数为true说明,如果只有一个provider是有效的,则返回当前provider
		provider = locationManager.getBestProvider(criteria, true);
	}

	// Gps消息监听器
	private final LocationListener locationListener = new LocationListener() {
		// 位置发生改变后调用
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		// provider被用户关闭后调用
		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}

		// provider被用户开启后调用
		public void onProviderEnabled(String provider) {
		}

		// provider状态变化时调用
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	// Gps监听器调用，处理位置信息
	private void updateWithNewLocation(Location location) {
		String latLongString;
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			latLongString = "\n纬度 : " + lat + "\n经度 : " + lng;
		} else {
			latLongString = "无法获取地理信息";
		}
//		Common.log(latLongString);
		List<Address> listAddress = getAddressbyGeoPoint(location);
		if(listAddress != null && listAddress.size() > 0){
			String addressList = listAddress.toString();
			
//			Common.log(addressList);
			
			latLongString += "\n" + addressList;
			
		}
		
//		item_text.setText(latLongString);
	}

	// 获取地址信息
	private List<Address> getAddressbyGeoPoint(Location location) {
		List<Address> result = null;
		// 先将Location转换为GeoPoint
		// GeoPoint gp=getGeoByLocation(location);
		try {
			if (location != null) {
				// 获取Geocoder，通过Geocoder就可以拿到地址信息
				Geocoder gc = new Geocoder(this, Locale.getDefault());
				result = gc.getFromLocation(location.getLatitude(),
						location.getLongitude(), 1);
			}
		} catch (Exception e) {
//			Common.log("getAddressbyGeoPoint Exception = " + e.toString());
		}
		return result;
	}


}
