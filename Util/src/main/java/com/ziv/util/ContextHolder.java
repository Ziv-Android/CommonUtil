package com.ziv.util;

import android.content.Context;

//FIXME !!
//This class should be carefully mark out the context
//Currently, we just use the setting Context as the global Context
public class ContextHolder {
	private Context mContext;
	private static ContextHolder sHolder = null;
	
	public static void initContextHolder(Context context) {
		sHolder = new ContextHolder(context);
	}
	
	private ContextHolder(Context context) {
		mContext = context;
	}

	public static void setContext(Context context) {
		sHolder.mContext = context;
	}
	
	public static Context getContext() {
		return sHolder.mContext;
	}
	
}
