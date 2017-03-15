package com.ziv.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FileUtil {
	private static final String TAG = "FileUtil";
	private static final boolean DEBUG = true;

	private static ImageNameFilter sImageFilter = new ImageNameFilter(Util.IMAGE_3D_FILE);
	public static final FileComparator sFileComparator = new FileComparator();

	public static final String ASSERT_PATH = "examples";

	private static final String SYSTEM_PHOTO_PATH = "/sdcard/DCIM/Camera/";

	public static final String EXTRA_MODEL_NAME = "com.sensetime.image.tone_model_name";

	public static final String TONE_MODEL_DIR = "tone_all";
	
	public static final String TONE_MODEL_ABS="tone_all"+File.separator;
	

	public static void copyModeIfNeed(Context context, String modelName) {
		String path = getModelPath(modelName, context);
		if (path != null) {
			File modelFile = new File(path);
			InputStream in = null;
			OutputStream out = null;
			try {
				if (modelFile.exists())
					modelFile.delete();
				modelFile.createNewFile();
				in = context.getAssets().open(TONE_MODEL_DIR + File.separator + modelName);
				out = new FileOutputStream(modelFile);
				byte[] buffer = new byte[4096];
				int n;
				while ((n = in.read(buffer)) > 0) {
					out.write(buffer, 0, n);
				}
			} catch (IOException e) {
				e.printStackTrace();
				modelFile.delete();
			} finally {
				try {
					if (in != null)
						in.close();
					if (out != null)
						out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getModelPath(String modelName, Context context) {
		String path = null;
		File dataDir = context.getExternalFilesDir(null);
		if (dataDir != null) {
			path = dataDir.getAbsolutePath() + File.separator + TONE_MODEL_DIR + File.separator + modelName;
		}
		return path;
	}

	public static boolean initDir(String path) {
		File dirFile = new File(path);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return dirFile.mkdirs();
		}
		return true;
	}

	public static String getFileName(String path) {
		return new File(path).getName();
	}

	public static boolean existFile(String path) {
		return new File(path).exists();
	}

	public static boolean fileExist(String path) {
		File file = new File(path);
		return file.exists() && !file.isDirectory();
	}

	public static void saveFile(String filePath, byte[] data) {
		try {
			File file = new File(filePath);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String loadLastImage(String path) {
		List<String> imgList = loadImages(path);
		if (null == imgList || imgList.size() <= 0) {
			return null;
		}
		return imgList.get(imgList.size() - 1);
	}

	public static List<String> loadImages(String path) {
		List<String> imgList = new ArrayList<String>();
		File file = new File(path);
		if (!file.exists()&&!file.isDirectory()) {
			return imgList;
		}
		File[] files = file.listFiles(sImageFilter);
		List<File> allFiles = new ArrayList<File>();
		for (File img : files) {
			allFiles.add(img);
		}
		Collections.sort(allFiles, sFileComparator);
		for (File img : allFiles) {
			imgList.add(img.getAbsolutePath());
		}
		return imgList;
	}

	public static byte[] readFile(String fileName) {
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

	public static void createDirIfNoExist(String path) {
		if (null == path || "".equals(path)) {
			return;
		}
		File file = new File(path);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
	}

	public static void deleteFile(File file) {
		if (null == file) {
			return;
		}

		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			}
		}
	}

	public static void deleteFile(String path) {
		if (StringUtils.isEmpty(path)) {
			return;
		}
		deleteFile(new File(path));
	}

	public static String getSavePath() {
		String path = StorageUtil.pictureStoragePath;
		checkDirPath(path);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		return path + File.separator + "IMAGE_" + timeStamp + Util.IMAGE_JPG;
	}

	public static String genSystemCameraPhotoPath() {
		if (DEBUG) {
			Log.d(TAG, "genSystemCameraPhotoPath SYSTEM_PHOTO_PATH = " + SYSTEM_PHOTO_PATH);
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		checkDirPath(SYSTEM_PHOTO_PATH);
		return SYSTEM_PHOTO_PATH + "IMAGE_" + timeStamp + Util.IMAGE_JPG;
	}

	private static void checkDirPath(String path) {
		FileUtil.createDirIfNoExist(path);
	}

	public static void copyFilesToLocalIfNeed(String assertPath,String targetPath) {
		File pictureDir = new File(targetPath);
		if (!pictureDir.exists() || !pictureDir.isDirectory()) {
			pictureDir.mkdirs();
		}
		try {
			String[] fileNames = ContextHolder.getContext().getAssets().list(assertPath);
			Log.e("ziv","fileNames.length = " + fileNames.length);
			if (fileNames.length == 0)
				return;
			for (int i = 0; i < fileNames.length; i++) {
				File file = new File(targetPath+File.separator + fileNames[i]);
				if (file.exists() && file.isFile()){
					continue;
				}
				Log.e("ziv","copy file name = " + fileNames[i]);
				InputStream is = ContextHolder.getContext().getAssets().open(assertPath + File.separator + fileNames[i]);
				int size = is.available();
				byte[] buffer = new byte[size];
				is.read(buffer);
				is.close();
				String mypath = targetPath + File.separator+fileNames[i];
				FileOutputStream fop = new FileOutputStream(mypath);
				fop.write(buffer);
				fop.flush();
				fop.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	@SuppressWarnings("resource")
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			System.out.println("-------------oldPath" + oldfile.exists());
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[inStream.available()];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}

	public static void deleteUnusedFile() {
		try {
			String[] fileNames = ContextHolder.getContext().getAssets().list(ASSERT_PATH);
			if (fileNames == null || fileNames.length == 0) {
				return;
			}
			File file = new File(StorageUtil.IMG_PATH_ABS);
			String[] childs = file.list();
			if (childs == null) {
				return;
			}
			int count_assert = fileNames.length;
			int count = childs.length;
			String childName;
			for (int i = 0; i < count; i++) {
				childName = childs[i];
				for (int j = 0; j < count_assert; j++) {
					if (childName.equals(fileNames[j])) {
						continue;
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class FileComparator implements Comparator<File> {
		public int compare(File file1, File file2) {
			if (file1.lastModified() < file2.lastModified()) {
				return -1;
			} else {
				return 1;
			}
		}
	}

}
