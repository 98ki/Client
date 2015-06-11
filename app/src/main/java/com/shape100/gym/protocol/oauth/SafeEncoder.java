package com.shape100.gym.protocol.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SafeEncoder {
	private static String CHARSET = "UTF-8";

	public static String encode(String plain) {

		String encoded = "";
		try {
			encoded = URLEncoder.encode(plain, CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encoded;
	}
}
