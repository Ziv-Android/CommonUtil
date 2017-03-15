package com.ziv.util;

import android.widget.Toast;

/**
 * Toast Class,when you need update message ,it will covered with a last message and update ui;
 * @author SenseTime
 *
 */
public class ToastUtil {
	private static Toast toast;

	/**
	 * Show toast
	 * @param object
	 */
	public static void showToast(Object object) {
		String msg = "";
		if (object instanceof String) {
			msg = (String) object;
		} else if (object instanceof Integer) {
			msg = (Integer) object + "";
		} else if (object instanceof Boolean) {
			msg = (Boolean) object + "";
		} else if (object instanceof Float) {
			msg = (Float) object + "";
		} else if (object instanceof Long) {
			msg = (Long) object + "";
		} else {
		      if (object==null) { 
			        msg= "null" ;
			   }else{
			     	msg = object.toString();
			   }
		}
		if (toast != null) {
			toast.setText(msg);
			toast.setDuration(Toast.LENGTH_LONG);
		} else {
			toast = Toast.makeText(ContextHolder.getContext(), msg, Toast.LENGTH_LONG);
		}
		toast.show();
	}
}
