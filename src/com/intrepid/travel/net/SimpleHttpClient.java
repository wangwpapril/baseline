package com.intrepid.travel.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;



import com.intrepid.travel.Enums.NetStatus;
import com.intrepid.travel.MyApplication;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SimpleHttpClient {
	
	
	
	private static final int OK = 200;// OK: Success!
	private static final int NOT_MODIFIED = 304;
	private static final int BAD_REQUEST = 400;
	private static final int NOT_AUTHORIZED = 401;
	private static final int FORBIDDEN = 403;
	private static final int NOT_FOUND = 404;
	private static final int NOT_ACCEPTABLE = 406;
	private static final int INTERNAL_SERVER_ERROR = 500;
	private static final int BAD_GATEWAY = 502;
	private static final int SERVICE_UNAVAILABLE = 503;
	
	private static final int NETWORK_DISABLED=601;

	String multipart_form_data = "multipart/form-data";
	String boundary = "7d33a816d302b6";
	String twoHyphens="--";
	String lineEnd="\r\n";

	private static int retryCount = 1;

	
	public static String doPost(PostParameter[] postParams,String connectionUrl,int connectTimeout) throws Exception{
		NetStatus netStatus = MyApplication.getNetStatus();
		if(netStatus == NetStatus.Disable){
			return String.valueOf(NETWORK_DISABLED);
		}
		
		int retriedCount = 0;
		Response response = null;

		for (retriedCount = 0; retriedCount < retryCount; retriedCount++) {

			int responseCode = -1;
			HttpURLConnection connection = null;
			OutputStream os = null;
			try {
				connection = (HttpURLConnection) new URL(connectionUrl).openConnection();
				if(connectTimeout !=0){
					connection.setConnectTimeout(connectTimeout);
				}
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				connection.setDoOutput(true);
				
				String postParam = "";
				if (postParams != null) {
					postParam = encodeParameters(postParams);
				}
				byte[] bytes = postParam.getBytes("UTF-8");
				connection.setRequestProperty("Content-Length",Integer.toString(bytes.length));
				os = connection.getOutputStream();
				os.write(bytes);
				os.flush();
				os.close();
				response = new Response(connection);
				responseCode = response.getStatusCode();

				if (responseCode != OK) {
					if (responseCode < INTERNAL_SERVER_ERROR || retriedCount == retryCount)
						throw new Exception(getCause(responseCode));
				} else {break;}				
			}catch(ConnectTimeoutException e){
				throw new Exception("ConnectionTimeout",e);
			}catch(InterruptedIOException e){	
				throw new Exception("ConnectionTimeout",e);
			}catch (Exception e) {
				throw new Exception(e.getMessage(), e);
			}
		}
		return response.asString();
	}

	public static String doGet(PostParameter[] getParams, String connectionUrl,int connectTimeout) throws Exception{
		NetStatus netStatus = MyApplication.getNetStatus();
		if(netStatus == NetStatus.Disable){
			return String.valueOf(NETWORK_DISABLED);
		}
	
		int retriedCount = 0;
		Response response = null;
		
		for(retriedCount = 0; retriedCount < retryCount; retriedCount++){
			
			int responseCode = -1;
			try {
				HttpURLConnection connection = null;
				try {

//					connection = (HttpURLConnection) new URL(connectionUrl+"?"+encodeParameters(getParams)).openConnection();
					connection = (HttpURLConnection) new URL(connectionUrl).openConnection();
					if(connectTimeout !=0){
						connection.setConnectTimeout(connectTimeout);
					}
					response = new Response(connection);
					responseCode = response.getStatusCode();

					if (responseCode != OK) {
						if (responseCode < INTERNAL_SERVER_ERROR || retriedCount == retryCount)
							//throw new XmcException(getCause(responseCode)+ "\n" + response.asString(), responseCode);
							throw new Exception(getCause(responseCode));
					} else {
						break;
					}

				} finally {
					
				}
			}catch(ConnectTimeoutException e){
				throw new Exception("ConnectionTimeout",e);
			}catch(InterruptedIOException e){	
				throw new Exception("ConnectionTimeout",e);
			}catch (Exception e) {
				throw new Exception(e.getMessage(), e);
			}
		}
		
		return response.asString();
	}

	public static String post(String entity, String connectionUrl,
			int connectTimeout) throws Exception{
		int retriedCount = 0;
		Response response = null;


		for (retriedCount = 0; retriedCount < retryCount; retriedCount++) {

			int responseCode = -1;
			HttpURLConnection connection = null;
			OutputStream os = null;
			try {
				connection = (HttpURLConnection) new URL(connectionUrl)
						.openConnection();
				if (connectTimeout != 0) {
					connection.setConnectTimeout(connectTimeout);
				}
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type",
						"application/json");
				connection.setRequestProperty("Accept", "application/json");

				connection.setDoOutput(true);

				String postParam = "";

				if (entity != null) {
					postParam = entity;
				}

				byte[] bytes = postParam.getBytes("UTF-8");
				connection.setRequestProperty("Content-Length",
						Integer.toString(bytes.length));
				os = connection.getOutputStream();
				os.write(bytes);
				os.flush();
				os.close();
				response = new Response(connection);
				responseCode = response.getStatusCode();

				if (responseCode != OK) {
					if (responseCode < INTERNAL_SERVER_ERROR
							|| retriedCount == retryCount)
						// throw new XmcException(getCause(responseCode) + "\n"
						// + response.asString(), responseCode);
					throw new Exception(getCause(responseCode));
				} else {
					break;
				}
			} catch (ConnectTimeoutException e) {
				throw new Exception("ConnectionTimeout", e);
			} catch (InterruptedIOException e) {
				throw new Exception("ConnectionTimeout", e);
			} catch (Exception e) {
				throw new Exception(e.getMessage(), e);
			}
		}

		return response.asString();
	}

	private String addFormField(PostParameter[] formPostParams) throws Exception {

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < formPostParams.length; i++) {

			buffer.append(twoHyphens+boundary+lineEnd);
			buffer.append(
					"Content-Disposition: form-data; name=\""
							+ URLEncoder.encode(formPostParams[i].getName(), "UTF-8")+ "\""+lineEnd);
			buffer.append(lineEnd);
			buffer.append(URLEncoder.encode(formPostParams[i].getObject().toString(), "UTF-8")+lineEnd);
		}
		return buffer.toString();
	}

	private String addContentField(PostParameter[] contentPostParams) {

		StringBuffer buffer = new StringBuffer();
		
		buffer.append(twoHyphens+boundary+lineEnd);
		buffer.append("Content-Disposition: form-data; name=\"" + contentPostParams[0].getObject().toString()
						+"/"+contentPostParams[1].getObject().toString() +"\"; filename=\"" 
						+ contentPostParams[2].getObject().toString() + "\"" + lineEnd); 
		buffer.append("Content-Type: " + contentPostParams[3].getObject().toString() + lineEnd); 
		buffer.append(lineEnd);

		return buffer.toString();
	}

	private static String encodeParameters(PostParameter[] postParams) throws Exception {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < postParams.length; i++) {
			if (i != 0){
				buffer.append("&");
			}
			buffer.append(URLEncoder.encode(postParams[i].getName(), "UTF-8"))
					.append("=")
					.append(URLEncoder.encode(postParams[i].getObject().toString(), "UTF-8"));
		}

		return buffer.toString();
	}

	private static String getCause(int statusCode) {
		String cause = null;
		switch (statusCode) {
		case NOT_MODIFIED:
			break;
		case BAD_REQUEST:
			cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
			break;
		case NOT_AUTHORIZED:
			cause = "Authentication credentials were missing or incorrect.";
			break;
		case FORBIDDEN:
			cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
			break;
		case NOT_FOUND:
			cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
			break;
		case NOT_ACCEPTABLE:
			cause = "Returned by the Search API when an invalid format is specified in the request.";
			break;
		case INTERNAL_SERVER_ERROR:
			cause = "Something is broken.  Please post to the group so the Weibo team can investigate.";
			break;
		case BAD_GATEWAY:
			cause = "Weibo is down or being upgraded.";
			break;
		case SERVICE_UNAVAILABLE:
			cause = "Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
			break;
		default:
			cause = "";
		}
		return statusCode + ":" + cause;
	}

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
