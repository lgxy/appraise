package com.tt.core;

import android.util.Log;

public class LogUtil {

	public static void d(String tag, String msg) {
		Log.d(tag, msg);
	}

	public static void w(String tag, String msg) {
		Log.w(tag, msg);
	}

	public static void e(String tag, String msg) {
		Log.e(tag, msg);
	}
}
