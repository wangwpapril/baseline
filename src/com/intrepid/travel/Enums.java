/**
 * 
 */
package com.intrepid.travel;

import java.security.PublicKey;

import org.apache.http.impl.conn.DefaultClientConnection;

import com.intrepid.travel.utils.ToastHelper;


public class Enums {

	/**
	 * 稿件上传任务状态(属于ManuscriptStatus中的StandTo的子状态)
	 * @author SongQing<p>
	 * 
	 * <发送中>当稿件正式开始上载时，修改其在数据库中的状态为发送中<p>
	 * <失败>当稿件发送中程序出现异常，导致发送停止，修改其在数据库中的状态为发送失败，
	 * 发送过程中，用户点击暂停按钮，并退出程序，也按照失败处理，下一次打开按照断点续传处理<p>
	 * <等待>当稿件变为待发状态且没有开始发送的之间的过程叫做等待<p>
	 * <完成>稿件发送完毕<p>
	 * <取消>上传任务被人工取消掉，稿件会回到编辑状态，出现在在编稿件列表<p>
	 */
	public enum UploadTaskStatus{
		Sending("发送中"),
		FailedRestart("失败需重传"),
		Paused("暂停"),
		Waiting("等待"),
		Finished("完成"),
		FailedContinue("失败需续传"),
		Cancel("取消"),
		Failed("失败"),
		Restart("重传");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		UploadTaskStatus(String value) {
				this.svalue = value;
		}
	}
	/**
	 * 稿件状态
	 * @author SongQing<p>
	 * <待发>当稿件点击发送，内容验证通过之后就修改其在数据库中的状态为待发<p>
	 * <编辑中>从新建稿件添加入库一直到点击发稿之间稿件一直处于编辑中状态<p>
	 * <淘汰>稿件从编辑中状态可以转换为淘汰<p>
	 * <已发>稿件发送成功，保存回传稿号，并更新数据库newsId字段和稿件状态为已发<p>
	 */
	public enum ManuscriptStatus{
		StandTo("待发"), 
		Editing("编辑中"), 
		Elimination("淘汰"),
		Sent("已发");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		ManuscriptStatus(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * 自定义布尔型，用作存布尔0与1
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
	 * 稿件附件类型
	 * @author SongQing
	 *
	 */
	public enum AccessoryType{
		Picture("图片"), 
		Video("视频"), 
		Voice("声音"),
		Complex("复杂文档"),
		Graph("图表"),
		Text("文字"),
		Cache("Cache");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		AccessoryType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * 日志类型
	 * @author SongQing
	 *
	 */
	public enum LogType{
		System("系统日志"), 
		Operation("用户操作日志");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		LogType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * Preference数据存储的类型
	 * @author SongQing
	 *
	 */
	public enum PreferenceType{
		Boolean("布尔型"),
		String("字符串型"),
		Int("整型"),
		Float("浮点型"),
		Long("长整型");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		PreferenceType(String value) {
				this.svalue = value;
		}
	} 
	
	/**
	 * 通用操作类型
	 * @author wenyujun
	 *
	 */
	public enum OperationType{
		Add("添加"), Update("更新"), Delete("删除"), View("查看"), None("无");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		OperationType(String value) {
				this.svalue = value;
		}
	}
	

	
	
	public enum TemplateType{
		NORMAL("用户自定义模板"),
		WORD("文字快讯"),
		PICTURE("图片快讯"),
		VOICE("语音快讯"),
		VIDEO("视频快讯"),
		INSTANT("即拍即传");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		TemplateType(String value) {
				this.svalue = value;
		}
	}
	
	public enum PreferenceKeys{
		User_DefaultTemplate("默认稿签"),
		Sys_CurrentUser("当前用户"),
		Sys_CurrentPassword("当前用户密码"),
		Sys_SystemVersion("当前系统版本"),
		Sys_DeviceID("当前设备ID"),
		Sys_NetStatus("当前网络状态"),
		Sys_LoginType("当前登录方式"),
		Sys_BaseData("更新基础数据"),
		Sys_SynManuTemp("稿签模板同步"),
		Sys_VersionUp("系统版本升级"),
		Sys_CleanData("清除数据"),
		Sys_CurrentServer("当前服务器"),			//当前程序实际连接的服务器，也是通过接口返回来的服务器
		Sys_LoginServer("登录服务器"),//用户配置的服务器
		Sys_VersionLowest("当前最低版本要求"),
		Sys_VersionCurrent("当前最新版本"),
		User_FileBlockSize("文件上传分块大小"),
		User_AutoSaveInterval("稿件自动保存时间设置"),
		User_AutoSendLocationInterval("定位服务自动发送时间设置"),
		User_ManuscriptSendPolicy("稿件发送策略"),
		User_FailurePolicy("稿件发送失败策略"),
		User_SendByWifi("稿件发送wifi模式"),
		User_MessageTime("最后取出消息时间"),
		User_SavePassword("保存用户密码"),
		running_state("运行状态"),
		username("登录名"),
		password("登录密码"),
		portNum("端口号"),
		chrootDir("访问目录"),
		stayAwake("是否一直处于运行状态"),
		show_password("明文显示密码"),
		instantUpload("即拍即传"),

		Ftp_ChooseUsername("Ftp用户名"),
		User_UploadPicCompressRatio("上传图片压缩比"),
		InstantMessage_Notification("消息推送"),
		InstantMessage_Notification_Sound("消息提示音"),
		InstantMessage_Notification_Vibrate("消息震动");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		PreferenceKeys(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * 网络连接请求的类型
	 * Initial/Login/Normal
	 * @author SongQing
	 *
	 */
	public enum DoPostType{
		Initial("初次连接"),
		Login("登陆"),
		Normal("其他");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		DoPostType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * 手机网络状态
	 * @author wenyujun
	 *
	 */
	public enum NetStatus
	{
		/*Disable("不可用"),
		WIFI("WIFI可用"),
		MOBILE("手机网络可用");*/
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
	 * 消息类型：系统信息(0) 建采信息(1) 即时信息(2) XMPP消息(3) OA消息(4)
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
	 * 信息阅读人类型：所有人(0) 部门 (1) 人员(2)
	 */
	public enum Readertype
	{
		All("0"),
		Department("1"),
		Person("2");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		public static Readertype parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return Readertype.values()[temp];
			} catch (Exception e) {
				return Readertype.Person;
			}
			
		}
		
		Readertype(String value) {
				this.svalue = value;
		}
		
	}
	/**
	 * 登录状态(联机/离线)
	 * @author SongQing
	 *
	 */
	public enum LoginStatus{
		/*online("联机"),
		offline("离线"),
		none("初始");*/
		
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
	 * 访问接口类型
	 * @author SongQing
	 *
	 */
	public enum InterfaceType{
		baseUrl("登陆文件传输"),
		instantUploadUrl("即拍即传"),
		sinosoftUrl("中科软"),
		initialUrl("初始接口"),
		topicdownloadUrl("基础文件下载"),
		oalistUrl("OA列表"),
		oaitemUrl("OA项"),
		locationUrl("定位服务");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		
		InterfaceType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * 消息接收者类型：全体（0） 部门 （1） 角色 （2） 个人（3） 目前只开放3
	 * @author llna
	 *
	 */
	public enum MsgOwnerType
	{
		All("0"),
		Department("1"),
		Role("2"),
		Person("3");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		public static MsgOwnerType parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return MsgOwnerType.values()[temp];
			} catch (Exception e) {
				return MsgOwnerType.Person;
			}
			
		}
		
		MsgOwnerType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * 消息接收或回复类型：接受（0）发送 （1）
	 * @author llna
	 *
	 */
	public enum SendOrReceiveType
	{
		Receive("0"),
		Send("1");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		public  static SendOrReceiveType parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return SendOrReceiveType.values()[temp];
			} catch (Exception e) {
				return SendOrReceiveType.Receive;
			}
			
		}
		
		SendOrReceiveType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * 消息已读或未读类型：已读（0）未读 （1）
	 * @author llna
	 *
	 */
	public enum ReadOrNotType
	{
		Read("0"),
		New("1");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		public static ReadOrNotType parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return ReadOrNotType.values()[temp];
			} catch (Exception e) {
				return ReadOrNotType.Read;
			}
			
		}
		
		ReadOrNotType(String value) {
				this.svalue = value;
		}
	}
	
	/**
	 * 消息发送状态：成功（1）失败 （0）
	 * @author llna
	 *
	 */
	public enum MsgSendStatus
	{
		Success("1"),
		failed("0");
		
		private final String svalue;

		public String getValue() {
			return svalue;
		}
		public static MsgSendStatus parseFromValue(String value){
			try {
				int temp = Integer.parseInt(value);
				return MsgSendStatus.values()[temp];
			} catch (Exception e) {
				return MsgSendStatus.failed;
			}
			
		}
		
		MsgSendStatus(String value) {
				this.svalue = value;
		}
	}
	
}
