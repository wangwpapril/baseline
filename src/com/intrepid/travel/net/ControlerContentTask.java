package com.intrepid.travel.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.intrepid.travel.Enums.ConnMethod;
import com.intrepid.travel.utils.Common;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;


public class ControlerContentTask extends
		AsyncTask<IContentParms, Void, ResultHolder> {

	private IControlerContentCallback icc;
	private String url;
	private ConnMethod connMethod;
	private boolean isHideLoading;

	public ControlerContentTask(String url, IControlerContentCallback icc,
			ConnMethod connMethod,boolean isHideLoading){
		this.icc = icc;
		this.url = url;
		this.connMethod = connMethod;
		this.isHideLoading = isHideLoading;
	}
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(!isHideLoading){
			new Handler().post(new Runnable() {
				public void run() {
						Common.showLoading("Loading");
				}
			});
		}
	}

	private List<NameValuePair> getParams(IContentParms[] params){
		List<NameValuePair> connParams = null;
		try {
			if (params != null && params.length > 0) {
				connParams = new ArrayList<NameValuePair>();
//				Iterator<String> it = params[0].keySet().iterator();
//				while (it.hasNext()) {
//					String key = it.next().toString();
//					connParams.add(new BasicNameValuePair(key, params[0].get(key)
//							));
//				}
				connParams.add(new BasicNameValuePair("json", params[0].getparmStr()));
			}
		} catch (Exception e) {
			return null;
		}
		return connParams;
	}

	protected ResultHolder doInBackground(IContentParms... params){

		ResultHolder rh = new ResultHolder();
		try {
			switch (connMethod) {
			case GET:
				rh.setResult(SimpleHttpClient.get(url, getParams(params)));
				break;
			case POST:
					rh.setResult(SimpleHttpClient.post(url, getParams(params)));
				break;
			}
			rh.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			rh.setSuccess(false);
			rh.setResult(e.getMessage());
		}
		return rh;
	}

	protected void onPostExecute(ResultHolder result){
		super.onPostExecute(result);
		Common.cancelLoading();
		if (result.isSuccess()) {
			icc.handleSuccess(result.getResult());
		} else {
			icc.handleError(new Exception(result.getResult()));
		}
	}


	
//	private String getParamsJson(IContentParms params){
//		try{
//			JSONObject jo = new JSONObject();
//			HashMap<String, String> param = params[0];
//			Iterator<String> iterator= param.keySet().iterator();
//			while(iterator.hasNext()){
//				String key = iterator.next();
//				jo.put(key, param.get(key));
//			}
//			return jo.toString();
//		}catch(Exception e){
//			return null;
//		}
//	}
	
	
}