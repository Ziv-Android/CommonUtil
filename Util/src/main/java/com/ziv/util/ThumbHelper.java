package com.ziv.util;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThumbHelper {

	public static final String IMG_NAME = "img_path";
	public static final String THUMB_NAME = "thumb_path";
	private static final int THUMB_WIDTH = 400;
	private static final int THUMB_HEIGHT = 300;
	private int mViewWidth;
	private int mThumbXMargin;
	private int mThumbYMargin;
	private int mThumbWidth;
	private int mThumbHeight;

	private static ThumbHelper mThumbHelper;

	private ThumbHelper() {
		init();
	}

	public static ThumbHelper getInstance() {
		if (mThumbHelper == null) {
			mThumbHelper = new ThumbHelper();
		}
		return mThumbHelper;
	}

	private void init() {
		mViewWidth = ScreenMetrics.getInstance().getViewWidth();
		mThumbWidth = mViewWidth / 3;
		if (mThumbWidth > THUMB_WIDTH) {
			mThumbWidth = THUMB_WIDTH;
		}
		mThumbHeight = mThumbWidth * 3 / 4;
		mThumbXMargin = (THUMB_WIDTH - mThumbWidth) / 2;
		mThumbYMargin = (THUMB_HEIGHT - mThumbHeight) / 2;
	}

	public int getThumbWidth() {
		return mThumbWidth;
	}

	public int getThumbHeight() {
		return mThumbHeight;
	}

	public int getThumbXMargin() {
		return mThumbXMargin;
	}

	public int getThumbYMargin() {
		return mThumbYMargin;
	}

	public void createThumbsIfNeed() {
		createThumbImage(StorageUtil.IMG_PATH_ABS, StorageUtil.THUMB_PATH_ABS);
	}

	private void createThumbImage(String imgPath, String thumbPath) {
		if (FileUtil.initDir(imgPath) && FileUtil.initDir(thumbPath)) {
			List<String> srcImg = FileUtil.loadImages(imgPath);
			if (null == srcImg) {
				return;
			}
			for (String p : srcImg) {
				if (null == p)// 容错处理
					return;
				String name = FileUtil.getFileName(p);
				String thumb = thumbPath + name;
				if (!FileUtil.existFile(thumb)) {
					Bitmap bitmap = BitmapUtil.createImageThumbnail(p, THUMB_WIDTH, THUMB_HEIGHT);
					BitmapUtil.saveBitmap(thumb, bitmap);
				}
			}
		}
	}

	public List<HashMap<String, String>> getThumbImages(int modeNumber) {
		// StorageUtil.setImgPathInfo(modeNumber);
		return getThumbImage(StorageUtil.IMG_PATH_ABS, StorageUtil.THUMB_PATH_ABS);
	}

	private List<HashMap<String, String>> getThumbImage(String imgPath, String thumbPath) {
		List<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
		if (FileUtil.initDir(imgPath) && FileUtil.initDir(thumbPath)) {
			Log.d("tag","-->imgPath : "+imgPath+",thumbPath :"+thumbPath);
			List<String> srcImg = FileUtil.loadImages(imgPath);
			if (null == srcImg) {
				return null;
			}
			for (String p : srcImg) {
				if (null == p)// 容错处理
					return null;
				String name = FileUtil.getFileName(p);
				String thumb = thumbPath + name;
				if (!FileUtil.existFile(thumb)) {
					Bitmap bitmap = BitmapUtil.createImageThumbnail(p, THUMB_WIDTH, THUMB_HEIGHT);
					//
					BitmapUtil.saveBitmap(thumb, bitmap);
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(IMG_NAME, p);
				map.put(THUMB_NAME, thumb);
				mapList.add(map);
			}
		}
		return mapList;
	}

}
