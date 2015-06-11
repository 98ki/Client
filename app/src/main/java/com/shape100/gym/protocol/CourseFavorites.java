/**    
 * file name：Favorites.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-14 
 * @version:    
 * Copyright zpy@98ki.com Corporation 2014         
 *    
 */
package com.shape100.gym.protocol;

import java.io.InputStream;

import android.os.Message;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;

/**    
 *     
 * project：shape100    
 * class：Favorites    
 * desc：    
 * author：zpy zpy@98ki.com    
 * create date：2014-11-14 下午7:00:32    
 * modify author: zpy    
 * update date：2014-11-14 下午7:00:32    
 * update remark：    
 * @version     
 *     
 */
public class CourseFavorites extends HttpTask {
		private static final Logger log = Logger.getLogger("Favorites");
		private static final String URL = "/schedule/favorites.json";

		
		
		public CourseFavorites(ProtocolHandler handler,String startDate, String endDate) {
			super(URL+"?start_date="+startDate+"&end_date="+endDate, null, handler, false);
			buildRequestData(startDate, endDate);
		}

		private void buildRequestData(String startDate, String endDate) {
			String token = "";
			String tokenSecret = "";
			AccountBean bean = AccountUtil.getAccount();
			if (bean != null) {
				token = bean.mToken;
				tokenSecret = bean.mTokenSecret;
			}
			String url = "http://" + HOST + URL;
			OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_GET, url);
			auth.addParameter("start_date", startDate);
			auth.addParameter("end_date", endDate);
			auth.doSign();
			mOAuthHeader = auth.getAuthHeader(); 
		}

		@Override
		protected void httpRspHandler(int statusCode, InputStream input) throws Exception {
			log.d("httpRspHandler start!");
			//[update native database] clear all favorite, 
			//[BUG] now clear all favorite. if support many account login and native cache, we should clear by user_id in favorite table, now favorite table don't have user_id column.
			//CourseFavoriteUtil.clearCourse();
			//save to Favorite table
			ExtProtocolUtil.parseFavoriteSchedule(input);	
			
			if (statusCode != HttpTask.SC_OK) {
				reportFailure(statusCode);
			} else {
				reportSuccess();
			}
		}

		private void reportSuccess() {
			if (mHandler != null) {
				mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_SUCCESS);
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
		protected void doError(){
			super.doError();

			if (mHandler != null) {
				mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_ERROR);
			}
		}
	}
