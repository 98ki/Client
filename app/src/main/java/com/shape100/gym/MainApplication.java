package com.shape100.gym;

import java.util.Stack;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com._98ki.util.FileUtils;
import com.avos.avoscloud.AVOSCloud;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.shape100.gym.config.AppConfig;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.entity.UMessage;

public class MainApplication extends Application {

	public static Context sContext;
	public static Stack<Activity> activityActivity;
	public static final String APP_KEY_WB = "879555002";
	private PushAgent pushAgent;

	// wx/
	public static final String APP_ID_WX = "wx159ec7bb04bcf95c";
	public static final String APP_SECREAT_WX = "1a2e22d1e40e2e66ebe359a1c22d7c22";
	public static MainApplication shape;

	// wx shape
	// public static final String APP_ID = "wx159ec7bb04bcf95c";
	public static MainApplication getInstance() {
		return shape;
	}

	@Override
	public void onCreate() {
		sContext = this;
		shape = this;
		activityActivity = new Stack();
		super.onCreate();
		initNotification();
		initApplication();
		initImageLoader(getApplicationContext());

		initAVOS();
	}
public void initAVOS(){
	// U need your AVOS key and so on to run the code.
	AVOSCloud.initialize(this,
			"gq24t5pkj7bf5uhof7o9dqt2dl6eax582piaycchxit5i37e",
			"kbt5a4ni8hwy9j806w0wrx9s343b59hfe0tje0k38v5glq34");

}
	public void initNotification() {
		pushAgent = PushAgent.getInstance(this);
		startNotification();
	}

	public void startNotification() {
		pushAgent.enable();
		pushAgent.onAppStart(); // 统计启动该数据
		pushAgent.setDebugMode(true);
		String device_token = UmengRegistrar.getRegistrationId(this); // 获取友盟设备
		AppConfig.getInstance().setDeviceToken(device_token); //

		FeedbackPush.getInstance(this).init(false);// 开启用户反馈

		/**
		 * 该Handler是在IntentService中被调用，故 1.
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK 2.
		 * IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
		 * 或者可以直接启动Service
		 * */
		UmengMessageHandler handler = new UmengMessageHandler() {

			@Override
			public void dealWithCustomMessage(Context context, UMessage msg) {
				super.dealWithCustomMessage(context, msg);
			}

			@Override
			public Notification getNotification(Context context, UMessage msg) {
				switch (msg.builder_id) {
				case 1:
					NotificationCompat.Builder builder = new NotificationCompat.Builder(
							sContext);
					RemoteViews remoteViews = new RemoteViews(getPackageName(),
							R.layout.notification_view);
					remoteViews.setTextViewText(R.id.notification_title,
							msg.title);
					remoteViews.setTextViewText(R.id.notification_text,
							msg.text);
					remoteViews.setImageViewBitmap(
							R.id.notification_large_icon,
							getLargeIcon(context, msg));
					remoteViews.setImageViewResource(
							R.id.notification_small_icon,
							getSmallIconId(context, msg));
					builder.setContent(remoteViews);
					builder.setAutoCancel(true);
					Notification mNotification = builder.build();
					// 由于Android
					// v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
					mNotification.contentView = remoteViews;
					return mNotification;
				default:
					return super.getNotification(context, msg);
				}
			}
		};
		pushAgent.setNotificationClickHandler(handler);

		/**
		 * 该Handler是在BroadcastReceiver中被调用，故
		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
		 * */
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				// Toast.makeText(context, msg.custom,
				// Toast.LENGTH_LONG).show();
			}
		};
		pushAgent.setNotificationClickHandler(notificationClickHandler);
	}

	public void closeNotification() {
		if (pushAgent.isEnabled()) {
			pushAgent.disable();
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @author yupu
	 * @date 2015年2月10日
	 */
	public void initApplication() {
		if (!FileUtils.isFileExist("shape100")) {
			FileUtils.createFile("shape100");
		}

		if (!FileUtils.isFileExist("shape100/imgcache")) {
			FileUtils.createFile("shape100/imgcache");
		}

		if (!FileUtils.isFileExist("shape100/shape100")) {
			FileUtils.createFile("shape100/shape100");
		}
		FileUtils.createCacheFile(getImageCacheDir());
		FileUtils.createCacheFile(getJsonCacheDir());
	}

	public void initImageLoader(Context context) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				// .resetViewBeforeLoading(true)//
				.cacheOnDisk(true)//
				.cacheInMemory(true)//
				.cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)//
				.bitmapConfig(Bitmap.Config.RGB_565)//
				.considerExifParams(true)//
				.build();
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.defaultDisplayImageOptions(options) //
				.memoryCache(new WeakMemoryCache()).build();
		// Scheme.FILE.wrap(""); //包装一下加载不同路径的图片

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public DisplayImageOptions getDisplayImageOptions(int defaultImage) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisk(true)//
				.cacheInMemory(true)//
				.showImageOnLoading(defaultImage)//
				.showImageForEmptyUri(defaultImage)//
				.showImageOnFail(defaultImage)//
				.imageScaleType(ImageScaleType.EXACTLY)//
				.bitmapConfig(Bitmap.Config.RGB_565)//
				.considerExifParams(true)//
				.build();
		return options;
	}

	/**
	 * 获取缓存路径
	 * 
	 * @author yupu
	 * @date 2015年3月25日
	 */
	public String getCacheAttDir() {
		return getExternalCacheDir().getPath() + "/att/";
	}

	public String getJsonCacheDir() {
		return getCacheAttDir() + "json/";
	}

	public String getImageCacheDir() {
		return getCacheAttDir() + "image/";
	}
}
