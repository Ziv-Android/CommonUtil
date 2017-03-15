package com.ziv.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class ProgressUtil {
	private static ProgressDialog mProgressDialog;
	private static final boolean DEBUG = false;
	private static final String TAG = "ProgressUtil";

	public static void showDialogIfNeed(Context context, String msg) {
		if (DEBUG) {
			Log.d(TAG, "showDialogIfNeed");
		}
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage(msg);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
	}

	public static void dimissDialog() {
		if (DEBUG) {
			Log.d(TAG, "dimissDialog");
		}
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.cancel();
			mProgressDialog.dismiss();
		}
	}
}
