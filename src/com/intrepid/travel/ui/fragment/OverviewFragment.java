package com.intrepid.travel.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.intrepid.travel.R;
import com.intrepid.travel.ui.control.TabNaviBar;
import com.intrepid.travel.ui.control.TabNaviBar.OnTabChangedListener;
import com.intrepid.travel.ui.control.TabNaviBar.TabAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



public class OverviewFragment extends BaseFragment {
	
	private final static String TAG = OverviewFragment.class.getSimpleName();
	
	private LayoutInflater mLayoutInflater;

	private ViewPager mViewPager;

	private TabNaviBar mTabNaviBar;

	private List<String> mOrderList = new ArrayList<String>();
	private List<String> mAcceptOrderList = new ArrayList<String>();
	

	private MyOrderAdapter mOrderAdapter;
	
	private MyOrderAdapter mAcceptOrderAdapter;

	private ListView mAcceptOrderListView;
	private ListView mOrderListView;

	private int mCurrAcceptIndex = 0;
	private int mCurrOrderIndex = 0;
	
//	private UserInfo mUserinfo;
	
	private View mRootView;
	
	private TextView mBtnCustomVideo;
	
	private TextView mBtnApplyAnchor;
	
	private TextView mTxtViewEmptyAcceptComment;
		
//	private PullToRefreshScrollView mViewGrpAcceptOrder;
	
	//private PullToRefreshScrollView mViewGrpEmptyOrder;
	
/*	private IDataSourceListener<DataType> mDataSourceListtener = new IDataSourceListener<DataType>() {

		@Override
		public void onChange(DataType type) {
			
			if (mTabNaviBar == null) {
				return;
			}
			
			switch (type) {
			case order:
				mTabNaviBar.move(0);
				onRefreshData();
				break;
			case accpet_order:
			case video_custom_posted:	
				mTabNaviBar.move(1);
				onRefreshData();
				PreferenceUtils.writeBoolConfig(Constant.PreferKeys.KEY_NOTIFY_ORDER, false, mActivity);				
				break;				
			default:
				break;
			}
		}
	};
*/	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		mUserinfo = mApp.getOnlineUser();
		for (int i=0;i<10;i++){
		mOrderList.add("test1");
		mOrderList.add("test2");
		}
		mAcceptOrderList.add("test3");
		mAcceptOrderList.add("test4");
		mOrderAdapter = new MyOrderAdapter(mOrderList, 0);
		mAcceptOrderAdapter = new MyOrderAdapter(mAcceptOrderList, 1);

//		mApp.registerDataListener(DataType.order, mDataSourceListtener);
	//	mApp.registerDataListener(DataType.accpet_order, mDataSourceListtener);
		//mApp.registerDataListener(DataType.video_custom_posted, mDataSourceListtener);
	}
		
	
	
	@Override
	public void onStop() {
		super.onStop();

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
//		mApp.unRegisterDataListener(mDataSourceListtener);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mLayoutInflater = inflater;
		if (mRootView != null) {
			return mRootView;
		}
		mRootView = inflater.inflate(R.layout.fragment_custom, null);
//		mRootView = LayoutInflater.from(this).inflate(R.layout.main, null);
		
		TextView txtView = (TextView) mRootView.findViewById(R.id.txtViewTitleCenter);
		txtView.setText("testing");
				
		LinearLayout leftLayout = (LinearLayout) mRootView.findViewById(R.id.btnTitleLeftLayout);
		leftLayout.setVisibility(View.GONE);
		
/*		ImageView btnTitleRight = (ImageView) mRootView.findViewById(R.id.btnTitleRight);
		if (mUserinfo.isSuperAnchor()) {
			btnTitleRight.setVisibility(View.GONE);
		} else {
			btnTitleRight.setVisibility(View.VISIBLE);
		}
		
		btnTitleRight.setImageResource(R.drawable.video_customize_bg);
		
		btnTitleRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CustomOrderCreateActivity.class);
				startActivity(intent);			
			}
		});
*/		
		mViewPager = (ViewPager) mRootView.findViewById(R.id.viewpager);

		mViewPager.setAdapter(new MyPagerAdapter());
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mTabNaviBar.move(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		mTabNaviBar = (TabNaviBar) mRootView.findViewById(R.id.tabNaviBar1);
		mTabNaviBar.setOnTabChangedListener(new OnTabChangedListener() {

			@Override
			public void onChange(int index) {
				mViewPager.setCurrentItem(index, true);
			}
		});

//		final String[] tabs = getResources().getStringArray(R.array.custom_tabs);
		final String[] tabs = {"test1", "test2"};
		
		mTabNaviBar.setAdapter(new TabAdapter() {

			@Override
			public List<View> getTabView() {
				List<View> views = new ArrayList<View>();
				for (String str: tabs){
					View item = LayoutInflater.from(mApp).inflate(R.layout.tab_item, null);
					views.add(item);
					TextView textView = (TextView) item.findViewById(R.id.btnTab);
					textView.setText(str);
				}											
				return views;
			}

			@Override
			public void viewStateChange(View view, boolean isSelect) {
				final TextView textView = (TextView) view.findViewById(R.id.btnTab);
				int color = isSelect ? getResources().getColor(R.color.tab_text_select) : getResources().getColor(
						R.color.tab_text);
				textView.setTextColor(color);
			}
		});

		return mRootView;
	}

/*	void getAcceptOrderList(final int pageIndex) {
		mWebApi.getCustomOrders(1, pageIndex, Constant.PAGE_COUNT, new IResponse<List<CustomOrder>>() {

			@Override
			public void onSuccessed(List<CustomOrder> result) {
				if (mActivity.isFinishing()){
					return;
				}
				
				if (pageIndex == 0) {
					mAcceptOrderList.clear();										
				} else {
					if (result.isEmpty()) {
						ToastUtil.showToast(mApp, R.string.no_more_data);
					}
					mCurrAcceptIndex++;
				}
				mAcceptOrderList.addAll(result);
				mAcceptOrderAdapter.notifyDataSetChanged();				
				if (pageIndex == 0) {
					mRelativeDateManager.updateDate(TAG + "video");
					mAcceptOrderListView.setLastUpdatedLabel(mRelativeDateManager.getDate(TAG + "video"));
					mAcceptOrderListView.onPullDownRefreshComplete();
					
					mViewGrpAcceptOrder.setLastUpdatedLabel(mRelativeDateManager.getDate(TAG + "video"));					
					mViewGrpAcceptOrder.onPullDownRefreshComplete();
										
					mAcceptOrderListView.setVisibility(mAcceptOrderList.isEmpty() ? View.GONE : View.VISIBLE);
										
					
					
				} else {
					mAcceptOrderListView.onPullUpRefreshComplete();
				}
			}

			@Override
			public void onFailed(String code, String errMsg) {
				ToastUtil.showToast(getActivity(), errMsg);
				if (pageIndex == 0) {
					mAcceptOrderListView.onPullDownRefreshComplete();
				} else {
					mAcceptOrderListView.onPullUpRefreshComplete();
				}
				mAcceptOrderListView.setVisibility(mAcceptOrderList.isEmpty() ? View.GONE : View.VISIBLE);
			}

			@Override
			public List<CustomOrder> asObject(String rspStr) {
				if (!TextUtils.isEmpty(rspStr)) {
					TypeToken<List<CustomOrder>> type = new TypeToken<List<CustomOrder>>() {
					};
					List<CustomOrder> result = GsonUtil.jsonToList(type.getType(), rspStr);

					return result;
				}
				return new ArrayList<CustomOrder>();
			}
		});
	}
	
	void getOrderList(final int pageIndex) {
		mWebApi.getCustomOrders(0, pageIndex, Constant.PAGE_COUNT, new IResponse<List<CustomOrder>>() {

			@Override
			public void onSuccessed(List<CustomOrder> result) {
				if (mActivity.isFinishing()) {
					return;
				}
				if (pageIndex == 0) {
					mOrderList.clear();
					boolean isShowOrderHint = PreferenceUtils.readBoolConfig(PreferKeys.KEY_NOTIFY_ORDER, mActivity);
					if (isShowOrderHint) {
						PreferenceUtils.writeBoolConfig(PreferKeys.KEY_NOTIFY_ORDER, false, mActivity);
					}
				} else {
					mCurrOrderIndex++;
					if (result.isEmpty()) {
						ToastUtil.showToast(mApp, R.string.no_more_data);
					}
				}
				mOrderList.addAll(result);
				mOrderAdapter.notifyDataSetChanged();

				if (pageIndex == 0) {
					mOrderListView.setVisibility(mOrderList.isEmpty() ? View.GONE : View.VISIBLE);
					if (mOrderList.isEmpty() && !mUserinfo.isSuperAnchor()) {
						mBtnCustomVideo.setVisibility(View.VISIBLE);
					} else {
						mBtnCustomVideo.setVisibility(View.GONE);
					}

					mRelativeDateManager.updateDate(TAG + "order");
					mOrderListView.setLastUpdatedLabel(mRelativeDateManager.getDate(TAG + "order"));
					mViewGrpEmptyOrder.setLastUpdatedLabel(mRelativeDateManager.getDate(TAG + "order"));
					mOrderListView.onPullDownRefreshComplete();
					mViewGrpEmptyOrder.onPullDownRefreshComplete();
				} else {
					mOrderListView.onPullUpRefreshComplete();
				}

			}

			@Override
			public void onFailed(String code, String errMsg) {
				if (mActivity.isFinishing()) {
					return;
				}
				ToastUtil.showToast(mActivity, errMsg);
				if (pageIndex == 0) {
					mOrderListView.onPullDownRefreshComplete();
					mViewGrpEmptyOrder.onPullDownRefreshComplete();
				} else {
					mOrderListView.onPullUpRefreshComplete();
				}

				mOrderListView.setVisibility(mOrderList.isEmpty() ? View.GONE : View.VISIBLE);
				if (mOrderList.isEmpty() && !mUserinfo.isSuperAnchor()) {
					mBtnCustomVideo.setVisibility(View.VISIBLE);
				} else {
					mBtnCustomVideo.setVisibility(View.GONE);
				}
			}

			@Override
			public List<CustomOrder> asObject(String rspStr) {
				if (!TextUtils.isEmpty(rspStr)) {
					TypeToken<List<CustomOrder>> type = new TypeToken<List<CustomOrder>>() {
					};
					return GsonUtil.jsonToList(type.getType(), rspStr);
				}
				return new ArrayList<CustomOrder>();
			}
		});
	}
*/
	public interface OnMenuItemClickListner {
		void onClick();
	}

	
	
	@Override
	public void onStart() {
		super.onStart();
/*		int orderType = PreferenceUtils.readIntConfig(Constant.PreferKeys.KEY_NOTIFY_ORDER_TYPE, mActivity, -1);
		if (orderType == PushInfo.TYPE_ORDER_FANS){		*/	
			mTabNaviBar.move(1);
			onRefreshData();
/*		}else if(orderType == PushInfo.TYPE_ORDER_MINE){
			mTabNaviBar.move(0);
			onRefreshData();
		}
		PreferenceUtils.writeIntConfig(Constant.PreferKeys.KEY_NOTIFY_ORDER_TYPE, -1, mActivity);
		mAcceptOrderListView.setLastUpdatedLabel(mRelativeDateManager.getDate(TAG + "video"));
		mOrderListView.setLastUpdatedLabel(mRelativeDateManager.getDate(TAG + "order"));		
		mBtnApplyAnchor.setVisibility(mApp.getOnlineUser().isAnchor() ? View.GONE : View.VISIBLE);
				
		if (mApp.getOnlineUser().isAnchor()) {
			mTxtViewEmptyAcceptComment.setText(R.string.empty_accept_order_comment_is_anchor);
		} else {
			mTxtViewEmptyAcceptComment.setText(R.string.empty_accept_order_comment);
		}	*/			
	}
	
	@Override
	public void onRefreshData() {
		super.onRefreshData();
		
		if (mViewPager == null) {
			return;
		}
		if (mViewPager.getCurrentItem() == 1 && mAcceptOrderListView != null) {
			mAcceptOrderListView.setVisibility(View.VISIBLE);
//			mViewGrpAcceptOrder.setVisibility(View.GONE);
//			mAcceptOrderListView.doPullRefreshing(true);
		}
		
		if (mViewPager.getCurrentItem() == 0 && mOrderListView != null) {
//			mViewGrpEmptyOrder.setVisibility(View.GONE);
			mOrderListView.setVisibility(View.VISIBLE);
//			mOrderListView.doPullRefreshing(true);
		}
	}

	class MyPagerAdapter extends PagerAdapter {
		private View orderView;		
		private View acceptOrderView;
        
		MyPagerAdapter() {			
			acceptOrderView = mLayoutInflater.inflate(R.layout.adapter_viewpager_my_custom_order, null);
						
/*			mBtnApplyAnchor = (TextView) acceptOrderView.findViewById(R.id.btnCustomVideo);
			mBtnApplyAnchor.setVisibility(mApp.getOnlineUser().isAnchor() ? View.GONE : View.VISIBLE);
			mBtnApplyAnchor.setText(R.string.menu_apply_anchor);
			mBtnApplyAnchor.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), AnchorApplyActivity.class);
					startActivity(intent);					
				}
			});
			*/
			mAcceptOrderListView = (ListView) acceptOrderView.findViewById(R.id.pullToRefreshListViewCustom);			
			
/*			mAcceptOrderListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(getActivity(), CustomOrderStateActivity.class);
					CustomOrder item = mAcceptOrderAdapter.getItem(position);
					intent.putExtra(CustomOrderStateActivity.EXTRA, item);
					intent.putExtra(CustomOrderStateActivity.EXTRA_ORDER_TYPE,
							CustomOrderStateActivity.TYPE_ACCEPT_ORDER);
					startActivity(intent);

				}
			});
			
			mAcceptOrderListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

				@Override
				public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
					mCurrAcceptIndex = 0;
					getAcceptOrderList(mCurrAcceptIndex);
					
				}

				@Override
				public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {					
					getAcceptOrderList(mCurrAcceptIndex + 1);
					
				}
			});
*/			
/*			mViewGrpAcceptOrder = (PullToRefreshScrollView) acceptOrderView.findViewById(R.id.viewGrpEmpty);

			mViewGrpAcceptOrder.setOnRefreshListener(new OnRefreshListener<ViewGroup>() {

				@Override
				public void onPullDownToRefresh(PullToRefreshBase<ViewGroup> refreshView) {
					mCurrAcceptIndex = 0;
					getAcceptOrderList(mCurrAcceptIndex);					
				}

				@Override
				public void onPullUpToRefresh(PullToRefreshBase<ViewGroup> refreshView) {
					// TODO Auto-generated method stub
					
				}
			});
					
			TextView txtView = (TextView) mViewGrpAcceptOrder.findViewById(R.id.txtViewEmptyTitle);
			txtView.setText(R.string.empty_accept_order_title);
			mTxtViewEmptyAcceptComment = (TextView) acceptOrderView.findViewById(R.id.txtViewEmptyContent);
			mTxtViewEmptyAcceptComment.setText(R.string.empty_accept_order_comment);
			
			mTxtViewEmptyAcceptComment.setVisibility(mApp.getOnlineUser().isAnchor() ? View.GONE : View.VISIBLE);
*/		
			orderView = mLayoutInflater.inflate(R.layout.adapter_viewpager_my_custom_order, null);
/*			mViewGrpEmptyOrder = (PullToRefreshScrollView) orderView.findViewById(R.id.viewGrpEmpty);	
			mViewGrpEmptyOrder.setOnRefreshListener(new OnRefreshListener<ViewGroup>() {

				@Override
				public void onPullDownToRefresh(PullToRefreshBase<ViewGroup> refreshView) {
					mCurrOrderIndex = 0;
					getOrderList(mCurrOrderIndex);				
				}

				@Override
				public void onPullUpToRefresh(PullToRefreshBase<ViewGroup> refreshView) {
					// TODO Auto-generated method stub
					
				}
			});
*/			
/*			txtView = (TextView) orderView.findViewById(R.id.txtViewEmptyTitle);
			txtView.setText(R.string.empty_order_title);
			txtView = (TextView) orderView.findViewById(R.id.txtViewEmptyContent);
			txtView.setText(R.string.empty_order_comment);			
								
			mBtnCustomVideo = (TextView) orderView.findViewById(R.id.btnCustomVideo);
			mBtnCustomVideo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), CustomOrderCreateActivity.class);
					startActivity(intent);
				}
			});
*/			
			mOrderListView = (ListView) orderView.findViewById(R.id.pullToRefreshListViewCustom);

/*			mOrderListView.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					
					CustomOrder item = mOrderAdapter.getItem(position);
					if(item.getStatus() == CustomOrder.STATUS_NOT_PAY){
						Intent intent = new Intent(getActivity(), CustomOrderCreateActivity.class);
						intent.putExtra(CustomOrderCreateActivity.PRE_CUSTOM_ORDER, item);
						startActivity(intent);
					}else{
						Intent intent = new Intent(getActivity(), CustomOrderStateActivity.class);
						intent.putExtra(CustomOrderStateActivity.EXTRA, item);
						intent.putExtra(CustomOrderStateActivity.EXTRA_ORDER_TYPE, CustomOrderStateActivity.TYPE_MY_ORDER);
						startActivity(intent);	
					}

				}
			});

			mOrderListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

				@Override
				public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
					mCurrOrderIndex = 0;
					getOrderList(mCurrOrderIndex);

				}

				@Override
				public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
					getOrderList(mCurrOrderIndex + 1);

				}
			});
*/
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View result = position == 0 ? orderView : acceptOrderView;
			container.addView(result);
			if (position == 0) {
				mOrderListView.setAdapter(mOrderAdapter);								
	//			mOrderListView.getRefreshableView().setAdapter(mOrderAdapter);								
//				mOrderListView.doPullRefreshing(true);				
			} else {								
				mAcceptOrderListView.setAdapter(mAcceptOrderAdapter);
	//			mAcceptOrderListView.getRefreshableView().setAdapter(mAcceptOrderAdapter);
//				mAcceptOrderListView.doPullRefreshing(true);							
			}

			return result;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
	

	class MyOrderAdapter extends BaseAdapter {
		
		private List<String> mOrderList;
		
		private int orderType ;
		
		
		MyOrderAdapter(List<String> orderList, int orderType) {
			mOrderList = orderList;
			this.orderType = orderType;
		}

//		private String[] stateDscList = getResources().getStringArray(R.array.order_state_dsc);

		@Override
		public int getCount() {
			return mOrderList.size();
		}

		@Override
		public Object getItem(int position) {
			return mOrderList.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.trip_list_item, null);
			}						
			
			Object order = getItem(position);
			
			if (order == null){
				return convertView;
			}
			
			ImageView imgViewAvatar = (ImageView) convertView.findViewById(R.id.category_top_item_iv);
			  
/*			View viewGrpPrice= convertView.findViewById(R.id.viewGrpPriceIcon);
			if (orderType == 1) {
				viewGrpPrice.setVisibility(View.VISIBLE);
				imgViewAvatar.setVisibility(View.GONE);
			} else {
				viewGrpPrice.setVisibility(View.GONE);
				imgViewAvatar.setVisibility(View.VISIBLE);
				if(order.getUserInfo() != null)
					mFinalBitmap.display(imgViewAvatar, order.getUserInfo().getAvatarUrl());
			}
*/									
			TextView txtView = (TextView) convertView.findViewById(R.id.category_top_item_name);
//			if (order.getTitle() != null) {
				txtView.setText("test");
	//		}			
/*			txtView = (TextView) convertView.findViewById(R.id.txtViewOrderPrice);
			txtView.setText((int)order.getPrice() + getString(R.string.price_unit));
			
			txtView = (TextView) convertView.findViewById(R.id.txtViewOrderPriceIcon);
			txtView.setText((int)order.getPrice()+"");
			
			txtView = (TextView) convertView.findViewById(R.id.txtViewOrderState);
			
			if (order.getStatus() < stateDscList.length) {
				txtView.setText(stateDscList[order.getStatus()]);
				if (order.getStatus() == CustomOrder.STATUS_MAKE){
					long leftTime = order.getDeliveryTime() - System.currentTimeMillis();
					if (leftTime < 0) {
						txtView.setText(R.string.over_time);
					}
				}				
			}else{
				txtView.setText(getResources().getString(R.string.order_state_error));
			}			
			txtView = (TextView) convertView.findViewById(R.id.txtViewTime);
			txtView.setText(StringUtil.getDateTime(order.getCreateTime()));

			UserInfo user = order.getUserInfo();
			if (user != null) {
//				ImageView imgView = (ImageView) convertView.findViewById(R.id.imgViewUserPortrait);
//				mFinalBitmap.display(imgView, user.getAvatarUrl());
				txtView = (TextView) convertView.findViewById(R.id.txtViewUserName);
				txtView.setText(user.getName());
			}			
*/			return convertView;
		}

	}
}