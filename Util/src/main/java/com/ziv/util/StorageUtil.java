package com.ziv.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageUtil {
	public static final String TAG = "StorageUtil";
	public static final boolean DEBUG = true;
	public static final String IMAGE_ROOT = "DCIM/Examples_photoFilter";
	public static final String BLOCK_REMOVAL_IMAGE_PATH_DIR = "image_block_removal";
	public static final String BLOCK_REMOVAL_THUMB_PATH_DIR = "thumb_block_removal";
	public static final String BLOCK_REMOVAL_SAVE_PATH_DIR = "save_block_removal";
	public static final String DEHAZE_IMAGE_PATH_DIR = "image_dehaze";
	public static final String DEHAZE_THUMB_PATH_DIR = "thumb_dehaze";
	public static final String DEHAZE_SAVE_PATH_DIR = "save_dehaze";
	public static final String FAST_DENOISE_IMAGE_PATH_DIR = "image_fast_denoise";
	public static final String FAST_DENOISE_THUMB_PATH_DIR = "thumb_fast_denoise";
	public static final String FAST_DENOISE_SAVE_PATH_DIR = "save_fast_denoise";
	public static final String NIGHT_ENHANZE_IMAGE_PATH_DIR = "image_night_enhanze";
	public static final String NIGHT_ENHANZE_THUMB_PATH_DIR = "thumb_night_enhanze";
	public static final String NIGHT_ENHANZE_SAVE_PATH_DIR = "save_night_enhanze";
	public static final String PENCIL_SKETCH_IMAGE_PATH_DIR = "image_pencil_sketch";
	public static final String PENCIL_SKETCH_THUMB_PATH_DIR = "thumb_pencil_sketch";
	public static final String PENCIL_SKETCH_SAVE_PATH_DIR = "save_pencil_sketch";
	public static final String SHARPEN_IMAGE_PATH_DIR = "image_sharpen";
	public static final String SHARPEN_THUMB_PATH_DIR = "thumb_sharpen";
	public static final String SHARPEN_SAVE_PATH_DIR = "save_sharpen";
	public static final String TONE_MAPPING_IMAGE_PATH_DIR = "image_tone_mapping";
	public static final String TONE_MAPPING_THUMB_PATH_DIR = "thumb_tone_mapping";
	public static final String TONE_MAPPING_SAVE_PATH_DIR = "save_tone_mapping";
	public static final String UPSCALE_IMAGE_PATH_DIR = "image_upscale";
	public static final String UPSCALE_THUMB_PATH_DIR = "thumb_upscale";
	public static final String UPSCALE_SAVE_PATH_DIR = "save_upscale";
	public static String mStrSaveName = null;

	// 图像源相关
	public static String IMG_PATH_ABS;
	public static String THUMB_PATH_ABS;
	public static String SAVE_PATH_ABS;

	public static String thumbStoragePath;
	public static String pictureStoragePath;

	public static File getExternalStorage() {
		return Environment.getExternalStorageDirectory();
	}

	public static String getExternalStoragePath() {
		if (DEBUG) {
			Log.d(TAG, "getExternalStoragePictureDirectory = " + Environment.getExternalStorageDirectory().getAbsolutePath());
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static String getPictureStorageRootPath() {
		if (DEBUG) {
			Log.d(TAG, "getPictureStorageRootPath = " + getExternalStoragePath() + File.separator + IMAGE_ROOT);
		}
		return getExternalStoragePath() + File.separator + IMAGE_ROOT;
	}

	public static String getPictureStoragePath(String pictureDir) {
		if (DEBUG) {
			Log.d(TAG, "getPictureStoragePath = " + getPictureStorageRootPath() + File.separator + pictureDir);
		}
		return getPictureStorageRootPath() + File.separator + pictureDir;
	}

	public static String getThumbStoragePath(String thumbDir) {
		if (DEBUG) {
			Log.d(TAG, "getThumbStoragePath = " + getPictureStorageRootPath() + File.separator + thumbDir);
		}
		return getPictureStorageRootPath() + File.separator + thumbDir;
	}

	public static String getSaveStoragePath(String saveDir) {
		if (DEBUG) {
			Log.d(TAG, "getSaveStoragePath = " + getPictureStorageRootPath() + File.separator + saveDir);
		}
		return getPictureStorageRootPath() + File.separator + saveDir;
	}

	public static Uri addImage(ContentResolver contentresolver, String s, long l, Location location, int i, Bitmap bitmap) {
		if (!saveImageToStorage(s, bitmap))
			return null;
		if (i != 0)
			try {
				(new ExifInterface(s)).setAttribute("Orientation", String.valueOf(i));
			} catch (IOException ioexception) {
				ioexception.printStackTrace();
			}
		return insertImageToMediaStore(s, l, i, 0L, location, contentresolver);
	}

	private static Uri insertImageToMediaStore(String s, long l, int i, long l1, Location location, ContentResolver contentresolver) {
		String s1 = s.substring(s.lastIndexOf("/"), s.lastIndexOf("."));
		ContentValues contentvalues = new ContentValues(9);
		contentvalues.put("title", s1);
		contentvalues.put("_display_name", (new StringBuilder()).append(s1).append(".jpg").toString());
		contentvalues.put("datetaken", Long.valueOf(l));
		contentvalues.put("mime_type", "image/jpeg");
		contentvalues.put("orientation", Integer.valueOf(i));
		contentvalues.put("_data", s);
		contentvalues.put("_size", Long.valueOf(l1));
		if (location != null) {
			contentvalues.put("latitude", Double.valueOf(location.getLatitude()));
			contentvalues.put("longitude", Double.valueOf(location.getLongitude()));
		}
		Uri uri = contentresolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentvalues);
		if (uri == null) {
			Log.e("CameraStorage", "Failed to write MediaStore");
			uri = null;
		}
		return uri;
	}

	private static boolean saveImageToStorage(String s, Bitmap bitmap) {
		FileOutputStream fileoutputstream = null;
		try {
			fileoutputstream = new FileOutputStream(s);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(CompressFormat.JPEG, 100, fileoutputstream);
		try {
			if (null != fileoutputstream) {
				fileoutputstream.close();
			}
			return true;
		} catch (Exception exception4) {
			Log.e(TAG, "saveImageToStorage error");
			return false;
		}
	}

	// 把图片实时更新到手机图库中
	public static void saveImageToGallery(Context context, Bitmap bmp, String absPath) {
		// 首先保存图片
		File appDir = new File(absPath);
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = "IMAGE_" + System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(absPath + fileName))));
		mStrSaveName = absPath + fileName;
	}

	// OpenCV 把图片实时更新到手机图库中
	public static void saveImageToGalleryWithOpenCV(Context context, Bitmap bmp, String absPath) {
		// 首先保存图片
		File appDir = new File(absPath);
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = "IMAGE_" +  + System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		Mat out = new Mat(bmp.getHeight(), bmp.getWidth(), CvType.CV_8UC4);
		Utils.bitmapToMat(bmp, out);
		Imgproc.cvtColor(out, out, Imgproc.COLOR_RGBA2BGR);
		Highgui.imwrite(file.getPath(), out);
		mStrSaveName = absPath + fileName;
	}

}
