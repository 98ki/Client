package com.shape100.gym;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import android.os.Environment;

public class Log extends Logger {

	private static final String APP_TAG = "Shape100";
	private static final String LOG_FILE_NAME = "shape100.txt";
	private static PrintStream logStream;
	private static final String LOG_ENTRY_FORMAT = "[%tF %tT]%s";

	public Log(String name) {
		super(name);
	}

	@Override
	protected void debug(String str) {
		if (Log.DBG) {
			android.util.Log.w(APP_TAG, str);
		}
		write(str, null);
	}

	@Override
	protected void error(String str) {
		android.util.Log.e(APP_TAG, str);
		write(str, null);
	}

	@Override
	protected void info(String str) {
		android.util.Log.i(APP_TAG, str);
		write(str, null);
	}

	@Override
	protected void warn(String str) {
		android.util.Log.w(APP_TAG, str);
		write(str, null);
	}

	@Override
	protected void debug(String str, Throwable tr) {
		android.util.Log.d(APP_TAG, str);
		write(str, tr);
	}

	@Override
	protected void error(String str, Throwable tr) {
		android.util.Log.e(APP_TAG, str);
		write(str, tr);
	}

	@Override
	protected void info(String str, Throwable tr) {
		android.util.Log.e(APP_TAG, str);
		// android.util.Log.i(APP_TAG, str);
		write(str, tr);
	}

	@Override
	protected void warn(String str, Throwable tr) {
		android.util.Log.e(APP_TAG, str);
		// android.util.Log.w(APP_TAG, str);
		write(str, tr);
	}

	private void write(String msg, Throwable tr) {
		if (!Log.DBG) {
			return;
		}
		try {

			if (null == logStream) {
				synchronized (Log.class) {
					if (null == logStream) {
						init();
					}
				}
			}

			Date now = new Date();
			if (null != logStream) {
				logStream.printf(LOG_ENTRY_FORMAT, now, now, msg);
				logStream.print("\n");
			}
			if (null != tr) {
				tr.printStackTrace(logStream);
				if (null != logStream) {
					logStream.print("\n");
				}
			}

		} catch (Throwable t) {
			// Empty catch block
		}
	}

	public static void init() {
		if (!Log.DBG) {
			return;
		}
		try {
			File sdRoot = null;
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				sdRoot = Environment.getExternalStorageDirectory();
			}
			if (sdRoot != null) {
				File logFile = new File(sdRoot, LOG_FILE_NAME);
				
				android.util.Log.d(APP_TAG, "Log to file : " + logFile);
				logStream = new PrintStream(new FileOutputStream(logFile, true), true);
			}
		} catch (Throwable e) {
			// Empty catch block
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			super.finalize();
			if (logStream != null) {
				logStream.close();
			}
		} catch (Throwable t) {
			// Empty catch block
		}
	}
}
