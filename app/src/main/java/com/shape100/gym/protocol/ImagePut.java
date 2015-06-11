/**    
 * file name：ImagePut.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-12-2 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.protocol;

import java.io.File;
import java.io.InputStream;

import org.apache.http.entity.FileEntity;

import android.os.Message;

import com._98ki.util.FileUtils;
import com._98ki.util.HttpClientUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.MainName;
import com.shape100.gym.model.AccountDetailBean;

/**
 * 
 * project：shape100 class：ImagePut desc： put image to aliyun author：zpy
 * zpy@98ki.com create date：2014-12-2 下午4:15:39 modify author: zpy update
 * date：2014-12-2 下午4:15:39 update remark：
 * 
 * @version
 * 
 */
public class ImagePut extends HttpClientUtils {
	private static final Logger log = Logger.getLogger("ImagePut");
	private File mFile;
	private AccountDetailBean mAccountDetailBean;

	public ImagePut(ProtocolHandler handler, AccountDetailBean accountDetailBean) {
		super(accountDetailBean.getProfileImageUrl(), null, handler, "put");
		mAccountDetailBean = accountDetailBean;
		buildRequestData();
	}

	private void buildRequestData() {
		FileEntity fe;
		log.d("put data start!");
		// MultipartEntity ecEntity=new MultipartEntity();
		fe = new FileEntity(new File(MainApplication.getInstance()
				.getImageCacheDir() + MainName.HEAD_LARGE_JPG), "");
		setEntity(fe);
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		if (Logger.DBG) {
			log.d("httpRspHandler in ImagePut");
		}

		if (statusCode != HttpTask.SC_OK) {
			reportFailure(statusCode);
		} else {
			log.e("image put json input:" + FileUtils.getError(input));
			// success
			if (mHandler != null) {
				Message msg = new Message();
				msg.what = ConstVar.HANDLER_MSG_CONTINUE1;
				mHandler.sendMessage(msg);
			}
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
