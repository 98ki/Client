package com.shape100.gym.activity;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com._98ki.util.FileUtils;
import com._98ki.util.ImageUtils;
import com._98ki.util.LocalImgUtils;
import com._98ki.util.NetUtiles;
import com.shape100.gym.Logger;
import com.shape100.gym.R;
import com.shape100.gym.adapter.GrideImgBlogAdapter;
import com.shape100.gym.adapter.GrideImgBlogAdapter.BlogClosePhotoChoiceListener;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.config.QueueData;
import com.shape100.gym.model.BlogData;
import com.shape100.gym.model.PicInfo;
import com.shape100.gym.protocol.PicUpload;
import com.shape100.gym.protocol.ThreadPool;
import com.shape100.gym.protocol.UpdateWb;
import com.shape100.widget.GrideforListview;

public class WriteBlogActivity extends SlideActivity implements
		View.OnClickListener, BlogClosePhotoChoiceListener {
	private ImageView imageview;
	private EditText et;
	private Logger log = Logger.getLogger("WriteBlogActivity.class");
	private ArrayList<String> selectPics;
	private ArrayList<PicInfo> picInfos;
	private GrideImgBlogAdapter adapter;
	private GrideforListview grideview;
	private int flag, check = 0;
	private TextView isPublic;

	private Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 10086) {
				adapter.upDate(selectPics);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_writeblog);
		initView();
	}

	private void initView() {
		findViewById(R.id.writeblog_cancel).setOnClickListener(this);
		findViewById(R.id.writeblog_ok).setOnClickListener(this);
		findViewById(R.id.layout_bottom).setOnClickListener(this);
		et = (EditText) findViewById(R.id.et_write_wb);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				AppConfig.getInstance().setSaveWb(s.toString());
			}
		});

		et.setText(AppConfig.getInstance().getSaveWb());

		isPublic = (TextView) findViewById(R.id.tv_ispublic_pra);
		setShare();
		imageview = (ImageView) findViewById(R.id.iv_select_multiple_img);
		imageview.setOnClickListener(this);
		grideview = (GrideforListview) findViewById(R.id.gride_write_blog_pic);
		picInfos = new ArrayList<PicInfo>();
		selectPics = new ArrayList<String>();
		adapter = new GrideImgBlogAdapter(this);
		grideview.setAdapter(adapter);
		grideview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == selectPics.size()) {
					MorePicActivity.ActionStart(WriteBlogActivity.this,
							selectPics.size());
				} else {
					SwitcherPhotoActivity.ActionStart(WriteBlogActivity.this,
							selectPics, position, true);
				}
			}
		});

		ThreadPool.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				LocalImgUtils.getInstence(); // 在这里提前查询本地图片
				for (int i = 0; i < LocalImgUtils.getInstence().getAllImgs()
						.size(); i++) {
					File file = new File(LocalImgUtils.getInstence()
							.getAllImgs().get(i));
					if (!file.exists()) {
						LocalImgUtils.getInstence().getAllImgs().remove(i);
					}
				}
			}
		});

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// LocalImgUtils.getInstence(); // 在这里提前查询本地图片
		// }
		// }).start();

	}

	@Override
	protected void onActivityResult(int requestcode, int resultcode,
			final Intent data) {
		switch (requestcode) {
		case MorePicActivity.CODE:
			// selectPic();
			if (data != null) {
				ThreadPool.getInstance().execute(new Runnable() {

					@Override
					public void run() {
						initGride(data
								.getStringArrayListExtra(MorePicActivity.key_code));
					}
				});
			}
			break;
		case BlogShareActivity.REQUESTCODE:
			if (data != null) {
				check = data.getIntExtra("share", 0);
				setShare();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 设置选中的图片显示
	 * 
	 * @author yupu
	 * @date 2015年2月5日
	 */
	private void initGride(ArrayList<String> pics) {
		if (pics != null && pics.size() != 0) {
			for (int i = 0; i < pics.size(); i++) {
				log.d("----------原始路径------" + pics.get(i));
				PicInfo picInfo = new PicInfo();
				if (new File(pics.get(i)).length() < 90 * 1000) { // 原图小于90KB就不压缩
					picInfo.setLocal_url(pics.get(i));
					selectPics.add(pics.get(i));
				} else {
					String[] names = pics.get(i).split("/");
					log.d("---------WriteBlog-压缩之前count-----"
							+ new File(pics.get(i)).length());
					Bitmap bit = ImageUtils.compressImageFromFile(pics.get(i));
					File file = ImageUtils.bitmapToFile(bit,
							FileUtils.getImagePath() + names[names.length - 1]);

					log.d("--------------WriteBlog-压缩之后count-----"
							+ file.length() + "------" + file.getAbsolutePath());

					picInfo.setLocal_url(FileUtils.getImagePath()
							+ names[names.length - 1]);

					selectPics.add(file.getAbsolutePath());
				}
				picInfos.add(picInfo);
			}
			mhandler.sendEmptyMessage(10086);
		}
	}

	private void setShare() {
		switch (check) {
		case 0:
			isPublic.setText("所有人可见");
			break;
		case 2:
			isPublic.setText("好友圈可见");
			break;
		case 1:
			isPublic.setText("仅自己可见");
			break;
		default:
			break;
		}
	}

	@Override
	public void choice(int position) {
		// remove 选中后要移除的图片
		selectPics.remove(position);
		picInfos.remove(position);
		adapter.upDate(selectPics);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.writeblog_cancel:
			finish();
			break;
		case R.id.writeblog_ok:
			allDataBlog();
			flag = 1;
			finish();
			break;
		case R.id.layout_bottom:
			BlogShareActivity.ActionStart(this, check);
			break;
		default:
			break;
		}
	}

	private void allDataBlog() {
		BlogData blogData = new BlogData();
		if (et.getText() != null) {
			blogData.setStatus(et.getText().toString());
		} else {
			blogData.setStatus("");
		}

		// blogData.setIn_reply_to_status_id(in_reply_to_status_id);
		blogData.setVisible(check);
		blogData.setRip(NetUtiles.getIp());
		blogData.setLat(Float.valueOf(AppConfig.getInstance().getCurrLat()));
		blogData.setLon(Float.valueOf(AppConfig.getInstance().getCurrLon()));
		blogData.setPic_ids(new ArrayList<String>());
		blogData.setUpLoaded(picInfos);

		if (picInfos.size() != 0 && flag != 1) {
			blogData.setFirstPosition(new int[picInfos.size()]);
			blogData.setSecondPosition(new int[picInfos.size()]);
			blogData.setThirdPosition(new int[picInfos.size()]);

			QueueData.getinstence().clear(); // 清空队列
			ThreadPool.getInstance().execute(new PicUpload(blogData, 0));
			// new PicUpload(blogData, 0).start();

			// 测试上传时间
			AppConfig.getInstance().setTime(System.currentTimeMillis());
		} else {
			ThreadPool.getInstance().execute(
					new UpdateWb(blogData.getStatus(), blogData.getVisible(),
							null, null, blogData.getLat(), blogData.getLon(),
							blogData.getRip()));
			// new UpdateWb(blogData.getStatus(), blogData.getVisible(), null,
			// null, blogData.getLat(), blogData.getLon(),
			// blogData.getRip()).start();
		}
	}

	// http://img01.shape100.com/sns/large/1000000307869dbf486f3b2b06.jpg
	// http://img01.shape100.com/sns/large/10000003075ab3ac5bb8f946e7.jpg
	// http://img01.shape100.com/sns/large/10000003071cb96d068f47f35a.jpg
	// http://img01.shape100.com/sns/large/10000003072612506637f86973.jpg
	// http://img01.shape100.com/sns/large/1000000307cd3d1dd5287e9c52.jpg
	// http://img01.shape100.com/sns/large/1000000307ba09e6d6ce487c88.jpg
	// http://img01.shape100.com/sns/large/10000003079dfa478501a32295.jpg
	// http://img01.shape100.com/sns/large/100000030723014412bde834c1.jpg
	// http://img01.shape100.com/sns/large/1000000307778c12a7d99ebf30.jpg

	// public class EventHandler extends ProtocolHandler {
	// @Override
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case Event.PICUPLOADCONFIRM:
	// Toast.makeText(WriteBlogActivity.this, "上传成功",
	// Toast.LENGTH_LONG).show();
	// // pic_ids.add(((PicInfo) msg.obj).getPic_id());
	// break;
	// case Event.UPDATEWB:
	// finish();
	// break;
	// case ConstVar.HANDLER_MSG_FAILURE:
	// log.d("-------------------------上传失败");
	// break;
	// default:
	// break;
	// }
	// }
	// }
}
