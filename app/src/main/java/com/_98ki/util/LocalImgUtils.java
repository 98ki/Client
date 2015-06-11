package com._98ki.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.shape100.gym.MainApplication;
import com.shape100.gym.model.FileFolder;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class LocalImgUtils {
	private ArrayList<String> allImgs;
	private ArrayList<FileFolder> fileFolders;
	private static LocalImgUtils util;

	private LocalImgUtils() {
		initImg();
	}

	public static LocalImgUtils getInstence() {
		if (util == null) {
			util = new LocalImgUtils();
		}
		return util;
	}

	/**
	 * 更新图库
	 * 
	 * @author yupu
	 * @date 2015年3月16日
	 */
	public void initImg() {
		allImgs = getAllImgDir();
		fileFolders = getFolder();
	}

	private ArrayList<String> getAllImgDir() {

		ArrayList<String> allImgs = new ArrayList<String>();
		String[] colums = { MediaStore.Images.Media.DATA,
				MediaStore.Images.Media._ID };

		// MediaStore.Images.Thumbnails.DATA; 系统缩略图的数据

		String orderBy = MediaStore.Images.Media._ID;
		Cursor imgCursor = MainApplication.sContext.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, colums, null,
				null, orderBy + " DESC");
		if (imgCursor != null && imgCursor.getCount() > 0) {
			while (imgCursor.moveToNext()) {
				int index = imgCursor
						.getColumnIndex(MediaStore.Images.Media.DATA);
				allImgs.add(imgCursor.getString(index));
			}
		}
		return allImgs;
	}

	private ArrayList<FileFolder> getFolder() {
		ArrayList<FileFolder> folders = new ArrayList<FileFolder>();
		Set<String> set = new HashSet<String>();

		// 去重，获取总共的文件夹名字
		for (int i = 0; i < allImgs.size(); i++) {
			set.add(getFolderName(allImgs.get(i)));
		}

		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			FileFolder folder = new FileFolder();
			String string = iterator.next();
			folder.setName(string);
			folders.add(folder);
		}

		// 设置每个文件夹下的所有图片
		for (int i = 0; i < folders.size(); i++) {
			ArrayList<String> contents = new ArrayList<String>();
			for (int j = 0; j < allImgs.size(); j++) {
				if (folders.get(i).getName()
						.equals(getFolderName(allImgs.get(j)))) {
					contents.add(allImgs.get(j));
				}
			}
			folders.get(i).setContent(contents);
		}
		return folders;
	}

	public ArrayList<String> getAllImgs() {
		return allImgs;
	}

	public ArrayList<FileFolder> getAllFolder() {
		return fileFolders;
	}

	private String getFolderName(String path) {
		String[] pth = path.split("/");
		return pth[pth.length - 2];
	}
}
