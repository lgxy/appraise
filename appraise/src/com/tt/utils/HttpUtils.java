package com.tt.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

public class HttpUtils {
	private static final String tag = "HttpUtils";
	private static final Object lock = new Object();
	public static int socketTimeOut = 5000;
	public static int connectionTimeOut = 15000;
	
	public final static HttpUriRequest getRequest(String url, final Hashtable<String, String> paramMap) throws Exception {
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> parms = new ArrayList<NameValuePair>();
		if (paramMap != null && paramMap.size() > 0) {
			for (String key : paramMap.keySet()){
				if(key == null || key.trim().length() == 0)
					continue ;
				parms.add(new BasicNameValuePair(key, paramMap.get(key)));
			}
		}
		httpPost.setEntity(new UrlEncodedFormEntity(parms, HTTP.UTF_8));
		httpPost.setHeader("Content-Type", "text/plain");
		return httpPost;
	}
	
	public static final HttpResponse execute(HttpUriRequest request) throws ClientProtocolException, IOException {
		return getDefaultHttpClient().execute(request);
	}
	
	/** 获得httpClient实例 */
	private static final HttpClient getDefaultHttpClient() {
		synchronized (lock) {
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeOut);
			HttpConnectionParams.setSoTimeout(httpParams, connectionTimeOut);
			DefaultHttpClient client = new DefaultHttpClient(httpParams);
			return client;
		}
	}
}
