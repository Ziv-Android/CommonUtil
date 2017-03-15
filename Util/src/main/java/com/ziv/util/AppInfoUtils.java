package com.ziv.util;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

/***
 * function：获取app相关信息类
 * 
 * @author jackshy
 * @date ：2016-03-01
 */

public class AppInfoUtils {

	private static final String TAG = "AppInfoUtils";

	/***
	 * 获取Imei号
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		String imei = "";
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Application.TELEPHONY_SERVICE);
			if (tm != null) {
				imei = tm.getDeviceId();
			}
		} catch (Exception e) {
			Log.d(TAG, "getIMEI", e);
		}
		return imei;
	}

	/**
	 * 获取version name
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		String version = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			Log.d(TAG, "getVersionName", e);
		}

		return version;
	}

	/**
	 * 获取versionCode
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static int getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		int versionCode = 0;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			versionCode = packInfo.versionCode;
		} catch (NameNotFoundException e) {
			Log.d(TAG, "getVersionCode", e);
		}
		return versionCode;
	}

	/**
	 * 获取sdk
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getPhoneSDKInt() {
		int version = 0;
		try {
			version = Integer.valueOf(Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			Log.d(TAG, "getPhoneSDKInt", e);
		}
		return version;
	}

	/**
	 * 获取mac地址
	 *
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info;
		String macAddress = "";
		try {
			info = wifi.getConnectionInfo();
			macAddress = info.getMacAddress();
		} catch (Exception e) {
			Log.d(TAG, "getMacAddress", e);
		}
		return macAddress;
	}

	/**
	 * 获取android id
	 *
	 * @param context
	 * @return
	 */
	public static String getAndroidId(Context context) {
		String androidID = "";
		try {
			androidID = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
		} catch (Exception e) {
			Log.d(TAG, "getAndroidId", e);
		}
		return androidID;
	}

	/**
	 * 获取build serial
	 *
	 * @param context
	 * @return
	 */
	@TargetApi(9)
	public static String getBuildSerial(Context context) {
		String buildSerial = "";
		try {
			if (Build.VERSION.SDK_INT >= 9) {
				buildSerial = Build.SERIAL;
			}
		} catch (Exception e) {
			Log.d(TAG, "getBuildSerial", e);
		}
		return buildSerial;
	}

}
