package com.intrepid.travel.ui.activity;

import java.util.List;

import com.intrepid.travel.MyApplication;
import com.intrepid.travel.R;
import com.intrepid.travel.utils.Common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseActivity extends Activity implements OnClickListener{

	ActivityManager activityManager;

	protected BaseActivity context;
	protected TextView tvTitleName;
	protected ImageView ivTitelName;
	protected ImageView ivTitleBack;
	protected ImageView ivTitleRight;
	protected EditText ivTitleMiddle;
	protected TextView tvTitleRight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.i(MyApplication.TAG, "Base Activity onCreate()");
//		Logger.logHeap(this.getClass()); 
		activityManager=(ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
		MyApplication.getInstance().addActivity(this);
		context = this;
		Common.context = this;


	}
	
	protected void initTitleView() {
		tvTitleName = (TextView) findViewById(R.id.title_name);
		ivTitelName = (ImageView)findViewById(R.id.title_name_iv);
		ivTitleBack = (ImageView) findViewById(R.id.title_iv_back);
		ivTitleRight = (ImageView) findViewById(R.id.title_iv_right);
		ivTitleMiddle = (EditText) findViewById(R.id.title_name_et);
		tvTitleRight = (TextView) findViewById(R.id.title_tv_right);
		initTitle();
	}

	protected abstract void initTitle();
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(MyApplication.TAG, "Base Activity onDestory()");
//		Logger.logHeap(this.getClass()); 
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	public void onBackClick(View view){
		onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Common.context = this;
	}

	@Override
	protected void onPause() {
		super.onPause();
		Common.context = null;
	}
	
}
