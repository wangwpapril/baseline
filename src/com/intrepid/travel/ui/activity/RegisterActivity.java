package com.intrepid.travel.ui.activity;

import org.json.JSONObject;

import com.intrepid.travel.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;




public class RegisterActivity extends BaseActivity {

	protected static final String TAG = "ActivityRegister";
	private EditText etPhone;
	private EditText etPassword;
	private EditText etPasswordConfirm;
	private EditText etVerifycode;
	private Button btnObtainVerifycode;
	private Button btnRegister;
	private boolean isResetPwd = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.signup_layout);
		isResetPwd = getIntent().getBooleanExtra(IntentKeys.KEY_IS_RESETPWD,
				false);
		initView();
	}

	private void initTimer() {
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
	}

	private void initView() {
		super.initTitleView();
		etPhone = (EditText) findViewById(R.id.register_phone);
		etPassword = (EditText) findViewById(R.id.register_password);
		etPasswordConfirm = (EditText) findViewById(R.id.register_password_confirm);
		etVerifycode = (EditText) findViewById(R.id.register_verifycode);
		btnObtainVerifycode = (Button) findViewById(R.id.register_obtain_verifycode);
		btnRegister = (Button) findViewById(R.id.register_btn_confirm);
		btnObtainVerifycode.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		if (isResetPwd) {
			btnRegister.setText("重置密码");
		}
		initTimer();
	}

	@Override
	protected void initTitle() {
		if (isResetPwd) {
			tvTitleName.setText("重置密码");
		} else {
			tvTitleName.setText(R.string.register_title_name);
		}
		ivTitleBack.setVisibility(View.VISIBLE);
		tvTitleRight.setVisibility(View.VISIBLE);
		tvTitleRight.setText(R.string.register_title_login);
		tvTitleRight.setOnClickListener(this);
		ivTitleBack.setOnClickListener(this);
		// TODO:btnTitleBack.setImageResource(resId);
		// TODO:ivTitleRight.setImageResource(resId);

	}

	@Override
	public void onClick(View v) {
		if (v == btnObtainVerifycode) {
			String phone = etPhone.getText().toString();
			if (TextUtils.isEmpty(phone) || !CommonUtil.isPhoneNo(phone)) {
				ToastUtil.show(context, R.string.register_phone_input_error,
						true);
				return;
			}
			IControlerContentCallback icc = new IControlerContentCallback() {
				public void handleSuccess(String content) {
					try {
						String result = CommonMethod.handleContent(content);
						// JSONObject jo = JsonUtil.getJsonObject(result);
						// String msg = jo.getString("message");
						// String code = msg.substring(msg.indexOf("：") + 1,
						// msg.indexOf("：") + 7);
						// etVerifycode.setText(code);//
						// TODO：测试使用，正式环境应该是给用户发短信，用户自己输入的
						time.start();// 开始计时
						ToastUtil.show(context,
								R.string.register_verifycode_sent, true);
					} catch (Exception e) {
						e.printStackTrace();
						CommonMethod.handleException(context, e);
					}
				}

				public void handleError(Exception e) {
					handleError(e);
				}
			};

			ControlerContentTask cct = new ControlerContentTask(
					RequestUtil.getRequestUrl(URL_SUB.REGISTER_VERIFYCODE),
					icc, ConnMethod.POST, false);
			// HashMap<String, String> params = new HashMap<String, String>();
			// params.put(RequestSmsCode.KEY_PHONE, phone);
			RequestSmsCode rmc = new RequestSmsCode();
			rmc.phone = phone;
			cct.execute(rmc);

		} else if (v == btnRegister) {
			String phone = etPhone.getText().toString();
			String password = etPassword.getText().toString();
			String passwordConfirm = etPasswordConfirm.getText().toString();
			String verifycode = etVerifycode.getText().toString();
			if (TextUtils.isEmpty(phone) || !CommonUtil.isPhoneNo(phone)) {
				ToastUtil.show(context, R.string.register_phone_input_error,
						true);
				return;
			}
			if (TextUtils.isEmpty(password)) {
				ToastUtil.show(context, R.string.register_password_null, true);
				return;
			}
			if (!password.equals(passwordConfirm)) {
				ToastUtil.show(context,
						R.string.register_tow_password_notequal, true);
				return;
			}

			if (TextUtils.isEmpty(verifycode) || verifycode.length() != 6) {
				ToastUtil.show(context, R.string.register_verify_error, true);
				return;
			}

			IControlerContentCallback icc = new IControlerContentCallback() {
				public void handleSuccess(String content) {
					try {
						CommonMethod.handleUserInfo(content);
						openHome();
						context.finish();
					} catch (KnownException ke) {
						// if(ke.errorCode.equals("11")){//忘记密码错误码与已经存在此账号一致，需要确认
						//
						// }
						CommonMethod.handleKnownException(context, ke, false);
					} catch (Exception e) {
						e.printStackTrace();
						CommonMethod.handleException(context, e);
					}
				}

				public void handleError(Exception e) {
					CommonMethod.handleException(context, e);
				}
			};
			ControlerContentTask cct = new ControlerContentTask(
					RequestUtil.getRequestUrl(URL_SUB.REGISTER), icc,
					ConnMethod.POST, false);

			RequestRegister rr = new RequestRegister();
			rr.password = password;
			rr.phoneNumber = phone;
			rr.smCode = verifycode;
			rr.resetpasswd = isResetPwd ? "1" : "0";
			cct.execute(rr);
		} else if (v == tvTitleRight) {
			Intent i = new Intent();
			i.setClass(context, ActivityLogin.class);
			context.startActivity(i);
			context.finish();
		} else if (v == ivTitleBack) {
			context.finish();
		}

	}

	private TimeCount time;

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btnObtainVerifycode.setText(context
					.getString(R.string.register_obtain_verifycode));
			btnObtainVerifycode.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btnObtainVerifycode.setClickable(false);
			btnObtainVerifycode.setText(millisUntilFinished / 1000 + "秒");
		}
	}

}
