package com.intrepid.travel.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.intrepid.travel.Enums.ConnMethod;
import com.intrepid.travel.R;
import com.intrepid.travel.models.Country;
import com.intrepid.travel.net.ControlerContentTask;
import com.intrepid.travel.net.IControlerContentCallback;




public class RegisterActivity extends BaseActivity {

	protected static final String TAG = "ActivityRegister";
	private EditText etFirstName;
	private EditText etLastName;
	private EditText etEmail;
	private AutoCompleteTextView etCountry;
	private EditText etUserName;
	private EditText etPassword;
	private EditText etPolicyNumber;
	private Button btnSignUp;
	
	private List<Country> countryList;
	
	private ArrayAdapter countryAdapter = null;
	private static String countryCode = null;
	private static String companyId = null;
	private static List<String> countryNames = null, countryCodes = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fetchCountries();
		this.setContentView(R.layout.signup_layout);
		initView();
	}
	
	private void fetchCountries() {
		
		IControlerContentCallback icc = new IControlerContentCallback() {
			public void handleSuccess(String content) {
				JSONObject countries = null;
				try {
					countries = new JSONObject(content);
					JSONArray array = countries.getJSONArray("countries");
					int len = array.length();
					countryList = new ArrayList<Country>(len);
					for (int i =0;i < len; i++){
						countryList.add(new Country(array.getJSONObject(i)));
					}

					countryNames = new ArrayList<String>();
					countryCodes = new ArrayList<String>();
					for (Country country : countryList) {
						countryNames.add(country.name);
						countryCodes.add(country.countryCode);
					}
					countryAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, countryNames);
					countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					etCountry.setAdapter(countryAdapter);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

			public void handleError(Exception e) {
				return;
			}
		};
		ControlerContentTask cct = new ControlerContentTask(
				"https://staging.intrepid247.com/v1/countries", icc,
				ConnMethod.GET, false);

		String ss = null;
		cct.execute(ss);		
	}


	private void initView() {
		super.initTitleView();
		etFirstName = (EditText) findViewById(R.id.firstNameEditText);
		etLastName = (EditText) findViewById(R.id.lastNameEditText);
		etEmail = (EditText) findViewById(R.id.emailEditText);
		etCountry = (AutoCompleteTextView) findViewById(R.id.countryEditText);
		etUserName = (EditText) findViewById(R.id.userNameEditText);
		etPassword = (EditText) findViewById(R.id.PasswordEditText);
		etPolicyNumber = (EditText) findViewById(R.id.policyNumberEditText);
		etUserName = (EditText) findViewById(R.id.userNameEditText);
		btnSignUp = (Button) findViewById(R.id.butSignUp);
		btnSignUp.setOnClickListener(this);
		
		
		etCountry.setAdapter(countryAdapter);
		etCountry.setEnabled(true);
		etCountry.setDropDownBackgroundResource(R.drawable.login_btn);

		etCountry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				etCountry.showDropDown();
				
				
			}
			
		});
		
		etCountry.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				countryCode = countryAdapter.getItem(position).toString();
				countryCode = countryCodes.get(position);
//				etCountry.setEnabled(false);
				
			}
			
		});
	}


	
	@Override
	protected void initTitle() {
		tvTitleName.setText("Sign Up");
		ivTitleBack.setVisibility(View.VISIBLE);
//		tvTitleRight.setVisibility(View.VISIBLE);
	//	tvTitleRight.setText(R.string.register_title_login);
//		tvTitleRight.setOnClickListener(this);
	//	ivTitleBack.setOnClickListener(this);
		// TODO:btnTitleBack.setImageResource(resId);
		// TODO:ivTitleRight.setImageResource(resId);

	}

	@Override
	public void onClick(View v) {
		if (v == etCountry) {
			etCountry.setEnabled(true);

		} else if (v == btnSignUp) {
			String firstName = etFirstName.getText().toString();
			String lastName = etLastName.getText().toString();
			String email = etEmail.getText().toString();
			String country = etCountry.getText().toString();
			String userName = etUserName.getText().toString();
			String password = etPassword.getText().toString();
			String policyNumber = etPolicyNumber.getText().toString();
			
			if (policyNumber != null) {
				checkGroupNumber(policyNumber);
			}
			
/*			if (TextUtils.isEmpty(phone) || !CommonUtil.isPhoneNo(phone)) {
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
*/

		} else if (v == tvTitleRight) {
			Intent i = new Intent();
//			i.setClass(context, ActivityLogin.class);
			context.startActivity(i);
			context.finish();
		} else if (v == ivTitleBack) {
			context.finish();
		}

	}


	private void checkGroupNumber( String gNumber) {

		IControlerContentCallback icc = new IControlerContentCallback() {
			public void handleSuccess(String content) {
				JSONObject company = null;
				try {
					company = new JSONObject(content).getJSONObject("company");
					companyId = company.getString("id");	
					signUp();
					
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

			public void handleError(Exception e) {
				return;
			}
		};
		ControlerContentTask cct = new ControlerContentTask(
				"https://api.intrepid247.com/v1/companies/checkGroupNum", icc,
				ConnMethod.POST, false);
		
		JSONObject company = new JSONObject();
		try {
			company.put("group_num", gNumber);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JSONObject check = new JSONObject();
		try {
			check.put("company", company);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		cct.execute(check.toString());		
	}

	private void signUp() {
		
	}
	
}
