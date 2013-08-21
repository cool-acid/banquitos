package com.coolacid.banquitos;

import android.util.Log;

import com.loopj.android.http.*;

public class BanquitosAPI {
	
	private static final String BASE_URL = "http://api.banquitos.pati.to/";
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public void apiCall(String endpoint, RequestParams params, String method, AsyncHttpResponseHandler responseHandler){
		Log.i("com.coolacid.banquitos", method);
		if (method == "GET"){
			client.get(getAbsoluteURL(endpoint), params, responseHandler);
		}else if (method == "POST"){
			Log.i("MYTAG", "Ejecutando POST");
			client.post(getAbsoluteURL(endpoint), params, responseHandler);
		}
	}
	
	private static String getAbsoluteURL(String endpoint){
		return BASE_URL + endpoint;
	}
	
}
