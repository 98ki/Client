package com.shape100.gym.protocol;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Queue;

import org.apache.http.entity.StringEntity;

import android.os.Message;
import android.util.LruCache;

import com._98ki.util.FileUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.config.QueueData;
import com.shape100.gym.model.BlogData;
import com.shape100.gym.model.PicInfo;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

public class PicUpload extends HttpTask {
	private Logger log = Logger.getLogger("PicUpload.class");
	private static final String URL = "/media/upload/pic.json";
	private PicInfo picInfo;
	private BlogData blogData;
	private int position = 0;

	public PicUpload(BlogData blogData, int position) {
		super(URL + "?pic="
				+ blogData.getUpLoaded().get(position).getLocal_url(), null,
				null, false);
		this.blogData = blogData;
		this.position = position;
		buildRequestData();
	}

	private static String buildUrl(BlogData data, int pos) {
		String url = data.getUpLoaded().get(pos).getLocal_url();
		String name[] = url.split(url);
		StringBuffer sb = new StringBuffer();
		sb.append(URL).append("?pic=").append(name[name.length - 1]);
		return sb.toString();

	}

	private void buildRequestData() {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}

		String url = "http://" + HOST + URL;
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_GET, url);
		auth.addParameter("pic", blogData.getUpLoaded().get(position)
				.getLocal_url());
		auth.doSign();
		mOAuthHeader = auth.getAuthHeader();

		StringEntity se;
		try {
			String data = auth.getRequestData();
			if (Logger.DBG) {
				log.d("post data : " + data);
			}
			se = new StringEntity(data);
			setEntity(se);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		if (statusCode != HttpTask.SC_OK) {
			// log.e("---------------picupload----" +
			// FileUtils.getError(input));
	//		FileUtils.writeString("图片上传错误信息:statuscode:" + statusCode
	//				+ "----picupload----" + FileUtils.getError(input) + "\n");
			reportFailure(statusCode);
		} else {
			log.e("---------------picupload---success--" + position);
	//		FileUtils.writeString("图片上传成功信息--picupload---success--" + position
	//				+ "\n");
			// reportSuccess();
			ArrayList<String> responses = ExtProtocolUtil.parseUploadPic(input);
			blogData.getUpLoaded().get(position).setPic_id(responses.get(0));
			blogData.getUpLoaded().get(position).setPut_url(responses.get(1));
			blogData.getPic_ids().add(responses.get(0));
			blogData.getFirstPosition()[position] = 1;
			if (position == 0) {
				blogData.setPicids(responses.get(0));
			} else {
				blogData.setPicids(blogData.getPicids() + ","
						+ responses.get(0));
			}

			QueueData.getinstence().offer(blogData);

			/*
			 * ThreadPool.getInstance().execute( new PicPutAliyun(blogData,
			 * position));
			 */

			// new PicPutAliyun(blogData, position).start();

			position++;
			if (blogData.getUpLoaded() != null
					&& blogData.getUpLoaded().size() != blogData.getPic_ids()
							.size()) {
				ThreadPool.getInstance().execute(
						new PicUpload(blogData, position));

				// new PicUpload(blogData, position).start();
			} else {
				ThreadPool.getInstance().execute(
						new PicPutAliyun(QueueData.getinstence().poll(), 0));
			}
		}
	}

	private void reportFailure(int resultCode) {
		log.e("picupload  failed");
	}
}
