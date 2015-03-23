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
	
	/**
	 * �Զ��岼���ͣ������沼��0��1
	 * @author wenyujun
	 *
	 */
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

	/**
	 * ��־����
	 * @author SongQing
	 *
	 */
	public enum LogType{
		System("ϵͳ��־"), 
		Operation("�û�������־");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		LogType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * Preference��ݴ洢������
	 * @author SongQing
	 *
	 */
	public enum PreferenceType{
		Boolean("������"),
		String("�ַ���"),
		Int("����"),
		Float("������"),
		Long("������");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		PreferenceType(String value) {
				this.svalue = value;
		}
	} 

	
	public enum TemplateType{
		NORMAL("�û��Զ���ģ��"),
		WORD("���ֿ�Ѷ"),
		PICTURE("ͼƬ��Ѷ"),
		VOICE("������Ѷ"),
		VIDEO("��Ƶ��Ѷ"),
		INSTANT("���ļ���");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		TemplateType(String value) {
				this.svalue = value;
		}
	}
	
	public enum PreferenceKeys{
		User_DefaultTemplate("Ĭ�ϸ�ǩ"),
		Sys_CurrentUser("��ǰ�û�"),
		Sys_CurrentPassword("��ǰ�û�����"),
		Sys_SystemVersion("��ǰϵͳ�汾"),
		Sys_DeviceID("��ǰ�豸ID"),
		Sys_NetStatus("��ǰ����״̬"),
		Sys_LoginType("��ǰ��¼��ʽ"),
		Sys_BaseData("���»����"),
		Sys_SynManuTemp("��ǩģ��ͬ��"),
		Sys_VersionUp("ϵͳ�汾��"),
		Sys_CleanData("������"),
		Sys_CurrentServer("��ǰ������"),			//��ǰ����ʵ�����ӵķ�������Ҳ��ͨ��ӿڷ������ķ�����
		Sys_LoginServer("��¼������"),//�û����õķ�����
		Sys_VersionLowest("��ǰ��Ͱ汾Ҫ��"),
		Sys_VersionCurrent("��ǰ���°汾"),
		User_FileBlockSize("�ļ��ϴ��ֿ��С"),
		User_AutoSaveInterval("����Զ�����ʱ������"),
		User_AutoSendLocationInterval("��λ�����Զ�����ʱ������"),
		User_ManuscriptSendPolicy("������Ͳ���"),
		User_FailurePolicy("�������ʧ�ܲ���"),
		User_SendByWifi("�������wifiģʽ"),
		User_MessageTime("���ȡ����Ϣʱ��"),
		User_SavePassword("�����û�����"),
		running_state("����״̬"),
		username("��¼��"),
		password("��¼����"),
		portNum("�˿ں�"),
		chrootDir("����Ŀ¼"),
		stayAwake("�Ƿ�һֱ��������״̬"),
		show_password("������ʾ����"),
		instantUpload("���ļ���"),

		Ftp_ChooseUsername("Ftp�û���"),
		User_UploadPicCompressRatio("�ϴ�ͼƬѹ����"),
		InstantMessage_Notification("��Ϣ����"),
		InstantMessage_Notification_Sound("��Ϣ��ʾ��"),
		InstantMessage_Notification_Vibrate("��Ϣ��");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		PreferenceKeys(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * �����������������
	 * Initial/Login/Normal
	 * @author SongQing
	 *
	 */
	public enum DoPostType{
		Initial("��������"),
		Login("��½"),
		Normal("����");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		DoPostType(String value) {
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
	
	/**
	 * ��Ϣ���ͣ�ϵͳ��Ϣ(0) ������Ϣ(1) ��ʱ��Ϣ(2) XMPP��Ϣ(3) OA��Ϣ(4)
	 * @author llna
	 *
	 */
	public enum MessageType
	{
		SystemMsg("0"),
		RecommendMsg("1"),
		InstantMsg("2"),
		XMPPMsg("3"),
		OaMsg("4");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		public static MessageType parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return MessageType.values()[temp];
			} catch (Exception e) {
				return MessageType.SystemMsg;
			}
			
		}

		
		
		MessageType(String value) {
				this.svalue = value;
		}
	}

	/**
	 * ��¼״̬(����/����)
	 * @author SongQing
	 *
	 */
	public enum LoginStatus{
		/*online("����"),
		offline("����"),
		none("��ʼ");*/
		
//		online(ToastHelper.getStringFromResources(R.string.online_enum)),
	//	offline(ToastHelper.getStringFromResources(R.string.offline_enum)),
	//	none(ToastHelper.getStringFromResources(R.string.none_enum));
		
/*		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		LoginStatus(String value) {
				this.svalue = value;
		}
	
	*/}
	
	/**
	 * ���ʽӿ�����
	 * @author SongQing
	 *
	 */
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
