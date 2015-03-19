package com.intrepid.travel.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Handler;
import android.os.Message;

public class SimpleHttpClient {

	// private static HttpClient hc = HttpFactury.getHttpClient();

	/**
	 * @param args
	 */
	// public static void main(String[] args){
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("email", "xxx@gmail.com"));
	// params.add(new BasicNameValuePair("pwd", "xxx"));
	// params.add(new BasicNameValuePair("save_login", "1"));
	//
	// String url = "http://www.oschina.net/action/user/login";
	//
	// // String body = post(url, params);
	// // System.out.println(body);
	// }

	/**
	 * Get请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url, List<NameValuePair> params)
			throws Exception{
//		LogUtil.d("GET connecting to " + url);
		String body = null;
		// Get请求
		HttpGet httpget = new HttpGet(url);
		// 设置参数

		if (null != params) {
			String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
			String link = url.contains("?") ? "&" : "?";
			httpget.setURI(new URI(httpget.getURI().toString() + link + str));
		} else {

			httpget.setURI(new URI(httpget.getURI().toString()));
		}
		// 发送请求
		HttpResponse httpresponse = HttpFactory.getHttpClient()
				.execute(httpget);
		if (httpresponse.getStatusLine().getStatusCode() != 200) {
			throw new Exception("连接错误！");
		}
		// 获取返回数据
		HttpEntity entity = httpresponse.getEntity();
		body = EntityUtils.toString(entity, "UTF-8");
		if (entity != null) {
			entity.consumeContent();
		}
		return body;
	}

	/**
	 * // Post请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, List<NameValuePair> params)
			throws Exception{
//		LogUtil.d("POST connecting to " + url);
		String body = null;
		// Post请求
		HttpPost httppost = new HttpPost(url);
		// 设置参数
		if (params != null) {
			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// String p = "请求参数：";
			// for (NameValuePair nvp : params) {
			// p += (nvp.getName() + ":" + nvp.getValue() + "    ");
			// }
//			LogUtil.d("请求参数:", params.toString());
		}
		// 发送请求
		HttpResponse httpresponse = HttpFactory.getHttpClient().execute(
				httppost);

		if (httpresponse.getStatusLine().getStatusCode() != 200) {
			throw new Exception("连接错误！");
		}
		// 获取返回数据
		HttpEntity entity = httpresponse.getEntity();
		body = EntityUtils.toString(entity, "UTF-8");
		if (entity != null) {
			entity.consumeContent();
		}
		JSONObject jo = new JSONObject(body);
		String s = jo.toString();
		return body;
	}

	public static String doHttpPost(String json, String URL){
		try {
			String j = new JSONObject(json).toString();
			System.out.println("发起的数据:" + j);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		byte[] xmlData = json.getBytes();
		InputStream instr = null;
		java.io.ByteArrayOutputStream out = null;
		try {
			URL url = new URL(URL);
			URLConnection urlCon = url.openConnection();
			urlCon.setDoOutput(true);
			urlCon.setDoInput(true);
			urlCon.setUseCaches(false);
			urlCon.setRequestProperty("Content-Type", "application/json");
			urlCon.setRequestProperty("Accept", "application/json");
			urlCon.setRequestProperty("Content-length",
					String.valueOf(xmlData.length));
			
			

			System.out.println(String.valueOf(xmlData.length));
			DataOutputStream printout = new DataOutputStream(
					urlCon.getOutputStream());
			printout.write(xmlData);
			printout.flush();
			printout.close();
			instr = urlCon.getInputStream();
			byte[] tmp = new byte[1024];
			StringBuffer result = new StringBuffer();
			while(instr.read(tmp)>0){
				 result.append(new String(tmp,"utf-8"));
			}
//			byte[] bis = new byte[instr..available()];
//			instr.read(bis);
			String ResponseString = result.toString();
			if ((ResponseString == null) || ("".equals(ResponseString.trim()))) {
				System.out.println("返回空");
			}
			System.out.println("返回数据为:" + ResponseString);
			
			try {
				out.close();
				instr.close();

			} catch (Exception ex) {
				return "0";
			}
			return ResponseString;

		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	public static String downloadGet(String url, String filePath,
			List<NameValuePair> params) throws Exception{
//		LogUtil.d("download connecting to " + url);
		HttpGet httpget = new HttpGet(url);
		if (null != params) {
			String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
			String link = url.contains("?") ? "&" : "?";
			httpget.setURI(new URI(httpget.getURI().toString() + link + str));
		} else {
			httpget.setURI(new URI(httpget.getURI().toString()));
		}
		try {
			HttpResponse httpResponse1 = HttpFactory.getHttpClient().execute(
					httpget);
			return parseStream(httpResponse1, filePath);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			HttpFactory.getHttpClient().getConnectionManager().shutdown();
		}

	}

	private static String parseStream(HttpResponse httpResponse1,
			String filePath) throws Exception{
		StatusLine statusLine = httpResponse1.getStatusLine();
		if (statusLine.getStatusCode() == 200) {
			InputStream inputStream = httpResponse1.getEntity().getContent();
			byte[] b = new byte[1024];
			File f = new File(filePath);
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			if (f.exists()) {
				return f.getAbsolutePath();
			} else {
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f);
			int j = 0;
			while ((j = inputStream.read(b)) != -1) {
				fos.write(b, 0, j);
			}
			fos.flush();
			fos.close();
			return filePath;
		}
		throw new Exception("网络连接错误！");
	}

	public static String downloadPost(String url, String filePath,
			List<NameValuePair> params) throws Exception{
//		LogUtil.d("download connecting to " + url);
		HttpPost httppost = new HttpPost(url);
		// 设置参数
		if (params != null)
			httppost.setEntity(new UrlEncodedFormEntity(params));
		try {
			HttpResponse httpResponse1 = HttpFactory.getHttpClient().execute(
					httppost);
			return parseStream(httpResponse1, filePath);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			HttpFactory.getHttpClient().getConnectionManager().shutdown();
		}
	}

	private static void showProgress(Handler handler, int curBytes,
			int totalBytes){
		Message msg = new Message();
		msg.arg1 = curBytes;
		msg.arg2 = totalBytes;
		handler.sendMessage(msg);
	}

}
