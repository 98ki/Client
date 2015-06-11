package com.shape100.gym.protocol;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Queue;

import org.apache.http.entity.StringEntity;

import com._98ki.util.FileUtils;
import com.shape100.gym.Logger;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.BlogData;
import com.shape100.gym.model.PicInfo;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

public class PicUploadConfirm extends HttpTask {
	private Logger log = Logger.getLogger("PicUpLoadConfirm.class");
	private static final String URL = "/media/upload/pic_confirm.json";
	private BlogData blogData;
	private int position;

	public PicUploadConfirm(BlogData blogData, int pos) {
		super(URL, null, null, true);
		this.blogData = blogData;
		position = pos;
		buildRequestData();
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
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_POST,
				url);
		auth.addParameter("pic_id", blogData.getUpLoaded().get(position)
				.getPic_id());
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
			// log.e("---------------picUploadConfirm----" +
			// FileUtils.getError(input));
			// FileUtils.writeString("图片第三部上传失败:statuscode:" + statusCode
			// + "---picUploadConfirm----" + FileUtils.getError(input)
			// + "\n");
			reportFailure(statusCode);
		} else {
			// FileUtils
			// .writeString("图片第三部上传成功-----------------PicUploadConfirm success.第"
			// + position + "张\n");
			log.e("------------------------------------PicUploadConfirm success.");
			blogData.getThirdPosition()[position] = 1;
			ArrayList<String> ins = ExtProtocolUtil
					.parseUploadPicConfirm(input);
			blogData.getUpLoaded().get(position).setPic_id(ins.get(0));
			blogData.getUpLoaded().get(position).setThumbnail_pic(ins.get(1));

			reportSuccess();
		}
	}

	private void reportSuccess() {
		// Message msg = Message.obtain();
		// msg.what = Event.PICUPLOADCONFIRM;
		// msg.obj = picInfo;
		// handler.sendMessage(msg);
		// System.out.println("-------------------上传成功-----第" + position + "张");
		int flag = 0;
		for (int i = 0; i < blogData.getUpLoaded().size(); i++) {
			if (blogData.getThirdPosition()[i] == 0) {
				flag = 1;
			} else {
				File file = new File(blogData.getUpLoaded().get(i)
						.getLocal_url());
				String[] imgpath = file.getAbsolutePath().split("/");
				if (file.exists() && imgpath.length >= 2
						&& imgpath[imgpath.length - 2].equals("imgcache")) {
					file.delete();
				}
			}
		}

		if (flag == 0) {
			ThreadPool.getInstance().execute(
					new UpdateWb(blogData.getStatus(), 0, blogData.getPicids(),
							"", blogData.getLat(), blogData.getLon(), blogData
									.getRip()));

			// new UpdateWb(blogData.getStatus(), 0, blogData.getPicids(), "",
			// blogData.getLat(), blogData.getLon(), blogData.getRip())
			// .start();
			long sb = System.currentTimeMillis()
					- AppConfig.getInstance().getTime();
			System.out
					.println("---------------------------------全部上传耗时------------"
							+ sb / 1000 + "." + sb % 1000 + "秒");
			// FileUtils.writeString("全部图片上传耗时------------" + sb / 1000 + "." +
			// sb
			// % 1000 + "秒\n");
		}
	}

	private void reportFailure(int resCode) {
		log.e("PicUploadConfirm failed----+rescode:" + resCode);
		// Message msg = Message.obtain();
		// msg.what = ConstVar.HANDLER_MSG_FAILURE;
		// msg.arg1 = resCode;
		// handler.sendMessage(msg);
	}

}
