package com.shape100.gym.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.model.ClubBean;
import com.shape100.gym.provider.DBConst.ClubColumns;

/**
 * Club Table
 * 
 * @author zpy zpy@98ki.com
 * @data 2014-10-29 下午1:43:34
 * @version: V1.01
 */
public class ClubUtil {
	static SQLiteDatabase db;
	private static final Logger log = Logger.getLogger("ClubUtil");

	
	
	/**
	 * Get Club by id
	 * 
	 * @throw
	 * @return ClubBean
	 */
	public static ClubBean getClubById(int id) {
		
		String sql = ClubColumns.CLUB_ID + "=" + id;
		
		ClubBean bean = new ClubBean();
		Cursor c = null;
		try {
			DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
			SQLiteDatabase db = dbHelper.getReadableDatabase();

			c = db.query(DBConst.TABLE_CLUB, null, sql, null, null, null, null);
			if (c.moveToNext()) {
		 		bean.setId(id);
				bean.setName(c.getString(c.getColumnIndex(ClubColumns.NAME)));
				bean.setAddress(c.getString(c.getColumnIndex(ClubColumns.ADDRESS)));
				bean.setCity(c.getString(c.getColumnIndex(ClubColumns.CITY)));
				bean.setPhone(c.getString(c.getColumnIndex(ClubColumns.PHONE)));
				bean.setEmail(c.getString(c.getColumnIndex(ClubColumns.EMAIL)));
				bean.setBusinessHours(c.getString(c.getColumnIndex(ClubColumns.BUSINESSHOURS)));
				bean.setHomepageUrl(c.getString(c.getColumnIndex(ClubColumns.HOMEPAGEURL)));
				bean.setStartUpPicUrls(c.getString(c.getColumnIndex(ClubColumns.STARTUPICURLS)));
				bean.setLogoUrl(c.getString(c.getColumnIndex(ClubColumns.LOGOURL)));
				bean.setPicUrls(c.getString(c.getColumnIndex(ClubColumns.PICURLS)));
				bean.setDescription(c.getString(c.getColumnIndex(ClubColumns.DESCRIPTION)));
				bean.setLon(c.getString(c.getColumnIndex(ClubColumns.LON)));
				bean.setLat(c.getString(c.getColumnIndex(ClubColumns.LAT)));
		
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
			log.e(bean.getName()+","+bean.getCity());
		}

		return bean;
	}
	/**
	 * save my club is this. if not login, to login page.
	 * else save club and to club page.
	 * @throw 
	 * @return void
	 */
	public static void saveClub(ClubBean bean) {
		DatabaseHelper dbHelper = DatabaseHelper.getInstance(MainApplication.sContext);
		db = dbHelper.getReadableDatabase();
		db.delete("club", null, null);

		
		
		// set my club is club.getId(),return Boolean
		if(AccountDetailUtil.joinClub(bean.getId())){
			ContentValues cv = new ContentValues();
			cv.put(ClubColumns.CLUB_ID, bean.getId());
			cv.put(ClubColumns.NAME, bean.getName());
			cv.put(ClubColumns.ADDRESS, bean.getAddress());
			cv.put(ClubColumns.CITY, bean.getCity());
			cv.put(ClubColumns.PHONE, bean.getPhone());
			cv.put(ClubColumns.EMAIL, bean.getEmail());
			cv.put(ClubColumns.LOGOURL, bean.getLogoUrl());
			cv.put(ClubColumns.BUSINESSHOURS, bean.getBusinessHours());
			cv.put(ClubColumns.HOMEPAGEURL, bean.getHomepageUrl());
			cv.put(ClubColumns.STARTUPICURLS, bean.getStartUpPicUrls());
			cv.put(ClubColumns.PICURLS, bean.getPicUrls());
			cv.put(ClubColumns.DESCRIPTION, bean.getDescription());
			cv.put(ClubColumns.LON, bean.getLon());
			cv.put(ClubColumns.LAT, bean.getLat());
			
			db.insert(DBConst.TABLE_CLUB, null, cv);
			log.d("club saved");
		}else{
			// to login page.
			
		}
	}


}