/**    
 * file name：ClubJoin.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-17 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.protocol;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Message;

import com._98ki.util.FileUtils;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.model.ClubBean;
import com.shape100.gym.protocol.oauth.OAuth;
import com.shape100.gym.protocol.oauth.OAuthConstants;
import com.shape100.gym.provider.AccountUtil;
import com.shape100.gym.provider.AccountUtil.AccountBean;
import com.shape100.gym.provider.ClubUtil;

/**    
 *     
 * project：shape100    
 * class：ClubJoin    
 * desc：    
 * author：zpy zpy@98ki.com    
 * create date：2014-11-17 下午3:58:20    
 * modify author: zpy    
 * update date：2014-11-17 下午3:58:20    
 * update remark：    
 * @version 
 *     
 */
public class ClubJoin extends HttpTask {
	private static final Logger log = Logger.getLogger("ClubJoin");
	private static final String URL = "/place/club/join.json";
	private ClubBean mClubBean;
	/**
	 * 
	 * New Instance： ClubJoin.    
	 *    
	 * @param handler
	 * @param ClubBean
	 *          
	 */
	public ClubJoin(ProtocolHandler handler, ClubBean clubBean) {
		super(URL, null, handler,true);
		buildRequestData(clubBean);
		mClubBean = clubBean;
	}

	private void buildRequestData(ClubBean clubBean) {
		String token = "";
		String tokenSecret = "";
		AccountBean bean = AccountUtil.getAccount();
		if (bean != null) {
			token = bean.mToken;
			tokenSecret = bean.mTokenSecret;
		}
		String url = "http://" + HOST + URL ;
		OAuth auth = new OAuth(token, tokenSecret, OAuthConstants.VERB_POST, url);
		auth.addParameter("club_id",clubBean.getId()+"" );
		auth.doSign();
		mOAuthHeader = auth.getAuthHeader(); 

		StringEntity se;
		try {
			String data = auth.getRequestData();
			log.d( "post data : " + data);
			se = new StringEntity(data);
			setEntity(se);		
		} catch (UnsupportedEncodingException e) {	
			e.printStackTrace();
		}
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input) throws Exception {
		log.d("httpRspHandler start!");
        
		if (statusCode != HttpTask.SC_OK) {
			reportFailure(statusCode);
		//	log.e("--->clubjoin:--->"+FileUtils.getError(input));
		} else {
			// save to club table
			ClubUtil.saveClub(mClubBean);
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
	protected void doError() {
		super.doError();

		if (mHandler != null) {
			mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_ERROR);
		}
	}
}