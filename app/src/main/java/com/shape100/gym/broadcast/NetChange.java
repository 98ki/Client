package com.shape100.gym.broadcast;

import com._98ki.util.NetUtiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;

/**
 * 网络变化的广播接收器
 * 
 * @author yupu
 * @date 2015年3月27日
 */

public class NetChange extends BroadcastReceiver {

	public static final String CONNECTTIVITY_CHANGE_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;
	private NetChangListener listener;

	public NetChange(NetChangListener listener) {
		this.listener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (TextUtils.equals(action, CONNECTTIVITY_CHANGE_ACTION)) {

			// 网络变化的广播
			if (NetUtiles.isConnected()) {
				listener.connnet();
			} else {
				listener.notconnet();
			}
		}
	}

	public interface NetChangListener {
		/**
		 * 连上网
		 * 
		 * @author yupu
		 * @date 2015年3月27日
		 */
		void connnet();

		/**
		 * 断网
		 * 
		 * @author yupu
		 * @date 2015年3月27日
		 */
		void notconnet();
	}
}
