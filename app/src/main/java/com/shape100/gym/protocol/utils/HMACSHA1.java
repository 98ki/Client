package com.shape100.gym.protocol.utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class HMACSHA1 {

	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";

	public static String HmacSHA1EncryptText(String encryptText, String encryptKey) {
		String signature = "";
		try {
			SecretKey secretKey = new SecretKeySpec(encryptKey.getBytes(ENCODING), MAC_NAME);
			Mac mac = Mac.getInstance(MAC_NAME);
			mac.init(secretKey);
			byte[] result = mac.doFinal(encryptText.getBytes(ENCODING));
			signature = Base64.encodeToString(result, Base64.NO_WRAP);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}
}