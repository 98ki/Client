package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.os.Message;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.model.ClubBean;

public class ClubNearby extends HttpTask {
	private static final Logger log = Logger.getLogger("ClubNearby");
	private static final String URL = "/place/club/nearby.json";
	private static final String SEARCHURL = "/place/club/search.json";

	public ClubNearby(ProtocolHandler handler, double lat, double lng,
			int page, String query) {
		super(buildUrl(lat, lng, page, query), null, handler, false);
	}

	private static String buildUrl(double lat, double lng, int page,
			String query) {
		log.d("ClubNearby buildUrl start");

		StringBuilder sb = new StringBuilder();
		if (query.equals("")) {
			sb.append(URL).append("?");
		} else {
			sb.append(SEARCHURL).append("?");
		}
		sb.append("lat=").append(lat);
		sb.append("&").append("long=").append(lng);
		sb.append("&").append("count=").append(50);
		sb.append("&").append("page=").append(page);
		try {
			sb.append("&").append("q=")
					.append(URLEncoder.encode(query, "utf8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		log.d("httpRspHandler in ClubNearby");
		reportSuccess(ExtProtocolUtil.parseClub(input));
	}

	private void reportSuccess(ArrayList<ClubBean> clubBeans) {
		if (mHandler != null) {
			Message msg = mHandler.obtainMessage();
			msg.obj = clubBeans;
			msg.what=ConstVar.HANDLER_MSG_SUCCESS;
			mHandler.sendMessage(msg);
		}
	}

	private void reportFailure() {

	}

}
