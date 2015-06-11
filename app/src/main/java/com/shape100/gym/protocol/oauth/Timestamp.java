package com.shape100.gym.protocol.oauth;

import java.util.Random;

public class Timestamp {
	private static final Random sRand = new Random();
	
	public static String getStamp(){		
		return String.valueOf(System.currentTimeMillis()/1000);
	}
	
	public static String getNonce(){
		return String.valueOf(System.currentTimeMillis()/1000 + sRand.nextInt());
	}
}
