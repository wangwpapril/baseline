package com.intrepid.travel.ui.fragment;

import java.util.List;

import com.intrepid.travel.MyApplication;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;


public class BaseFragment extends Fragment {

	protected final static int REQUEST_CODE = 300;
	
	protected MyApplication mApp;
	
	protected Activity mActivity;
	
	protected Handler mHandler = new Handler();
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		mApp = (MyApplication) getActivity().getApplicationContext();
	}	

	
	
	/**
	 * Refresh real business data.
	 */
	public void onRefreshData() {
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
}
