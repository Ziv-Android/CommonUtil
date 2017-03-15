package com.ziv.util;


import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenMetrics {
	
	private int mScreenWidth;
	private int mScreenHeight;
	private int mViewWidth;
	private float mDensity;
	private static ScreenMetrics sInstance;
	
	private ScreenMetrics(){
		
	}
	
	public static ScreenMetrics getInstance() {
		if (sInstance == null) {
			sInstance = new ScreenMetrics();
			sInstance.init(ContextHolder.getContext());
		}
		return sInstance;
	}
	private void init(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mDensity  = dm.density;
		mScreenWidth  = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		mViewWidth = mScreenWidth-(int)(20*mDensity);
	}

	public int getScreenWidth() {
		return mScreenWidth;
	}
	public int getScreenHeight() {
		return mScreenHeight;
	}
	public int getViewWidth() {
		return mViewWidth;
	}
	public float getDensity() {
		return mDensity;
	}
}
