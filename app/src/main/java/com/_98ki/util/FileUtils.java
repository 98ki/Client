package com._98ki.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

import android.os.Environment;
import android.util.Log;

import com.shape100.gym.Logger;

public class FileUtils {
	private static final int BUFFER_SIZE = 4 * 1024;

	private static final Logger log = Logger.getLogger("FileUtils");

	/**
	 * 获取SD卡路径
	 */
	public static String getSDPAHT() {
		return Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 获取缓存路径
	 * 
	 * @author yupu
	 * @date 2015年3月25日
	 */

	public static String getImagePath() {
		return getSDPAHT() + "shape100/imgcache/";
	}

	public static String getSaveImgPath() {
		return getSDPAHT() + "shape100/shape100/";
	}

	public static String getJsonPath() {
		return getSDPAHT() + "shape100/json/";
	}

	/**
	 * 在SD卡上创建文件
	 */
	public static File createFile(String dirName) {
		String path = getSDPAHT() + dirName;
		File dir = new File(path);
		dir.mkdirs();
		Log.e("FileUtils", path);
		return dir;
	}

	/**
	 * 创建缓存文件夹
	 * 
	 * @author yupu
	 * @date 2015年3月26日
	 */
	public static void createCacheFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 判断sd卡是否可用
	 * 
	 * @author yupu
	 * @date 2015年3月17日
	 */
	public static boolean isSDcardExist() {
		String sdStore = Environment.getExternalStorageState();
		if (sdStore.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public static boolean isFileExist(String fileName) {
		String path = getSDPAHT() + fileName;
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 将InputStream里面的数据写入到SD卡中
	 */
	public static File saveFile(String path, String fileName,
			InputStream inputStream, int fileSize) {
		if (!isFileExist(path)) {
			createFile(path);
		}
		File file = null;
		// 定义一个输出流，用来写数据
		OutputStream outputStream = null;
		// 创建文件夹
		createFile(path);
		// 保存文件
		file = createFile(path + fileName);
		try {
			// 构造一个新的文件输出流写入文件
			outputStream = new FileOutputStream(file);
			// 创建一个缓存区
			byte[] buffer = new byte[fileSize];
			// 从输入流中读取的数据存入缓存区
			while ((inputStream.read(buffer)) != -1) {
				// 将缓存区的数据写入输出流
				outputStream.write(buffer);
			}
			// 刷新流，清空缓冲区数据
			outputStream.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file;
	}

	public static byte[] readFromFile(String fileName, int offset, int len) {
		if (fileName == null) {
			return null;
		}

		File file = new File(fileName);
		if (!file.exists()) {
			log.d("readFromFile: file not found");
			return null;
		}

		if (len == -1) {
			len = (int) file.length();
		}

		log.d("readFromFile : offset = " + offset + " len = " + len
				+ " offset + len = " + (offset + len));

		if (offset < 0) {
			log.e("readFromFile invalid offset:" + offset);
			return null;
		}
		if (len <= 0) {
			log.e("readFromFile invalid len:" + len);
			return null;
		}
		if (offset + len > (int) file.length()) {
			log.e("readFromFile invalid file len:" + file.length());
			return null;
		}

		byte[] b = null;
		try {
			RandomAccessFile in = new RandomAccessFile(fileName, "r");
			b = new byte[len]; // 创建合适文件大小的数组
			in.seek(offset);
			in.readFully(b);
			in.close();

		} catch (Exception e) {
			log.e("readFromFile : errMsg = " + e.getMessage());
			e.printStackTrace();
		}
		return b;
	}

	public static byte[] getHtmlByteArray(final String url) {
		URL htmlUrl = null;
		InputStream inStream = null;
		try {
			htmlUrl = new URL(url);
			URLConnection connection = htmlUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] data = inputStreamToByte(inStream);

		return data;
	}

	public static byte[] inputStreamToByte(InputStream is) {
		try {
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 删除文件夹以及子文件目录
	 * 
	 * @author yupu
	 * @date 2015年3月26日
	 */
	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] subFiles = file.listFiles();
				for (File subFile : subFiles) {
					deleteFile(subFile);
				}
			}
			file.delete();
		}
	}

	/**
	 * 删除子文件
	 * 
	 * @author yupu
	 * @date 2015年3月26日
	 */
	public static void deleteSubFiles(File dir) {
		if (dir.exists() && dir.isDirectory()) {
			File[] subFiles = dir.listFiles();
			for (File subFile : subFiles) {
				deleteFile(subFile);
			}
		}
	}

	public static byte[] readFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream output = new ByteArrayOutputStream(BUFFER_SIZE);
		IOUtils.copy(fis, output);
		fis.close();
		return output.toByteArray();
	}

	public static void writeFile(String path, byte[] data, boolean append)
			throws IOException {
		FileOutputStream output = new FileOutputStream(path, append);
		ByteArrayInputStream input = new ByteArrayInputStream(data);
		IOUtils.copy(input, output);
		output.close();
	}

	/**
	 * 文件log测试
	 * 
	 * @author yupu
	 * @date 2015年3月31日
	 */
	public static void writeString(String log) {
		String pa = getSDPAHT() + "log.txt";
		createFile(pa);
		try {
			writeFile(pa, log.getBytes(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取返回数据
	 * 
	 * @author yupu
	 * @date 2015年1月26日
	 */
	public static String getError(InputStream inpu) {
		InputStream input = inpu;
		StringBuilder sb = null;
		try {
			sb = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			System.out.println("-----------------获取返回信息出错---");
		}
		return sb.toString();
	}

}