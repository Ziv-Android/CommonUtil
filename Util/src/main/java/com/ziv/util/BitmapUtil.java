package com.ziv.util;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.TypedValue;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class BitmapUtil {

	/**
	 * Used to load bitmap from path
	 * 
	 * @param path
	 * @param reqWidth
	 * @param reqHeight
	 * @return Bitmap
	 */
	public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {
		final Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	/**
	 * Used to calculate inSimpleSize value of BitmapFactpry;
	 *
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return int
	 */
	public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
		if (reqWidth == 0 || reqHeight == 0) {
			return 1;
		}
		final int height = options.outHeight;
		final int width = options.outWidth;
		// Log.d(TAG, "origin, w= " + width + " h=" + height);
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}
		// printLog("sampleSize:" + inSampleSize);
		return inSampleSize;
	}

	/**
	 * @param drawable
	 *            drawable 转 Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * @param bitmap
	 * @param roundPx
	 *            获取圆角图片
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap createImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		Options options = new Options();
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false;
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	public static void saveBitmap(String savePath, Bitmap bitmap) {
		if (null == bitmap)// 容错处理
			return;
		File f = new File(savePath);
		FileOutputStream out = null;
		try {
			f.createNewFile();
			out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveBitmapWithOpenCV(String path, Bitmap bmp, MatOfInt params) {
		File file = new File(path);
		Mat out = new Mat(bmp.getHeight(), bmp.getWidth(), CvType.CV_8UC4);
		Utils.bitmapToMat(bmp, out);
		Imgproc.cvtColor(out, out, Imgproc.COLOR_RGBA2BGR);
		if (params != null) {
			Highgui.imwrite(file.getPath(), out, params);
		} else {
			Highgui.imwrite(file.getPath(), out);
		}

	}

	public static Bitmap loadScaleBitmapFromPath(String path) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		int scaleX = opts.outWidth / 1080;
		int scaleY = opts.outHeight / 1080;
		int scale = scaleX > scaleY ? scaleX : scaleY;
		if (scale < 1)
			scale = 1;
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = scale;
		return BitmapFactory.decodeFile(path, opts);
	}

	public static Bitmap createScaleBitmap(Bitmap bitmap, int maxW) {
		if (bitmap == null || bitmap.isRecycled()) {
			return null;
		}
		if (bitmap.getWidth() < maxW) {
			return bitmap;
		}
		float scale = bitmap.getWidth() / (float) maxW;
		Bitmap result = Bitmap.createScaledBitmap(bitmap, maxW, (int) (bitmap.getHeight() / scale), true);
		bitmap.recycle();
		bitmap = null;
		return result;
	}

	public static Bitmap loadScaleBitmapFromData(byte[] data) {
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		int scaleX = opts.outWidth / 1080;
		int scaleY = opts.outHeight / 1080;
		int scale = scaleX > scaleY ? scaleX : scaleY;
		if (scale < 1)
			scale = 1;
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = scale;
		return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	}

	public static byte[] floatArray2ByteArray(float floatArray[]) {
		if (null == floatArray) {
			return null;
		}
		byte byteArray[] = new byte[floatArray.length * 4];
		ByteBuffer byteBuf = ByteBuffer.wrap(byteArray);
		FloatBuffer floatBuf = byteBuf.asFloatBuffer();
		floatBuf.put(floatArray);
		return byteArray;
	}

	public static float[] byteArray2FloatArray(byte byteArray[]) {
		if (null == byteArray) {
			return null;
		}
		float floatArray[] = new float[byteArray.length / 4];
		ByteBuffer byteBuf = ByteBuffer.wrap(byteArray);
		FloatBuffer floatBuf = byteBuf.asFloatBuffer();
		floatBuf.get(floatArray);
		return floatArray;
	}

	public static void saveJpegData(String path, byte[] data, int rotation) {
		try {
			Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
			Matrix matrix = new Matrix();
			matrix.reset();
			matrix.postRotate(rotation);
			bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			File file = new File(path);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			recycle(bmp);
			data = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap loadBitmapFromPath(String path) {
		File imgFile = new File(path);
		if (imgFile.exists()) {
			Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			return bmp;
		}
		return null;
	}

	public static byte[] readFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists() || file.isDirectory()) {
			return null;
		}
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			fin.close();
			return buffer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static RectF caculateFitinSize(int i, int j, int k, int l) {
		RectF rectf = new RectF();
		if (i * l > k * j) {
			rectf.top = 0.0F;
			rectf.bottom = j;
			int j1 = (j * k) / l;
			rectf.left = (i - j1) / 2;
			rectf.right = rectf.left + (float) j1;
			return rectf;
		} else {
			rectf.left = 0.0F;
			rectf.right = i;
			int i1 = (i * l) / k;
			rectf.top = (j - i1) / 2;
			rectf.bottom = rectf.top + (float) i1;
			return rectf;
		}
	}

	public static void recycle(Bitmap b) {
		if (null == b || b.isRecycled()) {
			return;
		}
		b.recycle();
		b = null;
	}

	public static boolean isEmtyBitmap(Bitmap bitmap) {
		if (bitmap == null || bitmap.isRecycled()) {
			return true;
		}
		return false;
	}

	/**
	 * 转换图片成圆形
	 *
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2 - 5;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2 - 5;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst_left + 3, dst_top + 3, dst_right - 3, dst_bottom - 3);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 *
	 * @param bitmap
	 *            原图
	 * @param edgeLength
	 *            希望得到的正方形部分的边长
	 * @return 缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
		if (null == bitmap || edgeLength <= 0) {
			return null;
		}
		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		if (widthOrg > edgeLength && heightOrg > edgeLength) {
			// 压缩到一个最小长度是edgeLength的bitmap
			int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
			int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
			int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
			Bitmap scaledBitmap;

			try {
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
			} catch (Exception e) {
				return null;
			}

			// 从图中截取正中间的正方形部分。
			int xTopLeft = (scaledWidth - edgeLength) / 2;
			int yTopLeft = (scaledHeight - edgeLength) / 2;

			try {
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
				scaledBitmap.recycle();
			} catch (Exception e) {
				return null;
			}
		}

		return result;
	}

	/**
	 * 按正方形裁切图片
	 */
	public static Bitmap imageCrop(Bitmap bitmap) {
		if (null == bitmap) {
			return null;
		}
		int w = bitmap.getWidth(); // 得到图片的宽，高
		int h = bitmap.getHeight();

		int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

		int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
		int retY = w > h ? 0 : (h - w) / 2;

		// 下面这句是关键
		return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
	}

	public static Bitmap decodeResource(Resources resources, int id) {
		TypedValue value = new TypedValue();
		resources.openRawResource(id, value);
		Options opts = new Options();
		opts.inTargetDensity = value.density;
		return BitmapFactory.decodeResource(resources, id, opts);
	}

	/**
	 * 读取图片属性：旋转的角度
	 *
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
				degree = 0;
				break;
			case ExifInterface.ORIENTATION_FLIP_VERTICAL:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_TRANSPOSE:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_TRANSVERSE:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return degree;
	}

	/**
	 * 旋转图片跟缩放图片
	 *
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotateAndScaleImageView(int angle, Bitmap bitmap, float scale) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		matrix.postScale(scale, scale);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 旋转图片
	 *
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotateImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// matrix.postScale(scale, scale);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	public static String uri2FilePath(Context context, Uri uri) {
		if (uri == null) {
			return null;
		}
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, proj, // Which
																		// columns
																		// to
				// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	public static String uri2Path(Context context, Uri fileUrl) {
		String fileName = null;
		Uri filePathUri = fileUrl;
		if (fileUrl != null) {
			if (fileUrl.getScheme().toString().compareTo("content") == 0) {
				Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					fileName = cursor.getString(column_index);
					if (fileName.startsWith("/mnt/storage/emulated/0")) {
						fileName = fileName.replace("/mnt/storage/emulated/0", "/sdcard");
					}
					cursor.close();
				}
			} else if (fileUrl.getScheme().compareTo("file") == 0) {
				fileName = filePathUri.toString();
				fileName = filePathUri.toString().replace("file://", "");
			}

		}
		System.out.println("-----------fileName" + fileName);
		return fileName;
	}

	public static int getOrientation(Context context, Uri photoUri) {
		int orientation = 0;
		Cursor cursor = context.getContentResolver().query(photoUri, new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
		if (cursor != null) {
			if (cursor.getCount() != 1) {
				return -1;
			}
			cursor.moveToFirst();
			orientation = cursor.getInt(0);
			cursor.close();
		}
		return orientation;
	}

	public static int getOrientationFromMedia(Context context, Uri photoUri) {
		String[] imgs = { MediaStore.Images.Media.ORIENTATION };
		Cursor cursor = context.getContentResolver().query(photoUri, imgs, null, null, null);
		cursor.moveToFirst();
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION);
		int roate = cursor.getInt(index);
		try {
			cursor.close();
		} catch (Exception e) {

		}
		return roate;
	}

	public static Bitmap readBitmapWithOpenCV(String path) {
		Bitmap bm = null;
		File file = new File(path);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] byte_buffer = new byte[fis.available()];
			int file_len = fis.read(byte_buffer);
			Mat buffer = new Mat(file_len, 1, CvType.CV_8UC1);
			buffer.put(0, 0, byte_buffer);
			Mat input = Highgui.imdecode(buffer, Highgui.CV_LOAD_IMAGE_COLOR);
			Imgproc.cvtColor(input, input, Imgproc.COLOR_BGR2RGBA);
			bm = Bitmap.createBitmap(input.cols(), input.rows(), Config.ARGB_8888);
			Utils.matToBitmap(input, bm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return bm;
	}

	/**
	 * 边界压缩参数options确定
	 * @return options
	 */
	public static Options setBitmapOption(String imagePath,int width, int height) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		// 度量图片的实际宽度和高度
		BitmapFactory.decodeFile(imagePath, options);
		int outWidth = options.outWidth;
		int outHeight = options.outHeight;
		options.inDither = false;
		// 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道
		//options.inPreferredConfig = Bitmap.Config.RGB_565;
		// 设置缩放比,1表示原比例，2表示原来的四分之一
		options.inSampleSize = 1;

		if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
			int sampleSize = (outWidth / width + outHeight / height) << 1;
			options.inSampleSize = sampleSize;
		}
		// 复原标志
		options.inJustDecodeBounds = false;
		return options;
	}

}
