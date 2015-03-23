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
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.intrepid.travel.MyApplication;
import com.intrepid.travel.R;
import com.intrepid.travel.utils.DeviceInfoHelper;
import com.intrepid.travel.utils.SharedPreferencesHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Splash screen activity is used to show splash . Here we use handler to show
 * splash for particular time and then disappear automatically.
 * 
 */
public class SplashActivity extends Activity {

	private SharedPreferencesHelper mSharedPreferencesHelper = null;
	private DeviceInfoHelper dvDeviceInfoHelper;

	private static final String TAG = "SplashActivity";	
	private static int NORMAL_TIMEOUT = 6000;				
	
   		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.splash);
		MyApplication.getInstance().addActivity(this);
		this.initialize();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			@Override
			public void run(){
				Intent mIntent = new Intent(SplashActivity.this,MainActivity.class);
//				Intent mIntent = new Intent(SplashActivity.this,LoginActivity.class);
//				Intent mIntent = new Intent(SplashScreenActivity.this,SlidingdrawerActivity.class);
//				Intent mIntent = new Intent(SplashScreenActivity.this,DraweringActivity.class);
				startActivity(mIntent);
				SplashActivity.this.finish();
			}
		},2000);	
  
	}

	private void initialize(){
		dvDeviceInfoHelper = new DeviceInfoHelper();
		mSharedPreferencesHelper = new SharedPreferencesHelper(SplashActivity.this);
	
	}
	

}
