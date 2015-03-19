package com.intrepid.travel.net;

public interface IControlerContentCallback {
	//所谓的Success，只是与服务器连接成功(未发生连接超时等异常)，包含服务器发回请求成功和请求失败的内容
	public void handleSuccess(String content);

	public void handleError(Exception e);
}
