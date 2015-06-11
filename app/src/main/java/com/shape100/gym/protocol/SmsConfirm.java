package com.shape100.gym.protocol;

import java.io.InputStream;

import android.os.Message;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.protocol.ExtProtocolModel.ExtResponse;

public class SmsConfirm extends HttpTask {
	private static final Logger log = Logger.getLogger("SmsConfirm");
	private static final String URL = "/account/register/sms/confirm.json";

	public SmsConfirm(ProtocolHandler handler, String phone, String code) {
		super(buildUrl(phone, code), null, handler, false);
	}

	private static String buildUrl(String phone, String code) {
		StringBuilder sb = new StringBuilder();
		sb.append(URL).append("?");
		sb.append("phone=").append(phone);
		sb.append("&").append("code=").append(code);
		return sb.toString();
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		if (Logger.DBG) {
			log.d("httpRspHandler in SmsConfirm");
		}
		ExtResponse rsp = ExtProtocolUtil.parseResponse(input);
		if (rsp.mResult == 0) {
			reportSuccess();
		} else {
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
