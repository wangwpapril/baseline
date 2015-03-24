package com.intrepid.travel;

import java.security.PublicKey;

import org.apache.http.impl.conn.DefaultClientConnection;

import com.intrepid.travel.utils.ToastHelper;


public class Enums {

	public enum ConnMethod {
		GET("get"), POST("post");
		private String type;

		private ConnMethod(String type){
			this.type = type;
		}

		public String getType(){
			return type;
		}

		public void setType(String type){
			this.type = type;
		}

	}
	
	public enum XmcBool{
		False("0"), True("1"), Error("2");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		public static XmcBool parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return XmcBool.values()[temp];
			} catch (Exception e) {
				return XmcBool.Error;
			}
			
		}
		
		XmcBool(String value) {
				this.svalue = value;
		}
	}




	public enum PreferenceType{
		Boolean("Boolean"),
		String("String"),
		Int("Int"),
		Float("Float"),
		Long("Long");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		PreferenceType(String value) {
				this.svalue = value;
		}
	} 

	

	
	public enum PreferenceKeys{
		currentUser("current user"),

		etStatus("network status̬"),
		username("user name"),
		token("token"),
		password("password"),
		loginStatus("login status"),
		userId("user id");
	
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		PreferenceKeys(String value) {
				this.svalue = value;
		}
	}
	


	public enum NetStatus
	{
		Disable(ToastHelper.getStringFromResources(R.string.network_disconnected)),
		WIFI(ToastHelper.getStringFromResources(R.string.network_wifi_connected)),
		MOBILE(ToastHelper.getStringFromResources(R.string.network_mobile_connected));
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		NetStatus(String value) {
				this.svalue = value;
		}
	}
	

	public enum InterfaceType{
		baseUrl("��½�ļ�����"),
		instantUploadUrl("���ļ���"),
		sinosoftUrl("�п���"),
		initialUrl("��ʼ�ӿ�"),
		topicdownloadUrl("���ļ�����"),
		oalistUrl("OA�б�"),
		oaitemUrl("OA��"),
		locationUrl("��λ����");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		InterfaceType(String value) {
				this.svalue = value;
		}
	}
	
}
