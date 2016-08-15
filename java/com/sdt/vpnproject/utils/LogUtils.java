package com.sdt.vpnproject.utils;

import android.util.Log;

public class LogUtils {
	public static void i (String TAG, String msg) {
		Log.i(TAG+" TDS", msg);
	}
	public static void e (String TAG,String msg) {
		Log.e(TAG+" TDS", msg);
	}
	public static void d (String TAG,String msg) {
		Log.d(TAG+" TDS", msg);
	}
	public static void i (String msg) {
		i("", msg);
	}
	
	public static void e (String msg) {
		e("", msg);
	}
	
	public static void d (String msg) {
		d("", msg);
	}
}
