package com.shape100.gym;

public class Event {
	public static final int ERROR = 100;

	public static final int UPDATEWB = 110;
	public static final int PICUPLOADCONFIRM = 111;
	public static final int ADDORREMOVE = 112;
	public static final int VERIFYCREDENTAIL = 113;
	public static final int REGNOTIFICATION = 114;
	public static final int DELETEDYNAMIC = 115;
	public static final int DELETEDYNAMIC_FAILED = 116;
	public static final int CREATECOMMENTS = 117; // 评论成功
	public static final int CREATECOMMENTS_FAILED = 118;// 评论失败
	public static final int COMMENTSSHOW = 119;
	public static final int COMMENTSSHOW_FAILED = 120;
	public static final int PRAISE = 121;
	public static final int PRAISE_ADD_FAILED = 122;
	public static final int PRAISE_CANCEL_FAILED = 123;
	public static final int CONCERN_LIST = 124;
	public static final int CONCERN_LIST_FAILED = 125;
	public static final int CREAT_CONCERN = 126;
	public static final int CANCEL_CONCERN = 127;
	public static final int CONCERN_FAILED = 128;
	public static final int USERINFO = 129;

	// 需要修改动态的事件
	public static final int COMMENT = 200;
	public static final int CHANGESETING = 201;
}
