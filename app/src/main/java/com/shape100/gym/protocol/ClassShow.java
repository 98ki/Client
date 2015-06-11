package com.shape100.gym.protocol;

import java.io.InputStream;

import android.os.Message;

import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.model.ClassBean;
import com.shape100.gym.model.CourseBean;

/**
 * 
 * CourseShow protocol
 * 
 * @author zpy zpy@98ki.com
 * @data 2014-10-30 下午5:44:10
 * @version: V1.1
 */
public class ClassShow extends HttpTask {
	private static final Logger log = Logger.getLogger("CourseShow");
	private static final String URL = "/class/show.json";
	
	/**
	 * In the system design, courseBean.getId is only used in club schedule activity to unique course. 
	 * 
	 * courseBean.getId is not in schedule/club.json, So I pass ID here to make a right connect.
	 * It's primary key, ExtProtocolUtil.parseCourse(input) use ID update TABLE course. 
	 */
	private CourseBean mCourseBean;
	/**
	 * 
	 * New Instance: CourseShow.
	 * 
	 * @param handler
	 * @param courseBean.id
	 * @param courseBean.classId
	 * @param courseBean.coachId
	 * 
	 * @remark show.json need classId and coachId.
	 *         courseBean.ID is helper when update Database.
	 */
	public ClassShow(ProtocolHandler handler, CourseBean bean) {
		super(buildUrl(bean.getClassId(), bean.getCoachId()), null, handler, false);
		log.d("schedule id:"+bean.getId()+", classId:"+bean.getClassId()+","+"coachId:" + bean.getCoachId());
		mCourseBean = bean;
	}

	private static String buildUrl(int classId, int coachId) {
		StringBuilder sb = new StringBuilder();
		sb.append(URL).append("?");
		sb.append("class_id=").append(classId).append("&coach_id=").append(coachId);
		return sb.toString();
	}

	@Override
	protected void httpRspHandler(int statusCode, InputStream input) throws Exception {
		if (Logger.DBG) {
			log.d("httpRspHandler in CourseShow");
		}
		if (statusCode != HttpTask.SC_OK) {
			reportFailure(statusCode);
			// get CourseShow from DB
		}else {
			// parse class/show.json, save bean to course 
			ClassBean classBean = new ClassBean();
			classBean.setClassId(mCourseBean.getClassId());
			classBean.setClassName(mCourseBean.getClassName());
			//[BUG] fuck the favorite course don't have club id.
			classBean.setClubId(mCourseBean.getClubId());
			ExtProtocolUtil.parseClass(classBean, input);
			reportSuccess();

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

	/**
	 * 
	 * report HANDLER_MSG_SUCCESS
	 * 
	 * @throw
	 * @return void
	 */
	private void reportSuccess() {
		mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_SUCCESS);
	}

	@Override
	protected void doError() {
		super.doError();
		if (mHandler != null) {
			mHandler.sendEmptyMessage(ConstVar.HANDLER_MSG_ERROR);
		}
	}

}
