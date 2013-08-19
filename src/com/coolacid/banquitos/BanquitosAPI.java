package com.coolacid.banquitos;

import com.loopj.android.http.*;

public class BanquitosAPI {
	
	private static final String BASE_URL = "http://api.banquitos.pati.to/";
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public void apiCall(String endpoint, RequestParams params, String method, AsyncHttpResponseHandler responseHandler){
		if (method == "GET"){
			client.get(getAbsoluteURL(endpoint), params, responseHandler);
		}else if (method == "POST"){
			client.post(getAbsoluteURL(endpoint), params, responseHandler);
		}
	}
	
	private static String getAbsoluteURL(String endpoint){
		return BASE_URL + endpoint;
	}
	
}
