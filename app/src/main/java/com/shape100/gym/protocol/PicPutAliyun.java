package com.shape100.gym.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.entity.FileEntity;

import android.os.Message;
import android.widget.Toast;

import com._98ki.util.FileUtils;
import com._98ki.util.HttpClientUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.config.QueueData;
import com.shape100.gym.model.BlogData;
import com.shape100.gym.model.PicInfo;

public class PicPutAliyun extends HttpClientUtils {
	private Logger log = Logger.getLogger("PicPutAliyun");
	private BlogData blogData;
	private int position;

	public PicPutAliyun(BlogData blogData, int pos) {
		super(blogData.getUpLoaded().get(pos).getPut_url(), null, null, "put");
		this.blogData = blogData;
		position = pos;
		buildRequestData();
	}

	private void buildRequestData() {
		FileEntity fe;
		fe = new FileEntity(new File(blogData.getUpLoaded().get(position)
				.getLocal_url()), "multipart/form-data");
	//	FileUtils.writeString("图片大小:" + fe.getContentLength() + "\n");
		System.out.println("------------------pic--size----"
				+ fe.getContentLength());
		setEntity(fe);
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input)
			throws Exception {
		if (statusCode != HttpClientUtils.SC_OK) {
			// StringBuilder builder = new StringBuilder();
			// BufferedReader bufferedReader = new BufferedReader(
			// new InputStreamReader(input));
			//
			// for (String s = bufferedReader.readLine(); s != null; s =
			// bufferedReader
			// .readLine()) {
			// builder.append(s);
			// log.e("--PicPutAliyun---failed--->" + builder.toString());
			// }
			
		//	FileUtils.writeString("图片上传阿里云失败：statuscode:" + statusCode + "----"
		//			+ FileUtils.getError(input) + "\n");
			reportFailure(statusCode);

		} else {
			log.e("--------------------put aliyun pic: success" + position);
		//	FileUtils.writeString("图片上传阿里云成功：statuscode:" + statusCode + "\n");
			blogData.getSecondPosition()[position] = 1;
			ThreadPool.getInstance().execute(
					new PicUploadConfirm(blogData, position));

			int flag = 0;
			for (int i = 0; i < blogData.getSecondPosition().length; i++) {
				if (blogData.getSecondPosition()[i] == 0) {
					flag = 1;
				}
			}

			if (flag == 1) {
				ThreadPool.getInstance().execute(
						new PicPutAliyun(QueueData.getinstence().poll(),
								++position));
			}
			// new PicUploadConfirm(blogData, position).start();
		}
	}

	private void reportFailure(int resCode) {
		log.e("aliyun failed");
	}

}
