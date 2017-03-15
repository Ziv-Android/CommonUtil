package com.ziv.util;

import java.io.File;
import java.io.FilenameFilter;

public class ImageNameFilter implements FilenameFilter {
	private String mNameFilter ;
	public ImageNameFilter(String filter){
		mNameFilter = filter.toLowerCase();
	}
	@Override
	public boolean accept(File dir, String pathname) {
        String filename = pathname.toLowerCase(); 
		if (filename.endsWith(mNameFilter + Util.IMAGE_JPG) || filename.endsWith(mNameFilter + Util.IMAGE_JPEG)) {//"_master.jpeg"
			return true;
		} 
		return false;
	}
}