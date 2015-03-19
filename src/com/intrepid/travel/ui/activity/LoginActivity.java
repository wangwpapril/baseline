package com.intrepid.travel.ui.activity;


import com.intrepid.travel.MyApplication;
import com.intrepid.travel.R;

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
