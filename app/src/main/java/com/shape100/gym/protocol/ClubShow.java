/**    
 * file name：ClubShow.java    
 * 
 * @author zpy zpy@98ki.com   
 * @date：2014-11-20 
 * Copyright shape100.com Corporation 2014         
 *    
 */
package com.shape100.gym.protocol;

import java.io.InputStream;

import android.os.Message;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.model.ClubBean;
import com.shape100.gym.provider.ClubUtil;

/**    
 *     
 * project：shape100    
 * class：ClubShow    
 * desc：    
 * author：zpy zpy@98ki.com    
 * create date：2014-11-20 下午6:42:49    
 * modify author: zpy    
 * update date：2014-11-20 下午6:42:49    
 * update remark：    
 * @version     
 *     
 */
public class ClubShow extends HttpTask {
	private static final Logger log = Logger.getLogger("ClubShow");
	private static final String URL = "/place/club/show.json";

	private ClubBean clubBean;

	public ClubShow(ProtocolHandler handler, int clubId) {
		super(buildUrl(clubId), null, handler, false);
	}

	private static String buildUrl(int clubId) {
		log.d("ClubShow buildUrl start");
		StringBuilder sb = new StringBuilder();
		sb.append(URL).append("?");
		sb.append("id=").append(clubId);
		return sb.toString();
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input) throws Exception {
		log.d("httpRspHandler in ClubShow");
		if (statusCode == 200) {
			ClubBean bean = ExtProtocolUtil.parseClub(input).get(0);
			log.e("Club list parsed!bean.id():" + bean.getId());
			ClubUtil.saveClub(bean);
			reportSuccess();
		} else {
			reportFailure(statusCode);
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

}
