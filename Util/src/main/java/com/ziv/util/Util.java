package com.ziv.util;

import android.graphics.Rect;
import android.graphics.RectF;

public class Util {
	public static final int MASTER_CAMERA_ID = 100;
	public static final int SLAVE_CAMERA_ID = 101;
	public static final int MASTER_WIDTH = 1600;
	public static final int MASTER_HEIGHT = 1200;
	public static final int SLAVE_WIDTH = 1600;
	public static final int SLAVE_HEIGHT = 1200;
	public static final int PREVIEW_WIDTH = 1280;
	public static final int PREVIEW_HEIGHT = 720;
	public static final int DEPTH_HEIGHT = 1280;
	public static final int DEPTH_WIDTH = 960;
	public static final int REFOCUS = 0;
	public static final int SMART_CUT = 3;
	public static final int PARALLAX = 4;
	public static final int PHOTO_ENHANCE = 6;
	public static final int PENCIL_SKETCH = 9;
	public static final int STEREO_IMAGE = 11;
	public static final int RADIAL_BLUR = 12;
	public static final int MOTION_BLUR = 13;
	public static final boolean SHOW_TOAST = false;
	public static final boolean DETECT_FACE = true;
	public static final String IMAGE_JPG = ".jpg";
	public static final String IMAGE_JPEG = ".jpeg";
	public static final String IMAGE_3D_FILE = "";
	// public static final String IMAGE_RECTIFY = "Image_M";

	public static Rect RectFtoRect(RectF rectf) {
		return new Rect((int) rectf.left, (int) rectf.top, (int) rectf.right, (int) rectf.bottom);
	}
}
