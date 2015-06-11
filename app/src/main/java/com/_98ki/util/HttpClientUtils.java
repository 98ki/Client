/**    
 * file name：HttpClientUtils.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-12-2 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com._98ki.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;

import com.shape100.gym.Logger;
import com.shape100.gym.protocol.ProtocolHandler;

/**
 * 
 * project：shape100 class：HttpClientUtils desc： author：zpy zpy@98ki.com create
 * date：2014-12-2 下午3:58:07 modify author: zpy update date：2014-12-2 下午3:58:07
 * update remark：
 * 
 * @version
 * 
 */
public abstract class HttpClientUtils extends Thread {
	private static final Logger log = Logger.getLogger("HttpClientUtils");

	protected static final int SC_OK = HttpStatus.SC_OK;
	protected static final int SC_UNAUTHORIZED = HttpStatus.SC_UNAUTHORIZED;
	/**
	 * Content-type Put aliyun Image
	 */
	protected static final String CONTENT_TYPE = "";
	/**
	 * Content-type Json
	 */
	// protected static final String CONTENT_TYPE =
	// "application/x-www-form-urlencoded; charset=UTF-8";

	protected static final String HOST = "oss-cn-beijing.aliyuncs.com";
	protected int PORT = 80;

	protected String mProxyAddr;
	protected int mProxyPort;

	private int mMaxRetry = 3;
	private int mRetry = 1;
	protected ProtocolHandler mHandler;
	private String mStrUrl;
	private AbstractHttpEntity mEntity;
	private String mMethod;
	private boolean mIsHttps;
	private String mContentType;

	private int mConnectionTimeout = 20 * 1000;
	private int mReadTimeout = 30 * 1000;

	private HttpClient mHttpClient = null;
	private HttpRequest mHttpRequest = null;
	private HttpHost mProxyHost = null;
	private HttpHost mTargetHost = null;

	public String mOAuthHeader;

	/**
	 * HTTP Post task which use the default APN.
	 * 
	 * @param strUrl
	 * @param entity
	 * @param handler
	 */

	public HttpClientUtils(String strUrl, AbstractHttpEntity entity,
			ProtocolHandler handler, String method) {
		init(strUrl, entity, handler, method, false);
	}

	public HttpClientUtils(String strUrl, AbstractHttpEntity entity,
			ProtocolHandler handler, String method, boolean bIsHttps) {
		init(strUrl, entity, handler, method, bIsHttps);
	}

	private void init(String strUrl, AbstractHttpEntity entity,
			ProtocolHandler handler, String method, boolean bIsHttps) {
		this.mStrUrl = strUrl;
		this.mEntity = entity;
		this.mHandler = handler;
		this.setPriority(MIN_PRIORITY);
		this.mMethod = method;
		this.mIsHttps = bIsHttps;

		log.d("URL:" + strUrl);
		if (entity != null) {
			try {
				log.d(EntityUtils.toString(entity));
			} catch (Exception e) {
				// Empty catch block
			}
		}

	}

	public void setEntity(AbstractHttpEntity entity) {
		this.mEntity = entity;
	}

	public void setHandler(ProtocolHandler handler) {
		this.mHandler = handler;
	}

	public void setConnectionTimeout(int value) {
		mConnectionTimeout = value;
	}

	public void setReadTimeout(int value) {
		mReadTimeout = value;
	}

	public void setMaxRetryCount(int value) {
		mMaxRetry = value;
	}

	public String getContentType() {
		return mContentType;
	}

	public void setContentType(String contentType) {
		this.mContentType = contentType;
	}

	@Override
	public void run() {
		try {
			startTask();
			prepareTask();
			runExecute();
			finishTask();
		} catch (Exception e) {
			doError();
		}
	}

	private void runExecute() {
		log.d("HttpTask runExecute in" + getClass().getSimpleName());

		try {
			initClient();
		} catch (Throwable t) {
			expInitClient(t);
			return;
		}

		HttpResponse rsp = null;
		try {
			rsp = executeClient();
		} catch (Exception e) {
			expExecuteClient(e);
			return;
		}

		try {
			parseClient(rsp);
		} catch (Exception e) {
			expParseClient(e);
			return;
		}

		if (Logger.DBG) {
			log.d("runExecute end.");
		}

		rsp = null;
		releaseClient();
	}

	private void initClient() {
		log.d("HttpTask initClient in " + getClass().getSimpleName());
		if (mIsHttps) {
			mHttpClient = HttpsUtil.getNewHttpClient();
		} else {
			mHttpClient = new DefaultHttpClient();
		}
		if (mProxyAddr != null) {
			mProxyHost = new HttpHost(mProxyAddr, mProxyPort);
			mHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					mProxyHost);
		}
		// 超时时间设置
		mHttpClient.getParams().setIntParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, mConnectionTimeout);
		mHttpClient.getParams().setIntParameter(
				CoreConnectionPNames.SO_TIMEOUT, mReadTimeout);

		log.d("HOST : " + HOST + ":" + PORT);

		mTargetHost = new HttpHost(HOST, PORT);

		if (mMethod.equals("post")) {
			mHttpRequest = new HttpPost(mStrUrl);
			((HttpPost) mHttpRequest).setEntity(mEntity);
		} else if (mMethod.equals("get")) {
			mHttpRequest = new HttpGet(mStrUrl);
		} else if (mMethod.equals("put")) {
			log.e("Put Url in Task :" + mStrUrl);
			mHttpRequest = new HttpPut(mStrUrl);
			((HttpPut) mHttpRequest).setEntity(mEntity);
			log.e("Put entity.length in Task :" + mEntity.getContentLength());
		}

		// mHttpRequest.setHeader("Content-Type", CONTENTJSON);
		mHttpRequest.setHeader("Content-Type", CONTENT_TYPE);
		if (!TextUtils.isEmpty(mOAuthHeader)) {
			if (Logger.DBG) {
				log.d("Authorization:" + mOAuthHeader);
			}
			// mHttpRequest.setHeader("Authorization", mOAuthHeader);
		}
	}

	private HttpResponse executeClient() throws IOException {
		if (Logger.DBG) {
			log.d("HttpTask executeClient in " + getClass().getSimpleName());
		}

		HttpResponse rsp = null;
		rsp = mHttpClient.execute(mTargetHost, mHttpRequest);
		return rsp;
	}

	private void parseClient(HttpResponse rsp) throws Exception {
		log.d("HttpTask parseClient in " + getClass().getSimpleName());
		log.d(rsp.getStatusLine().toString());

		HttpEntity httpRspEntity = rsp.getEntity();
		int code = rsp.getStatusLine().getStatusCode();
		httpRspHandler(code, httpRspEntity.getContent());
	}

	private void expInitClient(Throwable t) {
		log.d("expInitClient in " + getClass().getSimpleName());
		log.d("expInitClient: " + t.getLocalizedMessage());
		t.printStackTrace();

		doError();
	}

	private void expExecuteClient(Exception e) {
		if (Logger.DBG) {
			log.d("expExecuteClient in " + getClass().getSimpleName());
			log.d("expExecuteClient: " + e.getMessage() + ", try count:"
					+ mRetry);
			e.printStackTrace();
		}
		releaseClient();
		if (mRetry < mMaxRetry) {
			mRetry++;
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException exp) {
				exp.printStackTrace();
			}
			runExecute();
		} else {
			if (Logger.DBG) {
				log.d("expExecuteClient, call onError" + getClass().getName()
						+ "----->>>>" + (mHandler == null));
			}
			doError();
		}
	}

	private void expParseClient(Exception e) {
		if (Logger.DBG) {
			log.d("expParseClient in " + getClass().getSimpleName());
			log.d("expParseClient: " + e.getLocalizedMessage() + ", try count:"
					+ mRetry);
			e.printStackTrace();
		}
		releaseClient();
		if (mRetry < mMaxRetry) {
			mRetry++;
			runExecute();
		} else {
			doError();
		}
	}

	private void releaseClient() {
		if (mHttpClient != null) {
			mHttpClient.getConnectionManager().shutdown();
			mHttpClient = null;
			mHttpRequest = null;
			mProxyHost = null;
			mTargetHost = null;
		}
	}

	protected void prepareTask() throws Exception {
		if (Logger.DBG) {
			log.d("prepareTask in " + getClass().getSimpleName());
			log.d("HttpTask prepareTask");
		}

		if (Logger.DBG) {
			log.d("proxy : " + mProxyAddr);
		}
	}

	protected void finishTask() {
		if (Logger.DBG) {
			log.d("finishTask in " + getClass().getSimpleName());
			log.d("HttpTask finishTask");
		}
	}

	protected abstract void httpRspHandler(int statusCode, InputStream input)
			throws Exception;

	protected void startTask() {
	};

	protected void doError() {
		if (Logger.DBG) {
			log.d("HttpTask doError : " + getClass().getSimpleName());
		}
	};
}
