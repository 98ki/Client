package com.shape100.gym.config;

/**
 * 设置程序配置信息
 * 
 * @author yupu
 * @date 2014年12月31日
 */
public class AppConfig extends Config {
	private static AppConfig instance;
	private static String KEY_FIRST_APP = "first_app"; // 第一次启动app
	private static String KEY_FIRST_CLUB = "first_club"; // 第一次打开俱乐部界面
	private static String KEY_FIRST_MAIN = "first_main"; // 第一次打开主界面
	private static String KEY_USER_ID = "userid"; // 本地用户id
	private static String KEY_LAT = "lat"; // 当前的纬度
	private static String KEY_LON = "lon"; // 当前的经度
	private static String MOBILE = "mobile"; // 用户手机号码
	private static String SAVE_WB = "save"; // 保存微博不成功的内容
	private static String USERNAME = "username"; // 用户名
	private static String PASSWORD = "passname"; // 密码

	public AppConfig() {
		super("config");
	}

	public static synchronized AppConfig getInstance() {
		if (instance == null) {
			instance = new AppConfig();
		}
		return instance;
	}

	public void setUserId(int userid) {
		commitInt(KEY_USER_ID, userid);
	}

	public int getUserId() {
		return getInt(KEY_USER_ID, 0);
	}

	public boolean isFirstApp() {
		return getBoolean(KEY_FIRST_APP, true);
	}

	public void setFirstApp(boolean flag) {
		commitBoolean(KEY_FIRST_APP, flag);
	}

	public int isFirstClub() {
		return getInt(KEY_FIRST_CLUB, 0);
	}

	public void setFirstClub(int flag) {
		commitInt(KEY_FIRST_CLUB, flag);
	}

	public int isFirstMain() {
		return getInt(KEY_FIRST_MAIN, 0);
	}

	public void setFirstMain(int flag) {
		commitInt(KEY_FIRST_MAIN, flag);
	}

	public void setCurrLon(String lon) {
		commitString(KEY_LON, lon);
	}

	public String getCurrLon() {
		return getString(KEY_LON, "0");
	}

	public void setCurrLat(String lat) {
		commitString(KEY_LAT, lat);
	}

	public String getCurrLat() {
		return getString(KEY_LAT, "0");
	}

	public void setTime(long time) {
		commitLong("time", time);
	}

	public long getTime() {
		return getLong("time", 0);
	}

	public void setDeviceToken(String token) {
		commitString("deviceToken", token);
	}

	public String getDeviceToken() {
		return getString("deviceToken", "null");
	}

	public String getMobile() {
		return getString(MOBILE, "用户不让抓");
	}

	public void setMobile(String numble) {
		commitString(MOBILE, numble);
	}

	public String getSaveWb() {
		return getString(SAVE_WB, "");
	}

	public void setSaveWb(String content) {
		commitString(SAVE_WB, content);
	}

	public void setUserName(String name) {
		commitString(USERNAME, name);
	}

	public String getUserName() {
		return getString(USERNAME, "");
	}

	public void setPassWord(String pass) {
		commitString(PASSWORD, pass);
	}

	public String getPassWord() {
		return getString(PASSWORD, "");
	}
}
