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

	private RelativeLayout uploadLayout;
	private TextView fileNameTextView,proTextView;
	private ProgressBar progressBar;
	
	
	private static final String TAG = "SplashActivity";	
	private static final int MSG_GETTOPICLIST_ERROR = 1;
	private static final int MSG_GETTOPICLIST_START = 2;
	private static final int MSG_GETTOPICLIST_PRO = 3;
	private static final int MSG_GETTOPICLIST_END = 4;
	private static final int MSG_GETAPPSERVER_ERROR = 5;
	private static final int MSG_GETAPPSERVER_FINISH = 6;
	private static final int MSG_VALIDATE_VERSION_FINISH = 7;
	private static final int MSG_VALIDATE_VERSION_ERROR = 8;
	private static final int MSG_NETWORK_NONE = 0;
	private static final int MSG_NETWORK_TIMEOUT = -1;
	private static final int MSG_VERSION_UPDATE_START=9;
	private static int NORMAL_TIMEOUT = 6000;				
	
   	
    private static String apkUrl = "";							 //系统锟斤拷装锟斤拷路锟斤拷 
    private static String savePath ="";  						//锟斤拷锟截帮拷装路锟斤拷
    private static String saveFileName="";					//锟斤拷锟斤拷路锟斤拷锟斤拷(锟斤拷锟斤拷锟侥硷拷锟斤拷)
    /* 锟斤拷锟斤拷锟斤拷锟酵ㄖ猽i刷锟铰碉拷handler锟斤拷msg锟斤拷锟斤拷 */
    private ProgressBar mProgress; 
    private static final int DOWN_UPDATE = 10;      
    private static final int DOWN_OVER = 11;
	protected static final int MSG_SHOWNOTICEDIALOG = 12;
	protected static final int MSG_SHOWNOTICEDIALOGOPTION = 13; 
    private int progress;      
    private boolean interceptFlag = false;
    private AlertDialog.Builder builder = null;
	
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

	/**
	 * 锟斤拷始锟斤拷
	 */
	private void initialize(){
		dvDeviceInfoHelper = new DeviceInfoHelper();
		mSharedPreferencesHelper = new SharedPreferencesHelper(SplashActivity.this);
	
	}
	

	 
	private void setUpViews(){
/*		uploadLayout=(RelativeLayout)findViewById(R.id.updateLayout_Splash);
		progressBar=(ProgressBar)findViewById(R.id.proBar_Splash);
		proTextView=(TextView)findViewById(R.id.proText_Splash);
		fileNameTextView=(TextView)findViewById(R.id.fileName_Splash);
*/	}
	
 private static HashMap<String, String> toHashMap(JSONObject jsonObject) throws JSONException 
    { 
    	HashMap<String, String> result = new HashMap<String, String>(); 
        @SuppressWarnings("unchecked")
		Iterator<String> iterator = jsonObject.keys(); 
        String key = null; 
        String value = null; 
        while (iterator.hasNext()) 
        { 
            key = iterator.next(); 
            value = jsonObject.getString(key); 
            result.put(key, value); 
        } 
        return result; 
    } 
    
 
}
