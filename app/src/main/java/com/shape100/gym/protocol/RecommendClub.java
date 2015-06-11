package com.shape100.gym.protocol;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import android.os.Message;

import com._98ki.util.FileUtils;
import com.shape100.gym.BuildConfig;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.model.ClubBean;

public class RecommendClub extends HttpTask {
	private Logger log=Logger.getLogger("RecommendClub");
	private static final String URL = "/place/club/group/list.json";
	private ProtocolHandler handler;

	public RecommendClub(ProtocolHandler handler) {
		super(URL+"?club_group_id="+BuildConfig.CLUB_ID, null, handler);
		this.handler = handler;
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		if (statusCode != HttpTask.SC_OK) {
		//	log.e("------>"+FileUtils.getError(input));
			reportFailure(statusCode);
		} else {
			ArrayList<ClubBean> clubBeans = ExtProtocolUtil.parseClub(input);
			reportSuccess(clubBeans);
		}
	}

	private void reportSuccess(ArrayList<ClubBean> clubBeans) {
		Message msg = Message.obtain();
		msg.what = ConstVar.HANDLER_MSG_RESULT;
		msg.obj = clubBeans;
		handler.sendMessage(msg);
	}

	private void reportFailure(int errorCode) {
		Message msg = Message.obtain();
		msg.what = ConstVar.HANDLER_MSG_FAILURE;
		msg.arg1 = errorCode;
		handler.sendMessage(msg);
	}
}
