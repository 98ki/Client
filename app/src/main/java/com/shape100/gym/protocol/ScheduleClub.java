package com.shape100.gym.protocol;

import java.io.InputStream;

import android.os.Message;

import com._98ki.util.FileUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;

/**
 * 
 * Http Task to get Club Schedule.
 * 
 * @author zpy zpy@98ki.com
 * @data 2014-10-28 下午9:11:24
 * @version: V1.0
 */
public class ScheduleClub extends HttpTask {
	private static final Logger log = Logger.getLogger("ScheduleClub");
	private static final String URL = "/schedule/club.json";
	private int mClubId;

	public ScheduleClub(ProtocolHandler handler, int clubId, String startDate,
			String endDate) {
		super(buildUrl(clubId, startDate, endDate), null, handler, false);
		mClubId = clubId;
	}

	private static String buildUrl(int clubId, String startDate, String endDate) {
		StringBuilder sb = new StringBuilder();
		sb.append(URL).append("?");
		sb.append("club_id=").append(clubId).append("&start_date=")
				.append(startDate).append("&end_date=").append(endDate);
		return sb.toString();
	}

	/**
	 * Http response stream handler.
	 */
	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		if (Logger.DBG) {
			log.d("httpRspHandler in ScheduleClub");
		}
		if (statusCode != SC_OK) {
			reportFailure(statusCode);
		} else {
			// [update native Database] clear course by club Id
			// CourseUtil.clearCourseByClubId(mClubId);
			// Save schedule to DB and return list
			ExtProtocolUtil.parseClubSchedule(input);
			reportSuccess();
		}
	}

	/**
	 * 
	 * report HANDLER_MSG_ERROR
	 * 
	 * @throw
	 * @return void
	 */
	private void reportFailure(int result) {
		if (mHandler != null) {
			Message msg = new Message();
			msg.what = ConstVar.HANDLER_MSG_FAILURE;
			msg.arg1 = result;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 
	 * report HANDLER_MSG_SUCCESS
	 * 
	 * @throw
	 * @return void
	 */
	private void reportSuccess() {
		if (mHandler != null) {
			mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_SUCCESS);
		}
	}

}
