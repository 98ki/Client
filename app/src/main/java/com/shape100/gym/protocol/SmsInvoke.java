package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.os.Message;

import com._98ki.util.FileUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.protocol.ExtProtocolModel.ExtResponse;

public class SmsInvoke extends HttpTask {
	private static final Logger log = Logger.getLogger("SmsInvoke");
	private static final String URL = "/account/register/sms/invoke.json";

	public SmsInvoke(ProtocolHandler handler, String phoneNumber) {
		super(URL, buildRequestData(phoneNumber), handler);
	}

	private static StringEntity buildRequestData(String phoneNumber) {
		ArrayList<NameValuePair> pairlist = new ArrayList<NameValuePair>();
		pairlist.add(new BasicNameValuePair("phone", phoneNumber));

		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(pairlist, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			if (Logger.DBG) {
				log.d("buildRequestData:" + e.getLocalizedMessage());
			}
		}
		return entity;
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		if (Logger.DBG) {
			log.d("httpRspHandler in SmsInvoke");
		}

		ExtResponse rsp = ExtProtocolUtil.parseResponse(input);
		if (rsp.mResult == 0) {
			reportSuccess();
		} else {
		//	log.e("-------->" + FileUtils.getError(input));
			reportFailure(rsp.mResult);
		}
	}

	private void reportSuccess() {
		if (mHandler != null) {
			mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_SUCCESS);
		}
	}

	private void reportFailure(int result) {
		if (mHandler != null) {
			Message msg = new Message();
			msg.what = ConstVar.HANDLER_MSG_FAILURE;
			msg.arg1 = result;
			mHandler.sendMessage(msg);
		}
	}

	@Override
	protected void doError() {
		super.doError();
		if (mHandler != null) {
			mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_ERROR);
		}
	}

}
