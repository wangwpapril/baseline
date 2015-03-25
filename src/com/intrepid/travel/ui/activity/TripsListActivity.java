package com.intrepid.travel.ui.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.intrepid.travel.MyApplication;
import com.intrepid.travel.R;
import com.intrepid.travel.Enums.ConnMethod;
import com.intrepid.travel.Enums.PreferenceKeys;
import com.intrepid.travel.models.Destination;
import com.intrepid.travel.models.User;
import com.intrepid.travel.net.ControlerContentTask;
import com.intrepid.travel.net.IControlerContentCallback;
import com.intrepid.travel.store.beans.UserTable;
import com.intrepid.travel.ui.adapter.TripsListAdapter;
import com.intrepid.travel.utils.SharedPreferenceUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class TripsListActivity extends BaseActivity {

	protected static final String TAG = "TripsListActivity";
	private ListView listView;
	private List<Destination> datas;
	private TripsListAdapter tripsListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.trip_list);
		initView();
		
		if(datas == null) {
			getTripList();
		}else{
			tripsListAdapter = new TripsListAdapter(
					datas, context);
			listView.setAdapter(tripsListAdapter);
		}
	
	}
	

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void initView(){
		super.initTitleView();
		listView = (ListView) findViewById(R.id.trip_list);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
	//			Intent i = new Intent();
	//			i.setClass(context, WelcomeActivity.class);
	//			i.putExtra(IntentKeys.KEY_CATEGROY_SUBS, datas.get(arg2));
	//			context.startActivity(i);
			}
		});
	}
	
	private void getTripList(){

		IControlerContentCallback icc = new IControlerContentCallback() {
			public void handleSuccess(String content){

				JSONObject des;
				try {
					des = new JSONObject(content);
					JSONArray array = des.getJSONArray("destinations");
					int len = array.length();
					datas = new ArrayList<Destination>(len);
					for (int i =0;i < len; i++){
						datas.add(new Destination(array.getJSONObject(i)));
					}
					
					tripsListAdapter = new TripsListAdapter(
							datas, context);
					listView.setAdapter(tripsListAdapter);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

			public void handleError(Exception e){
//				CommonMethod.handleException(context,e);
//				showAlertDialog(getResources().getString(
	//					R.string.login_title_name), "Invalid login credentials");
				return;

			}
		};
		
		ControlerContentTask cct = new ControlerContentTask(
				"https://api.intrepid247.com/v1/destinations?short_list=true&token=ce6f284088d8c6bf88802f51f6d49776", icc,
				ConnMethod.GET,false);
		String ss = null;
		cct.execute(ss);

	}

	@Override
	protected void initTitle(){
		ivTitleBack.setVisibility(View.VISIBLE);
		ivTitleBack.setOnClickListener(this);
		tvTitleName.setText("Trips");
//		ivTitleRight.setVisibility(View.VISIBLE);
//		ivTitleBack.setImageResource(R.drawable.title_left_search);
	}

	@Override
	public void onClick(View v){
		if(v == ivTitleBack){
			finish();
		}else if(v == ivTitleRight){
			
		}
	}

}
