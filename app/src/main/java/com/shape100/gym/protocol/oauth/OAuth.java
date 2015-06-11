package com.shape100.gym.protocol.oauth;

import java.util.List;

import android.text.TextUtils;

import com.shape100.gym.Logger;
import com.shape100.gym.protocol.utils.HMACSHA1;

public class OAuth {
	private static final Logger log = Logger.getLogger("OAuth");

	public static final String APP_KEY = "fcb7795ebffca4bb3f834c2e054d9349";
	private static final String APP_SECRET = "0dcb000c608e5111e79fa5a506095a05"; // 正式

	// public static final String APP_KEY = "df6e585e70127f972572a9ea2b4951ff";
	// private static final String APP_SECRET =
	// "de507eeac34ab2bc3e063bb5194d6097"; // 测试

	private String mTokenSecret;
	private String mVerb;
	private String mUrl;

	private ParameterList mParamList = new ParameterList();

	public OAuth(String token, String tokenSecret, String verb, String url) {
		mTokenSecret = TextUtils.isEmpty(token) ? "" : tokenSecret;
		mVerb = verb;
		mUrl = url;

		mParamList.add(OAuthConstants.CONSUMER_KEY, APP_KEY);
		mParamList.add(OAuthConstants.SIGN_METHOD, OAuthConstants.HMAC_SHA1);
		mParamList.add(OAuthConstants.TIMESTAMP, Timestamp.getStamp());
		mParamList.add(OAuthConstants.NONCE, Timestamp.getNonce());
		mParamList.add(OAuthConstants.VERSION, "1.0");
		mParamList.add(OAuthConstants.CALLBACK, "oob");

		if (!TextUtils.isEmpty(token)) {
			mParamList.add(OAuthConstants.TOKEN, token);
		}
	}

	public void addParameter(String key, String value) {
		mParamList.add(key, value);
	}

	public void doSign() {
		mParamList = mParamList.sort();

		StringBuilder sb = new StringBuilder();
		sb.append(mVerb);
		sb.append("&").append(SafeEncoder.encode(mUrl));
		sb.append("&").append(
				SafeEncoder.encode(mParamList.asFormUrlEncodedString()));

		String baseString = sb.toString();

		log.d("base string : " + baseString);

		String secretKey = APP_SECRET + "&" + mTokenSecret;
		String signature = HMACSHA1.HmacSHA1EncryptText(baseString, secretKey);

		if (Logger.DBG) {
			log.d("signature : " + signature);
		}

		mParamList.add(OAuthConstants.SIGNATURE, signature);
	}

	public String getAuthHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("OAuth realm=\"\"");

		List<Parameter> paramList = mParamList.getParamList();
		for (Parameter param : paramList) {
			String key = param.getKey();
			if (key.startsWith(OAuthConstants.PARAM_PREFIX)) {
				sb.append(",");
				sb.append(param.asUrlEncodedPair());
			}
		}
		return sb.toString();
	}

	public String getRequestData() {
		StringBuilder sb = new StringBuilder();

		List<Parameter> paramList = mParamList.getParamList();
		for (Parameter param : paramList) {
			String key = param.getKey();
			if (!key.startsWith(OAuthConstants.PARAM_PREFIX)) {
				sb.append("&");
				sb.append(param.asUrlEncodedPair());
			}
		}
		String data = sb.toString();
		if (!TextUtils.isEmpty(data)) {
			return data.substring(1);
		}
		return "";
	}

}
