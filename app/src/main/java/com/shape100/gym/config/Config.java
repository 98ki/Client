package com.shape100.gym.config;

import com.shape100.gym.MainApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author yupu
 * @date 2014年12月31日
 */
public class Config {

	private SharedPreferences mPreferences;

	public Config(String name) {
		mPreferences = MainApplication.sContext.getSharedPreferences(name,
				Context.MODE_PRIVATE);
	}

	public Editor edit() {
		return mPreferences.edit();
	}

	protected void commitBoolean(String key, boolean value) {
		Editor ed = mPreferences.edit();
		ed.putBoolean(key, value);
		ed.commit();
	}

	protected boolean getBoolean(String key, boolean defValue) {
		return mPreferences.getBoolean(key, defValue);
	}

	protected void commitInt(String key, int value) {
		Editor ed = mPreferences.edit();
		ed.putInt(key, value);
		ed.commit();
	}

	protected int getInt(String key, int defValue) {
		return mPreferences.getInt(key, defValue);
	}

	protected void commitLong(String key, long value) {
		Editor ed = mPreferences.edit();
		ed.putLong(key, value);
		ed.commit();
	}

	protected long getLong(String key, long defValue) {
		return mPreferences.getLong(key, defValue);
	}

	protected void commitString(String key, String value) {
		Editor ed = mPreferences.edit();
		ed.putString(key, value);
		ed.commit();
	}

	protected String getString(String key, String defValue) {
		return mPreferences.getString(key, defValue);
	}

	protected void commitFloat(String key, float value) {
		Editor ed = mPreferences.edit();
		ed.putFloat(key, value);
		ed.commit();
	}

	protected float getFloat(String key, float defValue) {
		return mPreferences.getFloat(key, defValue);
	}

}
