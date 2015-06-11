package com.shape100.gym.model;

import java.util.ArrayList;
import java.util.jar.Pack200.Packer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片所在文件夹
 * 
 * @author yupu
 * @date 2015年1月29日
 */
public class FileFolder implements Parcelable {
	private String name; // 文件夹的名字
	private ArrayList<String> content;// 里面所有图片的路径

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getContent() {
		return content;
	}

	public void setContent(ArrayList<String> content) {
		this.content = content;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getName());
		dest.writeList(getContent());
	}

	public static final Parcelable.Creator<FileFolder> CREATOR = new Creator<FileFolder>() {

		@SuppressWarnings("unchecked")
		@Override
		public FileFolder createFromParcel(Parcel source) {
			FileFolder ff = new FileFolder();
			ff.name = source.readString();
			ff.content = source
					.readArrayList(FileFolder.class.getClassLoader());
			return ff;
		}

		@Override
		public FileFolder[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	};

}
