package com.intrepid.travel.ui.activity;

import java.io.File;
import java.util.List;

import com.intrepid.travel.MyApplication;
import com.intrepid.travel.ui.fragment.BaseFragment;

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


/**
 * User main activity, includes four fragments,
 * {@link com.huachuang.dingzhi.fragment.VideoPlayFragment},
 * {@link com.huachuang.dingzhi.fragment.CustomFragment},
 * {@link com.huachuang.dingzhi.fragment.AttentionFragment} ,
 * {@link com.huachuang.dingzhi.fragment.UserInfoFragment}
 * 
 */
public class MainActivity extends FragmentActivity {
		
	protected final static String TAG = MainActivity.class.getSimpleName();
	
	protected final static int REQUEST_CODE_CARMERA = 200;
	
	protected final static int REQUEST_CODE_LOGIN = 201;
	
	protected final static int REQUEST_CODE_FILTER_PICKER = 202;

//	private SquareFragment mFragmentSquare;

//	private BaseFragment mFragmentUserInfo;

//	private BaseFragment mFragmentAttention;

	private BaseFragment mFragmentCustom;


	private TextView mTxtViewLoginState;

	private ImageView mImgViewAvata;


	private MyApplication mApp;


	private int[] mSlidingMenuIds;

	private ImageView mTabViewPlaza;
	private ImageView mTabViewPerson;
	private ImageView mTabViewCustom;
	private ImageView mTabViewAtteton;
	
	
	/**
	 * Show a red dot,means that exist new message.
	 */
	private View mViewOrderHint;
	private View mViewMsgHint;

	private View mCurrTabView;

	private long exitTime;

	private BaseFragment mCurrFragment;
	private RelativeLayout mMainLayout;
	private RelativeLayout mBehindMenu;
	private View mWelcomePage;
	
	private IDataSourceListener<DataType> mDataSourceListener = new IDataSourceListener<DataType>() {

		@Override
		public void onChange(DataType type) {
			switch (type) {
			case notify_order:
				mViewOrderHint.setVisibility(View.VISIBLE);
				bindOrderHintCount(mViewOrderHint);
				break;
			case notify_msg:
				mViewMsgHint.setVisibility(View.VISIBLE);
				bindNotifyMsgHintCount(mViewMsgHint);
				break;
			case attention:
				mFragmentSquare.onRefreshData();
				mFragmentAttention.onRefreshData();			
				break;
			case anchor_apply_passed:
				getUserInfo();
				break;
			case switch_order_tab:
				mTabViewCustom.performClick();
//				PreferenceUtils.RemoveStrConfig(Constant.PreferKeys.KEY_NOTIFY_ORDER, mApp);
				mViewOrderHint.setVisibility(View.INVISIBLE);				
				break;
			case switch_square_tab:
				mTabViewPlaza.performClick();
				break;
			case switch_myself_publish_tab:
				((UserInfoFragment)mFragmentUserInfo).moveTo(UserInfoFragment.MY_VIDEO);
				mTabViewPerson.performClick();
				break;
			default:
				break;
			}

		}
	};
	
	void bindOrderHintCount(View parentView) {
		HintInfo item = mApp.getHintInfo();
		TextView txtView = (TextView) parentView.findViewById(R.id.txtViewNumber);
		txtView.setText(item.getDisplayOrderCount());		
	}
	
	void bindNotifyMsgHintCount(View parentView) {
		HintInfo item = mApp.getHintInfo();
		TextView txtView = (TextView) parentView.findViewById(R.id.txtViewNumber);
		txtView.setText(item.getDisplayMsgCount());				
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getSlidingMenu().setSlidingEnabled(false);
		registerBroadcast();
		mWebAPi = WebApi.getInstance(this);
		
		PushManager.getInstance().initialize(this);
		mSlideMenuAdapter = new SlideMenuAdapter(this, getResources().getStringArray(R.array.slide_menu_list), null);

		mFragmentSquare = new SquareFragment();
		mFragmentUserInfo = new UserInfoFragment();
		mFragmentAttention = new AttentionFragment();
		mFragmentCustom = new CustomFragment();
		
		mApp = (MyApplication) getApplication();
		checkVersion();
		
		initGetui();
		createUI();
		if(mWelcomePage instanceof ImageView){
			mUIHandler.sendEmptyMessageDelayed(0, 2000);
		}
		switchFramgment(mFragmentSquare);

		
		mFinalBitmap = FinalBitmap.create(this);
		mApp.addActivity(this);
		if (isAuthroized()) {
			getUserInfo();
		}
		mSlidingMenuIds = getResources().getIntArray(R.array.slide_menu_id_list);
		int loginType = getIntent().getIntExtra(BaseActivity.EXTRA, -1);
		if (loginType == LoginActivity.LOGIN_TYPE_FORGET_PSW) {
			showChoiceDlg(R.string.forget_psw_success);
		} else if (loginType == LoginActivity.LOGIN_TYPE_REGISTER) {
			showChoiceDlg(R.string.register_success);
		}
		if (isAuthroized()) {
			jumpActivity(getIntent());
		}
		mApp.registerDataListener(DataType.notify_msg, mDataSourceListener);
		mApp.registerDataListener(DataType.notify_order, mDataSourceListener);
		mApp.registerDataListener(DataType.attention, mDataSourceListener);
		mApp.registerDataListener(DataType.anchor_apply_passed, mDataSourceListener);
		mApp.registerDataListener(DataType.switch_myself_publish_tab, mDataSourceListener);
		mApp.registerDataListener(DataType.switch_order_tab, mDataSourceListener);
		mApp.registerDataListener(DataType.switch_square_tab, mDataSourceListener);
	}

	private void registerBroadcast(){
		IntentFilter filter = new IntentFilter(BroadConstant.BROAD_RE_LOGIN);
		registerReceiver(mReceiver, filter);
	}
	
	private void unregisterBroadcast(){
		unregisterReceiver(mReceiver);
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver(){
		public void onReceive(android.content.Context context, Intent intent) {
			
			if(intent.getAction().equals(BroadConstant.BROAD_RE_LOGIN)){
				
				if(!TimeInterval.getInstance().isOvertime(System.currentTimeMillis()))
					return;
				
				PreferenceUtils.RemoveStrConfig(Constant.PreferKeys.KEY_TOKEN, MainActivity.this);
				mWebAPi.setToken("");
				
				if(checkLoginActivityExist()){
					return;
				}
				Intent _intent = new Intent(getBaseContext(), LoginActivity.class);
				startActivityForResult(_intent, REQUEST_CODE_LOGIN);
				mApp.removeAllExceptMain();
				mApp.notifyDataChanged(DataType.switch_square_tab);
			}
			
		};
	};
	
	private boolean checkLoginActivityExist(){
		
		List<Activity> list = mApp.getActivityList();
		for(int i = 0 ; i < list.size() ; i ++){
			if(list.get(i) instanceof LoginActivity){
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isAuthroized(){
		 return mWebAPi.isLogined();
	}
	
	private void checkVersion(){
		
		final UpdateHelper helper = new UpdateHelper(this, true);
			
		UpdateInfo info = FinalDBHelper.getUpdateInfo(mApp.getFinalDb());
		
		if(info != null){
			if(info.getVersonId() == CommonUtils.getVersionCode(this)){ //check version if after upgrade.
				mApp.getFinalDb().deleteAll(UpdateInfo.class);
				resetInitFlag();
				return;
			}
			
			helper.setInfo(info);
			helper.setUpgradeType(UpdateHelper.UPGRADE_FORCE);
			helper.showForceUpgradeDialog();
			
			return;
		}
		
		helper.setCallBack(new OnCallBack() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(helper.getUpgradeType() == UpdateHelper.UPGRADE_FORCE){
					helper.showForceUpgradeDialog();
					FinalDBHelper.saveUpdateInfo(helper.getInfo(), mApp.getFinalDb());
				}else if(helper.getUpgradeType() == UpdateHelper.UPGRADE_NOT_FORCE){
					
					int count = PreferenceUtils.readIntConfig(Constant.PreferKeys.KEY_SHOW_UPDATE_COUNT, MainActivity.this, 0);
					if(count < UpdateHelper.DISPLAY_COUNT){
						helper.showNonUpgradeDialog();
						PreferenceUtils.writeIntConfig(Constant.PreferKeys.KEY_SHOW_UPDATE_COUNT, ++count, MainActivity.this);
					}
				}
			}
			
			@Override
			public void onFailed() {
				
			}
		});
		
		helper.doCheckUpdate();
		
	}
	
	private void resetInitFlag(){
		PreferenceUtils.writeBoolConfig(Constant.PreferKeys.KEY_HAS_DISPLAY_GUIDE_PAGE, false, this);
		PreferenceUtils.writeBoolConfig(Constant.PreferKeys.KEY_HAS_DISPLAY_SCRAWL, false, this);
		PreferenceUtils.writeIntConfig(Constant.PreferKeys.KEY_SHOW_UPDATE_COUNT, 0, this);
	}
	
	private void initGetui(){
		String cid = PreferenceUtils.readStrConfig(
				Constant.PreferKeys.KEY_GETUI_CLIENTID, getBaseContext(), "0");
		if (!isAuthroized()) {
			cid = "";
		}
		mWebAPi.postClientID(cid, null);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mApp.cachedVideoAttrs();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (isAuthroized()) {
			jumpActivity(intent);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		showUserAvatar(mApp.getOnlineUser());
		boolean isShowOrderHint = PreferenceUtils.readBoolConfig(Constant.PreferKeys.KEY_NOTIFY_ORDER, this);
		mViewOrderHint.setVisibility(isShowOrderHint ? View.VISIBLE : View.INVISIBLE);
		
		if (isShowOrderHint) {
			PreferenceUtils.writeBoolConfig(Constant.PreferKeys.KEY_NOTIFY_ORDER, false, this);
			bindOrderHintCount(mViewOrderHint);
			mTabViewAtteton.setImageResource(R.drawable.follow);
			mTabViewPerson.setImageResource(R.drawable.personal);
			mTabViewPlaza.setImageResource(R.drawable.plaza);
			mTabViewCustom.setImageResource(R.drawable.video_customize_active);
			switchFramgment(mFragmentCustom);
		}
		boolean isShowMsgHint = PreferenceUtils.readBoolConfig(Constant.PreferKeys.KEY_NOTIFY_MSG, this);				
		mViewMsgHint.setVisibility(isShowMsgHint ? View.VISIBLE : View.INVISIBLE);
		if (isShowMsgHint) {
			bindNotifyMsgHintCount(mViewMsgHint);
		}
		
		if(PreferenceUtils.readBoolConfig(Constant.PreferKeys.KEY_BACK_TO_SQUARE, this)){
			if(isAuthroized()){
				if(!(mCurrFragment instanceof SquareFragment)){
					mApp.notifyDataChanged(DataType.switch_square_tab);
				}
				refreshAllData();
				
			}else{
				mApp.notifyDataChanged(DataType.switch_square_tab);
			}
			PreferenceUtils.writeBoolConfig(Constant.PreferKeys.KEY_BACK_TO_SQUARE,false, this);
		}
	}

	private void refreshAllData(){
		mFragmentUserInfo = new UserInfoFragment();
		mFragmentAttention = new AttentionFragment();
		mFragmentCustom = new CustomFragment();
		initGetui();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, "onDestory");
		unregisterBroadcast();
		mApp.removeActivity(this);
		mApp.unRegisterDataListener(mDataSourceListener);
		// PreferenceUtils.RemoveStrConfig(Constant.PreferKeys.KEY_FIRST_LOGIN,
		// this);
	}

	@Override
	public void onBackPressed() {
		if ((SystemClock.elapsedRealtime() - exitTime) > 2000) {
			ToastUtil.showToast(mApp, R.string.exist_app_hint);
			exitTime = SystemClock.elapsedRealtime();
		} else {
 			quit();
		}
	}
	
	private void quit(){
		DownloadManager.getInstante(this).cancelAllTask();
		DownloadManager.free();
		
		clearRecordVideo();
		mApp.exit();
		finish();
	}
	
	private void clearRecordVideo(){
		List<VideoInfoPost> mVideoList = mApp.getFinalDb().findAll(VideoInfoPost.class);
		
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), CameraHelper.FOLDER_NAME);
		File[] allFile = mediaStorageDir.listFiles();
		if(allFile == null){
			return;
		}
		for(int k = 0 ; k < allFile.length ; k ++){
			File temp = allFile[k];
			String path = temp.getAbsolutePath();
			if(path.contains(DownloadManager.CUSTOM_DOWN_FLAG)){
				continue;
			}
			
			if(!path.contains("final")){
				temp.delete();
				continue;
			}
			boolean isExist = false;
			for(int i = 0 ; i < mVideoList.size() ; i ++){
				if(mVideoList.get(i).getVideoPath().equals(path)){
					isExist = true;
					break;
				}
			}
			
			if(!isExist){
				temp.delete();
			}
		}
		
	}
	
	private Handler mUIHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			switch(what){
			case 0:
				mMainLayout.removeView(mWelcomePage);
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
				break;
			}
		};
	};
	
	void createUI() {
		mMainLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.main, null);
		mBehindMenu = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.fragment_behindmenu, null);
			
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
		
		setContentView(mMainLayout);
		
		setBehindContentView(mBehindMenu);
		mViewOrderHint = mMainLayout.findViewById(R.id.tabVideoCustomHint);
		mViewMsgHint = mMainLayout.findViewById(R.id.tabPersonHint);

		View view = mBehindMenu.findViewById(R.id.viewGrpUserInfo);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				switchFramgment(mFragmentSquare);
				mTabViewPerson.performClick();
//				Intent intent = new Intent(getBaseContext(), UserHomeActivity.class);
//				startActivity(intent);
				getSlidingMenu().toggle();
			}
		});

		mTxtViewLoginState = (TextView) mBehindMenu.findViewById(R.id.txtViewLoginState);

		mImgViewAvata = (ImageView) mBehindMenu.findViewById(R.id.imgViewAvata);
		getSlidingMenu().setBehindOffset((int) (100 * getResources().getDisplayMetrics().density));

//		getSlidingMenu().setSlidingEnabled(isAuthroized());
		
		mFragmentSquare.setOnMenuListener(new OnMenuItemClickListner() {

			@Override
			public void onClick() {
				if (isAuthroized()) {
					getSlidingMenu().toggle();
				}
			}
		});

		ListView listView = (ListView) mBehindMenu.findViewById(R.id.listViewSlideMenu);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position < mSlideMenuAdapter.getCount()) {
					getSlidingMenu().toggle();
					mFragmentSquare.notifyTypeChange(mSlidingMenuIds[position]);
				} else {
					// Intent intent = new Intent(getBaseContext(),
					// CustomOrderActivity.class);
					// startActivity(intent);
					// getSlidingMenu().toggle();
				}

			}
		});
		listView.setAdapter(mSlideMenuAdapter);

		View viewCapture = mMainLayout.findViewById(R.id.btnCapture);
		viewCapture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isAuthroized()) {
					Intent intent = new Intent(getBaseContext(), VideoRecordActivity.class);
//					startActivityForResult(intent, REQUEST_CODE_CARMERA);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getBaseContext(), LoginActivity.class);
					startActivityForResult(intent, REQUEST_CODE_LOGIN);
				}
			}
		});

		mTabViewPlaza = (ImageView) mMainLayout.findViewById(R.id.tabPlaza);
		mTabViewPerson = (ImageView) mMainLayout.findViewById(R.id.tabPerson);
		mTabViewCustom = (ImageView) mMainLayout.findViewById(R.id.tabVideoCustom);
		mTabViewAtteton = (ImageView) mMainLayout.findViewById(R.id.tabVideoAt);

		mTabViewPerson.setOnClickListener(mOnClickListener);
		mTabViewAtteton.setOnClickListener(mOnClickListener);
		mTabViewPlaza.setOnClickListener(mOnClickListener);
		mTabViewCustom.setOnClickListener(mOnClickListener);

		mTabViewPlaza.setImageResource(R.drawable.plaza_active);
		
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			boolean isShowOrderHint = PreferenceUtils.readBoolConfig(Constant.PreferKeys.KEY_NOTIFY_ORDER,
//					getBaseContext());
//			if (isShowOrderHint && v == mTabViewCustom) {
//				mApp.notifyDataChanged(DataType.accpet_order);
//			}
			mViewOrderHint.setVisibility(View.INVISIBLE);
			if (v == mCurrTabView) {
				mCurrFragment.onRefreshData();
				return;
			}
			if(v != mTabViewPlaza){
				if (!isAuthroized()) {
					Intent intent = new Intent(getBaseContext(), LoginActivity.class);
					startActivityForResult(intent, REQUEST_CODE_LOGIN);
					return;
				}
			}

			mTabViewAtteton.setImageResource(R.drawable.follow);
			mTabViewCustom.setImageResource(R.drawable.video_customize);
			mTabViewPerson.setImageResource(R.drawable.personal);
			mTabViewPlaza.setImageResource(R.drawable.plaza);

			if (v == mTabViewAtteton) {
				mTabViewAtteton.setImageResource(R.drawable.follow_active);
				switchFramgment(mFragmentAttention);
				getSlidingMenu().setSlidingEnabled(false);
			} else if (v == mTabViewCustom) {
				switchFramgment(mFragmentCustom);
				mTabViewCustom.setImageResource(R.drawable.video_customize_active);
				getSlidingMenu().setSlidingEnabled(false);
			} else if (v == mTabViewPerson) {
				mTabViewPerson.setImageResource(R.drawable.personal_active);
				switchFramgment(mFragmentUserInfo);
				getSlidingMenu().setSlidingEnabled(false);
			} else if (v == mTabViewPlaza) {
				mTabViewPlaza.setImageResource(R.drawable.plaza_active);
				switchFramgment(mFragmentSquare);
//				getSlidingMenu().setSlidingEnabled(isAuthroized());
				getSlidingMenu().setSlidingEnabled(false);
			}
			mCurrTabView = v;
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

	private void showUserAvatar(UserInfo info) {
		if (info != null) {
			mTxtViewLoginState.setText(info.getName());
			mFinalBitmap.display(mImgViewAvata, info.getAvatarUrl());
		}
	}

	void getUserInfo() {
		mWebAPi.getUserInfo(-1, new IResponse<UserInfo>() {

			@Override
			public void onSuccessed(UserInfo result) {

				mApp.setOnlineUser(result);
				showUserAvatar(result);
				mApp.notifyDataChanged(DataType.userinfo);
			}

			@Override
			public void onFailed(String code, String errMsg) {
				ToastUtil.showToast(mApp, errMsg);
			}

			@Override
			public UserInfo asObject(String rspStr) {
				UserInfo info = GsonUtil.jsonToObject(UserInfo.class, rspStr);
				return info;
			}
		});
	}

	private void showChoiceDlg(int msgResId) {
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

	/**
	 * According data including in a intent, we knows that which activity we should start.
	 * @param intent
	 */
	private void jumpActivity(Intent intent) {
		String data = intent.getStringExtra(BaseActivity.EXTRA_PUSH);
		if (!TextUtils.isEmpty(data)) {
			PushInfo info = GsonUtil.jsonToObject(PushInfo.class, data);
			if (PushInfo.TYPE_ORDER.equals(info.getType())) {
				mTabViewCustom.performClick();
			} else {
				intent = new Intent(this, NotificationCenterActivity.class);
				startActivity(intent);
			}
		} else {
			boolean isApplyAnchor = PreferenceUtils.readBoolConfig(PreferKeys.KEY_START_APPLY_ANCHOR, this);
			if (isApplyAnchor) {
				PreferenceUtils.writeBoolConfig(PreferKeys.KEY_START_APPLY_ANCHOR, false, this);
				intent = new Intent(this, AnchorApplyActivity.class);
				startActivity(intent);
			}
		}

	}

}
