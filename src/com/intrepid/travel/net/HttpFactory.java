package com.intrepid.travel.net;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

public class HttpFactory {

	public static HttpClient getHttpClient(){

		HttpClient hc = new DefaultHttpClient();
		// 请求超时
		hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				20000);
		// 读取超时
		hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
		return hc;
	}
}
