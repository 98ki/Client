package com.shape100.gym.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com._98ki.util.FileUtils;
import com.shape100.gym.MainApplication;
import com.shape100.gym.MainName;
import com.shape100.gym.R;
import com.tencent.weibo.sdk.android.component.sso.tools.MD5Tools;

public class SelectPicPopupWindow extends BaseActivity implements
		OnClickListener {
	public static final int RESULTAKE = 11;
	public static final int RESULTPIC = 12;
	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private LinearLayout layout;
	private Intent intent;
	private String cur_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pop_window_pic);
		intent = getIntent();
		btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
		btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
		btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

		layout = (LinearLayout) findViewById(R.id.pop_layout);

		layout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
			}
		});
		btn_cancel.setOnClickListener(this);
		btn_pick_photo.setOnClickListener(this);
		btn_take_photo.setOnClickListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == 1) {
			String path = Environment.getExternalStorageDirectory()
					+ File.separator + Environment.DIRECTORY_DCIM + "/";
			try {

				MediaStore.Images.Media.insertImage(getContentResolver(), path
						+ cur_name, cur_name, null);

				// 通知更新
				getApplicationContext().sendBroadcast(
						new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
								.parse("file://" + path + cur_name)));

				// 另存为到一个目录
				FileUtils.writeFile(MainApplication.getInstance()
						.getImageCacheDir() + MainName.HEAD_LARGE_JPG,
						FileUtils.readFile(new File(path + cur_name)), false);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			setResult(RESULTAKE);
		}

		if (requestCode == 2) {
			if (data.getExtras() != null)
				intent.putExtras(data.getExtras());
			if (data.getData() != null)
				intent.setData(data.getData());
			setResult(RESULTPIC, intent);
		}
		finish();
	}

	/**
	 * 调用相机获取原图
	 * 
	 * @author yupu
	 * @date 2015年3月26日
	 */
	private void takePhoto() {
		try {
			if (FileUtils.isSDcardExist()) {
				cur_name = sdf.format(new Date(System.currentTimeMillis()))
						+ ".jpg";
				String path = Environment.getExternalStorageDirectory()
						+ File.separator + Environment.DIRECTORY_DCIM + "/"
						+ cur_name;
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(path);
				Uri uri = Uri.fromFile(file);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 1);
			}
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(SelectPicPopupWindow.this, "调用相机失败",
					Toast.LENGTH_LONG).show();
		}
	}

	@SuppressLint("InlinedApi")
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_take_photo:
			takePhoto();
			break;
		case R.id.btn_pick_photo:
			try {
				if (Build.VERSION.SDK_INT < 19) {
					intent = new Intent();
					intent.setAction(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					startActivityForResult(intent, 2);
				} else {
					intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*");
					startActivityForResult(intent, 2);
				}
			} catch (ActivityNotFoundException e) {

			}
			break;
		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}

	}

}
