package com.intrepid.travel.ui.activity;

import java.io.File;
import java.util.List;

import com.intrepid.travel.MyApplication;
import com.intrepid.travel.R;
import com.intrepid.travel.ui.fragment.BaseFragment;
import com.intrepid.travel.ui.fragment.OverviewFragment;
import com.intrepid.travel.utils.ToastHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends FragmentActivity {
		
	protected final static String TAG = MainActivity.class.getSimpleName();
	
	protected final static int REQUEST_CODE_CARMERA = 200;
	
	protected final static int REQUEST_CODE_LOGIN = 201;
	
	protected final static int REQUEST_CODE_FILTER_PICKER = 202;


	private BaseFragment mFragmentOverview;
	private long exitTime;

	private TextView mTxtViewLoginState;

	private ImageView mImgViewAvata;


	private MyApplication mApp;


	private View mCurrTabView;

	private BaseFragment mCurrFragment;
	private RelativeLayout mMainLayout;
	private RelativeLayout mBehindMenu;
	private View mWelcomePage;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mFragmentOverview = new OverviewFragment();
		
		mApp = (MyApplication) getApplication();

		createUI();

		switchFramgment(mFragmentOverview);

		
		mApp.addActivity(this);
	}


/*	
	private void resetInitFlag(){
		PreferenceUtils.writeBoolConfig(Constant.PreferKeys.KEY_HAS_DISPLAY_GUIDE_PAGE, false, this);
		PreferenceUtils.writeBoolConfig(Constant.PreferKeys.KEY_HAS_DISPLAY_SCRAWL, false, this);
		PreferenceUtils.writeIntConfig(Constant.PreferKeys.KEY_SHOW_UPDATE_COUNT, 0, this);
	}
	

*/	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void refreshAllData(){
		mFragmentOverview = new OverviewFragment();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if ((SystemClock.elapsedRealtime() - exitTime) > 2000) {
			ToastHelper.showToast("Press one more time to exit the app!", 2000);
			exitTime = SystemClock.elapsedRealtime();
		} else {
 			quit();
		}
	}
	
	private void quit(){
		mApp.exit();
		finish();
	}
	

	
	private Handler mUIHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			switch(what){
			case 0:
/*				mMainLayout.removeView(mWelcomePage);
				if(!PreferenceUtils.readBoolConfig(Constant.PreferKeys.KEY_HAS_DISPLAY_SCRAWL, MainActivity.this, false)){
					ScrawlDialog dialog = new ScrawlDialog(MainActivity.this);
					dialog.show();
					PreferenceUtils.writeBoolConfig(Constant.PreferKeys.KEY_HAS_DISPLAY_SCRAWL, true, MainActivity.this);
				}
				
				if(mWelcomePage instanceof WelcomePage){
					((WelcomePage)mWelcomePage).recycle();
				}else if(mWelcomePage instanceof ImageView){
					CommonUtils.recycleImageView((ImageView)mWelcomePage);
				}
*/				break;
			}
		};
	};
	
	void createUI() {
		mMainLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.main, null);
/*		mBehindMenu = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.fragment_behindmenu, null);
			
		if(!this.getIntent().getBooleanExtra(LoginActivity.FLAG_LOGIN, false)){
			if(!PreferenceUtils.readBoolConfig(Constant.PreferKeys.KEY_HAS_DISPLAY_GUIDE_PAGE, MainActivity.this, false)){
				mWelcomePage = new WelcomePage(this, mUIHandler);
				PreferenceUtils.writeBoolConfig(Constant.PreferKeys.KEY_HAS_DISPLAY_GUIDE_PAGE, true, MainActivity.this);
			}else{
				mWelcomePage = new ImageView(this);
				mWelcomePage.setLayoutParams(new LayoutParams(
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
						android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
				
				Bitmap b = BitmapFactory.decodeResource(this.getResources(), R.drawable.about_background);
				((ImageView)mWelcomePage).setImageBitmap(b);
				((ImageView)mWelcomePage).setScaleType(ScaleType.FIT_XY);
			}
			mMainLayout.addView(mWelcomePage);
		}
*/		
		setContentView(mMainLayout);
		

		
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};

	/**
	 * switch next fragment.
	 */
	void switchFramgment(BaseFragment fragment) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
		ft.commitAllowingStateLoss();
		mCurrFragment = fragment;
	}
/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (REQUEST_CODE_CARMERA == requestCode) {
				Intent intent = new Intent(this, FilterPickerActivity.class);
				intent.putExtras(data);
				startActivityForResult(intent, REQUEST_CODE_FILTER_PICKER);
			} else if (REQUEST_CODE_FILTER_PICKER == requestCode) {
				Intent intent = new Intent(this, VideoPlayActivity.class);
				intent.putExtras(data);
				startActivity(intent);
			} else if (REQUEST_CODE_LOGIN == requestCode) {
//				finish();
				mApp.notifyDataChanged(DataType.switch_square_tab);
			}
		}else if(resultCode == Activity.RESULT_CANCELED){
			if(REQUEST_CODE_LOGIN == requestCode){
				if(!(mCurrFragment instanceof SquareFragment)){
					mApp.notifyDataChanged(DataType.switch_square_tab);
				}
			}
		}
	}
*/


/*	private void showChoiceDlg(int msgResId) {
		new AlertDialog.Builder(this).setMessage(msgResId)
				.setNegativeButton(R.string.if_setting_psw_yes, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(getBaseContext(), ModifyPasswordActivity.class);
						intent.putExtra(BaseActivity.EXTRA, ModifyPasswordActivity.TYPE_INIT_PSW);
						startActivity(intent);
					}

				}).setPositiveButton(R.string.if_setting_psw_no, null).setCancelable(false).show();
	}
*/


}
