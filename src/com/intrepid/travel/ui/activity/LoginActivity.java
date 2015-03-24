package com.intrepid.travel.ui.activity;



import org.json.JSONException;
import org.json.JSONObject;

import com.google.mitijson.Gson;
import com.intrepid.travel.Enums.ConnMethod;
import com.intrepid.travel.Enums.PreferenceKeys;
import com.intrepid.travel.Enums.PreferenceType;
import com.intrepid.travel.MyApplication;
import com.intrepid.travel.R;
import com.intrepid.travel.models.User;
import com.intrepid.travel.net.ControlerContentTask;
import com.intrepid.travel.net.IControlerContentCallback;
import com.intrepid.travel.net.UserDemo;
import com.intrepid.travel.net.UserInfoDemo;
import com.intrepid.travel.store.beans.UserTable;
import com.intrepid.travel.utils.SharedPreferenceUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class LoginActivity extends BaseActivity {

	protected static final String TAG = "LoginActivity";
	
	private Button imBtnSignIn;
	private EditText editTextEmail, editTextPassword;
	private TextView signUp;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login_layout);
		MyApplication.getInstance().addActivity(this);

		initView();
	}

	private void initView(){
		super.initTitleView();
		imBtnSignIn = (Button) findViewById(R.id.butSignIn);
		editTextEmail = (EditText) findViewById(R.id.signinEmailEditText);
		editTextPassword = (EditText) findViewById(R.id.signinPasswordEditText);
		signUp = (TextView) findViewById(R.id.sign_up);
		
		imBtnSignIn.setOnClickListener(this);
		editTextPassword.setTransformationMethod(PasswordTransformationMethod
				.getInstance());


	}

	@Override
	protected void initTitle(){
		tvTitleName.setText(R.string.login_title_name);
		ivTitleBack.setVisibility(View.VISIBLE);
//		tvTitleRight.setVisibility(View.VISIBLE);
//		tvTitleRight.setText(R.string.login_title_register);
//		tvTitleRight.setOnClickListener(this);
		ivTitleBack.setOnClickListener(this);
		// TODO:btnTitleBack.setImageResource(resId);
		// TODO:ivTitleRight.setImageResource(resId);
	}
	

	@Override
	public void onClick(View v){
		if (v == imBtnSignIn) {			

			String email = editTextEmail.getText().toString();
			String password = editTextPassword.getText().toString();
			if (TextUtils.isEmpty(email)) {
				showAlertDialog(getResources().getString(
						R.string.login_title_name), getResources()
						.getString(R.string.login_email_input_error));
				return;
			}

			if (TextUtils.isEmpty(password)) {
				showAlertDialog(getResources().getString(
						R.string.login_title_name), getResources()
						.getString(R.string.login_password_null));
				return;
			}
			
			IControlerContentCallback icc = new IControlerContentCallback() {
				public void handleSuccess(String content){

					JSONObject jsonObj = null, userObj = null;
					User user = null;
					
					try {
						jsonObj = new JSONObject(content);
						userObj = jsonObj.getJSONObject("user");
						user = new User(userObj);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
		            
		            UserTable.getInstance().saveUser(user);
		            User ww = null;
		            ww = UserTable.getInstance().getUser(user.id);
		            
		            
		            MyApplication.setLoginStatus(true);
		            
		    		SharedPreferenceUtil.setString(PreferenceKeys.userId.toString(), user.id);
		    		SharedPreferenceUtil.setString(PreferenceKeys.token.toString(), user.token);
		    		SharedPreferenceUtil.setBoolean(getApplicationContext(), PreferenceKeys.loginStatus.toString(), true);
		            
					
/*					try {
//						CommonMethod.handleUserInfo(content);
//						openHome();
//						CommonMethod.sendBroadcastOfLogin(context);
	//					setResult(RequestAndResultCode.RESULT_CODE_LOGIN_SUCCESS);
						context.finish();
//					} catch (KnownException ke) {
						// if(ke.errorCode.equals("11")){//忘记密码错误码与已经存在此账号一致，需要确认
						//
						// }
//						CommonMethod.handleException(context,ke.errorDesc);
					} catch (Exception e) {
						e.printStackTrace();
//						CommonMethod.handleException(context,e);
					}*/
				}

				public void handleError(Exception e){
//					CommonMethod.handleException(context,e);
					showAlertDialog(getResources().getString(
							R.string.login_title_name), "Invalid login credentials");
					return;

				}
			};
			
			ControlerContentTask cct = new ControlerContentTask(
					"https://api.intrepid247.com/v1/users/login", icc,
//					"https://api.intrepid247.com/v1/destinations?short_list=true&token=ce6f284088d8c6bf88802f51f6d49776", icc,
					ConnMethod.POST,false);

/*			UserInfoDemo demo = new UserInfoDemo();
			UserDemo user = new UserDemo();
			user.setEmail("wwang@peakcontact.com");
			user.setPsw("12345678");
			demo.setUser(user);
			Gson gson = new Gson();
			String parasString = gson.toJson(demo);
*/			
/*				JSONObject user1 = new JSONObject();
				try {
					user1.put("email", "wwang@peakcontact.com");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					user1.put("password", "12345678");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JSONObject jo = new JSONObject();
				try {
					jo.put("user", user1);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				String parasString = jo.toString();
				cct.execute(parasString);
*/				
			JSONObject user = new JSONObject();
			try {
				user.put("email", email);
				user.put("password", password);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			JSONObject login = new JSONObject();
			try {
				login.put("user", user);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			cct.execute(login.toString());

/*			HttpClient httpClient = new HttpClient();
			PostParameter[] postParams = null;
		
//			String JSONResult = null;
			String JSONResult = httpClient.post(parasString, "https://api.intrepid247.com/v1/users/login", 6000);	
			
            JSONObject jsonObj = new JSONObject(JSONResult);	
            JSONObject userObj = jsonObj.getJSONObject("user");
            User1 user1 = new User1(userObj);
            String userid = user1.id;
//            user1.id = "1160591404";
            UserTable1.getInstance().saveUser(user1);
            User1 ww = null;
            ww = UserTable1.getInstance().getUser1(userid);
*/

		} else if (v == signUp) {
			

	} else if (v == ivTitleBack) {
		onBackPressed();
//		setResult(RequestAndResultCode.RESULT_CODE_LOGIN_FAIL);
	}

	
	}
	
	private void showAlertDialog(String strTitle, String strMessage) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(strMessage);
		builder.setTitle(strTitle);
		builder.setPositiveButton(R.string.confirm_button,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		AlertDialog alertDialog = builder.create();

		alertDialog.setCancelable(false);

		alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return false; 
				}
			}
		});

		alertDialog.show();
	}

}
