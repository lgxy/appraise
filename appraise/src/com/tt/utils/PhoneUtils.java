package com.tt.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * 手机功能 android.permission.READ_PHONE_STATE
 * android.permission.ACCESS_NETWORK_STATE
 * 
 * @author HuangLW
 */
public class PhoneUtils {

	private TelephonyManager telephonyManager = null ;
	private ConnectivityManager connectivityManager = null ;

	public PhoneUtils(Context context) {
		if (context == null)
			throw new IllegalArgumentException("Context is null");
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager == null)
			throw new UnsupportedOperationException("can't get telephoneyManager");
		connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null)
			throw new UnsupportedOperationException("can't get connectiveManager");
	}

	public String getModel() {
		return Build.MODEL;
	}

	public String getIMEI() {
		String imei = telephonyManager.getDeviceId();
		if (imei == null)
			return "0";
		return imei;
	}

	public String getLine1Number() {
		String Line1Number = telephonyManager.getLine1Number();
		if (Line1Number == null)
			return "";
		return Line1Number;
	}

	public String getSimSerialNumber() {
		String simsrl = telephonyManager.getSimSerialNumber();
		if (simsrl == null)
			return "";
		return simsrl;
	}

	public String getSubscriberId() {
		String subuser = telephonyManager.getSubscriberId();
		if (subuser == null)
			return "";
		return subuser;
	}

	public String getDeviceSoftwareVersion() {
		return telephonyManager.getDeviceSoftwareVersion();
	}

	public String getSdkVersion() {
		return Build.VERSION.SDK;
	}

	public String getReleaseVersion() {
		return Build.VERSION.RELEASE;
	}

	public boolean isNetworkAvailable() {
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return info != null ;
	}
}
